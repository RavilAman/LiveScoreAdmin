package ravil.amangeldiuly.example.minelivescoreuser.calendar;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.List;

import ravil.amangeldiuly.example.minelivescoreuser.R;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder> {

    private Context context;
    private List<Integer> days;
    private final OnItemListener onItemListener;

    public CalendarAdapter(Context context, List<Integer> days, OnItemListener onItemListener) {
        this.context = context;
        this.days = days;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.calendar_cell, parent, false);
        return new CalendarViewHolder(view, onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        if (days.get(position) != 0) {
            holder.cellDayText.setText(
                    String.valueOf(days.get(position))
            );
        } else {
            holder.cellDayText.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    public interface OnItemListener {
        void onItemClick(int day);
    }
}
