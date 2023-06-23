package ravil.amangeldiuly.example.minelivescoreadmin.statistics;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ravil.amangeldiuly.example.minelivescoreadmin.R;

public class TableViewHolder extends RecyclerView.ViewHolder {

    View divider2;
    View divider3;
    View divider4;
    TextView count;
    TextView name;
    TextView playedGames;
    TextView goalDifference;
    TextView points;
    TextView total;
    TextView perGame;
    ImageView teamLogo;

    public TableViewHolder(@NonNull View itemView) {
        super(itemView);

        divider2 = itemView.findViewById(R.id.table_item_divider_view_2);
        divider3 = itemView.findViewById(R.id.table_item_divider_view_3);
        divider4 = itemView.findViewById(R.id.table_item_divider_view_4);
        count = itemView.findViewById(R.id.table_item_table_item_count);
        name = itemView.findViewById(R.id.table_item_name);
        playedGames = itemView.findViewById(R.id.table_item_played_games_count);
        goalDifference = itemView.findViewById(R.id.table_item_goal_difference);
        points = itemView.findViewById(R.id.table_item_points);
        total = itemView.findViewById(R.id.table_item_total);
        perGame = itemView.findViewById(R.id.table_item_per_game);
        teamLogo = itemView.findViewById(R.id.table_item_team_logo);
    }
}
