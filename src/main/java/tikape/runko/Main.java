package tikape.runko;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.AlueDao;
import tikape.runko.database.Database;
import tikape.runko.database.LankaDao;
import tikape.runko.database.ViestiDao;

public class Main {

    public static void main(String[] args) throws Exception {
        
                // asetetaan portti jos heroku antaa PORT-ympäristömuuttujan
        if (System.getenv("PORT") != null) {
            port(Integer.valueOf(System.getenv("PORT")));
        }
        // käytetään oletuksena paikallista sqlite-tietokantaa
        String jdbcOsoite = "jdbc:sqlite:tietokanta.db";
        // jos heroku antaa käyttöömme tietokantaosoitteen, otetaan se käyttöön
        if (System.getenv("DATABASE_URL") != null) {
            jdbcOsoite = System.getenv("DATABASE_URL");
        } 
        Database database = new Database(jdbcOsoite);
        database.init();

        AlueDao aluedao = new AlueDao(database);
        LankaDao lankadao = new LankaDao(database,aluedao);
        ViestiDao viestidao = new ViestiDao(database,lankadao);


        get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("alueet", aluedao.findAll());

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());
    

        post("/alueenlisays", (req, res) -> {
            String nimi = req.queryParams("nimi");
            aluedao.lisaa(nimi, "html sivusta lisatty alue");
            res.redirect("/");
            return "";
        });

            get("/alue/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("alue", aluedao.findOne(Integer.parseInt(req.params("id"))));
            map.put("langat", lankadao.findAllIn(Integer.parseInt(req.params("id"))));

            return new ModelAndView(map, "alue");
        }, new ThymeleafTemplateEngine());
}
}
