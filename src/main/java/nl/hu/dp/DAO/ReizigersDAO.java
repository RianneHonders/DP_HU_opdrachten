package nl.hu.dp.DAO;

import nl.hu.dp.domains.Reiziger;
import java.sql.SQLException;
import java.util.List;

public interface ReizigersDAO {
    boolean save(Reiziger inReiziger) throws SQLException;
    boolean update(Reiziger inReiziger) throws SQLException;
    boolean delete(Reiziger inReiziger) throws SQLException;

    List<Reiziger> findAll() throws SQLException;
    Reiziger findById(int id) throws SQLException;
    Reiziger findByGbdatum(String datum) throws SQLException;
}
