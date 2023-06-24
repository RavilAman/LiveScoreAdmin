package ravil.amangeldiuly.example.minelivescoreadmin.tournaments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import ravil.amangeldiuly.example.minelivescoreadmin.R;
import ravil.amangeldiuly.example.minelivescoreadmin.web.requests.TournamentDto;

public class TournamentListAdapter extends RecyclerView.Adapter<TournamentListViewHolder> {

    private Context context;
    private List<TournamentDto> tournaments;
    private final OnItemListener onItemListener;

    public TournamentListAdapter(Context context, List<TournamentDto> tournaments, OnItemListener onItemListener) {
        this.context = context;
        this.tournaments = tournaments;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public TournamentListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.tournament_admin_item, parent, false);
        return new TournamentListViewHolder(view, onItemListener, context);
    }

    @Override
    public void onBindViewHolder(@NonNull TournamentListViewHolder holder, int position) {
        TournamentDto currentTournament = tournaments.get(position);
        if (currentTournament.getTournamentStatus().equals("FINISHED")) {
            // Set the desired appearance for FINISHED status
            ColorMatrix colorMatrix = new ColorMatrix();
            colorMatrix.setSaturation(0);
            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorMatrix);
            holder.tournamentLogo.setColorFilter(filter);
            holder.tournamentName.setTextColor(Color.parseColor("#FF757575"));
            holder.tournamentLocation.setTextColor(Color.parseColor("#FF757575"));
        } else {
            holder.tournamentLogo.setColorFilter(null);
            holder.tournamentName.setTextColor(Color.parseColor("#FFFFFFFF"));
            holder.tournamentLocation.setTextColor(Color.parseColor("#FFFFFFFF"));
        }

        holder.tournamentName.setText(currentTournament.getTournamentName());
        holder.tournamentLocation.setText(currentTournament.getTournamentLocation());
        holder.tournamentDto = currentTournament;

        Glide.with(context)
                .load(currentTournament.getTournamentLogo())
                .into(holder.tournamentLogo);

        holder.itemView.setOnClickListener(v -> onItemListener.onItemClick(currentTournament));
    }

    @Override
    public int getItemCount() {
        return tournaments.size();
    }

    public interface OnItemListener {
        void onItemClick(TournamentDto tournament);
    }
}
