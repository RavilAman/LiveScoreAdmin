package ravil.amangeldiuly.example.minelivescoreuser.groups;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;

import ravil.amangeldiuly.example.minelivescoreuser.R;

public class GroupViewHolder extends RecyclerView.ViewHolder {

    ShapeableImageView tournamentLogo;
    TextView tournamentName;
    TextView groupName;
    RecyclerView gamesRecyclerView;

    public GroupViewHolder(@NonNull View itemView) {
        super(itemView);

        tournamentLogo = itemView.findViewById(R.id.group_card_tournament_logo);
        tournamentName = itemView.findViewById(R.id.group_card_tournament_name);
        groupName = itemView.findViewById(R.id.group_card_group_name);
        gamesRecyclerView = itemView.findViewById(R.id.games_recycler_view);
    }
}
