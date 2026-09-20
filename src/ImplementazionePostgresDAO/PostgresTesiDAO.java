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

    private Database database;

    public PostgresTesiDAO() {
        database = new Database();
    }

    @Override
    public void SalvaTesi(Tesi tesi) {

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
            VALUES
            (
                CAST(? AS stato_tesi),
                ?,
                ?,
                ?,
                ?,
                ?
            )
            """;

        try (Connection connection = database.GetConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(
                    1,
                    tesi.getStato().name()
            );

            statement.setString(
                    2,
                    tesi.getFile()
            );

            statement.setInt(
                    3,
                    tesi.getVersione()
            );

            statement.setString(
                    4,
                    tesi.getStudente().getMatricola()
            );

            statement.setInt(
                    5,
                    tesi.getRelatore().getIdDocente()
            );

            statement.setInt(
                    6,
                    tesi.getSeduta().getId()
            );

            int righe = statement.executeUpdate();

            if (righe != 1) {

                throw new RuntimeException(
                        "Errore: la tesi non è stata salvata."
                );
            }

        } catch (Exception e) {

            throw new RuntimeException(
                    "Errore durante il salvataggio della tesi: "
                            + e.getMessage(),
                    e
            );
        }
    }

    @Override
    public List<Tesi> ListaTesi() {

        List<Tesi> lista = new ArrayList<>();

        String sql = """
                SELECT id,
                       stato,
                       file,
                       versione,
                       matricola_studente,
                       id_relatore,
                       id_seduta
                FROM tesi
                ORDER BY id
                """;

        try (Connection connection = database.GetConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
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

                Studente studente =
                        new Studente();

                studente.setMatricola(
                        resultSet.getString(
                                "matricola_studente"
                        )
                );

                Docente relatore =
                        new Docente();

                relatore.setIdDocente(
                        resultSet.getInt(
                                "id_relatore"
                        )
                );

                SedutaLaurea seduta =
                        new SedutaLaurea();

                seduta.setId(
                        resultSet.getInt(
                                "id_seduta"
                        )
                );

                tesi.setStudente(studente);
                tesi.setRelatore(relatore);
                tesi.setSeduta(seduta);

                lista.add(tesi);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public void AggiornaStato(Tesi tesi) {

        String sql = """
            UPDATE tesi
            SET stato = CAST(? AS stato_tesi)
            WHERE id = ?
            """;

        try (Connection connection = database.GetConnection();
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

            int righe = statement.executeUpdate();

            if (righe != 1) {
                throw new RuntimeException(
                        "Nessuna tesi trovata con id "
                                + tesi.getId()
                );
            }

        } catch (Exception e) {

            throw new RuntimeException(
                    "Errore durante l'aggiornamento dello stato della tesi: "
                            + e.getMessage(),
                    e
            );
        }
    }}