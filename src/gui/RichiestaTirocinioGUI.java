package ImplementazionePostgresDAO;

import Database.Database;
import dao.RichiestaTirocinioDAO;
import model.Docente;
import model.RichiestaTirocinio;
import model.StatoRichiesta;
import model.Studente;
import model.Tirocinio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PostgresRichiestaTirocinioDAO
        implements RichiestaTirocinioDAO {

    private Database database;

    public PostgresRichiestaTirocinioDAO() {
        database = new Database();
    }

    @Override
    public void SalvaRichiesta(RichiestaTirocinio richiesta) {

        String sql = """
                INSERT INTO richiesta_tirocinio
                (stato, matricola_studente, id_tirocinio, id_docente)
                VALUES (?::stato_richiesta, ?, ?, ?)
                """;

        try (Connection connection = database.GetConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(
                    1,
                    richiesta.getStato().name()
            );

            statement.setString(
                    2,
                    richiesta.getStudente().getMatricola()
            );

            statement.setInt(
                    3,
                    richiesta.getTirocinio().getId()
            );

            statement.setInt(
                    4,
                    richiesta.getDocente().getIdDocente()
            );

            statement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<RichiestaTirocinio> ListaRichieste() {

        return eseguiListaRichieste(null);
    }

    @Override
    public List<RichiestaTirocinio> ListaRichiestePerStato(
            StatoRichiesta stato) {

        return eseguiListaRichieste(stato);
    }

    private List<RichiestaTirocinio> eseguiListaRichieste(
            StatoRichiesta stato) {

        List<RichiestaTirocinio> lista =
                new ArrayList<>();

        String sql = """
                SELECT id,
                       stato,
                       matricola_studente,
                       id_tirocinio,
                       id_docente
                FROM richiesta_tirocinio
                """;

        if (stato != null) {
            sql += " WHERE stato = ?::stato_richiesta";
        }

        sql += " ORDER BY id";

        try (Connection connection = database.GetConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            if (stato != null) {
                statement.setString(
                        1,
                        stato.name()
                );
            }

            ResultSet resultSet =
                    statement.executeQuery();

            while (resultSet.next()) {

                RichiestaTirocinio richiesta =
                        new RichiestaTirocinio();

                richiesta.setId(
                        resultSet.getInt("id")
                );

                richiesta.setStato(
                        StatoRichiesta.valueOf(
                                resultSet.getString("stato")
                        )
                );

                Studente studente =
                        new Studente();

                studente.setMatricola(
                        resultSet.getString(
                                "matricola_studente"
                        )
                );

                Tirocinio tirocinio =
                        new Tirocinio();

                tirocinio.setId(
                        resultSet.getInt(
                                "id_tirocinio"
                        )
                );

                Docente docente =
                        new Docente();

                docente.setIdDocente(
                        resultSet.getInt(
                                "id_docente"
                        )
                );

                richiesta.setStudente(studente);
                richiesta.setTirocinio(tirocinio);
                richiesta.setDocente(docente);

                lista.add(richiesta);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public void AggiornaStato(
            RichiestaTirocinio richiesta) {

        String sql = """
                UPDATE richiesta_tirocinio
                SET stato = ?::stato_richiesta
                WHERE id = ?
                """;

        try (Connection connection = database.GetConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(
                    1,
                    richiesta.getStato().name()
            );

            statement.setInt(
                    2,
                    richiesta.getId()
            );

            statement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}