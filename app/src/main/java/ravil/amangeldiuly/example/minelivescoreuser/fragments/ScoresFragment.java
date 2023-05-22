package ravil.amangeldiuly.example.minelivescoreuser.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import ravil.amangeldiuly.example.minelivescoreuser.Constants;
import ravil.amangeldiuly.example.minelivescoreuser.R;
import ravil.amangeldiuly.example.minelivescoreuser.utils.LocalDateTimeDeserializer;
import ravil.amangeldiuly.example.minelivescoreuser.web.apis.GameApi;
import ravil.amangeldiuly.example.minelivescoreuser.web.responses.NewGameDTO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ScoresFragment extends Fragment {

    private View currentView;
    private Retrofit retrofit;
    private Button liveButton;
    private Button dateButton1;
    private Button dateButton2;
    private Button dateButton3;
    private Button dateButton4;
    private Button dateButton5;
    private List<String> daysButtonsText;
    private List<Button> daysButtons;
    private GameApi gameApi;
    private String selectedDate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.fragment_scores, container, false);

        daysButtonsText = new ArrayList<>();
        daysButtons = new ArrayList<>();

        Gson gson = new GsonBuilder()
                .setLenient()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer())
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BACKEND_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        gameApi = retrofit.create(GameApi.class);

        dateButton1 = currentView.findViewById(R.id.scores_page_button_1);
        dateButton2 = currentView.findViewById(R.id.scores_page_button_2);
        dateButton3 = currentView.findViewById(R.id.scores_page_button_3);
        dateButton4 = currentView.findViewById(R.id.scores_page_button_4);
        dateButton5 = currentView.findViewById(R.id.scores_page_button_5);

        daysButtons.add(dateButton1);
        daysButtons.add(dateButton2);
        daysButtons.add(dateButton3);
        daysButtons.add(dateButton4);
        daysButtons.add(dateButton5);

        dateButton1.setOnClickListener(daysButtonsClickListener());

        getCurrentDate(LocalDate.now());
        setDaysButtonText();

        return currentView;
    }

    private void getCurrentDate(LocalDate date) {
        StringBuilder buttonDateText = new StringBuilder();
        LocalDate forSave = LocalDate.of(date.getYear(), date.getMonth(), date.getDayOfMonth());
        for (int i = -2; i < 3; i++) {
            buttonDateText.setLength(0);
            date = date.plusDays(i);
            buttonDateText.append(date.format(DateTimeFormatter.ofPattern("EEE")).toUpperCase());
            buttonDateText.append("\n");
            buttonDateText.append(date.getDayOfMonth());
            buttonDateText.append(" ");
            buttonDateText.append(date.format(DateTimeFormatter.ofPattern("MMM")).toUpperCase());
            daysButtonsText.add(buttonDateText.toString());
            date = forSave;
        }
    }

    private void setDaysButtonText() {
        for (int i = 0; i < daysButtons.size(); i++) {
            daysButtons.get(i)
                    .setText(daysButtonsText.get(i));
        }
    }

    private View.OnClickListener daysButtonsClickListener() {
        return view -> {
            gameApi.getGamesByDate(selectedDate).enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<List<NewGameDTO>> call, Response<List<NewGameDTO>> response) {

                }

                @Override
                public void onFailure(Call<List<NewGameDTO>> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        };
    }






















    /** Calendar dump

     implements CalendarAdapter.OnItemListener

     private TextView monthText;
     private RecyclerView calendarRecyclerView;
     private LocalDate selectedDate;
     private ImageButton previousMonthButton;
     private ImageButton nextMonthButton;


     initWidgets();
     selectedDate = LocalDate.now();
     setMonthView();

     previousMonthButton = currentView.findViewById(R.id.previous_month_button);
     nextMonthButton = currentView.findViewById(R.id.next_month_button);
     previousMonthButton.setOnClickListener(previousMonthAction());
     nextMonthButton.setOnClickListener(nextMonthAction());

    private void initWidgets() {
        calendarRecyclerView = currentView.findViewById(R.id.calendar_recycler_view);
        monthText = currentView.findViewById(R.id.current_month_text);
    }

    private void setMonthView() {
        monthText.setText(monthFromDate(selectedDate));
        ArrayList<String> daysInMonth = daysInMonthArray(selectedDate);
        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    private ArrayList<String> daysInMonthArray(LocalDate date) {
        ArrayList<String> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);
        int daysInMonth = yearMonth.lengthOfMonth();
        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        for (int i = 1; i <= 42; i++) {
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
                daysInMonthArray.add("");
            } else {
                daysInMonthArray.add(String.valueOf(i - dayOfWeek));
            }
        }
        return daysInMonthArray;
    }

    private String monthFromDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM");
        return date.format(formatter);
    }

    public View.OnClickListener previousMonthAction() {
        return view -> {
            selectedDate = selectedDate.minusMonths(1);
            setMonthView();
        };
    }

    public View.OnClickListener nextMonthAction() {
        return view -> {
            selectedDate = selectedDate.plusMonths(1);
            setMonthView();
        };
    }

    @Override
    public void onItemClick(int position, String dayText) {
        if (!dayText.equals("")) {
            String message = "Selected Date " + dayText + " " + monthFromDate(selectedDate);
            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
        }
    }
    */
}
