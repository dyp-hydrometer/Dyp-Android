package com.test.framer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.test.framer.model.Gravity;
import com.test.framer.model.TimeElapsed;
import com.test.framer.model.profile.*;

import com.test.framer.model.adapter.RecyclerViewAdapter;
import com.test.framer.model.profile;

import java.util.ArrayList;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.test.framer.model.temperature;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Random;

import static com.test.framer.MainActivity.urlGetProfle;
import static com.test.framer.UnitFragment.gravUnit;
import static com.test.framer.UnitFragment.tempUnit;


public class BrewList extends AppCompatActivity {
    // recycle view
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    public ArrayList<profile> profileArrayList;
    private ArrayAdapter<profile> arrayAdapter;
    RequestQueue queue;

    private int BPid;
    private String BPName;
    private double BPGrav;
    private double BPTemp;
    private String BPDesc;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brew_list);
        // create a new instance of the singleton if it does not exist
        queue = DypSingletonAPI.getInstance(this.getApplicationContext())
                .getRequestQueue();

//        // set up the recycl view
          recyclerView = findViewById(R.id.recycleView);
          recyclerView.setHasFixedSize(true);  // make the size to fit right
//        // to show thinks linearly on the screen
          recyclerView.setLayoutManager(new LinearLayoutManager( this));
//
          profileArrayList = new ArrayList();

//        //TODO create a list of profile from the data base
//         //List<profile> profList = from API

        //----------------------------mock API object------------------------
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                "https://jsonplaceholder.typicode.com/todos/1", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonbject) {
                        try {

                            // num = random.nextInt(50) + 1;    // generate the random number from
                            Log.d("PROFILE", "onResponse: " + jsonbject.getInt("id"));
                            BPid=jsonbject.getInt("id");
                            BPName=jsonbject.getString("title");
                            BPGrav=jsonbject.getDouble("id");
                            BPTemp=jsonbject.getDouble("id");
                            BPDesc=jsonbject.getString("title");

                            //---------------------------- new
                            //lastTimeGotData = System.currentTimeMillis();
                          // Log.d("PROFILE", "onResponse" + BPDesc );
                           profileArrayList.add(new profile(1,"Guiness",2,70,"Description"));
                           profileArrayList.add(new profile(BPid,BPName,BPGrav,BPTemp,"Description"));

                            GetArrayValue(); //< must have this method to get the data


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
       // profileArrayList.add(new profile(1,"Guiness",2,70,"Description"));



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
//                        JSONObject jsonObject=response.getJSONObject(i);
//                            BPid=jsonObject.getInt("id");
//                            BPName=jsonObject.getString("title");
//                            BPGrav=jsonObject.getDouble("id");
//                            BPTemp=jsonObject.getDouble("id");
//                            BPDesc=jsonObject.getString("title");
//

//                         profileArrayList.add(new profile(BPid,BPName,BPGrav,BPTemp,"Description"));
//
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


        // generate random data for the profile Array list

//            for(int count=0; count<10; count++ ){
//            Log.d ("LIST", "onCreate"+ "Hello");
//            profileArrayList.add(new profile(count,"Beer " + count,"c","80", "Description"));
//        }
//
//
//







    }
    private void GetArrayValue(){
        Log.d("test",profileArrayList.get(0).getName());
        //< Set up adapter
        recyclerViewAdapter = new RecyclerViewAdapter(this,profileArrayList);
        recyclerView.setAdapter(recyclerViewAdapter);
    }
}
