package nl.hu.dp;

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

            System.out.println("#"+ set.getString("reiziger_id") + " " + set.getString("voorletters") + ". " + set.getString("tussenvoegsel") + " " + set.getString("achternaam") + " (" + set.getString("geboortedatum") + ")");
            }

        closeConnection();
    }



}
