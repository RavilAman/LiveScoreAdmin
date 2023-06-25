package ravil.amangeldiuly.example.minelivescoreadmin;


import static ravil.amangeldiuly.example.minelivescoreadmin.SharedPreferencesConstants.EMPTY;
import static ravil.amangeldiuly.example.minelivescoreadmin.SharedPreferencesConstants.FALSE;
import static ravil.amangeldiuly.example.minelivescoreadmin.SharedPreferencesConstants.FCM_TOKEN;
import static ravil.amangeldiuly.example.minelivescoreadmin.SharedPreferencesConstants.LOGGED_IN;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.messaging.FirebaseMessaging;

import ravil.amangeldiuly.example.minelivescoreadmin.activities.AuthActivity;
import ravil.amangeldiuly.example.minelivescoreadmin.fragments.ScoresFragment;
import ravil.amangeldiuly.example.minelivescoreadmin.fragments.admin.TournamentFragment;
import ravil.amangeldiuly.example.minelivescoreadmin.fragments.admin.TournamentList;
import ravil.amangeldiuly.example.minelivescoreadmin.utils.SharedPreferencesUtil;

public class MainActivity extends AppCompatActivity {

    // todo: add check for internet availability

    // todo: как нибудь добавить чек на токен, если заекспайрился, взять новый, и переподписаться на все топики в базе локальной

    private Context context;
    private TextView noInternetTextView;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle drawerToggle;
    private MenuItem selectedDrawerMenuItem;
    private MenuItem selectedButtomNavMenuItem;
    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();

        getToken();

        noInternetTextView = findViewById(R.id.no_internet_text_view);
        bottomNavigationView = findViewById(R.id.bottom_navigation_bar);
        bottomNavigationView.setOnItemSelectedListener(navigationListener());

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new ScoresFragment(getSupportFragmentManager()))
                .commit();

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerToggle.setToolbarNavigationClickListener(v -> drawerLayout.openDrawer(GravityCompat.END));
        drawerLayout.addDrawerListener(onCloseListener());
        navigationView.setNavigationItemSelectedListener(this::drawerNavigationListener);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
//        checkInternetAvailability();
    }

    @NonNull
    private static DrawerLayout.SimpleDrawerListener onCloseListener() {
        return new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
    }

    @SuppressLint("NonConstantResourceId")
    private boolean drawerNavigationListener(MenuItem item) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch (item.getItemId()) {
            case R.id.nav_tournament:
                replaceFragment(fragmentManager, new TournamentFragment(fragmentManager, item, selectedButtomNavMenuItem, bottomNavigationView), item);
                selectedButtomNavMenuItem.setChecked(true);
                break;
            case R.id.nav_upload_teams:
                replaceFragment(fragmentManager, new TournamentList(fragmentManager, item, selectedButtomNavMenuItem, bottomNavigationView, R.string.upload_team), item);
                selectedButtomNavMenuItem.setChecked(true);
                break;
            case R.id.nav_transfer:
                replaceFragment(fragmentManager, new TournamentList(fragmentManager, item, selectedButtomNavMenuItem, bottomNavigationView, R.string.transfers), item);
                selectedButtomNavMenuItem.setChecked(true);
                break;
            case R.id.nav_create_in_draw:
                replaceFragment(fragmentManager, new TournamentList(fragmentManager, item, selectedButtomNavMenuItem, bottomNavigationView, R.string.create_in_draw), item);
                selectedButtomNavMenuItem.setChecked(true);
                break;
            case R.id.log_out:
                SharedPreferencesUtil.putValue(context, LOGGED_IN, FALSE);
                Intent authIntent = new Intent(context, AuthActivity.class);
                authIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(authIntent);
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.END);
        return true;
    }

    private void replaceFragment(FragmentManager fragmentManager, Fragment fragment, MenuItem item) {
        if (selectedDrawerMenuItem != null) {
            selectedDrawerMenuItem.setChecked(false);
        }
        selectedDrawerMenuItem = item;

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
                        if (selectedDrawerMenuItem != null) {
                            selectedDrawerMenuItem.setChecked(false);
                            selectedDrawerMenuItem = null;
                        }
                        if (selectedButtomNavMenuItem != null) {
                            selectedButtomNavMenuItem.setChecked(false);
                        }
                        selectedButtomNavMenuItem = item;
                        item.setChecked(true);
                        selectedFragment = new ScoresFragment(getSupportFragmentManager());
                        break;
                    case R.id.nav_menu_item:
                        selectedButtomNavMenuItem = item;
                        drawerLayout.openDrawer(getEnd());
                        return false;
                }

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
                return true;
            }

            private int getEnd() {
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

    private void getToken() {
        if (SharedPreferencesUtil.getValue(this, FCM_TOKEN).equals(EMPTY)) {
            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(task -> {
                        if (!task.isSuccessful()) {
                            Log.w("ERROR", "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        String token = task.getResult();
                        SharedPreferencesUtil.putValue(this, FCM_TOKEN, token);
                    });
        }
    }

    //    private void checkInternetAvailability() {
//        if (isConnected(this)) {
//            noInternetTextView.setVisibility(View.VISIBLE);
//        }
//    }
}