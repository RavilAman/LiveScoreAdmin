package ravil.amangeldiuly.example.minelivescoreuser.fragments.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import ravil.amangeldiuly.example.minelivescoreuser.R;

public class LoadTeamsFragment extends Fragment {

    private View currentView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.fragment_load_teams, container, false);

        return currentView;
    }
}
