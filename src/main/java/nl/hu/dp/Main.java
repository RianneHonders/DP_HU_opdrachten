package nl.hu.dp;
import nl.hu.dp.domains.Reiziger;

import javax.sound.midi.Soundbank;
import java.sql.*;

import static java.lang.String.format;

public class Main {

    private static Connection connection = null;

    public static void main(String[] args) throws SQLException {
        System.out.println("Hello");

        testConnection();

        System.out.println("\n----\nendTestConnection\n----\n");
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
        System.out.println("Alle reizigers");
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
