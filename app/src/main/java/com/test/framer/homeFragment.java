package com.test.framer;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.NotificationCompat;

import android.support.v7.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static android.support.v4.content.ContextCompat.getSystemService;
import static com.test.framer.UnitFragment.gravUnit;
import static com.test.framer.UnitFragment.tempUnit;
import static com.test.framer.MainActivity.url1 ;
import static com.test.framer.MainActivity.ProfileId;
import static com.test.framer.MainActivity.brewName;
import static com.test.framer.MainActivity.brewStatus;


import com.android.volley.toolbox.StringRequest;
import com.test.framer.model.TimeElapsed;
import com.test.framer.model.Gravity;
import com.test.framer.model.temperature;

public class homeFragment extends Fragment {
    private TextView txtTimeAgo;
    private TextView gravTexview;
    private TextView tempTextView;
    private TextView unitGrav;
    private TextView unitTemp;
    private Button refreshBtn;
    private Button startBtn;
    RequestQueue queue;
    Random random = new Random();
    private int num = 0;
    private double grav = 0;   // gravity of the Hydrometer
    private double temp = 0;
    private static Gravity G;
    private static temperature T;
    private long lastTimeGotData = System.currentTimeMillis();   // the late time you get the data in millisecond
    String timeAgo = "";


    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.home_page, container, false);
        gravTexview = v.findViewById(R.id.gravityValue);
        tempTextView = v.findViewById(R.id.tempValue);
        refreshBtn = v.findViewById(R.id.refresh);
        startBtn = v.findViewById(R.id.startBrew);
        unitGrav = v.findViewById(R.id.gravityUnit);
        unitTemp = v.findViewById(R.id.tempUnit);
        txtTimeAgo = v.findViewById(R.id.timelast);

        // create a new instance of the singleton if it does not exist
        queue = DypSingletonAPI.getInstance(getActivity().getApplicationContext())
                .getRequestQueue();

        if(brewStatus.equals("begin")) {
            startBtn.setBackgroundColor(0xFFFF0000);
            startBtn.setText("END BREWING");

        }else{
            startBtn.setBackgroundColor(getResources().getColor(R.color.colorGreen)); // red color
            startBtn.setText("START BREWING");
        }

        // Json Object request

        //  http://192.168.43.244:5000/api/hydrometers/2/last
        // "https://jsonplaceholder.typicode.com/todos/1", null,
//            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
//                    "https://jsonplaceholder.typicode.com/todos/1", null,
//                    new Response.Listener<JSONObject>() {
//                        @Override
//                        public void onResponse(JSONObject jsonbject) {
//                            try {
//                                num = random.nextInt(50) + 1;
//                                Log.d("JSON", "onResponse: " + jsonbject.get("data"));
//                                gravTexview.setText(String.valueOf(num));
//                               // tempTextView.setText(String.valueOf(jsonbject.getString("id")));
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    Log.d("Error", "onErrorResponse: " + error.getMessage());
//                }
//            });
//
//            queue.add(jsonObjectRequest);

