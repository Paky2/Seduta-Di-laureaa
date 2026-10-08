package Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseInitializer {

    private static final String DATABASE_NAME = "seduta_di_laurea";

    public static void initialize() throws Exception {

        Database database = new Database();

        // 1. Controlla/crea il database
        try (Connection connection = database.GetAdminConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT 1 FROM pg_database WHERE datname = ?"
             )) {

            statement.setString(1, DATABASE_NAME);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (!resultSet.next()) {

                    try (Statement createStatement =
                                 connection.createStatement()) {

                        createStatement.executeUpdate(
                                "CREATE DATABASE " + DATABASE_NAME
                        );
                    }
                }
            }
        }

        // 2. Crea la struttura del database
        try (Connection connection = database.GetConnection();
             Statement statement = connection.createStatement()) {

            creaEnum(statement);
            creaTabelle(statement);
            creaDatiIniziali(statement);
        }
    }

    private static void creaEnum(Statement statement)
            throws Exception {

        statement.execute("""
                DO $$
                BEGIN
                    IF NOT EXISTS (
                        SELECT 1 FROM pg_type
                        WHERE typname = 'stato_richiesta'
                    ) THEN
                        CREATE TYPE stato_richiesta AS ENUM (
                            'ACCETTATA',
                            'RIFIUTATA',
                            'PENDENTE'
                        );
                    END IF;
                END
                $$;
                """);

        statement.execute("""
                DO $$
                BEGIN
                    IF NOT EXISTS (
                        SELECT 1 FROM pg_type
                        WHERE typname = 'stato_tesi'
                    ) THEN
                        CREATE TYPE stato_tesi AS ENUM (
                            'IN_CORSO',
                            'FINITA',
                            'ACCETTATA',
                            'RIFIUTATA'
                        );
                    END IF;
                END
                $$;
                """);

        statement.execute("""
                DO $$
                BEGIN
                    IF NOT EXISTS (
                        SELECT 1 FROM pg_type
                        WHERE typname = 'stato_tirocinio'
                    ) THEN
                        CREATE TYPE stato_tirocinio AS ENUM (
                            'IN_CORSO',
                            'FINITO'
                        );
                    END IF;
                END
                $$;
                """);

        statement.execute("""
                DO $$
                BEGIN
                    IF NOT EXISTS (
                        SELECT 1 FROM pg_type
                        WHERE typname = 'tipo_tirocinio'
                    ) THEN
                        CREATE TYPE tipo_tirocinio AS ENUM (
                            'INTERNO',
                            'ESTERNO'
                        );
                    END IF;
                END
                $$;
                """);
    }

    private static void creaTabelle(Statement statement)
            throws Exception {

        statement.execute("""
                CREATE TABLE IF NOT EXISTS utente (
                    id SERIAL PRIMARY KEY,
                    nome VARCHAR(100) NOT NULL,
                    cognome VARCHAR(100) NOT NULL,
                    email VARCHAR(150) NOT NULL UNIQUE,
                    login VARCHAR(100) NOT NULL UNIQUE,
                    password VARCHAR(255) NOT NULL
                );
                """);

        statement.execute("""
                CREATE TABLE IF NOT EXISTS studente (
                    matricola VARCHAR(50) PRIMARY KEY,
                    id_utente INTEGER NOT NULL UNIQUE,
                    CONSTRAINT fk_studente_utente
                    FOREIGN KEY (id_utente)
                    REFERENCES utente(id)
                    ON DELETE CASCADE
                );
                """);

        statement.execute("""
                CREATE TABLE IF NOT EXISTS docente (
                    id_docente SERIAL PRIMARY KEY,
                    id_utente INTEGER NOT NULL UNIQUE,
                    CONSTRAINT fk_docente_utente
                    FOREIGN KEY (id_utente)
                    REFERENCES utente(id)
                    ON DELETE CASCADE
                );
                """);

        statement.execute("""
                CREATE TABLE IF NOT EXISTS coordinatore (
                    id_docente INTEGER PRIMARY KEY,
                    CONSTRAINT fk_coordinatore_docente
                    FOREIGN KEY (id_docente)
                    REFERENCES docente(id_docente)
                    ON DELETE CASCADE
                );
                """);

        statement.execute("""
                CREATE TABLE IF NOT EXISTS tirocinio (
                    id SERIAL PRIMARY KEY,
                    argomento VARCHAR(255) NOT NULL,
                    id_docente_responsabile INTEGER NOT NULL,
                    stato stato_tirocinio NOT NULL,
                    tipo tipo_tirocinio NOT NULL,
                    CONSTRAINT fk_tirocinio_docente
                    FOREIGN KEY (id_docente_responsabile)
                    REFERENCES docente(id_docente)
                );
                """);

        statement.execute("""
                CREATE TABLE IF NOT EXISTS tirocinio_interno (
                    id_tirocinio INTEGER PRIMARY KEY,
                    CONSTRAINT fk_tirocinio_interno
                    FOREIGN KEY (id_tirocinio)
                    REFERENCES tirocinio(id)
                    ON DELETE CASCADE
                );
                """);

        statement.execute("""
                CREATE TABLE IF NOT EXISTS tirocinio_esterno (
                    id_tirocinio INTEGER PRIMARY KEY,
                    nominativo_referente_aziendale VARCHAR(150) NOT NULL,
                    CONSTRAINT fk_tirocinio_esterno
                    FOREIGN KEY (id_tirocinio)
                    REFERENCES tirocinio(id)
                    ON DELETE CASCADE
                );
                """);

        statement.execute("""
                CREATE TABLE IF NOT EXISTS seduta_laurea (
                    id SERIAL PRIMARY KEY,
                    data DATE NOT NULL,
                    ora TIME NOT NULL,
                    luogo VARCHAR(255) NOT NULL,
                    disponibilita BOOLEAN NOT NULL DEFAULT TRUE
                );
                """);

        statement.execute("""
                CREATE TABLE IF NOT EXISTS prenotazione_seduta (
                    id SERIAL PRIMARY KEY,
                    matricola_studente VARCHAR(50) NOT NULL,
                    id_seduta INTEGER NOT NULL UNIQUE,
                    CONSTRAINT fk_prenotazione_studente
                    FOREIGN KEY (matricola_studente)
                    REFERENCES studente(matricola),
                    CONSTRAINT fk_prenotazione_seduta
                    FOREIGN KEY (id_seduta)
                    REFERENCES seduta_laurea(id)
                    ON DELETE CASCADE
                );
                """);

        statement.execute("""
                CREATE TABLE IF NOT EXISTS richiesta_tirocinio (
                    id SERIAL PRIMARY KEY,
                    stato stato_richiesta NOT NULL,
                    matricola_studente VARCHAR(50) NOT NULL,
                    id_tirocinio INTEGER NOT NULL,
                    id_docente INTEGER NOT NULL,
                    CONSTRAINT fk_richiesta_studente
                    FOREIGN KEY (matricola_studente)
                    REFERENCES studente(matricola),
                    CONSTRAINT fk_richiesta_tirocinio
                    FOREIGN KEY (id_tirocinio)
                    REFERENCES tirocinio(id),
                    CONSTRAINT fk_richiesta_docente
                    FOREIGN KEY (id_docente)
                    REFERENCES docente(id_docente)
                );
                """);

        statement.execute("""
                CREATE TABLE IF NOT EXISTS tesi (
                    id SERIAL PRIMARY KEY,
                    stato stato_tesi NOT NULL,
                    file VARCHAR(500) NOT NULL,
                    versione INTEGER NOT NULL DEFAULT 1,
                    matricola_studente VARCHAR(50) NOT NULL,
                    id_relatore INTEGER NOT NULL,
                    id_seduta INTEGER NOT NULL,
                    CONSTRAINT fk_tesi_studente
                    FOREIGN KEY (matricola_studente)
                    REFERENCES studente(matricola),
                    CONSTRAINT fk_tesi_relatore
                    FOREIGN KEY (id_relatore)
                    REFERENCES docente(id_docente),
                    CONSTRAINT fk_tesi_seduta
                    FOREIGN KEY (id_seduta)
                    REFERENCES seduta_laurea(id)
                );
                """);

        statement.execute("""
                CREATE TABLE IF NOT EXISTS commissione (
                    id SERIAL PRIMARY KEY,
                    id_seduta INTEGER NOT NULL UNIQUE,
                    CONSTRAINT fk_commissione_seduta
                    FOREIGN KEY (id_seduta)
                    REFERENCES seduta_laurea(id)
                    ON DELETE CASCADE
                );
                """);

        statement.execute("""
                CREATE TABLE IF NOT EXISTS commissione_docente (
                    id_commissione INTEGER NOT NULL,
                    id_docente INTEGER NOT NULL,
                    PRIMARY KEY (id_commissione, id_docente),
                    CONSTRAINT fk_commissione_docente_commissione
                    FOREIGN KEY (id_commissione)
                    REFERENCES commissione(id)
                    ON DELETE CASCADE,
                    CONSTRAINT fk_commissione_docente_docente
                    FOREIGN KEY (id_docente)
                    REFERENCES docente(id_docente)
                    ON DELETE CASCADE
                );
                """);
    }

    private static void creaDatiIniziali(Statement statement)
            throws Exception {

        // STUDENTE DEMO
        statement.execute("""
                INSERT INTO utente
                    (nome, cognome, email, login, password)
                VALUES
                    ('Studente', 'Demo',
                     'studente@demo.it',
                     'studente',
                     '1234')
                ON CONFLICT (login) DO NOTHING;
                """);

        statement.execute("""
                INSERT INTO studente (matricola, id_utente)
                SELECT
                    'SP001',
                    id
                FROM utente
                WHERE login = 'studente'
                ON CONFLICT (matricola) DO NOTHING;
                """);

        // COORDINATORE DEMO
        statement.execute("""
                INSERT INTO utente
                    (nome, cognome, email, login, password)
                VALUES
                    ('Coordinatore', 'Demo',
                     'coordinatore@demo.it',
                     'coordinatore',
                     '1234')
                ON CONFLICT (login) DO NOTHING;
                """);

        statement.execute("""
                INSERT INTO docente (id_utente)
                SELECT id
                FROM utente
                WHERE login = 'coordinatore'
                ON CONFLICT (id_utente) DO NOTHING;
                """);

        statement.execute("""
                INSERT INTO coordinatore (id_docente)
                SELECT d.id_docente
                FROM docente d
                JOIN utente u
                  ON d.id_utente = u.id
                WHERE u.login = 'coordinatore'
                ON CONFLICT DO NOTHING;
                """);

        // DOCENTE DEMO
        statement.execute("""
                INSERT INTO utente
                    (nome, cognome, email, login, password)
                VALUES
                    ('Docente', 'Demo',
                     'docente@demo.it',
                     'docente',
                     '1234')
                ON CONFLICT (login) DO NOTHING;
                """);

        statement.execute("""
                INSERT INTO docente (id_utente)
                SELECT id
                FROM utente
                WHERE login = 'docente'
                ON CONFLICT (id_utente) DO NOTHING;
                """);

        // SEDUTA DEMO
        statement.execute("""
                INSERT INTO seduta_laurea
                    (data, ora, luogo, disponibilita)
                SELECT
                    DATE '2030-01-15',
                    TIME '10:00:00',
                    'Aula Magna',
                    TRUE
                WHERE NOT EXISTS (
                    SELECT 1
                    FROM seduta_laurea
                    WHERE data = DATE '2030-01-15'
                      AND ora = TIME '10:00:00'
                      AND luogo = 'Aula Magna'
                );
                """);

        // TIROCINIO DEMO
        statement.execute("""
                INSERT INTO tirocinio
                    (
                        argomento,
                        id_docente_responsabile,
                        stato,
                        tipo
                    )
                SELECT
                    'Sviluppo applicazioni Java',
                    d.id_docente,
                    'IN_CORSO',
                    'INTERNO'
                FROM docente d
                JOIN utente u
                    ON d.id_utente = u.id
                WHERE u.login = 'docente'
                  AND NOT EXISTS (
                      SELECT 1
                      FROM tirocinio t
                      WHERE t.argomento =
                            'Sviluppo applicazioni Java'
                  );
                """);

        statement.execute("""
                INSERT INTO tirocinio_interno (id_tirocinio)
                SELECT id
                FROM tirocinio
                WHERE argomento =
                      'Sviluppo applicazioni Java'
                ON CONFLICT DO NOTHING;
                """);
    }
}