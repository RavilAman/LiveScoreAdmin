package ravil.amangeldiuly.example.minelivescoreadmin.teams;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import ravil.amangeldiuly.example.minelivescoreadmin.R;
import ravil.amangeldiuly.example.minelivescoreadmin.web.requests.TeamDTO;

public class TeamsAdapter extends RecyclerView.Adapter<TeamsViewHolder> {

    private Context context;
    private List<TeamDTO> teamDTOS;
    private final OnItemListener onItemListener;

    public TeamsAdapter(Context context, List<TeamDTO> teamDTOS, OnItemListener onItemListener) {
        this.context = context;
        this.teamDTOS = teamDTOS;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public TeamsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.team_item, parent, false);
        return new TeamsViewHolder(view, onItemListener, context);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamsViewHolder holder, int position) {
        TeamDTO currentTeam = teamDTOS.get(position);
        holder.teamName.setText(currentTeam.getTeamName());
        holder.teamDTO = currentTeam;

        Glide.with(context)
                .load(currentTeam.getTeamLogo())
                .into(holder.teamLogo);

        holder.itemView.setOnClickListener(v -> onItemListener.onItemClick(currentTeam));
    }

    @Override
    public int getItemCount() {
        return teamDTOS.size();
    }

    public interface OnItemListener {
        void onItemClick(TeamDTO team);
    }
}
