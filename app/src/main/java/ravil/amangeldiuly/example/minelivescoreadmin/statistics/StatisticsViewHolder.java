package ravil.amangeldiuly.example.minelivescoreadmin.statistics;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ravil.amangeldiuly.example.minelivescoreadmin.R;

public class StatisticsViewHolder extends RecyclerView.ViewHolder {

    LinearLayout groupStatisticsItemLabel;
    TextView groupOrCategoryName;
    RecyclerView tableItemRecyclerView;
    TextView total;
    TextView perGame;

    public StatisticsViewHolder(@NonNull View itemView) {
        super(itemView);

        total = itemView.findViewById(R.id.statistics_item_total);
        perGame = itemView.findViewById(R.id.statistics_item_per_game);
        groupStatisticsItemLabel = itemView.findViewById(R.id.group_statistics_item_label);
        groupOrCategoryName = itemView.findViewById(R.id.statistics_item_group_or_category_name);
        tableItemRecyclerView = itemView.findViewById(R.id.statistics_item_table_item_recycler_view);
    }
}
