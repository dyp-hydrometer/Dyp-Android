package com.test.framer;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.test.framer.util.Prefs;
import android.widget.Toast;
import com.test.framer.model.TimeElapsed;
import com.test.framer.model.regexIP;

import java.util.HashMap;
import java.util.Map;

import static com.test.framer.MainActivity.ProfileId;
import static com.test.framer.MainActivity.brewStatus;
import static com.test.framer.MainActivity.url1 ;
import static com.test.framer.MainActivity.portNum ;
import static com.test.framer.MainActivity.stDypId ;
import static com.test.framer.MainActivity.stDypIP ;
import static com.test.framer.MainActivity.interval;
import static com.test.framer.model.regexIP.isValidIP;


public class UnitFragment extends Fragment implements View.OnClickListener{
    final private String LOG ="Unit";
    private Prefs prefs;
    RequestQueue queue;
    private RadioGroup Gravityrg;
    private RadioButton gravityRb;
    private RadioButton brixRb;
    private RadioButton platoRb;
    private EditText addressET;
    private EditText dypIdET;
    private EditText intervalET;

    private Button saveSetiings;
    private Button cancelSetiings;
    public static String tempUnit="C";
    public static String gravUnit=null;
    private String regexNum ="[0-9]+";
    private String regexIP = "[0-9]+";
    private double minTime;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_unit , container, false);
        prefs = new Prefs(getActivity());
        //Gravityrg =
              //  v.findViewById(R.id.rgGavity).setOnClickListener(this);
      // gravityRb =
               v.findViewById(R.id.rbSpecificGravity).setOnClickListener(this);
       // brixRb =
                v.findViewById(R.id.rbBrix).setOnClickListener(this);
        //platoRb =
                v.findViewById(R.id.rbPlato).setOnClickListener(this);

                v.findViewById(R.id.rbCelcius).setOnClickListener(this);

                v.findViewById(R.id.rbFahrenheit).setOnClickListener(this);

