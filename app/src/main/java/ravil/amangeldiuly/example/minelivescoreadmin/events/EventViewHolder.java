package ravil.amangeldiuly.example.minelivescoreadmin.events;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ravil.amangeldiuly.example.minelivescoreadmin.R;

public class EventViewHolder extends RecyclerView.ViewHolder {

    public ImageButton editEvent;
    TextView time;
    TextView team1Player;
    TextView team2Player;
    TextView gameScore;
    TextView team1Assist;
    TextView team2Assist;
    ImageView eventLogoTeam1;
    ImageView eventLogoTeam2;

    public EventViewHolder(@NonNull View itemView) {
        super(itemView);

        time = itemView.findViewById(R.id.event_item_time);
        team1Player = itemView.findViewById(R.id.event_item_player_team_1);
        team2Player = itemView.findViewById(R.id.event_item_player_team_2);
        gameScore = itemView.findViewById(R.id.event_item_game_score);
        team1Assist = itemView.findViewById(R.id.event_item_assist_team_1);
        team2Assist = itemView.findViewById(R.id.event_item_assist_team_2);
        eventLogoTeam1 = itemView.findViewById(R.id.event_item_event_logo_team_1);
        eventLogoTeam2 = itemView.findViewById(R.id.event_item_event_logo_team_2);
        editEvent = itemView.findViewById(R.id.event_item_edit_event);
    }
}
