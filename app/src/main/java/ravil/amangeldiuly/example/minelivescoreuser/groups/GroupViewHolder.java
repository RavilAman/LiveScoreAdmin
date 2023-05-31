package ravil.amangeldiuly.example.minelivescoreuser.groups;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;

import ravil.amangeldiuly.example.minelivescoreuser.R;

public class GroupViewHolder extends RecyclerView.ViewHolder {

    private LinearLayout groupItemLayout;
    ShapeableImageView tournamentLogo;
    TextView tournamentName;
    TextView groupName;
    RecyclerView gamesRecyclerView;
    private GroupAdapter.OnItemListener onItemListener;
    long tournamentId;
    long groupId;

    public GroupViewHolder(@NonNull View itemView, GroupAdapter.OnItemListener onItemListener) {
        super(itemView);

        groupItemLayout = itemView.findViewById(R.id.group_item_layout);
        this.onItemListener = onItemListener;
        tournamentLogo = itemView.findViewById(R.id.group_card_tournament_logo);
        tournamentName = itemView.findViewById(R.id.group_card_tournament_name);
        groupName = itemView.findViewById(R.id.group_card_group_name);
        gamesRecyclerView = itemView.findViewById(R.id.games_recycler_view);

        groupItemLayout.setOnClickListener(groupItemLayoutListener());
    }

    private View.OnClickListener groupItemLayoutListener() {
        return view -> {
            Bundle data = new Bundle();
            data.putLong("tournamentId", tournamentId);
            data.putLong("groupId", groupId);
            onItemListener.onItemClick(data);
        };
    }
}
