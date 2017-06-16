package tikape.runko.domain;

import java.sql.Date;

public class Viesti {

    private Integer id;
    private Lanka lanka;
    private String nimimerkki;
    private Date aika;
    private String sisalto;
    

    public Viesti(Integer id, Lanka lanka, String nimimerkki, Date aika, String sisalto) {
        this.id = id;
        this.lanka = lanka;
        this.nimimerkki = nimimerkki;
        this.aika = aika;
        this.sisalto = sisalto;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNimimerkki() {
        return nimimerkki;
    }

    public void setNimimerkki(String nimimerkki) {
        this.nimimerkki = nimimerkki;
    }
    
    public Lanka getLanka(){
        return this.lanka;
    }
    
    public void setLanka(Lanka lanka){
        this.lanka=lanka;
    }
    
    public Date getAika(){
        return this.aika;
    }
    
    public void setAika(Date uusiAika){
        this.aika = uusiAika;
    }
    
    public String getSisalto(){
        return this.sisalto;
    }
    
    public void setSisalto(String uusiSisalto){
        this.sisalto = uusiSisalto;
    }

}
