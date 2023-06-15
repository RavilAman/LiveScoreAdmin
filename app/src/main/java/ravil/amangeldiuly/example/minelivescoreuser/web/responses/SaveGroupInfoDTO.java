package ravil.amangeldiuly.example.minelivescoreuser.web.responses;

public class SaveGroupInfoDTO {

    private Long teamId;
    private Long groupId;

    public SaveGroupInfoDTO(Long teamId, Long groupId) {
        this.teamId = teamId;
        this.groupId = groupId;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }
}
