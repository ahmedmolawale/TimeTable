package mute;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;


import com.ahmedmolawale.timetablemanager.R;

import data.DataHandler;
import data.ModelConstants;

public class DeviceBootMuteService extends IntentService {

    private DataHandler dataHandler;
    private SharedPreferences sharedPreferences;

    public DeviceBootMuteService() {
        super("name10");
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // TODO Auto-generated method stub


        // we need to know the status of the checkboxes first to work on
        // muting or vibrate

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean auto_mute_status = this.sharedPreferences.getBoolean(getString(R.string.pref_checkbox_key), false);
        boolean auto_vibrate_status = this.sharedPreferences.getBoolean(getString(R.string.pref_checkbox_key_2), false);

        Cursor cursor = dataHandler.retrieveAllCoursesForMutingDevice();
        MuteDevice muteDevice = new MuteDevice(getApplicationContext());
        if (auto_mute_status || auto_vibrate_status) {
            if (cursor.moveToFirst() == true) {
                cursor.moveToFirst();
                do {
                    String id = cursor.getString(cursor
                            .getColumnIndex(ModelConstants.ID));
                    String day = cursor.getString(cursor
                            .getColumnIndex(ModelConstants.DAY));
                    String startTimeInMins = cursor.getString(cursor
                            .getColumnIndex(ModelConstants.REAL_START_TIME_MINS));
                    String endTimeInMins = cursor.getString(cursor
                            .getColumnIndex(ModelConstants.REAL_END_TIME_MINS));

                    if (auto_mute_status) {
                        muteDevice.setMuteOrVibrate(Integer.parseInt(id), day, Integer.parseInt(startTimeInMins), Integer.parseInt(endTimeInMins), "Mute");
                    } else if (auto_vibrate_status) {
                        muteDevice.setMuteOrVibrate(Integer.parseInt(id), day, Integer.parseInt(startTimeInMins), Integer.parseInt(endTimeInMins), "Vibrate");
                    }
                } while (cursor.moveToNext());

            }

        }
    }
}
