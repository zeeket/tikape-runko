/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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

public class AlueDao implements Dao<Alue, Integer> {

    private Database database;

    public AlueDao(Database database) {
        this.database = database;
    }

    @Override
    public Alue findOne(Integer id) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Alue WHERE id = ?");
        stmt.setObject(1, id);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        int alueenid = rs.getInt("id");
        String alueennimi = rs.getString("nimi");
        String kuvaus = rs.getString("kuvaus");

        Alue a = new Alue(alueenid, alueennimi, kuvaus);

        rs.close();
        stmt.close();
        connection.close();

        return a;
    }

    /**
     * tämä metodi palauttaa listan kaikista tietokannan Alueista
     *
     * @return lista kaikista alueista
     * @throws SQLException
     */
    @Override
    public List<Alue> findAll() throws SQLException {

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Alue");
        ResultSet rs = stmt.executeQuery();
        List<Alue> alueet = new ArrayList<>();
        while (rs.next()) {
            int id = rs.getInt("id");
            String nimi = rs.getString("nimi");
            String viimeisin = this.uusinViesti(id);
            int viesteja = this.montakoViestia(id);
            alueet.add(new Alue(id, nimi,"kuvaus",viesteja,viimeisin));
        }
        
        rs.close();
        stmt.close();
        connection.close();

        return alueet;
    }
    
    public int montakoViestia(Integer id) throws SQLException{
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT COUNT(*) AS summa FROM Viesti JOIN Lanka ON Viesti.lanka=Lanka.id WHERE Lanka.alue = ?");
        stmt.setInt(1,id);
        ResultSet rs = stmt.executeQuery();
        //SELECT * FROM Alue Viesti INNER JOIN Lanka ON Viesti.lanka=Lanka.id WHERE Lanka.alue = Alue.id
        int palautettava=0;
        if(rs.next()){
            palautettava=rs.getInt("summa");
        }
        rs.close();
        stmt.close();
        connection.close();
                return palautettava;
    }
    
        public String uusinViesti(Integer id) throws SQLException{
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT MAX(Viesti.aika) AS viimeisin FROM Viesti JOIN Lanka ON Viesti.lanka=Lanka.id WHERE Lanka.alue = ?");
        stmt.setInt(1,id);
        ResultSet rs = stmt.executeQuery();
        //SELECT * FROM Alue Viesti INNER JOIN Lanka ON Viesti.lanka=Lanka.id WHERE Lanka.alue = Alue.id
        String palautettava="ei koskaan";
        if(rs.next()&&rs.getTimestamp("viimeisin")!=null){
            palautettava=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(rs.getTimestamp("viimeisin"));
        }
        rs.close();
        stmt.close();
        connection.close();
                return palautettava;
    }

    @Override
    public void delete(Integer id) throws SQLException {
        //ei tee mitään jos paramissa annettua id:tä ei löydy
        if (this.findOne(id) != null) {
            Connection connection = database.getConnection();
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM Alue WHERE id = ?");
            //vaihdetaan kysymysmerkki edellisellä rivillä parametrissa saatuun kokonaislukuun
            stmt.setObject(1, id);
            //toteutetaan SQL query
            stmt.executeQuery();
            stmt.close();
            connection.close();

        }
    }

    public Alue lisaa(String nimi, String kuvaus) throws SQLException {
        if(nimi.isEmpty()){
            return null;
        } 
        Connection connection = database.getConnection();
        //valitaan suurin numero id sarakkeessa
        PreparedStatement stmt = connection.prepareStatement("SELECT max(id) FROM Alue");
        ResultSet rs = stmt.executeQuery();
        //jos taulu on tyhjä, ensimmäisen rivin id on 0
        int uudenAlueenId = 0;
        //muuten inkrementoidaan yhdellä seuraavaan id lukuun
        if (rs.next()) {
            uudenAlueenId = rs.getInt(1) + 1;
        }
        //Lopuksi tehdään SQL update
        PreparedStatement stmt2 = connection.prepareStatement("INSERT INTO Alue VALUES(?,?,?)");
        stmt2.setInt(1, uudenAlueenId);
        stmt2.setString(2, nimi);
        stmt2.setString(3, kuvaus);
        stmt2.executeUpdate();
        rs.close();
        stmt.close();
        stmt2.close();
        connection.close();
        return this.findOne(uudenAlueenId);
    }

}
