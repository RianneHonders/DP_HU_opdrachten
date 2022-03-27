package nl.hu.dp.DAO;

import nl.hu.dp.domains.Adres;
import nl.hu.dp.domains.OVChipkaart;
import nl.hu.dp.domains.Product;

import java.sql.SQLException;
import java.util.List;

public interface ProductDAO {
    boolean save(Product inProduct) throws SQLException;
    boolean update(Product inProduct) throws SQLException;
    boolean delete(Product inProduct) throws SQLException;


    List<Product> findByOVChipkaart(OVChipkaart ovChipkaart);
}
