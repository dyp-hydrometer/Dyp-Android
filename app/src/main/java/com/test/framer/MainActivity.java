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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Random;

/**
 * <h1>Android Application for DYP Bluetooth hydrometer </h1>
 * <p>
 * MainActivity
 * @author Boris
 * @since 2019-11-01
 * @Version 1.0
 *
 */

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    final Handler refreshHandler = new Handler();
    final Handler refreshBattHandler = new Handler();
    private Prefs pref;
    private double batPercent=0.0;
    RequestQueue queue;
    private TextView batt;
    Random random = new Random();
    public static StringBuffer url1,urlGetProfle,urlBattery, urlInterval,urlStart,urlProfile;
    public static final String portNum = "5000" ;
    public static String stDypIP="";
    public static String stDypId="";
    static int ProfileId;
    static String brewName;
    static Double brewSetGrav;
    static Double lastGrav=0.0;
    static Double lastTemp=0.0;
    static String brewStatus = "idle";
    static Double o_gravity=0.0;
    public static long interval;
    public static String Pid="0";
    private Spinner spinner;
    private RequestQueue requestqueue;
    public ArrayList<profile> profileArrayList1;
    private ArrayAdapter<profile> arrayAdapter1;
    private int BPid;
    private String BPName;
    private double BPGrav;
    private double BPTemp;
    private String BPDesc;

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
     // get the all data save in the shared preferences
        pref = new Prefs(this);
        gravUnit = pref.getDefaultGravity();
        tempUnit = pref.getDefaultTemp();
        stDypIP =pref.getPiIP();
        stDypId =pref.getDypId();
        interval=pref.getInterval();
        brewStatus = pref.getStatus();
        o_gravity = Double.valueOf(pref.getOG());

        // build the url1(last reading) for the api
        url1 = new StringBuffer("http://");
        url1.append(stDypIP);
        url1.append(":"+portNum+"/api/hydrometers/");
        url1.append(stDypId+"/data/last");  //< append the Dyp id

        // build the url for beer profile description
        urlGetProfle = new StringBuffer("http://");
        urlGetProfle.append(stDypIP);
        urlGetProfle.append(":"+portNum+"/api/profiles/");

        // build the url for beer Battery level request
        urlBattery = new StringBuffer("http://");
        urlBattery.append(stDypIP);
        urlBattery.append(":"+portNum+"/api/hydrometers/");
        urlBattery.append(stDypId+"/battery");  //< append the Dyp id

        // build the url for beer Battery level request
        urlBattery = new StringBuffer("http://");
        urlBattery.append(stDypIP);
        urlBattery.append(":"+portNum+"/api/hydrometers/");
        urlBattery.append(stDypId+"/battery");  //< append the Dyp id

        // build the url for request interval
        urlInterval = new StringBuffer("http://");
        urlInterval.append(stDypIP);
        urlInterval.append(":"+portNum+"/api/hydrometers/");
        urlInterval.append(stDypId+"/interval");  //< append the Dyp id

        // build the url for request active
        urlStart = new StringBuffer("http://");
        urlStart.append(stDypIP);
        urlStart.append(":"+portNum+"/api/hydrometers/");
        urlStart.append(stDypId+"/active");  //< append the Dyp id

        // build the url for request active
        urlProfile = new StringBuffer("http://");
        urlProfile.append(stDypIP);
        urlProfile.append(":"+portNum+"/api/hydrometers/");
        urlProfile.append(stDypId+"/profile");  //< append the Dyp id

         //< set the spinner on the toolbar
        spinner = findViewById(R.id.spinProfile);
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
//            case R.id.nav_brew_profile:
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                        new BrewProfileFragment()).commit();
//                break;
            case R.id.nav_profile_list:
               // Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
                Intent listBrewIntent = new Intent(MainActivity.this, BrewList.class);
                startActivity(listBrewIntent);
                break;
            case R.id.nav_calibrate:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new CalibrateFragment()).commit();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Request the list of beer profile from the server(Raspberrypi) via REST IPA call and display in the recycleview in the app
     */
    public void GetProfileList(){
        //---------Get the last data entry of the hydrometer, Specific gravity and temperature
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlGetProfle.toString().trim()
                , null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int i=0; i < response.length(); i++){
                    try {
                        JSONObject jsonobject=response.getJSONObject(i);
                        BPid=jsonobject.getInt("id");
                        BPName=jsonobject.getString("name");
                        BPGrav=jsonobject.getDouble("req_gravity");
                        BPTemp=jsonobject.getDouble("req_temp");
                        BPDesc=jsonobject.getString("description");
                        Log.d("PROFILE", "onResponse" + BPName );
                        profileArrayList1.add(new profile(BPid,BPName,BPGrav,BPTemp,"Description"));
                        GetProfileValue(); //< must have this method to get the data
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(jsonArrayRequest);

    }

    /**
     * Request the battery level of the DYP from the PI
     */
    public void GetDypInfo(){
        // ----------------------------mock API object------------------------
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                urlBattery.toString().trim(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonbject) {
                        try {
                            batPercent = jsonbject.getDouble("battery");
                            batt.setText(String.valueOf(batPercent));
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
        refreshToolBar(interval+40);
    }

    public void refreshToolBar(Long milliseconds) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // do updates
                setToolbarData();
            }
        };
        refreshBattHandler.postDelayed(runnable, milliseconds);
    }
    public void contents() {
         getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new homeFragment()).commit();
        Refresh(interval);
    }

    /**
     * wait a x amount of time before infating the home screen
     * @param milliseconds the length between request
     */
    public void Refresh(Long milliseconds) {
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

//    @Override
//    public void onPause(){
//        refreshHandler.removeMessages(0);
//        super.onPause();
//    }
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     *  save the selected profile in the drop down menu
     * @param parent
     * @param view
     * @param position position of the item selected
     * @param l
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
        profile p = (profile) parent.getSelectedItem();
        ProfileId = p.getId();
        Pid = String.valueOf(ProfileId);
        brewSetGrav = p.getGravity();
        brewName = p.getName();
        ToastProfileData(p);
        postProfile();
    }

    public void postProfile(){
        requestqueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlProfile.toString().trim(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                   JSONObject objres=new JSONObject(response);
                    Toast.makeText(MainActivity.this,objres.getString("error") ,Toast.LENGTH_LONG).show();
                } catch (JSONException e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.d("VOLLEYO", error.getMessage());
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody(){
                try {
                    return Pid.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    //Log.v("Unsupported Encoding while trying to get the bytes", data);
                    return null;
                }
            }
        };
        requestqueue.add(stringRequest);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    /**
     * Connect the Adapter to the brew profile and display it in the spinner
     */
    private void GetProfileValue(){
        arrayAdapter1 = new ArrayAdapter<profile>(this,
                android.R.layout.simple_spinner_item, profileArrayList1);
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter1);
        spinner.setOnItemSelectedListener(this);
    }

    /**
     * Toast more information about the beer profile selected
     * @param p the beer profile object
     */
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