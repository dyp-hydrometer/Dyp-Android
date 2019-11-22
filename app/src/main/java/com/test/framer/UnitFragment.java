package com.test.framer;
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
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.test.framer.util.Prefs;
import android.widget.Toast;
import com.test.framer.model.TimeElapsed;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import static com.test.framer.MainActivity.url1 ;
import static com.test.framer.MainActivity.urlInterval;
import static com.test.framer.MainActivity.portNum ;
import static com.test.framer.MainActivity.stDypId ;
import static com.test.framer.MainActivity.stDypIP ;
import static com.test.framer.MainActivity.interval;
import static com.test.framer.model.TimeElapsed.minToHMS;
import static com.test.framer.model.regexIP.isValidIP;

/**
 * Setting page, set unit, IP, request interval
 */
public class UnitFragment extends Fragment implements View.OnClickListener{
    private Prefs prefs;
    RequestQueue queue;
    private EditText addressET;
    private EditText dypIdET;
    private EditText intervalET;
    private RequestQueue requestQueue;
    private Button saveSetiings;
    private Button cancelSetiings;
    public static String tempUnit="C";
    public static String gravUnit=null;
    private String regexNum ="[0-9]+";
    private double minTime;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_unit , container, false);
        prefs = new Prefs(getActivity());

        // create a new instance of the singleton if it does not exist
        queue = DypSingletonAPI.getInstance(getActivity().getApplicationContext())
                .getRequestQueue();
                v.findViewById(R.id.rbSpecificGravity).setOnClickListener(this);
                v.findViewById(R.id.rbBrix).setOnClickListener(this);
                v.findViewById(R.id.rbPlato).setOnClickListener(this);
                v.findViewById(R.id.rbCelcius).setOnClickListener(this);
                v.findViewById(R.id.rbFahrenheit).setOnClickListener(this);

                saveSetiings = (Button)v.findViewById(R.id.saveSetting);
                cancelSetiings =(Button)v.findViewById(R.id.cancelSetting);

                 addressET =   v.findViewById(R.id.Dypaddress);
                 addressET.setText(stDypIP);
                 dypIdET =  v.findViewById(R.id.txtId);
                 dypIdET.setText(stDypId);
                 intervalET =  v.findViewById(R.id.txtinterval);
                 minTime =  interval * TimeElapsed.millis_min;
                 intervalET.setText(String.valueOf((int)Math.ceil(minTime)));

        /**
         * Handle the save button statement
         */
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
                         //Log.d("ErrorPut", " URL interval " + urlInterval);
                         postInternal();
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

    /**
     * Change the data request interval(time), send a POST request to the PI
     */
    public void postInternal(){
        requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlInterval.toString().trim(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject objres=new JSONObject(response);
                     Log.d("VOLLEYO :","In response");
                   // Toast.makeText(getContext(),objres.toString(),Toast.LENGTH_LONG).show();
                } catch (JSONException e) {

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               Log.d("VOLLEYO", error.getMessage());
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody(){
                try {
                    String T = minToHMS((int) interval); //< covert the time into H:M:S format
                    return T.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    //Log.v("Unsupported Encoding while trying to get the bytes", data);
                    return null;
                }
            }
        };
        requestQueue.add(stringRequest);
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
