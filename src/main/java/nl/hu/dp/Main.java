package nl.hu.dp;
import nl.hu.dp.DAO.*;
import nl.hu.dp.domains.Adres;
import nl.hu.dp.domains.OVChipkaart;
import nl.hu.dp.domains.Product;
import nl.hu.dp.domains.Reiziger;

import javax.sound.midi.Soundbank;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

public class Main {

    private static Connection connection = null;

    public static void main(String[] args) throws SQLException {
        ReizigersDAOPostgres rdao = new ReizigersDAOPostgres(getConnection());
        AdresDAOPostgres adoa = new AdresDAOPostgres(getConnection());
        OVChipkaartDAOpostgres odao = new OVChipkaartDAOpostgres(getConnection());
        ProductDAOPostgres pdao = new ProductDAOPostgres(getConnection());
        rdao.setAdao(adoa);
        adoa.setRdao(rdao);
        odao.setRdao(rdao);
        rdao.setOdao(odao);
        odao.setPdao(pdao);
        pdao.setOdao(odao);


//        testReizigerDAO(rdao);
//        testAdresDAO(adoa, rdao);
        testProductDAO(pdao, odao, rdao);
//        testOVChipkaartDAO(odao, rdao, adoa);
//        testConnection();
        System.out.println("\n----\nendTest\n----\n");
    }

    private static void testProductDAO(ProductDAOPostgres pdao, OVChipkaartDAOpostgres odao, ReizigersDAOPostgres rdao) throws SQLException {

        // Maak een nieuwe ovchipkaart en product aan aan en persisteer deze in de database
        String geldig_tot = "2026-03-19";
        String geboorteD = "1985-10-10";
        Reiziger r1 = new Reiziger(6, "A","", "Benschop", java.sql.Date.valueOf(geboorteD));
        OVChipkaart oc2 = new OVChipkaart(12589, java.sql.Date.valueOf(geldig_tot), 2, 50.00 );
        Product p1 = new Product(7, "test", "test", 20.50);
        ArrayList<OVChipkaart> ovChipkaarten = new ArrayList<OVChipkaart>();

        r1.setoVChipkaarten(ovChipkaarten);
        oc2.setReiziger(r1);
        ovChipkaarten.add(oc2);
        oc2.addProduct(p1);
        p1.addOVChipkaart(oc2);

        pdao.save(p1);
        rdao.save(r1);

        System.out.println("[Test] ProductDAO.findAll() geeft de volgende producten:");

        List<Product> producten2 = pdao.findAll();
        for (Product product : producten2) {
            System.out.println(product);
        }
        System.out.println("Aantal producten in de database na save " + producten2.size());


        System.out.println("\n[Testing delete]: Haal nieuwe data uit database");
        pdao.delete(p1);
        odao.delete(oc2);
        rdao.delete(r1);

        List<Product> producten3 = pdao.findAll();
        for (Product product : producten3) {
            System.out.println(product);
        }
        System.out.println("Aantal producten in de database na delete " + producten3.size());

    }


    private static void testOVChipkaartDAO(OVChipkaartDAOpostgres odao, ReizigersDAOPostgres rdao, AdresDAOPostgres adao) throws SQLException {
        System.out.println("\n---------- testOVChipkaartDAO -------------");

        // Haal alle OVChipkaarten op uit de database
        List<OVChipkaart> ovChipkaarten = odao.findAll();
        System.out.println("[Test] ReizigerDAO.findAll() geeft de volgende reizigers:");
        for (OVChipkaart ov : ovChipkaarten) {
            System.out.println(ov);
        }
        System.out.println();

        // Haal alle reizigers met adres en ovchipkaart op uit de database
        List<Reiziger> reizigers = rdao.findAll();
        System.out.println("[Test] ReizigerDAO.findAll() geeft de volgende reizigers:");
        for (Reiziger r : reizigers) {
            System.out.println(r);
        }
        System.out.println();

        // Maak een nieuwe reiziger en adres aan aan en persisteer deze in de database
        String geldig_tot = "2026-03-19";
        String gbdatum = "1987-06-16";
        Reiziger r3 = new Reiziger(7, "E", "", "Kolk", java.sql.Date.valueOf(gbdatum));
        OVChipkaart oc2 = new OVChipkaart(58469, java.sql.Date.valueOf(geldig_tot), 2, 50.00 );
        Adres a3 = new Adres(7, "3522ST", "128", "Waalstraat", "Utrecht");
        ArrayList<OVChipkaart> ovChipkaartenr3 = new ArrayList<OVChipkaart>();

        oc2.setReiziger(r3);
        a3.setReiziger(r3);
        ovChipkaartenr3.add(oc2);
        r3.setoVChipkaarten(ovChipkaartenr3);

        rdao.save(r3);

        System.out.println("[Test] ReizigerDAO.findAll() geeft de volgende reizigers:");
        List<Reiziger> reiziger = rdao.findAll();
        for (Reiziger r : reiziger) {
            System.out.println(r);
        }
        System.out.println();

        //Test delete
        System.out.println("\n[Testing delete]: Haal nieuwe reiziger uit database");
        adao.delete(a3);
        odao.delete(oc2);
        rdao.delete(r3);
        List<Reiziger> alleReizigers = rdao.findAll();
        for(Reiziger r : alleReizigers){
            System.out.println(r.toString());
        }
    }





