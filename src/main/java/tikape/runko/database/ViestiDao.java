/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko.database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Viesti WHERE id = ?");
        stmt.setObject(1, id);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

            int viestiid = rs.getInt("id");
            Lanka lanka = lankadao.findOne(rs.getInt("lanka"));
            String nimimerkki = rs.getString("nimimerkki");
            Date aika = rs.getDate("aika");
            String sisalto = rs.getString("sisalto");

        //Viesti(Integer id, Lanka lanka, String nimimerkki, Date aika, String sisalto)
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
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Viesti");

        ResultSet rs = stmt.executeQuery();
        List<Viesti> langat = new ArrayList<>();
        while (rs.next()) {
            int id = rs.getInt("id");
            Lanka lanka = lankadao.findOne(rs.getInt("lanka"));
            String nimimerkki = rs.getString("nimimerkki");
            Date aika = rs.getDate("aika");
            String sisalto = rs.getString("sisalto");

            //Viesti(Integer id, Lanka lanka, String nimimerkki, Date aika, String sisalto)
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
        PreparedStatement stmt = connection.prepareStatement("DELETE FROM Viesti WHERE id = ?");
        //vaihdetaan kysymysmerkki edellisellä rivillä parametrissa saatuun mjonoon
        stmt.setObject(1, id);
        //toteutetaan SQL query
        stmt.executeQuery();
    }
    
    /**
     * metodi palauttaa uusimman Date:in viestilistasta
     * @param viestit lista viesteistä josta halutaan löytää uusin Date
     * @return most recent Date
     */
    public Date findNewestDate(List<Viesti> viestit){
        if (viestit.isEmpty()){
            return null;
        }
        Date uusin = new Date((long)0);
        for (Viesti v : viestit){
            if (v.getAika().after(uusin)){
                    uusin=v.getAika();
            }
        }
        if(uusin.equals(new Date((long)0))){
            /** jos jostain syystä mikään 
             * parametri listan viesti ei ole
             * 1.1.1970 jälkeen, palautetaan null
             */
            return null;
        }
        return uusin;
    }


}
