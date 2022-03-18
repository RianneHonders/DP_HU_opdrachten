package nl.hu.dp.DAO;

import nl.hu.dp.domains.Adres;
import nl.hu.dp.domains.Reiziger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdresDAOPostgres implements AdresDAO {
    private Connection connection = null;
    private ReizigersDAO rdao;

    public AdresDAOPostgres(Connection inConnection) throws SQLException {
        this.connection = inConnection;
    }

    public void setRdao(ReizigersDAO rdao) {
        this.rdao = rdao;
    }

    public ReizigersDAO getRdao() {
        return rdao;
    }


    @Override
    public boolean save(Adres inAdres) throws SQLException {
        try {
            PreparedStatement statement = this.connection.prepareStatement(
                    "INSERT INTO adres (adres_id, postcode, huisnummer, straat, woonplaats, reiziger_id) VALUES (?, ?, ?, ?, ?, ?)");
            statement.setInt(1, inAdres.getAdres_id());
            statement.setString(2, inAdres.getPostcode());
            statement.setString(3, inAdres.getHuisnummer());
            statement.setString(4, inAdres.getStraat());
            statement.setString(5, inAdres.getWoonplaats());
            statement.setInt(6, inAdres.getReiziger().getId());

            statement.executeUpdate();
            statement.close();

            return true;
        } catch (SQLException throwables) {
            System.err.println("SQLException: " + throwables.getMessage());
        }
        return false;
    }

    @Override
    public boolean update(Adres inAdres) throws SQLException {
        try{
            PreparedStatement statement = this.connection.prepareStatement(
                    "UPDATE adres SET adres_id = ?, postcode = ?, huisnummer=? , straat = ?, woonplaats = ?, reiziger_id = ? WHERE adres_id = ?;"
            );
            statement.setInt(1, inAdres.getAdres_id());
            statement.setString(2, inAdres.getPostcode());
            statement.setString(3, inAdres.getHuisnummer());
            statement.setString(4, inAdres.getStraat());
            statement.setString(5, inAdres.getWoonplaats());
            statement.setInt(6, inAdres.getReiziger().getId());
            statement.setInt(7, inAdres.getAdres_id());

            statement.executeUpdate();
            statement.close();

            return true;

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean delete(Adres inAdres) throws SQLException {
        try{
            PreparedStatement statement = this.connection.prepareStatement(
                    "DELETE FROM adres WHERE adres_id = ?;"
            );
            statement.setInt(1, inAdres.getAdres_id());


            statement.executeUpdate();
            statement.close();
            return true;
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return false;
    }

    public Adres parseStatement(ResultSet theSet) throws SQLException {
        Adres adres = new Adres();

        adres.setAdres_id(theSet.getInt("adres_id"));
        adres.setPostcode(theSet.getString("postcode"));
        adres.setHuisnummer(theSet.getString("huisnummer"));
        adres.setStraat(theSet.getString("straat"));
        adres.setWoonplaats(theSet.getString("woonplaats"));

        return adres;
    }

    @Override
    public Adres findByReiziger(Reiziger reiziger) throws SQLException {
        ArrayList<Adres> adressen = new ArrayList<Adres>();

        try {
            PreparedStatement statement = this.connection.prepareStatement("SELECT * FROM adres where reiziger_id = ?;");
            statement.setInt(1, reiziger.getId());
            ResultSet theSet = statement.executeQuery();

            if (theSet != null && theSet.next()) {
                return parseStatement(theSet);
            }

            theSet.close();
            statement.close();

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Adres findById(int id) throws SQLException {

        try {
            PreparedStatement statement = this.connection.prepareStatement("SELECT * FROM adres WHERE adres_id = ?");
            statement.setInt(1, id);
            ResultSet theSet = statement.executeQuery();

            while (theSet.next()) {
                return parseStatement(theSet);
            }

            theSet.close();
            statement.close();
        } catch (SQLException sqlex) {
            System.err.println("SQLException: " + sqlex.getMessage());
        }
        return null;
    }


    @Override
    public List<Adres> findAll() throws SQLException {
        ArrayList<Adres> adressen = new ArrayList<Adres>();

        try {
            PreparedStatement statement = this.connection.prepareStatement("SELECT * FROM adres");
            ResultSet theSet = statement.executeQuery();

            while (theSet.next()) {
                adressen.add(parseStatement(theSet));
            }

            theSet.close();
            statement.close();
        } catch (SQLException sqlex) {
            System.err.println("SQLException: " + sqlex.getMessage());
        }

        return adressen;
    }
}