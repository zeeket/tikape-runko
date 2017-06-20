package tikape.runko.domain;

public class Lanka {

    private Integer id;
    private Alue alue;
    private String nimi;
    private int viesteja;
    private String viimeisin;

    public Lanka(Integer id, Alue alue, String nimi) {
        this.id = id;
        this.alue = alue;
        this.nimi = nimi;
    }
    
    public Lanka(Integer id,Alue alue,String nimi,int viesteja,String viimeisin) {
        this.id=id;
        this.alue=alue;
        this.nimi=nimi;
        this.viesteja=viesteja;
        this.viimeisin=viimeisin;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNimi() {
        return nimi;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }
    
    public Alue getAlue(){
        return this.alue;
    }
    
    public void setAlue(Alue alue){
        this.alue=alue;
    }

    public void setViimeisin(String viimeisin) {
        this.viimeisin = viimeisin;
    }

    public void setViesteja(int viesteja) {
        this.viesteja = viesteja;
    }

    public String getViimeisin() {
        return viimeisin;
    }

    public int getViesteja() {
        return viesteja;
    }

}
