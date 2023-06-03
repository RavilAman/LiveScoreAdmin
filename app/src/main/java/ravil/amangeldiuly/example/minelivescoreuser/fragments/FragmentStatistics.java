package ravil.amangeldiuly.example.minelivescoreuser.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDateTime;
import java.util.List;

import ravil.amangeldiuly.example.minelivescoreuser.Constants;
import ravil.amangeldiuly.example.minelivescoreuser.R;
import ravil.amangeldiuly.example.minelivescoreuser.statistics.GroupsStatisticsAdapter;
import ravil.amangeldiuly.example.minelivescoreuser.utils.LocalDateTimeDeserializer;
import ravil.amangeldiuly.example.minelivescoreuser.web.apis.GroupApi;
import ravil.amangeldiuly.example.minelivescoreuser.web.responses.GroupInfoListDTO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FragmentStatistics extends Fragment {

    private Context context;
    private FragmentManager fragmentManager;

    private Retrofit retrofit;
    private GroupApi groupApi;

    private View currentView;
    private ImageView tournamentLogo;
    private TextView tournamentName;
    private TextView groupName;
    private TextView matches;
    private TextView table;
    private TextView playerStatistics;
    private TextView teamStatistics;
    private ImageButton backButton;
    private RecyclerView groupStatisticsRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.fragment_statistics, container, false);
        initializeRetrofit();
        initializeViews();
        setOnClickListeners();
        Bundle data = getArguments();
        assert data != null;
        getAndSetStatistics(
                (int) data.getLong("tournamentId"),
                (int) data.getLong("groupId")
        );
        table.setTextColor(Color.parseColor("#FFF76D09"));
        return currentView;
    }

    private void initializeRetrofit() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer())
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BACKEND_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        groupApi = retrofit.create(GroupApi.class);
    }

    private void initializeViews() {
        context = getContext();
        fragmentManager = requireActivity().getSupportFragmentManager();
        tournamentLogo = currentView.findViewById(R.id.fragment_statistics_tournament_logo);
        tournamentName = currentView.findViewById(R.id.fragment_statistics_tournament_name);
        groupName = currentView.findViewById(R.id.fragment_statistics_group_name);
        groupStatisticsRecyclerView = currentView.findViewById(R.id.fragment_statistics_group_statistics_recycler_vew);
        matches = currentView.findViewById(R.id.fragment_statistics_matches);
        table = currentView.findViewById(R.id.fragment_statistics_table);
        playerStatistics = currentView.findViewById(R.id.fragment_statistics_player_statistics);
        teamStatistics = currentView.findViewById(R.id.fragment_statistics_team_statistics);
        backButton = currentView.findViewById(R.id.fragment_statistics_back_button);
    }

    private void setStatistics(List<GroupInfoListDTO> statistics) {
        GroupsStatisticsAdapter groupsStatisticsAdapter = new GroupsStatisticsAdapter(context, statistics);
        groupStatisticsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        groupStatisticsRecyclerView.setAdapter(groupsStatisticsAdapter);
    }

    private void getAndSetStatistics(int tournamentId, int groupId) {
        groupApi.getGroupStatistics(groupId, tournamentId).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<GroupInfoListDTO>> call, Response<List<GroupInfoListDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    GroupInfoListDTO groupInfoListDTO = response.body().get(0);
                    setStatistics(response.body());
                    setTournamentData(
                            groupInfoListDTO.getTournamentLogo(),
                            groupInfoListDTO.getTournamentName(),
                            groupInfoListDTO.getGroupName()
                    );
                }
            }

            @Override
            public void onFailure(Call<List<GroupInfoListDTO>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void setTournamentData(String tournamentLogo, String tournamentName, String groupName) {
        Glide.with(context)
                .load(tournamentLogo)
                .into(this.tournamentLogo);
        this.tournamentName.setText(tournamentName);
        this.groupName.setText(groupName);
    }

    private void setOnClickListeners() {
        backButton.setOnClickListener(backButtonListener());
    }

    private View.OnClickListener backButtonListener() {
        return view -> {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(
                    R.anim.fragment_enter,
                    R.anim.fragment_exit,
                    R.anim.fragment_previous_enter,
                    R.anim.fragment_previous_exit
            );
            fragmentManager.popBackStack();
            fragmentTransaction.commit();
        };
    }
}