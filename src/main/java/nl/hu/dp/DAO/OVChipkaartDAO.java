package nl.hu.dp.DAO;

import nl.hu.dp.domains.Adres;
import nl.hu.dp.domains.OVChipkaart;
import nl.hu.dp.domains.Reiziger;

import java.sql.SQLException;
import java.util.List;

public interface OVChipkaartDAO {
    boolean update(OVChipkaart inOVChipkaart) throws SQLException;
    boolean save(OVChipkaart inOVChipkaart) throws SQLException;
    boolean delete(OVChipkaart inOVChipkaart) throws SQLException;

    List<OVChipkaart> findAll() throws SQLException;
    List<OVChipkaart> findByReiziger(Reiziger reiziger) throws SQLException;
    OVChipkaart findById(int id) throws SQLException;
}
