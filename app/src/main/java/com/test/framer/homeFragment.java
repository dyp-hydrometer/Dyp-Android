package com.test.framer;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import static android.support.v4.content.ContextCompat.getSystemService;
import static com.test.framer.MainActivity.o_gravity;
import static com.test.framer.MainActivity.urlStart;
import static com.test.framer.UnitFragment.gravUnit;
import static com.test.framer.UnitFragment.tempUnit;
import static com.test.framer.MainActivity.url1 ;
import static com.test.framer.MainActivity.brewName;
import static com.test.framer.MainActivity.brewStatus;
import static com.test.framer.MainActivity.brewSetGrav;
import static com.test.framer.MainActivity.lastGrav;
import static com.test.framer.MainActivity.lastTemp;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.test.framer.model.TimeElapsed;
import com.test.framer.model.Gravity;
import com.test.framer.model.temperature;
import com.test.framer.util.Prefs;

/**
 *  Home page, refresh automatically at a set interval
 */

public class homeFragment extends Fragment {
    //< declare all GUI element
    private TextView txtTimeAgo;
    private TextView gravTexview;
    private TextView tempTextView;
    private TextView unitGrav;
    private TextView unitTemp;
    private Button refreshBtn;
    private Button startBtn;
    private TextView ABVTxtVal;

    RequestQueue queue;
    private Prefs preference;
    private double grav = 0;   // gravity of the Hydrometer
    private double temp = 0;
    private long lastTimeGotData = System.currentTimeMillis();   // the late time you get the data in millisecond
    String timeAgo = "";
    private RequestQueue requestQ;

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
        ABVTxtVal = v.findViewById(R.id.ABVValue);

        preference = new Prefs(getActivity());
        // create the channel id for notifications
        createNotificationChannel();
        // create a new instance of the singleton if it does not exist
        queue = DypSingletonAPI.getInstance(getActivity().getApplicationContext())
                .getRequestQueue();

        if(brewStatus.equals("true")) {
            startBtn.setBackgroundColor(0xFFFF0000);
            startBtn.setText("END BREWING");

        }else{
            startBtn.setBackgroundColor(getResources().getColor(R.color.colorGreen)); // red color
            startBtn.setText("START BREWING");
        }
        // pop up a notification when the brew is completed
        if(lastGrav == brewSetGrav && brewStatus == "true"){
            addNotification();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url1.toString().trim(), null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject jsonbject) {
                        try {
                            lastGrav = jsonbject.getDouble("specific_gravity");
                            lastTemp = jsonbject.getDouble("temp");
                            unitGrav.setText(gravUnit);
                            unitTemp.setText(tempUnit);
                            ABVTxtVal.setText(String.valueOf(Gravity.ABV(o_gravity,lastGrav)));
                            grav = Gravity.prefGravUnit(lastGrav, gravUnit);
                            temp = temperature.prefTemprature(lastTemp, tempUnit.trim());
                            Log.d("URL", "Tem  " + temp + " " + tempUnit.trim());
                            gravTexview.setText(String.format("%.2f", grav));
                            tempTextView.setText(String.format("%.2f", temp));
                            timeAgo = TimeElapsed.getTimeAgo(lastTimeGotData);
                            txtTimeAgo.setText(timeAgo);

                            lastTimeGotData = System.currentTimeMillis(); // set the last update time
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ErrorHome", "onErrorResponse: " + error.getMessage());
            }
        });

        queue.add(jsonObjectRequest);

/**
 * Handle the start button
 */
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(brewStatus.equals("true")) {
                    startBtn.setBackgroundColor(getResources().getColor(R.color.colorGreen)); // red color
                    startBtn.setText("START BREWING");
                    brewStatus="false";

                    ABVTxtVal.setText(String.valueOf(Gravity.ABV(o_gravity,lastGrav)));
                    o_gravity = 0.0;
                    preference.saveOG(String.valueOf(o_gravity));
                    preference.saveStatus(brewStatus);

                    Toast.makeText(getContext(), "Ending brew for " + brewName.toUpperCase(), Toast.LENGTH_LONG).show();
                }else{
                    startBtn.setBackgroundColor(0xFFFF0000);
                    startBtn.setText("END BREWING");
                    brewStatus="true";
                    o_gravity = lastGrav;
                    preference.saveOG(String.valueOf(o_gravity));
                    preference.saveStatus(brewStatus);

                    Toast.makeText(getContext(), "Starting brew for " + brewName.toUpperCase(), Toast.LENGTH_LONG).show();
                }

                requestQ = Volley.newRequestQueue(getContext());
                StringRequest stringRequest = new StringRequest(Request.Method.POST, urlStart.toString().trim(), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject objres=new JSONObject(response);
                        } catch (JSONException e) {

                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Log.d("VOLLEYO", error.getMessage());
                    }
                }) {
                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8";
                    }
                    @Override
                    public byte[] getBody(){
                        try {
                            return brewStatus.getBytes("utf-8");
                        } catch (UnsupportedEncodingException uee) {
                            //Log.v("Unsupported Encoding while trying to get the bytes", data);
                            return null;
                        }
                    }
                };
               requestQ.add(stringRequest);
            }
        });

        /**
         *  Handle the reflesh button
         */
            refreshBtn.setOnClickListener(new View.OnClickListener(){
              @Override
              public void onClick(View v){
                 // homeFragment hf = new homeFragment();
                  FragmentManager manager = getFragmentManager();
                  manager.beginTransaction()
                  .replace(R.id.fragment_container,new homeFragment()).commit();

              }
            });
        return v;
    }

    /**
     *  Creates and displays a notification
     */
    private void addNotification() {

        // Builds your notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(),"lemubitA")
                .setSmallIcon(R.drawable.dyp)  // logo
                .setContentTitle("Brew complete")
                .setContentText("Specific gravity reached")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getContext());

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(1, builder.build());
    }
    /**
     *  Create the notification channel
     */
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("lemubitA", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(getContext(),NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


}
