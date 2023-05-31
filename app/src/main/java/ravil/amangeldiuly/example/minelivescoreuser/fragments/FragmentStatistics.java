package ravil.amangeldiuly.example.minelivescoreuser.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
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

    private Retrofit retrofit;
    private GroupApi groupApi;

    private View currentView;
    private ImageView tournamentLogo;
    private TextView tournamentName;
    private TextView groupName;
    private RecyclerView groupStatisticsRecyclerView;
    private Bundle data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.fragment_statistics, container, false);
        initializeRetrofit();
        initializeViews();
        data = getArguments();
        assert data != null;
        getStatistics(
                (int) data.getLong("tournamentId"),
                (int) data.getLong("groupId")
        );
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
        tournamentLogo = currentView.findViewById(R.id.fragment_statistics_tournament_logo);
        tournamentName = currentView.findViewById(R.id.fragment_statistics_tournament_name);
        groupName = currentView.findViewById(R.id.fragment_statistics_group_name);
        groupStatisticsRecyclerView = currentView.findViewById(R.id.fragment_statistics_group_statistics_recycler_vew);
    }

    private void setStatistics(List<GroupInfoListDTO> statistics) {
        GroupsStatisticsAdapter groupsStatisticsAdapter = new GroupsStatisticsAdapter(context, statistics);
        groupStatisticsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        groupStatisticsRecyclerView.setAdapter(groupsStatisticsAdapter);
    }

    private void getStatistics(int tournamentId, int groupId) {
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
}