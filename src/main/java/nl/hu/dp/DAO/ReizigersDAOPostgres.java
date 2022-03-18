package nl.hu.dp.DAO;

import nl.hu.dp.domains.Adres;
import nl.hu.dp.domains.Reiziger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReizigersDAOPostgres implements ReizigersDAO {
    private Connection connection = null;
    private AdresDAO adao;

    public ReizigersDAOPostgres(Connection inConnection) throws SQLException {
        this.connection = inConnection;
    }

    public AdresDAO getAdao() {
        return adao;
    }

    public void setAdao(AdresDAO adao) {
        this.adao = adao;
    }

    public boolean save(Reiziger inReiziger) throws SQLException {
        try {
            PreparedStatement statement = this.connection.prepareStatement("INSERT INTO reiziger (reiziger_id, voorletters, tussenvoegsel, achternaam, geboortedatum) VALUES (?, ?, ?, ?, ?)");
            statement.setInt(1, inReiziger.getId());
            statement.setString(2, inReiziger.getVoorletters());
            statement.setString(3, inReiziger.getTussenvoegsel());
            statement.setString(4, inReiziger.getAchternaam());
            statement.setDate(5, inReiziger.getGeboortedatum());



            statement.executeUpdate();


                adao.save(inReiziger.getAdres());

            statement.close();

            return true;
        } catch (SQLException throwables) {
            System.err.println("SQLException: " + throwables.getMessage());
        }
        return false;
    }


    public boolean update(Reiziger inReiziger) throws SQLException {
        try{
            PreparedStatement statement = this.connection.prepareStatement(
                    "UPDATE reiziger SET reiziger_id = ?, voorletters = ?, tussenvoegsel=? , achternaam = ?, geboortedatum = ? WHERE reiziger_id = ?;"
            );
            statement.setInt(1, inReiziger.getId());
            statement.setString(2, inReiziger.getVoorletters());
            statement.setString(3, inReiziger.getTussenvoegsel());
            statement.setString(4, inReiziger.getAchternaam());
            statement.setDate(5, inReiziger.getGeboortedatum());
            statement.setInt(6, inReiziger.getId());

            statement.executeUpdate();
            statement.close();

            return true;

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return false;
    }


    public boolean delete(Reiziger inReiziger) throws SQLException {
        try{
            PreparedStatement statement = this.connection.prepareStatement(
                    "DELETE FROM reiziger WHERE reiziger_id = ?;"
            );
            statement.setInt(1, inReiziger.getId());

            statement.executeUpdate();
            statement.close();
            return true;
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return false;
    }


    public Reiziger parseStatement(ResultSet theSet) throws SQLException {
        Reiziger reiziger = new Reiziger();

        reiziger.setId(theSet.getInt("reiziger_id"));
        reiziger.setVoorletters(theSet.getString("voorletters"));
        reiziger.setTussenvoegsel(theSet.getString("tussenvoegsel"));
        reiziger.setAchternaam(theSet.getString("achternaam"));
        reiziger.setGeboortedatum(theSet.getDate("geboortedatum"));

        return reiziger;
    }


    public Reiziger findById(int id) throws SQLException {
        ArrayList<Reiziger> reizigers = new ArrayList<Reiziger>();
        try {
            PreparedStatement statement = this.connection.prepareStatement("SELECT * FROM reiziger;");
            ResultSet theSet = statement.executeQuery();

            while (theSet.next()) {
                Reiziger reiziger = new Reiziger();
                reiziger.setId(theSet.getInt("reiziger_id"));
                reiziger.setVoorletters(theSet.getString("voorletters"));
                reiziger.setTussenvoegsel(theSet.getString("tussenvoegsel"));
                reiziger.setAchternaam(theSet.getString("achternaam"));
                reiziger.setGeboortedatum(theSet.getDate("geboortedatum"));


                return reiziger;
            }
            theSet.close();
            statement.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Reiziger findByGbdatum(String datum) throws SQLException {
        for (Reiziger r: findAll()) {
            if(r.getGeboortedatum().equals(datum)) {
                return r;
            }
        }
        return null;
    }


    public List<Reiziger> findAll() throws SQLException {
        ArrayList<Reiziger> reizigers = new ArrayList<Reiziger>();

        try {
            PreparedStatement statement = this.connection.prepareStatement("SELECT * FROM reiziger");
            ResultSet theSet = statement.executeQuery();

            while (theSet.next()) {
                Reiziger reiziger = new Reiziger();
                reiziger.setId(theSet.getInt("reiziger_id"));
                reiziger.setVoorletters(theSet.getString("voorletters"));
                reiziger.setTussenvoegsel(theSet.getString("tussenvoegsel"));
                reiziger.setAchternaam(theSet.getString("achternaam"));
                reiziger.setGeboortedatum(theSet.getDate("geboortedatum"));


                if (adao.findByReiziger(reiziger) != null) {
                    reiziger.setAdres(adao.findByReiziger(reiziger));
                    reiziger.getAdres().setReiziger(reiziger);
                }

                reizigers.add(reiziger);
            }

            theSet.close();
            statement.close();

        } catch (SQLException sqlex) {
            System.err.println("SQLException: " + sqlex.getMessage());
        }
        return reizigers;
    }
}
