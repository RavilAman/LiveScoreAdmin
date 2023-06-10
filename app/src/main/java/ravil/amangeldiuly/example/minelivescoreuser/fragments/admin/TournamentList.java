package ravil.amangeldiuly.example.minelivescoreuser.fragments.admin;

import static ravil.amangeldiuly.example.minelivescoreuser.UrlConstants.BACKEND_URL;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import ravil.amangeldiuly.example.minelivescoreuser.R;
import ravil.amangeldiuly.example.minelivescoreuser.fragments.ScoresFragment;
import ravil.amangeldiuly.example.minelivescoreuser.tournaments.TournamentListAdapter;
import ravil.amangeldiuly.example.minelivescoreuser.utils.LocalDateTimeDeserializer;
import ravil.amangeldiuly.example.minelivescoreuser.web.apis.TournamentApi;
import ravil.amangeldiuly.example.minelivescoreuser.web.responses.TournamentDto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TournamentList extends Fragment implements TournamentListAdapter.OnItemListener {

    private View currentView;
    private Retrofit retrofit;
    private RecyclerView tournamentsRecyclerView;
    private List<TournamentDto> tournamentList;
    private TournamentApi tournamentApi;
    private TextView headerText;
    private ImageButton imageButton;
    private FragmentManager fragmentManager;
    private MenuItem selectedDrawerMenuItem;
    private MenuItem selectedBottomMenuItem;
    private BottomNavigationView bottomNavigationView;
    private int pageHeaderText;

    public TournamentList(FragmentManager fragmentManager, MenuItem menuItem, MenuItem selectedBottomMenuItem, BottomNavigationView bottomNavigationView, int pageHeaderText) {
        this.selectedDrawerMenuItem = menuItem;
        this.fragmentManager = fragmentManager;
        this.selectedBottomMenuItem = selectedBottomMenuItem;
        this.bottomNavigationView = bottomNavigationView;
        this.pageHeaderText = pageHeaderText;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.tournaments_list, container, false);

        tournamentList = new ArrayList<>();

        tournamentsRecyclerView = currentView.findViewById(R.id.list_tournaments);
        imageButton = currentView.findViewById(R.id.game_back_button);
        headerText = currentView.findViewById(R.id.page_header_text);
        headerText.setText(pageHeaderText);

        fragmentManager = requireActivity().getSupportFragmentManager();

        imageButton.setOnClickListener(backButtonListener());
        initializeRetrofit();
        findAllTournamentsByUser();


        return currentView;
    }

    private View.OnClickListener backButtonListener() {
        return view -> {
            if (selectedDrawerMenuItem != null) {
                selectedDrawerMenuItem.setChecked(false);
                selectedDrawerMenuItem = null;
            }
            selectedBottomMenuItem.setChecked(false);

            bottomNavigationView.getMenu().findItem(R.id.nav_scores).setChecked(true);

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentManager.popBackStack();
            ScoresFragment scoreFragment = new ScoresFragment();
            fragmentTransaction.replace(R.id.fragment_container, scoreFragment);


            fragmentTransaction.commit();
        };
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
        tournamentApi = retrofit.create(TournamentApi.class);
    }


    private void findAllTournamentsByUser() {
        tournamentList.clear();

        Callback<List<TournamentDto>> callback = new Callback<>() {
            @Override
            public void onResponse(Call<List<TournamentDto>> call, Response<List<TournamentDto>> response) {
                if (response.body() != null) {
                    tournamentList = response.body();
                    createTournamentCards(tournamentList);
//                if (tournamentList.isEmpty()) {
//                    noTournaments.setText(R.string.no_tournaments_with_name);
//                }
                }
            }

            @Override
            public void onFailure(Call<List<TournamentDto>> call, Throwable t) {
                t.printStackTrace();
            }
        };

        if (pageHeaderText == R.string.create_in_draw) {
            tournamentApi.findAllCupByUser().enqueue(callback);
        } else {
            tournamentApi.findAllByUser().enqueue(callback);
        }
    }

    private void createTournamentCards(List<TournamentDto> tournaments) {
        TournamentListAdapter tournamentAdapter = new TournamentListAdapter(getLayoutInflater().getContext(), tournaments, this);
        tournamentsRecyclerView.setLayoutManager(new LinearLayoutManager(getLayoutInflater().getContext()));
        tournamentsRecyclerView.setAdapter(tournamentAdapter);
    }

    @Override
    public void onItemClick(TournamentDto tournament) {
        Fragment fragment = null;

        switch (pageHeaderText) {
            case R.string.upload_team:
                fragment = LoadTeamsFragment.newInstance(tournament, fragmentManager);
                break;
            case R.string.create_in_draw:
                fragment = new CreateInDrawFragment();
                break;
            case R.string.transfers:
                fragment = new TransfersFragment();
                break;
        }

        if (fragment != null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

}
