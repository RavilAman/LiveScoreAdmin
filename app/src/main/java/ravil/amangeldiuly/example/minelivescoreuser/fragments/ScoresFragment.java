package ravil.amangeldiuly.example.minelivescoreuser.fragments;

import static ravil.amangeldiuly.example.minelivescoreuser.utils.GeneralUtils.titleCaseWord;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import ravil.amangeldiuly.example.minelivescoreuser.Constants;
import ravil.amangeldiuly.example.minelivescoreuser.R;
import ravil.amangeldiuly.example.minelivescoreuser.calendar.CalendarAdapter;
import ravil.amangeldiuly.example.minelivescoreuser.utils.LocalDateTimeDeserializer;
import ravil.amangeldiuly.example.minelivescoreuser.web.apis.GameApi;
import ravil.amangeldiuly.example.minelivescoreuser.web.responses.NewGameDTO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ScoresFragment extends Fragment implements CalendarAdapter.OnItemListener {

    private View currentView;
    private Retrofit retrofit;
    private List<LocalDate> daysButtonsText;
    private GameApi gameApi;
    private Context context;
    private RadioGroup radioGroup;
    private RadioButton centerRadioButton;
    private List<Integer> currentMonthDays;
    private TextView currentMonthText;
    private TextView currentYearText;
    private ImageButton calendarButton;
    private LinearLayout calendarHolderLayout;
    private LocalDate lastSelectedDate;
    private boolean calendarButtonClicked = true;
    private RecyclerView calendarRecyclerView;
    private ImageButton previousMonthButton;
    private ImageButton nextMonthButton;
    private int startingWeekDay;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        lastSelectedDate = LocalDate.now();
        currentView = inflater.inflate(R.layout.fragment_scores, container, false);
        calendarRecyclerView = currentView.findViewById(R.id.calendar_recycler_view);
        radioGroup = currentView.findViewById(R.id.scores_page_days_button_radio_group);
        context = getContext();
        daysButtonsText = new ArrayList<>();
        currentMonthDays = new ArrayList<>();
        calendarButton = currentView.findViewById(R.id.scores_page_calendar_button);
        calendarButton.setOnClickListener(calendarButtonOnClickListener());
        calendarHolderLayout = currentView.findViewById(R.id.calendar_holder_linear_layout);
        currentMonthText = currentView.findViewById(R.id.current_month_text);
        currentYearText = currentView.findViewById(R.id.current_year_text);
        previousMonthButton = currentView.findViewById(R.id.previous_month_button);
        nextMonthButton = currentView.findViewById(R.id.next_month_button);

        previousMonthButton.setOnClickListener(previousMonthButtonOnClick());
        nextMonthButton.setOnClickListener(nextMonthButtonOnClick());

        Gson gson = new GsonBuilder()
                .setLenient()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer())
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BACKEND_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        gameApi = retrofit.create(GameApi.class);

        getDays(LocalDate.now());
        setDaysToButtons();

        radioGroup.setOnCheckedChangeListener(radioGroupListener());
        centerRadioButton = currentView.findViewById(R.id.scores_page_radio_button_3);
        centerRadioButton.setChecked(true);

        return currentView;
    }

    private RadioGroup.OnCheckedChangeListener radioGroupListener() {
        return (group, checkedId) -> {
            RadioButton radioButton = currentView.findViewById(checkedId);
            setData(formatDateForRequest(
                    daysButtonsText.get(
                            getCheckedRadioButtonPosition(checkedId)
                    )
            ));

            Toast.makeText(context, "Selected date for request: " + formatDateForRequest(
                    daysButtonsText.get(
                            getCheckedRadioButtonPosition(radioButton.getId()))
            ), Toast.LENGTH_SHORT).show();
        };
    }


    private void setData(String requestedDate) {
        gameApi.getGamesByDate(requestedDate).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<NewGameDTO>> call, Response<List<NewGameDTO>> response) {
                Log.d("Retrieved data", response.toString());
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
        daysButtonsText.clear();
        LocalDate forSave = LocalDate.of(date.getYear(), date.getMonth(), date.getDayOfMonth());
        for (int i = -2; i < 3; i++) {
            date = date.plusDays(i);
            daysButtonsText.add(date);
            date = forSave;
        }
    }

    private void setDaysToButtons() {
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
            radioButton.setText(
                    formatDaysButtonText(daysButtonsText.get(i))
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
        setStartingWeekDay(date);
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

    private void setStartingWeekDay(LocalDate date) {
        startingWeekDay = date.withDayOfMonth(1)
                .getDayOfWeek()
                .getValue() - 1;
        for (int i = 0; i < startingWeekDay; i++) {
            currentMonthDays.add(0);
        }
    }

    @Override
    public void onItemClick(int day) {
        calendarHolderLayout.setVisibility(View.GONE);
        LocalDate selectedDate = LocalDate.of(lastSelectedDate.getYear(), lastSelectedDate.getMonth(), day);
        getDays(selectedDate);
        setDaysToButtons();
        setData(formatDateForRequest(selectedDate));
    }
}
