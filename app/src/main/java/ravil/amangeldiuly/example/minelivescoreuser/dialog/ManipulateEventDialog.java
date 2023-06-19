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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ravil.amangeldiuly.example.minelivescoreuser.R;
import ravil.amangeldiuly.example.minelivescoreuser.enums.EventEnum;
import ravil.amangeldiuly.example.minelivescoreuser.web.RequestHandler;
import ravil.amangeldiuly.example.minelivescoreuser.web.apis.EventApi;
import ravil.amangeldiuly.example.minelivescoreuser.web.apis.PlayerApi;
import ravil.amangeldiuly.example.minelivescoreuser.web.responses.PlayerDTO;
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

    private long protocolId;
    private String teamLogoLink;
    private long teamId;
    private EventEnum eventEnum;
    private ArrayAdapter<String> authorsAdapter;
    private ArrayAdapter<String> assistAdapter;
    private List<PlayerDTO> players;
    private List<String> playerFullNames;

    private RequestHandler requestHandler;
    private Retrofit retrofit;
    private EventApi eventApi;
    private PlayerApi playerApi;

    public ManipulateEventDialog(long protocolId, String teamLogoLink, long teamId, EventEnum eventEnum) {
        this.protocolId = protocolId;
        this.teamLogoLink = teamLogoLink;
        this.teamId = teamId;
        this.eventEnum = eventEnum;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        initializeViews();
        initializeRetrofit();
        initializeObjects();
        setFullWidth();

        setData();
        return manipulateEventDialog;
    }

    private void initializeObjects() {
        players = new ArrayList<>();
        playerFullNames = new ArrayList<>();
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

    private void bendLayoutUnderEvent() {
        switch (eventEnum) {
            case YELLOW_CARD:
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
                break;
        }
    }

    private void configureMinutePicker() {
        minutePicker.setMinValue(1);
        minutePicker.setMaxValue(90);
        minutePicker.setValue(1);
        minutePicker.setWrapSelectorWheel(false);
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
                    setPlayerNames();

                    authorsAdapter = new ArrayAdapter<>(context, R.layout.players_spinner_item, playerFullNames);
                    authorsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    authorSpinner.setAdapter(authorsAdapter);

                    assistAdapter = new ArrayAdapter<>(context, R.layout.players_spinner_item, playerFullNames);
                    assistAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    assistSpinner.setAdapter(assistAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<PlayerDTO>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void setPlayerNames() {
        playerFullNames = players.stream()
                .map(playerDTO -> playerDTO.getName() + " " + playerDTO.getSurname())
                .collect(Collectors.toList());
    }
}
