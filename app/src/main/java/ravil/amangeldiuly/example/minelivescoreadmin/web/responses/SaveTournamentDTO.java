package ravil.amangeldiuly.example.minelivescoreadmin.web.responses;

public class SaveTournamentDTO {

    private String tournamentName;
    private String tournamentType;
    private String tournamentLogo;
    private String location;
    private Integer teamsNum;


    public SaveTournamentDTO(String tournamentName, String tournamentType, String tournamentLogo, String location, Integer teamsNum) {
        this.tournamentName = tournamentName;
        this.tournamentType = tournamentType;
        this.tournamentLogo = tournamentLogo;
        this.location = location;
        this.teamsNum = teamsNum;
    }

    public String getTournamentName() {
        return tournamentName;
    }

    public void setTournamentName(String tournamentName) {
        this.tournamentName = tournamentName;
    }

    public String getTournamentType() {
        return tournamentType;
    }

    public void setTournamentType(String tournamentType) {
        this.tournamentType = tournamentType;
    }

    public String getTournamentLogo() {
        return tournamentLogo;
    }

    public void setTournamentLogo(String tournamentLogo) {
        this.tournamentLogo = tournamentLogo;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getTeamsNum() {
        return teamsNum;
    }

    public void setTeamsNum(Integer teamsNum) {
        this.teamsNum = teamsNum;
    }

    @Override
    public String toString() {
        return "SaveTournamentDTO{" +
                "tournamentName='" + tournamentName + '\'' +
                ", tournamentType='" + tournamentType + '\'' +
                ", tournamentLogo='" + tournamentLogo + '\'' +
                ", location='" + location + '\'' +
                ", teamsNum=" + teamsNum +
                '}';
    }
}