package ImplementazionePostgresDAO;

import Database.Database;
import dao.TesiDAO;
import model.Docente;
import model.SedutaLaurea;
import model.StatoTesi;
import model.Studente;
import model.Tesi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PostgresTesiDAO implements TesiDAO {

    private final Database database;

    public PostgresTesiDAO() {
        database = new Database();
    }

    /*
     * Salva una tesi soltanto se lo studente possiede
     * una richiesta di tirocinio accettata.
     *
     * Il relatore viene ricavato dalla richiesta accettata
     * più recente: non viene scelto dallo studente.
     */
    @Override
    public void SalvaTesi(Tesi tesi) {

        if (tesi == null
                || tesi.getStudente() == null
                || tesi.getSeduta() == null) {

            throw new IllegalArgumentException(
                    "Dati della tesi incompleti."
            );
        }

        String matricola =
                tesi.getStudente().getMatricola();

        String sql = """
                INSERT INTO tesi
                (
                    stato,
                    file,
                    versione,
                    matricola_studente,
                    id_relatore,
                    id_seduta
                )
                SELECT
                    'IN_CORSO'::stato_tesi,
                    ?,
                    ?,
                    r.matricola_studente,
                    r.id_docente,
                    s.id
                FROM richiesta_tirocinio r
                JOIN seduta_laurea s
                    ON s.id = ?
                   AND s.disponibilita = TRUE
                WHERE r.matricola_studente = ?
                  AND r.stato = 'ACCETTATA'::stato_richiesta
                ORDER BY r.id DESC
                LIMIT 1
                """;

        try (Connection connection =
                     database.GetConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(
                    1,
                    tesi.getFile()
            );

            statement.setInt(
                    2,
                    tesi.getVersione()
            );

            statement.setInt(
                    3,
                    tesi.getSeduta().getId()
            );

            statement.setString(
                    4,
                    matricola
            );

            int righe = statement.executeUpdate();

            if (righe != 1) {

                if (!HaRichiestaTirocinioAccettata(matricola)) {
                    throw new IllegalStateException(
                            "Non puoi caricare la tesi: "
                                    + "non hai una richiesta di tirocinio accettata."
                    );
                }

                throw new IllegalStateException(
                        "La seduta selezionata non esiste "
                                + "oppure non è più disponibile."
                );
            }

        } catch (IllegalStateException e) {

            throw e;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Errore durante il salvataggio della tesi: "
                            + e.getMessage(),
                    e
            );
        }
    }

    /*
     * Lista generale. Le schermate operative devono usare
     * ListaTesiPerRelatore oppure ListaTesiValutate.
     */
    @Override
    public List<Tesi> ListaTesi() {

        return eseguiLista(null, null);
    }

    /*
     * Il docente vede soltanto le tesi IN_CORSO:
     * - assegnate a lui;
     * - il cui studente ha una richiesta accettata
     *   corrispondente al relatore della tesi.
     */
    @Override
    public List<Tesi> ListaTesiPerRelatore(int idDocente) {

        String filtro = """
                t.id_relatore = ?
                AND t.stato = 'IN_CORSO'::stato_tesi
                AND t.id_relatore = (
                    SELECT r.id_docente
                    FROM richiesta_tirocinio r
                    WHERE r.matricola_studente = t.matricola_studente
                      AND r.stato = 'ACCETTATA'::stato_richiesta
                    ORDER BY r.id DESC
                    LIMIT 1
                )
                """;

        return eseguiLista(
                filtro,
                idDocente
        );
    }

    /*
     * Il coordinatore può consultare le tesi già valutate,
     * ma non le tesi ancora in attesa di approvazione.
     */
    @Override
    public List<Tesi> ListaTesiValutate() {

        String filtro = """
                t.stato IN (
                    'ACCETTATA'::stato_tesi,
                    'RIFIUTATA'::stato_tesi
                )
                """;

        return eseguiLista(
                filtro,
                null
        );
    }

    private List<Tesi> eseguiLista(
            String filtro,
            Integer idDocente) {

        List<Tesi> lista = new ArrayList<>();

        String sql = """
                SELECT
                    t.id,
                    t.stato,
                    t.file,
                    t.versione,
                    t.matricola_studente,
                    t.id_relatore,
                    t.id_seduta
                FROM tesi t
                """;

        if (filtro != null) {
            sql += " WHERE " + filtro;
        }

        sql += " ORDER BY t.id";

        try (Connection connection =
                     database.GetConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            if (idDocente != null) {
                statement.setInt(
                        1,
                        idDocente
                );
            }

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {

                    Tesi tesi = new Tesi();

                    tesi.setId(
                            resultSet.getInt("id")
                    );

                    tesi.setStato(
                            StatoTesi.valueOf(
                                    resultSet.getString("stato")
                            )
                    );

                    tesi.setFile(
                            resultSet.getString("file")
                    );

                    tesi.setVersione(
                            resultSet.getInt("versione")
                    );

                    Studente studente = new Studente();

                    studente.setMatricola(
                            resultSet.getString(
                                    "matricola_studente"
                            )
                    );

                    Docente relatore = new Docente();

                    relatore.setIdDocente(
                            resultSet.getInt("id_relatore")
                    );

                    SedutaLaurea seduta =
                            new SedutaLaurea();

                    seduta.setId(
                            resultSet.getInt("id_seduta")
                    );

                    tesi.setStudente(studente);
                    tesi.setRelatore(relatore);
                    tesi.setSeduta(seduta);

                    lista.add(tesi);
                }
            }

        } catch (Exception e) {

            throw new RuntimeException(
                    "Errore nel caricamento delle tesi: "
                            + e.getMessage(),
                    e
            );
        }

        return lista;
    }

    /*
     * Controlla anche lato database che il docente che
     * effettua l'operazione sia il relatore autorizzato.
     *
     * È consentito aggiornare soltanto una tesi IN_CORSO.
     */
    @Override
    public void AggiornaStato(
            Tesi tesi,
            int idDocente) {

        if (tesi == null
                || tesi.getStato() == null) {

            throw new IllegalArgumentException(
                    "Dati della valutazione incompleti."
            );
        }

        if (tesi.getStato() != StatoTesi.ACCETTATA
                && tesi.getStato() != StatoTesi.RIFIUTATA) {

            throw new IllegalArgumentException(
                    "La valutazione deve essere ACCETTATA oppure RIFIUTATA."
            );
        }

        String sql = """
                UPDATE tesi t
                SET stato = CAST(? AS stato_tesi)
                WHERE t.id = ?
                  AND t.id_relatore = ?
                  AND t.stato = 'IN_CORSO'::stato_tesi
                  AND t.id_relatore = (
                      SELECT r.id_docente
                      FROM richiesta_tirocinio r
                      WHERE r.matricola_studente =
                            t.matricola_studente
                        AND r.stato =
                            'ACCETTATA'::stato_richiesta
                      ORDER BY r.id DESC
                      LIMIT 1
                  )
                """;

        try (Connection connection =
                     database.GetConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(
                    1,
                    tesi.getStato().name()
            );

            statement.setInt(
                    2,
                    tesi.getId()
            );

            statement.setInt(
                    3,
                    idDocente
            );

            int righe = statement.executeUpdate();

            if (righe != 1) {

                throw new IllegalStateException(
                        "Valutazione non consentita: "
                                + "la tesi non è assegnata a questo docente, "
                                + "non ha una richiesta accettata corrispondente "
                                + "oppure è già stata valutata."
                );
            }

        } catch (IllegalStateException e) {

            throw e;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Errore durante la valutazione della tesi: "
                            + e.getMessage(),
                    e
            );
        }
    }

    @Override
    public boolean HaRichiestaTirocinioAccettata(
            String matricola) {

        String sql = """
                SELECT EXISTS (
                    SELECT 1
                    FROM richiesta_tirocinio
                    WHERE matricola_studente = ?
                      AND stato = 'ACCETTATA'::stato_richiesta
                )
                """;

        try (Connection connection =
                     database.GetConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(
                    1,
                    matricola
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                return resultSet.next()
                        && resultSet.getBoolean(1);
            }

        } catch (Exception e) {

            throw new RuntimeException(
                    "Errore nel controllo della richiesta di tirocinio: "
                            + e.getMessage(),
                    e
            );
        }
    }

    @Override
    public boolean HaTesiAccettataPerSeduta(
            String matricola,
            int idSeduta) {

        String sql = """
                SELECT EXISTS (
                    SELECT 1
                    FROM tesi
                    WHERE matricola_studente = ?
                      AND id_seduta = ?
                      AND stato = 'ACCETTATA'::stato_tesi
                )
                """;

        try (Connection connection =
                     database.GetConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(
                    1,
                    matricola
            );

            statement.setInt(
                    2,
                    idSeduta
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                return resultSet.next()
                        && resultSet.getBoolean(1);
            }

        } catch (Exception e) {

            throw new RuntimeException(
                    "Errore nel controllo della tesi accettata: "
                            + e.getMessage(),
                    e
            );
        }
    }
}