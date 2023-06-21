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
import ravil.amangeldiuly.example.minelivescoreuser.enums.EventEnum;
import ravil.amangeldiuly.example.minelivescoreuser.utils.ActionInterfaces;
import ravil.amangeldiuly.example.minelivescoreuser.web.responses.AssistDTO;
import ravil.amangeldiuly.example.minelivescoreuser.web.responses.EventDTO;

public class EventAdapter extends RecyclerView.Adapter<EventViewHolder> {

    private Context context;
    private List<EventDTO> events;
    private Long team1Id;
    private ActionInterfaces.ManipulateEventDialogOpenListener manipulateEventDialogOpenListener;

    public EventAdapter(Context context, List<EventDTO> events, Long team1Id,
                        ActionInterfaces.ManipulateEventDialogOpenListener manipulateEventDialogOpenListener) {
        this.context = context;
        this.events = events;
        this.team1Id = team1Id;
        this.manipulateEventDialogOpenListener = manipulateEventDialogOpenListener;
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
        EventEnum eventEnum = null;
        holder.time.setText(eventDTO.getMinute() + "°");
        String eventName = eventDTO.getEventName();
        if (eventName.equals("GOAL")) {
            eventEnum = EventEnum.GOAL;
        } else if (eventName.equals("PENALTY")) {
            eventEnum = EventEnum.PENALTY;
        }
        switch (eventDTO.getEventName()) {
            case "GOAL":
            case "PENALTY":
                holder.gameScore.setText(gameScoreIntoDashFormat(eventDTO.getGameScore()));
                if (Objects.equals(team1Id, eventDTO.getTeamId())) {
                    holder.eventLogoTeam1.setImageResource(R.drawable.soccer_ball);
                    holder.eventLogoTeam2.setVisibility(View.GONE);
                    holder.team1Player.setText(eventDTO.getPlayerName());
                    if (eventDTO.getAssist() != null) {
                        holder.team1Assist.setText(eventDTO.getAssist().getAssistPlayer());
                    } else {
                        holder.team1Assist.setVisibility(View.GONE);
                    }
                    holder.team2Player.setVisibility(View.GONE);
                    holder.team2Assist.setVisibility(View.GONE);
                } else {
                    holder.eventLogoTeam1.setVisibility(View.GONE);
                    holder.eventLogoTeam2.setImageResource(R.drawable.soccer_ball);
                    holder.team2Player.setText(eventDTO.getPlayerName());
                    if (eventDTO.getAssist() != null) {
                        holder.team2Assist.setText(eventDTO.getAssist().getAssistPlayer());
                    } else {
                        holder.team2Assist.setVisibility(View.GONE);
                    }
                    holder.team1Player.setVisibility(View.GONE);
                    holder.team1Assist.setVisibility(View.GONE);
                }
                break;
            case "YELLOW_CARD":
                holder.team1Assist.setVisibility(View.GONE);
                holder.team2Assist.setVisibility(View.GONE);
                holder.gameScore.setText("     ");
                if (Objects.equals(team1Id, eventDTO.getTeamId())) {
                    holder.eventLogoTeam1.setImageResource(R.drawable.yellow_card);
                    holder.eventLogoTeam2.setVisibility(View.GONE);
                    holder.team1Player.setText(eventDTO.getPlayerName());
                    holder.team2Player.setVisibility(View.GONE);
                } else {
                    holder.eventLogoTeam2.setImageResource(R.drawable.yellow_card);
                    holder.eventLogoTeam1.setVisibility(View.GONE);
                    holder.team2Player.setText(eventDTO.getPlayerName());
                    holder.team1Player.setVisibility(View.GONE);
                }
                eventEnum = EventEnum.YELLOW_CARD;
                break;
            case "RED_CARD":
                holder.team1Assist.setVisibility(View.GONE);
                holder.team2Assist.setVisibility(View.GONE);
                holder.gameScore.setText("     ");
                if (Objects.equals(team1Id, eventDTO.getTeamId())) {
                    holder.eventLogoTeam1.setImageResource(R.drawable.red_card);
                    holder.eventLogoTeam2.setVisibility(View.GONE);
                    holder.team1Player.setText(eventDTO.getPlayerName());
                    holder.team2Player.setVisibility(View.GONE);
                } else {
                    holder.eventLogoTeam2.setImageResource(R.drawable.red_card);
                    holder.eventLogoTeam1.setVisibility(View.GONE);
                    holder.team2Player.setText(eventDTO.getPlayerName());
                    holder.team1Player.setVisibility(View.GONE);
                }
                eventEnum = EventEnum.RED_CARD;
                break;
            case "SECOND_YELLOW_CARD":
                holder.team1Assist.setVisibility(View.GONE);
                holder.team2Assist.setVisibility(View.GONE);
                holder.gameScore.setText("     ");
                if (Objects.equals(team1Id, eventDTO.getTeamId())) {
                    holder.eventLogoTeam1.setImageResource(R.drawable.second_yellow);
                    holder.eventLogoTeam2.setVisibility(View.GONE);
                    holder.team1Player.setText(eventDTO.getPlayerName());
                    holder.team2Player.setVisibility(View.GONE);
                } else {
                    holder.eventLogoTeam2.setImageResource(R.drawable.second_yellow);
                    holder.eventLogoTeam1.setVisibility(View.GONE);
                    holder.team2Player.setText(eventDTO.getPlayerName());
                    holder.team1Player.setVisibility(View.GONE);
                }
                eventEnum = EventEnum.SECOND_YELLOW_CARD;
                break;
        }
        holder.editEvent.setOnClickListener(editEventListener(eventDTO.getTeamLogo(), eventDTO.getTeamId(),
                eventEnum, eventDTO.getPlayerId(), eventDTO.getMinute(), eventDTO.getEventId(), eventDTO.getAssist()));
    }

    private View.OnClickListener editEventListener(String teamLogo, Long teamId, EventEnum eventEnum, Long playerId, Integer minute, Long eventId, AssistDTO assistDTO) {
        return view -> manipulateEventDialogOpenListener.onDialogOpen(teamLogo, teamId, eventEnum, playerId, minute, eventId, assistDTO);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }
}
