package ravil.amangeldiuly.example.minelivescoreuser.players;

import static ravil.amangeldiuly.example.minelivescoreuser.UrlConstants.BACKEND_URL;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ravil.amangeldiuly.example.minelivescoreuser.R;
import ravil.amangeldiuly.example.minelivescoreuser.web.responses.PlayerDTO;
import ravil.amangeldiuly.example.minelivescoreuser.web.responses.TeamDTO;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PlayersViewHolder extends RecyclerView.ViewHolder {

    TextView playerName;
    TextView playerNumber;
    Spinner spinner;
    PlayerAdapter.OnItemListener onItemListener;
    Context context;
    PlayerDTO playerDTO;
    Retrofit retrofit;
    List<TeamDTO> teamList;

    public PlayersViewHolder(@NonNull View itemView, PlayerAdapter.OnItemListener onItemListener, Context context,List<TeamDTO>teamList) {
        super(itemView);
        this.playerName = itemView.findViewById(R.id.card_player_name);
        this.playerNumber = itemView.findViewById(R.id.card_player_number);
        this.spinner = itemView.findViewById(R.id.list_teams);
        this.onItemListener = onItemListener;
        this.context = context;
        this.teamList = teamList;

        List<String> itemList = teamList.stream().map(TeamDTO::getTeamName).collect(Collectors.toList());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, itemList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(BACKEND_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        itemView.setOnClickListener(v -> onItemListener.onItemClick(playerDTO));
    }

    public void setDefaultSelectedItem(Long playerTeamId) {
        int defaultSelectedIndex = -1;
        for (int i = 0; i < teamList.size(); i++) {
            if (playerTeamId.equals(teamList.get(i).getTeamId())) {
                defaultSelectedIndex = i;
                break;
            }
        }

        // Set the default selected item
        if (defaultSelectedIndex != -1) {
            spinner.setSelection(defaultSelectedIndex);
        }
    }

}
