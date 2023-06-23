package ravil.amangeldiuly.example.minelivescoreadmin.web.responses;


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
                "protocolId=" + protocolId +
                ", playerId=" + playerId +
                ", minute=" + minute +
                ", assistId=" + assistId +
                '}';
    }
}
