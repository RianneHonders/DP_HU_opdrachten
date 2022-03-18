package nl.hu.dp;
import nl.hu.dp.DAO.AdresDAOPostgres;
import nl.hu.dp.DAO.ReizigersDAO;
import nl.hu.dp.DAO.ReizigersDAOPostgres;
import nl.hu.dp.domains.Adres;
import nl.hu.dp.domains.Reiziger;

import javax.sound.midi.Soundbank;
import java.sql.*;
import java.util.List;

import static java.lang.String.format;

public class Main {

    private static Connection connection = null;

    public static void main(String[] args) throws SQLException {
        ReizigersDAOPostgres rdao = new ReizigersDAOPostgres(getConnection());
        AdresDAOPostgres adoa = new AdresDAOPostgres(getConnection());
        rdao.setAdao(adoa);
        adoa.setRdao(rdao);

//        testReizigerDAO(rdao);
        testAdresDAO(adoa, rdao);
//        testConnection();
        System.out.println("\n----\nendTestReizigerDAO\n----\n");
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
        System.out.println("\n[Testing update]: check achternaam van persoon met Id: 77");
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
        System.out.println("\n[Testing delete]: check achternaam van persoon met Id: 77");
        adao.delete(a1);
        rdao.delete(r1);
        reizigers = rdao.findAll();
        for(Reiziger r : reizigers){
            System.out.println(r.toString());
        }
        System.out.print("\n[Test] aantal reizigers: " + reizigers.size() + " reizigers, na ReizigerDAO.delete() \n");
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
