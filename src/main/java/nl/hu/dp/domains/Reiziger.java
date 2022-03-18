package nl.hu.dp.domains;

import java.util.Date;

public class Reiziger {
    private int id = 0;
    private String voorletters = "";
    private String tussenvoegsel = "";
    private String achternaam = "";
    private Date geboortedatum = new Date(0);
    private Adres adres;

    public Reiziger(){
    }
    public Reiziger(int id, String voorlt, String tussenv, String achternm, Date gebdt){
        this.id = id;
        this.voorletters = voorlt;
        this.tussenvoegsel = tussenv;
        this.achternaam = achternm;
        this.geboortedatum = gebdt;
    }

    public Adres getAdres() {
        return adres;
    }

    public void setAdres(Adres adres) {
        this.adres = adres;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVoorletters() {
        return voorletters;
    }

    public void setVoorletters(String voorletters) {
        if(voorletters != null){

            if(voorletters.length() > 10) {
                voorletters = voorletters.substring(0,10);
            }
        }
        this.voorletters = voorletters;
    }

    public String getTussenvoegsel() {
        return tussenvoegsel;
    }

    public void setTussenvoegsel(String tussenvoegsel) {
            this.tussenvoegsel = tussenvoegsel;
    }

    public String getAchternaam() {
        return achternaam;
    }

    public void setAchternaam(String achternaam) {
           this.achternaam = achternaam;
    }

    public java.sql.Date getGeboortedatum() {
        return (java.sql.Date) geboortedatum;
    }

    public void setGeboortedatum(Date geboortedatum) {
        this.geboortedatum = geboortedatum;
    }

    @Override
    public String toString() {
        String s = "";
        if(tussenvoegsel != null) {
            s = String.format("{#%s %s %s %s, geb. %s, ", id, voorletters, tussenvoegsel, achternaam, geboortedatum);
        }        if(tussenvoegsel == null) {
            s = String.format("{#%s %s %s, geb. %s, ", id, voorletters, achternaam, geboortedatum);
        }
        if(adres != null){
        s += adres.toString() + '}';
        } if(adres == null){
        s += '}';
        }
        return s;
    }

}
