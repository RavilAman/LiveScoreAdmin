package ravil.amangeldiuly.example.minelivescoreuser.groups;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;

import ravil.amangeldiuly.example.minelivescoreuser.R;
import ravil.amangeldiuly.example.minelivescoreuser.activities.StatisticsActivity;

public class GroupViewHolder extends RecyclerView.ViewHolder {

    Context context;
    private LinearLayout groupItemLayout;
    ShapeableImageView tournamentLogo;
    TextView tournamentName;
    TextView groupName;
    RecyclerView gamesRecyclerView;
    long tournamentId;
    long groupId;

    public GroupViewHolder(@NonNull View itemView) {
        super(itemView);

        groupItemLayout = itemView.findViewById(R.id.group_item_layout);
        tournamentLogo = itemView.findViewById(R.id.group_card_tournament_logo);
        tournamentName = itemView.findViewById(R.id.group_card_tournament_name);
        groupName = itemView.findViewById(R.id.group_card_group_name);
        gamesRecyclerView = itemView.findViewById(R.id.games_recycler_view);

        groupItemLayout.setOnClickListener(groupItemLayoutListener());
    }

    private View.OnClickListener groupItemLayoutListener() {
        return view -> {
            Intent statisticsIntent = new Intent(context, StatisticsActivity.class);
            statisticsIntent.putExtra("tournamentId", tournamentId);
            statisticsIntent.putExtra("groupId", groupId);
            context.startActivity(statisticsIntent);
        };
    }
}
