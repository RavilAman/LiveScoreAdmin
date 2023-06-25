package ravil.amangeldiuly.example.minelivescoreadmin.activities;

import static ravil.amangeldiuly.example.minelivescoreadmin.ColorConstants.APP_ORANGE;
import static ravil.amangeldiuly.example.minelivescoreadmin.SharedPreferencesConstants.FCM_TOKEN;
import static ravil.amangeldiuly.example.minelivescoreadmin.utils.GeneralUtils.gameScoreIntoDashFormat;
import static ravil.amangeldiuly.example.minelivescoreadmin.utils.SharedPreferencesUtil.getValue;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.internal.ServiceSpecificExtraArgs;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ravil.amangeldiuly.example.minelivescoreadmin.ColorConstants;
import ravil.amangeldiuly.example.minelivescoreadmin.R;
import ravil.amangeldiuly.example.minelivescoreadmin.dialog.ManipulateEventDialog;
import ravil.amangeldiuly.example.minelivescoreadmin.enums.EventEnum;
import ravil.amangeldiuly.example.minelivescoreadmin.enums.GameState;
import ravil.amangeldiuly.example.minelivescoreadmin.events.EventAdapter;
import ravil.amangeldiuly.example.minelivescoreadmin.events.EventViewHolder;
import ravil.amangeldiuly.example.minelivescoreadmin.utils.ActionInterfaces;
import ravil.amangeldiuly.example.minelivescoreadmin.utils.GeneralUtils;
import ravil.amangeldiuly.example.minelivescoreadmin.web.RequestHandler;
import ravil.amangeldiuly.example.minelivescoreadmin.web.apis.GameApi;
import ravil.amangeldiuly.example.minelivescoreadmin.web.apis.NotificationApi;
import ravil.amangeldiuly.example.minelivescoreadmin.web.apis.ProtocolApi;
import ravil.amangeldiuly.example.minelivescoreadmin.web.requests.AssistDTO;
import ravil.amangeldiuly.example.minelivescoreadmin.web.requests.CustomNotificationDto;
import ravil.amangeldiuly.example.minelivescoreadmin.web.requests.EventDTO;
import ravil.amangeldiuly.example.minelivescoreadmin.web.requests.GameDTO;
import ravil.amangeldiuly.example.minelivescoreadmin.web.requests.ProtocolDTO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GameActivity extends AppCompatActivity implements ActionInterfaces.ManipulateEventDialogCloseListener,
        ActionInterfaces.ManipulateEventDialogOpenListener {

    private Context context;

    private RequestHandler requestHandler;
    private Retrofit retrofit;
    private ProtocolApi protocolApi;
    private GameApi gameApi;
    private NotificationApi notificationApi;

    private LinearLayout manipulateGame;
    private LinearLayout manipulateEvent;
    private ImageButton backButton;
    private ImageView team1Logo;
    private ImageView team2Logo;
    private TextView team1Name;
    private TextView team2Name;
    private TextView gameScore;
    private TextView fullTime;
    private RecyclerView eventsRecyclerView;
    private Button startGame;
    private Button goalEventTeam1;
    private Button yellowCardEventTeam1;
    private Button redCardEventTeam1;
    private Button goalEventTeam2;
    private Button yellowCardEventTeam2;
    private Button redCardEventTeam2;
    private Button penaltyEventTeam1;
    private Button penaltyEventTeam2;
    private Button endMatch;

    private List<EventDTO> events;
    private ProtocolDTO protocolDTO;
    private long protocolId;
    private String topicName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        initializeViews();
        initializeRetrofit();
        initializeObjects();
        setOnClickListeners();

        Intent intent = getIntent();
        protocolId = intent.getExtras().getLong("protocolId");
        setData();
        getTopicName();
    }

    private void initializeRetrofit() {
        requestHandler = new RequestHandler(context);
        retrofit = requestHandler.getRetrofit();
        protocolApi = retrofit.create(ProtocolApi.class);
        gameApi = retrofit.create(GameApi.class);
        notificationApi = retrofit.create(NotificationApi.class);
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
        manipulateGame = findViewById(R.id.game_activity_manipulate_game);
        startGame = findViewById(R.id.game_activity_start_match);
        manipulateEvent = findViewById(R.id.game_activity_manipulate_event_layout);
        goalEventTeam1 = findViewById(R.id.game_activity_goal_team_1);
        yellowCardEventTeam1 = findViewById(R.id.game_activity_yellow_card_team_1);
        redCardEventTeam1 = findViewById(R.id.game_activity_red_card_team_1);
        penaltyEventTeam1 = findViewById(R.id.game_activity_penalty_team_1);
        goalEventTeam2 = findViewById(R.id.game_activity_goal_team_2);
        yellowCardEventTeam2 = findViewById(R.id.game_activity_yellow_card_team_2);
        redCardEventTeam2 = findViewById(R.id.game_activity_red_card_team_2);
        penaltyEventTeam2 = findViewById(R.id.game_activity_penalty_team_2);
        endMatch = findViewById(R.id.game_activity_end_match);
    }

    private void initializeObjects() {
        events = new ArrayList<>();
        topicName = "";
    }

    private void getTopicName() {
        notificationApi.getTopicNameByProtocolId((int) protocolId).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    topicName = response.body();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void setData() {
        protocolApi.getProtocolById(protocolId).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ProtocolDTO> call, Response<ProtocolDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    protocolDTO = response.body();
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
                            fillDataGameStateNotStarted();
                            break;
                        case STARTED:
                            fillDataGameStateStart();
                            break;
                        case ENDED:
                            fillDataGameStateEnded();
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

    private void fillDataGameStateNotStarted() {
        manipulateGame.setVisibility(View.VISIBLE);
        manipulateEvent.setVisibility(View.GONE);
        String time = protocolDTO.getDateAndTime().format(DateTimeFormatter.ofPattern("HH:mm"));
        fullTime.setText("");
        gameScore.setText(time);
    }

    private void fillDataGameStateStart() {
        manipulateEvent.setVisibility(View.VISIBLE);
        fullTime.setText(R.string.live);
        fullTime.setTextColor(Color.parseColor(APP_ORANGE));
        gameScore.setText(gameScoreIntoDashFormat(protocolDTO.getGameScore()));
    }

    private void fillDataGameStateEnded() {
        manipulateEvent.setVisibility(View.GONE);
        fullTime.setText(R.string.full_time);
        fullTime.setTextColor(Color.parseColor(ColorConstants.LIGHT_GREY));
        gameScore.setText(gameScoreIntoDashFormat(protocolDTO.getGameScore()));
    }

    private void setOnClickListeners() {
        backButton.setOnClickListener(backButtonOnClickListener());
        startGame.setOnClickListener(startGameListener());
        goalEventTeam1.setOnClickListener(createEventListener());
        yellowCardEventTeam1.setOnClickListener(createEventListener());
        redCardEventTeam1.setOnClickListener(createEventListener());
        penaltyEventTeam1.setOnClickListener(createEventListener());
        goalEventTeam2.setOnClickListener(createEventListener());
        yellowCardEventTeam2.setOnClickListener(createEventListener());
        redCardEventTeam2.setOnClickListener(createEventListener());
        penaltyEventTeam2.setOnClickListener(createEventListener());
        endMatch.setOnClickListener(endMatchListener());
    }

    @SuppressLint("NonConstantResourceId")
    private View.OnClickListener createEventListener() {
        return view -> {
            switch (view.getId()) {
                case R.id.game_activity_goal_team_1:
                    openEventDialogToCreate(
                            protocolDTO.getProtocolId(),
                            protocolDTO.getTeam1Logo(),
                            protocolDTO.getTeam1Id(),
                            EventEnum.GOAL
                    );
                    break;
                case R.id.game_activity_yellow_card_team_1:
                    openEventDialogToCreate(
                            protocolDTO.getProtocolId(),
                            protocolDTO.getTeam1Logo(),
                            protocolDTO.getTeam1Id(),
                            EventEnum.YELLOW_CARD
                    );
                    break;
                case R.id.game_activity_red_card_team_1:
                    openEventDialogToCreate(
                            protocolDTO.getProtocolId(),
                            protocolDTO.getTeam1Logo(),
                            protocolDTO.getTeam1Id(),
                            EventEnum.RED_CARD
                    );
                    break;
                case R.id.game_activity_penalty_team_1:
                    openEventDialogToCreate(
                            protocolDTO.getProtocolId(),
                            protocolDTO.getTeam1Logo(),
                            protocolDTO.getTeam1Id(),
                            EventEnum.PENALTY
                    );
                    break;
                case R.id.game_activity_goal_team_2:
                    openEventDialogToCreate(
                            protocolDTO.getProtocolId(),
                            protocolDTO.getTeam2Logo(),
                            protocolDTO.getTeam2Id(),
                            EventEnum.GOAL
                    );
                    break;
                case R.id.game_activity_yellow_card_team_2:
                    openEventDialogToCreate(
                            protocolDTO.getProtocolId(),
                            protocolDTO.getTeam2Logo(),
                            protocolDTO.getTeam2Id(),
                            EventEnum.YELLOW_CARD
                    );
                    break;
                case R.id.game_activity_red_card_team_2:
                    openEventDialogToCreate(
                            protocolDTO.getProtocolId(),
                            protocolDTO.getTeam2Logo(),
                            protocolDTO.getTeam2Id(),
                            EventEnum.RED_CARD
                    );
                    break;
                case R.id.game_activity_penalty_team_2:
                    openEventDialogToCreate(
                            protocolDTO.getProtocolId(),
                            protocolDTO.getTeam2Logo(),
                            protocolDTO.getTeam2Id(),
                            EventEnum.PENALTY
                    );
                    break;
            }
        };
    }

    private View.OnClickListener endMatchListener() {
        return view -> endMatch();
    }

    private void endMatch() {
        gameApi.endGame(protocolDTO.getGameId().intValue()).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<GameDTO> call, Response<GameDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    GameDTO finishedGame = response.body();
                    fillDataGameStateEnded();
                    disableEventChange();

                    CustomNotificationDto customNotificationDto = new CustomNotificationDto();
                    StringBuilder messageBuilder = new StringBuilder();
                    customNotificationDto.setRegistrationTokens(List.of(getValue(context, FCM_TOKEN)));
                    customNotificationDto.setTitle("Match ended");
                    messageBuilder.append(finishedGame.getTeam1Name());
                    messageBuilder.append(" ");
                    messageBuilder.append(gameScoreIntoDashFormat(finishedGame.getGameScore()));
                    messageBuilder.append(" ");
                    messageBuilder.append(finishedGame.getTeam2Name());
                    customNotificationDto.setBody(messageBuilder.toString());
                    sendNotification(customNotificationDto);

                    Toast.makeText(context, "Game finished successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "error: " + response + "; code: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GameDTO> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void openEventDialogToCreate(long protocolId, String teamLogo, long teamId, EventEnum eventEnum) {
        ManipulateEventDialog manipulateEventDialog = new ManipulateEventDialog(protocolId,
                teamLogo, teamId, eventEnum, topicName, this, protocolDTO);
        manipulateEventDialog.setGameDateTime(protocolDTO.getDateAndTime());
        manipulateEventDialog.show(getSupportFragmentManager(), "");
    }

    private void openEventDialogToUpdate(long protocolId, String teamLogo, long teamId, EventEnum eventEnum,
                                         Long playerId, Integer minute, Long eventId, AssistDTO assistDTO) {
        ManipulateEventDialog manipulateEventDialog = new ManipulateEventDialog(protocolId, teamLogo,
                teamId, eventEnum, topicName, this, playerId, minute,
                eventId, assistDTO, protocolDTO);
        manipulateEventDialog.setGameDateTime(protocolDTO.getDateAndTime());
        manipulateEventDialog.show(getSupportFragmentManager(), "");
    }

    private View.OnClickListener startGameListener() {
        return view -> startGame();
    }

    private void startGame() {
        gameApi.startGame(protocolDTO.getGameId().intValue()).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<GameDTO> call, Response<GameDTO> response) {
                if (response.isSuccessful()) {
                    protocolDTO.setGameState(GameState.STARTED);
                    manipulateGame.setVisibility(View.GONE);
                    fillDataGameStateStart();

                    CustomNotificationDto customNotificationDto = new CustomNotificationDto();
                    StringBuilder messageBuilder = new StringBuilder();
                    customNotificationDto.setRegistrationTokens(List.of(getValue(context, FCM_TOKEN)));
                    customNotificationDto.setTitle("Match started!");
                    messageBuilder.append(protocolDTO.getTeam1());
                    messageBuilder.append(" vs ");
                    messageBuilder.append(protocolDTO.getTeam2());
                    customNotificationDto.setBody(messageBuilder.toString());
                    sendNotification(customNotificationDto);
                }
            }

            @Override
            public void onFailure(Call<GameDTO> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void sendNotification(CustomNotificationDto customNotificationDto) {
        notificationApi.postToTopic(topicName, customNotificationDto).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private View.OnClickListener backButtonOnClickListener() {
        return view -> finish();
    }

    private void setEvents(Long team1Id) {
        EventAdapter eventAdapter = new EventAdapter(context, events, team1Id, protocolDTO.getGameState(), this);
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventsRecyclerView.setAdapter(eventAdapter);
    }

    private void disableEventChange() {
        RecyclerView.Adapter<EventViewHolder> adapter = eventsRecyclerView.getAdapter();
        for (int i = 0; i < Objects.requireNonNull(adapter).getItemCount(); i++) {
            RecyclerView.ViewHolder eventViewHolder = eventsRecyclerView.findViewHolderForAdapterPosition(i);
            if (eventViewHolder instanceof EventViewHolder) {
                EventViewHolder yourViewHolder = (EventViewHolder) eventViewHolder;
                yourViewHolder.editEvent.setEnabled(false);
            }
        }
    }

    @Override
    public void onDialogClosed(String message) {
        setData();
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDialogOpen(String teamLogo, long teamId, EventEnum eventEnum, Long playerId,
                             Integer minute, Long eventId, AssistDTO assistDTO) {
        openEventDialogToUpdate(protocolId, teamLogo, teamId, eventEnum, playerId, minute, eventId, assistDTO);
    }
}