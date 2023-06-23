package ravil.amangeldiuly.example.minelivescoreadmin.web.tournament_type;

public class TournamentType {

    private Integer image;
    private String tournamentType;

    public TournamentType(Integer image, String tournamentType) {
        this.image = image;
        this.tournamentType = tournamentType;
    }

    public Integer getImage() {
        return image;
    }

    public void setImage(Integer image) {
        this.image = image;
    }

    public String getTournamentType() {
        return tournamentType;
    }

    public void setTournamentType(String tournamentType) {
        this.tournamentType = tournamentType;
    }
}
