package ImplementazionePostgresDAO;

import Database.Database;
import dao.TirocinioDAO;
import model.Tirocinio;
import model.TirocinioEsterno;
import model.TirocinioInterno;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PostgresTirocinioDAO implements TirocinioDAO {

    private Database database;

    public PostgresTirocinioDAO() {
        database = new Database();
    }

    @Override
    public void SalvaTirocinio(Tirocinio tirocinio) {

        String sqlTirocinio = """
                INSERT INTO tirocinio
                (argomento, id_docente_responsabile, stato, tipo)
                VALUES (?, ?, ?::stato_tirocinio, ?::tipo_tirocinio)
                RETURNING id
                """;

        try (Connection connection = database.GetConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sqlTirocinio)) {

            statement.setString(1, tirocinio.getArgomento());
            statement.setInt(
                    2,
                    tirocinio.getDocenteResponsabile().getIdDocente()
            );
            statement.setString(3, tirocinio.getStato().name());

            if (tirocinio instanceof TirocinioEsterno) {
                statement.setString(4, "ESTERNO");
            } else {
                statement.setString(4, "INTERNO");
            }

            ResultSet resultSet = statement.executeQuery();
            resultSet.next();

            int idTirocinio = resultSet.getInt("id");

            if (tirocinio instanceof TirocinioEsterno) {

                TirocinioEsterno esterno =
                        (TirocinioEsterno) tirocinio;

                String sqlEsterno = """
                        INSERT INTO tirocinio_esterno
                        (id_tirocinio, nominativo_referente_aziendale)
                        VALUES (?, ?)
                        """;

                try (PreparedStatement statementEsterno =
                             connection.prepareStatement(sqlEsterno)) {

                    statementEsterno.setInt(1, idTirocinio);
                    statementEsterno.setString(
                            2,
                            esterno.getNominativoReferenteAziendale()
                    );

                    statementEsterno.executeUpdate();
                }

            } else if (tirocinio instanceof TirocinioInterno) {

                String sqlInterno = """
                        INSERT INTO tirocinio_interno(id_tirocinio)
                        VALUES (?)
                        """;

                try (PreparedStatement statementInterno =
                             connection.prepareStatement(sqlInterno)) {

                    statementInterno.setInt(1, idTirocinio);
                    statementInterno.executeUpdate();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Tirocinio> ListaTirocini() {

        return listaTirocini(false);
    }

    @Override
    public List<Tirocinio> ListaTirociniInCorso() {

        return listaTirocini(true);
    }

    private List<Tirocinio> listaTirocini(boolean soloInCorso) {

        List<Tirocinio> lista = new ArrayList<>();

        String sql = """
                SELECT t.id,
                       t.argomento,
                       t.id_docente_responsabile,
                       t.stato,
                       t.tipo,
                       te.nominativo_referente_aziendale
                FROM tirocinio t
                LEFT JOIN tirocinio_esterno te
                    ON t.id = te.id_tirocinio
                """;

        if (soloInCorso) {
            sql += " WHERE t.stato = 'IN_CORSO'::stato_tirocinio";
        }

        try (Connection connection = database.GetConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                DocenteStub:
                {
                    /*
                     * Per ora creiamo il docente con il solo id.
                     * Il Controller potrà recuperare i dati completi
                     * tramite DocenteDAO quando necessario.
                     */
                    model.Docente docente = new model.Docente();
                    docente.setIdDocente(
                            resultSet.getInt("id_docente_responsabile")
                    );

                    model.StatoTirocinio stato =
                            model.StatoTirocinio.valueOf(
                                    resultSet.getString("stato")
                            );

                    if ("ESTERNO".equals(resultSet.getString("tipo"))) {

                        lista.add(
                                new TirocinioEsterno(
                                        resultSet.getInt("id"),
                                        resultSet.getString("argomento"),
                                        docente,
                                        stato,
                                        resultSet.getString(
                                                "nominativo_referente_aziendale"
                                        )
                                )
                        );

                    } else {

                        lista.add(
                                new TirocinioInterno(
                                        resultSet.getInt("id"),
                                        resultSet.getString("argomento"),
                                        docente,
                                        stato
                                )
                        );
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
}
