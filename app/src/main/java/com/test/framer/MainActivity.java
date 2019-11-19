package com.test.framer;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.test.framer.model.profile;
import com.test.framer.util.Prefs;
import static com.test.framer.UnitFragment.gravUnit;
import static com.test.framer.UnitFragment.tempUnit;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    final Handler refreshHandler = new Handler();
    final Handler refreshBattHandler = new Handler();
    private Prefs pref;
    private int batPercent=50;
    RequestQueue queue;
    private TextView batt;
    Random random = new Random();
    public static StringBuffer url1,urlGetProfle;
    public static String portNum = "5000" ;
    public static String stDypIP="";
    public static String stDypId="";
    static int ProfileId;
    static String brewName;

    static String brewStatus;
    public static long interval;
    private Spinner spinner;


    public ArrayList<profile> profileArrayList1;
    private ArrayAdapter<profile> arrayAdapter1;




    private int BPid;
    private String BPName;
    private double BPGrav;
    private double BPTemp;
    private String BPDesc;


//    // recycle view
//    private RecyclerView recyclerView;
//    private RecyclerViewAdapter recyclerViewAdapter;


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
        brewStatus = pref.getStaus();

        // build the url1(last reading) for the api
        url1 = new StringBuffer("http://");
        url1.append(stDypIP);
        url1.append(":"+portNum+"/api/hydrometers/");
        url1.append(stDypId+"/data/last");  //< append the Dyp id

        // build the url for beer profile description
        urlGetProfle = new StringBuffer("http://");
        urlGetProfle.append(stDypIP);
        urlGetProfle.append(":"+portNum+"/api/hydrometers/");
        urlGetProfle.append(stDypId+"/data/last");  //< append the Dyp id


//       //< set the spinner on the toolbar
        spinner = findViewById(R.id.spinProfile);
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
//                R.array.numbers, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //< pick the type of spinner
//        spinner.setAdapter(adapter);
//        spinner.setOnItemSelectedListener(this);
        profileArrayList1 = new ArrayList();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new homeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
            // using contain for auto refresh
            contents();
        }
        GetProfileList();
        setToolbarData();
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
            case R.id.nav_profile_list:
               // Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
                Intent listBrewIntent = new Intent(MainActivity.this, BrewList.class);
                startActivity(listBrewIntent);
                break;
            case R.id.nav_calibrate:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new CalibrateFragment()).commit();
                break;
//
//            case R.id.nav_send:
//                Toast.makeText(this, "Send", Toast.LENGTH_SHORT).show();
//                break;

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void GetProfileList(){
        //----------------------------mock API object------------------------
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                "https://jsonplaceholder.typicode.com/todos/1", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonbject) {
                        try {


                            Log.d("PROFILE", "onResponse: " + jsonbject.getInt("id"));
                            BPid=jsonbject.getInt("id");
                            BPName=jsonbject.getString("title");
                            BPGrav=jsonbject.getDouble("id");
                            BPTemp=jsonbject.getDouble("id");
                            BPDesc=jsonbject.getString("title");
//
//                            //---------------------------- new
//
                            Log.d("PROFILE", "onResponse" + BPDesc );
                            profileArrayList1.add(new profile(1,"Guiness",2,70,"Description"));
                            profileArrayList1.add(new profile(BPid,BPName,BPGrav,BPTemp,"Description"));

                            GetProfileValue(); //< must have this method to get the data


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

   //<  make a Get list of profile from the  request from the profile (actual call)
   // use this urlGetProfle use------------------------
        // Json Array request
        //---------Get the last data entry of the hydrometer, Specific gravity and temperature
//        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
//                "http://192.168.1.2:5000/api/hydrometers/2/data/last", null, new Response.Listener<JSONArray>() {
//             //Log.d("JSONARRAY", "onResponse: " );
//
//            @Override
//            public void onResponse(JSONArray response) {
//
//                Log.d("JSONARRAY", "onResponse: " + " Test" );
//                for(int i=0; i < response.length(); i++){
//                  //  Log.d("JSONARRAY", "onResponse: " + response.length());
//                    try {
//                        JSONObject jsonobject=response.getJSONObject(i);
//                        Log.d("PROFILE", "onResponse: " + jsonbject.getInt("id"));
//                        BPid=jsonbject.getInt("id");
//                        BPName=jsonbject.getString("title");
//                        BPGrav=jsonbject.getDouble("id");
//                        BPTemp=jsonbject.getDouble("id");
//                        BPDesc=jsonbject.getString("title");
//
//
//                        Log.d("PROFILE", "onResponse" + BPDesc );
//                        profileArrayList1.add(new profile(1,"Guiness",2,70,"Description"));
//                        profileArrayList1.add(new profile(BPid,BPName,BPGrav,BPTemp,"Description"));
//
//                        GetProfileValue(); //< must have this method to get the data

//        //----------------------------------
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//                  GetArrayValue(); //< must have this method to get the data
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
//        queue.add(jsonArrayRequest);






    }
//< The device information will be requested
    public void GetDypInfo(){

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
   //-----------------------------------------------------------------------------------------------


    }
    public void setToolbarData() {
        GetDypInfo();

        refreshToolBar(10000);
    }
    public void refreshToolBar(int milliseconds) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // do updates
                //this.run();
                setToolbarData();

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
        Refresh(20000);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
      //  String text = parent.getItemAtPosition(position).toString();
       // Toast.makeText(parent.getContext(), "It happening", Toast.LENGTH_SHORT).show();
        profile p = (profile) parent.getSelectedItem();
        ProfileId = p.getId();
        brewName = p.getName();
        //Toast.makeText(this, "Selected " + ProfileId, Toast.LENGTH_LONG).show();
        ToastProfileData(p);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void GetProfileValue(){
        Log.d("test",profileArrayList1.get(0).getName());
        arrayAdapter1 = new ArrayAdapter<profile>(this,
                android.R.layout.simple_spinner_item, profileArrayList1);
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter1);
        spinner.setOnItemSelectedListener(this);

    }
    public void ToastProfileData(profile p){
        String name = p.getName();
        double grav = p.getGravity();
        double temp = p.getTemperature();
        String Desc = p.getDescription();

        String ProfileData = "Beer Name: " + name + "\nSpec gravity: " + grav + "\n Temp: "
                + temp + "\nDesc: ";
        Toast.makeText(this, ProfileData, Toast.LENGTH_LONG).show();
    }
}