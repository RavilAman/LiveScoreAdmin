package ravil.amangeldiuly.example.minelivescoreadmin.players;

import static ravil.amangeldiuly.example.minelivescoreadmin.UrlConstants.BACKEND_URL;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.stream.Collectors;

import ravil.amangeldiuly.example.minelivescoreadmin.R;
import ravil.amangeldiuly.example.minelivescoreadmin.web.responses.PlayerDTO;
import ravil.amangeldiuly.example.minelivescoreadmin.web.responses.TeamDTO;
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

    public PlayersViewHolder(@NonNull View itemView, PlayerAdapter.OnItemListener onItemListener, Context context, List<TeamDTO> teamList) {
        super(itemView);
        this.playerName = itemView.findViewById(R.id.card_player_name);
        this.playerNumber = itemView.findViewById(R.id.card_player_number);
        this.spinner = itemView.findViewById(R.id.list_teams);
        this.onItemListener = onItemListener;
        this.context = context;
        this.teamList = teamList;

        List<String> itemList = teamList.stream().map(i -> {
            String name = i.getTeamName();
            if (name.contains(" ") && name.length() > 11) {
                String[] s = name.split(" ");
                String s1 = s[0] + " " + s[1].charAt(0)+".";
                return s1;
            } else if (!name.contains(" ") && name.length() >= 11) {
                return name.substring(0, 10) + "...";
            }

            return name;
        }).collect(Collectors.toList());

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

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TeamDTO selectedTeam = teamList.get(position);
                if (playerDTO.getTeamId() != selectedTeam.getTeamId()) {
                    playerDTO.setTeamId(selectedTeam.getTeamId());
                    onItemListener.onItemClick(playerDTO);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

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

        if (defaultSelectedIndex != -1) {
            spinner.setSelection(defaultSelectedIndex);
        }
    }

}
