package ravil.amangeldiuly.example.minelivescoreuser.statistics;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ravil.amangeldiuly.example.minelivescoreuser.R;
import ravil.amangeldiuly.example.minelivescoreuser.enums.StatisticsType;
import ravil.amangeldiuly.example.minelivescoreuser.web.responses.DistinctPlayerStatisticsDTO;
import ravil.amangeldiuly.example.minelivescoreuser.web.responses.GroupInfoListDTO;
import ravil.amangeldiuly.example.minelivescoreuser.web.responses.PlayerStatisticsAllDTO;

public class StatisticsAdapter extends RecyclerView.Adapter<StatisticsViewHolder> {

    private Context context;
    private final StatisticsType statisticsType;
    private List<GroupInfoListDTO> groupStatisticsList;
    private List<DistinctPlayerStatisticsDTO> individualStatistics;
    private List<PlayerStatisticsAllDTO> generalStatisticsList;
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
            case GENERAL:
                holder.groupStatisticsItemLabel.setVisibility(View.GONE);
                PlayerStatisticsAllDTO playerStatistics = generalStatisticsList.get(position);
                holder.groupOrCategoryName.setText(playerStatistics.getStatName());
                linearLayoutManager.setInitialPrefetchItemCount(playerStatistics.getStatistics().size());
                tableAdapter.setGeneralStatistics(playerStatistics.getStatistics());
                break;
            case INDIVIDUAL:
                holder.groupStatisticsItemLabel.setVisibility(View.GONE);
                holder.groupOrCategoryName.setText(individualCategoryName);
                linearLayoutManager.setInitialPrefetchItemCount(individualStatistics.size());
                tableAdapter.setIndividualStatistics(individualStatistics);
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
            case GENERAL:
                return generalStatisticsList.size();
            case INDIVIDUAL:
                return 1;
        }
        return 0;
    }

    public void setGroupStatisticsList(List<GroupInfoListDTO> groupStatisticsList) {
        this.groupStatisticsList = groupStatisticsList;
    }

    public void setGeneralStatisticsList(List<PlayerStatisticsAllDTO> generalStatisticsList) {
        this.generalStatisticsList = generalStatisticsList;
    }

    public void setIndividualCategoryName(String individualCategoryName) {
        this.individualCategoryName = individualCategoryName;
    }

    public void setIndividualStatistics(List<DistinctPlayerStatisticsDTO> individualStatistics) {
        this.individualStatistics = individualStatistics;
    }
}
