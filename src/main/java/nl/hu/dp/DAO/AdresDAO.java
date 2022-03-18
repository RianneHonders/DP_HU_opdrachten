package nl.hu.dp.DAO;

import nl.hu.dp.domains.Adres;
import nl.hu.dp.domains.Reiziger;

import java.sql.SQLException;
import java.util.List;

public interface AdresDAO {
    boolean save(Adres inAdres) throws SQLException;
    boolean update(Adres inAdres) throws SQLException;
    boolean delete(Adres inAdres) throws SQLException;

    Adres findByReiziger(Reiziger reiziger) throws SQLException;
    Adres findById(int id) throws SQLException;
    List<Adres> findAll() throws SQLException;
}
