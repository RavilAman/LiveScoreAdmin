package ravil.amangeldiuly.example.minelivescoreuser.events;

import static ravil.amangeldiuly.example.minelivescoreuser.utils.GeneralUtils.gameScoreIntoDashFormat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

import ravil.amangeldiuly.example.minelivescoreuser.R;
import ravil.amangeldiuly.example.minelivescoreuser.web.responses.EventDTO;

public class EventAdapter extends RecyclerView.Adapter<EventViewHolder> {

    private Context context;
    private List<EventDTO> events;
    private Long team1Id;

    public EventAdapter(Context context, List<EventDTO> events, Long team1Id) {
        this.context = context;
        this.events = events;
        this.team1Id = team1Id;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.event_item, parent, false);
        return new EventViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        EventDTO eventDTO = events.get(position);
        holder.time.setText(eventDTO.getMinute() + "°");
        switch (eventDTO.getEventName()) {
            case "GOAL":
                holder.eventLogo.setImageResource(R.drawable.soccer_ball);
                if (Objects.equals(team1Id, eventDTO.getTeamId())) {
                    holder.team1Player.setText(eventDTO.getPlayerName());
                    if (eventDTO.getAssist() != null) {
                        holder.team1Assist.setText(eventDTO.getAssist().getAssistPlayer());
                    } else {
                        holder.team1Assist.setVisibility(View.GONE);
                    }
                    holder.team1Score.setText(gameScoreIntoDashFormat(eventDTO.getGameScore()));
                    holder.team2Player.setVisibility(View.GONE);
                    holder.team2Assist.setVisibility(View.GONE);
                    holder.team2Score.setVisibility(View.GONE);
                } else {
                    holder.team2Player.setText(eventDTO.getPlayerName());
                    if (eventDTO.getAssist() != null) {
                        holder.team2Assist.setText(eventDTO.getAssist().getAssistPlayer());
                    } else {
                        holder.team2Assist.setVisibility(View.GONE);
                    }
                    holder.team2Score.setText(gameScoreIntoDashFormat(eventDTO.getGameScore()));
                    holder.team1Player.setVisibility(View.GONE);
                    holder.team1Assist.setVisibility(View.GONE);
                    holder.team1Score.setVisibility(View.GONE);
                }
                break;
            case "YELLOW_CARD":
                holder.eventLogo.setImageResource(R.drawable.yellow_card);
                holder.team1Assist.setVisibility(View.GONE);
                holder.team1Score.setVisibility(View.GONE);
                holder.team2Assist.setVisibility(View.GONE);
                holder.team2Score.setVisibility(View.GONE);
                if (Objects.equals(team1Id, eventDTO.getTeamId())) {
                    holder.team1Player.setText(eventDTO.getPlayerName());
                    holder.team2Player.setVisibility(View.GONE);
                } else {
                    holder.team2Player.setText(eventDTO.getPlayerName());
                    holder.team1Player.setVisibility(View.GONE);
                }
                break;
            case "RED_CARD":
                holder.eventLogo.setImageResource(R.drawable.red_card);
                holder.team1Assist.setVisibility(View.GONE);
                holder.team1Score.setVisibility(View.GONE);
                holder.team2Assist.setVisibility(View.GONE);
                holder.team2Score.setVisibility(View.GONE);
                if (Objects.equals(team1Id, eventDTO.getTeamId())) {
                    holder.team1Player.setText(eventDTO.getPlayerName());
                    holder.team2Player.setVisibility(View.GONE);
                } else {
                    holder.team2Player.setText(eventDTO.getPlayerName());
                    holder.team1Player.setVisibility(View.GONE);
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return events.size();
    }
}
