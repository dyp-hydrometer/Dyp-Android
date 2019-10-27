package com.test.framer;


import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import com.test.framer.util.Prefs;
import static com.test.framer.UnitFragment.gravUnit;
import static com.test.framer.UnitFragment.tempUnit;

//import static java.lang.Thread.currentThread;
//import android.widget.Toast;
//import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    final Handler refreshHandler = new Handler();
    private Prefs pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
     // get the units save in the shared preferences
        pref = new Prefs(this);
        gravUnit = pref.getDefaultGravity();
        tempUnit = pref.getDefaultTemp();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new homeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
            // using contain for auto refresh
            contents();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_home:
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                        new homeFragment()).commit();

                contents();

                break;
            case R.id.nav_unit:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new UnitFragment()).commit();
                break;
            case R.id.nav_brew_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new BrewProfileFragment()).commit();
                break;
            case R.id.nav_calibrate:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new CalibrateFragment()).commit();
                break;
//            case R.id.nav_share:
//                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.nav_send:
//                Toast.makeText(this, "Send", Toast.LENGTH_SHORT).show();
//                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void contents() {
        //private volatile boolean exit = false;

        //        homeFragment hf = new homeFragment();
//        FragmentManager manager = getFragmentManager();
//        manager.beginTransaction()
//                .replace(R.id.fragment_container,new homeFragment()).commit();
//        public void run() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new homeFragment()).commit();
        Refresh(10000);
    }

//        public void stop() {
//            exit = true;
//        }

    // onCreateView().getAutofillId();
    // public Handler refreshHandler = new Handler();


    public void Refresh(int milliseconds) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // do updates
                //this.run();
                contents();
            }

        };
        refreshHandler.postDelayed(runnable, milliseconds);

    }




    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}