package ravil.amangeldiuly.example.minelivescoreadmin.spinner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

import ravil.amangeldiuly.example.minelivescoreadmin.R;
import ravil.amangeldiuly.example.minelivescoreadmin.enums.SpinnerSelected;
import ravil.amangeldiuly.example.minelivescoreadmin.web.requests.GroupDTO;
import ravil.amangeldiuly.example.minelivescoreadmin.web.requests.PlayerDTO;
import ravil.amangeldiuly.example.minelivescoreadmin.web.requests.TeamDTO;
import ravil.amangeldiuly.example.minelivescoreadmin.web.requests.TournamentDto;

public class UniversalSpinnerAdapter extends BaseAdapter {

    private Context context;
    private List<TournamentDto> tournaments;
    private List<GroupDTO> groups;
    private List<TeamDTO> teams;
    private List<PlayerDTO> players;
    private SpinnerSelected spinnerSelected;

    public UniversalSpinnerAdapter(Context context, SpinnerSelected spinnerSelected) {
        this.context = context;
        this.spinnerSelected = spinnerSelected;
    }

    @Override
    public int getCount() {
        switch (spinnerSelected) {
            case TOURNAMENT:
                return tournaments.size();
            case GROUP:
                return groups.size();
            case PLAYER:
                return players.size();
            case TEAM:
                return teams.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        @SuppressLint("ViewHolder")
        View rootView = LayoutInflater.from(context)
                .inflate(R.layout.spinner_item, parent, false);
        ShapeableImageView logo = rootView.findViewById(R.id.spinner_item_logo);
        TextView number = rootView.findViewById(R.id.spinner_item_number);
        TextView name = rootView.findViewById(R.id.spinner_item_name);
        switch (spinnerSelected) {
            case TOURNAMENT:
                number.setVisibility(View.GONE);
                TournamentDto tournament = tournaments.get(position);
                Glide.with(context)
                        .load(tournament.getTournamentLogo())
                        .into(logo);
                name.setText(tournament.getTournamentName());
                break;
            case GROUP:
                logo.setVisibility(View.GONE);
                number.setVisibility(View.GONE);
                GroupDTO group = groups.get(position);
                name.setPadding(50, 0, 0, 0);
                name.setText(group.getGroupName());
                break;
            case PLAYER:
                logo.setVisibility(View.GONE);
                PlayerDTO player = players.get(position);
                number.setText(position != 0
                        ? String.valueOf(player.getPlayerNumber())
                        : "");
                name.setText(player.fullNameNameFirst());
                break;
            case TEAM:
                number.setVisibility(View.GONE);
                TeamDTO team = teams.get(position);
                Glide.with(context)
                        .load(team.getTeamLogo())
                        .into(logo);
                name.setText(team.getTeamName());
                break;
        }
        return rootView;
    }

    public void setTournaments(List<TournamentDto> tournaments) {
        this.tournaments = tournaments;
    }

    public void setGroups(List<GroupDTO> groups) {
        this.groups = groups;
    }

    public void setTeams(List<TeamDTO> teams) {
        this.teams = teams;
    }

    public void setPlayers(List<PlayerDTO> players) {
        this.players = players;
    }
}
