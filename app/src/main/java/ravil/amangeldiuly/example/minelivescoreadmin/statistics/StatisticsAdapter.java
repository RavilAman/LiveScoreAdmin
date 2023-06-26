package ravil.amangeldiuly.example.minelivescoreadmin.statistics;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ravil.amangeldiuly.example.minelivescoreadmin.R;
import ravil.amangeldiuly.example.minelivescoreadmin.enums.StatisticsType;
import ravil.amangeldiuly.example.minelivescoreadmin.web.requests.DistinctPlayerStatisticsDTO;
import ravil.amangeldiuly.example.minelivescoreadmin.web.requests.DistinctTeamStatisticsDTO;
import ravil.amangeldiuly.example.minelivescoreadmin.web.requests.GroupInfoListDTO;
import ravil.amangeldiuly.example.minelivescoreadmin.web.requests.PlayerStatisticsAllDTO;
import ravil.amangeldiuly.example.minelivescoreadmin.web.requests.TeamStatisticsAllDTO;

public class StatisticsAdapter extends RecyclerView.Adapter<StatisticsViewHolder> {

    private Context context;
    private final StatisticsType statisticsType;
    private List<GroupInfoListDTO> groupStatisticsList;
    private List<DistinctPlayerStatisticsDTO> individualPlayerStatistics;
    private List<DistinctTeamStatisticsDTO> individualTeamStatistics;
    private List<PlayerStatisticsAllDTO> generalPlayerStatistics;
    private List<TeamStatisticsAllDTO> generalTeamStatistic;
    private RecyclerView.RecycledViewPool recycledViewPool;
    private String individualCategoryName;

    public StatisticsAdapter(Context context, StatisticsType statisticsType) {
        recycledViewPool = new RecyclerView.RecycledViewPool();
        this.context = context;
        this.statisticsType = statisticsType;
    }

    @NonNull
    @Override
    public StatisticsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.statistics_item, parent, false);
        return new StatisticsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StatisticsViewHolder holder, int position) {
        TableAdapter tableAdapter = new TableAdapter(context, statisticsType);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                holder.tableItemRecyclerView.getContext(),
                LinearLayoutManager.VERTICAL,
                false);
        switch (statisticsType) {
            case GROUP:
                GroupInfoListDTO groupInfoListDTO = groupStatisticsList.get(position);
                holder.groupOrCategoryName.setText(groupInfoListDTO.getGroupName());
                linearLayoutManager.setInitialPrefetchItemCount(groupInfoListDTO.getSortedByPointTeams().size());
                tableAdapter.setGroupStatistics(groupInfoListDTO.getSortedByPointTeams());
                holder.total.setVisibility(View.GONE);
                holder.perGame.setVisibility(View.GONE);
                break;
            case GENERAL_PLAYER:
                holder.groupStatisticsItemLabel.setVisibility(View.GONE);
                PlayerStatisticsAllDTO playerStatistics = generalPlayerStatistics.get(position);
                holder.groupOrCategoryName.setText(playerStatistics.getStatName().replace("_", " "));
                linearLayoutManager.setInitialPrefetchItemCount(playerStatistics.getStatistics().size());
                tableAdapter.setPlayerStatistics(playerStatistics.getStatistics());
                break;
            case GENERAL_TEAM:
                holder.groupStatisticsItemLabel.setVisibility(View.GONE);
                TeamStatisticsAllDTO teamStatistics = generalTeamStatistic.get(position);
                holder.groupOrCategoryName.setText(teamStatistics.getStatName().replace("_", " "));
                linearLayoutManager.setInitialPrefetchItemCount(teamStatistics.getStatisticsDTOS().size());
                tableAdapter.setTeamStatistics(teamStatistics.getStatisticsDTOS());
                break;
            case INDIVIDUAL_PLAYER:
                holder.groupStatisticsItemLabel.setVisibility(View.GONE);
                holder.groupOrCategoryName.setText(individualCategoryName.replace("_", " "));
                linearLayoutManager.setInitialPrefetchItemCount(individualPlayerStatistics.size());
                tableAdapter.setPlayerStatistics(individualPlayerStatistics);
                break;
            case INDIVIDUAL_TEAM:
                holder.groupStatisticsItemLabel.setVisibility(View.GONE);
                holder.groupOrCategoryName.setText(individualCategoryName.replace("_", " "));
                linearLayoutManager.setInitialPrefetchItemCount(individualTeamStatistics.size());
                tableAdapter.setTeamStatistics(individualTeamStatistics);
                break;
        }
        holder.tableItemRecyclerView.setLayoutManager(linearLayoutManager);
        holder.tableItemRecyclerView.setAdapter(tableAdapter);
        holder.tableItemRecyclerView.setRecycledViewPool(recycledViewPool);
    }

    @Override
    public int getItemCount() {
        switch (statisticsType) {
            case GROUP:
                return groupStatisticsList.size();
            case GENERAL_PLAYER:
                return generalPlayerStatistics.size();
            case GENERAL_TEAM:
                return generalTeamStatistic.size();
            case INDIVIDUAL_PLAYER:
            case INDIVIDUAL_TEAM:
                return 1;
        }
        return 0;
    }

    public void setGroupStatisticsList(List<GroupInfoListDTO> groupStatisticsList) {
        this.groupStatisticsList = groupStatisticsList;
    }

    public void setGeneralPlayerStatistics(List<PlayerStatisticsAllDTO> generalPlayerStatistics) {
        this.generalPlayerStatistics = generalPlayerStatistics;
    }

    public void setIndividualCategoryName(String individualCategoryName) {
        this.individualCategoryName = individualCategoryName;
    }

    public void setIndividualPlayerStatistics(List<DistinctPlayerStatisticsDTO> individualPlayerStatistics) {
        this.individualPlayerStatistics = individualPlayerStatistics;
    }

    public void setIndividualTeamStatistics(List<DistinctTeamStatisticsDTO> individualTeamStatistics) {
        this.individualTeamStatistics = individualTeamStatistics;
    }

    public void setGeneralTeamStatistic(List<TeamStatisticsAllDTO> generalTeamStatistic) {
        this.generalTeamStatistic = generalTeamStatistic;
    }
}
