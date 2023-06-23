package ravil.amangeldiuly.example.minelivescoreuser.fragments.admin;

import static ravil.amangeldiuly.example.minelivescoreuser.UrlConstants.BACKEND_URL;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDateTime;
import java.util.List;

import ravil.amangeldiuly.example.minelivescoreuser.R;
import ravil.amangeldiuly.example.minelivescoreuser.utils.LocalDateTimeDeserializer;
import ravil.amangeldiuly.example.minelivescoreuser.web.RequestHandler;
import ravil.amangeldiuly.example.minelivescoreuser.web.apis.GroupInfoApi;
import ravil.amangeldiuly.example.minelivescoreuser.web.responses.GroupInfoDTO;
import ravil.amangeldiuly.example.minelivescoreuser.web.responses.GroupInfoListDTO;
import ravil.amangeldiuly.example.minelivescoreuser.web.responses.TeamDTO;
import ravil.amangeldiuly.example.minelivescoreuser.web.responses.TournamentDto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TournamentLeagueInfoFragment extends Fragment {

    private List<GroupInfoListDTO> groupInfoListDTO;
    private TableLayout tableLayout;
    private ImageButton imageButton;
    private ShapeableImageView tournamentLogo;
    private FragmentManager fragmentManager;
    private TextView tournamentName;
    private TextView tournamentGroup;
    private TournamentDto tournamentDto;
    private Retrofit retrofit;
    private GroupInfoApi groupInfoApi;
    private Button finishButton;
    private TeamDTO winnerTeam;
    private TextView pageHeader;

    public TournamentLeagueInfoFragment(FragmentManager fragmentManager, TournamentDto tournamentDto) {
        this.fragmentManager = fragmentManager;
        this.tournamentDto = tournamentDto;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View currentView = inflater.inflate(R.layout.fragment_tournament_league_info_item, container, false);

        tableLayout = currentView.findViewById(R.id.table_layout);
        imageButton = currentView.findViewById(R.id.fragment_back_button);
        imageButton.setOnClickListener(backButtonListener());
        tournamentGroup = currentView.findViewById(R.id.fragment_tournament_group);
        tournamentName = currentView.findViewById(R.id.fragment_tournament_name);
        tournamentLogo = currentView.findViewById(R.id.fragment_tournament_logo);
        finishButton = currentView.findViewById(R.id.finish_button);
        pageHeader = currentView.findViewById(R.id.page_header);
        if (tournamentDto.getTournamentStatus().equals("FINISHED")){
            finishButton.setEnabled(false);
        }

        finishButton.setOnClickListener(finishTournament());

        initializeTournamentNameLogo();

        initializeRetrofit();
        getTable();

        return currentView;
    }

    private void initializeTournamentNameLogo() {
        if (tournamentDto.getTournamentStatus().equals("FINISHED")) {
            ColorMatrix colorMatrix = new ColorMatrix();
            colorMatrix.setSaturation(0);
            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorMatrix);
            tournamentLogo.setColorFilter(filter);
            tournamentName.setTextColor(Color.parseColor("#FF757575"));
            tournamentGroup.setTextColor(Color.parseColor("#FF757575"));
            pageHeader.setTextColor(Color.parseColor("#FF757575"));
        } else {
            tournamentLogo.setColorFilter(null);
            tournamentName.setTextColor(Color.parseColor("#FFFFFFFF"));
            tournamentGroup.setTextColor(Color.parseColor("#FFFFFFFF"));
        }

        tournamentName.setText(tournamentDto.getTournamentName());
        tournamentGroup.setText(tournamentDto.getTournamentLocation());
        Glide.with(this)
                .load(tournamentDto.getTournamentLogo())
                .into(tournamentLogo);
    }

    @NonNull
    private View.OnClickListener finishTournament() {
        return v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            View customView = getLayoutInflater().inflate(R.layout.custom_alert_dialog, null);
            builder.setView(customView);
            Button positiveButton = customView.findViewById(R.id.positive_button);
            Button negativeButton = customView.findViewById(R.id.negative_button);
            AlertDialog dialog = builder.create();

            // Set click listeners for the buttons
            positiveButton.setOnClickListener(v1 -> {
                groupInfoApi.finishLeague(tournamentDto.getTournamentId()).enqueue(finishLeague());
                dialog.dismiss();
            });

            negativeButton.setOnClickListener(v12 -> {
                dialog.dismiss();
            });

            dialog.show();
        };
    }

    @NonNull
    private Callback<TeamDTO> finishLeague() {
        return new Callback<>() {
            @Override
            public void onResponse(Call<TeamDTO> call, Response<TeamDTO> response) {
                if (response.isSuccessful()) {
                    winnerTeam = response.body();
                    assert winnerTeam != null;
                    Log.i("",winnerTeam.toString());
                    finishButton.setEnabled(false);
                }
            }

            @Override
            public void onFailure(Call<TeamDTO> call, Throwable t) {

            }
        };
    }

    private void getTable() {
        Callback<List<GroupInfoListDTO>> callback = new Callback<>() {
            @Override
            public void onResponse(Call<List<GroupInfoListDTO>> call, Response<List<GroupInfoListDTO>> response) {
                if (response.isSuccessful()) {
                    groupInfoListDTO = response.body();
                    assert groupInfoListDTO != null;
                    List<GroupInfoDTO> sortedByPointTeams = groupInfoListDTO.get(0).getSortedByPointTeams();
                    createTableHeaderRow();

                    fillTable(sortedByPointTeams);
                }
            }

            @Override
            public void onFailure(Call<List<GroupInfoListDTO>> call, Throwable t) {

            }
        };
        groupInfoApi.allGroupsByPoint(tournamentDto.getTournamentId()).enqueue(callback);
    }

    private View.OnClickListener backButtonListener() {
        return view -> {

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentManager.popBackStack();
            fragmentTransaction.commit();
        };
    }


    private void fillTable(List<GroupInfoDTO> sortedByPointTeams) {
        for (GroupInfoDTO rowData : sortedByPointTeams) {
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
    }

    private void initializeRetrofit() {
        RequestHandler requestHandler = new RequestHandler(getContext());
        retrofit = requestHandler.getRetrofit();
        groupInfoApi = retrofit.create(GroupInfoApi.class);
    }

    private void createTableHeaderRow() {
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

    private  String getTeamName(String teamName) {
        if (teamName.contains(" ") && teamName.length() > 11) {
            String[] s = teamName.split(" ");
            return s[0] + " " + s[1].charAt(0)+".";
        } else if (!teamName.contains(" ") && teamName.length() >= 11) {
            return teamName.substring(0, 10) + "...";
        }
        return teamName;
    }
}
