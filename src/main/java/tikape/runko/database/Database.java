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
        // heroku käyttää SERIAL-avainsanaa uuden tunnuksen automaattiseen luomiseen
        lista.add("DROP TABLE Viesti;");
        lista.add("DROP TABLE Lanka;");
        lista.add("DROP TABLE Alue;");
        lista.add("CREATE TABLE Alue (id integer SERIAL PRIMARY KEY,nimi varchar(50) NOT NULL,kuvaus varchar(300));");
        lista.add("CREATE TABLE Lanka (id integer SERIAL PRIMARY KEY,nimi varchar(100),alue integer NOT NULL,FOREIGN KEY(alue) REFERENCES Alue(id));");
        lista.add("CREATE TABLE Viesti (id integer SERIAL PRIMARY KEY,sisalto text NOT NULL,aika datetime NOT NULL,nimimerkki varchar(15) NOT NULL,lanka integer NOT NULL,FOREIGN KEY(lanka) REFERENCES Lanka(id));");
        lista.add("INSERT INTO Alue VALUES(1,\'Musiikki\',\'Keskustelua musisoinnista ja lauluista\');");
        lista.add("INSERT INTO Alue VALUES(2,\'Finanssi\',\'Kaikenlaisia finanssiaiheita osakesijoittamisesta finanssiteorioihinkin\');");
        lista.add("INSERT INTO Alue VALUES(3,\'Aurinkolasit\',\'Siistii keskusteluu blehoist\');");
        lista.add("INSERT INTO Lanka Values(1,\'Onko The Doors vai Pink Floyd parempi?\',1);");
        lista.add("INSERT INTO Lanka Values(2,\'Kuka on lempi DJ:si?\',1);");
        lista.add("INSERT INTO Lanka Values(3,\'Ray Banit ovat ylihinnoiteltuja!\',3);");
        lista.add("INSERT INTO Viesti VALUES(0,\'Kysyn vaan koska kummatkin ovat aika suosittuja...\',\'2017-06-09 23:20:30\',\'Veera564\',1);");
        lista.add("INSERT INTO Viesti VALUES(1,\'The Doors on paras koska Riders On The Storm\',\'2017-06-10 00:58:40\',\'Kalevi\',1);");
        lista.add("INSERT INTO Viesti VALUES(2,\'Eikun Pink Floyd on ehdottomasti paras\',\'2017-06-10 01:00:00\',\'Anne\',1);");
        lista.add("INSERT INTO Viesti VALUES(3,\'Kummatkin yhtyeet ovat ihan hyvia\',\'2017-06-10 01:10:30\',\'Emil\',1);");
        lista.add("INSERT INTO Viesti VALUES(4,\'Itseni kohdalla taitaa olla Nina Kraviz <3\',\'2017-06-10 03:20:30\',\'Olli\',2);");
        lista.add("INSERT INTO Viesti VALUES(5,\'Samalla rahalla voisi ostaa monet siistit lasit vaikka Kiinasta\',\'2017-06-16 09:59:00\',\'Dundee\',3);");

        return lista;
    }

    private List<String> sqliteLauseet() {
        ArrayList<String> lista = new ArrayList<>();
        // tietokantataulujen luomiseen tarvittavat komennot suoritusjärjestyksessä
        lista.add("DROP TABLE Viesti;");
        lista.add("DROP TABLE Lanka;");
        lista.add("DROP TABLE Alue;");
        lista.add("CREATE TABLE Alue (id integer PRIMARY KEY,nimi varchar(50) NOT NULL,kuvaus varchar(300));");
        lista.add("CREATE TABLE Lanka (id integer PRIMARY KEY,nimi varchar(100),alue integer NOT NULL,FOREIGN KEY(alue) REFERENCES Alue(id));");
        lista.add("CREATE TABLE Viesti (id integer PRIMARY KEY,sisalto text NOT NULL,aika datetime NOT NULL,nimimerkki varchar(15) NOT NULL,lanka integer NOT NULL,FOREIGN KEY(lanka) REFERENCES Lanka(id));");
        lista.add("INSERT INTO Alue VALUES(1,\"Musiikki\",\"Keskustelua musisoinnista ja lauluista\");");
        lista.add("INSERT INTO Alue VALUES(2,\"Finanssi\",\"Kaikenlaisia finanssiaiheita osakesijoittamisesta finanssiteorioihinkin\");");
        lista.add("INSERT INTO Alue VALUES(3,\"Aurinkolasit\",\"Siistii keskusteluu blehoist\");");
        lista.add("INSERT INTO Lanka Values(1,\"Onko The Doors vai Pink Floyd parempi?\",1);");
        lista.add("INSERT INTO Lanka Values(2,\"Kuka on lempi DJ:si?\",1);");
        lista.add("INSERT INTO Lanka Values(3,\"Ray Banit ovat ylihinnoiteltuja!\",3);");
        lista.add("INSERT INTO Viesti VALUES(0,\"Kysyn vaan koska kummatkin ovat aika suosittuja...\",\"2017-06-09 23:20:30.000\",\"Veera564\",1);");
        lista.add("INSERT INTO Viesti VALUES(1,\"The Doors on paras koska Riders On The Storm\",\"2017-06-10 00:58:40.000\",\"Kalevi\",1);");
        lista.add("INSERT INTO Viesti VALUES(2,\"Eikun Pink Floyd on ehdottomasti paras\",\"2017-06-10 01:00:00.000\",\"Anne\",1);");
        lista.add("INSERT INTO Viesti VALUES(3,\"Kummatkin yhtyeet ovat ihan hyvia\",\"2017-06-10 01:10:30.000\",\"Emil\",1);");
        lista.add("INSERT INTO Viesti VALUES(4,\"Itseni kohdalla taitaa olla Nina Kraviz <3\",\"2017-06-10 03:20:30.000\",\"Olli\",2);");
        lista.add("INSERT INTO Viesti VALUES(5,\"Samalla rahalla voisi ostaa monet siistit lasit vaikka Kiinasta\",\"2017-06-16 09:59:00.000\",\"Dundee\",3);");
        
        return lista;
    }
}

