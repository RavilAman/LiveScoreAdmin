package ravil.amangeldiuly.example.minelivescoreadmin.web.responses;

public class EventDTO {

    private Long eventId;
    private Integer minute;
    private String gameScore;
    private String eventName;
    private String playerName;
    private Long playerId;
    private Long teamId;
    private String teamName;
    private String teamLogo;
    private AssistDTO assist;

    public EventDTO() {
    }

    public EventDTO(Long eventId, Integer minute, String gameScore, String eventName, String playerName, Long playerId, Long teamId, String teamName, String teamLogo, AssistDTO assist) {
        this.eventId = eventId;
        this.minute = minute;
        this.gameScore = gameScore;
        this.eventName = eventName;
        this.playerName = playerName;
        this.playerId = playerId;
        this.teamId = teamId;
        this.teamName = teamName;
        this.teamLogo = teamLogo;
        this.assist = assist;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Integer getMinute() {
        return minute;
    }

    public void setMinute(Integer minute) {
        this.minute = minute;
    }

    public String getGameScore() {
        return gameScore;
    }

    public void setGameScore(String gameScore) {
        this.gameScore = gameScore;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamLogo() {
        return teamLogo;
    }

    public void setTeamLogo(String teamLogo) {
        this.teamLogo = teamLogo;
    }

    public AssistDTO getAssist() {
        return assist;
    }

    public void setAssist(AssistDTO assist) {
        this.assist = assist;
    }

    @Override
    public String toString() {
        return "EventDTO{" +
                "eventId=" + eventId +
                ", minute=" + minute +
                ", gameScore='" + gameScore + '\'' +
                ", eventName='" + eventName + '\'' +
                ", playerName='" + playerName + '\'' +
                ", playerId=" + playerId +
                ", teamId=" + teamId +
                ", teamName='" + teamName + '\'' +
                ", teamLogo='" + teamLogo + '\'' +
                ", assist=" + assist +
                '}';
    }
}
