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
        this.aluedao = aluedao;
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
            Integer alueid = rs.getInt("alue");
            Alue alue = aluedao.findOne(alueid);
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

        public Lanka lisaa(String nimi, int alueId) throws SQLException {
            if(aluedao.findOne(alueId)==null){
                return null;
            }
        Connection connection = database.getConnection();
        //valitaan suurin numero id sarakkeessa
        PreparedStatement stmt = connection.prepareStatement("SELECT max(id) FROM Lanka");
        ResultSet rs = stmt.executeQuery();
        //jos taulu on tyhjä, ensimmäisen rivin id on 0
        int uudenLanganId = 0;
        //muuten inkrementoidaan yhdellä seuraavaan id lukuun
        if (rs.next()) {
            uudenLanganId = rs.getInt(1) + 1;
        }
        //lopuksi tehdään SQL update
        PreparedStatement stmt2 = connection.prepareStatement("INSERT INTO Lanka VALUES(?,?,?)");
        stmt2.setInt(1,uudenLanganId);
        stmt2.setString(2, nimi);
        stmt2.setInt(3, alueId);
        stmt2.executeUpdate();
        rs.close();
        stmt.close();
        stmt2.close();
        connection.close();
        return this.findOne(uudenLanganId);
    }


}
