package ravil.amangeldiuly.example.minelivescoreuser.web.responses;


import java.util.List;


public class PlayerStatisticsAllDTO {

    private String statName;
    private List<DistinctPlayerStatisticsDTO> statistics;

    public PlayerStatisticsAllDTO() {
    }

    public PlayerStatisticsAllDTO(String statName, List<DistinctPlayerStatisticsDTO> statistics) {
        this.statName = statName;
        this.statistics = statistics;
    }

    public String getStatName() {
        return statName;
    }

    public void setStatName(String statName) {
        this.statName = statName;
    }

    public List<DistinctPlayerStatisticsDTO> getStatistics() {
        return statistics;
    }

    public void setStatistics(List<DistinctPlayerStatisticsDTO> statistics) {
        this.statistics = statistics;
    }
}
