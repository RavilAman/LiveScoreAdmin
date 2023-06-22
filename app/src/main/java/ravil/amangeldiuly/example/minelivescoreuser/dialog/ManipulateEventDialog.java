package ravil.amangeldiuly.example.minelivescoreuser.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.bumptech.glide.Glide;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import ravil.amangeldiuly.example.minelivescoreuser.R;
import ravil.amangeldiuly.example.minelivescoreuser.enums.EventEnum;
import ravil.amangeldiuly.example.minelivescoreuser.utils.ActionInterfaces;
import ravil.amangeldiuly.example.minelivescoreuser.web.RequestHandler;
import ravil.amangeldiuly.example.minelivescoreuser.web.apis.EventApi;
import ravil.amangeldiuly.example.minelivescoreuser.web.apis.PlayerApi;
import ravil.amangeldiuly.example.minelivescoreuser.web.responses.AssistDTO;
import ravil.amangeldiuly.example.minelivescoreuser.web.responses.EventDTO;
import ravil.amangeldiuly.example.minelivescoreuser.web.responses.PlayerDTO;
import ravil.amangeldiuly.example.minelivescoreuser.web.responses.SaveEventDTO;
import ravil.amangeldiuly.example.minelivescoreuser.web.responses.SaveGoalEventDTO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ManipulateEventDialog extends AppCompatDialogFragment {

    private Context context;

    private View currentView;
    private Dialog manipulateEventDialog;
    private ImageView teamLogo;
    private TextView eventLabel;
    private TextView assistLabel;
    private Spinner authorSpinner;
    private Spinner assistSpinner;
    private NumberPicker minutePicker;
    private Button saveButton;

    private ActionInterfaces.ManipulateEventDialogCloseListener manipulateEventDialogCloseListener;
    private LocalDateTime gameDateTime;
    private long protocolId;
    private String teamLogoLink;
    private long teamId;
    private EventEnum eventEnum;
    private ArrayAdapter<String> authorsAdapter;
    private ArrayAdapter<String> assistAdapter;
    private List<PlayerDTO> players;
    private List<String> playerFullNames;
    private String closeMessage;
    private Long playerId;
    private Integer minute;
    private Long eventId;
    private AssistDTO assistDTO;
    private boolean update;

    private RequestHandler requestHandler;
    private Retrofit retrofit;
    private EventApi eventApi;
    private PlayerApi playerApi;

    public ManipulateEventDialog(long protocolId, String teamLogoLink, long teamId, EventEnum eventEnum,
                                 ActionInterfaces.ManipulateEventDialogCloseListener manipulateEventDialogCloseListener) {
        this.protocolId = protocolId;
        this.teamLogoLink = teamLogoLink;
        this.teamId = teamId;
        this.eventEnum = eventEnum;
        this.manipulateEventDialogCloseListener = manipulateEventDialogCloseListener;
    }

    public ManipulateEventDialog(long protocolId, String teamLogoLink, long teamId, EventEnum eventEnum,
                                 ActionInterfaces.ManipulateEventDialogCloseListener manipulateEventDialogCloseListener,
                                 Long playerId, Integer minute, Long eventId, AssistDTO assistDTO) {
        this.protocolId = protocolId;
        this.teamLogoLink = teamLogoLink;
        this.teamId = teamId;
        this.eventEnum = eventEnum;
        this.manipulateEventDialogCloseListener = manipulateEventDialogCloseListener;
        this.playerId = playerId;
        this.minute = minute;
        this.eventId = eventId;
        this.assistDTO = assistDTO;
        update = true;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        initializeViews();
        initializeRetrofit();
        initializeObjects();
        setFullWidth();
        setData();
        setListeners();
        return manipulateEventDialog;
    }

    private void initializeObjects() {
        players = new ArrayList<>();
        playerFullNames = new ArrayList<>();
        closeMessage = "error!";
    }

    private void initializeViews() {
        context = requireContext();
        manipulateEventDialog = new Dialog(context, R.style.CustomDialogTheme);
        currentView = getActivity().getLayoutInflater()
                .inflate(R.layout.manipulate_event_dialog, null);
        manipulateEventDialog.setContentView(currentView);
        teamLogo = currentView.findViewById(R.id.manipulate_event_team_logo);
        eventLabel = currentView.findViewById(R.id.manipulate_event_event_label);
        assistLabel = currentView.findViewById(R.id.manipulate_event_assist_label);
        authorSpinner = currentView.findViewById(R.id.manipulate_event_author_spinner);
        assistSpinner = currentView.findViewById(R.id.manipulate_event_assist_spinner);
        minutePicker = currentView.findViewById(R.id.manipulate_event_minute_picker);
        saveButton = currentView.findViewById(R.id.manipulate_event_save_button);
    }

    private void setFullWidth() {
        Window window = manipulateEventDialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(layoutParams);
        }
    }

    private void initializeRetrofit() {
        requestHandler = new RequestHandler(context);
        retrofit = requestHandler.getRetrofit();
        eventApi = retrofit.create(EventApi.class);
        playerApi = retrofit.create(PlayerApi.class);
    }

    private void setListeners() {
        saveButton.setOnClickListener(saveButtonListener());
    }

    private View.OnClickListener saveButtonListener() {
        return view -> saveOrUpdateEvent();
    }

    private void saveOrUpdateEvent() {
        PlayerDTO player = identifyPlayer(authorSpinner.getSelectedItem().toString());
        if (player != null) {
            if (eventEnum.equals(EventEnum.GOAL)) {
                SaveGoalEventDTO saveGoalEventDTO = new SaveGoalEventDTO();
                saveGoalEventDTO.setProtocolId(protocolId);
                saveGoalEventDTO.setPlayerId(player.getPlayerId());
                saveGoalEventDTO.setMinute(minutePicker.getValue());
                PlayerDTO assistPlayer = identifyPlayer(assistSpinner.getSelectedItem().toString());
                if (assistPlayer != null) {
                    saveGoalEventDTO.setAssistId(assistPlayer.getPlayerId());
                } else {
                    saveGoalEventDTO.setAssistId(0L);
                }
                if (!Objects.equals(saveGoalEventDTO.getPlayerId(), saveGoalEventDTO.getAssistId())) {
                    Call<EventDTO> goalCall = eventApi.postGoalEvent(saveGoalEventDTO);
                    if (update) {
                        goalCall = eventApi.putGoalEvent(eventId.intValue(), saveGoalEventDTO);
                    }
                    goalCall.enqueue(new Callback<>() {
                        @Override
                        public void onResponse(Call<EventDTO> call, Response<EventDTO> response) {
                            if (response.isSuccessful()) {
                                closeMessage = "Goal scored successfully!";
                                if (update) {
                                    closeMessage = "Event changed successfully!";
                                }
                            }
                            manipulateEventDialogCloseListener.onDialogClosed(closeMessage);
                            dismiss();
                        }

                        @Override
                        public void onFailure(Call<EventDTO> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
                } else {
                    Toast.makeText(context, "Same players have been selected for author and assist!", Toast.LENGTH_SHORT).show();
                }
            } else {
                SaveEventDTO saveEventDTO = new SaveEventDTO();
                saveEventDTO.setProtocolId(protocolId);
                saveEventDTO.setMinute(minutePicker.getValue());
                saveEventDTO.setPlayerId(player.getPlayerId());
                if (eventEnum.equals(EventEnum.PENALTY)) {
                    saveEventDTO.setEventEnumId(5L);
                    Call<EventDTO> penaltyCall = eventApi.postPenaltyEvent(saveEventDTO);
                    if (update) {
                        eventApi.putPenaltyEvent(eventId.intValue(), saveEventDTO);
                    }
                    penaltyCall.enqueue(new Callback<>() {
                        @Override
                        public void onResponse(Call<EventDTO> call, Response<EventDTO> response) {
                            if (response.isSuccessful()) {
                                closeMessage = "Penalty scored successfully!";
                                if (update) {
                                    closeMessage = "Event changed successfully!";
                                }
                            }
                            manipulateEventDialogCloseListener.onDialogClosed(closeMessage);
                            dismiss();
                        }

                        @Override
                        public void onFailure(Call<EventDTO> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
                    return;
                } else if (eventEnum.equals(EventEnum.YELLOW_CARD) || eventEnum.equals(EventEnum.SECOND_YELLOW_CARD)) {
                    closeMessage = "Yellow card awarded successfully!";
                    saveEventDTO.setEventEnumId(3L);
                } else if (eventEnum.equals(EventEnum.RED_CARD)) {
                    closeMessage = "Red card awarded successfully!";
                    saveEventDTO.setEventEnumId(4L);
                }
                Call<EventDTO> eventCall = eventApi.postEvent(saveEventDTO);
                if (update) {
                    eventCall = eventApi.putEvent(eventId.intValue(), saveEventDTO);
                }
                eventCall.enqueue(new Callback<>() {
                    @Override
                    public void onResponse(Call<EventDTO> call, Response<EventDTO> response) {
                        if (update) {
                            closeMessage = "Event changed successfully!";
                        }
                        if (!response.isSuccessful()) {
                            closeMessage = "error!";
                        }
                        manipulateEventDialogCloseListener.onDialogClosed(closeMessage);
                        dismiss();
                    }

                    @Override
                    public void onFailure(Call<EventDTO> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            }
        } else {
            Toast.makeText(context, "Please, select player!", Toast.LENGTH_SHORT).show();
        }
    }

    private void bendLayoutUnderEvent() {
        switch (eventEnum) {
            case YELLOW_CARD:
            case SECOND_YELLOW_CARD:
                eventLabel.setText(R.string.yellow_card);
                assistLabel.setVisibility(View.GONE);
                assistSpinner.setVisibility(View.GONE);
                break;
            case RED_CARD:
                eventLabel.setText(R.string.red_card);
                assistLabel.setVisibility(View.GONE);
                assistSpinner.setVisibility(View.GONE);
                break;
            case PENALTY:
                eventLabel.setText(R.string.penalty);
                assistLabel.setVisibility(View.GONE);
                assistSpinner.setVisibility(View.GONE);
                break;
        }
    }

    private void configureMinutePicker() {
        minutePicker.setMinValue(1);
        minutePicker.setMaxValue(90);
        if (minute == null) {
            minutePicker.setValue(getGameMinute());
        } else {
            minutePicker.setValue(minute);
        }
        minutePicker.setValue(getGameMinute());
        minutePicker.setWrapSelectorWheel(false);
    }

    private int getGameMinute() {
        ZoneId timeZone = ZoneId.of("GMT+6");
        ZonedDateTime zonedDateTime = ZonedDateTime.now(timeZone);
        LocalDateTime rightNow = zonedDateTime.toLocalDateTime();
        long minutesDifference = Duration.between(gameDateTime, rightNow).toMinutes();
        return minutesDifference > 0L && minutesDifference <= 90
                ? (int) minutesDifference
                : 45;
    }

    private void setData() {
        bendLayoutUnderEvent();
        configureMinutePicker();
        Glide.with(context)
                .load(teamLogoLink)
                .into(teamLogo);
        getPlayers();
    }

    private void getPlayers() {
        players.clear();
        playerFullNames.clear();
        playerApi.findAllPlayerByTeamId(teamId).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<PlayerDTO>> call, Response<List<PlayerDTO>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    players = response.body();
                    deleteSpaceFromNames();
                    setPlayerNames();

                    authorsAdapter = new ArrayAdapter<>(context, R.layout.players_spinner_item, playerFullNames);
                    authorsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    authorSpinner.setAdapter(authorsAdapter);

                    assistAdapter = new ArrayAdapter<>(context, R.layout.players_spinner_item, playerFullNames);
                    assistAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    assistSpinner.setAdapter(assistAdapter);

                    if (playerId != null) {
                        authorSpinner.setSelection(getPlayerById(playerId));
                    }
                    if (assistDTO != null) {
                        assistSpinner.setSelection(getPlayerById(assistDTO.getAssistPlayerId()));
                    }
                }
            }

            @Override
            public void onFailure(Call<List<PlayerDTO>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void setPlayerNames() {
        playerFullNames.add("");
        playerFullNames.addAll(
                players.stream()
                        .map(playerDTO ->
                                playerDTO.getName() + " " + playerDTO.getSurname())
                        .collect(Collectors.toList())
        );
    }

    private void deleteSpaceFromNames() {
        players.forEach(playerDTO -> {
            playerDTO.setSurname(
                    playerDTO.getSurname().replaceAll("\\s", "")
            );
            playerDTO.setName(
                    playerDTO.getName().replaceAll("\\s", "")
            );
        });
    }

    public void setGameDateTime(LocalDateTime gameDateTime) {
        this.gameDateTime = gameDateTime;
    }

    private PlayerDTO identifyPlayer(String playerFullName) {
        if (playerFullName.isEmpty()) {
            return null;
        }
        Optional<PlayerDTO> answer = players.stream().filter(playerDTO ->
                playerDTO.getName()
                        .equals(playerFullName.substring(0, playerFullName.indexOf(" ")))
                        && playerDTO.getSurname()
                        .equals(playerFullName.substring(playerFullName.indexOf(" ") + 1))
        ).findFirst();
        return answer.orElse(null);
    }

    private int getPlayerById(Long playerId) {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getPlayerId().equals(playerId)) {
                return i + 1;
            }
        }
        return 0;
    }
}
