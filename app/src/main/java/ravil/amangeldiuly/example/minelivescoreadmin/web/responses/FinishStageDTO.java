package ravil.amangeldiuly.example.minelivescoreadmin.web.responses;

import java.util.List;

public class FinishStageDTO {

    private List<Long> teamIds;
    private Long groupId;

    public FinishStageDTO(List<Long> teamIds, Long groupId) {
        this.teamIds = teamIds;
        this.groupId = groupId;
    }

    public List<Long> getTeamIds() {
        return teamIds;
    }

    public void setTeamIds(List<Long> teamIds) {
        this.teamIds = teamIds;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }
}
