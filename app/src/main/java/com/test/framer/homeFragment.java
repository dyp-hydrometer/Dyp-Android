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
import java.util.Date;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Random;


public class homeFragment extends Fragment {
    final Handler refreshHandler = new Handler();
    private TextView gravTexview;
    private TextView tempTextView;
    private Button refreshBtn;
    RequestQueue queue;
    Random random = new Random();
    private int num=0;




    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.home_page , container, false);
        gravTexview = v.findViewById(R.id.gravityValue);
        tempTextView = v.findViewById(R.id.tempValue);
        refreshBtn = v.findViewById(R.id.refresh);

        // create a new instance of the singleton if it does not exist
        queue = DypSingletonAPI.getInstance(getActivity().getApplicationContext())
                .getRequestQueue();

        // Json Object request


            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                    "https://jsonplaceholder.typicode.com/todos/1", null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonbject) {
                            try {
                                num = random.nextInt(50) + 1;
                                Log.d("JSON", "onResponse: " + jsonbject.getString("id"));
                                gravTexview.setText(String.valueOf(num));
                               // tempTextView.setText(String.valueOf(jsonbject.getString("id")));
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
    public View content(View v){
        homeFragment hf = new homeFragment();
        FragmentManager manager = getFragmentManager();
        manager.beginTransaction()
                .replace(R.id.fragment_container,new homeFragment()).commit();

        return Refresh(1000, v);
        // onCreateView().getAutofillId();
        // public Handler refreshHandler = new Handler();
    }

    public View Refresh(int milliseconds, final View v){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // do updates
               content(v);
            }

        };
        refreshHandler.postDelayed(runnable, milliseconds);
        return v;
    }
//    FragmantClass rSum = new FragmantClass();
//    getSupportFragmentManager().beginTransaction().remove(rSum).commit();








}
