package ravil.amangeldiuly.example.minelivescoreuser.intro;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.MainCoroutineDispatcher;
import ravil.amangeldiuly.example.minelivescoreuser.Constants;
import ravil.amangeldiuly.example.minelivescoreuser.MainActivity;
import ravil.amangeldiuly.example.minelivescoreuser.R;
import ravil.amangeldiuly.example.minelivescoreuser.db.SQLiteManager;
import ravil.amangeldiuly.example.minelivescoreuser.tournaments.TournamentAdapter;
import ravil.amangeldiuly.example.minelivescoreuser.utils.SharedPreferencesUtil;
import ravil.amangeldiuly.example.minelivescoreuser.web.apis.NotificationApi;
import ravil.amangeldiuly.example.minelivescoreuser.web.apis.TournamentApi;
import ravil.amangeldiuly.example.minelivescoreuser.web.responses.TournamentDto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class IntroActivity extends AppCompatActivity implements TournamentAdapter.OnItemListener {

    private ViewPager viewPager;
    private IntroViewPagerAdapter introViewPagerAdapter;
    private TabLayout tabLayout;
    private RecyclerView tournamentsRecyclerView;
    private List<TournamentDto> tournaments = new ArrayList<>();
    private Button startButton;
    private TournamentApi tournamentApi;
    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        // todo: раскоментировать когда закончу
        checkIfIntroduced();

        tabLayout = findViewById(R.id.intro_tab_indicator);
        viewPager = findViewById(R.id.intro_view_pager);
        introViewPagerAdapter = new IntroViewPagerAdapter(this);
        viewPager.setAdapter(introViewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(tabSelectedListener());

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BACKEND_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        tournamentApi = retrofit.create(TournamentApi.class);
    }

    private TabLayout.OnTabSelectedListener tabSelectedListener() {
        return new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 1:
                        tournamentsRecyclerView = findViewById(R.id.intro_tournament_cards_holder);
                        getTournamentData();
                        break;
                    case 2:
                        startButton = findViewById(R.id.intro_start_button);
                        startButton.setOnClickListener(startButtonOnClickListener());
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        };
    }

    private void getTournamentData() {
        tournamentApi.findAllTournaments().enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<TournamentDto>> call, Response<List<TournamentDto>> response) {
                if (response.body() != null) {
                    tournaments.clear();
                    tournaments.addAll(response.body());
                    createTournamentCards(tournaments);
                } else {
                    createTournamentCards(List.of(
                            new TournamentDto(0L, "Currently, there are no tournaments", "", "", ""))
                    );
                }
            }

            @Override
            public void onFailure(Call<List<TournamentDto>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void createTournamentCards(List<TournamentDto> tournaments) {
        TournamentAdapter tournamentAdapter = new TournamentAdapter(this, tournaments, this);
        tournamentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tournamentsRecyclerView.setAdapter(tournamentAdapter);
    }

    @Override
    public void onItemClick(long tournamentId) {
    }

    private View.OnClickListener startButtonOnClickListener() {
        return view -> {
            SharedPreferencesUtil.putValue(this, "introduced", "true");
            changeToMainActivity();
        };
    }

    private void changeToMainActivity() {
        Intent mainActivityIntent = new Intent(this, MainActivity.class);
        startActivity(mainActivityIntent);
        finish();
    }

    private void checkIfIntroduced() {
        if (SharedPreferencesUtil.getValue(this, "introduced").equals("true")) {
            changeToMainActivity();
        }
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        sqLiteManager.close();
//    }
}