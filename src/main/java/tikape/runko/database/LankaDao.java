/*
 * Tämä luokka hoitaa javan lanka olioiden ja tietokannan väliset
 * toiminnot.
 * 
 */
package tikape.runko.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import tikape.runko.domain.Alue;
import tikape.runko.domain.Lanka;

public class LankaDao implements Dao<Lanka, Integer> {

    private Database database;
    private AlueDao aluedao;

    public LankaDao(Database database,AlueDao aluedao) {
        this.database = database;
    }

    @Override
    public Lanka findOne(Integer id) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Lanka WHERE id = ?");
        stmt.setObject(1, id);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

            int langanid = rs.getInt("id");
            Alue alue = aluedao.findOne(rs.getInt("alue"));
            String nimi = rs.getString("nimi");

        Lanka a = new Lanka(langanid,alue,nimi);

        rs.close();
        stmt.close();
        connection.close();

        return a;
    }

    
    /**
     * tämä metodi palauttaa listan kaikista tietokannan Langoista
     * @return lista kaikista langoista
     * @throws SQLException 
     */
    @Override
    public List<Lanka> findAll() throws SQLException {

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Lanka");

        ResultSet rs = stmt.executeQuery();
        List<Lanka> langat = new ArrayList<>();
        while (rs.next()) {
            int id = rs.getInt("id");
            Alue alue = aluedao.findOne(rs.getInt("alue"));
            String nimi = rs.getString("nimi");

            langat.add(new Lanka(id,alue,nimi));
        }

        rs.close();
        stmt.close();
        connection.close();

        return langat;
    }

    @Override
    public void delete(Integer id) throws SQLException {
        // ei toteutettu alkuperäisessä opiskelijaDaossa
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("DELETE FROM Lanka WHERE id = ?");
        //vaihdetaan kysymysmerkki edellisellä rivillä parametrissa saatuun mjonoon
        stmt.setObject(1, id);
        //toteutetaan SQL query
        stmt.executeQuery();
    }



}
