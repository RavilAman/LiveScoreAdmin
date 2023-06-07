package ravil.amangeldiuly.example.minelivescoreuser;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

import ravil.amangeldiuly.example.minelivescoreuser.fragments.FavouritesFragment;
import ravil.amangeldiuly.example.minelivescoreuser.fragments.ScoresFragment;
import ravil.amangeldiuly.example.minelivescoreuser.fragments.admin.CreateInDrawFragment;
import ravil.amangeldiuly.example.minelivescoreuser.fragments.admin.LoadTeamsFragment;
import ravil.amangeldiuly.example.minelivescoreuser.fragments.admin.TournamentListFragment;
import ravil.amangeldiuly.example.minelivescoreuser.fragments.admin.TransfersFragment;

public class MainActivity extends AppCompatActivity {

    // todo: add check for internet availability

    // todo: как нибудь добавить чек на токен, если заекспайрился, взять новый, и переподписаться на все топики в базе локальной

    private TextView noInternetTextView;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle drawerToggle;
    private MenuItem selectedMenuItem;


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

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerToggle.setToolbarNavigationClickListener(v -> drawerLayout.openDrawer(GravityCompat.END));

        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        });

        navigationView.setNavigationItemSelectedListener(item -> {
            FragmentManager fragmentManager = getSupportFragmentManager();
            switch (item.getItemId()) {
                case R.id.nav_tournament:
                    replaceFragment(fragmentManager, new TournamentListFragment(fragmentManager, item), item);
                    break;
                case R.id.nav_upload_teams:
                    replaceFragment(fragmentManager, new LoadTeamsFragment(), item);
                    break;
                case R.id.nav_transfer:
                    replaceFragment(fragmentManager, new TransfersFragment(), item);
                    break;
                case R.id.nav_create_in_draw:
                    replaceFragment(fragmentManager, new CreateInDrawFragment(), item);
                    break;
            }

            drawerLayout.closeDrawer(GravityCompat.END);
            return true;
        });

        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
//        checkInternetAvailability();
    }

    private void replaceFragment(FragmentManager fragmentManager, Fragment fragment, MenuItem item) {
        if (selectedMenuItem != null) {
            selectedMenuItem.setChecked(false);
        }
        selectedMenuItem = item;

        item.setChecked(true);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
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
                        drawerLayout.openDrawer(getEnd());
                        break;
                }

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
                return true;
            }

            private int getEnd() {
                if (selectedMenuItem != null) {
                    selectedMenuItem.setChecked(false);
                    selectedMenuItem = null;
                }
                return GravityCompat.END;
            }
        };
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }

    //    private void checkInternetAvailability() {
//        if (isConnected(this)) {
//            noInternetTextView.setVisibility(View.VISIBLE);
//        }
//    }
}