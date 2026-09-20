package controller;

import ImplementazionePostgresDAO.PostgresCommissioneDAO;
import ImplementazionePostgresDAO.PostgresDocenteDAO;
import ImplementazionePostgresDAO.PostgresRichiestaTirocinioDAO;
import ImplementazionePostgresDAO.PostgresSedutaLaureaDAO;
import ImplementazionePostgresDAO.PostgresStudenteDAO;
import ImplementazionePostgresDAO.PostgresTesiDAO;
import ImplementazionePostgresDAO.PostgresTirocinioDAO;
import ImplementazionePostgresDAO.PostgresUtenteDAO;

import dao.CommissioneDAO;
import dao.DocenteDAO;
import dao.RichiestaTirocinioDAO;
import dao.SedutaLaureaDAO;
import dao.StudenteDAO;
import dao.TesiDAO;
import dao.TirocinioDAO;
import dao.UtenteDAO;

import model.Commissione;
import model.Docente;
import model.RichiestaTirocinio;
import model.SedutaLaurea;
import model.StatoRichiesta;
import model.Studente;
import model.Tesi;
import model.Tirocinio;
import model.Utente;

import java.util.List;

public class Controller {

    private UtenteDAO utenteDAO;
    private StudenteDAO studenteDAO;
    private DocenteDAO docenteDAO;
    private TirocinioDAO tirocinioDAO;
    private RichiestaTirocinioDAO richiestaTirocinioDAO;
    private TesiDAO tesiDAO;
    private SedutaLaureaDAO sedutaLaureaDAO;
    private CommissioneDAO commissioneDAO;

    public Controller() {

        utenteDAO = new PostgresUtenteDAO();
        studenteDAO = new PostgresStudenteDAO();
        docenteDAO = new PostgresDocenteDAO();
        tirocinioDAO = new PostgresTirocinioDAO();
        richiestaTirocinioDAO = new PostgresRichiestaTirocinioDAO();
        tesiDAO = new PostgresTesiDAO();
        sedutaLaureaDAO = new PostgresSedutaLaureaDAO();
        commissioneDAO = new PostgresCommissioneDAO();
    }

    // =========================
    // LOGIN
    // =========================

    public boolean LoginStudente(String login, String password) {

        Utente utente = utenteDAO.Login(login, password);

        if (utente == null) {
            return false;
        }

        return studenteDAO.TrovaStudenteDaLogin(login) != null;
    }

    public boolean LoginDocente(String login, String password) {

        Utente utente = utenteDAO.Login(login, password);

        if (utente == null) {
            return false;
        }

        return docenteDAO.TrovaDocenteDaLogin(login) != null;
    }

    public boolean LoginCoordinatore(String login, String password) {

        Utente utente = utenteDAO.Login(login, password);

        if (utente == null) {
            return false;
        }

        return docenteDAO.TrovaCoordinatoreDaLogin(login) != null;
    }

    public Studente OttieniStudente(String login) {

        return studenteDAO.TrovaStudenteDaLogin(login);
    }

    public Docente OttieniDocente(String login) {

        return docenteDAO.TrovaDocenteDaLogin(login);
    }

    public Docente OttieniCoordinatore(String login) {

        return docenteDAO.TrovaCoordinatoreDaLogin(login);
    }

    // =========================
    // REGISTRAZIONE
    // =========================

    public void RegistrazioneStudente(
            String nome,
            String cognome,
            String email,
            String matricola,
            String login,
            String password) {

        Studente studente = new Studente(
                nome,
                cognome,
                email,
                login,
                password,
                matricola
        );

        studenteDAO.SalvaStudente(studente);
    }

    public void RegistrazioneDocente(
            String nome,
            String cognome,
            String email,
            String login,
            String password) {

        Docente docente = new Docente(
                nome,
                cognome,
                email,
                login,
                password,
                0
        );

        docenteDAO.SalvaDocente(docente);
    }

    // =========================
    // STUDENTE
    // =========================

    public List<Tirocinio> ListaTirocini() {

        return tirocinioDAO.ListaTirocini();
    }

    public List<Docente> ListaDocenti() {

        return docenteDAO.ListaDocenti();
    }

    public void RichiestaTirocinio(
            RichiestaTirocinio richiesta) {

        richiestaTirocinioDAO.SalvaRichiesta(richiesta);
    }

    public List<RichiestaTirocinio> VisualizzaRichieste(
            StatoRichiesta stato) {

        if (stato == null) {
            return richiestaTirocinioDAO.ListaRichieste();
        }

        return richiestaTirocinioDAO.ListaRichiestePerStato(stato);
    }

    public void PrenotaSedutaLaurea(
            SedutaLaurea seduta,
            Studente studente) {

        sedutaLaureaDAO.PrenotaSeduta(
                seduta,
                studente
        );
    }

    public List<SedutaLaurea> ListaSedute() {

        return sedutaLaureaDAO.ListaSedute();
    }

    public void CaricaTesi(Tesi tesi) {

        tesiDAO.SalvaTesi(tesi);
    }

    // =========================
    // DOCENTE
    // =========================

    public void AggiuntaArgomentoTirocinio(
            Tirocinio tirocinio) {

        tirocinioDAO.SalvaTirocinio(tirocinio);
    }

    public void AccettaORifiutaRichiesta(
            RichiestaTirocinio richiesta,
            boolean accetta) {

        if (accetta) {

            richiesta.setStato(
                    StatoRichiesta.ACCETTATA
            );

        } else {

            richiesta.setStato(
                    StatoRichiesta.RIFIUTATA
            );
        }

        richiestaTirocinioDAO.AggiornaStato(
                richiesta
        );
    }

    public void AccettaORifiutaTesi(Tesi tesi) {

        tesiDAO.AggiornaStato(tesi);
    }

    // =========================
    // COORDINATORE
    // =========================

    public void InserisciSedutaLaurea(
            SedutaLaurea seduta) {

        sedutaLaureaDAO.SalvaSeduta(seduta);
    }

    public List<Studente> VisualizzaStudentiAppartSeduta(
            SedutaLaurea seduta) {

        return sedutaLaureaDAO.ListaStudenti(seduta);
    }

    public List<Tesi> ListaTesi() {

        return tesiDAO.ListaTesi();
    }

    public void ControllaTesi(Tesi tesi) {

        tesiDAO.AggiornaStato(tesi);
    }

    public void FormaCommissione(
            Commissione commissione) {

        commissioneDAO.SalvaCommissione(commissione);
    }

    public List<Commissione> ListaCommissioni() {

        return commissioneDAO.ListaCommissioni();
    }
}