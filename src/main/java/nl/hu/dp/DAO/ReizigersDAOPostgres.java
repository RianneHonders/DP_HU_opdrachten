package nl.hu.dp.DAO;

import nl.hu.dp.domains.Adres;
import nl.hu.dp.domains.OVChipkaart;
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
    private OVChipkaartDAO odao;

    public ReizigersDAOPostgres(Connection inConnection) throws SQLException {
        this.connection = inConnection;
    }

    public OVChipkaartDAO getOdao() {
        return odao;
    }

    public void setOdao(OVChipkaartDAO odao) {
        this.odao = odao;
    }

    public void setAdao(AdresDAO adao) {
        this.adao = adao;
    }

    public boolean save(Reiziger inReiziger) throws SQLException {
        try {
            PreparedStatement statement = this.connection.prepareStatement(
                    "INSERT INTO reiziger (reiziger_id, voorletters, tussenvoegsel, achternaam, geboortedatum) VALUES (?,?,?,?,?)"
            );
            statement.setInt(1, inReiziger.getId());
            statement.setString(2, inReiziger.getVoorletters());
            statement.setString(3, inReiziger.getTussenvoegsel());
            statement.setString(4, inReiziger.getAchternaam());
            statement.setDate(5, inReiziger.getGeboortedatum());

            statement.executeUpdate();

            if (adao != null && inReiziger.getAdres() != null) {
                adao.save(inReiziger.getAdres());
            }

            if (odao != null && inReiziger.getOVChipkaarten() != null){
                for(OVChipkaart ovChipkaart: inReiziger.getOVChipkaarten()){
                    ovChipkaart.setReiziger(inReiziger);
                    odao.save(ovChipkaart);
                }
            }

            statement.close();

            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
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
        try {
            PreparedStatement statement = this.connection.prepareStatement("SELECT * FROM reiziger WHERE reiziger_id = ?");
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
            PreparedStatement statement = this.connection.prepareStatement("SELECT * FROM reiziger;");
            ResultSet theSet = statement.executeQuery();

            while (theSet.next()) {
                Reiziger r = new Reiziger();
                r.setId(theSet.getInt("reiziger_id"));
                r.setVoorletters(theSet.getString("voorletters"));
                r.setTussenvoegsel(theSet.getString("tussenvoegsel"));
                r.setAchternaam(theSet.getString("achternaam"));
                r.setGeboortedatum(theSet.getDate("geboortedatum"));

                if (adao != null && adao.findByReiziger(r) != null) {
                    r.setAdres(adao.findByReiziger(r));
                    r.getAdres().setReiziger(r);
                }

                if (odao != null && odao.findByReiziger(r) != null) {
                    r.setoVChipkaarten(odao.findByReiziger(r));
                    for(OVChipkaart ovChipkaart: odao.findByReiziger(r)){
                        ovChipkaart.setReiziger(r);
                    }
                }
                reizigers.add(r);
            }

            theSet.close();
            statement.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return reizigers;
    }
}
