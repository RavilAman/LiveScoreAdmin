package ravil.amangeldiuly.example.minelivescoreadmin.fragments.admin;

import static ravil.amangeldiuly.example.minelivescoreadmin.SharedPreferencesConstants.FCM_TOKEN;
import static ravil.amangeldiuly.example.minelivescoreadmin.utils.SharedPreferencesUtil.getValue;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import ravil.amangeldiuly.example.minelivescoreadmin.R;
import ravil.amangeldiuly.example.minelivescoreadmin.SharedPreferencesConstants;
import ravil.amangeldiuly.example.minelivescoreadmin.fragments.ScoresFragment;
import ravil.amangeldiuly.example.minelivescoreadmin.fragments.tournament.FragmentSliderAdapter;
import ravil.amangeldiuly.example.minelivescoreadmin.slider.SliderAdapter;
import ravil.amangeldiuly.example.minelivescoreadmin.tournaments.TournamentListAdapter;
import ravil.amangeldiuly.example.minelivescoreadmin.utils.SharedPreferencesUtil;
import ravil.amangeldiuly.example.minelivescoreadmin.web.RequestHandler;
import ravil.amangeldiuly.example.minelivescoreadmin.web.apis.NotificationApi;
import ravil.amangeldiuly.example.minelivescoreadmin.web.apis.TournamentApi;
import ravil.amangeldiuly.example.minelivescoreadmin.web.requests.CreateTopicDTO;
import ravil.amangeldiuly.example.minelivescoreadmin.web.requests.CustomNotificationDto;
import ravil.amangeldiuly.example.minelivescoreadmin.web.responses.SaveCupTournamentDTO;
import ravil.amangeldiuly.example.minelivescoreadmin.web.responses.SaveTournamentDTO;
import ravil.amangeldiuly.example.minelivescoreadmin.web.requests.TournamentDto;
import ravil.amangeldiuly.example.minelivescoreadmin.web.tournament_type.TournamentType;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TournamentFragment<T extends SaveTournamentDTO> extends Fragment implements TournamentListAdapter.OnItemListener {

    private View currentView;
    private Retrofit retrofit;
    private RecyclerView tournamentsRecyclerView;
    private List<TournamentDto> tournamentList;
    private TournamentApi tournamentApi;
    private NotificationApi notificationApi;
    private TextView noTournaments;
    private ImageButton imageButton;
    private FragmentManager fragmentManager;
    private MenuItem selectedDrawerMenuItem;
    private MenuItem selectedBottomMenuItem;
    private BottomNavigationView bottomNavigationView;
    private Button addButton;
    private ImageView tab1, tab2, tab3;
    private TextView teamNumberTextView;
    private String selectedTournamentType = "Cup (Group Stage)";
    private Integer teamNum;
    private String tournamentName;
    private String tournamentLogoLink;
    private String tournamentLocation;

    private int count = 4;

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
        fragmentManager = requireActivity().getSupportFragmentManager();

        addButton.setOnClickListener(this::showPopUp);
        imageButton.setOnClickListener(backButtonListener());
        initializeRetrofit();
        findAllTournamentsByUser();


        return currentView;
    }

    private void showPopUp(View v) {
        PopupWindow popupWindow = new PopupWindow(getContext());
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        Drawable background = getResources().getDrawable(R.drawable.create_game_background); // Replace with your desired background drawable
        popupWindow.setBackgroundDrawable(background);


        View popupView = LayoutInflater.from(getContext()).inflate(R.layout.popup_layout, null);
        popupWindow.setContentView(popupView);


        tab1 = popupView.findViewById(R.id.tab1);
        tab2 = popupView.findViewById(R.id.tab2);
        tab3 = popupView.findViewById(R.id.tab3);

        tab1.setImageResource(R.drawable.circle_filled);
        tab2.setImageResource(R.drawable.circle_empty);
        tab3.setImageResource(R.drawable.circle_empty);


        List<RelativeLayout> relativeLayoutList = new ArrayList<>();
        RelativeLayout page1 = (RelativeLayout) LayoutInflater.from(getContext()).inflate(R.layout.fragment1_layout, null);
        EditText tournamentNameEdt = page1.findViewById(R.id.tournament_name_edittext);
        EditText tournamentLogoEdt = page1.findViewById(R.id.logo_link_edittext);
        EditText tournamentLocationEdt = page1.findViewById(R.id.tournament_location_edittext);

        relativeLayoutList.add(page1);

        RelativeLayout page2 = (RelativeLayout) LayoutInflater.from(getContext()).inflate(R.layout.fragment2_layout, null);
        ViewPager viewById = page2.findViewById(R.id.viewPager);
        page2Content(viewById, page2.findViewById(R.id.btnPrevious), page2.findViewById(R.id.btnNext));
        relativeLayoutList.add(page2);

        RelativeLayout page3 = (RelativeLayout) LayoutInflater.from(getContext()).inflate(R.layout.fragment3_layout, null);
        page3content(page3);
        Button create = page3.findViewById(R.id.create_button);

        create.setOnClickListener(view -> createTournament(tournamentNameEdt, tournamentLogoEdt, tournamentLocationEdt, popupWindow));


        popupWindow.setOnDismissListener(() -> {
            count = 4;
            updateTeamNumber(teamNumberTextView);
            viewById.setCurrentItem(0);
        });


        relativeLayoutList.add(page3);


        ViewPager viewPager = popupView.findViewById(R.id.viewPager);
        FragmentSliderAdapter pagerAdapter = new FragmentSliderAdapter(relativeLayoutList);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(0);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Not needed for tab highlighting
            }

            @Override
            public void onPageSelected(int position) {
                updateTabHighlight(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // Not needed for tab highlighting
            }
        });

        tab1.setOnClickListener(a -> viewPager.setCurrentItem(0));
        tab2.setOnClickListener(a -> viewPager.setCurrentItem(1));
        tab3.setOnClickListener(a -> viewPager.setCurrentItem(2));

        popupWindow.setAnimationStyle(R.style.PopupAnimation);
        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
    }

    private void createTournament(EditText tournamentNameEdt, EditText tournamentLogoEdt, EditText tournamentLocationEdt, PopupWindow popupWindow) {
        tournamentName = tournamentNameEdt.getText().toString();
        tournamentLocation = tournamentLocationEdt.getText().toString();
        tournamentLogoLink = tournamentLogoEdt.getText().toString();
        teamNum = count;

        if (tournamentName.length() == 0 || tournamentLocation.length() == 0 || tournamentLogoLink.length() == 0) {
            Toast.makeText(getContext(), "Enter Tournament name, logo, and location", Toast.LENGTH_SHORT).show();
        } else {
            if (selectedTournamentType.equals("Cup (Group Stage)")) {
                SaveCupTournamentDTO saveTournamentDTO = new SaveCupTournamentDTO(tournamentName, "CUP", tournamentLogoLink, tournamentLocation, teamNum, "FALSE");
                Log.i("", saveTournamentDTO.toString());
                tournamentApi.createTournamentCup(saveTournamentDTO).enqueue(createTournamentCallBack(popupWindow));
            } else if (selectedTournamentType.equals("Cup (Play Off)")) {
                SaveCupTournamentDTO saveTournamentDTO = new SaveCupTournamentDTO(tournamentName, "CUP", tournamentLogoLink, tournamentLocation, teamNum, "TRUE");
                Log.i("", saveTournamentDTO.toString());
                tournamentApi.createTournamentCup(saveTournamentDTO).enqueue(createTournamentCallBack(popupWindow));
            } else {
                SaveTournamentDTO saveTournamentDTO = new SaveTournamentDTO(tournamentName, "LEAGUE", tournamentLogoLink, tournamentLocation, teamNum);
                Log.i("", saveTournamentDTO.toString());
                tournamentApi.createTournamentLeague(saveTournamentDTO).enqueue(createTournamentCallBack(popupWindow));
            }

        }
    }

    private Callback<TournamentDto> createTournamentCallBack(PopupWindow popupWindow) {
        return new Callback<>() {
            @Override
            public void onResponse(Call<TournamentDto> call, Response<TournamentDto> response) {
                assert response.body() != null;
                if (response.isSuccessful()) {
                    TournamentDto tournamentDto = response.body();
                    CreateTopicDTO createTopicDTO = new CreateTopicDTO();
                    StringBuilder topicName = new StringBuilder();
                    topicName.append(
                            tournamentDto.getTournamentName()
                                    .toLowerCase()
                                    .replace(" ", "_")
                    );
                    topicName.append("_");
                    topicName.append(tournamentDto.getTournamentLocation());
                    topicName.append("_");
                    topicName.append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd")));
                    createTopicDTO.setTopicName(topicName.toString());
                    createTopicDTO.setTournamentId(tournamentDto.getTournamentId());
                    createTopicDTO.setRegistrationToken(getValue(requireContext(), FCM_TOKEN));
                    createTopic(createTopicDTO);
                    Toast.makeText(getContext(), "Tournament added", Toast.LENGTH_SHORT).show();
                    findAllTournamentsByUser();
                } else {
                    Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
                }

                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
            }

            @Override
            public void onFailure(Call<TournamentDto> call, Throwable t) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
                Toast.makeText(getContext(), "Vse Ploho", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void createTopic(CreateTopicDTO createTopicDTO) {
        notificationApi.createTopic(createTopicDTO).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void page3content(RelativeLayout page3) {
        ImageButton previous = page3.findViewById(R.id.btnPrevious);
        ImageButton next = page3.findViewById(R.id.btnNext);
        teamNumberTextView = page3.findViewById(R.id.team_count);

        previous.setOnClickListener(v12 -> decrementTeamNumber(teamNumberTextView, (selectedTournamentType.equals("Cup (Group Stage)") || selectedTournamentType.equals("Cup (Play Off)"))));
        next.setOnClickListener(v1 -> incrementTeamNumber(teamNumberTextView, (selectedTournamentType.equals("Cup (Group Stage)") || selectedTournamentType.equals("Cup (Play Off)"))));
    }

    private void decrementTeamNumber(TextView teamNumberTextView, boolean isCup) {
        if (count > 1 && !isCup) {
            count--;
            updateTeamNumber(teamNumberTextView);
        } else if (count > 1) {
            count = count >> 1;
            updateTeamNumber(teamNumberTextView);
        }
    }

    private void incrementTeamNumber(TextView teamNumberTextView, boolean isCup) {
        if (isCup) {
            count = count << 1;
        } else {
            count++;
        }
        updateTeamNumber(teamNumberTextView);
    }

    private void updateTeamNumber(TextView teamNumberTextView) {
        if (teamNumberTextView != null) {
            teamNumberTextView.setText(String.valueOf(count));
        }
    }

    private void page2Content(ViewPager page2, ImageButton page21, ImageButton page22) {
        List<TournamentType> tournamentTypes = new ArrayList<>();
        tournamentTypes.add(new TournamentType(R.drawable.cup, "Cup (Group Stage)"));
        tournamentTypes.add(new TournamentType(R.drawable.league, "League"));
        tournamentTypes.add(new TournamentType(R.drawable.cup_play_off, "Cup (Play Off)"));

        page2.setCurrentItem(0);
        page2.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Not needed for tab highlighting
            }

            @Override
            public void onPageSelected(int position) {
                updateTournamentType(position, tournamentTypes.get(position).getTournamentType());
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // Not needed for tab highlighting
            }
        });

        SliderAdapter sliderAdapter = new SliderAdapter(getContext(), tournamentTypes);
        page2.setAdapter(sliderAdapter);

        page21.setOnClickListener(a -> {
            int currentPosition = page2.getCurrentItem();
            if (currentPosition > 0) {
                page2.setCurrentItem(currentPosition - 1);
            }
        });

        page22.setOnClickListener(a -> {
            int currentPosition = page2.getCurrentItem();
            int totalItems = sliderAdapter.getCount();
            if (currentPosition < totalItems - 1) {
                page2.setCurrentItem(currentPosition + 1);
            }
        });
    }

    private void updateTournamentType(int possition, String tournamentType) {

        if (possition == 0) {
            selectedTournamentType = tournamentType;
        } else if (possition == 1) {
            selectedTournamentType = tournamentType;
        } else if (possition == 2) {
            selectedTournamentType = tournamentType;
        }
    }

    private void updateTabHighlight(int position) {
        tab1.setImageResource(position == 0 ? R.drawable.circle_filled : R.drawable.circle_empty);
        tab2.setImageResource(position == 1 ? R.drawable.circle_filled : R.drawable.circle_empty);
        tab3.setImageResource(position == 2 ? R.drawable.circle_filled : R.drawable.circle_empty);
        count = 4;
        updateTeamNumber(teamNumberTextView);
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
        RequestHandler requestHandler = new RequestHandler(getContext());
        retrofit = requestHandler.getRetrofit();
        tournamentApi = retrofit.create(TournamentApi.class);
        notificationApi = retrofit.create(NotificationApi.class);
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
        if (tournament.getTournamentType().equals("CUP")) {
            TournamentCupInfoFragment tournamentCupInfoFragment = new TournamentCupInfoFragment(fragmentManager, tournament);

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, tournamentCupInfoFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else {
            TournamentLeagueInfoFragment tournamentLeagueInfoFragment = new TournamentLeagueInfoFragment(fragmentManager, tournament);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, tournamentLeagueInfoFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }
}
