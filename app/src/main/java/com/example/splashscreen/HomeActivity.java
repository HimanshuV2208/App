package com.example.splashscreen;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity {

    //variables for back button pressed check
    private static final int TIME_TO_PRESS_BACK = 2000;
    private long backPressed;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    private boolean isAdmin;
    protected UserHelper currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
        //changes color of notification bar to black
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.black, this.getTheme()));
        } else {
            getWindow().setStatusBarColor(getResources().getColor(R.color.black));

        }

        setContentView(R.layout.side_nav_menu);

        //loading default home fragment into container
        loadFragment(new HomeFragment());

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.sideNavBar);
        toolbar = findViewById(R.id.toolbar);

        //gets the current user object
        currentUser = (UserHelper)getIntent().getSerializableExtra("class");

        //options to hide from normal user
        //these are only visible to admin
        if(!(currentUser.getIsAdmin())) {
            Menu navMenu = navigationView.getMenu();
            navMenu.findItem(R.id.optionNewEvent).setVisible(false);
        }

        //display username in drawer
        View headerView = navigationView.getHeaderView(0);
        TextView username = headerView.findViewById(R.id.username);
        username.setText(currentUser.getEmail());
        TextView username2 = headerView.findViewById(R.id.username2);
        username2.setText(currentUser.getName());

        //display top toolbar and add side navigation bar
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.toolbar_open, R.string.toolbar_closed);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //checking which option is clicked on
                int id = item.getItemId();
                if(id == R.id.optionHome)
                    loadFragment(new HomeFragment());
                else if(id == R.id.optionDSW)
                    loadFragment(new AboutDSWFragment());
                else if(id == R.id.optionTechnicalSocieties)
                    loadFragment(new TechnicalSocietiesFragment());
                else if(id == R.id.optionCulturalSocieties)
                    loadFragment(new CulturalSocietiesFragment());
                else if(id == R.id.optionFAQ)
                    loadFragment(new FAQFragment());
                else if(id == R.id.optionEvents)
                    loadFragment(new EventsFragment());
                else if(id == R.id.optionLostNFound)
                    loadFragment(new LNFFragment());
                else if(id == R.id.optionCampus)
                    loadFragment(new CampusFragment());
                else if(id == R.id.optionFeedbackNComplain)
                    loadFragment(new FeedbackFragment());
                else if(id == R.id.optionSports)
                    loadFragment(new SportsFragment());
                else if(id == R.id.optionHostels)
                    loadFragment(new HostelFragment());
                else if(id == R.id.optionNewEvent)
                    loadFragment(new AddEventFragment());
                else if(id == R.id.optionSignOut){
                    Intent goSignIn = new Intent(HomeActivity.this, SignInActivity.class);
                    startActivity(goSignIn);
                    finish();
                }

                //close drawer when user chooses option
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    @Override
    public void onBackPressed(){

        //if drawer is open, close it on back press
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
            return;
        }

        //press back twice to close the app
        if(TIME_TO_PRESS_BACK + backPressed > System.currentTimeMillis()){
            super.onBackPressed();
            return;
        }else{
            Toast.makeText(getApplicationContext(), "Press Back again to Exit!",
                    Toast.LENGTH_SHORT).show();
        }
        backPressed = System.currentTimeMillis();
    }

    //load fragment into the frame
    private void loadFragment(Fragment fragment){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        ft.setCustomAnimations(
                R.anim.fade_in_short,
                R.anim.fade_out
        ).replace(R.id.container, fragment);
        ft.commit();
    }
}