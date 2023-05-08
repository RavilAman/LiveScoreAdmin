package ravil.amangeldiuly.example.minelivescoreuser.web.responses;

public class TournamentDto {

    private Long tournamentId;
    private String tournamentName;
    private String tournamentLogo;
    private String tournamentType;
    private String tournamentLocation;

    public TournamentDto() {}

    public TournamentDto(Long tournamentId, String tournamentName, String tournamentLogo, String tournamentType, String tournamentLocation) {
        this.tournamentId = tournamentId;
        this.tournamentName = tournamentName;
        this.tournamentLogo = tournamentLogo;
        this.tournamentType = tournamentType;
        this.tournamentLocation = tournamentLocation;
    }

    public Long getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(Long tournamentId) {
        this.tournamentId = tournamentId;
    }

    public String getTournamentName() {
        return tournamentName;
    }

    public void setTournamentName(String tournamentName) {
        this.tournamentName = tournamentName;
    }

    public String getTournamentLogo() {
        return tournamentLogo;
    }

    public void setTournamentLogo(String tournamentLogo) {
        this.tournamentLogo = tournamentLogo;
    }

    public String getTournamentType() {
        return tournamentType;
    }

    public void setTournamentType(String tournamentType) {
        this.tournamentType = tournamentType;
    }

    public String getTournamentLocation() {
        return tournamentLocation;
    }

    public void setTournamentLocation(String tournamentLocation) {
        this.tournamentLocation = tournamentLocation;
    }

    @Override
    public String toString() {
        return "TournamentDto{" +
                "tournamentId=" + tournamentId +
                ", tournamentName='" + tournamentName + '\'' +
                ", tournamentLogo='" + tournamentLogo + '\'' +
                ", tournamentType='" + tournamentType + '\'' +
                ", tournamentLocation='" + tournamentLocation + '\'' +
                '}';
    }
}