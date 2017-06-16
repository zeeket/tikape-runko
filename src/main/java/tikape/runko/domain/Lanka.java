package tikape.runko.domain;

public class Lanka {

    private Integer id;
    private Alue alue;
    private String nimi;

    public Lanka(Integer id, Alue alue, String nimi) {
        this.id = id;
        this.alue = alue;
        this.nimi = nimi;
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

}
