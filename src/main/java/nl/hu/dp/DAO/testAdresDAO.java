package nl.hu.dp.DAO;

import nl.hu.dp.domains.Adres;
import nl.hu.dp.domains.Reiziger;

import java.sql.SQLException;
import java.util.List;

public class testAdresDAO {

    private static void testAdresDAO(AdresDAOPostgres adao) throws SQLException {
        System.out.println("\n---------- testAdresDAO -------------");

        // Haal alle reizigers op uit de database
        List<Adres> Adressen = adao.findAll();
        System.out.println("[Test] ReizigerDAO.findAll() geeft de volgende reizigers:");
        for (Adres a : Adressen) {
            System.out.println(a);
        }
        System.out.println();

    }
}