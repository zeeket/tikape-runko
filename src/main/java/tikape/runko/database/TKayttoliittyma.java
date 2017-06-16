/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko.database;

import tikape.runko.domain.Alue;
import tikape.runko.domain.Lanka;
import tikape.runko.domain.Viesti;


public class TKayttoliittyma {
    public static void main(String[] args) throws Exception {
        Database database = new Database("jdbc:sqlite:tietokanta.db");
        database.init();

        AlueDao aluedao = new AlueDao(database);
        LankaDao lankadao = new LankaDao(database,aluedao);
        ViestiDao viestidao = new ViestiDao(database,lankadao);

        System.out.println("Tekstikäyttöliittymä testausta varten:");
        for(Alue a : aluedao.findAll()){
            System.out.println("("+a.getId()+") "+a.getNimi()+" - "+a.getKuvaus());
            for(Lanka l : lankadao.findAll()){
                //jos lanka on alueella niin se tulostetaan
                if(l.getAlue().getId() == a.getId()){
                    System.out.println("\t("+l.getId()+") " + l.getNimi());
                    for(Viesti v : viestidao.findAll()){
                        //jos viesti on langassa niin se tulostetaan
                        if(v.getLanka().getId() == l.getId()){
                            System.out.println("\t\tAika: "+
                                    v.getAika().toString()+" id("+v.getId()+") "+v.getSisalto()+" nimimerkki: "+v.getNimimerkki());
                        }
                    }
                }
            }
        }
    }
}
