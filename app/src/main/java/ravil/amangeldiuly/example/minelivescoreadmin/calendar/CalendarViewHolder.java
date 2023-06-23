package ravil.amangeldiuly.example.minelivescoreadmin.calendar;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ravil.amangeldiuly.example.minelivescoreadmin.R;

public class CalendarViewHolder extends RecyclerView.ViewHolder {

    TextView cellDayText;
    private final CalendarAdapter.OnItemListener onItemListener;

    public CalendarViewHolder(@NonNull View itemView, CalendarAdapter.OnItemListener onItemListener) {
        super(itemView);
        cellDayText = itemView.findViewById(R.id.cell_day_text);
        this.onItemListener = onItemListener;
        cellDayText.setOnClickListener(calendarDayClickListener());
    }

    private View.OnClickListener calendarDayClickListener() {
        return view -> {
            String day = cellDayText.getText().toString();
            if (!day.isEmpty()) {
                onItemListener.onItemClick(Integer.parseInt(day));
            }
        };
    }
}
