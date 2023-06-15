package ravil.amangeldiuly.example.minelivescoreuser.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import ravil.amangeldiuly.example.minelivescoreuser.R;
import ravil.amangeldiuly.example.minelivescoreuser.UrlConstants;
import ravil.amangeldiuly.example.minelivescoreuser.utils.LocalDateTimeDeserializer;
import ravil.amangeldiuly.example.minelivescoreuser.web.apis.GroupApi;
import ravil.amangeldiuly.example.minelivescoreuser.web.apis.TeamApi;
import ravil.amangeldiuly.example.minelivescoreuser.web.apis.TournamentApi;
import ravil.amangeldiuly.example.minelivescoreuser.web.responses.GroupDTO;
import ravil.amangeldiuly.example.minelivescoreuser.web.responses.TeamDTO;
import ravil.amangeldiuly.example.minelivescoreuser.web.responses.TournamentDto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreateGameDialog extends AppCompatDialogFragment implements AdapterView.OnItemSelectedListener {

    private Context context;

    private View currentView;
    private Dialog createGameDialog;
    private Spinner tournamentSpinner;
    private Spinner groupSpinner;
    private Spinner homeTeamsSpinner;
    private Spinner awayTeamsSpinner;
    private ArrayAdapter tournamentsAdapter;
    private ArrayAdapter groupsAdapter;
    private ArrayAdapter homeTeamsAdapter;
    private ArrayAdapter awayTeamsAdapter;

    private TournamentDto selectedTournament;
    private GroupDTO selectedGroup;
    private TeamDTO selectedHomeTeam;
    private TeamDTO selectedAwayTeam;
    private List<TournamentDto> tournaments;
    private List<String> tournamentNames;
    private List<GroupDTO> groups;
    private List<String> groupNames;
    private List<TeamDTO> teams;
    private List<String> teamNames;

    private Retrofit retrofit;
    private TournamentApi tournamentApi;
    private GroupApi groupApi;
    private TeamApi teamApi;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        initializeRetrofit();
        initializeObjects();
        initializeViews();
        setFullWidth();
        return createGameDialog;
    }

    private void initializeRetrofit() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer())
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(UrlConstants.BACKEND_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        tournamentApi = retrofit.create(TournamentApi.class);
        groupApi = retrofit.create(GroupApi.class);
        teamApi = retrofit.create(TeamApi.class);
    }

    private void initializeViews() {
        context = requireContext();
        createGameDialog = new Dialog(context, R.style.CustomDialogTheme);
        currentView = getActivity().getLayoutInflater().inflate(R.layout.create_game_window, null);
        createGameDialog.setContentView(currentView);
        tournamentSpinner = currentView.findViewById(R.id.create_game_tournaments_spinner);
        tournamentSpinner.setOnItemSelectedListener(this);
        groupSpinner = currentView.findViewById(R.id.create_game_group_spinner);
        groupSpinner.setOnItemSelectedListener(this);
        homeTeamsSpinner = currentView.findViewById(R.id.create_game_home_teams_spinner);
        homeTeamsSpinner.setOnItemSelectedListener(this);
        awayTeamsSpinner = currentView.findViewById(R.id.create_game_away_teams_spinner);
        awayTeamsSpinner.setOnItemSelectedListener(this);
    }

    private void initializeObjects() {
        tournaments = new ArrayList<>();
        tournamentNames = new ArrayList<>();
        groups = new ArrayList<>();
        groupNames = new ArrayList<>();
        teams = new ArrayList<>();
        teamNames = new ArrayList<>();
        getTournaments();
    }

    private void setFullWidth() {
        Window window = createGameDialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(layoutParams);
        }
    }

    private void getTournaments() {
        tournaments.clear();
        tournamentNames.clear();
        // todo: убрать хардкод, юсер айди будет вшиваться в токен
        tournamentApi.findTournamentsByUser(1).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<TournamentDto>> call, Response<List<TournamentDto>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    tournaments = response.body();
                    setTournamentNames();
                    tournamentsAdapter = new ArrayAdapter<>(context, R.layout.tournaments_spinner_item, tournamentNames);
                    tournamentsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    tournamentSpinner.setAdapter(tournamentsAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<TournamentDto>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void getGroups() {
        groups.clear();
        groupNames.clear();
        groupApi.findGroupsByTournament(selectedTournament.getTournamentId().intValue()).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<GroupDTO>> call, Response<List<GroupDTO>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    groups = response.body();
                    setGroupNames();
                    groupsAdapter = new ArrayAdapter<>(context, R.layout.groups_spinner_item, groupNames);
                    groupsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    groupSpinner.setAdapter(groupsAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<GroupDTO>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void getTeams() {
        teams.clear();
        teamNames.clear();
        teamApi.findTeamsByTournamentAndGroups(
                selectedTournament.getTournamentId().intValue(),
                selectedGroup.getGroupId().intValue()
        ).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<TeamDTO>> call, Response<List<TeamDTO>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    teams = response.body();
                    setTeamNames();
                }
                homeTeamsAdapter = new ArrayAdapter<>(context, R.layout.groups_spinner_item, teamNames);
                homeTeamsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                homeTeamsSpinner.setAdapter(homeTeamsAdapter);

                awayTeamsAdapter = new ArrayAdapter<>(context, R.layout.groups_spinner_item, teamNames);
                awayTeamsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                awayTeamsSpinner.setAdapter(awayTeamsAdapter);
            }

            @Override
            public void onFailure(Call<List<TeamDTO>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.create_game_tournaments_spinner) {
            selectedTournament = identifyTournament(tournamentSpinner.getSelectedItem().toString());
            getGroups();
        }
        if (parent.getId() == R.id.create_game_group_spinner) {
            selectedGroup = identifyGroup(groupSpinner.getSelectedItem().toString());
            getTeams();
        }
        if (parent.getId() == R.id.create_game_home_teams_spinner) {
            selectedHomeTeam = identifyTeam(homeTeamsSpinner.getSelectedItem().toString());
        }
        if (parent.getId() == R.id.create_game_away_teams_spinner) {
            selectedAwayTeam = identifyTeam(awayTeamsSpinner.getSelectedItem().toString());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private TournamentDto identifyTournament(String tournamentName) {
        Optional<TournamentDto> answer = tournaments.stream()
                .filter(tournamentDto -> tournamentDto.getTournamentName().equals(tournamentName))
                .findFirst();
        return answer.orElse(null);
    }

    private GroupDTO identifyGroup(String groupName) {
        Optional<GroupDTO> answer = groups.stream()
                .filter(groupDTO -> groupDTO.getGroupName().equals(groupName))
                .findFirst();
        return answer.orElse(null);
    }

    private TeamDTO identifyTeam(String teamName) {
        Optional<TeamDTO> answer = teams.stream()
                .filter(teamDTO -> teamDTO.getTeamName().equals(teamName))
                .findFirst();
        return answer.orElse(null);
    }

    private void setTournamentNames() {
        tournamentNames = tournaments.stream()
                .map(TournamentDto::getTournamentName)
                .collect(Collectors.toList());
    }

    private void setGroupNames() {
        groupNames = groups.stream()
                .map(GroupDTO::getGroupName)
                .collect(Collectors.toList());
    }

    private void setTeamNames() {
        teamNames = teams.stream()
                .map(TeamDTO::getTeamName)
                .collect(Collectors.toList());
    }
}
