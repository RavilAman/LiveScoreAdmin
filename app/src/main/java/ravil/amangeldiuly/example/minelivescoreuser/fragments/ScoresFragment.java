package ravil.amangeldiuly.example.minelivescoreuser.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import ravil.amangeldiuly.example.minelivescoreuser.R;
import ravil.amangeldiuly.example.minelivescoreuser.calendar.CalendarAdapter;

public class ScoresFragment extends Fragment {

    private View currentView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.fragment_scores, container, false);

        return currentView;
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
