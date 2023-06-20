package ravil.amangeldiuly.example.minelivescoreuser.web.responses;


public class SaveGoalEventDTO extends AbstractSaveEventDTO {

    private Long assistId;

    public SaveGoalEventDTO() {
    }

    public SaveGoalEventDTO(Long assistId) {
        this.assistId = assistId;
    }

    public Long getAssistId() {
        return assistId;
    }

    public void setAssistId(Long assistId) {
        this.assistId = assistId;
    }

    @Override
    public String toString() {
        return "SaveGoalEventDTO{" +
                "assistId=" + assistId +
                '}';
    }
}
