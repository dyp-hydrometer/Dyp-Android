package com.test.framer.util;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import java.lang.String;

/**
 * Save startup data in the shared preference(XML file)
 */

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
        return String.valueOf(preferences.getString("temp_unit","F"));
    }
    public String getDefaultGravity() {
        return String.valueOf(preferences.getString("g_unit","Brix"));
    }

    public void savePiIP(String newIp) {
        String PiIP = preferences.getString("ip_address", "127.0.0.1");
        if ( PiIP!= newIp) {
            // Save the new gravity unit if it is different
            preferences.edit().putString("ip_address", newIp).apply();
        }
    }
    public String getPiIP() {
        return String.valueOf(preferences.getString("ip_address","127.0.0.1"));
    }

    public void saveDypId(String newDypId) {
        String DypId = preferences.getString("dyp_id", "1");

        if ( DypId!= newDypId) {
            // Save the new gravity unit if it is different
            preferences.edit().putString("dyp_id", newDypId).apply();
        }

    }
    public String getDypId() {
       // return String.valueOf(preferences.getString("dyp_id","1"));
        return String.valueOf(preferences.getString("dyp_id","1"));
    }
    public void saveRInterval(long newRInterval) {
        long Rinterval = preferences.getLong("interval", 40000);

        if ( Rinterval!= newRInterval) {
            // Save the new gravity unit if it is different
            preferences.edit().putLong("interval", newRInterval).apply();
        }

    }
    public long getInterval() {
        return preferences.getLong("interval",40000);
    }

    public void saveStatus(String Newstatus) {
        String status = preferences.getString("status", "true");

        if ( status!= Newstatus) {
            // Save the new gravity unit if it is different
            preferences.edit().putString("status", Newstatus).apply();
        }

    }
    public String getStatus() {
        return preferences.getString("status","true");
    }

    /**
     * save the origin gravity
     * @param NewOG
     */
    public void saveOG(String NewOG) {
        String og = preferences.getString("og", "0");
        if ( og!= NewOG) {
            // Save the new gravity unit if it is different
            preferences.edit().putString ("og", NewOG).apply();
        }
    }
    public String getOG() {
        return preferences.getString("og","0");
    }

}
