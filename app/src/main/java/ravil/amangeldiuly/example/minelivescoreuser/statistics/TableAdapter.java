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
import ravil.amangeldiuly.example.minelivescoreuser.web.responses.GroupInfoDTO;

public class TableAdapter extends RecyclerView.Adapter<TableViewHolder> {

    private Context context;
    private List<GroupInfoDTO> groupStatistics;

    public TableAdapter(Context context, List<GroupInfoDTO> groupStatistics) {
        this.context = context;
        this.groupStatistics = groupStatistics;
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
        GroupInfoDTO groupStatisticsItem = groupStatistics.get(position);
        holder.count.setText(String.valueOf(position + 1));
        Glide.with(context)
                .load(groupStatisticsItem.getTeamLogo())
                .into(holder.teamLogo);
        holder.teamName.setText(groupStatisticsItem.getTeamName());
        holder.points.setText(String.valueOf(groupStatisticsItem.getPoints()));
        holder.goalDifference.setText(
                String.valueOf(groupStatisticsItem.getGoalCount() - groupStatisticsItem.getGoalMissed())
        );
        holder.points.setText(String.valueOf(groupStatisticsItem.getPoints()));
    }

    @Override
    public int getItemCount() {
        return groupStatistics.size();
    }
}
