package ravil.amangeldiuly.example.minelivescoreuser.web.responses;

public class DistinctTeamStatisticsDTO {

    private String statName;
    private String groupName;
    private String teamName;
    private Integer total;
    private String perGame;
    private String teamLogo;

    public DistinctTeamStatisticsDTO() {
    }

    public DistinctTeamStatisticsDTO(String statName, String groupName, String teamName, Integer total, String perGame, String teamLogo) {
        this.statName = statName;
        this.groupName = groupName;
        this.teamName = teamName;
        this.total = total;
        this.perGame = perGame;
        this.teamLogo = teamLogo;
    }

    public String getStatName() {
        return statName;
    }

    public void setStatName(String statName) {
        this.statName = statName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public String getPerGame() {
        return perGame;
    }

    public void setPerGame(String perGame) {
        this.perGame = perGame;
    }

    public String getTeamLogo() {
        return teamLogo;
    }

    public void setTeamLogo(String teamLogo) {
        this.teamLogo = teamLogo;
    }
}
