package ravil.amangeldiuly.example.minelivescoreuser;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import ravil.amangeldiuly.example.minelivescoreuser.fragments.FavouritesFragment;
import ravil.amangeldiuly.example.minelivescoreuser.fragments.ScoresFragment;

public class MainActivity extends AppCompatActivity {

    // todo: add check for internet availability

    // todo: как нибудь добавить чек на токен, если заекспайрился, взять новый, и переподписаться на все топики в базе локальной

    private TextView noInternetTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noInternetTextView = findViewById(R.id.no_internet_text_view);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_bar);
        bottomNavigationView.setOnItemSelectedListener(navigationListener());

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new ScoresFragment())
                .commit();

//        checkInternetAvailability();
    }

    private NavigationBarView.OnItemSelectedListener navigationListener() {
        return new NavigationBarView.OnItemSelectedListener() {
            Fragment selectedFragment = null;

            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_scores:
                        item.setChecked(true);
                        selectedFragment = new ScoresFragment();
                        break;
                    case R.id.nav_favourites:
                        item.setChecked(true);
                        selectedFragment = new FavouritesFragment();
                        break;
                }

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
                return true;
            }
        };
    }

//    private void checkInternetAvailability() {
//        if (isConnected(this)) {
//            noInternetTextView.setVisibility(View.VISIBLE);
//        }
//    }
}