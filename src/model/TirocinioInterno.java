package model;

public class TirocinioInterno extends Tirocinio {

    public TirocinioInterno() {
        super();
    }

    public TirocinioInterno(int id, String argomento,
                            Docente docenteResponsabile,
                            StatoTirocinio stato) {
        super(id, argomento, docenteResponsabile, stato);
    }
}