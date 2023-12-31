package ravil.amangeldiuly.example.minelivescoreadmin.fragments.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

import ravil.amangeldiuly.example.minelivescoreadmin.R;
import ravil.amangeldiuly.example.minelivescoreadmin.teams.TeamsAdapter;
import ravil.amangeldiuly.example.minelivescoreadmin.web.RequestHandler;
import ravil.amangeldiuly.example.minelivescoreadmin.web.apis.TeamApi;
import ravil.amangeldiuly.example.minelivescoreadmin.web.requests.TeamDTO;
import ravil.amangeldiuly.example.minelivescoreadmin.web.requests.TournamentDto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TransfersFragment extends Fragment implements TeamsAdapter.OnItemListener {

    private View currentView;
    private Retrofit retrofit;
    private RecyclerView teamsRecyclerView;
    private List<TeamDTO> teamList;
    private TeamApi teamApi;
    private ImageButton imageButton;
    private FragmentManager fragmentManager;
    private ShapeableImageView tournamentLogo;
    private TextView tournamentName;
    private TextView tournamentGroup;
    private TournamentDto tournamentDto;


    public TransfersFragment(TournamentDto tournamentDto, FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
        this.tournamentDto = tournamentDto;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.fragment_transfers, container, false);

        teamList = new ArrayList<>();

        teamsRecyclerView = currentView.findViewById(R.id.list_teams);
        imageButton = currentView.findViewById(R.id.fragment_back_button);
        imageButton.setOnClickListener(backButtonListener());
        tournamentName = currentView.findViewById(R.id.fragment_tournament_name);
        tournamentLogo = currentView.findViewById(R.id.fragment_tournament_logo);
        tournamentGroup = currentView.findViewById(R.id.fragment_tournament_group);

        initializeRetrofit();
        findAllTeams();


        tournamentName.setText(tournamentDto.getTournamentName());
        tournamentGroup.setText(tournamentDto.getTournamentLocation());
        Glide.with(this)
                .load(tournamentDto.getTournamentLogo())
                .into(tournamentLogo);

        return currentView;
    }

    private void initializeRetrofit() {
        RequestHandler requestHandler = new RequestHandler(getContext());
        retrofit = requestHandler.getRetrofit();
        teamApi = retrofit.create(TeamApi.class);
    }

    private void findAllTeams() {
        teamList.clear();


        Callback<List<TeamDTO>> callback = new Callback<>() {
            @Override
            public void onResponse(Call<List<TeamDTO>> call, Response<List<TeamDTO>> response) {
                if (response.body() != null) {
                    teamList = response.body();
                    createTeamCards(teamList);
                }
            }

            @Override
            public void onFailure(Call<List<TeamDTO>> call, Throwable t) {
                t.printStackTrace();
            }
        };
        teamApi.findAllTeamsByTournament(tournamentDto.getTournamentId()).enqueue(callback);

    }

    private void createTeamCards(List<TeamDTO> teamList) {
        TeamsAdapter teamsAdapter = new TeamsAdapter(getLayoutInflater().getContext(), teamList, this);
        teamsRecyclerView.setLayoutManager(new LinearLayoutManager(getLayoutInflater().getContext()));
        teamsRecyclerView.setAdapter(teamsAdapter);
    }

    private View.OnClickListener backButtonListener() {
        return view -> {

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentManager.popBackStack();
            fragmentTransaction.commit();
        };
    }

    @Override
    public void onItemClick(TeamDTO team) {
        Fragment fragment = new PlayerListFragment(team, fragmentManager,teamList);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }
}
