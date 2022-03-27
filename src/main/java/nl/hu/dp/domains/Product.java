package nl.hu.dp.domains;

import java.util.ArrayList;
import java.util.List;

public class Product {
    private int product_nummer;
    private String naam;
    private String beschijving;
    private double prijs;
    private List<OVChipkaart> OVChipkaarten = new ArrayList<OVChipkaart>();


    public Product() {

    }

    public Product(int pnm, String nm, String b, double pr) {
        this.product_nummer = pnm;
        this.naam = nm;
        this.beschijving = b;
        this.prijs = pr;
    }

    public int getProduct_nummer() {
        return product_nummer;
    }


    public String getNaam() {
        return naam;
    }


    public String getBeschijving() {
        return beschijving;
    }

    public double getPrijs() {
        return prijs;
    }


    public List<OVChipkaart> getOVChipkaarten(){
        return this.OVChipkaarten;
    }


    public void setOVChipkaarten(List<OVChipkaart> OVChipkaarten) {
        this.OVChipkaarten = (ArrayList<OVChipkaart>) OVChipkaarten;
    }


    public boolean addOVChipkaart(OVChipkaart ovChipkaart) {
        for (OVChipkaart ov : OVChipkaarten) {
            if (ov.getKaart_nummer() == ovChipkaart.getKaart_nummer()) {
                return false;
            }
        }
        OVChipkaarten.add(ovChipkaart);
        return true;
    }

    public boolean deleteOVChipkaart(OVChipkaart ovChipkaart) {
        for (OVChipkaart ov : OVChipkaarten) {
            if (ov.getKaart_nummer() == ovChipkaart.getKaart_nummer()) {
                OVChipkaarten.remove(ovChipkaart);
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        String pr = "";
        pr = String.format("Product: %d, naam: %s, beschrijving: %s, prijs: €%.2f", product_nummer, naam, beschijving, prijs);

        if(OVChipkaarten != null){
            for (OVChipkaart ov: OVChipkaarten){
                pr += "\n\t" + ov.toString() ;
            }
        }
        return pr;
    }
}
