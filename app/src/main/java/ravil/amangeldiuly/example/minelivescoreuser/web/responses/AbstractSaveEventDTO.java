package ravil.amangeldiuly.example.minelivescoreuser.web.responses;


public class AbstractSaveEventDTO {

    private Long protocolId;
    private Long playerId;
    private Integer minute;

    public AbstractSaveEventDTO() {
    }

    public AbstractSaveEventDTO(Long protocolId, Long playerId, Integer minute) {
        this.protocolId = protocolId;
        this.playerId = playerId;
        this.minute = minute;
    }

    public Long getProtocolId() {
        return protocolId;
    }

    public void setProtocolId(Long protocolId) {
        this.protocolId = protocolId;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public Integer getMinute() {
        return minute;
    }

    public void setMinute(Integer minute) {
        this.minute = minute;
    }

    @Override
    public String toString() {
        return "AbstractSaveEventDTO{" +
                "protocolId=" + protocolId +
                ", playerId=" + playerId +
                ", minute=" + minute +
                '}';
    }
}
