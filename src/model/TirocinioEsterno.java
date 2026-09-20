package model;

public class TirocinioEsterno extends model.Tirocinio {

    private String nominativoReferenteAziendale;

    public TirocinioEsterno() {
        super();
    }

    public TirocinioEsterno(int id, String argomento,
                            model.Docente docenteResponsabile,
                            model.StatoTirocinio stato,
                            String nominativoReferenteAziendale) {
        super(id, argomento, docenteResponsabile, stato);
        this.nominativoReferenteAziendale = nominativoReferenteAziendale;
    }

    public String getNominativoReferenteAziendale() {
        return nominativoReferenteAziendale;
    }

    public void setNominativoReferenteAziendale(String nominativoReferenteAziendale) {
        this.nominativoReferenteAziendale = nominativoReferenteAziendale;
    }
}
