package ravil.amangeldiuly.example.minelivescoreuser.events;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ravil.amangeldiuly.example.minelivescoreuser.R;

public class EventViewHolder extends RecyclerView.ViewHolder {

    TextView time;
    TextView team1Player;
    TextView team2Player;
    TextView team1Score;
    TextView team2Score;
    TextView team1Assist;
    TextView team2Assist;
    ImageView eventLogo;

    public EventViewHolder(@NonNull View itemView) {
        super(itemView);

        time = itemView.findViewById(R.id.event_item_time);
        team1Player = itemView.findViewById(R.id.event_item_player_team_1);
        team2Player = itemView.findViewById(R.id.event_item_player_team_2);
        team1Score = itemView.findViewById(R.id.event_item_score_team_1);
        team2Score = itemView.findViewById(R.id.event_item_score_team_2);
        team1Assist = itemView.findViewById(R.id.event_item_assist_team_1);
        team2Assist = itemView.findViewById(R.id.event_item_assist_team_2);
        eventLogo = itemView.findViewById(R.id.event_item_event_logo);
    }
}
