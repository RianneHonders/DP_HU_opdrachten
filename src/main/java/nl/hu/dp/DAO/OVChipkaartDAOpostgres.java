package nl.hu.dp.DAO;

import nl.hu.dp.domains.Adres;
import nl.hu.dp.domains.OVChipkaart;
import nl.hu.dp.domains.Reiziger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OVChipkaartDAOpostgres implements OVChipkaartDAO {
    private Connection connection = null;
    private ReizigersDAO rdao;

    public OVChipkaartDAOpostgres(Connection inConnection) throws SQLException {
        this.connection = inConnection;
    }

    public ReizigersDAO getRdao() {
        return rdao;
    }

    public void setRdao(ReizigersDAO rdao) {
        this.rdao = rdao;
    }

    public boolean save(OVChipkaart ovKaart) throws SQLException {
            OVChipkaart ovChipkaart = null;
            try{
                PreparedStatement preparedStatement =  connection.prepareStatement("INSERT INTO ov_chipkaart (kaart_nummer, saldo, geldig_tot, klasse, reiziger_id) VALUES (?,?,?,?,?);");
                preparedStatement.setInt(1, ovKaart.getKaart_nummer());
                preparedStatement.setDouble(2, ovKaart.getSaldo());
                preparedStatement.setDate(3, ovKaart.getGeldig_tot());
                preparedStatement.setInt(4, ovKaart.getKlasse());
                preparedStatement.setInt(5, ovKaart.getReiziger().getId());
                preparedStatement.executeQuery();
                preparedStatement.close();
                return true;
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            return false;
    }


    public boolean update(OVChipkaart ovKaart) throws SQLException {
        try{
            PreparedStatement statement = this.connection.prepareStatement(
                    "UPDATE ov_chipkaart SET kaart_nummer = ?, geldig_tot = ?, klasse =? , saldo = ? WHERE kaartnummer = ?;"
            );
            statement.setInt(1, ovKaart.getKaart_nummer());
            statement.setDate(2, ovKaart.getGeldig_tot());
            statement.setInt(3, ovKaart.getKlasse());
            statement.setDouble(4, ovKaart.getSaldo());
            statement.setInt(5, ovKaart.getKaart_nummer());


            statement.executeUpdate();
            statement.close();

            return true;

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return false;
    }


    public boolean delete(OVChipkaart ovKaart) throws SQLException {
        OVChipkaart ovChipkaart = null;
        try{
            PreparedStatement preparedStatement =  connection.prepareStatement("DELETE FROM ov_chipkaart WHERE kaart_nummer = ?;");
            preparedStatement.setInt(1, ovKaart.getKaart_nummer());

            preparedStatement.executeQuery();
            preparedStatement.close();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public OVChipkaart parseStatement(ResultSet theSet) throws SQLException {
        OVChipkaart ovChipkaart = new OVChipkaart();

        ovChipkaart.setKaart_nummer(theSet.getInt("kaart_nummer"));
        ovChipkaart.setGeldig_tot(theSet.getDate("geldig_tot"));
        ovChipkaart.setKlasse(theSet.getInt("klasse"));
        ovChipkaart.setSaldo(theSet.getFloat("saldo"));

        return ovChipkaart;
    }


    public List<OVChipkaart> findByReiziger(Reiziger reiziger) throws SQLException {
        ArrayList<OVChipkaart> ovChipkaarten = new ArrayList<OVChipkaart>();
        try{
            PreparedStatement preparedStatement =  connection.prepareStatement("SELECT * FROM ov_chipkaart WHERE reiziger_id = ?;");
            preparedStatement.setInt(1, reiziger.getId());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet != null && resultSet.next()){
                ovChipkaarten.add(parseStatement(resultSet));
            }

            resultSet.close();
            return ovChipkaarten;
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return null;
    }


    public OVChipkaart findById(int id) throws SQLException {
        OVChipkaart ovChipkaart = null;
        try{
            PreparedStatement preparedStatement =  connection.prepareStatement("SELECT * FROM ov_chipkaart WHERE kaart_nummer = ?;");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet != null && resultSet.next()){
                ovChipkaart = (parseStatement(resultSet));
            }

            resultSet.close();
            return ovChipkaart;
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return null;
    }


    public List<OVChipkaart> findAll() throws SQLException {
        ArrayList<OVChipkaart> ovChipkaarten = new ArrayList<OVChipkaart>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM ov_chipkaart;");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet != null && resultSet.next()) {
                ovChipkaarten.add(parseStatement(resultSet));
            }

            resultSet.close();
            return ovChipkaarten;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }
    }

