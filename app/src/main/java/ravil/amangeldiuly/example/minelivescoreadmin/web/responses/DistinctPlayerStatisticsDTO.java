package ravil.amangeldiuly.example.minelivescoreadmin.web.responses;

public class DistinctPlayerStatisticsDTO {

    private String statName;
    private String teamName;
    private String teamLogo;
    private String playerName;
    private Long total;
    private String perGame;

    public DistinctPlayerStatisticsDTO() {
    }

    public DistinctPlayerStatisticsDTO(String statName, String teamName, String teamLogo, String playerName, Long total, String perGame) {
        this.statName = statName;
        this.teamName = teamName;
        this.teamLogo = teamLogo;
        this.playerName = playerName;
        this.total = total;
        this.perGame = perGame;
    }

    public String getStatName() {
        return statName;
    }

    public void setStatName(String statName) {
        this.statName = statName;
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

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public String getPerGame() {
        return perGame;
    }

    public void setPerGame(String perGame) {
        this.perGame = perGame;
    }

    @Override
    public String toString() {
        return "DistinctPlayerStatisticsDTO{" +
                "statName='" + statName + '\'' +
                ", teamName='" + teamName + '\'' +
                ", teamLogo='" + teamLogo + '\'' +
                ", playerName='" + playerName + '\'' +
                ", total=" + total +
                ", perGame='" + perGame + '\'' +
                '}';
    }
}
