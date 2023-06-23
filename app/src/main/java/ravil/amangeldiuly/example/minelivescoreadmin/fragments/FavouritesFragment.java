package ravil.amangeldiuly.example.minelivescoreadmin.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import ravil.amangeldiuly.example.minelivescoreadmin.UrlConstants;
import ravil.amangeldiuly.example.minelivescoreadmin.R;
import ravil.amangeldiuly.example.minelivescoreadmin.db.SQLiteManager;
import ravil.amangeldiuly.example.minelivescoreadmin.tournaments.TournamentAdapter;
import ravil.amangeldiuly.example.minelivescoreadmin.web.apis.TournamentApi;
import ravil.amangeldiuly.example.minelivescoreadmin.web.responses.TournamentDto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FavouritesFragment extends Fragment {

    private View currentView;
    private Retrofit retrofit;
    private RecyclerView tournamentsRecyclerView;
    private List<TournamentDto> favouriteTournaments;
    private TournamentApi tournamentApi;
    private TextView noTournaments;
    private SQLiteManager sqLiteManager;
    private SearchView searchView;
    private List<TournamentDto> tournamentByNames;

    // todo: можно ли добавить анимацию, как все турнаменты сохраненные в бд плано по одному прорисовываются на фронте

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.fragment_favourites, container, false);

        favouriteTournaments = new ArrayList<>();
        tournamentByNames = new ArrayList<>();

        tournamentsRecyclerView = currentView.findViewById(R.id.tournament_cards_holder);
        noTournaments = currentView.findViewById(R.id.favourites_no_tournaments_label);
        searchView = currentView.findViewById(R.id.favourites_search_view);

        sqLiteManager = SQLiteManager.getInstance(getLayoutInflater().getContext());

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(UrlConstants.BACKEND_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        tournamentApi = retrofit.create(TournamentApi.class);

        getTournamentData();

        searchView.setOnQueryTextListener(introQueryTextListener());
        return currentView;
    }

    private void getTournamentData() {
        favouriteTournaments.clear();
        noTournaments.setText("");
        favouriteTournaments = sqLiteManager.getAllTournaments();
        if (!favouriteTournaments.isEmpty()) {
            createTournamentCards(favouriteTournaments);
        } else {
            noTournaments.setText(R.string.no_tournaments);
        }
    }

    private void createTournamentCards(List<TournamentDto> tournaments) {
        TournamentAdapter tournamentAdapter = new TournamentAdapter(getLayoutInflater().getContext(), tournaments);
        tournamentsRecyclerView.setLayoutManager(new LinearLayoutManager(getLayoutInflater().getContext()));
        tournamentsRecyclerView.setAdapter(tournamentAdapter);
    }

    private SearchView.OnQueryTextListener introQueryTextListener() {
        return new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.isEmpty()) {
                    getTournamentData();
                } else {
                    noTournaments.setText("");
                    searchResultTournaments(s);
                }
                return true;
            }
        };
    }

    private void searchResultTournaments(String name) {
        tournamentByNames.clear();
        tournamentApi.findTournamentsByName(name).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<TournamentDto>> call, Response<List<TournamentDto>> response) {
                if (response.body() != null) {
                    tournamentByNames = response.body();
                    createTournamentCards(tournamentByNames);
                    if (tournamentByNames.isEmpty()) {
                        noTournaments.setText(R.string.no_tournaments_with_name);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<TournamentDto>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
