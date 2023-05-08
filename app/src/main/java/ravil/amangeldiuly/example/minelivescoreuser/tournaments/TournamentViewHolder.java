package ravil.amangeldiuly.example.minelivescoreuser.tournaments;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;

import ravil.amangeldiuly.example.minelivescoreuser.R;

public class TournamentViewHolder extends RecyclerView.ViewHolder {

    ShapeableImageView tournamentLogo;
    TextView tournamentName;
    TextView tournamentLocation;
    TournamentAdapter.OnItemListener onItemListener;
    long tournamentId;
    ImageButton imageButton;
    boolean imageButtonPressed = false;

    public TournamentViewHolder(@NonNull View itemView, TournamentAdapter.OnItemListener onItemListener) {
        super(itemView);
        this.onItemListener = onItemListener;

        tournamentLogo = itemView.findViewById(R.id.card_tournament_logo);
        tournamentName = itemView.findViewById(R.id.card_tournament_name);
        tournamentLocation = itemView.findViewById(R.id.card_tournament_location);
        imageButton = itemView.findViewById(R.id.toggle_favourites_button);

        imageButton.setOnClickListener(v -> {
            onItemListener.onItemClick(tournamentId);
            imageButtonPressed = !imageButtonPressed;
            if (imageButtonPressed) {
                imageButton.setImageResource(R.drawable.ic_baseline_star_24);
            } else {
                imageButton.setImageResource(R.drawable.ic_baseline_star_border_24);
            }
        });
    }
}