//----------------------------mock API object------------------------
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                "https://jsonplaceholder.typicode.com/todos/1", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonbject) {
                        try {
                            // num = random.nextInt(50) + 1;    // generate the random number from
                            Log.d("JSON", "onResponse: " + jsonbject.getInt("id"));
                            grav = jsonbject.getDouble("id");
                            temp = jsonbject.getDouble("id");

                            //---------------------------- new
                            //lastTimeGotData = System.currentTimeMillis();
                            Log.d("URL", "onResponse" + url1);

                            unitGrav.setText(gravUnit);
                            unitTemp.setText(tempUnit);
                            grav = Gravity.prefGravUnit(grav, gravUnit);
                            temp = temperature.prefTemprature(temp, tempUnit);

                            gravTexview.setText(String.format("%.2f", grav));
                            tempTextView.setText(String.format("%.2f", temp));
                            //1573096170
                            timeAgo = TimeElapsed.getTimeAgo(lastTimeGotData);
                            txtTimeAgo.setText(timeAgo);
                            lastTimeGotData = System.currentTimeMillis(); // set the last update time
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


//----------------------------------------------


        // print the url here to chen


        // Json Array request


// Json Array request
        //---------Get the last data entry of the hydrometer, Specific gravity and temperature
//        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
//                "http://192.168.1.2:5000/api/hydrometers/2/data/last", null, new Response.Listener<JSONArray>() {
//             //Log.d("JSONARRAY", "onResponse: " );
//
//            @Override
//            public void onResponse(JSONArray response) {
//               // System.out.printf("testtsts");
//                Log.d("JSONARRAY", "onResponse: " + " Test" );
//                for(int i=0; i < response.length(); i++){
//                  //  Log.d("JSONARRAY", "onResponse: " + response.length());
//                    try {
//                        JSONObject jsonObject=response.getJSONObject(i);
//                        grav=jsonObject.getDouble("specific_gravity");
//                        temp=jsonObject.getDouble("temp");
//                      //  gravTexview.setText(String.valueOf(num));
//   //                   gravTexview.setText(String.valueOf(grav));
//   //                   tempTextView.setText(String.valueOf(temp));
////---------------------------- new
//                        unitGrav.setText(gravUnit);
//                        unitTemp.setText(tempUnit);
//                        grav = Gravity.prefGravUnit(grav,gravUnit);
//                        temp = temperature.prefTemprature(temp,tempUnit);
//
//
//                        gravTexview.setText(String.format("%.2f",grav));
//                        tempTextView.setText(String.format("%.2f", temp));
//        //----------------------------------
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
//        queue.add(jsonArrayRequest);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(brewStatus.equals("begin")) {
                    startBtn.setBackgroundColor(getResources().getColor(R.color.colorGreen)); // red color
                    startBtn.setText("START BREWING");
                    brewStatus="end";
                    Toast.makeText(getContext(), "Ending brew for " + brewName.toUpperCase(), Toast.LENGTH_LONG).show();

                }else{
                    startBtn.setBackgroundColor(0xFFFF0000);
                    startBtn.setText("END BREWING");
                    brewStatus="begin";
                    Toast.makeText(getContext(), "Starting brew for " + brewName.toUpperCase(), Toast.LENGTH_LONG).show();
                }
            //< send the profile id to the PI via Post request
                StringRequest postRequest= new StringRequest(Request.Method.POST,
                        "https://jsonplaceholder.typicode.com/todos/1",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String str) {
                                try {
                                    // num = random.nextInt(50) + 1;    // generate the random number from
                                    Log.d("POST", "POST " + ProfileId);


                                } catch (StackOverflowError e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("Error", "onErrorResponse: " + error.getMessage());
                            }
                }){
                @Override
                protected Map<String, String> getParams()
                {    Map<String, String> params = new HashMap<String, String>();
                    if(brewStatus.equals("begin")) {
                        params.put("id", String.valueOf(ProfileId));
                        params.put("status", "end");
                     }
                     else{
                        params.put("id", String.valueOf(ProfileId));
                        params.put("status", "begin");
                     }
                    return params;
                }
              };

                queue.add(postRequest);


            }
        });



            refreshBtn.setOnClickListener(new View.OnClickListener(){
              @Override
              public void onClick(View v){
                 // homeFragment hf = new homeFragment();
                  FragmentManager manager = getFragmentManager();
                  manager.beginTransaction()
                  .replace(R.id.fragment_container,new homeFragment()).commit();

              }
            });

//            refreshHandler.postDelayed(runnable,1000 );
          // refreshHandler.run();
        return v;
       // return content(v);
    }

    // Creates and displays a notification

//    private void addNotification() {
//
//        // Builds your notification
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,1)
//                .setSmallIcon(R.drawable.dyp)
//                .setContentTitle("My notification")
//                .setContentText("Much longer text that cannot fit one line...")
//                .setStyle(new NotificationCompat.BigTextStyle()
//                        .bigText("Much longer text that cannot fit one line..."))
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//    }



}
