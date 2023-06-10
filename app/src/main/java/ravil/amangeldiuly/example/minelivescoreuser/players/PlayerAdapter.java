package ravil.amangeldiuly.example.minelivescoreuser.players;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ravil.amangeldiuly.example.minelivescoreuser.R;
import ravil.amangeldiuly.example.minelivescoreuser.web.responses.PlayerDTO;
import ravil.amangeldiuly.example.minelivescoreuser.web.responses.TeamDTO;

public class PlayerAdapter extends RecyclerView.Adapter<PlayersViewHolder> {

    private Context context;
    private List<PlayerDTO> players;
    private List<TeamDTO> teamList;
    private final OnItemListener onItemListener;

    public PlayerAdapter(Context context, List<PlayerDTO> players, OnItemListener onItemListener, List<TeamDTO> teamList) {
        this.context = context;
        this.players = players;
        this.onItemListener = onItemListener;
        this.teamList = teamList;
    }

    @NonNull
    @Override
    public PlayersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.player_item, parent, false);
        return new PlayersViewHolder(view, onItemListener, context, teamList);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull PlayersViewHolder holder, int position) {
        PlayerDTO currentPlayer = players.get(position);
        holder.playerDTO = currentPlayer;
        holder.playerName.setText(currentPlayer.getName() + " " + currentPlayer.getSurname());
        holder.playerNumber.setText("#" + currentPlayer.getPlayerNumber());
        holder.setDefaultSelectedItem(currentPlayer.getTeamId());

        holder.itemView.setOnClickListener(v -> onItemListener.onItemClick(currentPlayer));
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    public interface OnItemListener {
        void onItemClick(PlayerDTO player);
    }
}
