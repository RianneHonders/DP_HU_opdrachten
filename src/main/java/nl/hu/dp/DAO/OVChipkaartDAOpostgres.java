package nl.hu.dp.DAO;

import nl.hu.dp.domains.OVChipkaart;
import nl.hu.dp.domains.Product;
import nl.hu.dp.domains.Reiziger;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OVChipkaartDAOpostgres implements OVChipkaartDAO {
    private Connection connection = null;
    private ReizigersDAO rdao;
    private ProductDAO pdao;

    public OVChipkaartDAOpostgres(Connection inConnection) throws SQLException {
        this.connection = inConnection;
    }

    public void setRdao(ReizigersDAO rdao) {
        this.rdao = rdao;
    }

    public void setPdao(ProductDAO pdao) {
        this.pdao = pdao;
    }

    public boolean save(OVChipkaart ovKaart) throws SQLException {
            OVChipkaart ovChipkaart = new OVChipkaart();

            try{
                PreparedStatement preparedStatement =  connection.prepareStatement("INSERT INTO ov_chipkaart (kaart_nummer, saldo, geldig_tot, klasse, reiziger_id) VALUES (?,?,?,?,?)");
                preparedStatement.setInt(1, ovKaart.getKaart_nummer());
                preparedStatement.setDouble(2, ovKaart.getSaldo());
                preparedStatement.setDate(3, ovKaart.getGeldig_tot());
                preparedStatement.setInt(4, ovKaart.getKlasse());
                preparedStatement.setInt(5, ovKaart.getReiziger().getId());

                preparedStatement.executeUpdate();

                for (Product product : ovKaart.getProducten()) {
                    PreparedStatement statement = connection.prepareStatement("INSERT INTO ov_chipkaart_product (kaart_nummer, product_nummer, status, last_update) VALUES (?, ?, ?, ?)");
                    statement.setInt(1, ovKaart.getKaart_nummer());
                    statement.setInt(2, product.getProduct_nummer());
                    statement.setString(3, "actief");
                    statement.setDate(4, Date.valueOf(LocalDate.now()));

                    statement.executeUpdate();
                    statement.close();
                }

                preparedStatement.close();
                return true;

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            return false;
    }


    public boolean update(OVChipkaart ovKaart) throws SQLException {
        OVChipkaart ovChipkaart = null;
        try{
            PreparedStatement statement = this.connection.prepareStatement(
                    "UPDATE ov_chipkaart SET kaart_nummer = ?, geldig_tot = ?, klasse =? , saldo = ? WHERE kaartnummer = ?");
            statement.setInt(1, ovKaart.getKaart_nummer());
            statement.setDate(2, ovKaart.getGeldig_tot());
            statement.setInt(3, ovKaart.getKlasse());
            statement.setDouble(4, ovKaart.getSaldo());
            statement.setInt(5, ovKaart.getKaart_nummer());

            statement.executeUpdate();

            PreparedStatement statement3 = this.connection.prepareStatement(
                    "DELETE FROM ov_chipkaart_product WHERE kaart_nummer = ?");
            statement3.setInt(1, ovKaart.getKaart_nummer());
            statement3.executeQuery();

            for (Product product : ovChipkaart.getProducten()) {
                PreparedStatement statement2 = connection.prepareStatement(
                        "INSERT INTO ov_chipkaart_product (kaart_nummer, product_nummer, status, last_update) VALUES (?,?,?,?)");
                statement2.setInt(1, ovChipkaart.getKaart_nummer());
                statement2.setInt(2, product.getProduct_nummer());
                statement2.setString(3, "actief");
                statement2.setDate(4, Date.valueOf(LocalDate.now()));

                statement2.executeUpdate();
            }

            statement.close();
            statement3.close();

            return true;

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return false;
    }


    public boolean delete(OVChipkaart ovKaart) throws SQLException {
        OVChipkaart ovChipkaart = null;

        try{
            PreparedStatement statement1 =  connection.prepareStatement(
                    "DELETE FROM ov_chipkaart_product WHERE kaart_nummer = ?");
            statement1.setInt(1, ovKaart.getKaart_nummer());

            PreparedStatement statement2 =  connection.prepareStatement(
                    "DELETE FROM ov_chipkaart WHERE kaart_nummer = ?");
            statement2.setInt(1, ovKaart.getKaart_nummer());



            statement1.executeQuery();
            statement2.executeQuery();

            statement1.close();
            statement2.close();

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
            PreparedStatement preparedStatement =  connection.prepareStatement(
                    "SELECT * FROM ov_chipkaart WHERE reiziger_id = ?;");
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
            PreparedStatement preparedStatement =  connection.prepareStatement(
                    "SELECT * FROM ov_chipkaart WHERE kaart_nummer = ?;");
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

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM ov_chipkaart;");
            ResultSet theSet = preparedStatement.executeQuery();

            ArrayList<OVChipkaart> ovChipkaarten = new ArrayList<OVChipkaart>();

            while (theSet != null && theSet.next()) {
                OVChipkaart ov = new OVChipkaart();
                ov.setKaart_nummer(theSet.getInt("kaart_nummer"));
                ov.setGeldig_tot(theSet.getDate("geldig_tot"));
                ov.setKlasse(theSet.getInt("klasse"));
                ov.setSaldo(theSet.getFloat("saldo"));

                List<Product> pr = pdao.findByOVChipkaart(ov);
                ov.setProducten(pr);
                ovChipkaarten.add(ov);

            }
            theSet.close();
            return ovChipkaarten;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }
    }

