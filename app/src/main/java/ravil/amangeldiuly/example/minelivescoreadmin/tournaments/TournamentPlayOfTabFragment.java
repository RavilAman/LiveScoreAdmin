package ravil.amangeldiuly.example.minelivescoreadmin.tournaments;

import static ravil.amangeldiuly.example.minelivescoreadmin.UrlConstants.BACKEND_URL;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import ravil.amangeldiuly.example.minelivescoreadmin.R;
import ravil.amangeldiuly.example.minelivescoreadmin.utils.LocalDateTimeDeserializer;
import ravil.amangeldiuly.example.minelivescoreadmin.web.apis.GroupInfoApi;
import ravil.amangeldiuly.example.minelivescoreadmin.web.responses.FinishStageDTO;
import ravil.amangeldiuly.example.minelivescoreadmin.web.responses.GroupDTO;
import ravil.amangeldiuly.example.minelivescoreadmin.web.responses.GroupInfoDTO;
import ravil.amangeldiuly.example.minelivescoreadmin.web.responses.GroupInfoListDTO;
import ravil.amangeldiuly.example.minelivescoreadmin.web.responses.TeamDTO;
import ravil.amangeldiuly.example.minelivescoreadmin.web.responses.TournamentDto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TournamentPlayOfTabFragment extends Fragment {

    private static final String ARG_TAB_NAME = "arg_tab_name";
    private TextView groupName;
    private GroupDTO groupDTO;
    private ScrollView scrollView;
    private TableLayout tableLayout;
    private GroupInfoApi groupInfoApi;
    private TournamentDto tournamentDto;
    private Retrofit retrofit;
    private TextView notStarted;
    private Button finishButton;
    private List<GroupInfoListDTO> groupInfoListDTO;
    private TournamentGroupStageFragment.OnFinishButtonClickListener onFinishButtonClickListener;
    private FinishStageDTO finishStageDTO;
    private List<Long> selectedTeams;
    private int teamSize;
    private int selectedTeamCount = 0;
    private OnNotifySetData onNotifySetOnChanged;

    public TournamentPlayOfTabFragment(GroupDTO groupDTO, TournamentDto tournamentDto, TournamentGroupStageFragment.OnFinishButtonClickListener listener) {
        this.tournamentDto = tournamentDto;
        this.groupDTO = groupDTO;
        this.onFinishButtonClickListener = listener;
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tournament_play_of_item, container, false);

        scrollView = view.findViewById(R.id.table_scroll_view);
        tableLayout = view.findViewById(R.id.table_layout);
        notStarted = view.findViewById(R.id.not_started);
        finishButton = view.findViewById(R.id.finish_button);

        selectedTeams = new ArrayList<>();
        initializeRetrofit();
        getTable();

        if (!groupDTO.isCurrentStage()) {
            finishButton.setEnabled(false);
        }

        finishButton.setOnClickListener(finishStage());

        return view;
    }

    @NonNull
    private View.OnClickListener finishStage() {
        return v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            View customView = getLayoutInflater().inflate(R.layout.custom_alert_dialog, null);
            builder.setView(customView);
            Button positiveButton = customView.findViewById(R.id.positive_button);
            Button negativeButton = customView.findViewById(R.id.negative_button);
            AlertDialog dialog = builder.create();

            positiveButton.setOnClickListener(v1 -> {
                if (selectedTeams.isEmpty()) {
                    Toast.makeText(getContext(), "Select teams to finish stage", Toast.LENGTH_SHORT).show();
                } else {
                    finishStageDTO = new FinishStageDTO(selectedTeams, groupDTO.getGroupId());
                    groupInfoApi
                            .finishStage(tournamentDto.getTournamentId(), finishStageDTO)
                            .enqueue(finishStageCallback());


                }

                dialog.dismiss();
            });

            negativeButton.setOnClickListener(v12 -> {
                dialog.dismiss();
            });


            dialog.show();
        };
    }

    @NonNull
    private Callback<List<TeamDTO>> finishStageCallback() {
        return new Callback<>() {
            @Override
            public void onResponse(Call<List<TeamDTO>> call, Response<List<TeamDTO>> response) {
                if (response.isSuccessful()) {
                    finishButton.setEnabled(false);
                    if (onNotifySetOnChanged != null) {
                        onNotifySetOnChanged.onNotifySetData();
                    }

                    if (onFinishButtonClickListener != null) {
                        onFinishButtonClickListener.onFinishButtonClicked();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<TeamDTO>> call, Throwable t) {

            }
        };
    }

    private void getTable() {
        Callback<List<GroupInfoListDTO>> callback = new Callback<>() {
            @Override
            public void onResponse(Call<List<GroupInfoListDTO>> call, Response<List<GroupInfoListDTO>> response) {
                if (response.isSuccessful()) {
                    groupInfoListDTO = response.body();
                    List<GroupInfoDTO> sortedByPointTeams = groupInfoListDTO.get(0).getSortedByPointTeams();
                    if (sortedByPointTeams.isEmpty()) {
                        finishButton.setVisibility(View.GONE);
                        scrollView.setVisibility(View.GONE);
                        notStarted.setVisibility(View.VISIBLE);
                    } else {
                        createTableHeaderRow();
                        fillTable(sortedByPointTeams);
                        finishButton.setVisibility(View.VISIBLE);
                        scrollView.setVisibility(View.VISIBLE);
                        notStarted.setVisibility(View.GONE);
                        teamSize = sortedByPointTeams.size() / 2;
                    }
                }
            }

            @Override
            public void onFailure(Call<List<GroupInfoListDTO>> call, Throwable t) {

            }
        };
        groupInfoApi.groupInfoByGroup(groupDTO.getGroupId(), tournamentDto.getTournamentId()).enqueue(callback);
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
        groupInfoApi = retrofit.create(GroupInfoApi.class);
    }

    private void fillTable(List<GroupInfoDTO> sortedByPointTeams) {
        for (int i = 0; i < sortedByPointTeams.size(); i++) {
            GroupInfoDTO rowData = sortedByPointTeams.get(i);
            TableRow row = createTableRow();

            TextView positionText = createTextView(rowData.getPosition() + "", 1);
            LinearLayout team = createTeamTextView(rowData.getTeamLogo(), rowData.getTeamName(), 4);
            TextView playedMatchText = createTextView(rowData.getGamePlayed() + "", 1);

            Integer diff = rowData.getGoalCount() - rowData.getGoalMissed();
            TextView goalsScoredText = createTextView(diff + "", 1);
            CheckBox checkBox = getCheckBox(1);

            checkBox.setTag(i);

            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                int rowPosition = (int) buttonView.getTag();
                if (isChecked) {
                    if (selectedTeamCount < teamSize) {
                        selectedTeams.add(sortedByPointTeams.get(rowPosition).getTeamId());
                        selectedTeamCount++;
                    } else {
                        checkBox.setChecked(false);
                    }
                    Log.i("", "SELECTED TEAMS" + selectedTeams.toString());
                } else {
                    selectedTeams.remove(sortedByPointTeams.get(rowPosition).getTeamId());
                    selectedTeamCount--;
                    Log.i("", "SELECTED TEAMS" + selectedTeams.toString());

                }
            });

            row.addView(positionText);
            row.addView(team);
            row.addView(playedMatchText);
            row.addView(goalsScoredText);
            row.addView(checkBox);

            tableLayout.addView(row);
        }
    }

    @NonNull
    private CheckBox getCheckBox(int weight) {
        CheckBox checkBox = new CheckBox(requireContext());
//        checkBox.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1);
//        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        checkBox.setLayoutParams(layoutParams);
        checkBox.setChecked(false);
        checkBox.setButtonTintList(getResources().getColorStateList(R.color.app_orange));
//        checkBox.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.checkbox_border));
        return checkBox;
    }

    private void createTableHeaderRow() {
        TableRow headerRow = createTableRow();
        TextView positionHeader = createHeaderTextView(getString(R.string.Num), 1);
        TextView teamHeader = createHeaderTextView(getString(R.string.team), 4);
        TextView playedMatchHeader = createHeaderTextView(getString(R.string.played_match_count), 1);
        TextView goalDifferenceHeader = createHeaderTextView(getString(R.string.goal_difference), 1);
        TextView nextStage = createHeaderTextView(getString(R.string.next_stage), 1);

        headerRow.addView(positionHeader);
        headerRow.addView(teamHeader);
        headerRow.addView(playedMatchHeader);
        headerRow.addView(goalDifferenceHeader);
        headerRow.addView(nextStage);

        tableLayout.addView(headerRow);
    }

    private TableRow createTableRow() {
        TableRow row = new TableRow(requireContext());
        row.setPadding(15, 15, 15, 15);
        row.setBackgroundResource(R.drawable.table_row_background);
        return row;
    }

    private TextView createHeaderTextView(String text, int weight) {
        TextView textView = new TextView(requireContext());
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, weight);
        textView.setLayoutParams(layoutParams);
        textView.setText(text);
        textView.setTextColor(Color.parseColor("#FFFFFFFF"));
        if (!text.equals("Team")) {
            textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        }
        textView.setTypeface(null, Typeface.BOLD);
        return textView;
    }


    private TextView createTextView(String text, int weight) {
        TextView textView = new TextView(requireContext());
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, weight);
        textView.setLayoutParams(layoutParams);
        textView.setText(text);
        textView.setTextColor(Color.parseColor("#FFFFFFFF"));
        textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        return textView;
    }

    private LinearLayout createTeamTextView(String teamLogoUrl, String teamName, int weight) {
        LinearLayout layout = new LinearLayout(requireContext());
        layout.setPadding(15, 15, 15, 15);
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, weight);
        layout.setLayoutParams(layoutParams);
        layout.setOrientation(LinearLayout.HORIZONTAL);

        ImageView teamLogo = new ImageView(requireContext());
        LinearLayout.LayoutParams logoParams = new LinearLayout.LayoutParams(60, 60);
        teamLogo.setLayoutParams(logoParams);
        teamLogo.setScaleType(ImageView.ScaleType.FIT_CENTER);

        Glide.with(this)
                .load(teamLogoUrl)
                .into(teamLogo);

        TextView teamNameText = new TextView(requireContext());
        LinearLayout.LayoutParams nameParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        nameParams.setMarginStart(10);
        teamNameText.setLayoutParams(nameParams);
        teamNameText.setText(getTeamName(teamName));
        teamNameText.setTextColor(Color.parseColor("#FFFFFFFF"));

        layout.addView(teamLogo);
        layout.addView(teamNameText);

        return layout;
    }

    private String getTeamName(String teamName) {
        if (teamName.contains(" ") && teamName.length() > 11) {
            String[] s = teamName.split(" ");
            return s[0] + " " + s[1].charAt(0) + ".";
        } else if (!teamName.contains(" ") && teamName.length() >= 11) {
            return teamName.substring(0, 10) + "...";
        }
        return teamName;
    }

    public void setOnFinishButtonClickListener(OnNotifySetData listener) {
        onNotifySetOnChanged = listener;
    }

    public interface OnNotifySetData {
        void onNotifySetData();
    }
}
