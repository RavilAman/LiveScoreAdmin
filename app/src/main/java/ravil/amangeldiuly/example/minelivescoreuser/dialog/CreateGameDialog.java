package ravil.amangeldiuly.example.minelivescoreuser.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import ravil.amangeldiuly.example.minelivescoreuser.R;
import ravil.amangeldiuly.example.minelivescoreuser.UrlConstants;
import ravil.amangeldiuly.example.minelivescoreuser.utils.LocalDateTimeDeserializer;
import ravil.amangeldiuly.example.minelivescoreuser.web.apis.GameApi;
import ravil.amangeldiuly.example.minelivescoreuser.web.apis.GroupApi;
import ravil.amangeldiuly.example.minelivescoreuser.web.apis.TeamApi;
import ravil.amangeldiuly.example.minelivescoreuser.web.apis.TournamentApi;
import ravil.amangeldiuly.example.minelivescoreuser.web.responses.GameDTO;
import ravil.amangeldiuly.example.minelivescoreuser.web.responses.GroupDTO;
import ravil.amangeldiuly.example.minelivescoreuser.web.responses.SaveGameDTO;
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
    private ArrayAdapter<String> tournamentsAdapter;
    private ArrayAdapter<String> groupsAdapter;
    private ArrayAdapter<String> homeTeamsAdapter;
    private ArrayAdapter<String> awayTeamsAdapter;
    private NumberPicker monthPicker;
    private NumberPicker dayPicker;
    private NumberPicker yearPicker;
    private NumberPicker hourPicker;
    private NumberPicker minutePicker;
    private Button createGameButton;

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
    private String[] months;
    private String[] days;
    private String[] hours;
    private String[] minutes;
    private SaveGameDTO saveGameDTO;
    private DateTimeFormatter formatter;

    private Retrofit retrofit;
    private TournamentApi tournamentApi;
    private GroupApi groupApi;
    private TeamApi teamApi;
    private GameApi gameApi;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        initializeRetrofit();
        initializeObjects();
        initializeViews();
        setListeners();
        setFullWidth();
        resolveDates();
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
        gameApi = retrofit.create(GameApi.class);
    }

    private void initializeViews() {
        context = requireContext();
        createGameDialog = new Dialog(context, R.style.CustomDialogTheme);
        currentView = getActivity().getLayoutInflater().inflate(R.layout.create_game_window, null);
        createGameDialog.setContentView(currentView);

        tournamentSpinner = currentView.findViewById(R.id.create_game_tournaments_spinner);
        groupSpinner = currentView.findViewById(R.id.create_game_group_spinner);
        homeTeamsSpinner = currentView.findViewById(R.id.create_game_home_teams_spinner);
        awayTeamsSpinner = currentView.findViewById(R.id.create_game_away_teams_spinner);
        monthPicker = currentView.findViewById(R.id.create_game_month_picker);
        dayPicker = currentView.findViewById(R.id.create_game_day_picker);
        yearPicker = currentView.findViewById(R.id.create_game_year_picker);
        hourPicker = currentView.findViewById(R.id.create_game_hour_picker);
        minutePicker = currentView.findViewById(R.id.create_game_minute_picker);
        createGameButton = currentView.findViewById(R.id.create_game_button);
    }

    private void setListeners() {
        tournamentSpinner.setOnItemSelectedListener(this);
        groupSpinner.setOnItemSelectedListener(this);
        homeTeamsSpinner.setOnItemSelectedListener(this);
        awayTeamsSpinner.setOnItemSelectedListener(this);
        createGameButton.setOnClickListener(createGameButtonListener());
        monthPicker.setOnValueChangedListener(monthPickerListener());
    }

    private LocalDateTime getCurrentDateFromPicker(String day) {
        String dateTimeString = yearPicker.getValue()
                + "-" + String.format(Locale.getDefault(), "%02d", monthPicker.getValue() + 1)
                + "-" + day
                + " " + hours[hourPicker.getValue()]
                + ":" + minutes[minutePicker.getValue()];
        System.out.println("Дату сформировал такую: " + dateTimeString);
        return LocalDateTime.parse(dateTimeString, formatter);
    }

    private NumberPicker.OnValueChangeListener monthPickerListener() {
        return (picker, oldVal, newVal) -> {
            // todo: то же самое на year сделать
            LocalDateTime currentDate = getCurrentDateFromPicker(days[0]);
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println(currentDate);
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            Month currentMonth = currentDate.getMonth();
            System.out.println(currentMonth);
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

            System.out.println("############################################");
            int currentYear = currentDate.getYear();
            System.out.println(currentYear);
            System.out.println("############################################");

            System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
            int monthLength = currentMonth.length(Year.of(currentYear).isLeap());
            System.out.println(monthLength);
            System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");

            System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
            days = new String[monthLength];
            Arrays.stream(days).forEach(System.out::println);
            System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");

            System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
            fillArrayData(days, monthLength, true);
            Arrays.stream(days).forEach(System.out::println);
            System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

            System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
            System.out.println(currentDate.getDayOfMonth());
            System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");

            daysPickerSetup(monthLength, currentDate.getDayOfMonth());
        };
    }

    private void resolveDates() {
        ZoneId timeZone = ZoneId.of("GMT+6");
        ZonedDateTime zonedDateTime = ZonedDateTime.now(timeZone);
        LocalDateTime currentDate = zonedDateTime.toLocalDateTime();
        Month currentMonth = currentDate.getMonth();
        int currentYear = currentDate.getYear();
        int monthLength = currentMonth.length(Year.of(currentYear).isLeap());

        days = new String[monthLength];
        hours = new String[24];
        minutes = new String[60];

        fillArrayData(days, monthLength, true);
        fillArrayData(hours, 24, false);
        fillArrayData(minutes, 60, false);

        monthPickerSetup(currentDate.getMonthValue() - 1);
        daysPickerSetup(monthLength, currentDate.getDayOfMonth());
        yearPickerSetup(currentYear);
        hourPickerSetup(currentDate.getHour());
        minutePickerSetup(currentDate.getMinute());
    }

    private View.OnClickListener createGameButtonListener() {
        return view -> createGame();
    }

    private void monthPickerSetup(int currentMonth) {
        monthPicker.setMinValue(0);
        monthPicker.setMaxValue(11);
        monthPicker.setDisplayedValues(months);
        monthPicker.setValue(currentMonth);
        monthPicker.setWrapSelectorWheel(false);
    }

    private void daysPickerSetup(int currentMonthDayCount, int currentDay) {
        dayPicker.setMinValue(1);
        System.out.println("*********************************************");
        System.out.println(currentMonthDayCount);
        dayPicker.setMaxValue(currentMonthDayCount);
        System.out.println("*********************************************");
        dayPicker.setDisplayedValues(days);
        dayPicker.setValue(currentDay);
        dayPicker.setWrapSelectorWheel(false);
    }

    private void yearPickerSetup(int currentYear) {
        yearPicker.setMinValue(currentYear - 5);
        yearPicker.setMaxValue(currentYear + 5);
        yearPicker.setValue(currentYear);
        yearPicker.setWrapSelectorWheel(false);
    }

    private void hourPickerSetup(int currentHour) {
        hourPicker.setMinValue(0);
        hourPicker.setMaxValue(23);
        hourPicker.setDisplayedValues(hours);
        hourPicker.setValue(currentHour);
        hourPicker.setWrapSelectorWheel(false);
    }

    private void minutePickerSetup(int currentMinute) {
        minutePicker.setMinValue(0);
        minutePicker.setMaxValue(59);
        minutePicker.setDisplayedValues(minutes);
        minutePicker.setValue(currentMinute);
        minutePicker.setWrapSelectorWheel(false);
    }

    private void fillArrayData(String[] array, int endValue, boolean days) {
        int count = days ? 1 : 0;
        for (int i = 0; i < endValue; i++) {
            array[i] = String.format(Locale.getDefault(), "%02d", count);
            count++;
        }
    }

    private void initializeObjects() {
        tournaments = new ArrayList<>();
        tournamentNames = new ArrayList<>();
        groups = new ArrayList<>();
        groupNames = new ArrayList<>();
        teams = new ArrayList<>();
        teamNames = new ArrayList<>();
        getTournaments();
        months = getResources().getStringArray(R.array.months);
        saveGameDTO = new SaveGameDTO();
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
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
            saveGameDTO.setGroupId(selectedGroup.getGroupId());
        }
        if (parent.getId() == R.id.create_game_home_teams_spinner) {
            selectedHomeTeam = identifyTeam(homeTeamsSpinner.getSelectedItem().toString());
            saveGameDTO.setTeam1Id(selectedHomeTeam.getTeamId());
        }
        if (parent.getId() == R.id.create_game_away_teams_spinner) {
            selectedAwayTeam = identifyTeam(awayTeamsSpinner.getSelectedItem().toString());
            saveGameDTO.setTeam2Id(selectedAwayTeam.getTeamId());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private void createGame() {
        saveGameDTO.setDateTime(getCurrentDateFromPicker(
                days[dayPicker.getValue() - 1])
        );
        System.out.println("@#$%^#$%^#$%^$%");
        System.out.println(saveGameDTO);
        System.out.println("@#$%^#$%^#$%^$%");

        gameApi.postGame(saveGameDTO).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<GameDTO> call, Response<GameDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    System.out.println("!!Success!!");
                } else if (response.code() == 403) {
                    System.out.println("!!403!!");
                }
                else {
                    System.out.println("!!failure!!, " + response.code());
                }
            }

            @Override
            public void onFailure(Call<GameDTO> call, Throwable t) {
                System.out.println("!!error!!");
                t.printStackTrace();
            }
        });
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
