package ravil.amangeldiuly.example.minelivescoreadmin.web.responses;

public class SaveCupTournamentDTO {

    private String tournamentName;
    private String tournamentType;
    private String tournamentLogo;
    private String location;
    private Integer teamsNum;
    private String isPlayOf;

    public SaveCupTournamentDTO(String tournamentName, String tournamentType, String tournamentLogo, String location, Integer teamsNum, String isPlayOf) {
        this.tournamentName = tournamentName;
        this.tournamentType = tournamentType;
        this.tournamentLogo = tournamentLogo;
        this.location = location;
        this.teamsNum = teamsNum;
        this.isPlayOf = isPlayOf;
    }

    public String getTournamentName() {
        return tournamentName;
    }

    public String getTournamentType() {
        return tournamentType;
    }

    public String getTournamentLogo() {
        return tournamentLogo;
    }

    public String getLocation() {
        return location;
    }

    public Integer getTeamsNum() {
        return teamsNum;
    }


    public void setTournamentName(String tournamentName) {
        this.tournamentName = tournamentName;
    }

    public void setTournamentType(String tournamentType) {
        this.tournamentType = tournamentType;
    }

    public void setTournamentLogo(String tournamentLogo) {
        this.tournamentLogo = tournamentLogo;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setTeamsNum(Integer teamsNum) {
        this.teamsNum = teamsNum;
    }

    public String getIsPlayOf() {
        return isPlayOf;
    }

    public void setIsPlayOf(String isPlayOf) {
        this.isPlayOf = isPlayOf;
    }

    @Override
    public String toString() {
        return "SaveCupTournamentDTO{" +
                "tournamentName='" + tournamentName + '\'' +
                ", tournamentType='" + tournamentType  + '\'' +
                ", tournamentLogo='" + tournamentLogo  + '\'' +
                ", location='" + location + '\'' +
                ", teamsNum=" + teamsNum  + '\'' +
                ", isPlayOf=" + isPlayOf +
                '}';
    }
}
