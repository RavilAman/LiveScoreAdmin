package ravil.amangeldiuly.example.minelivescoreadmin.web.responses;

import java.time.LocalDateTime;
import java.util.List;

import ravil.amangeldiuly.example.minelivescoreadmin.enums.GameState;

public class ProtocolDTO {

    private Long protocolId;
    private Long gameId;
    private String team1;
    private String team2;
    private String team1Logo;
    private String team2Logo;
    private Long team1Id;
    private Long team2Id;
    private LocalDateTime dateAndTime;
    private String gameScore;
    private List<EventDTO> events;
    private GameState gameState;

    public ProtocolDTO() {
    }

    public ProtocolDTO(Long protocolId, Long gameId, String team1, String team2, String team1Logo, String team2Logo, Long team1Id, Long team2Id, LocalDateTime dateAndTime, String gameScore, List<EventDTO> events, GameState gameState) {
        this.protocolId = protocolId;
        this.gameId = gameId;
        this.team1 = team1;
        this.team2 = team2;
        this.team1Logo = team1Logo;
        this.team2Logo = team2Logo;
        this.team1Id = team1Id;
        this.team2Id = team2Id;
        this.dateAndTime = dateAndTime;
        this.gameScore = gameScore;
        this.events = events;
        this.gameState = gameState;
    }

    public Long getProtocolId() {
        return protocolId;
    }

    public void setProtocolId(Long protocolId) {
        this.protocolId = protocolId;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public String getTeam1() {
        return team1;
    }

    public void setTeam1(String team1) {
        this.team1 = team1;
    }

    public String getTeam2() {
        return team2;
    }

    public void setTeam2(String team2) {
        this.team2 = team2;
    }

    public String getTeam1Logo() {
        return team1Logo;
    }

    public void setTeam1Logo(String team1Logo) {
        this.team1Logo = team1Logo;
    }

    public String getTeam2Logo() {
        return team2Logo;
    }

    public void setTeam2Logo(String team2Logo) {
        this.team2Logo = team2Logo;
    }

    public Long getTeam1Id() {
        return team1Id;
    }

    public void setTeam1Id(Long team1Id) {
        this.team1Id = team1Id;
    }

    public Long getTeam2Id() {
        return team2Id;
    }

    public void setTeam2Id(Long team2Id) {
        this.team2Id = team2Id;
    }

    public LocalDateTime getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(LocalDateTime dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public String getGameScore() {
        return gameScore;
    }

    public void setGameScore(String gameScore) {
        this.gameScore = gameScore;
    }

    public List<EventDTO> getEvents() {
        return events;
    }

    public void setEvents(List<EventDTO> events) {
        this.events = events;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }
}
