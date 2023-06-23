package ravil.amangeldiuly.example.minelivescoreadmin.games;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ravil.amangeldiuly.example.minelivescoreadmin.R;
import ravil.amangeldiuly.example.minelivescoreadmin.activities.GameActivity;

public class GameViewHolder extends RecyclerView.ViewHolder {

    private LinearLayout gameItemLayout;
    Context context;
    TextView gameTime;
    TextView team1Name;
    TextView team2Name;
    TextView team1Score;
    TextView team2Score;
    ImageView team1Logo;
    ImageView team2Logo;
    long protocolId;

    public GameViewHolder(@NonNull View itemView) {
        super(itemView);

        gameItemLayout = itemView.findViewById(R.id.game_item_layout);
        gameTime = itemView.findViewById(R.id.game_item_time);
        team1Name = itemView.findViewById(R.id.game_item_team_1_name);
        team2Name = itemView.findViewById(R.id.game_item_team_2_name);
        team1Score = itemView.findViewById(R.id.game_item_team_1_score);
        team2Score = itemView.findViewById(R.id.game_item_team_2_score);
        team1Logo = itemView.findViewById(R.id.game_item_team_1_logo);
        team2Logo = itemView.findViewById(R.id.game_item_team_2_logo);

        gameItemLayout.setOnClickListener(gameItemLayoutListener());
    }

    private View.OnClickListener gameItemLayoutListener() {
        return view -> {
            Intent gameIntent = new Intent(context, GameActivity.class);
            gameIntent.putExtra("protocolId", protocolId);
            context.startActivity(gameIntent);
        };
    }
}
