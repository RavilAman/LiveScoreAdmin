package ravil.amangeldiuly.example.minelivescoreadmin.statistics;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import ravil.amangeldiuly.example.minelivescoreadmin.R;
import ravil.amangeldiuly.example.minelivescoreadmin.enums.StatisticsType;
import ravil.amangeldiuly.example.minelivescoreadmin.web.requests.DistinctPlayerStatisticsDTO;
import ravil.amangeldiuly.example.minelivescoreadmin.web.requests.DistinctTeamStatisticsDTO;
import ravil.amangeldiuly.example.minelivescoreadmin.web.requests.GroupInfoDTO;

public class TableAdapter extends RecyclerView.Adapter<TableViewHolder> {

    private Context context;
    private final StatisticsType statisticsType;
    private List<GroupInfoDTO> groupStatistics;
    private List<DistinctPlayerStatisticsDTO> playerStatistics;
    private List<DistinctTeamStatisticsDTO> teamStatistics;

    public TableAdapter(Context context, StatisticsType statisticsType) {
        this.context = context;
        this.statisticsType = statisticsType;
    }

    @NonNull
    @Override
    public TableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.table_item, parent, false);
        return new TableViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TableViewHolder holder, int position) {
        holder.count.setText(String.valueOf(position + 1));
        switch (statisticsType) {
            case GROUP:
                holder.total.setVisibility(View.GONE);
                holder.perGame.setVisibility(View.GONE);
                GroupInfoDTO groupStatisticsItem = groupStatistics.get(position);
                Glide.with(context)
                        .load(groupStatisticsItem.getTeamLogo())
                        .into(holder.teamLogo);
                holder.name.setText(groupStatisticsItem.getTeamName());
                holder.points.setText(String.valueOf(groupStatisticsItem.getPoints()));
                holder.goalDifference.setText(
                        String.valueOf(groupStatisticsItem.getGoalCount() - groupStatisticsItem.getGoalMissed())
                );
                holder.playedGames.setText(String.valueOf(groupStatisticsItem.getGamePlayed()));
                holder.divider4.setVisibility(View.GONE);
                break;
            case GENERAL_TEAM:
            case INDIVIDUAL_TEAM:
                DistinctTeamStatisticsDTO distinctTeamStatisticsDTO = teamStatistics.get(position);
                Glide.with(context)
                        .load(distinctTeamStatisticsDTO.getTeamLogo())
                        .into(holder.teamLogo);
                holder.name.setText(distinctTeamStatisticsDTO.getTeamName());
                holder.points.setVisibility(View.GONE);
                holder.goalDifference.setVisibility(View.GONE);
                holder.playedGames.setVisibility(View.GONE);
                holder.total.setText(String.valueOf(distinctTeamStatisticsDTO.getTotal()));
                holder.perGame.setText(distinctTeamStatisticsDTO.getPerGame());
                holder.divider2.setVisibility(View.GONE);
                holder.divider3.setVisibility(View.GONE);
                break;
            case GENERAL_PLAYER:
            case INDIVIDUAL_PLAYER:
                DistinctPlayerStatisticsDTO distinctPlayerStatistics = playerStatistics.get(position);
                Glide.with(context)
                        .load(distinctPlayerStatistics.getTeamLogo())
                        .into(holder.teamLogo);
                holder.name.setText(distinctPlayerStatistics.getPlayerName());
                holder.points.setVisibility(View.GONE);
                holder.goalDifference.setVisibility(View.GONE);
                holder.playedGames.setVisibility(View.GONE);
                holder.total.setText(String.valueOf(distinctPlayerStatistics.getTotal()));
                holder.perGame.setText(distinctPlayerStatistics.getPerGame());
                holder.divider2.setVisibility(View.GONE);
                holder.divider3.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public int getItemCount() {
        switch (statisticsType) {
            case GROUP:
                return groupStatistics.size();
            case INDIVIDUAL_PLAYER:
            case GENERAL_PLAYER:
                return playerStatistics.size();
            case GENERAL_TEAM:
            case INDIVIDUAL_TEAM:
                return teamStatistics.size();
        }
        return 0;
    }

    public void setGroupStatistics(List<GroupInfoDTO> groupStatistics) {
        this.groupStatistics = groupStatistics;
    }

    public void setPlayerStatistics(List<DistinctPlayerStatisticsDTO> playerStatistics) {
        this.playerStatistics = playerStatistics;
    }

    public void setTeamStatistics(List<DistinctTeamStatisticsDTO> teamStatistics) {
        this.teamStatistics = teamStatistics;
    }
}
