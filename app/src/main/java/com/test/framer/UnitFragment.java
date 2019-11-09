package com.test.framer;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.test.framer.util.Prefs;
import android.widget.Toast;
import com.test.framer.model.TimeElapsed;

public class UnitFragment extends Fragment implements View.OnClickListener{
    final private String LOG ="Unit";
    private Prefs prefs;
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
    public static String stDypIP="";
    public static String stDypId="1";
    public static long interval;

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
                 addressET =  v.findViewById(R.id.Dypaddress);
                 dypIdET =  v.findViewById(R.id.txtId);
                 intervalET =  v.findViewById(R.id.txtinterval);

                saveSetiings.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                     stDypIP = addressET.getText().toString();
                     stDypId = dypIdET.getText().toString();


                     if(!stDypIP.isEmpty()){
                         prefs.savePiIP(stDypIP);
                         Toast.makeText( getActivity(), "Settings saved", Toast.LENGTH_LONG).show();
                     }
                     if(!stDypId.isEmpty()){
                         prefs.saveDypId(stDypId);
                     }
                     if(!(intervalET.getText().toString().isEmpty())){

                         interval = Long.valueOf(intervalET.getText().toString());
                         //convert to milliseconds
                         interval*=TimeElapsed.MINUTE_MILLIS;  // convert form minute to millisecond
                         prefs.saveRInterval(interval);
                     }
                    // prefs.savePiIP(stDypIP);
                    // Toast.makeText( getActivity(), "ip", Toast.LENGTH_LONG).show();

                    }
                });

//            cancelSetiings.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Toast.makeText( getActivity(), "canceling", Toast.LENGTH_LONG).show();
//
//                }
//            });


        return v;
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

//        switch(view.getId()) {
//
//            case R.id.saveSetting:
//                stDypIP = addressET.getText().toString();
//               // prefs.savePiIP (stDypIP);
//
//                Toast.makeText(getActivity(), "ip"+stDypIP, Toast.LENGTH_LONG).show();
//                break;
//
//            case R.id.cancelSetting:
//                Toast.makeText(getActivity(), "cancelling ", Toast.LENGTH_LONG).show();
//                break;
//        }



    }
}
