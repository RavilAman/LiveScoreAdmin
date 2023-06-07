package ravil.amangeldiuly.example.minelivescoreuser.web.responses;

import java.util.List;


public class TeamStatisticsAllDTO {

    private String statName;
    private List<DistinctTeamStatisticsDTO> statisticsDTOS;

    public TeamStatisticsAllDTO() {
    }

    public TeamStatisticsAllDTO(String statName, List<DistinctTeamStatisticsDTO> statisticsDTOS) {
        this.statName = statName;
        this.statisticsDTOS = statisticsDTOS;
    }

    public String getStatName() {
        return statName;
    }

    public void setStatName(String statName) {
        this.statName = statName;
    }

    public List<DistinctTeamStatisticsDTO> getStatisticsDTOS() {
        return statisticsDTOS;
    }

    public void setStatisticsDTOS(List<DistinctTeamStatisticsDTO> statisticsDTOS) {
        this.statisticsDTOS = statisticsDTOS;
    }
}
