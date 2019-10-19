package com.test.framer.util;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import java.lang.String;

public class Prefs {
    private SharedPreferences preferences;


    public Prefs(Activity activity) {
        this.preferences = activity.getPreferences(Context.MODE_PRIVATE);
    }
    public void saveTempUnit(String newTUnit) {
        String tUnit = preferences.getString("temp_unit", "F");

        if (tUnit != newTUnit) {
            // Save if the temperature unit if it is different
            preferences.edit().putString("temp_unit", newTUnit).apply();
        }

    }

    public void saveGravityUnit(String newGUnit) {
        String gUnit = preferences.getString("g_unit", "Brix");

        if (gUnit != newGUnit) {
            // Save the new gravity unit if it is different
            preferences.edit().putString("g_unit", newGUnit).apply();
        }

    }

    public String getDefaultTemp() {
        return String.valueOf(preferences.edit().putString("temp_unit","F"));
    }
    public String getDefaultGravity() {
        return String.valueOf(preferences.edit().putString("g_unit","Brix"));
    }
}
