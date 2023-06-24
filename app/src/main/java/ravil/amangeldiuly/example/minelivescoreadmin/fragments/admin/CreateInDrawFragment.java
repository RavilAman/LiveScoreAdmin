package ravil.amangeldiuly.example.minelivescoreadmin.fragments.admin;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ravil.amangeldiuly.example.minelivescoreadmin.R;
import ravil.amangeldiuly.example.minelivescoreadmin.web.RequestHandler;
import ravil.amangeldiuly.example.minelivescoreadmin.web.apis.GroupInfoApi;
import ravil.amangeldiuly.example.minelivescoreadmin.web.apis.TeamApi;
import ravil.amangeldiuly.example.minelivescoreadmin.web.responses.AfterDrawDTO;
import ravil.amangeldiuly.example.minelivescoreadmin.web.requests.GroupInfoDTO;
import ravil.amangeldiuly.example.minelivescoreadmin.web.responses.SaveGroupInfoDTO;
import ravil.amangeldiuly.example.minelivescoreadmin.web.requests.TeamDTO;
import ravil.amangeldiuly.example.minelivescoreadmin.web.requests.TournamentDto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CreateInDrawFragment extends Fragment {

    private View currentView;
    private FragmentManager fragmentManager;
    private List<TeamDTO> teamList;
    private List<AfterDrawDTO> groupList;
    private LinearLayout teamContainer;
    private GridLayout groupTableLayout;
    private Button uploadButton;
    private Retrofit retrofit;
    private GroupInfoApi groupInfoApi;
    private TeamApi teamApi;
    private TournamentDto tournamentDto;
    private int completedRequests = 0;
    private ShapeableImageView tournamentLogo;
    private TextView tournamentName;
    private TextView tournamentGroup;
    private ImageButton imageButton;

    public CreateInDrawFragment(TournamentDto tournamentDto, FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
        this.tournamentDto = tournamentDto;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.fragment_create_in_draw, container, false);

        teamContainer = currentView.findViewById(R.id.teamContainer);
        groupTableLayout = currentView.findViewById(R.id.groupTableLayout);
        uploadButton = currentView.findViewById(R.id.uploadButton);
        tournamentName = currentView.findViewById(R.id.fragment_tournament_name);
        tournamentLogo = currentView.findViewById(R.id.fragment_tournament_logo);
        tournamentGroup = currentView.findViewById(R.id.fragment_tournament_group);
        imageButton = currentView.findViewById(R.id.fragment_back_button);
        imageButton.setOnClickListener(backButtonListener());

        teamList = new ArrayList<>();
        groupList = new ArrayList<>();

        initializeRetrofit();


        Callback<List<TeamDTO>> teamCallback = new Callback<>() {
            @Override
            public void onResponse(Call<List<TeamDTO>> call, Response<List<TeamDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    teamList = response.body();
                    handleDataResponse();
                } else {
                    // Handle error case
                }
            }

            @Override
            public void onFailure(Call<List<TeamDTO>> call, Throwable t) {
                // Handle error case
            }
        };

        Callback<List<AfterDrawDTO>> groupCallback = new Callback<List<AfterDrawDTO>>() {
            @Override
            public void onResponse(Call<List<AfterDrawDTO>> call, Response<List<AfterDrawDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    groupList = response.body();
                    handleDataResponse();
                } else {
                    // Handle error case
                }
            }

            @Override
            public void onFailure(Call<List<AfterDrawDTO>> call, Throwable t) {
                // Handle error case
            }
        };

        fetchTeamData(teamCallback);
        fetchGroupData(groupCallback);

        // Setup the upload button click listener
        uploadButton.setOnClickListener(v -> {
            // Collect the updated draw data from the group tables
            List<SaveGroupInfoDTO> drawList = collectDrawData();

            // Send the draw data to the backend
            sendDrawData(drawList);
        });

        tournamentName.setText(tournamentDto.getTournamentName());
        tournamentGroup.setText(tournamentDto.getTournamentLocation());
        Glide.with(this)
                .load(tournamentDto.getTournamentLogo())
                .into(tournamentLogo);


        return currentView;
    }

    private void handleDataResponse() {
        completedRequests++;
        if (completedRequests == 2) {
            boolean haveDraw = false;
            for (AfterDrawDTO g : groupList) {
                haveDraw = g.getTeams().isEmpty();
            }
            if (haveDraw) {
                setupTeamNames(teamList);
                setupGroupTables(groupList);
            } else {
                setupGroupTables(groupList);
            }
        }
    }

    private void initializeRetrofit() {
        RequestHandler requestHandler = new RequestHandler(getContext());
        retrofit = requestHandler.getRetrofit();
        groupInfoApi = retrofit.create(GroupInfoApi.class);
        teamApi = retrofit.create(TeamApi.class);
    }

    private void fetchTeamData(Callback<List<TeamDTO>> callback) {
        teamList.clear();
        teamApi.findAllTeamsByTournament(tournamentDto.getTournamentId()).enqueue(callback);
    }

    private void fetchGroupData(Callback<List<AfterDrawDTO>> callback) {
        groupList.clear();
        groupInfoApi.teamAfterDraw(tournamentDto.getTournamentId()).enqueue(callback);
    }

    @SuppressLint({"ClickableViewAccessibility", "ResourceAsColor", "SetTextI18n"})
    private void setupTeamNames(List<TeamDTO> teamList) {

        for (TeamDTO team : teamList) {
            View teamView = getLayoutInflater().inflate(R.layout.item_team, null);
            ImageView teamLogoImageView = teamView.findViewById(R.id.teamLogoImageView);
            TextView teamNameTextView = teamView.findViewById(R.id.teamNameTextView);
            TextView teamId = teamView.findViewById(R.id.teamId);

            Glide.with(this).load(team.getTeamLogo()).into(teamLogoImageView);
            teamNameTextView.setText(team.getTeamName());
            teamId.setText(team.getTeamId() + "");

            teamView.setOnTouchListener((v, event) -> {
                ClipData clipData = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                v.startDragAndDrop(clipData, shadowBuilder, v, 0);
                return true;
            });

            teamContainer.addView(teamView);
        }
    }

    @SuppressLint("ResourceAsColor")
    private void setupGroupTables(List<AfterDrawDTO> groupList) {
        for (AfterDrawDTO group : groupList) {
            LinearLayout groupLayout = createCustomGroupLayout();
            groupLayout.setOrientation(LinearLayout.VERTICAL);

            // Create a TextView for the group name
            TextView groupNameTextView = new TextView(getContext());
            groupNameTextView.setText(group.getGroupName());
            groupNameTextView.setTextColor(Color.parseColor("#FFFFFFFF"));

            // Add the group name to the group layout
            groupLayout.addView(groupNameTextView);

            // Add team names to the group layout
            for (TeamDTO team : group.getTeams()) {
                View teamView = getLayoutInflater().inflate(R.layout.item_team, null);
                ImageView teamLogoImageView = teamView.findViewById(R.id.teamLogoImageView);
                TextView teamNameTextView = teamView.findViewById(R.id.teamNameTextView);
                TextView teamId = teamView.findViewById(R.id.teamId);

                Glide.with(this).load(team.getTeamLogo()).into(teamLogoImageView);
                teamNameTextView.setText(team.getTeamName());
                teamId.setText(team.getTeamId() + "");

                groupLayout.addView(teamView);
            }

            // Add the group layout to the groupTableLayout
            groupTableLayout.addView(groupLayout);

            // Set the drop listener for drag and drop
            groupNameTextView.setOnDragListener(new View.OnDragListener() {
                @Override
                public boolean onDrag(View v, DragEvent event) {
                    int action = event.getAction();
                    switch (action) {
                        case DragEvent.ACTION_DROP:
                            View draggedView = (View) event.getLocalState();
                            TextView teamNameTextView = draggedView.findViewById(R.id.teamId);

                            // Get the team name from the dropped view
                            String droppedTeamId = teamNameTextView.getText().toString();

                            // Find the corresponding TeamDTO object based on the dropped team name
                            TeamDTO droppedTeam = null;
                            for (TeamDTO team : teamList) {
                                if (team.getTeamId().toString().equals(droppedTeamId)) {
                                    droppedTeam = team;
                                    break;
                                }
                            }

                            if (droppedTeam != null) {
                                // Update the group table data
                                updateGroupTable(group.getGroupId(), droppedTeam);
                            }

                            // Remove the dragged view from the team container
                            teamContainer.removeView(draggedView);

                            // Add the dragged view to the group table
                            LinearLayout targetLayout = (LinearLayout) v.getParent();
                            targetLayout.addView(draggedView);
                            uploadButton.setEnabled(true);
                            return true;
                    }
                    return true;
                }
            });
        }
    }


    private LinearLayout createCustomGroupLayout() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                dpToPx(170) + (2 * dpToPx(5)), // Account for stroke width on both sides
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
//        layoutParams.setMargins(dpToPx(5), 0, dpToPx(5), 0); // Set margins if needed

        LinearLayout groupLayout = new LinearLayout(getContext());
        groupLayout.setOrientation(LinearLayout.VERTICAL);
        groupLayout.setBackgroundColor(Color.BLACK); // Set background color

        // Set background drawable with rounded border and white color
        int strokeWidth = 5; // Border stroke width in pixels
        int roundRadius = 12; // Border corner radius in pixels
        int strokeColor = Color.WHITE; // Border color
        int fillColor = Color.TRANSPARENT; // Fill color (in this case, transparent)

        // Adjust layout width to accommodate stroke width
        int layoutWidth = dpToPx(170) + (2 * strokeWidth);
        LinearLayout.LayoutParams groupLayoutParams = new LinearLayout.LayoutParams(
                layoutWidth,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        // Create a border drawable with stroke and fill colors
        GradientDrawable borderDrawable = new GradientDrawable();
        borderDrawable.setStroke(strokeWidth, strokeColor);
        borderDrawable.setCornerRadius(roundRadius);
        borderDrawable.setColor(fillColor);

        // Set the border drawable as the background of the layout
        groupLayout.setBackground(borderDrawable);
        groupLayout.setLayoutParams(groupLayoutParams);

        groupLayout.setPadding(16, 16, 16, 16); // Set padding if needed

        // Update layout parameters to increase clickable area
        int clickableAreaPadding = 32; // Increase the padding for the clickable area
        groupLayout.setClickable(true);
        groupLayout.setFocusable(true);
        groupLayout.setFocusableInTouchMode(true);
        groupLayout.setPadding(
                clickableAreaPadding,
                clickableAreaPadding,
                clickableAreaPadding,
                clickableAreaPadding
        );
        groupLayout.setLayoutParams(layoutParams);

        return groupLayout;
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    private void updateGroupTable(Long groupID, TeamDTO teamDTO) {
        for (AfterDrawDTO group : groupList) {
            if (Objects.equals(group.getGroupId(), groupID)) {
                group.getTeams().add(teamDTO);
                break;
            }
        }
    }

    private List<SaveGroupInfoDTO> collectDrawData() {
        List<SaveGroupInfoDTO> drawList = new ArrayList<>();

        for (AfterDrawDTO group : groupList) {
            List<TeamDTO> teams = group.getTeams();
            for (TeamDTO team : teams) {
                drawList.add(new SaveGroupInfoDTO(team.getTeamId(), group.getGroupId()));
            }
        }
        return drawList;
    }

    private void sendDrawData(List<SaveGroupInfoDTO> drawList) {
        // Convert the drawList to JSON and send a POST request to the backend API
        // Use a library like Retrofit or Volley for HTTP requests

        Callback<List<GroupInfoDTO>> callback = new Callback<>() {
            @Override
            public void onResponse(Call<List<GroupInfoDTO>> call, Response<List<GroupInfoDTO>> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Draw uploaded successfully", Toast.LENGTH_SHORT).show();
                    uploadButton.setEnabled(false);
                } else {
                    Toast.makeText(getContext(), "Failed to upload draw", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<GroupInfoDTO>> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to upload draw", Toast.LENGTH_SHORT).show();

            }
        };
        groupInfoApi.createDrawInCup(drawList).enqueue(callback);

        // Example using Retrofit:

    }

    private View.OnClickListener backButtonListener() {
        return view -> {

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentManager.popBackStack();
            fragmentTransaction.commit();
        };
    }
}
