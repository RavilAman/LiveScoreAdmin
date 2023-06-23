package ravil.amangeldiuly.example.minelivescoreadmin.fragments.tournament;

import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

public class FragmentSliderAdapter extends PagerAdapter {
    private List<RelativeLayout> relativeLayouts;

    public FragmentSliderAdapter(List<RelativeLayout> relativeLayouts) {
        this.relativeLayouts = relativeLayouts;
    }

    @Override
    public int getCount() {
        return relativeLayouts.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        RelativeLayout relativeLayout = relativeLayouts.get(position);
        container.addView(relativeLayout);
        return relativeLayout;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);
    }
}
