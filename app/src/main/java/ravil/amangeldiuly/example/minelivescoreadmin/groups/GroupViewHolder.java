package ravil.amangeldiuly.example.minelivescoreadmin.groups;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;

import ravil.amangeldiuly.example.minelivescoreadmin.R;
import ravil.amangeldiuly.example.minelivescoreadmin.activities.StatisticsActivity;
import ravil.amangeldiuly.example.minelivescoreadmin.fragments.admin.TournamentCupInfoFragment;
import ravil.amangeldiuly.example.minelivescoreadmin.fragments.admin.TournamentLeagueInfoFragment;
import ravil.amangeldiuly.example.minelivescoreadmin.web.requests.TournamentDto;

public class GroupViewHolder extends RecyclerView.ViewHolder {

    Context context;
    private LinearLayout groupItemLayout;
    ShapeableImageView tournamentLogo;
    TextView tournamentName;
    TextView groupName;
    RecyclerView gamesRecyclerView;
    ImageButton statisticsButton;
    long tournamentId;
    long groupId;

    TournamentDto tournament;
    FragmentManager fragmentManager;

    public GroupViewHolder(@NonNull View itemView) {
        super(itemView);

        groupItemLayout = itemView.findViewById(R.id.group_item_layout);
        tournamentLogo = itemView.findViewById(R.id.group_card_tournament_logo);
        tournamentName = itemView.findViewById(R.id.group_card_tournament_name);
        groupName = itemView.findViewById(R.id.group_card_group_name);
        gamesRecyclerView = itemView.findViewById(R.id.games_recycler_view);
        statisticsButton = itemView.findViewById(R.id.group_item_statistics_button);

        statisticsButton.setOnClickListener(statisticsButtonListener());
        groupItemLayout.setOnClickListener(groupItemLayoutListener());
    }

    private View.OnClickListener statisticsButtonListener() {
        return view -> {
            Intent statisticsIntent = new Intent(context, StatisticsActivity.class);
            statisticsIntent.putExtra("tournamentId", tournamentId);
            statisticsIntent.putExtra("groupId", groupId);
            context.startActivity(statisticsIntent);
        };
    }

    private View.OnClickListener groupItemLayoutListener() {
        return view -> {
            if (tournament.getTournamentType().equals("CUP")){
                TournamentCupInfoFragment tournamentCupInfoFragment = new TournamentCupInfoFragment(fragmentManager,tournament);

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, tournamentCupInfoFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }else {
                TournamentLeagueInfoFragment tournamentLeagueInfoFragment = new TournamentLeagueInfoFragment(fragmentManager,tournament);
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, tournamentLeagueInfoFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        };
    }
}
