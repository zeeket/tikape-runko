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
import java.util.ArrayList;
import java.util.List;
import tikape.runko.domain.Alue;

public class AlueDao implements Dao<Alue, String> {

    private Database database;

    public AlueDao(Database database) {
        this.database = database;
    }

    public Alue findOne(String nimi) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Alue WHERE nimi = ?");
        stmt.setObject(1, nimi);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        String alueennimi = rs.getString("nimi");
        String kuvaus = rs.getString("kuvaus");

        Alue a = new Alue(alueennimi,kuvaus);

        rs.close();
        stmt.close();
        connection.close();

        return a;
    }

    
    /**
     * tämä metodi palauttaa listan kaikista tietokannan Alueista
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
            String nimi = rs.getString("nimi");
            String kuvaus = rs.getString("kuvaus");

            alueet.add(new Alue(nimi, kuvaus));
        }

        rs.close();
        stmt.close();
        connection.close();

        return alueet;
    }

    public void delete(String nimi) throws SQLException {
        // ei toteutettu alkuperäisessä opiskelijaDaossa
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("DELETE FROM Alue WHERE nimi = ?");
        //vaihdetaan kysymysmerkki edellisellä rivillä parametrissa saatuun mjonoon
        stmt.setObject(1, nimi);
        //toteutetaan SQL query
        stmt.executeQuery();
    }


}
