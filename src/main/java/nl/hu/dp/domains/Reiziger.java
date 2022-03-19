package nl.hu.dp.domains;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Reiziger {
    private int id = 0;
    private String voorletters = "";
    private String tussenvoegsel = "";
    private String achternaam = "";
    private Date geboortedatum = new Date(0);
    private Adres adres;
    private ArrayList<OVChipkaart> oVChipkaarten;

    public List<OVChipkaart> getOVChipkaarten(){
        return this.oVChipkaarten;
    }

    public void setoVChipkaarten(List<OVChipkaart> oVChipkaarten){
        this.oVChipkaarten = (ArrayList<OVChipkaart>) oVChipkaarten;
    }

    public Adres getAdres() {
        return adres;
    }

    public void setAdres(Adres adres) {
        this.adres = adres;
    }

    public Reiziger(int id, String voorletters, String tussenvoegsel, String achternaam, Date geboortedatum){
        this.id = id;
        this.voorletters =  voorletters;
        this.tussenvoegsel = tussenvoegsel;
        this.achternaam = achternaam;
        this.geboortedatum = geboortedatum;
    }

    public Reiziger(){}

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
            s = String.format("\n#%s %s %s %s, geb. %s, ", id, voorletters, tussenvoegsel, achternaam, geboortedatum);
        }        if(tussenvoegsel == null) {
            s = String.format("\n#%s %s %s, geb. %s, ", id, voorletters, achternaam, geboortedatum);
        }
        if(adres != null) {
            s += adres.toString();
        }
        if(oVChipkaarten != null){
            for (OVChipkaart ovChipkaart: oVChipkaarten){
                s+= "\n\t" + ovChipkaart.toString() ;
            }
        }
        return s ;
    }

}
