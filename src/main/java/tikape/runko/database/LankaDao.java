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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import tikape.runko.domain.Alue;
import tikape.runko.domain.Lanka;

public class LankaDao implements Dao<Lanka, Integer> {

    private Database database;
    private AlueDao aluedao;

    public LankaDao(Database database, AlueDao aluedao) {
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

        Lanka a = new Lanka(langanid, alue, nimi);

        rs.close();
        stmt.close();
        connection.close();

        return a;
    }

    /**
     * tämä metodi palauttaa listan kaikista tietokannan Langoista
     *
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

            langat.add(new Lanka(id, alue, nimi));
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
        stmt.close();
        connection.close();
    }

    public Lanka lisaa(String nimi, int alueId) throws SQLException {
        if (aluedao.findOne(alueId) == null) {
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
        stmt2.setInt(1, uudenLanganId);
        stmt2.setString(2, nimi);
        stmt2.setInt(3, alueId);
        stmt2.executeUpdate();
        rs.close();
        stmt.close();
        stmt2.close();
        connection.close();
        return this.findOne(uudenLanganId);
    }

    
    /**
     * metodi joka palauttaa listan lanka olioita joilla on myös viestimäärä ja viimeisimmän viestin aika.
     * @param alueId alueenid josta halutaaan lötää kaikki langat
     * @return lista langoista
     * @throws SQLException 
     */
    public List<Lanka> findAllIn(int alueId) throws SQLException {

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM lanka WHERE alue = ?");
        stmt.setInt(1, alueId);
        ResultSet rs = stmt.executeQuery();
        List<Lanka> langat = new ArrayList<>();
        while (rs.next()) {
            int id = rs.getInt("id");
            Integer alueid = rs.getInt("alue");
            Alue alue = aluedao.findOne(alueid);
            String nimi = rs.getString("nimi");
            int viesteja = this.montakoViestia(id);
            String viimeisin = this.uusinViesti(id);
            langat.add(new Lanka(id, alue, nimi, viesteja, viimeisin));
        }

        rs.close();
        stmt.close();
        connection.close();
        return langat;
    }

    /**
     * metodi palauttaa halutun langan viestimäärän
     *
     * @param id langan id
     * @return langan viestimäärä
     * @throws SQLException
     */
    public int montakoViestia(Integer id) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT COUNT(*) AS summa FROM viesti JOIN lanka ON viesti.lanka=lanka.id WHERE lanka.id = ?");
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        //SELECT * FROM Alue Viesti INNER JOIN Lanka ON Viesti.lanka=Lanka.id WHERE Lanka.alue = Alue.id
        int palautettava = 0;
        if (rs.next()) {
            palautettava = rs.getInt("summa");
        }
        rs.close();
        stmt.close();
        connection.close();
        return palautettava;
    }

    /**
     *
     * @param id langan id
     * @return langan uusimman viestin aika mjonona
     * @throws SQLException
     */
    public String uusinViesti(Integer id) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT MAX(viesti.aika) AS viimeisin FROM viesti JOIN lanka ON viesti.lanka=lanka.id WHERE lanka.id = ?");
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        //SELECT * FROM Alue Viesti INNER JOIN Lanka ON Viesti.lanka=Lanka.id WHERE Lanka.alue = Alue.id
        String palautettava = "ei koskaan";
        if (rs.next() && rs.getTimestamp("viimeisin") != null) {
            palautettava = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(rs.getTimestamp("viimeisin"));
        }
        rs.close();
        stmt.close();
        connection.close();
        return palautettava;
    }

}
