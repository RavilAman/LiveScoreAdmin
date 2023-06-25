package ravil.amangeldiuly.example.minelivescoreadmin.activities;

import static ravil.amangeldiuly.example.minelivescoreadmin.ColorConstants.APP_ORANGE;
import static ravil.amangeldiuly.example.minelivescoreadmin.enums.SelectedStatisticsClass.PLAYER;
import static ravil.amangeldiuly.example.minelivescoreadmin.enums.SelectedStatisticsClass.TEAM;
import static ravil.amangeldiuly.example.minelivescoreadmin.enums.StatisticsType.GENERAL_PLAYER;
import static ravil.amangeldiuly.example.minelivescoreadmin.enums.StatisticsType.GENERAL_TEAM;
import static ravil.amangeldiuly.example.minelivescoreadmin.enums.StatisticsType.GROUP;
import static ravil.amangeldiuly.example.minelivescoreadmin.enums.StatisticsType.INDIVIDUAL_PLAYER;
import static ravil.amangeldiuly.example.minelivescoreadmin.enums.StatisticsType.INDIVIDUAL_TEAM;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import ravil.amangeldiuly.example.minelivescoreadmin.ColorConstants;
import ravil.amangeldiuly.example.minelivescoreadmin.R;
import ravil.amangeldiuly.example.minelivescoreadmin.UrlConstants;
import ravil.amangeldiuly.example.minelivescoreadmin.enums.SelectedStatisticsClass;
import ravil.amangeldiuly.example.minelivescoreadmin.enums.StatisticsType;
import ravil.amangeldiuly.example.minelivescoreadmin.statistics.StatisticsAdapter;
import ravil.amangeldiuly.example.minelivescoreadmin.utils.LocalDateTimeDeserializer;
import ravil.amangeldiuly.example.minelivescoreadmin.web.apis.GroupStatisticsApi;
import ravil.amangeldiuly.example.minelivescoreadmin.web.apis.PlayerStatisticsApi;
import ravil.amangeldiuly.example.minelivescoreadmin.web.apis.TeamStatisticsApi;
import ravil.amangeldiuly.example.minelivescoreadmin.web.requests.DistinctPlayerStatisticsDTO;
import ravil.amangeldiuly.example.minelivescoreadmin.web.requests.DistinctTeamStatisticsDTO;
import ravil.amangeldiuly.example.minelivescoreadmin.web.requests.GroupInfoListDTO;
import ravil.amangeldiuly.example.minelivescoreadmin.web.requests.PlayerStatisticsAllDTO;
import ravil.amangeldiuly.example.minelivescoreadmin.web.requests.TeamStatisticsAllDTO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StatisticsActivity extends AppCompatActivity {

    private Context context;

    private Retrofit retrofit;
    private GroupStatisticsApi groupStatisticsApi;
    private PlayerStatisticsApi playerStatisticsApi;
    private TeamStatisticsApi teamStatisticsApi;

    private ImageView tournamentLogo;
    private TextView tournamentName;
    private TextView groupName;
    private TextView table;
    private TextView playerStatistics;
    private TextView teamStatistics;
    private ImageButton backButton;
    private LinearLayout statisticTypes;
    private TextView statisticTypeAll;
    private TextView statisticTypeAssists;
    private TextView statisticTypeGoals;
    private TextView statisticTypeRedCards;
    private TextView statisticTypeYellowCards;

    private RecyclerView groupStatisticsRecyclerView;
    private StatisticsAdapter statisticsAdapter;

    private StatisticsType currentlySelectedStatisticType;
    private SelectedStatisticsClass selectedStatisticsClass;
    private int lastSelectedCategoryNumber;
    private int tournamentId;
    private int groupId;
    private int lastSelectedStatisticTypeNumber;
    private List<GroupInfoListDTO> groupStatistics;
    private List<PlayerStatisticsAllDTO> playerStatisticsAll;
    private List<DistinctPlayerStatisticsDTO> playerAssistStatistics;
    private List<DistinctPlayerStatisticsDTO> playerGoalStatistics;
    private List<DistinctPlayerStatisticsDTO> playerRedCardStatistics;
    private List<DistinctPlayerStatisticsDTO> playerYellowCardStatistics;
    private List<TeamStatisticsAllDTO> teamStatisticsAll;
    private List<DistinctTeamStatisticsDTO> teamGoalStatistics;
    private List<DistinctTeamStatisticsDTO> teamRedCardStatistics;
    private List<DistinctTeamStatisticsDTO> teamYellowCardStatistics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        initializeRetrofit();
        initializeObjects();
        initializeViews();
        setOnClickListeners();

        table.performClick();
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
        groupStatisticsApi = retrofit.create(GroupStatisticsApi.class);
        playerStatisticsApi = retrofit.create(PlayerStatisticsApi.class);
        teamStatisticsApi = retrofit.create(TeamStatisticsApi.class);
    }

    private void initializeViews() {
        context = getApplicationContext();
        tournamentLogo = findViewById(R.id.activity_statistics_tournament_logo);
        tournamentName = findViewById(R.id.activity_statistics_tournament_name);
        groupName = findViewById(R.id.activity_statistics_group_name);
        groupStatisticsRecyclerView = findViewById(R.id.activity_statistics_statistics_recycler_vew);
        table = findViewById(R.id.activity_statistics_table);
        playerStatistics = findViewById(R.id.activity_statistics_player_statistics);
        teamStatistics = findViewById(R.id.activity_statistics_team_statistics);
        backButton = findViewById(R.id.activity_statistics_back_button);
        statisticTypes = findViewById(R.id.activity_statistics_statistics_types);
        statisticTypeAll = findViewById(R.id.activity_statistics_type_all);
        statisticTypeAssists = findViewById(R.id.activity_statistics_type_assists);
        statisticTypeGoals = findViewById(R.id.activity_statistics_type_goals);
        statisticTypeRedCards = findViewById(R.id.activity_statistics_type_red_cards);
        statisticTypeYellowCards = findViewById(R.id.activity_statistics_type_yellow_cards);
    }

    public void initializeObjects() {
        Intent intent = getIntent();
        tournamentId = (int) intent.getExtras().getLong("tournamentId");
        groupId = (int) intent.getExtras().getLong("groupId");
        groupStatistics = new ArrayList<>();
        playerStatisticsAll = new ArrayList<>();
        playerAssistStatistics = new ArrayList<>();
        playerGoalStatistics = new ArrayList<>();
        playerRedCardStatistics = new ArrayList<>();
        playerYellowCardStatistics = new ArrayList<>();
        teamGoalStatistics = new ArrayList<>();
        teamRedCardStatistics = new ArrayList<>();
        teamYellowCardStatistics = new ArrayList<>();
        teamStatisticsAll = new ArrayList<>();
    }

    private void setOnClickListeners() {
        backButton.setOnClickListener(backButtonListener());
        table.setOnClickListener(tableListener());
        playerStatistics.setOnClickListener(playerStatisticsListener());
        teamStatistics.setOnClickListener(teamStatisticsListener());
        statisticTypeAll.setOnClickListener(statisticTypeAllListener());
        statisticTypeAssists.setOnClickListener(statisticTypeAssistsListener());
        statisticTypeGoals.setOnClickListener(statisticTypeGoalsListener());
        statisticTypeRedCards.setOnClickListener(statisticTypeRedCardsListener());
        statisticTypeYellowCards.setOnClickListener(statisticTypeYellowCardsListener());
    }

    private void setStatistics(
            List<DistinctPlayerStatisticsDTO> individualPlayerStatistics,
            List<DistinctTeamStatisticsDTO> individualTeamStatistics,
            String statisticsCategory) {
        statisticsAdapter = new StatisticsAdapter(context, currentlySelectedStatisticType);
        switch (currentlySelectedStatisticType) {
            case GROUP:
                statisticsAdapter.setGroupStatisticsList(groupStatistics);
                break;
            case GENERAL_PLAYER:
                statisticsAdapter.setGeneralPlayerStatistics(playerStatisticsAll);
                break;
            case GENERAL_TEAM:
                statisticsAdapter.setGeneralTeamStatistic(teamStatisticsAll);
                break;
            case INDIVIDUAL_PLAYER:
                statisticsAdapter.setIndividualPlayerStatistics(individualPlayerStatistics);
                statisticsAdapter.setIndividualCategoryName(statisticsCategory);
                break;
            case INDIVIDUAL_TEAM:
                statisticsAdapter.setIndividualTeamStatistics(individualTeamStatistics);
                statisticsAdapter.setIndividualCategoryName(statisticsCategory);
                break;
        }
        groupStatisticsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        groupStatisticsRecyclerView.setAdapter(statisticsAdapter);
    }

    private void getTableStatisticsForGroup(int tournamentId, int groupId) {
        groupStatistics.clear();
        groupStatisticsApi.getGroupStatistics(groupId, tournamentId).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<GroupInfoListDTO>> call, Response<List<GroupInfoListDTO>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    GroupInfoListDTO groupInfoListDTO = response.body().get(0);
                    groupStatistics = response.body();
                    setStatistics(null, null, null);
                    setTournamentData(
                            groupInfoListDTO.getTournamentLogo(),
                            groupInfoListDTO.getTournamentName(),
                            groupInfoListDTO.getGroupName()
                    );
                }
            }

            @Override
            public void onFailure(Call<List<GroupInfoListDTO>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void getTableStatisticsForTournament(int tournamentId) {
        groupStatistics.clear();
        groupStatisticsApi.getStatisticsForTournament(tournamentId).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<GroupInfoListDTO>> call, Response<List<GroupInfoListDTO>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    GroupInfoListDTO groupInfoListDTO = response.body().get(0);
                    groupStatistics = response.body();
                    setStatistics(null, null, null);
                    setTournamentData(
                            groupInfoListDTO.getTournamentLogo(),
                            groupInfoListDTO.getTournamentName(),
                            groupInfoListDTO.getGroupName()
                    );
                }
            }

            @Override
            public void onFailure(Call<List<GroupInfoListDTO>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void getPlayerStatisticsAll() {
        playerStatisticsAll.clear();
        playerStatisticsApi.getAll(tournamentId).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<PlayerStatisticsAllDTO>> call, Response<List<PlayerStatisticsAllDTO>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    playerStatisticsAll = response.body();
                    setStatistics(null, null, null);
                }
            }

            @Override
            public void onFailure(Call<List<PlayerStatisticsAllDTO>> call, Throwable t) {

            }
        });
    }

    private void getPlayerAssistStatistics() {
        playerAssistStatistics.clear();
        playerStatisticsApi.getAssists(tournamentId).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<DistinctPlayerStatisticsDTO>> call, Response<List<DistinctPlayerStatisticsDTO>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    playerAssistStatistics = response.body();
                    setStatistics(playerAssistStatistics, null, "ASSIST");
                }
            }

            @Override
            public void onFailure(Call<List<DistinctPlayerStatisticsDTO>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void getPlayerGoalStatistics() {
        playerGoalStatistics.clear();
        playerStatisticsApi.getGoals(tournamentId).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<DistinctPlayerStatisticsDTO>> call, Response<List<DistinctPlayerStatisticsDTO>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    playerGoalStatistics = response.body();
                    setStatistics(playerGoalStatistics, null, "GOAL");
                }
            }

            @Override
            public void onFailure(Call<List<DistinctPlayerStatisticsDTO>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void getPlayerRedCardStatistics() {
        playerRedCardStatistics.clear();
        playerStatisticsApi.getRedCards(tournamentId).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<DistinctPlayerStatisticsDTO>> call, Response<List<DistinctPlayerStatisticsDTO>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    playerRedCardStatistics = response.body();
                    setStatistics(playerRedCardStatistics, null, "RED CARD");
                }
            }

            @Override
            public void onFailure(Call<List<DistinctPlayerStatisticsDTO>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void getPlayerYellowCardStatistics() {
        playerYellowCardStatistics.clear();
        playerStatisticsApi.getYellowCards(tournamentId).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<DistinctPlayerStatisticsDTO>> call, Response<List<DistinctPlayerStatisticsDTO>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    playerYellowCardStatistics = response.body();
                    setStatistics(playerYellowCardStatistics, null, "YELLOW CARD");
                }
            }

            @Override
            public void onFailure(Call<List<DistinctPlayerStatisticsDTO>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void setTournamentData(String tournamentLogo, String tournamentName, String groupName) {
        Glide.with(context)
                .load(tournamentLogo)
                .into(this.tournamentLogo);
        this.tournamentName.setText(tournamentName);
        this.groupName.setText(groupName);
    }

    private View.OnClickListener backButtonListener() {
        return view -> finish();
    }

    private View.OnClickListener tableListener() {
        return view -> {
            currentlySelectedStatisticType = GROUP;
            statisticTypes.setVisibility(View.GONE);
            deSelectLastSelectedCategory();
            lastSelectedCategoryNumber = 1;
            table.setTextColor(Color.parseColor(APP_ORANGE));
            if (groupId == -1) {
                getTableStatisticsForTournament(tournamentId);
            } else {
                getTableStatisticsForGroup(tournamentId, groupId);
            }
        };
    }

    private View.OnClickListener playerStatisticsListener() {
        return view -> {
            statisticTypeAssists.setVisibility(View.VISIBLE);
            currentlySelectedStatisticType = GENERAL_PLAYER;
            selectedStatisticsClass = PLAYER;
            deSelectLastSelectedCategory();
            lastSelectedCategoryNumber = 2;
            playerStatistics.setTextColor(Color.parseColor(APP_ORANGE));
            statisticTypes.setVisibility(View.VISIBLE);
            statisticTypeAll.performClick();
        };
    }

    private View.OnClickListener teamStatisticsListener() {
        return view -> {
            currentlySelectedStatisticType = GENERAL_TEAM;
            selectedStatisticsClass = TEAM;
            deSelectLastSelectedCategory();
            lastSelectedCategoryNumber = 3;
            statisticTypes.setVisibility(View.VISIBLE);
            teamStatistics.setTextColor(Color.parseColor(APP_ORANGE));
            statisticTypeAssists.setVisibility(View.GONE);
            statisticTypeAll.performClick();
        };
    }

    private View.OnClickListener statisticTypeAllListener() {
        return view -> {
            deSelectLastSelectedStatisticType();
            lastSelectedStatisticTypeNumber = 0;
            makeStatisticSelected(statisticTypeAll);
            if (selectedStatisticsClass == TEAM) {
                currentlySelectedStatisticType = GENERAL_TEAM;
                getTeamStatisticsAll();
            } else if (selectedStatisticsClass == PLAYER) {
                currentlySelectedStatisticType = GENERAL_PLAYER;
                getPlayerStatisticsAll();
            }
        };
    }

    private View.OnClickListener statisticTypeAssistsListener() {
        return view -> {
            currentlySelectedStatisticType = INDIVIDUAL_PLAYER;
            deSelectLastSelectedStatisticType();
            getPlayerAssistStatistics();
            lastSelectedStatisticTypeNumber = 1;
            makeStatisticSelected(statisticTypeAssists);
        };
    }

    private View.OnClickListener statisticTypeGoalsListener() {
        return view -> {
            deSelectLastSelectedStatisticType();
            lastSelectedStatisticTypeNumber = 2;
            makeStatisticSelected(statisticTypeGoals);
            if (selectedStatisticsClass == TEAM) {
                currentlySelectedStatisticType = INDIVIDUAL_TEAM;
                getTeamGoalStatistics();
            } else if (selectedStatisticsClass == PLAYER) {
                currentlySelectedStatisticType = INDIVIDUAL_PLAYER;
                getPlayerGoalStatistics();
            }
        };
    }

    private View.OnClickListener statisticTypeRedCardsListener() {
        return view -> {
            deSelectLastSelectedStatisticType();
            lastSelectedStatisticTypeNumber = 3;
            makeStatisticSelected(statisticTypeRedCards);
            if (selectedStatisticsClass == TEAM) {
                currentlySelectedStatisticType = INDIVIDUAL_TEAM;
                getTeamRedCardStatistics();
            } else if (selectedStatisticsClass == PLAYER) {
                currentlySelectedStatisticType = INDIVIDUAL_PLAYER;
                getPlayerRedCardStatistics();
            }
        };
    }

    private View.OnClickListener statisticTypeYellowCardsListener() {
        return view -> {
            deSelectLastSelectedStatisticType();
            lastSelectedStatisticTypeNumber = 4;
            makeStatisticSelected(statisticTypeYellowCards);
            if (selectedStatisticsClass == TEAM) {
                currentlySelectedStatisticType = INDIVIDUAL_TEAM;
                getTeamYellowCardStatistics();
            } else if (selectedStatisticsClass == PLAYER) {
                currentlySelectedStatisticType = INDIVIDUAL_PLAYER;
                getPlayerYellowCardStatistics();
            }
        };
    }

    @SuppressLint("NotifyDataSetChanged")
    private void deSelectLastSelectedCategory() {
        switch (lastSelectedCategoryNumber) {
            case 1:
                groupStatistics.clear();
                statisticsAdapter.notifyDataSetChanged();
                table.setTextColor(Color.parseColor(ColorConstants.WHITE));
                break;
            case 2:
                playerStatisticsAll.clear();
                playerAssistStatistics.clear();
                playerGoalStatistics.clear();
                playerRedCardStatistics.clear();
                playerYellowCardStatistics.clear();
                statisticsAdapter.notifyDataSetChanged();
                playerStatistics.setTextColor(Color.parseColor(ColorConstants.WHITE));
                break;
            case 3:
                teamStatisticsAll.clear();
                teamGoalStatistics.clear();
                teamRedCardStatistics.clear();
                teamYellowCardStatistics.clear();
                statisticsAdapter.notifyDataSetChanged();
                teamStatistics.setTextColor(Color.parseColor(ColorConstants.WHITE));
                break;
        }
    }

    private void deSelectLastSelectedStatisticType() {
        switch (lastSelectedStatisticTypeNumber) {
            case 0:
                makeStatisticDefault(statisticTypeAll);
                break;
            case 1:
                makeStatisticDefault(statisticTypeAssists);
                break;
            case 2:
                makeStatisticDefault(statisticTypeGoals);
                break;
            case 3:
                makeStatisticDefault(statisticTypeRedCards);
                break;
            case 4:
                makeStatisticDefault(statisticTypeYellowCards);
                break;
        }
    }

    private void makeStatisticSelected(TextView statisticType) {
        statisticType.setBackgroundResource(R.drawable.statistics_category_background);
    }

    private void makeStatisticDefault(TextView statisticType) {
        statisticType.setBackground(null);
    }

    private void getTeamStatisticsAll() {
        teamStatisticsAll.clear();
        teamStatisticsApi.getAll(tournamentId).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<TeamStatisticsAllDTO>> call, Response<List<TeamStatisticsAllDTO>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    teamStatisticsAll = response.body();
                    setStatistics(null, null, null);
                }
            }

            @Override
            public void onFailure(Call<List<TeamStatisticsAllDTO>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void getTeamGoalStatistics() {
        teamGoalStatistics.clear();
        teamStatisticsApi.getGoals(tournamentId).enqueue(new Callback<List<DistinctTeamStatisticsDTO>>() {
            @Override
            public void onResponse(Call<List<DistinctTeamStatisticsDTO>> call, Response<List<DistinctTeamStatisticsDTO>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    teamGoalStatistics = response.body();
                    setStatistics(null, teamGoalStatistics, "GOALS");
                }
            }

            @Override
            public void onFailure(Call<List<DistinctTeamStatisticsDTO>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void getTeamRedCardStatistics() {
        teamRedCardStatistics.clear();
        teamStatisticsApi.getRedCards(tournamentId).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<DistinctTeamStatisticsDTO>> call, Response<List<DistinctTeamStatisticsDTO>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    teamRedCardStatistics = response.body();
                    setStatistics(null, teamRedCardStatistics, "RED CARDS");
                }
            }

            @Override
            public void onFailure(Call<List<DistinctTeamStatisticsDTO>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void getTeamYellowCardStatistics() {
        teamYellowCardStatistics.clear();
        teamStatisticsApi.getYellowCards(tournamentId).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<DistinctTeamStatisticsDTO>> call, Response<List<DistinctTeamStatisticsDTO>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    teamYellowCardStatistics = response.body();
                    setStatistics(null, teamYellowCardStatistics, "YELLOW CARDS");
                }
            }

            @Override
            public void onFailure(Call<List<DistinctTeamStatisticsDTO>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}