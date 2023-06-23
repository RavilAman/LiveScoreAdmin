package ravil.amangeldiuly.example.minelivescoreadmin.web.responses;

public class SaveCupTournamentDTO {

    private String tournamentName;
    private String tournamentType;
    private String tournamentLogo;
    private String location;
    private Integer teamsNum;
    private boolean isPlayOf;

    public SaveCupTournamentDTO(String tournamentName, String tournamentType, String tournamentLogo, String location, Integer teamsNum, boolean isPlayOf) {
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

    public boolean isPlayOf() {
        return isPlayOf;
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

    public void setPlayOf(boolean playOf) {
        isPlayOf = playOf;
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
