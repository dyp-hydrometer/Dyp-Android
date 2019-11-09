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
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.test.framer.model.Gravity;
import com.test.framer.model.temperature;
import com.test.framer.util.Prefs;
import static com.test.framer.UnitFragment.gravUnit;
import static com.test.framer.UnitFragment.tempUnit;
import static com.test.framer.UnitFragment.stDypIP;
import static com.test.framer.UnitFragment.stDypId;
import static com.test.framer.UnitFragment.interval;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

//import static java.lang.Thread.currentThread;
//import android.widget.Toast;
//import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    final Handler refreshHandler = new Handler();
    final Handler refreshBattHandler = new Handler();
    private Prefs pref;
    private int batPercent=50;
    RequestQueue queue;
    private TextView batt;
    Random random = new Random();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // create a new instance of the singleton if it does not exist
        queue = DypSingletonAPI.getInstance(this.getApplicationContext())
                .getRequestQueue();
        batt = findViewById(R.id.bat);

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
        stDypIP =pref.getPiIP();
        stDypId =pref.getDypId();
        interval=pref.getInterval();


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new homeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
            // using contain for auto refresh
            contents();
        }
        setBat();
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
//< The device information will be requested
    public void GetDypInfo()
    {
        // ----------------------------mock API object------------------------
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                "https://jsonplaceholder.typicode.com/todos/1", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonbject) {
                        try {
                            // num = random.nextInt(50) + 1;    // generate the random number from
                            Log.d("JSON", "onResponse: " + jsonbject.getInt("id"));
                            //---------------------------- new
                           // batPercent = jsonbject.getInt("id");
                            batPercent = random.nextInt(50) + 1;
                            batt.setText(String.valueOf(batPercent));
                            //----------------------------------
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", "onErrorResponse: " + error.getMessage());
            }
        });

        queue.add(jsonObjectRequest);
    }
    public void setBat() {
        GetDypInfo();
        refreshBat(10000);
    }
    public void refreshBat(int milliseconds) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // do updates
                //this.run();
                setBat();
            }

        };
        refreshBattHandler.postDelayed(runnable, milliseconds);
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