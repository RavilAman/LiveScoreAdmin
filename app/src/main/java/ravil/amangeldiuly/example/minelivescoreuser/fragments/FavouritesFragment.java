package ravil.amangeldiuly.example.minelivescoreuser.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import ravil.amangeldiuly.example.minelivescoreuser.Constants;
import ravil.amangeldiuly.example.minelivescoreuser.R;
import ravil.amangeldiuly.example.minelivescoreuser.db.SQLiteManager;
import ravil.amangeldiuly.example.minelivescoreuser.tournaments.TournamentAdapter;
import ravil.amangeldiuly.example.minelivescoreuser.web.apis.NotificationApi;
import ravil.amangeldiuly.example.minelivescoreuser.web.apis.TournamentApi;
import ravil.amangeldiuly.example.minelivescoreuser.web.responses.TournamentDto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FavouritesFragment extends Fragment implements TournamentAdapter.OnItemListener {

    private View currentView;
    private Retrofit retrofit;
    private RecyclerView tournamentsRecyclerView;
    private List<TournamentDto> favouriteTournaments = new ArrayList<>();
    private TournamentApi tournamentApi;
    private NotificationApi notificationApi;


    private SQLiteManager sqLiteManager;

    // todo: add check for internet availability

    // todo: можно ли добавить анимацию, как все турнаменты сохраненные в бд плано по одному прорисовываются на фронте

    // todo: добавть поиск

    // обновить ресайклер вью после удаления, тоесть удалить с листа, и нотифицировать


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.fragment_favourites, container, false);
        favouriteTournaments.clear();
        tournamentsRecyclerView = currentView.findViewById(R.id.tournament_cards_holder);
        sqLiteManager = SQLiteManager.getInstance(getLayoutInflater().getContext());

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BACKEND_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        tournamentApi = retrofit.create(TournamentApi.class);
        notificationApi = retrofit.create(NotificationApi.class);

        getTournamentData();

        return currentView;
    }

    private void getTournamentData() {
        favouriteTournaments = sqLiteManager.getAllTournaments();
        if (!favouriteTournaments.isEmpty()) {
            createTournamentCards(favouriteTournaments);
        } else {
            createTournamentCards(List.of(
                    new TournamentDto(0L, "Currently, there are no tournaments", "https://cdn-icons-png.flaticon.com/512/1622/1622553.png", "", ""))
            );
        }
    }

    private void createTournamentCards(List<TournamentDto> tournaments) {
        TournamentAdapter tournamentAdapter = new TournamentAdapter(getLayoutInflater().getContext(), tournaments, this);
        tournamentsRecyclerView.setLayoutManager(new LinearLayoutManager(getLayoutInflater().getContext()));
        tournamentsRecyclerView.setAdapter(tournamentAdapter);
    }

    @Override
    public void onItemClick(long tournamentId) {
    }
}
