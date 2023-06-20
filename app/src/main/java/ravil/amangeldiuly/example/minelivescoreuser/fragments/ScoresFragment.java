package ravil.amangeldiuly.example.minelivescoreuser.fragments;

import static ravil.amangeldiuly.example.minelivescoreuser.utils.GeneralUtils.titleCaseWord;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import ravil.amangeldiuly.example.minelivescoreuser.R;
import ravil.amangeldiuly.example.minelivescoreuser.UrlConstants;
import ravil.amangeldiuly.example.minelivescoreuser.calendar.CalendarAdapter;
import ravil.amangeldiuly.example.minelivescoreuser.dialog.CreateGameDialog;
import ravil.amangeldiuly.example.minelivescoreuser.groups.GroupAdapter;
import ravil.amangeldiuly.example.minelivescoreuser.utils.ActionInterfaces;
import ravil.amangeldiuly.example.minelivescoreuser.utils.LocalDateTimeDeserializer;
import ravil.amangeldiuly.example.minelivescoreuser.web.apis.GameApi;
import ravil.amangeldiuly.example.minelivescoreuser.web.responses.NewGameDTO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ScoresFragment extends Fragment implements CalendarAdapter.OnItemListener, ActionInterfaces.CreateGameDialogCloseListener {

    private Context context;

    private View currentView;
    private RadioGroup radioGroup;
    private RadioButton centerRadioButton;
    private TextView currentMonthText;
    private TextView currentYearText;
    private TextView noGamesText;
    private ImageButton calendarButton;
    private LinearLayout calendarHolderLayout;
    private RecyclerView calendarRecyclerView;
    private RecyclerView groupRecyclerView;
    private ImageButton previousMonthButton;
    private ImageButton nextMonthButton;
    private Button liveButton;
    private Button createGameButton;
    private Drawable liveButtonBackground;

    private Retrofit retrofit;
    private GameApi gameApi;

    private List<LocalDate> fiveDays;
    private List<Integer> currentMonthDays;
    private LocalDate lastSelectedDate;
    private int monthFirstWeekDay;
    private boolean calendarButtonClicked = true;
    private int lastCheckedRadioButtonId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.fragment_scores, container, false);
        initializeObjects();
        initializeRetrofit();
        initializeViews();
        setListeners();

        getDays(lastSelectedDate);
        setDaysToButtons();

        centerRadioButton.setChecked(true);

        return currentView;
    }

    private void initializeViews() {
        context = getContext();
        calendarRecyclerView = currentView.findViewById(R.id.calendar_recycler_view);
        groupRecyclerView = currentView.findViewById(R.id.group_recycler_view);
        radioGroup = currentView.findViewById(R.id.scores_page_days_button_radio_group);
        calendarButton = currentView.findViewById(R.id.scores_page_calendar_button);
        calendarHolderLayout = currentView.findViewById(R.id.calendar_holder_linear_layout);
        currentMonthText = currentView.findViewById(R.id.current_month_text);
        currentYearText = currentView.findViewById(R.id.current_year_text);
        noGamesText = currentView.findViewById(R.id.no_games_text_view);
        previousMonthButton = currentView.findViewById(R.id.previous_month_button);
        nextMonthButton = currentView.findViewById(R.id.next_month_button);
        centerRadioButton = currentView.findViewById(R.id.scores_page_radio_button_3);
        liveButton = currentView.findViewById(R.id.scores_page_live_button);
        liveButtonBackground = liveButton.getBackground();
        createGameButton = currentView.findViewById(R.id.fragment_scores_create_game_button);
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
        gameApi = retrofit.create(GameApi.class);
    }

    private void initializeObjects() {
        if (lastSelectedDate == null) {
            lastSelectedDate = LocalDate.now();
        }
        fiveDays = new ArrayList<>();
        currentMonthDays = new ArrayList<>();
    }

    private void setListeners() {
        calendarButton.setOnClickListener(calendarButtonOnClickListener());
        previousMonthButton.setOnClickListener(previousMonthButtonOnClick());
        nextMonthButton.setOnClickListener(nextMonthButtonOnClick());
        radioGroup.setOnCheckedChangeListener(radioGroupListener());
        liveButton.setOnClickListener(liveButtonListener());
        createGameButton.setOnClickListener(createGameListener());
    }

    private View.OnClickListener createGameListener() {
        return view -> {
            CreateGameDialog createGameDialog = new CreateGameDialog(this);
            createGameDialog.show(getParentFragmentManager(), "");
        };
    }

    private RadioGroup.OnCheckedChangeListener radioGroupListener() {
        return (group, checkedId) -> {
            lastSelectedDate = fiveDays.get(getCheckedRadioButtonPosition(checkedId));
            setGamesForSelectedDate(formatDateForRequest(lastSelectedDate));
            lastCheckedRadioButtonId = checkedId;
            changeLiveButtonColor(R.color.button_grey);
        };
    }

    private View.OnClickListener liveButtonListener() {
        return view -> {
            RadioButton lastCheckedButton = currentView.findViewById(lastCheckedRadioButtonId);
            lastCheckedButton.setChecked(false);
            changeLiveButtonColor(R.color.app_orange);
            setGamesLive();
        };
    }

    private void changeLiveButtonColor(int color) {
        ColorStateList colorStateList = ContextCompat.getColorStateList(context, color);
        Drawable wrappedDrawable = DrawableCompat.wrap(liveButtonBackground);
        DrawableCompat.setTintList(wrappedDrawable, colorStateList);
        liveButton.setBackground(wrappedDrawable);
    }

    private void setGamesLive() {
        gameApi.getLiveGames().enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<NewGameDTO>> call, Response<List<NewGameDTO>> response) {
                noGamesText.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    setGames(response.body());
                    if (response.body().isEmpty()) {
                        noGamesText.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<NewGameDTO>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void setGamesForSelectedDate(String requestedDate) {
        noGamesText.setVisibility(View.GONE);
        gameApi.getGamesByDate(requestedDate).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<NewGameDTO>> call, Response<List<NewGameDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    setGames(response.body());
                    if (response.body().isEmpty()) {
                        noGamesText.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<NewGameDTO>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private int getCheckedRadioButtonPosition(int id) {
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            if (radioGroup.getChildAt(i).getId() == id) {
                return i;
            }
        }
        return 2;
    }

    private String formatDateForRequest(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    private String formatDaysButtonText(LocalDate date) {
        StringBuilder buttonDateText = new StringBuilder();
        buttonDateText.append(date.format(DateTimeFormatter.ofPattern("EEE")).toUpperCase());
        buttonDateText.append("\n");
        buttonDateText.append(date.getDayOfMonth());
        buttonDateText.append(" ");
        buttonDateText.append(date.format(DateTimeFormatter.ofPattern("MMM")).toUpperCase());
        return buttonDateText.toString();
    }

    private void getDays(LocalDate date) {
        fiveDays.clear();
        LocalDate forSave = LocalDate.of(date.getYear(), date.getMonth(), date.getDayOfMonth());
        for (int i = -2; i < 3; i++) {
            date = date.plusDays(i);
            fiveDays.add(date);
            date = forSave;
        }
    }

    private void setDaysToButtons() {
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
            radioButton.setText(
                    formatDaysButtonText(fiveDays.get(i))
            );
        }
    }

    private void loadAndSetMonthAndYear(LocalDate date) {
        currentMonthText.setText(titleCaseWord(
                date.getMonth().toString()
        ));
        currentYearText.setText(titleCaseWord(
                String.valueOf(date.getYear())
        ));
    }

    private void loadMonthDays(LocalDate date) {
        currentMonthDays.clear();
        setMonthFirstWeekDay(date);
        int year = date.getYear();
        int month = date.getMonthValue();
        YearMonth yearMonth = YearMonth.of(year, month);
        int daysInMonth = yearMonth.lengthOfMonth();
        for (int day = 1; day <= daysInMonth; day++) {
            currentMonthDays.add(day);
        }
    }

    public View.OnClickListener calendarButtonOnClickListener() {
        return view -> {
            if (calendarButtonClicked) {
                calendarHolderLayout.setVisibility(View.VISIBLE);
                loadAndSetMonthAndYear(lastSelectedDate);
                loadMonthDays(lastSelectedDate);
                setCalendar(currentMonthDays);
            } else {
                calendarHolderLayout.setVisibility(View.GONE);
            }
            calendarButtonClicked = !calendarButtonClicked;
        };
    }

    private void setCalendar(List<Integer> currentMonthDays) {
        GridLayoutManager calendarLayoutManager = new GridLayoutManager(context, 7);
        CalendarAdapter calendarAdapter = new CalendarAdapter(context, currentMonthDays, this);
        calendarRecyclerView.setLayoutManager(calendarLayoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    private void setGames(List<NewGameDTO> games) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        GroupAdapter groupAdapter = new GroupAdapter(context, games);
        groupRecyclerView.setLayoutManager(linearLayoutManager);
        groupRecyclerView.setAdapter(groupAdapter);
    }

    private View.OnClickListener previousMonthButtonOnClick() {
        return view -> {
            lastSelectedDate = lastSelectedDate.minusMonths(1);
            loadAndSetMonthAndYear(lastSelectedDate);
            loadMonthDays(lastSelectedDate);
            setCalendar(currentMonthDays);
        };
    }

    private View.OnClickListener nextMonthButtonOnClick() {
        return view -> {
            lastSelectedDate = lastSelectedDate.plusMonths(1);
            loadAndSetMonthAndYear(lastSelectedDate);
            loadMonthDays(lastSelectedDate);
            setCalendar(currentMonthDays);
        };
    }

    private void setMonthFirstWeekDay(LocalDate date) {
        monthFirstWeekDay = date.withDayOfMonth(1)
                .getDayOfWeek()
                .getValue() - 1;
        for (int i = 0; i < monthFirstWeekDay; i++) {
            currentMonthDays.add(0);
        }
    }

    @Override
    public void onItemClick(int day) {
        calendarHolderLayout.setVisibility(View.GONE);
        LocalDate selectedDate = LocalDate.of(lastSelectedDate.getYear(), lastSelectedDate.getMonth(), day);
        getDays(selectedDate);
        setDaysToButtons();
        lastSelectedDate = selectedDate;
        centerRadioButton.setChecked(true);
        setGamesForSelectedDate(formatDateForRequest(selectedDate));
    }

    @Override
    public void onDialogClosed(LocalDate toDate) {
        getDays(toDate);
        setDaysToButtons();
        lastSelectedDate = toDate;
        centerRadioButton.setChecked(true);
        setGamesForSelectedDate(formatDateForRequest(toDate));
    }
}