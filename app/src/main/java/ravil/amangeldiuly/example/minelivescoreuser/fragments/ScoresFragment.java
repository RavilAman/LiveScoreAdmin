package ravil.amangeldiuly.example.minelivescoreuser.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
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
    private List<LocalDate> daysButtonsText;
    private GameApi gameApi;
    private String selectedDate;
    private Context context;
    private RadioGroup radioGroup;
    private RadioButton radioButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.fragment_scores, container, false);
        radioGroup = currentView.findViewById(R.id.scores_page_days_button_radio_group);
        context = getContext();
        daysButtonsText = new ArrayList<>();

        getDays(LocalDate.now());
        setDaysToButtons();

        radioGroup.setOnCheckedChangeListener(radioGroupListener());
        radioButton = currentView.findViewById(R.id.scores_page_radio_button_3);
        radioButton.setChecked(true);
        Gson gson = new GsonBuilder()
                .setLenient()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer())
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BACKEND_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        gameApi = retrofit.create(GameApi.class);

        return currentView;
    }

    private RadioGroup.OnCheckedChangeListener radioGroupListener() {
        return (group, checkedId) -> {
            RadioButton radioButton = currentView.findViewById(checkedId);
            Toast.makeText(context, "Selected date for request: " + formatDateForRequest(
                    daysButtonsText.get(
                            getCheckedRadioButtonPosition(radioButton.getId())
                    )
            ), Toast.LENGTH_SHORT).show();
            System.out.println("Selected date for request: " + formatDateForRequest(
                    daysButtonsText.get(
                            getCheckedRadioButtonPosition(radioButton.getId())
                    )));
            Toast.makeText(context, "Selected date: " + radioButton.getText(), Toast.LENGTH_SHORT).show();
            System.out.println("Selected date: " + radioButton.getText());
        };
    }

    private void getDays(LocalDate date) {
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

    private String formatDaysButtonText(LocalDate date) {
        StringBuilder buttonDateText = new StringBuilder();
        buttonDateText.append(date.format(DateTimeFormatter.ofPattern("EEE")).toUpperCase());
        buttonDateText.append("\n");
        buttonDateText.append(date.getDayOfMonth());
        buttonDateText.append(" ");
        buttonDateText.append(date.format(DateTimeFormatter.ofPattern("MMM")).toUpperCase());
        return buttonDateText.toString();
    }

    private int getCheckedRadioButtonPosition(int id) {
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            if (radioGroup.getChildAt(i).getId() == id) {
                return i;
            }
        }
        return 2;
    }

    private void checkDates() {
        System.out.println("########################################################################################################################");

        daysButtonsText.forEach(date -> System.out.println(formatDaysButtonText(date)));

        System.out.println("########################################################################################################################");
    }


    private void setData() {
        gameApi.getGamesByDate(selectedDate).enqueue(new Callback<>() {
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

    private String formatDateForRequest(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
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

     @Override public void onItemClick(int position, String dayText) {
     if (!dayText.equals("")) {
     String message = "Selected Date " + dayText + " " + monthFromDate(selectedDate);
     Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
     }
     }
     */
}
