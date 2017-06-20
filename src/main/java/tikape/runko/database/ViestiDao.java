/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko.database;

import java.sql.Connection;
import java.sql.Timestamp;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import tikape.runko.domain.Lanka;
import tikape.runko.domain.Viesti;

public class ViestiDao implements Dao<Viesti, Integer> {

    private Database database;
    private LankaDao lankadao;

    public ViestiDao(Database database,LankaDao lankadao) {
        this.database = database;
        this.lankadao = lankadao;
    }

    @Override
    public Viesti findOne(Integer id) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM viesti WHERE id = ?");
        stmt.setObject(1, id);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

            int viestiid = rs.getInt("id");
            Lanka lanka = lankadao.findOne(rs.getInt("lanka"));
            String nimimerkki = rs.getString("nimimerkki");
            Timestamp aika = rs.getTimestamp("aika");
            String sisalto = rs.getString("sisalto");

        //Viesti(Integer id, Lanka lanka, String nimimerkki, Timestamp aika, String sisalto)
        Viesti v = new Viesti(viestiid,lanka,nimimerkki,aika,sisalto);

        rs.close();
        stmt.close();
        connection.close();

        return v;
    }

    
    /**
     * tämä metodi palauttaa listan kaikista tietokannan Viesteistä
     * @return lista kaikista langoista
     * @throws SQLException 
     */
    @Override
    public List<Viesti> findAll() throws SQLException {

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM viesti");

        ResultSet rs = stmt.executeQuery();
        List<Viesti> langat = new ArrayList<>();
        while (rs.next()) {
            int id = rs.getInt("id");
            Lanka lanka = lankadao.findOne(rs.getInt("lanka"));
            String nimimerkki = rs.getString("nimimerkki");
            Timestamp aika = rs.getTimestamp("aika");
            String sisalto = rs.getString("sisalto");

            //Viesti(Integer id, Lanka lanka, String nimimerkki, Timestamp aika, String sisalto)
            langat.add(new Viesti(id,lanka,nimimerkki,aika,sisalto));
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
        PreparedStatement stmt = connection.prepareStatement("DELETE FROM viesti WHERE id = ?");
        //vaihdetaan kysymysmerkki edellisellä rivillä parametrissa saatuun mjonoon
        stmt.setObject(1, id);
        //toteutetaan SQL query
        stmt.executeQuery();
    }
    
    /**
     * metodi palauttaa uusimman Timestamp:in viestilistasta
     * @param viestit lista viesteistä josta halutaan löytää uusin Timestamp
     * @return most recent Timestamp
     */
    public Timestamp findNewestTimestamp(List<Viesti> viestit){
        if (viestit.isEmpty()){
            return null;
        }
        Timestamp uusin = new Timestamp((long)0);
        for (Viesti v : viestit){
            if (v.getAika().after(uusin)){
                    uusin=v.getAika();
            }
        }
        if(uusin.equals(new Timestamp((long)0))){
            /** jos jostain syystä mikään 
             * parametri listan viesti ei ole
             * 1.1.1970 jälkeen, palautetaan null
             */
            return null;
        }
        return uusin;
    }
    
      public Viesti lisaa(String sisalto, String nimimerkki, int lankaId) throws SQLException {
            if(lankadao.findOne(lankaId)==null){
                return null;
            }
        Connection connection = database.getConnection();
        //valitaan suurin numero id sarakkeessa
        PreparedStatement stmt = connection.prepareStatement("SELECT max(id) FROM viesti");
        ResultSet rs = stmt.executeQuery();
        //jos taulu on tyhjä, ensimmäisen rivin id on 0
        int uudenViestinId = 0;
        //muuten inkrementoidaan yhdellä seuraavaan id lukuun
        if (rs.next()) {
            uudenViestinId = rs.getInt(1) + 1;
        }
        String lahetysAika = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssX").format(new Date());
        PreparedStatement stmt2 = connection.prepareStatement("Insert INTO viesti VALUES(?,?,?,?,?)");
        stmt2.setInt(1, uudenViestinId);
        stmt2.setString(2,sisalto);
        stmt2.setString(3,lahetysAika);
        stmt2.setString(4, nimimerkki);
        stmt2.setInt(5, lankaId);
        
        
                
                //+ uudenViestinId + "," + sisalto + "," +lahetysAika+","+ nimimerkki+","+lankaId + ")";
        stmt2.executeUpdate();
        rs.close();
        stmt.close();
        stmt2.close();
        connection.close();
        return this.findOne(uudenViestinId);
    }
      
          public List<Viesti> findAllIn(int lankaId) throws SQLException {

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM viesti WHERE lanka = ?");
        stmt.setInt(1, lankaId);
        ResultSet rs = stmt.executeQuery();
        List<Viesti> viestit = new ArrayList<>();
        while (rs.next()) {
            int id = rs.getInt("id");
            Lanka lanka = lankadao.findOne(rs.getInt("lanka"));
            String nimimerkki = rs.getString("nimimerkki");
            Timestamp aika = rs.getTimestamp("aika");
            String sisalto = rs.getString("sisalto");
            //public Viesti(Integer id, Lanka lanka, String nimimerkki, Timestamp aika, String sisalto)
            viestit.add(new Viesti(id, lanka, nimimerkki, aika, sisalto));
        }

        rs.close();
        stmt.close();
        connection.close();
        return viestit;
    }


}
