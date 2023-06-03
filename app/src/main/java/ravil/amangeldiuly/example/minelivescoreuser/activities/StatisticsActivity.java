package ravil.amangeldiuly.example.minelivescoreuser.activities;

import static ravil.amangeldiuly.example.minelivescoreuser.ColorConstants.APP_ORANGE;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
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

import ravil.amangeldiuly.example.minelivescoreuser.ColorConstants;
import ravil.amangeldiuly.example.minelivescoreuser.R;
import ravil.amangeldiuly.example.minelivescoreuser.UrlConstants;
import ravil.amangeldiuly.example.minelivescoreuser.enums.StatisticsType;
import ravil.amangeldiuly.example.minelivescoreuser.statistics.StatisticsAdapter;
import ravil.amangeldiuly.example.minelivescoreuser.utils.LocalDateTimeDeserializer;
import ravil.amangeldiuly.example.minelivescoreuser.web.apis.GroupStatisticsApi;
import ravil.amangeldiuly.example.minelivescoreuser.web.apis.PlayerStatisticsApi;
import ravil.amangeldiuly.example.minelivescoreuser.web.responses.DistinctPlayerStatisticsDTO;
import ravil.amangeldiuly.example.minelivescoreuser.web.responses.GroupInfoListDTO;
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

    private ImageView tournamentLogo;
    private TextView tournamentName;
    private TextView groupName;
    private TextView matches;
    private TextView table;
    private TextView playerStatistics;
    private TextView teamStatistics;
    private ImageButton backButton;
    private RecyclerView groupStatisticsRecyclerView;

    private int lastSelectedCategoryNumber;
    private int tournamentId;
    private int groupId;
    private List<String> categoriesList;
    private List<DistinctPlayerStatisticsDTO> assistStatistics;
    private List<DistinctPlayerStatisticsDTO> goalStatistics;
    private List<DistinctPlayerStatisticsDTO> redCardStatistics;
    private List<DistinctPlayerStatisticsDTO> yellowCardStatistics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

//        initializeApis();
        initializeRetrofit();
        initializeObjects();
        initializeViews();
        setOnClickListeners();

        table.performClick();
    }

