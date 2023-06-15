package ravil.amangeldiuly.example.minelivescoreuser.tournaments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import ravil.amangeldiuly.example.minelivescoreuser.R;
import ravil.amangeldiuly.example.minelivescoreuser.web.responses.GroupDTO;

public class TournamentInfoTabFragment extends Fragment {

    private static final String ARG_TAB_NAME = "arg_tab_name";
    private TextView groupName;
    private GroupDTO groupDTO;

    public static TournamentInfoTabFragment newInstance(GroupDTO groupTab) {
        TournamentInfoTabFragment fragment = new TournamentInfoTabFragment(groupTab);
        Bundle args = new Bundle();
        args.putString(ARG_TAB_NAME, groupTab.getGroupName());
        fragment.setArguments(args);
        return fragment;
    }

    public TournamentInfoTabFragment(GroupDTO groupDTO) {
        this.groupDTO = groupDTO;
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tournament_cup_info_item, container, false);
        groupName = view.findViewById(R.id.group_name);
        groupName.setText(groupDTO.getGroupName());

        return view;
    }
}
