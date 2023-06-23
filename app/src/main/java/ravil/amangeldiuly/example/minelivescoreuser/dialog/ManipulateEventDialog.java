package ravil.amangeldiuly.example.minelivescoreuser.dialog;

import static ravil.amangeldiuly.example.minelivescoreuser.utils.GeneralUtils.beautifyScoreForNotification;

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
import ravil.amangeldiuly.example.minelivescoreuser.utils.GeneralUtils;
import ravil.amangeldiuly.example.minelivescoreuser.web.RequestHandler;
import ravil.amangeldiuly.example.minelivescoreuser.web.apis.EventApi;
import ravil.amangeldiuly.example.minelivescoreuser.web.apis.NotificationApi;
import ravil.amangeldiuly.example.minelivescoreuser.web.apis.PlayerApi;
import ravil.amangeldiuly.example.minelivescoreuser.web.requests.CustomNotificationDto;
import ravil.amangeldiuly.example.minelivescoreuser.web.responses.AssistDTO;
import ravil.amangeldiuly.example.minelivescoreuser.web.responses.EventDTO;
import ravil.amangeldiuly.example.minelivescoreuser.web.responses.PlayerDTO;
import ravil.amangeldiuly.example.minelivescoreuser.web.responses.ProtocolDTO;
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
    private String currentTeamLogoLink;
    private long currentTeamId;
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
    private String topicName;
    private ProtocolDTO protocolDTO;

    private RequestHandler requestHandler;
    private Retrofit retrofit;
    private EventApi eventApi;
    private PlayerApi playerApi;
    private NotificationApi notificationApi;

    public ManipulateEventDialog(long protocolId, String currentTeamLogoLink, long currentTeamId, EventEnum eventEnum, String topicName,
                                 ActionInterfaces.ManipulateEventDialogCloseListener manipulateEventDialogCloseListener, ProtocolDTO protocolDTO) {
        this.protocolId = protocolId;
        this.currentTeamLogoLink = currentTeamLogoLink;
        this.currentTeamId = currentTeamId;
        this.eventEnum = eventEnum;
        this.manipulateEventDialogCloseListener = manipulateEventDialogCloseListener;
        this.topicName = topicName;
        this.protocolDTO = protocolDTO;
    }

    public ManipulateEventDialog(long protocolId, String currentTeamLogoLink, long currentTeamId, EventEnum eventEnum, String topicName,
                                 ActionInterfaces.ManipulateEventDialogCloseListener manipulateEventDialogCloseListener,
                                 Long playerId, Integer minute, Long eventId, AssistDTO assistDTO, ProtocolDTO protocolDTO) {
        this.protocolId = protocolId;
        this.currentTeamLogoLink = currentTeamLogoLink;
        this.currentTeamId = currentTeamId;
        this.eventEnum = eventEnum;
        this.manipulateEventDialogCloseListener = manipulateEventDialogCloseListener;
        this.playerId = playerId;
        this.minute = minute;
        this.eventId = eventId;
        this.assistDTO = assistDTO;
        this.topicName = topicName;
        this.protocolDTO = protocolDTO;
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
        notificationApi = retrofit.create(NotificationApi.class);
    }

    private void setListeners() {
        saveButton.setOnClickListener(saveButtonListener());
    }

    private View.OnClickListener saveButtonListener() {
        return view -> saveOrUpdateEvent();
    }

    private void saveOrUpdateEvent() {
        CustomNotificationDto customNotificationDto = new CustomNotificationDto();
        customNotificationDto.setImage(currentTeamLogoLink);
        StringBuilder messageBuilder = new StringBuilder();
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

                    customNotificationDto.setTitle("Гол!");
                    messageBuilder.append(protocolDTO.getTeam1());
                    messageBuilder.append(" ");
                    messageBuilder.append(beautifyScoreForNotification(protocolDTO.getGameScore(), protocolDTO.getTeam1Id().equals(currentTeamId)));
                    messageBuilder.append(" ");
                    messageBuilder.append(protocolDTO.getTeam2());
                    messageBuilder.append("\n");
                    messageBuilder.append(player.getSurname());
                    messageBuilder.append(" ");
                    messageBuilder.append(player.getName());
                    messageBuilder.append(minute);
                    messageBuilder.append("'");
                    if (assistPlayer != null) {
                        messageBuilder.append(" ");
                        messageBuilder.append("(");
                        messageBuilder.append(assistPlayer.getSurname());
                        messageBuilder.append(" ");
                        messageBuilder.append(assistPlayer.getName());
                        messageBuilder.append(")");
                    }
                    customNotificationDto.setBody(messageBuilder.toString());

                    goalCall.enqueue(new Callback<>() {
                        @Override
                        public void onResponse(Call<EventDTO> call, Response<EventDTO> response) {
                            if (response.isSuccessful()) {
                                sendNotification(customNotificationDto);
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

                    customNotificationDto.setTitle("Пенальти!");
                    messageBuilder.append(protocolDTO.getTeam1());
                    messageBuilder.append(" ");
                    messageBuilder.append(beautifyScoreForNotification(protocolDTO.getGameScore(), protocolDTO.getTeam1Id().equals(currentTeamId)));
                    messageBuilder.append(" ");
                    messageBuilder.append(protocolDTO.getTeam2());
                    messageBuilder.append("\n");
                    messageBuilder.append(player.getSurname());
                    messageBuilder.append(" ");
                    messageBuilder.append(player.getName());
                    messageBuilder.append(minute);
                    messageBuilder.append("'");
                    customNotificationDto.setBody(messageBuilder.toString());

                    saveEventDTO.setEventEnumId(5L);
                    Call<EventDTO> penaltyCall = eventApi.postPenaltyEvent(saveEventDTO);
                    if (update) {
                        eventApi.putPenaltyEvent(eventId.intValue(), saveEventDTO);
                    }
                    penaltyCall.enqueue(new Callback<>() {
                        @Override
                        public void onResponse(Call<EventDTO> call, Response<EventDTO> response) {
                            if (response.isSuccessful()) {
                                sendNotification(customNotificationDto);
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
                    customNotificationDto.setTitle("Желтая карточка");
                } else if (eventEnum.equals(EventEnum.RED_CARD)) {
                    closeMessage = "Red card awarded successfully!";
                    saveEventDTO.setEventEnumId(4L);
                    customNotificationDto.setTitle("Красная карточка");
                }
                Call<EventDTO> eventCall = eventApi.postEvent(saveEventDTO);
                if (update) {
                    eventCall = eventApi.putEvent(eventId.intValue(), saveEventDTO);
                }

                messageBuilder.append(minute);
                messageBuilder.append("'");
                messageBuilder.append(player.getSurname());
                messageBuilder.append(" ");
                messageBuilder.append(player.getName());
                messageBuilder.append(" ");
                messageBuilder.append("(");
                messageBuilder.append(
                        protocolDTO.getTeam1Id().equals(currentTeamId)
                                ? protocolDTO.getTeam1()
                                : protocolDTO.getTeam2());
                messageBuilder.append(")");
                customNotificationDto.setBody(messageBuilder.toString());

                eventCall.enqueue(new Callback<>() {
                    @Override
                    public void onResponse(Call<EventDTO> call, Response<EventDTO> response) {
                        if (update) {
                            sendNotification(customNotificationDto);
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
                .load(currentTeamLogoLink)
                .into(teamLogo);
        getPlayers();
    }

    private void getPlayers() {
        players.clear();
        playerFullNames.clear();
        playerApi.findAllPlayerByTeamId(currentTeamId).enqueue(new Callback<>() {
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

    private void sendNotification(CustomNotificationDto customNotificationDto) {
        notificationApi.postToTopic(topicName, customNotificationDto).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    System.out.println("Success!");
                } else {
                    System.out.println("Fail!");
                    System.out.println("responseCode: " + response.code());
                    System.out.println("----------------------------------");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
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
