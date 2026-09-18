package model;

public class TirocinioInterno extends model.Tirocinio {

    public TirocinioInterno() {
        super();
    }

    public TirocinioInterno(int id, String argomento,
                            model.Docente docenteResponsabile,
                            model.StatoTirocinio stato) {
        super(id, argomento, docenteResponsabile, stato);
    }
}
