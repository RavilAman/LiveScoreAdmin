package ravil.amangeldiuly.example.minelivescoreadmin.games;

import static ravil.amangeldiuly.example.minelivescoreadmin.ColorConstants.APP_ORANGE;
import static ravil.amangeldiuly.example.minelivescoreadmin.utils.GeneralUtils.getGameScoreForTeam;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.time.format.DateTimeFormatter;
import java.util.List;

import ravil.amangeldiuly.example.minelivescoreadmin.R;
import ravil.amangeldiuly.example.minelivescoreadmin.enums.GameState;
import ravil.amangeldiuly.example.minelivescoreadmin.web.requests.GameDTO;

public class GameAdapter extends RecyclerView.Adapter<GameViewHolder> {

    private Context context;
    private List<GameDTO> games;

    public GameAdapter(Context context, List<GameDTO> games) {
        this.context = context;
        this.games = games;
    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.game_item, parent, false);
        return new GameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GameViewHolder holder, int position) {
        GameDTO game = games.get(position);
        Glide.with(context)
                .load(game.getTeam1Logo())
                .into(holder.team1Logo);
        Glide.with(context)
                .load(game.getTeam2Logo())
                .into(holder.team2Logo);
        holder.team1Name.setText(game.getTeam1Name());
        holder.team2Name.setText(game.getTeam2Name());
        if (game.getGameState() == GameState.NOT_STARTED) {
            holder.team1Score.setText("");
            holder.team2Score.setText("");
            holder.gameTime.setText(
                    game.getGameDateTime()
                            .format(DateTimeFormatter.ofPattern("HH:mm"))
            );
        } else {
            if (game.getGameState() == GameState.STARTED) {
                holder.gameTime.setText(R.string.game_item_live);
                holder.gameTime.setTextColor(Color.parseColor(APP_ORANGE));
            } else if (game.getGameState() == GameState.ENDED) {
                holder.gameTime.setText(R.string.game_item_ft);
            }
            holder.team1Score.setText(
                    getGameScoreForTeam(game.getGameScore(), 1));
            holder.team2Score.setText(
                    getGameScoreForTeam(game.getGameScore(), 2));
        }
        holder.protocolId = game.getProtocolId();
        holder.context = context;
    }

    @Override
    public int getItemCount() {
        return games.size();
    }
}
