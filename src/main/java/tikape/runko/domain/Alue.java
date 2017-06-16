package tikape.runko.domain;

public class Alue {

    private String nimi;
    private String kuvaus;

    public Alue(String nimi, String kuvaus) {
        this.nimi= nimi;
        this.kuvaus = nimi;
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
