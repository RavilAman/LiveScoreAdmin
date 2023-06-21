package ravil.amangeldiuly.example.minelivescoreuser.slider;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

import ravil.amangeldiuly.example.minelivescoreuser.web.tournament_type.TournamentType;

public class SliderAdapter extends PagerAdapter {
    private Context mContext;
    private List<TournamentType> tournamentTypes;

    public SliderAdapter(Context context, List<TournamentType> tournamentTypes) {
        this.mContext = context;
        this.tournamentTypes = tournamentTypes;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LinearLayout layout = new LinearLayout(mContext);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);

        ImageView imageView = new ImageView(mContext);
        imageView.setImageResource(tournamentTypes.get(position).getImage());

        // Create the text view and set the text
        TextView textView = new TextView(mContext);
        textView.setText(tournamentTypes.get(position).getTournamentType());
        textView.setTextColor(Color.BLACK);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        textView.setGravity(Gravity.CENTER);

        layout.addView(imageView);
        layout.addView(textView);

        // Add the parent layout to the container
        container.addView(layout);

        return layout;
    }

    @Override
    public int getCount() {
        return tournamentTypes.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }
}