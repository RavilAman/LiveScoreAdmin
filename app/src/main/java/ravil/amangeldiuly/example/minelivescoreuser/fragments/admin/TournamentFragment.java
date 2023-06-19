package ravil.amangeldiuly.example.minelivescoreuser.fragments.admin;

import static ravil.amangeldiuly.example.minelivescoreuser.UrlConstants.BACKEND_URL;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import ravil.amangeldiuly.example.minelivescoreuser.R;
import ravil.amangeldiuly.example.minelivescoreuser.fragments.ScoresFragment;
import ravil.amangeldiuly.example.minelivescoreuser.fragments.tournament.CreateTournamentDialogFragment;
import ravil.amangeldiuly.example.minelivescoreuser.slider.SliderAdapter;
import ravil.amangeldiuly.example.minelivescoreuser.tournaments.TournamentListAdapter;
import ravil.amangeldiuly.example.minelivescoreuser.utils.LocalDateTimeDeserializer;
import ravil.amangeldiuly.example.minelivescoreuser.web.apis.TournamentApi;
import ravil.amangeldiuly.example.minelivescoreuser.web.responses.TournamentDto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TournamentFragment extends Fragment implements TournamentListAdapter.OnItemListener {
    private View currentView;
    private Retrofit retrofit;
    private RecyclerView tournamentsRecyclerView;
    private List<TournamentDto> tournamentList;
    private TournamentApi tournamentApi;
    private TextView noTournaments;
    private ImageButton imageButton;
    private FragmentManager fragmentManager;
    private MenuItem selectedDrawerMenuItem;
    private MenuItem selectedBottomMenuItem;
    private BottomNavigationView bottomNavigationView;
    private Button addButton;

    public TournamentFragment(FragmentManager fragmentManager, MenuItem menuItem, MenuItem selectedBottomMenuItem, BottomNavigationView bottomNavigationView) {
        this.selectedDrawerMenuItem = menuItem;
        this.fragmentManager = fragmentManager;
        this.selectedBottomMenuItem = selectedBottomMenuItem;
        this.bottomNavigationView = bottomNavigationView;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.fragment_tournament_list_, container, false);

        tournamentList = new ArrayList<>();

        tournamentsRecyclerView = currentView.findViewById(R.id.list_tournaments);
        imageButton = currentView.findViewById(R.id.game_back_button);
        addButton = currentView.findViewById(R.id.add_button);

        addButton.setOnClickListener(this::showCreateTournamentDialog);

        fragmentManager = requireActivity().getSupportFragmentManager();

        imageButton.setOnClickListener(backButtonListener());
        initializeRetrofit();
        findAllTournamentsByUser();


        return currentView;
    }

    private void showCreateTournamentDialog(View anchorView) {
        View popupView = LayoutInflater.from(getContext()).inflate(R.layout.create_tournament_pop_up, null);

        // Create the popup window
        PopupWindow popupWindow = new PopupWindow(popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);

        ViewPager sliderViewPager = popupView.findViewById(R.id.viewPager);
        List<Integer> imageList = new ArrayList<>();
        imageList.add(R.drawable.cup);
        imageList.add(R.drawable.league);
        SliderAdapter sliderAdapter = new SliderAdapter(getContext(), imageList);
        sliderViewPager.setAdapter(sliderAdapter);

        // Set onClickListener for previous button
        ImageButton btnPrevious = popupView.findViewById(R.id.btnPrevious);
        btnPrevious.setOnClickListener(v -> {
            int currentPosition = sliderViewPager.getCurrentItem();
            if (currentPosition > 0) {
                sliderViewPager.setCurrentItem(currentPosition - 1);
            } else {
                Toast.makeText(getContext(), "Already at the first image", Toast.LENGTH_SHORT).show();
            }
        });

        // Set onClickListener for next button
        ImageButton btnNext = popupView.findViewById(R.id.btnNext);
        btnNext.setOnClickListener(v -> {
            int currentPosition = sliderViewPager.getCurrentItem();
            int totalItems = sliderAdapter.getCount();
            if (currentPosition < totalItems - 1) {
                sliderViewPager.setCurrentItem(currentPosition + 1);
            } else {
                Toast.makeText(getContext(), "Already at the last image", Toast.LENGTH_SHORT).show();
            }
        });

        // Show the popup window at the center of the anchor view
        popupWindow.showAtLocation(anchorView, Gravity.CENTER, 0, 0);
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
            ScoresFragment scoreFragment = new ScoresFragment(fragmentManager);
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
        tournamentApi.findAllByUser().enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<TournamentDto>> call, Response<List<TournamentDto>> response) {
                if (response.body() != null) {
                    tournamentList = response.body();
                    createTournamentCards(tournamentList);
//                    if (tournamentList.isEmpty()) {
//                        noTournaments.setText(R.string.no_tournaments_with_name);
//                    }
                }
            }

            @Override
            public void onFailure(Call<List<TournamentDto>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void createTournamentCards(List<TournamentDto> tournaments) {
        TournamentListAdapter tournamentAdapter = new TournamentListAdapter(getLayoutInflater().getContext(), tournaments, this);
        tournamentsRecyclerView.setLayoutManager(new LinearLayoutManager(getLayoutInflater().getContext()));
        tournamentsRecyclerView.setAdapter(tournamentAdapter);
    }


    @Override
    public void onItemClick(TournamentDto tournament) {
        if (tournament.getTournamentType().equals("CUP")){
            TournamentCupInfoFragment tournamentCupInfoFragment = new TournamentCupInfoFragment(fragmentManager,tournament);

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, tournamentCupInfoFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }else {
            TournamentLeagueInfoFragment tournamentLeagueInfoFragment = new TournamentLeagueInfoFragment(fragmentManager,tournament);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, tournamentLeagueInfoFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }
}