//    private void initializeApis() {
//        RequestObject<GroupStatisticsApi> groupStatisticsApiRequestObject = new RequestObject<>(GroupStatisticsApi.class);
//        RequestObject<PlayerStatisticsApi> playerStatisticsApiRequestObject = new RequestObject<>(PlayerStatisticsApi.class);
//        groupStatisticsApi = groupStatisticsApiRequestObject.getApi();
//        playerStatisticsApi = playerStatisticsApiRequestObject.getApi();
//    }

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
    }

    private void initializeViews() {
        context = getApplicationContext();
        tournamentLogo = findViewById(R.id.activity_statistics_tournament_logo);
        tournamentName = findViewById(R.id.activity_statistics_tournament_name);
        groupName = findViewById(R.id.activity_statistics_group_name);
        groupStatisticsRecyclerView = findViewById(R.id.activity_statistics_statistics_recycler_vew);
        matches = findViewById(R.id.activity_statistics_matches);
        table = findViewById(R.id.activity_statistics_table);
        playerStatistics = findViewById(R.id.activity_statistics_player_statistics);
        teamStatistics = findViewById(R.id.activity_statistics_team_statistics);
        backButton = findViewById(R.id.activity_statistics_back_button);
    }

    public void initializeObjects() {
        Intent intent = getIntent();
        tournamentId = (int) intent.getExtras().getLong("tournamentId");
        groupId = (int) intent.getExtras().getLong("groupId");
        categoriesList = new ArrayList<>();
        assistStatistics = new ArrayList<>();
        goalStatistics = new ArrayList<>();
        redCardStatistics = new ArrayList<>();
        yellowCardStatistics = new ArrayList<>();
    }

    private void setGroupStatistics(List<GroupInfoListDTO> statistics) {
        StatisticsAdapter statisticsAdapter = new StatisticsAdapter(context, StatisticsType.GROUP);
        statisticsAdapter.setGroupStatisticsList(statistics);
        groupStatisticsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        groupStatisticsRecyclerView.setAdapter(statisticsAdapter);
    }

    private void setGeneralStatistics(List<List<DistinctPlayerStatisticsDTO>> statistics) {
        StatisticsAdapter statisticsAdapter = new StatisticsAdapter(context, StatisticsType.PLAYER);
        statisticsAdapter.setGeneralStatisticsList(statistics);
        groupStatisticsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        groupStatisticsRecyclerView.setAdapter(statisticsAdapter);
    }

    private void getTableStatistics(int tournamentId, int groupId) {
        groupStatisticsApi.getGroupStatistics(groupId, tournamentId).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<GroupInfoListDTO>> call, Response<List<GroupInfoListDTO>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    GroupInfoListDTO groupInfoListDTO = response.body().get(0);
                    setGroupStatistics(response.body());
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

    private void getPlayerAssistStatistics(int tournamentId) {
        assistStatistics.clear();
        playerStatisticsApi.getAssists(tournamentId).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<DistinctPlayerStatisticsDTO>> call, Response<List<DistinctPlayerStatisticsDTO>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    assistStatistics = response.body();
                }
            }

            @Override
            public void onFailure(Call<List<DistinctPlayerStatisticsDTO>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void getPlayerGoalStatistics(int tournamentId) {
        goalStatistics.clear();
        playerStatisticsApi.getGoals(tournamentId).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<DistinctPlayerStatisticsDTO>> call, Response<List<DistinctPlayerStatisticsDTO>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    goalStatistics = response.body();
                }
            }

            @Override
            public void onFailure(Call<List<DistinctPlayerStatisticsDTO>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void getPlayerRedCardStatistics(int tournamentId) {
        redCardStatistics.clear();
        playerStatisticsApi.getRedCards(tournamentId).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<DistinctPlayerStatisticsDTO>> call, Response<List<DistinctPlayerStatisticsDTO>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    redCardStatistics = response.body();
                }
            }

            @Override
            public void onFailure(Call<List<DistinctPlayerStatisticsDTO>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void getPlayerYellowCardStatistics(int tournamentId) {
        yellowCardStatistics.clear();
        playerStatisticsApi.getYellowCards(tournamentId).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<DistinctPlayerStatisticsDTO>> call, Response<List<DistinctPlayerStatisticsDTO>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    yellowCardStatistics = response.body();
                    setIndividualStatistics(yellowCardStatistics, "Yellow cards");
                }
            }

            @Override
            public void onFailure(Call<List<DistinctPlayerStatisticsDTO>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void setIndividualStatistics(List<DistinctPlayerStatisticsDTO> statistics, String statisticsCategory) {
        StatisticsAdapter statisticsAdapter = new StatisticsAdapter(context, StatisticsType.INDIVIDUAL);
        statisticsAdapter.setIndividualStatistics(statistics);
        statisticsAdapter.setIndividualCategoryName(statisticsCategory);
        groupStatisticsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        groupStatisticsRecyclerView.setAdapter(statisticsAdapter);
    }

    private void setTournamentData(String tournamentLogo, String tournamentName, String groupName) {
        Glide.with(context)
                .load(tournamentLogo)
                .into(this.tournamentLogo);
        this.tournamentName.setText(tournamentName);
        this.groupName.setText(groupName);
    }

    private void setOnClickListeners() {
        backButton.setOnClickListener(backButtonListener());
        matches.setOnClickListener(matcherListener());
        table.setOnClickListener(tableListener());
        playerStatistics.setOnClickListener(playerStatisticsListener());
        teamStatistics.setOnClickListener(teamStatisticsListener());
    }

    private View.OnClickListener backButtonListener() {
        return view -> finish();
    }

    private View.OnClickListener matcherListener() {
        return view -> {
            deSelectLastSelected();
            lastSelectedCategoryNumber = 0;
            matches.setTextColor(Color.parseColor(APP_ORANGE));
        };
    }

    private View.OnClickListener tableListener() {
        return view -> {
            deSelectLastSelected();
            lastSelectedCategoryNumber = 1;
            table.setTextColor(Color.parseColor(APP_ORANGE));
            getTableStatistics(tournamentId, groupId);
        };
    }

    private View.OnClickListener playerStatisticsListener() {
        return view -> {
            deSelectLastSelected();
            lastSelectedCategoryNumber = 2;
            playerStatistics.setTextColor(Color.parseColor(APP_ORANGE));
            getPlayerYellowCardStatistics(tournamentId);
        };
    }

    private View.OnClickListener teamStatisticsListener() {
        return view -> {
            deSelectLastSelected();
            lastSelectedCategoryNumber = 3;
            teamStatistics.setTextColor(Color.parseColor(APP_ORANGE));
        };
    }

    private void deSelectLastSelected() {
        switch (lastSelectedCategoryNumber) {
            case 0:
                matches.setTextColor(Color.parseColor(ColorConstants.WHITE));
                break;
            case 1:
                table.setTextColor(Color.parseColor(ColorConstants.WHITE));
                break;
            case 2:
                playerStatistics.setTextColor(Color.parseColor(ColorConstants.WHITE));
                break;
            case 3:
                teamStatistics.setTextColor(Color.parseColor(ColorConstants.WHITE));
                break;
        }
    }
}