package ravil.amangeldiuly.example.minelivescoreuser.fragments.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

import ravil.amangeldiuly.example.minelivescoreuser.R;
import ravil.amangeldiuly.example.minelivescoreuser.web.responses.TournamentDto;

public class LoadTeamsFragment extends Fragment {

    private View currentView;
    private TournamentDto tournamentDto;
    private ShapeableImageView tournamentLogo;
    private TextView tournamentName;
    private TextView tournamentGroup;
    private ImageButton imageButton;
    private FragmentManager fragmentManager;



    public static LoadTeamsFragment newInstanse(TournamentDto tournamentDto,FragmentManager fragmentManager){
        LoadTeamsFragment fragment = new LoadTeamsFragment(fragmentManager);
        Bundle args = new Bundle();
        args.putString("tournament_logo", tournamentDto.getTournamentLogo());
        args.putString("tournament_name", tournamentDto.getTournamentName());
        args.putString("tournament_group", tournamentDto.getTournamentLocation());
        fragment.setArguments(args);
        return fragment;
    }

    public LoadTeamsFragment(FragmentManager fragmentManager){
        this.fragmentManager = fragmentManager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.fragment_load_teams, container, false);
        tournamentLogo = currentView.findViewById(R.id.fragment_tournament_logo);
        tournamentName = currentView.findViewById(R.id.fragment_tournament_name);
        tournamentGroup = currentView.findViewById(R.id.fragment_tournament_group);
        imageButton = currentView.findViewById(R.id.fragment_back_button);

        Bundle args = getArguments();

        if (args !=null){
            tournamentName.setText(args.getString("tournament_name"));
            tournamentGroup.setText(args.getString("tournament_group"));
            Glide.with(this)
                    .load(args.getString("tournament_logo"))
                    .into(tournamentLogo);
        }
        imageButton.setOnClickListener(backButtonListener());

        return currentView;
    }

    private View.OnClickListener backButtonListener() {
        return view -> {

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentManager.popBackStack();
            fragmentTransaction.commit();
        };
    }

}
