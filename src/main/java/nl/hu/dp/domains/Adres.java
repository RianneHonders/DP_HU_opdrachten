package nl.hu.dp.domains;

import java.util.Date;

public class Adres {
    private int adres_id = 0;
    private String postcode = "";
    private String huisnummer = "";
    private String straat = "";
    private String woonplaats = "";
    private Reiziger reiziger;


    public Adres(){
    }

    public Adres(int id, String pscode, String huisnr, String str, String woonpts){
        this.adres_id = id;
        this.postcode = pscode;
        this.huisnummer = huisnr;
        this.straat = str;
        this.woonplaats = woonpts;
    }

    public int getAdres_id() {
        return adres_id;
    }

    public void setAdres_id(int adres_id) {
        this.adres_id = adres_id;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getHuisnummer() {
        return huisnummer;
    }

    public void setHuisnummer(String huisnummer) {
        this.huisnummer = huisnummer;
    }

    public String getStraat() {
        return straat;
    }

    public void setStraat(String straat) {
        this.straat = straat;
    }

    public String getWoonplaats() {
        return woonplaats;
    }

    public void setWoonplaats(String woonplaats) {
        this.woonplaats = woonplaats;
    }

    public Reiziger getReiziger() {
        return reiziger;
    }

    public void setReiziger(Reiziger reiziger) {
        this.reiziger = reiziger;
    }

    @Override
    public String toString() {
        String s = "";
        return s = String.format("Adres {#%s %s-%s}", adres_id, postcode, huisnummer);
           }

}
