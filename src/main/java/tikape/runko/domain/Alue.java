package tikape.runko.domain;

public class Alue {

    private int id;
    private String nimi;
    private String kuvaus;
    private int viesteja;
    private String viimeisin;

    public Alue(int id,String nimi, String kuvaus) {
        this.id = id;
        this.nimi= nimi;
        this.kuvaus = kuvaus;
    }

    public Alue(int id, String nimi, String kuvaus, int viesteja, String viimeisin) {
        this.id = id;
        this.nimi = nimi;
        this.kuvaus = kuvaus;
        this.viesteja = viesteja;
        this.viimeisin = viimeisin;
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

    public int getViesteja() {
        return viesteja;
    }

    public String getViimeisin() {
        return viimeisin;
    }

    public void setViesteja(int viesteja) {
        this.viesteja = viesteja;
    }

    public void setViimeisin(String viimeisin) {
        this.viimeisin = viimeisin;
    }
    
    

}
