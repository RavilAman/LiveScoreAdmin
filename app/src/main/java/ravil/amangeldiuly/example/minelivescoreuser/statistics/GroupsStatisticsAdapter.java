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
import ravil.amangeldiuly.example.minelivescoreuser.web.responses.GroupInfoListDTO;

public class GroupsStatisticsAdapter extends RecyclerView.Adapter<GroupStatisticsViewHolder> {

    private Context context;
    private List<GroupInfoListDTO> statistics;
    private RecyclerView.RecycledViewPool recycledViewPool;

    public GroupsStatisticsAdapter(Context context, List<GroupInfoListDTO> statistics) {
        recycledViewPool = new RecyclerView.RecycledViewPool();
        this.context = context;
        this.statistics = statistics;
    }

    @NonNull
    @Override
    public GroupStatisticsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.group_statistics_item, parent, false);
        return new GroupStatisticsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupStatisticsViewHolder holder, int position) {
        GroupInfoListDTO groupInfoListDTO = statistics.get(position);
        holder.groupName.setText(groupInfoListDTO.getGroupName());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                holder.tableItemRecyclerView.getContext(),
                LinearLayoutManager.VERTICAL,
                false);
        linearLayoutManager.setInitialPrefetchItemCount(groupInfoListDTO.getSortedByPointTeams().size());

        TableAdapter tableAdapter = new TableAdapter(context, groupInfoListDTO.getSortedByPointTeams());
        holder.tableItemRecyclerView.setLayoutManager(linearLayoutManager);
        holder.tableItemRecyclerView.setAdapter(tableAdapter);
        holder.tableItemRecyclerView.setRecycledViewPool(recycledViewPool);
    }

    @Override
    public int getItemCount() {
        return statistics.size();
    }
}
