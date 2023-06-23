package ravil.amangeldiuly.example.minelivescoreadmin.web.responses;

import java.time.LocalDateTime;

import ravil.amangeldiuly.example.minelivescoreadmin.enums.GameState;

public class GameDTO {

    private Long gameId;
    private Long groupId;
    private String team1Name;
    private String team2Name;
    private String team1Logo;
    private String team2Logo;
    private String gameScore;
    private GameState gameState;
    private Long protocolId;
    private LocalDateTime gameDateTime;

    public GameDTO() {
    }


    public GameDTO(Long gameId, Long groupId, String team1Name, String team2Name, String team1Logo, String team2Logo, String gameScore, GameState gameState, Long protocolId, LocalDateTime gameDateTime) {
        this.gameId = gameId;
        this.groupId = groupId;
        this.team1Name = team1Name;
        this.team2Name = team2Name;
        this.team1Logo = team1Logo;
        this.team2Logo = team2Logo;
        this.gameScore = gameScore;
        this.gameState = gameState;
        this.protocolId = protocolId;
        this.gameDateTime = gameDateTime;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getTeam1Name() {
        return team1Name;
    }

    public void setTeam1Name(String team1Name) {
        this.team1Name = team1Name;
    }

    public String getTeam2Name() {
        return team2Name;
    }

    public void setTeam2Name(String team2Name) {
        this.team2Name = team2Name;
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

    public String getGameScore() {
        return gameScore;
    }

    public void setGameScore(String gameScore) {
        this.gameScore = gameScore;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public Long getProtocolId() {
        return protocolId;
    }

    public void setProtocolId(Long protocolId) {
        this.protocolId = protocolId;
    }

    public LocalDateTime getGameDateTime() {
        return gameDateTime;
    }

    public void setGameDateTime(LocalDateTime gameDateTime) {
        this.gameDateTime = gameDateTime;
    }

    @Override
    public String toString() {
        return "GameDTO{" +
                "gameId=" + gameId +
                ", groupId=" + groupId +
                ", team1Name='" + team1Name + '\'' +
                ", team2Name='" + team2Name + '\'' +
                ", team1Logo='" + team1Logo + '\'' +
                ", team2Logo='" + team2Logo + '\'' +
                ", gameScore='" + gameScore + '\'' +
                ", gameState=" + gameState +
                ", protocolId=" + protocolId +
                ", gameDateTime=" + gameDateTime +
                '}';
    }
}
