package ravil.amangeldiuly.example.minelivescoreadmin.dialog;

import static ravil.amangeldiuly.example.minelivescoreadmin.SharedPreferencesConstants.FCM_TOKEN;
import static ravil.amangeldiuly.example.minelivescoreadmin.enums.SpinnerSelected.PLAYER;
import static ravil.amangeldiuly.example.minelivescoreadmin.utils.GeneralUtils.beautifyScoreForNotification;
import static ravil.amangeldiuly.example.minelivescoreadmin.utils.SharedPreferencesUtil.getValue;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import java.util.stream.IntStream;

import ravil.amangeldiuly.example.minelivescoreadmin.R;
import ravil.amangeldiuly.example.minelivescoreadmin.enums.EventEnum;
import ravil.amangeldiuly.example.minelivescoreadmin.spinner.UniversalSpinnerAdapter;
import ravil.amangeldiuly.example.minelivescoreadmin.utils.ActionInterfaces;
import ravil.amangeldiuly.example.minelivescoreadmin.web.RequestHandler;
import ravil.amangeldiuly.example.minelivescoreadmin.web.apis.EventApi;
import ravil.amangeldiuly.example.minelivescoreadmin.web.apis.NotificationApi;
import ravil.amangeldiuly.example.minelivescoreadmin.web.apis.PlayerApi;
import ravil.amangeldiuly.example.minelivescoreadmin.web.requests.CustomNotificationDto;
import ravil.amangeldiuly.example.minelivescoreadmin.web.requests.AssistDTO;
import ravil.amangeldiuly.example.minelivescoreadmin.web.requests.EventDTO;
import ravil.amangeldiuly.example.minelivescoreadmin.web.requests.PlayerDTO;
import ravil.amangeldiuly.example.minelivescoreadmin.web.requests.ProtocolDTO;
import ravil.amangeldiuly.example.minelivescoreadmin.web.responses.SaveEventDTO;
import ravil.amangeldiuly.example.minelivescoreadmin.web.responses.SaveGoalEventDTO;
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
    private UniversalSpinnerAdapter spinnerAdapter;

    private ActionInterfaces.ManipulateEventDialogCloseListener manipulateEventDialogCloseListener;
    private LocalDateTime gameDateTime;
    private long protocolId;
    private String currentTeamLogoLink;
    private long currentTeamId;
    private EventEnum eventEnum;
    private List<PlayerDTO> players;
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
        customNotificationDto.setRegistrationTokens(List.of(getValue(context, FCM_TOKEN)));
        StringBuilder messageBuilder = new StringBuilder();
        int selectedPlayerIndex = (int) authorSpinner.getSelectedItem();
        PlayerDTO player = players.get(selectedPlayerIndex);
        if (selectedPlayerIndex > 0) {
            if (eventEnum.equals(EventEnum.GOAL)) {
                SaveGoalEventDTO saveGoalEventDTO = new SaveGoalEventDTO();
                saveGoalEventDTO.setProtocolId(protocolId);
                saveGoalEventDTO.setPlayerId(player.getPlayerId());
                saveGoalEventDTO.setMinute(minutePicker.getValue());
                PlayerDTO assistPlayer = players.get((int) assistSpinner.getSelectedItem());
                if (!player.getPlayerId().equals(assistPlayer.getPlayerId())) {
                    saveGoalEventDTO.setAssistId(assistPlayer.getPlayerId());
                    Call<EventDTO> goalCall = eventApi.postGoalEvent(saveGoalEventDTO);
                    if (update) {
                        goalCall = eventApi.putGoalEvent(eventId.intValue(), saveGoalEventDTO);
                    }

                    customNotificationDto.setTitle("Гол!");
                    messageBuilder.append(protocolDTO.getTeam1());
                    messageBuilder.append(" ");
                    messageBuilder.append(beautifyScoreForNotification(protocolDTO.getGameScore(),
                            protocolDTO.getTeam1Id().equals(currentTeamId)));
                    messageBuilder.append(" ");
                    messageBuilder.append(protocolDTO.getTeam2());
                    messageBuilder.append("\n");
                    messageBuilder.append(player.fullNameNameFirst());
                    messageBuilder.append(" ");
                    messageBuilder.append(minutePicker.getValue());
                    messageBuilder.append("'");
                    if (!assistPlayer.isEmpty()) {
                        messageBuilder.append(" ");
                        messageBuilder.append("(");
                        messageBuilder.append(player.fullNameNameFirst());
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
                    messageBuilder.append(player.fullNameNameFirst());
                    messageBuilder.append(" ");
                    messageBuilder.append(minutePicker.getValue());
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

                messageBuilder.append(minutePicker.getValue());
                messageBuilder.append("'");
                messageBuilder.append(" ");
                messageBuilder.append(player.fullNameNameFirst());
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
                            closeMessage = "Event changed successfully!";
                        }
                        if (!response.isSuccessful()) {
                            closeMessage = "error!";
                        } else {
                            sendNotification(customNotificationDto);
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
        players.add(PlayerDTO.defaultBuilder());
        playerApi.findAllPlayerByTeamId(currentTeamId).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<PlayerDTO>> call, Response<List<PlayerDTO>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    players.addAll(response.body());

                    spinnerAdapter = new UniversalSpinnerAdapter(context, PLAYER);
                    spinnerAdapter.setPlayers(players);
                    authorSpinner.setAdapter(spinnerAdapter);
                    assistSpinner.setAdapter(spinnerAdapter);

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
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void setGameDateTime(LocalDateTime gameDateTime) {
        this.gameDateTime = gameDateTime;
    }

    private int getPlayerById(Long playerId) {
        return IntStream.range(0, players.size())
                .filter(i -> players.get(i).getPlayerId().equals(playerId))
                .findFirst()
                .orElse(0);
    }
}
