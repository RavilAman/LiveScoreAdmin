package ravil.amangeldiuly.example.minelivescoreuser.statistics;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ravil.amangeldiuly.example.minelivescoreuser.R;

public class TableViewHolder extends RecyclerView.ViewHolder {

    TextView count;
    TextView teamName;
    TextView playedGames;
    TextView goalDifference;
    TextView points;
    ImageView teamLogo;

    public TableViewHolder(@NonNull View itemView) {
        super(itemView);

        count = itemView.findViewById(R.id.table_item_table_item_count);
        teamName = itemView.findViewById(R.id.table_item_team_name);
        playedGames = itemView.findViewById(R.id.table_item_played_games_count);
        goalDifference = itemView.findViewById(R.id.table_item_goal_difference);
        points = itemView.findViewById(R.id.table_item_points);
        teamLogo = itemView.findViewById(R.id.table_item_team_logo);
    }
}