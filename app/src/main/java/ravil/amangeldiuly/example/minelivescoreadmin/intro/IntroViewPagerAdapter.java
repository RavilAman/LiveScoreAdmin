package ravil.amangeldiuly.example.minelivescoreadmin.intro;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

import ravil.amangeldiuly.example.minelivescoreadmin.R;

public class IntroViewPagerAdapter extends PagerAdapter {

    private Context context;
    private List<Integer> layouts;

    public IntroViewPagerAdapter(Context context) {
        this.context = context;
        layouts = List.of(
                R.layout.intro_first,
                R.layout.intro_second,
                R.layout.intro_third
        );
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layoutScreen = inflater.inflate(layouts.get(position), null);
        container.addView(layoutScreen);
        return layoutScreen;
    }

    @Override
    public int getCount() {
        return layouts.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
