package ravil.amangeldiuly.example.minelivescoreuser.web.responses;


public class GroupDTO {

    private Long groupId;
    private String tournamentName;
    private String groupName;
    private boolean isPlayoff;
    private boolean currentStage;

    public GroupDTO() {
    }

    public GroupDTO(Long groupId, String tournamentName, String groupName, boolean isPlayoff, boolean currentStage) {
        this.groupId = groupId;
        this.tournamentName = tournamentName;
        this.groupName = groupName;
        this.isPlayoff = isPlayoff;
        this.currentStage = currentStage;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getTournamentName() {
        return tournamentName;
    }

    public void setTournamentName(String tournamentName) {
        this.tournamentName = tournamentName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public boolean isPlayoff() {
        return isPlayoff;
    }

    public void setPlayoff(boolean playoff) {
        isPlayoff = playoff;
    }

    public boolean isCurrentStage() {
        return currentStage;
    }

    public void setCurrentStage(boolean currentStage) {
        this.currentStage = currentStage;
    }

    @Override
    public String toString() {
        return "GroupDTO{" +
                "groupId=" + groupId +
                ", tournamentName='" + tournamentName + '\'' +
                ", groupName='" + groupName + '\'' +
                ", isPlayoff=" + isPlayoff +
                ", currentStage=" + currentStage +
                '}';
    }
}
