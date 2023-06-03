package ravil.amangeldiuly.example.minelivescoreuser.activities;

import static ravil.amangeldiuly.example.minelivescoreuser.ColorConstants.APP_ORANGE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDateTime;
import java.util.List;

import ravil.amangeldiuly.example.minelivescoreuser.R;
import ravil.amangeldiuly.example.minelivescoreuser.UrlConstants;
import ravil.amangeldiuly.example.minelivescoreuser.statistics.GroupsStatisticsAdapter;
import ravil.amangeldiuly.example.minelivescoreuser.utils.LocalDateTimeDeserializer;
import ravil.amangeldiuly.example.minelivescoreuser.web.apis.GroupApi;
import ravil.amangeldiuly.example.minelivescoreuser.web.responses.GroupInfoListDTO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StatisticsActivity extends AppCompatActivity {

    private Context context;

    private Retrofit retrofit;
    private GroupApi groupApi;

    private ImageView tournamentLogo;
    private TextView tournamentName;
    private TextView groupName;
    private TextView matches;
    private TextView table;
    private TextView playerStatistics;
    private TextView teamStatistics;
    private ImageButton backButton;
    private RecyclerView groupStatisticsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        initializeRetrofit();
        initializeViews();
        setOnClickListeners();

        Intent intent = getIntent();
        int tournamentId = (int) intent.getExtras().getLong("tournamentId");
        int groupId = (int) intent.getExtras().getLong("groupId");
        getAndSetStatistics(tournamentId, groupId);

        table.setTextColor(Color.parseColor(APP_ORANGE));
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
        groupApi = retrofit.create(GroupApi.class);
    }

    private void initializeViews() {
        context = getApplicationContext();
        tournamentLogo = findViewById(R.id.activity_statistics_tournament_logo);
        tournamentName = findViewById(R.id.activity_statistics_tournament_name);
        groupName = findViewById(R.id.activity_statistics_group_name);
        groupStatisticsRecyclerView = findViewById(R.id.activity_statistics_group_statistics_recycler_vew);
        matches = findViewById(R.id.activity_statistics_matches);
        table = findViewById(R.id.activity_statistics_table);
        playerStatistics = findViewById(R.id.activity_statistics_player_statistics);
        teamStatistics = findViewById(R.id.activity_statistics_team_statistics);
        backButton = findViewById(R.id.activity_statistics_back_button);
    }

    private void setStatistics(List<GroupInfoListDTO> statistics) {
        GroupsStatisticsAdapter groupsStatisticsAdapter = new GroupsStatisticsAdapter(context, statistics);
        groupStatisticsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        groupStatisticsRecyclerView.setAdapter(groupsStatisticsAdapter);
    }

    private void getAndSetStatistics(int tournamentId, int groupId) {
        groupApi.getGroupStatistics(groupId, tournamentId).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<GroupInfoListDTO>> call, Response<List<GroupInfoListDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    GroupInfoListDTO groupInfoListDTO = response.body().get(0);
                    setStatistics(response.body());
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

    private void setTournamentData(String tournamentLogo, String tournamentName, String groupName) {
        Glide.with(context)
                .load(tournamentLogo)
                .into(this.tournamentLogo);
        this.tournamentName.setText(tournamentName);
        this.groupName.setText(groupName);
    }

    private void setOnClickListeners() {
        backButton.setOnClickListener(backButtonListener());
    }

    private View.OnClickListener backButtonListener() {
        return view -> finish();
    }
}