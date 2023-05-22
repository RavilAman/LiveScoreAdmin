package ravil.amangeldiuly.example.minelivescoreuser.web.responses;

import java.util.List;

public class NewGameDTO {

    private Long tournamentId;
    private String tournamentName;
    private String tournamentLogo;
    private String groupName;
    private Long groupId;
    private List<GameDTO> games;

    public NewGameDTO() {
    }

    public NewGameDTO(Long tournamentId, String tournamentName, String tournamentLogo, String groupName, Long groupId, List<GameDTO> games) {
        this.tournamentId = tournamentId;
        this.tournamentName = tournamentName;
        this.tournamentLogo = tournamentLogo;
        this.groupName = groupName;
        this.groupId = groupId;
        this.games = games;
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

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public List<GameDTO> getGames() {
        return games;
    }

    public void setGames(List<GameDTO> games) {
        this.games = games;
    }

    @Override
    public String toString() {
        return "NewGameDTO{" +
                "tournamentId=" + tournamentId +
                ", tournamentName='" + tournamentName + '\'' +
                ", tournamentLogo='" + tournamentLogo + '\'' +
                ", groupName='" + groupName + '\'' +
                ", groupId=" + groupId +
                ", games=" + games +
                '}';
    }
}
