package nl.hu.dp.DAO;

import nl.hu.dp.domains.OVChipkaart;
import nl.hu.dp.domains.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOPostgres implements ProductDAO{
    private Connection connection = null;
    private OVChipkaartDAO odao;

    public ProductDAOPostgres(Connection inConnection) throws SQLException {
        this.connection = inConnection;
    }

    public void setOdao(OVChipkaartDAO odao) {
        this.odao = odao;
    }

    @Override
    public boolean save(Product inProduct) throws SQLException {

        try{
            PreparedStatement preparedStatement =  connection.prepareStatement(
                    "INSERT INTO product (product_nummer , naam, beschrijving , prijs) VALUES (?,?,?,?)");
            preparedStatement.setInt(1, inProduct.getProduct_nummer());
            preparedStatement.setString(2, inProduct.getNaam());
            preparedStatement.setString(3, inProduct.getBeschijving());
            preparedStatement.setDouble(4, inProduct.getPrijs());

            preparedStatement.executeUpdate();
            preparedStatement.close();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean update(Product inProduct) throws SQLException {

        try{
            PreparedStatement statement = this.connection.prepareStatement(
                    "UPDATE product SET product_nummer = ?, naam = ? , beschrijving = ? , prijs = ? WHERE product_nummer = ?");
            statement.setInt(1, inProduct.getProduct_nummer());
            statement.setString(2, inProduct.getNaam());
            statement.setString(3, inProduct.getBeschijving());
            statement.setDouble(4, inProduct.getPrijs());
            statement.setInt(5, inProduct.getProduct_nummer());

            statement.executeUpdate();
            statement.close();

            return true;

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean delete(Product inProduct) throws SQLException {
        OVChipkaart Product = null;
        try{
            PreparedStatement statement1 =  connection.prepareStatement(
                    "DELETE FROM ov_chipkaart_product WHERE product_nummer = ?");
            PreparedStatement statement2 =  connection.prepareStatement(
                    "DELETE FROM product WHERE product_nummer = ?");


            statement1.setInt(1, inProduct.getProduct_nummer());
            statement2.setInt(1, inProduct.getProduct_nummer());

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

    public List<Product> findByOVChipkaart(OVChipkaart ovChipkaart) {
        String findByOV = "SELECT ov_chipkaart_product.kaart_nummer, product.product_nummer, product.naam, product.beschrijving, product.prijs " +
                "FROM product " +
                "JOIN ov_chipkaart_product " +
                "ON ov_chipkaart_product.product_nummer = product.product_nummer " +
                "WHERE ov_chipkaart_product.kaart_nummer = ? " +
                "ORDER BY kaart_nummer, product_nummer";

        try {
            PreparedStatement statement = connection.prepareStatement(findByOV);
            statement.setInt(1, ovChipkaart.getKaart_nummer());
            ResultSet theSet = statement.executeQuery();
            List<Product> producten = new ArrayList<>();

            while (theSet.next()) {
                int pnr = theSet.getInt("product_nummer");
                String nm = theSet.getString("naam");
                String b = theSet.getString("beschrijving");
                double pr = theSet.getDouble("prijs");

                Product product = new Product(pnr, nm, b, pr);
                product.addOVChipkaart(ovChipkaart);
                producten.add(product);
            }
            return producten;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public List<Product> findAll(){
        String findAllSQL = "SELECT ov_chipkaart_product.kaart_nummer, ov_chipkaart.geldig_tot, ov_chipkaart.klasse, ov_chipkaart.saldo, product.product_nummer, product.naam, product.beschrijving, product.prijs " +
                "FROM product " +
                "JOIN ov_chipkaart_product " +
                "ON ov_chipkaart_product.product_nummer = product.product_nummer " +
                "JOIN ov_chipkaart " +
                "ON ov_chipkaart_product.kaart_nummer = ov_chipkaart.kaart_nummer " +
                "ORDER BY kaart_nummer, product_nummer;";

        try {
            PreparedStatement statement = connection.prepareStatement(findAllSQL);

            ResultSet theSet = statement.executeQuery();

            List<Product> producten = new ArrayList<>();
            while(theSet.next()) {
                int pnr = theSet.getInt("product_nummer");
                String nm = theSet.getString("naam");
                String b = theSet.getString("beschrijving");
                double pr = theSet.getDouble("prijs");

                int knm = theSet.getInt("kaart_nummer");
                Date gTot = theSet.getDate("geldig_tot");
                int kl = theSet.getInt("klasse");
                double sal = theSet.getDouble("saldo");

                OVChipkaart ovChipkaart = new OVChipkaart(knm, gTot, kl, sal);
                Product product = new Product(pnr, nm, b, pr);

                for(Product p : producten){
                    if(p.getProduct_nummer() == product.getProduct_nummer()){
                        p.addOVChipkaart(ovChipkaart);
                    }
                }
                product.addOVChipkaart(ovChipkaart);
                producten.add(product);
            }

            return producten;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }
}
