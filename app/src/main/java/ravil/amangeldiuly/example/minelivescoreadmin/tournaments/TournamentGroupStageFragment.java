package ravil.amangeldiuly.example.minelivescoreadmin.tournaments;

import static ravil.amangeldiuly.example.minelivescoreadmin.UrlConstants.BACKEND_URL;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDateTime;
import java.util.List;

import ravil.amangeldiuly.example.minelivescoreadmin.R;
import ravil.amangeldiuly.example.minelivescoreadmin.utils.LocalDateTimeDeserializer;
import ravil.amangeldiuly.example.minelivescoreadmin.web.apis.GroupInfoApi;
import ravil.amangeldiuly.example.minelivescoreadmin.web.requests.GroupDTO;
import ravil.amangeldiuly.example.minelivescoreadmin.web.requests.GroupInfoDTO;
import ravil.amangeldiuly.example.minelivescoreadmin.web.requests.GroupInfoListDTO;
import ravil.amangeldiuly.example.minelivescoreadmin.web.requests.TeamDTO;
import ravil.amangeldiuly.example.minelivescoreadmin.web.requests.TournamentDto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TournamentGroupStageFragment extends Fragment {

    private static final String ARG_TAB_NAME = "arg_tab_name";
    private TextView groupName;
    private GroupDTO groupDTO;
    private ScrollView tableScrollView;
    private List<GroupInfoListDTO> groupInfoListDTOS;
    private Retrofit retrofit;
    private GroupInfoApi groupInfoApi;
    private TournamentDto tournamentDto;
    private LinearLayout parentLayout;
    private Button finishButton;
    private OnFinishButtonClickListener onFinishButtonClickListener;


    public TournamentGroupStageFragment(GroupDTO groupDTO, TournamentDto tournamentDto, OnFinishButtonClickListener listener) {
        this.tournamentDto = tournamentDto;
        this.groupDTO = groupDTO;
        this.onFinishButtonClickListener = listener;
    }

    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tournament_group_stage_item, container, false);
        groupName = view.findViewById(R.id.group_name);
        tableScrollView = view.findViewById(R.id.group_stage_scroll_view);
        finishButton = view.findViewById(R.id.finish_button);

        initializeRetrofit();
        parentLayout = new LinearLayout(getContext());
        parentLayout.setOrientation(LinearLayout.VERTICAL);

        getTables();
        tableScrollView.addView(parentLayout);

        if (!groupDTO.isCurrentStage()){
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
                groupInfoApi.finishGroupStage(tournamentDto.getTournamentId()).enqueue(finishGroupStage());
                if (onFinishButtonClickListener != null) {
                    onFinishButtonClickListener.onFinishButtonClicked();
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
    private Callback<List<TeamDTO>> finishGroupStage() {
        return new Callback<>() {
            @Override
            public void onResponse(Call<List<TeamDTO>> call, Response<List<TeamDTO>> response) {
                if (response.isSuccessful()) {
                    finishButton.setEnabled(false);
                }
            }

            @Override
            public void onFailure(Call<List<TeamDTO>> call, Throwable t) {

            }
        };
    }



    private void fillTables(LinearLayout parentLayout, List<GroupInfoListDTO> groupInfoListDTOS) {

        for (int i = 0; i < groupInfoListDTOS.size(); i++) {
            GroupInfoListDTO groupInfoListDTO = groupInfoListDTOS.get(i);
            List<GroupInfoDTO> sortedByPointTeams = groupInfoListDTO.getSortedByPointTeams();

            TextView groupTextView = setGroupName(groupInfoListDTO.getGroupName());
            TableLayout tableLayout = new TableLayout(getContext());
            createTableHeaderRow(tableLayout);


            tableLayout.setLayoutParams(getParams(15, 0, 15, 0));


            for (int j = 0; j < sortedByPointTeams.size(); j++) {
                GroupInfoDTO rowData = sortedByPointTeams.get(j);
                TableRow row = createTableRow();
                TextView positionText = createTextView(rowData.getPosition() + "", 1);
                LinearLayout team = createTeamTextView(rowData.getTeamLogo(), rowData.getTeamName(), 4);
                TextView playedMatchText = createTextView(rowData.getGamePlayed() + "", 1);
                TextView winText = createTextView(rowData.getWinCount() + "", 1);
                TextView drawText = createTextView(rowData.getDrawCount() + "", 1);
                TextView loseText = createTextView(rowData.getLoseCount() + "", 1);
                Integer diff = rowData.getGoalCount() - rowData.getGoalMissed();
                TextView goalsScoredText = createTextView(diff + "", 1);
                TextView pointsText = createTextView(rowData.getPoints() + "", 1);

                row.addView(positionText);
                row.addView(team);
                row.addView(playedMatchText);
                row.addView(winText);
                row.addView(drawText);
                row.addView(loseText);
                row.addView(goalsScoredText);
                row.addView(pointsText);

                tableLayout.addView(row);
            }
            parentLayout.addView(groupTextView);
            parentLayout.addView(tableLayout);
        }
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

    private void getTables() {
        Callback<List<GroupInfoListDTO>> callback = new Callback<>() {
            @Override
            public void onResponse(retrofit2.Call<List<GroupInfoListDTO>> call, Response<List<GroupInfoListDTO>> response) {
                if (response.isSuccessful()) {
                    groupInfoListDTOS = response.body();
                    fillTables(parentLayout, groupInfoListDTOS);
                }
            }

            @Override
            public void onFailure(Call<List<GroupInfoListDTO>> call, Throwable t) {

            }
        };
        groupInfoApi.allGroupsByPoint(tournamentDto.getTournamentId()).enqueue(callback);
    }

    private TextView setGroupName(String groupName) {
        TextView groupTextView = new TextView(getContext());
        groupTextView.setText(groupName);
        groupTextView.setPadding(10, 10, 10, 10);
        groupTextView.setTextColor(Color.parseColor("#FFFFFFFF"));
        groupTextView.setLayoutParams(getParams(0, 40, 0, 0));
        groupTextView.setTypeface(null, Typeface.BOLD);
        groupTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, 50);
        return groupTextView;
    }

    private LinearLayout createTeamTextView(String teamLogoUrl, String teamName, int weight) {
        LinearLayout layout = new LinearLayout(requireContext());
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

    private TextView createTextView(String text, int weight) {
        TextView textView = new TextView(requireContext());
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, weight);
        textView.setLayoutParams(layoutParams);
        textView.setText(text);
        textView.setTextColor(Color.parseColor("#FFFFFFFF"));
        textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        return textView;
    }

    @NonNull
    private static LinearLayout.LayoutParams getParams(int left, int top, int right, int bottom) {
        LinearLayout.LayoutParams tableHeaderParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        tableHeaderParams.setMargins(left, top, right, bottom);

        return tableHeaderParams;
    }

    private void createTableHeaderRow(TableLayout tableLayout) {
        TableRow headerRow = createTableRow();
        TextView positionHeader = createHeaderTextView(getString(R.string.Num), 1);
        TextView teamHeader = createHeaderTextView(getString(R.string.team), 4);
        TextView playedMatchHeader = createHeaderTextView(getString(R.string.played_match_count), 1);
        TextView winHeader = createHeaderTextView(getString(R.string.win_count), 1);
        TextView drawHeader = createHeaderTextView(getString(R.string.draw_count), 1);
        TextView loseHeader = createHeaderTextView(getString(R.string.lose_count), 1);
        TextView goalDifferenceHeader = createHeaderTextView(getString(R.string.goal_difference), 1);
        TextView pointsHeader = createHeaderTextView(getString(R.string.points), 1);

        headerRow.addView(positionHeader);
        headerRow.addView(teamHeader);
        headerRow.addView(playedMatchHeader);
        headerRow.addView(winHeader);
        headerRow.addView(drawHeader);
        headerRow.addView(loseHeader);
        headerRow.addView(goalDifferenceHeader);
        headerRow.addView(pointsHeader);
        tableLayout.addView(headerRow);
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

    private TableRow createTableRow() {
        TableRow row = new TableRow(requireContext());
        row.setPadding(15, 15, 15, 15);
        row.setBackgroundResource(R.drawable.table_row_background);
        return row;
    }

    public interface OnFinishButtonClickListener {
        void onFinishButtonClicked();
    }
}