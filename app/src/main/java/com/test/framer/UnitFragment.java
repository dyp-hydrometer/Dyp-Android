package com.test.framer;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.test.framer.util.Prefs;
import android.widget.Toast;


public class UnitFragment extends Fragment implements View.OnClickListener{
    final private String LOG ="Unit";
    private Prefs prefs;
    private RadioGroup Gravityrg;
    private RadioButton gravityRb;
    private RadioButton brixRb;
    private RadioButton platoRb;
    public static String tempUnit="C";
    public static String gravUnit=null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_unit , container, false);

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

    }
}
