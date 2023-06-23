package ravil.amangeldiuly.example.minelivescoreadmin.web.responses;

public class AssistDTO {

    private String assistPlayer;
    private Long assistPlayerId;

    public AssistDTO() {
    }

    public AssistDTO(String assistPlayer, Long assistPlayerId) {
        this.assistPlayer = assistPlayer;
        this.assistPlayerId = assistPlayerId;
    }

    public String getAssistPlayer() {
        return assistPlayer;
    }

    public void setAssistPlayer(String assistPlayer) {
        this.assistPlayer = assistPlayer;
    }

    public Long getAssistPlayerId() {
        return assistPlayerId;
    }

    public void setAssistPlayerId(Long assistPlayerId) {
        this.assistPlayerId = assistPlayerId;
    }

    @Override
    public String toString() {
        return "AssistDTO{" +
                "assistPlayer='" + assistPlayer + '\'' +
                ", assistPlayerId=" + assistPlayerId +
                '}';
    }
}
