package ravil.amangeldiuly.example.minelivescoreuser.statistics;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import ravil.amangeldiuly.example.minelivescoreuser.R;
import ravil.amangeldiuly.example.minelivescoreuser.enums.StatisticsType;
import ravil.amangeldiuly.example.minelivescoreuser.web.responses.DistinctPlayerStatisticsDTO;
import ravil.amangeldiuly.example.minelivescoreuser.web.responses.GroupInfoDTO;

public class TableAdapter extends RecyclerView.Adapter<TableViewHolder> {

    private Context context;
    private final StatisticsType statisticsType;
    private List<GroupInfoDTO> groupStatistics;
    private List<DistinctPlayerStatisticsDTO> individualStatistics;
    private List<DistinctPlayerStatisticsDTO> generalStatistics;

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
        DistinctPlayerStatisticsDTO distinctPlayerStatistics;
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
            case GENERAL:
                distinctPlayerStatistics = generalStatistics.get(position);
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
            case INDIVIDUAL:
                distinctPlayerStatistics = individualStatistics.get(position);
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
            case GENERAL:
                return generalStatistics.size();
            case INDIVIDUAL:
                return individualStatistics.size();
        }
        return 0;
    }

    public void setGroupStatistics(List<GroupInfoDTO> groupStatistics) {
        this.groupStatistics = groupStatistics;
    }

    public void setGeneralStatistics(List<DistinctPlayerStatisticsDTO> generalStatistics) {
        this.generalStatistics = generalStatistics;
    }

    public void setIndividualStatistics(List<DistinctPlayerStatisticsDTO> individualStatistics) {
        this.individualStatistics = individualStatistics;
    }
}
