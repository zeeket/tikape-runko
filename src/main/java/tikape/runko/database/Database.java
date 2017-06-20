package tikape.runko.database;

import java.net.URI;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private String databaseAddress;

    public Database(String databaseAddress) throws Exception {
        this.databaseAddress = databaseAddress;

        init();
    }

    public void init() {
        List<String> lauseet = null;
        if (this.databaseAddress.contains("postgres")) {
            lauseet = postgreLauseet();
        } else {
            lauseet = sqliteLauseet();
        }

        // "try with resources" sulkee resurssin automaattisesti lopuksi
        try (Connection conn = getConnection()) {
            Statement st = conn.createStatement();

            // suoritetaan komennot
            for (String lause : lauseet) {
                System.out.println("Running command >> " + lause);
                st.executeUpdate(lause);
            }

        } catch (Throwable t) {
            // jos tietokantataulu on jo olemassa, ei komentoja suoriteta
            System.out.println("Error >> " + t.getMessage());
        }
    }

    public Connection getConnection() throws SQLException {
        if (this.databaseAddress.contains("postgres")) {
            try {
                URI dbUri = new URI(databaseAddress);

                String username = dbUri.getUserInfo().split(":")[0];
                String password = dbUri.getUserInfo().split(":")[1];
                String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();

                return DriverManager.getConnection(dbUrl, username, password);
            } catch (Throwable t) {
                System.out.println("Error: " + t.getMessage());
                t.printStackTrace();
            }
        }

        return DriverManager.getConnection(databaseAddress);
    }

    private List<String> postgreLauseet() {
        ArrayList<String> lista = new ArrayList<>();

        // tietokantataulujen luomiseen tarvittavat komennot suoritusjärjestyksessä
//        lista.add("DROP TABLE Alue;");
//        lista.add("DROP TABLE Lanka;");
//        lista.add("DROP TABLE Viesti;");
        // heroku käyttää SERIAL-avainsanaa uuden tunnuksen automaattiseen luomiseen
        lista.add("CREATE TABLE lanka (id integer PRIMARY KEY, nimi varchar(100), alue integer NOT NULL, FOREIGN KEY(alue) REFERENCES alue(id));");

        lista.add("CREATE TABLE alue (id integer PRIMARY KEY, nimi varchar(50) NOT NULL, kuvaus varchar(300));");

        lista.add("CREATE TABLE viesti (id integer PRIMARY KEY, sisalto text NOT NULL, aika datetime NOT NULL, nimimerkki varchar(15) NOT NULL, lanka integer NOT NULL, FOREIGN KEY(lanka) REFERENCES lanka(id));");

        return lista;
    }

    private List<String> sqliteLauseet() {
        ArrayList<String> lista = new ArrayList<>();

        // tietokantataulujen luomiseen tarvittavat komennot suoritusjärjestyksessä
        lista.add("CREATE TABLE lanka (id integer PRIMARY KEY, nimi varchar(100), alue integer NOT NULL, FOREIGN KEY(alue) REFERENCES alue(id));");

        lista.add("CREATE TABLE alue (id integer PRIMARY KEY, nimi varchar(50) NOT NULL, kuvaus varchar(300));");

        lista.add("CREATE TABLE viesti (id integer PRIMARY KEY, sisalto text NOT NULL, aika datetime NOT NULL, nimimerkki varchar(15) NOT NULL, lanka integer NOT NULL, FOREIGN KEY(lanka) REFERENCES lanka(id));");

        return lista;
    }
}