    private static void testAdresDAO(AdresDAOPostgres adao, ReizigersDAOPostgres rdao) throws SQLException {

        System.out.println("\n---------- testAdresDAO -------------");

        // Haal alle reizigers op uit de database
        List<Adres> adressen = adao.findAll();
        System.out.println("[Test] ReizigerDAO.findAll() geeft de volgende reizigers:");
        for (Adres a : adressen) {
            System.out.println(a);
        }
        System.out.println();

        // Haal alle reizigers op uit de database
        List<Reiziger> reizigers = rdao.findAll();
        System.out.println("[Test] ReizigerDAO.findAll() geeft de volgende reizigers:");
        for (Reiziger r : reizigers) {
            System.out.println(r);
        }
        System.out.println();

        // Maak een nieuwe reiziger en adres aan aan en persisteer deze in de database
        Adres a1 = new Adres(80, "3582KD", "30", "Burg Fockema Andreaelaan", "Utrecht");
        String gbdatum = "1987-11-20";
        Reiziger r1 = new Reiziger(6, "RC", "", "Loch", java.sql.Date.valueOf(gbdatum));
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.save() ");
        reizigers = rdao.findAll();
        a1.setReiziger(r1);
        r1.setAdres(a1);
        rdao.save(r1);
        List<Reiziger> allereizigers = rdao.findAll();
        System.out.println("[Test] ReizigerDAO.findAll() geeft de volgende reizigers:");
        for (Reiziger r : allereizigers) {
            System.out.println(r);
        }
        System.out.println();

        // Test update
        System.out.println("\n[Testing update]: past achternaam van persoon met Id: 77 aan & huisnummer ");
        r1.setAchternaam("Zwart");
        rdao.update(r1);
        a1.setHuisnummer("40");
        adao.update(a1);
        reizigers = rdao.findAll();
        for(Reiziger r : reizigers){
            System.out.println(r.toString());
        }
        System.out.print("\n[Test] aantal reizigers: " + reizigers.size() + " reizigers, na ReizigerDAO.update() \n");

        //Test delete
        System.out.println("\n[Testing delete]: check achternaam & adres van persoon met Id: 77");
        adao.delete(a1);
        rdao.delete(r1);
        reizigers = rdao.findAll();
        for(Reiziger r : reizigers){
            System.out.println(r.toString());
        }
        System.out.print("\n[Test] aantal reizigers: " + reizigers.size() + " reizigers, na AdresDAO.delete() & ReizigerDAO.delete() \n");
    }


    private static void testReizigerDAO(ReizigersDAOPostgres rdao) throws SQLException {
        System.out.println("\n---------- testReizigerDAO -------------");

        // Haal alle reizigers op uit de database
        List<Reiziger> reizigers = rdao.findAll();
        System.out.println("[Test] ReizigerDAO.findAll() geeft de volgende reizigers:");
        for (Reiziger r : reizigers) {
            System.out.println(r);
        }
        System.out.println();

        // Maak een nieuwe reiziger aan en persisteer deze in de database
        String gbdatum = "1981-03-14";
        Reiziger sietske = new Reiziger(77, "S", "", "Boers", java.sql.Date.valueOf(gbdatum));
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.save() ");
        rdao.save(sietske);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers\n");

        // Test update
        System.out.println("\n[Testing update]: check achternaam van persoon met Id: 77");
        sietske.setAchternaam("Zwart");
        rdao.update(sietske);
        reizigers = rdao.findAll();
        for(Reiziger r : reizigers){
            System.out.println(r.toString());
        }
        System.out.print("\n[Test] aantal reizigers: " + reizigers.size() + " reizigers, na ReizigerDAO.update() \n");

        //Test delete
        System.out.println("\n[Testing delete]: check achternaam van persoon met Id: 77");
        rdao.delete(sietske);
        reizigers = rdao.findAll();
        for(Reiziger r : reizigers){
            System.out.println(r.toString());
        }
        System.out.print("\n[Test] aantal reizigers: " + reizigers.size() + " reizigers, na ReizigerDAO.delete() \n");
    }

    private static Connection getConnection() throws SQLException  {
        if(connection == null) {

            String databaseName = "ovchip";
            String username = "postgres";
            String password = "Anoniem12!";

            String url = format(
                    "jdbc:postgresql://localhost/%s?user=%s&password=%s",
                    databaseName, username, password
            );
            connection = DriverManager.getConnection(url);
        }
        return connection;
    }

    private static void closeConnection() throws SQLException {
        if (connection != null) {
            connection.close();
            connection = null;
        }
    }

    private static void testConnection() throws SQLException {

        System.out.println("\n----\ntestConnection\n----\n");
        System.out.println("Alle reizigers:");
        getConnection();

        String query = "SELECT * FROM reiziger;";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet set = statement.executeQuery();

        while (set != null && set.next()) {
            String id = set.getString("reiziger_id");
            String voorl = set.getString("voorletters");
            String tussenv = set.getString("tussenvoegsel");
            String achternm = set.getString("achternaam");
            String gebdt = set.getString("geboortedatum");

            if (set.getString("tussenvoegsel") != null) {
                System.out.println(String.format("#%s %s. %s %s (%s)", id, voorl, tussenv, achternm, gebdt));
            } else {
                System.out.println(String.format("#%s %s. %s (%s)", id, voorl, achternm, gebdt));
            }

            closeConnection();
        }
    }


}
