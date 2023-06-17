package ravil.amangeldiuly.example.minelivescoreuser.fragments.admin;

import static ravil.amangeldiuly.example.minelivescoreuser.UrlConstants.BACKEND_URL;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ravil.amangeldiuly.example.minelivescoreuser.R;
import ravil.amangeldiuly.example.minelivescoreuser.tournaments.TournamentGroupStageFragment;
import ravil.amangeldiuly.example.minelivescoreuser.tournaments.TournamentInfoAdapter;
import ravil.amangeldiuly.example.minelivescoreuser.tournaments.TournamentPlayOfTabFragment;
import ravil.amangeldiuly.example.minelivescoreuser.utils.LocalDateTimeDeserializer;
import ravil.amangeldiuly.example.minelivescoreuser.web.apis.GroupApi;
import ravil.amangeldiuly.example.minelivescoreuser.web.responses.GroupDTO;
import ravil.amangeldiuly.example.minelivescoreuser.web.responses.TournamentDto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TournamentCupInfoFragment extends Fragment implements TabLayout.OnTabSelectedListener,TournamentGroupStageFragment.OnFinishButtonClickListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<GroupDTO> tabList;
    private FragmentManager fragmentManager;
    private ImageButton imageButton;
    private ShapeableImageView tournamentLogo;
    private TextView tournamentName;
    private TextView tournamentGroup;
    private TournamentDto tournamentDto;
    private Retrofit retrofit;
    private GroupApi groupApi;

    public TournamentCupInfoFragment(FragmentManager fragmentManager, TournamentDto tournamentDto) {
        this.fragmentManager = fragmentManager;
        this.tournamentDto = tournamentDto;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View currentView = inflater.inflate(R.layout.fragment_tournament_manipulation, container, false);

        tabList = new ArrayList<>();
        tabLayout = currentView.findViewById(R.id.tab_layout);
        viewPager = currentView.findViewById(R.id.view_pager);
        tournamentGroup = currentView.findViewById(R.id.fragment_tournament_group);
        imageButton = currentView.findViewById(R.id.fragment_back_button);
        imageButton.setOnClickListener(backButtonListener());
        tournamentName = currentView.findViewById(R.id.fragment_tournament_name);
        tournamentLogo = currentView.findViewById(R.id.fragment_tournament_logo);

        initializeRetrofit();
        tabLayout.addOnTabSelectedListener(this);


        tournamentName.setText(tournamentDto.getTournamentName());
        tournamentGroup.setText(tournamentDto.getTournamentLocation());
        Glide.with(this)
                .load(tournamentDto.getTournamentLogo())
                .into(tournamentLogo);

        fetchGroupTabs();


        return currentView;
    }

    private void fetchGroupTabs() {

        Callback<List<GroupDTO>> callback = new Callback<>() {
            @Override
            public void onResponse(Call<List<GroupDTO>> call, Response<List<GroupDTO>> response) {
                if (response.body() != null) {
                    tabList = response.body();
                    if (tabList.size() > 3) {
                        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
                    } else {
                        tabLayout.setTabMode(TabLayout.MODE_FIXED);
                    }
                    setupViewPager(tabList);
                }
            }

            @Override
            public void onFailure(Call<List<GroupDTO>> call, Throwable t) {
                t.printStackTrace();
            }
        };
        Call<List<GroupDTO>> call = groupApi.getGroupTabs(tournamentDto.getTournamentId());
        call.enqueue(callback);
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
        groupApi = retrofit.create(GroupApi.class);
    }

    private View.OnClickListener backButtonListener() {
        return view -> {

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentManager.popBackStack();
            fragmentTransaction.commit();
        };
    }

    private void setupViewPager(List<GroupDTO> tabList) {
        TournamentInfoAdapter adapter = new TournamentInfoAdapter(getChildFragmentManager());

        int currentStage = 0;

        for (int i = 0; i < tabList.size(); i++) {
            GroupDTO group = tabList.get(i);
            if (group.isCurrentStage()) {
                currentStage = i;
            }
            if (group.getGroupId() == null){
                adapter.addFragment(new TournamentGroupStageFragment(group, tournamentDto,this),group);
            }else {
                adapter.addFragment(new TournamentPlayOfTabFragment(group,tournamentDto,this), group);
            }

        }

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        Objects.requireNonNull(tabLayout.getTabAt(currentStage)).select();
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int position = tab.getPosition();
        GroupDTO clickedGroup = tabList.get(position);
        Log.i("",clickedGroup.toString());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onFinishButtonClicked() {
        int currentTabPosition = viewPager.getCurrentItem();

        if (currentTabPosition < tabList.size() - 1) {
            viewPager.setCurrentItem(currentTabPosition + 1);
        }
    }

}
