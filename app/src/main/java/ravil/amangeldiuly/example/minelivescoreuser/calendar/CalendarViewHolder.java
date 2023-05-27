package ravil.amangeldiuly.example.minelivescoreuser.calendar;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ravil.amangeldiuly.example.minelivescoreuser.R;

public class CalendarViewHolder extends RecyclerView.ViewHolder{

    TextView cellDayText;

    public CalendarViewHolder(@NonNull View itemView) {
        super(itemView);

        cellDayText = itemView.findViewById(R.id.cell_day_text);
    }
}
