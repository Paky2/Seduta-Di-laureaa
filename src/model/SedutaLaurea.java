package model;

import java.time.LocalTime;
import java.util.Date;

public class SedutaLaurea {

    private int id;
    private Date data;
    private LocalTime ora;
    private String luogo;
    private boolean disponibilita;

    public SedutaLaurea() {
    }

    public SedutaLaurea(int id, Date data, LocalTime ora,
                        String luogo, boolean disponibilita) {
        this.id = id;
        this.data = data;
        this.ora = ora;
        this.luogo = luogo;
        this.disponibilita = disponibilita;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public LocalTime getOra() {
        return ora;
    }

    public void setOra(LocalTime ora) {
        this.ora = ora;
    }

    public String getLuogo() {
        return luogo;
    }

    public void setLuogo(String luogo) {
        this.luogo = luogo;
    }

    public boolean isDisponibilita() {
        return disponibilita;
    }

    public void setDisponibilita(boolean disponibilita) {
        this.disponibilita = disponibilita;
    }
    @Override
    public String toString() {
        return data + " - " + ora + " - " + luogo;
    }
}
