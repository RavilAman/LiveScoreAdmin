package ravil.amangeldiuly.example.minelivescoreuser.web.responses;

public class GroupInfoDTO {

    private Integer position;
    private Long teamId;
    private String teamName;
    private String teamLogo;
    private Integer gamePlayed;
    private Integer winCount;
    private Integer drawCount;
    private Integer loseCount;
    private Integer goalCount;
    private Integer goalMissed;
    private Integer points;
    private boolean isLive;

    public GroupInfoDTO(String teamName, String teamLogo, Integer gamePlayed, Integer winCount, Integer drawCount, Integer loseCount, Integer goalCount, Integer goalMissed, Integer points, Long teamId) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.teamLogo = teamLogo;
        this.gamePlayed = gamePlayed;
        this.winCount = winCount;
        this.drawCount = drawCount;
        this.loseCount = loseCount;
        this.goalCount = goalCount;
        this.goalMissed = goalMissed;
        this.points = points;
    }

    public GroupInfoDTO(String teamName, String teamLogo, Integer gamePlayed, Integer winCount, Integer drawCount, Integer loseCount, Integer goalCount, Integer goalMissed, Integer points, boolean isLive, Long teamId) {
        this.teamName = teamName;
        this.teamLogo = teamLogo;
        this.gamePlayed = gamePlayed;
        this.winCount = winCount;
        this.drawCount = drawCount;
        this.loseCount = loseCount;
        this.goalCount = goalCount;
        this.goalMissed = goalMissed;
        this.points = points;
        this.isLive = isLive;
        this.teamId = teamId;
    }

    public GroupInfoDTO(Integer position, Long teamId, String teamName, String teamLogo, Integer gamePlayed, Integer winCount, Integer drawCount, Integer loseCount, Integer goalCount, Integer goalMissed, Integer points, boolean isLive) {
        this.position = position;
        this.teamId = teamId;
        this.teamName = teamName;
        this.teamLogo = teamLogo;
        this.gamePlayed = gamePlayed;
        this.winCount = winCount;
        this.drawCount = drawCount;
        this.loseCount = loseCount;
        this.goalCount = goalCount;
        this.goalMissed = goalMissed;
        this.points = points;
        this.isLive = isLive;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
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

    public Integer getGamePlayed() {
        return gamePlayed;
    }

    public void setGamePlayed(Integer gamePlayed) {
        this.gamePlayed = gamePlayed;
    }

    public Integer getWinCount() {
        return winCount;
    }

    public void setWinCount(Integer winCount) {
        this.winCount = winCount;
    }

    public Integer getDrawCount() {
        return drawCount;
    }

    public void setDrawCount(Integer drawCount) {
        this.drawCount = drawCount;
    }

    public Integer getLoseCount() {
        return loseCount;
    }

    public void setLoseCount(Integer loseCount) {
        this.loseCount = loseCount;
    }

    public Integer getGoalCount() {
        return goalCount;
    }

    public void setGoalCount(Integer goalCount) {
        this.goalCount = goalCount;
    }

    public Integer getGoalMissed() {
        return goalMissed;
    }

    public void setGoalMissed(Integer goalMissed) {
        this.goalMissed = goalMissed;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public boolean isLive() {
        return isLive;
    }

    public void setLive(boolean live) {
        isLive = live;
    }

    @Override
    public String toString() {
        return "GroupInfoDTO{" +
                "position=" + position +
                ", teamId=" + teamId +
                ", teamName='" + teamName + '\'' +
                ", teamLogo='" + teamLogo + '\'' +
                ", gamePlayed=" + gamePlayed +
                ", winCount=" + winCount +
                ", drawCount=" + drawCount +
                ", loseCount=" + loseCount +
                ", goalCount=" + goalCount +
                ", goalMissed=" + goalMissed +
                ", points=" + points +
                ", isLive=" + isLive +
                '}';
    }
}
