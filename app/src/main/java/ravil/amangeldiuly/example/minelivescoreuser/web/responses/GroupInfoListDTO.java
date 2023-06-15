package ravil.amangeldiuly.example.minelivescoreuser.web.responses;

import java.util.List;

public class GroupInfoListDTO {

    private String tournamentName;
    private String tournamentLogo;
    private String groupName;
    private Long groupId;
    List<GroupInfoDTO> sortedByPointTeams;

    public GroupInfoListDTO(String tournamentName, String tournamentLogo, String groupName, Long groupId, List<GroupInfoDTO> sortedByPointTeams) {
        this.tournamentName = tournamentName;
        this.tournamentLogo = tournamentLogo;
        this.groupName = groupName;
        this.groupId = groupId;
        this.sortedByPointTeams = sortedByPointTeams;
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

    public List<GroupInfoDTO> getSortedByPointTeams() {
        return sortedByPointTeams;
    }

    public void setSortedByPointTeams(List<GroupInfoDTO> sortedByPointTeams) {
        this.sortedByPointTeams = sortedByPointTeams;
    }

    @Override
    public String toString() {
        return "GroupInfoListDTO{" +
                "tournamentName='" + tournamentName + '\'' +
                ", tournamentLogo='" + tournamentLogo + '\'' +
                ", groupName='" + groupName + '\'' +
                ", groupId=" + groupId +
                ", sortedByPointTeams=" + sortedByPointTeams +
                '}';
    }
}
