package ravil.amangeldiuly.example.minelivescoreadmin.tournaments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import ravil.amangeldiuly.example.minelivescoreadmin.R;
import ravil.amangeldiuly.example.minelivescoreadmin.web.responses.GroupDTO;

public class TournamentInfoAdapter extends FragmentPagerAdapter {

    private final List<Fragment> fragmentList = new ArrayList<>();
    private final List<GroupDTO> fragmentTitleList = new ArrayList<>();

    public TournamentInfoAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    public void addFragment(Fragment fragment, GroupDTO group) {
        fragmentList.add(fragment);
        fragmentTitleList.add(group);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitleList.get(position).getGroupName();
    }

}