//                v.findViewById(R.id.Dypaddress).setOnClickListener(this);
//                v.findViewById(R.id.txtId).setOnClickListener(this);
//                v.findViewById(R.id.txtinterval).setOnClickListener(this);
//
                saveSetiings = (Button)v.findViewById(R.id.saveSetting);
                cancelSetiings =(Button)v.findViewById(R.id.cancelSetting);

                //cancelSetiings.setOnClickListener(this);
                 addressET =   v.findViewById(R.id.Dypaddress);
                 addressET.setText(stDypIP);
                 dypIdET =  v.findViewById(R.id.txtId);
                 dypIdET.setText(stDypId);
                 intervalET =  v.findViewById(R.id.txtinterval);
                 minTime =  interval * TimeElapsed.millis_min;
                 Log.d("MIN",String.valueOf(TimeElapsed.millis_min));
                 intervalET.setText(String.valueOf(minTime));

                saveSetiings.setOnClickListener(new View.OnClickListener() {
                    @Override


                    public void onClick(View view) {
                        //< Data Validation
                        if(isValidIP(addressET.getText().toString().trim())== false) {
                            addressET.setError("Invalid IP address");
                            return;
                        }
                        if(TextUtils.isEmpty(addressET.getText().toString())) {
                            addressET.setError("IP is required");
                            return;
                        }
                        if(TextUtils.isEmpty(dypIdET.getText().toString())) {
                            dypIdET.setError("DYP id is required");
                            return;
                        }
                        if(TextUtils.isEmpty(intervalET.getText().toString())) {
                            intervalET.setError("DYP interval required");
                            return;
                        }
                        if(!dypIdET.getText().toString().trim().matches(regexNum)) {
                            dypIdET.setError("must be a number");
                            return;
                        }
                        if(!intervalET.getText().toString().trim().matches(regexNum)){
                            intervalET.setError("must be a number");
                            return;
                        }

                     if(!addressET.getText().toString().isEmpty() && !dypIdET.getText().toString().isEmpty()){
                         stDypIP = addressET.getText().toString().trim();
                         stDypId = dypIdET.getText().toString().trim();
                         prefs.savePiIP(stDypIP);
                         prefs.saveDypId(stDypId);
                         // build the url1 for the api
                         String temp = "http://"+stDypIP+":"+portNum+"/api/hydrometers/"+stDypId+"/data/last";
                         url1.replace(0,url1.length(),temp);
                     }
                     else if(!addressET.getText().toString().isEmpty()){
                         stDypIP = addressET.getText().toString().trim();
                         prefs.savePiIP(stDypIP);

                         // build the url1 for the api
                         String temp = "http://"+stDypIP+":"+portNum+"/api/hydrometers/"+stDypId+"/data/last";

                         url1.replace(0,url1.length(),temp);


                         Toast.makeText( getActivity(), "Settings saved", Toast.LENGTH_LONG).show();
                         Log.d("URL1", url1.toString() );

                     }
                     else if(!dypIdET.getText().toString().isEmpty()){

                         stDypId = dypIdET.getText().toString().trim();
                         prefs.saveDypId(stDypId);
                         // build the url1 for the api
                         String temp = "http://"+stDypIP+":"+portNum+"/api/hydrometers/"+stDypId+"/data/last";

                         url1.replace(0,url1.length(),temp);
                     }

                     if(!(intervalET.getText().toString().isEmpty())){

                         interval = Long.valueOf(intervalET.getText().toString().trim());
                         //convert to milliseconds
                         interval*=TimeElapsed.MINUTE_MILLIS;  // convert form minute to millisecond
                         prefs.saveRInterval(interval);
                     }
                        //go back to the home page
                        FragmentManager manager = getFragmentManager();
                        manager.beginTransaction().replace(R.id.fragment_container,new homeFragment()).commit();


                    }
                });

            cancelSetiings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText( getActivity(), "back to home", Toast.LENGTH_LONG).show();
                    //go back to the home page
                    FragmentManager manager = getFragmentManager();
                    manager.beginTransaction().replace(R.id.fragment_container,new homeFragment()).commit();


                }
            });


        return v;
    }

    public void postInternal(){
        //< send the interval of receiving data to the Pi(server)  via Post request
        StringRequest postRequest= new StringRequest(Request.Method.POST,
                "https://jsonplaceholder.typicode.com/todos/1",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String str) {
                        try {
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
            {    Map<String, String> params = new HashMap<String,String>();
                params.put("interval",String.valueOf(interval));
                return params;
            }
        };

        queue.add(postRequest);
    }
    @Override
    public void onClick(View view) {
        prefs = new Prefs(getActivity());
        boolean checked = ((RadioButton) view).isChecked();
        // save the unit selected
        switch(view.getId()){

            case R.id.rbSpecificGravity:
                prefs.saveGravityUnit("SG");
                gravUnit="SG";
                Toast.makeText(getActivity(), "Specific Gravity", Toast.LENGTH_LONG ).show();
                break;

            case R.id.rbBrix:
                prefs.saveGravityUnit("Brix");
                gravUnit="Brix";
                Toast.makeText(getActivity(), "Brix", Toast.LENGTH_LONG ).show();
                break;
            case R.id.rbPlato:
                prefs.saveGravityUnit("Plato");
                gravUnit="Plato";
                Toast.makeText(getActivity(), "Plato", Toast.LENGTH_LONG ).show();
                break;
        }
        switch(view.getId()) {

            case R.id.rbCelcius:
                prefs.saveTempUnit("C");
                tempUnit="C";
                Toast.makeText(getActivity(), "Celsius", Toast.LENGTH_LONG).show();
                break;

            case R.id.rbFahrenheit:
                prefs.saveTempUnit("F");
                tempUnit="F";
                Toast.makeText(getActivity(), "Fahrenheit", Toast.LENGTH_LONG).show();
                break;
        }





    }
}
