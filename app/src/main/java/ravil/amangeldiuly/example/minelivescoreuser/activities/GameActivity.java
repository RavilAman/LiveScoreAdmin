package ravil.amangeldiuly.example.minelivescoreuser.activities;

import static ravil.amangeldiuly.example.minelivescoreuser.ColorConstants.APP_ORANGE;
import static ravil.amangeldiuly.example.minelivescoreuser.utils.GeneralUtils.gameScoreIntoDashFormat;

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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import ravil.amangeldiuly.example.minelivescoreuser.ColorConstants;
import ravil.amangeldiuly.example.minelivescoreuser.UrlConstants;
import ravil.amangeldiuly.example.minelivescoreuser.R;
import ravil.amangeldiuly.example.minelivescoreuser.events.EventAdapter;
import ravil.amangeldiuly.example.minelivescoreuser.utils.LocalDateTimeDeserializer;
import ravil.amangeldiuly.example.minelivescoreuser.web.apis.ProtocolApi;
import ravil.amangeldiuly.example.minelivescoreuser.web.responses.EventDTO;
import ravil.amangeldiuly.example.minelivescoreuser.web.responses.ProtocolDTO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GameActivity extends AppCompatActivity {

    private Context context;

    private Retrofit retrofit;
    private ProtocolApi protocolApi;

    private ImageButton backButton;
    private ImageView team1Logo;
    private ImageView team2Logo;
    private TextView team1Name;
    private TextView team2Name;
    private TextView gameScore;
    private TextView fullTime;
    private RecyclerView eventsRecyclerView;

    private List<EventDTO> events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        initializeRetrofit();
        initializeViews();
        initializeObjects();
        setOnClickListeners();

        Intent intent = getIntent();
        long protocolId = intent.getExtras().getLong("protocolId");
        setData(protocolId);
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
        protocolApi = retrofit.create(ProtocolApi.class);
    }

    private void initializeViews() {
        context = getApplicationContext();
        backButton = findViewById(R.id.game_back_button);
        team1Logo = findViewById(R.id.game_activity_team_1_logo);
        team2Logo = findViewById(R.id.game_activity_team_2_logo);
        team1Name = findViewById(R.id.game_activity_team_1_name);
        team2Name = findViewById(R.id.game_activity_team_2_name);
        gameScore = findViewById(R.id.game_activity_game_score);
        fullTime = findViewById(R.id.game_activity_full_time);
        eventsRecyclerView = findViewById(R.id.game_activity_events_recycler_view);
    }

    private void initializeObjects() {
        events = new ArrayList<>();
    }

    private void setData(long protocolId) {
        protocolApi.getProtocolById(protocolId).enqueue(new Callback<ProtocolDTO>() {
            @Override
            public void onResponse(Call<ProtocolDTO> call, Response<ProtocolDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ProtocolDTO protocolDTO = response.body();
                    Glide.with(context)
                            .load(protocolDTO.getTeam1Logo())
                            .into(team1Logo);
                    Glide.with(context)
                            .load(protocolDTO.getTeam2Logo())
                            .into(team2Logo);
                    team1Name.setText(protocolDTO.getTeam1());
                    team2Name.setText(protocolDTO.getTeam2());
                    switch (protocolDTO.getGameState()) {
                        case NOT_STARTED:
                            String time = protocolDTO.getDateAndTime().format(DateTimeFormatter.ofPattern("HH:mm"));
                            fullTime.setText("");
                            gameScore.setText(time);
                            break;
                        case STARTED:
                            fullTime.setText(R.string.live);
                            fullTime.setTextColor(Color.parseColor(APP_ORANGE));
                            gameScore.setText(gameScoreIntoDashFormat(protocolDTO.getGameScore()));
                            break;
                        case ENDED:
                            fullTime.setText(R.string.full_time);
                            gameScore.setText(gameScoreIntoDashFormat(protocolDTO.getGameScore()));
                            break;
                    }
                    events = response.body().getEvents();
                    setEvents(protocolDTO.getTeam1Id());
                }
            }

            @Override
            public void onFailure(Call<ProtocolDTO> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void setOnClickListeners() {
        backButton.setOnClickListener(backButtonOnClickListener());
    }

    private View.OnClickListener backButtonOnClickListener() {
        return view -> finish();
    }

    private void setEvents(Long team1Id) {
        EventAdapter eventAdapter = new EventAdapter(context, events, team1Id);
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventsRecyclerView.setAdapter(eventAdapter);
    }
}