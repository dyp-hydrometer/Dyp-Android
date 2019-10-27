package com.test.framer;

import android.os.Handler;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Random;
import static com.test.framer.UnitFragment.gravUnit;
import static com.test.framer.UnitFragment.tempUnit;
import com.test.framer.model.Gravity;
import com.test.framer.model.temperature;

import javax.xml.transform.Templates;


public class homeFragment extends Fragment {

    private TextView gravTexview;
    private TextView tempTextView;
    private TextView unitGrav;
    private TextView unitTemp;
    private Button refreshBtn;
    RequestQueue queue;
    Random random = new Random();
    private int num=0;
    private double grav=0;   // gravity of the Hydrometer
    private double temp=0;
    private static Gravity G;
    private static temperature T;





    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.home_page , container, false);
        gravTexview = v.findViewById(R.id.gravityValue);
        tempTextView = v.findViewById(R.id.tempValue);
        refreshBtn = v.findViewById(R.id.refresh);
        unitGrav = v.findViewById(R.id.gravityUnit);
        unitTemp = v.findViewById(R.id.tempUnit);

        // create a new instance of the singleton if it does not exist
        queue = DypSingletonAPI.getInstance(getActivity().getApplicationContext())
                .getRequestQueue();

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
                            grav=jsonbject.getDouble("id");
                            temp=jsonbject.getDouble("id");

                            //---------------------------- new
                            unitGrav.setText(gravUnit);
                            unitTemp.setText(tempUnit);
                            grav = Gravity.prefGravUnit(grav,gravUnit);
                            temp = temperature.prefTemprature(temp,tempUnit);


                            gravTexview.setText(String.format("%.2f",grav));
                            tempTextView.setText(String.format("%.2f", temp));
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

//------------------------------------------------------------------------

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


            refreshBtn.setOnClickListener(new View.OnClickListener(){
              @Override
              public void onClick(View v){
                  homeFragment hf = new homeFragment();
                  FragmentManager manager = getFragmentManager();
                  manager.beginTransaction()
                  .replace(R.id.fragment_container,new homeFragment()).commit();
                         // new homeFragment()).commit();
              }
            });



//            refreshHandler.postDelayed(runnable,1000 );
          // refreshHandler.run();

        return v;

       // return content(v);



        //gravTexview.setText(String.valueOf(6));
        //gravTexview.setText(String.valueOf(70));



    }
//    public void content(){
//        homeFragment hf = new homeFragment();
//        FragmentManager manager = getFragmentManager();
//        manager.beginTransaction()
//                .replace(R.id.fragment_container,new homeFragment()).commit();
//
//        Refresh(1000, v);
//        // onCreateView().getAutofillId();
//        // public Handler refreshHandler = new Handler();
//    }
//
//    public void Refresh(int milliseconds){
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                // do updates
//               content();
//            }
//
//        };
//        refreshHandler.postDelayed(runnable, milliseconds);
//
//    }


//    FragmantClass rSum = new FragmantClass();
//    getSupportFragmentManager().beginTransaction().remove(rSum).commit();








}
