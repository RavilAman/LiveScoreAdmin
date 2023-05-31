package ravil.amangeldiuly.example.minelivescoreuser.statistics;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ravil.amangeldiuly.example.minelivescoreuser.R;

public class GroupStatisticsViewHolder extends RecyclerView.ViewHolder {

    TextView groupName;
    RecyclerView tableItemRecyclerView;

    public GroupStatisticsViewHolder(@NonNull View itemView) {
        super(itemView);

        groupName = itemView.findViewById(R.id.group_statistics_item_group_name);
        tableItemRecyclerView = itemView.findViewById(R.id.group_statistics_item_table_item_recycler_view);
    }
}
