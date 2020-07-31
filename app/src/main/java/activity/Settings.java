package activity;


import alarm.Alarm;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;


import com.ahmedmolawale.timetablemanager.R;

import data.DataHandler;
import data.ModelConstants;

import mute.MuteDevice;

public class Settings extends PreferenceActivity {
    private Preference cancelAlarmPreference;
    private CheckBoxPreference checkBoxPreferenceForAutoVibrate;
    private CheckBoxPreference checkBoxPreferenceForAutomute;
    private Preference chooseNotificationPreference;
    private DataHandler dataHandler;
    private Preference deleteAllDataPreference;
    private SharedPreferences sharedPreferences;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);
        this.dataHandler = new DataHandler(this);
        this.dataHandler.openDatabase();
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        this.checkBoxPreferenceForAutomute = (CheckBoxPreference) findPreference(getString(R.string.pref_checkbox_key));
        this.checkBoxPreferenceForAutoVibrate = (CheckBoxPreference) findPreference(getString(R.string.pref_checkbox_key_2));
        this.deleteAllDataPreference = findPreference(getString(R.string.pref_data_key));
        this.cancelAlarmPreference = findPreference(getString(R.string.pref_alarm_key));
        this.chooseNotificationPreference = findPreference(getString(R.string.pref_notification_key));
        this.checkBoxPreferenceForAutomute.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (newValue.toString().equals("true")) {
                    dataFetchToMuteOrVibrateDevice("Mute");
                } else {
                    dataFetchToUnmuteDevice();
                }
                return true;
            }
        });
        this.checkBoxPreferenceForAutoVibrate.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (newValue.toString().equals("true")) {
                    dataFetchToMuteOrVibrateDevice("Vibrate");
                } else {
                    dataFetchToUnmuteDevice();
                }
                return true;
            }
        });
        this.deleteAllDataPreference.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                confirmationDialog("Warning", "Sure you want to delete all Courses?", "Courses");
                return true;
            }
        });
        this.cancelAlarmPreference.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                confirmationDialog("Warning", "Sure you want to cancel all Notification?", "Alarms");
                return true;
            }
        });
        this.chooseNotificationPreference.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent("android.intent.action.RINGTONE_PICKER");
                intent.putExtra("android.intent.extra.ringtone.TYPE", 1);
                intent.putExtra("android.intent.extra.ringtone.TITLE", "Select a Notification");
                intent.putExtra("android.intent.extra.ringtone.EXISTING_URI", RingtoneManager.getActualDefaultRingtoneUri(Settings.this.getApplicationContext(), 2));
                startActivityForResult(intent, 5);
                return true;
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == -1) {
            Uri uri = (Uri) intent.getParcelableExtra("android.intent.extra.ringtone.PICKED_URI");
            if (uri != null) {
                RingtoneManager.setActualDefaultRingtoneUri(this, 2, uri);
            }
        }
    }

    public void confirmationDialog(String type, String message, final String executionType) {
        Builder builder = new Builder(this);
        builder.setTitle(type);
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setPositiveButton("Ok", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (executionType.equals("Courses")) {
                    deleteAllCourses();
                } else if (executionType.equals("Alarms")) {
                    deleteAllAlarms();
                }
            }
        });
        builder.setNegativeButton("No", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create().show();
    }

    public void deleteAllCourses() {
        this.dataHandler = new DataHandler(this);
        this.dataHandler.openDatabase();
        if (this.dataHandler.retrieveAllCoursesForAlarmDelete().moveToFirst()) {
            deleteAlarms();
            dataFetchToUnmuteDevice();
            this.dataHandler.deleteAllCourses();
            displayMessage("Message", "All courses deleted successfully.");
            return;
        }
        displayMessage("Message", "No Course registered yet!");
    }

    public void deleteAlarms() {
        this.dataHandler = new DataHandler(this);
        this.dataHandler.openDatabase();
        Cursor cursor = this.dataHandler.retrieveAllCoursesForAlarmDelete();
        cursor.moveToFirst();
        do {
            new Alarm().cancelAlarm(this, cursor.getString(cursor.getColumnIndex(ModelConstants.ID)));
        } while (cursor.moveToNext());
    }

    public void displayMessage(String type, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(type);
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create().show();
    }

    public void dataFetchToUnmuteDevice() {
        this.dataHandler = new DataHandler(this);
        this.dataHandler.openDatabase();
        Cursor cursor = this.dataHandler.retrieveAllCoursesForMutingDevice();
        if (cursor.moveToFirst()) {
            do {
                new MuteDevice(this).cancelMute(cursor.getString(cursor.getColumnIndex(ModelConstants.ID)));
            } while (cursor.moveToNext());
        }
    }

    public void deleteAllAlarms() {
        this.dataHandler = new DataHandler(this);
        this.dataHandler.openDatabase();
        Cursor cursor = this.dataHandler.retrieveAllCoursesForAlarmDelete();
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            do {
                new Alarm().cancelAlarm(this, cursor.getString(cursor.getColumnIndex(ModelConstants.ID)));
            } while (cursor.moveToNext());
            ContentValues contentValues = new ContentValues();
            contentValues.put(ModelConstants.REAL_ALARM_TIME_MINS, Integer.valueOf(-1));
            this.dataHandler.updateAfterAlarmDelete(contentValues);
            displayMessage("Message", "All alarms deleted!");
            return;
        }
        displayMessage("Message", "No Course registered yet!");
    }

    public void dataFetchToMuteOrVibrateDevice(String type) {
        this.dataHandler = new DataHandler(this);
        this.dataHandler.openDatabase();
        Cursor cursor = this.dataHandler.retrieveAllCoursesForMutingDevice();
        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(cursor.getColumnIndex(ModelConstants.ID));
                String day = cursor.getString(cursor.getColumnIndex(ModelConstants.DAY));
                String startTimeInMins = cursor.getString(cursor.getColumnIndex(ModelConstants.REAL_START_TIME_MINS));
                String endTimeInMins = cursor.getString(cursor.getColumnIndex(ModelConstants.REAL_END_TIME_MINS));
                MuteDevice muteDevice = new MuteDevice(this);
                if (type == "Mute") {
                    muteDevice.setMuteOrVibrate(Integer.parseInt(id), day, Integer.parseInt(startTimeInMins), Integer.parseInt(endTimeInMins), "Mute");
                } else if (type == "Vibrate") {
                    muteDevice.setMuteOrVibrate(Integer.parseInt(id), day, Integer.parseInt(startTimeInMins), Integer.parseInt(endTimeInMins), "Vibrate");
                }
            } while (cursor.moveToNext());
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        refresh();
    }

    public void refresh() {
        if (MainActivity.fa != null) {
            MainActivity.fa.finish();
        }
        startActivity(new Intent(this, MainActivity.class));
    }
}
