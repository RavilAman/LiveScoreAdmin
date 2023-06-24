package ravil.amangeldiuly.example.minelivescoreadmin.web.requests;


public class TeamDTO {

    private Long teamId;
    private String teamName;
    private String teamLogo;

    public TeamDTO() {
    }

    public TeamDTO(Long teamId, String teamName, String teamLogo) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.teamLogo = teamLogo;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamLogo() {
        return teamLogo;
    }

    public void setTeamLogo(String teamLogo) {
        this.teamLogo = teamLogo;
    }

    @Override
    public String toString() {
        return "TeamDTO{" +
                "teamName='" + teamName + '\'' +
                '}';
    }
}
