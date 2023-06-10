package ravil.amangeldiuly.example.minelivescoreuser.fragments.admin;

import static ravil.amangeldiuly.example.minelivescoreuser.UrlConstants.BACKEND_URL;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import ravil.amangeldiuly.example.minelivescoreuser.R;
import ravil.amangeldiuly.example.minelivescoreuser.players.PlayerAdapter;
import ravil.amangeldiuly.example.minelivescoreuser.utils.LocalDateTimeDeserializer;
import ravil.amangeldiuly.example.minelivescoreuser.web.apis.PLayerApi;
import ravil.amangeldiuly.example.minelivescoreuser.web.responses.PlayerDTO;
import ravil.amangeldiuly.example.minelivescoreuser.web.responses.TeamDTO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PlayerListFragment extends Fragment implements PlayerAdapter.OnItemListener {


    private View currentView;
    private Retrofit retrofit;
    private RecyclerView playerRecyclerView;
    private List<PlayerDTO> playerList;
    private PLayerApi pLayerApi;
    private ImageButton imageButton;
    private FragmentManager fragmentManager;
    private ImageView teamLogo;
    private TextView teamName;
    private TeamDTO teamDTO;
    private List<TeamDTO>teamList;

    public PlayerListFragment(TeamDTO teamDTO, FragmentManager fragmentManager,List<TeamDTO> teamList) {
        this.teamDTO = teamDTO;
        this.fragmentManager = fragmentManager;
        this.teamList = teamList;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.fragment_players_list, container, false);
        playerList = new ArrayList<>();

        playerRecyclerView = currentView.findViewById(R.id.list_players);
        imageButton = currentView.findViewById(R.id.fragment_back_button);
        imageButton.setOnClickListener(backButtonListener());
        teamName = currentView.findViewById(R.id.fragment_team_name);
        teamLogo = currentView.findViewById(R.id.fragment_team_logo);

        initializeRetrofit();

        teamName.setText(teamDTO.getTeamName());
        Glide.with(this)
                .load(teamDTO.getTeamLogo())
                .into(teamLogo);

        findAllPlayerByTeam();

        return currentView;
    }

    private void findAllPlayerByTeam() {
        playerList.clear();


        Callback<List<PlayerDTO>> callback = new Callback<>() {
            @Override
            public void onResponse(Call<List<PlayerDTO>> call, Response<List<PlayerDTO>> response) {
                if (response.body() != null) {
                    playerList = response.body();
                    createTeamCards(playerList);
//                if (tournamentList.isEmpty()) {
//                    noTournaments.setText(R.string.no_tournaments_with_name);
//                }
                }
            }

            @Override
            public void onFailure(Call<List<PlayerDTO>> call, Throwable t) {
                t.printStackTrace();
            }
        };
        pLayerApi.findAllPlayerByTeamId(teamDTO.getTeamId()).enqueue(callback);

    }

    private void createTeamCards(List<PlayerDTO> playerList) {
        PlayerAdapter playerAdapter = new PlayerAdapter(getLayoutInflater().getContext(), playerList, this,teamList);
        playerRecyclerView.setLayoutManager(new LinearLayoutManager(getLayoutInflater().getContext()));
        playerRecyclerView.setAdapter(playerAdapter);
    }

    private void initializeRetrofit() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer())
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(BACKEND_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        pLayerApi = retrofit.create(PLayerApi.class);
    }

    private View.OnClickListener backButtonListener() {
        return view -> {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentManager.popBackStack();
            fragmentTransaction.commit();
        };
    }

    @Override
    public void onItemClick(PlayerDTO player) {

    }
}
