package tikape.runko.domain;

public class Alue {

    private int id;
    private String nimi;
    private String kuvaus;

    public Alue(int id,String nimi, String kuvaus) {
        this.id = id;
        this.nimi= nimi;
        this.kuvaus = kuvaus;
    }
    
    public int getId(){
        return this.id;
    }

    public String getNimi() {
        return nimi;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    public String getKuvaus() {
        return kuvaus;
    }

    public void setKuvaus(String kuvaus) {
        this.kuvaus = kuvaus;
    }

}
