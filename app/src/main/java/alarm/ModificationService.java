package alarm;

import mute.MuteDevice;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.util.Log;


import com.domainname.timetablemanager.R;

import data.DataHandler;


//Do not mute notification service
public class ModificationService extends IntentService {

    private DataHandler dataHandler;
    private SharedPreferences sharedPreferences;

    public ModificationService() {
        super("name3");
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // TODO Auto-generated method stub

        Bundle bundle = intent.getExtras();
        String id = bundle.getString("DATAZ");
        String day = bundle.getString("DAYZ");
        String startTime = bundle.getString("STARTTIMEZ");
        String endTime = bundle.getString("ENDTIMEZ");

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        MuteDevice muteDevice = new MuteDevice(getApplicationContext());
        muteDevice.cancelMute(id);   //cancelling mute for the particular course in the notification bar
        dataHandler = new DataHandler(getApplicationContext());
        dataHandler.openDatabase();


        boolean auto_mute_status = this.sharedPreferences.getBoolean(getString(R.string.pref_checkbox_key), false);
        boolean auto_vibrate_status = this.sharedPreferences.getBoolean(getString(R.string.pref_checkbox_key_2), false);
        //if checkbox status is true, set mut for subsequent course lecture time
        if (auto_mute_status) {
            //setting muting for subsequent after canceling the alarm sequence above...you mean mute sequence
            muteDevice.setSubsequentMuteOrVibrate(Integer.parseInt(id), day, Integer.parseInt(startTime), Integer.parseInt(endTime), "Mute");
            Log.d("Muting", "Device will be muted for subsequent time of lecture");
        } else if (auto_vibrate_status) {
            muteDevice.setSubsequentMuteOrVibrate(Integer.parseInt(id), day, Integer.parseInt(startTime), Integer.parseInt(endTime), "Vibrate");
        }
        cancelNotification(id);
    }

    private void cancelNotification(String id) {
        String s = Context.NOTIFICATION_SERVICE;
        NotificationManager mNM = (NotificationManager) getApplicationContext().getSystemService(s);
        mNM.cancel(Integer.parseInt(id));

    }

}
