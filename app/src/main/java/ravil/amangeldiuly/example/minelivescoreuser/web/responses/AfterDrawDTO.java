package ravil.amangeldiuly.example.minelivescoreuser.web.responses;

import java.util.List;

public class AfterDrawDTO {

    Long groupId;
    String groupName;
    List<TeamDTO> teams;


    public AfterDrawDTO(Long groupId, String groupName, List<TeamDTO> teams) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.teams = teams;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<TeamDTO> getTeams() {
        return teams;
    }

    public void setTeams(List<TeamDTO> teams) {
        this.teams = teams;
    }
}