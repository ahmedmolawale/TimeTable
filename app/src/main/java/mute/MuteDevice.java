package mute;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

import com.ahmedmolawale.timetablemanager.R;


public class MuteDevice {
    Context context;

    private SharedPreferences sharedPreferences;


    public MuteDevice(Context con) {

        context = con;
    }

    public void setMuteOrVibrate(int id, String day, int startTimeInMins, int endTimeInMins, String type) {
        int dayOfTheWeek = -1;
        if (day.equals("Sunday"))
            dayOfTheWeek = 1;
        else if (day.equals("Monday"))
            dayOfTheWeek = 2;
        else if (day.equals("Tuesday"))
            dayOfTheWeek = 3;
        else if (day.equals("Wednesday"))
            dayOfTheWeek = 4;
        else if (day.equals("Thursday"))
            dayOfTheWeek = 5;
        else if (day.equals("Friday"))
            dayOfTheWeek = 6;
        else if (day.equals("Saturday"))
            dayOfTheWeek = 7;

        AlarmManager alarmManager = (AlarmManager) this.context.getSystemService(Context.ALARM_SERVICE);
        Calendar calendarNow = Calendar.getInstance();
        calendarNow.setTimeInMillis(System.currentTimeMillis());
        long currentTime = calendarNow.getTimeInMillis();
        int muteHour = startTimeInMins / 60;
        int muteMin = startTimeInMins % 60;
        Calendar calendarAlarm = Calendar.getInstance();
        calendarAlarm.setTimeInMillis(System.currentTimeMillis());
        calendarAlarm.set(Calendar.DAY_OF_WEEK, dayOfTheWeek);
        calendarAlarm.set(Calendar.HOUR_OF_DAY, muteHour);
        calendarAlarm.set(Calendar.MINUTE, muteMin);
        calendarAlarm.set(Calendar.SECOND, 0);
        long muteTime = calendarAlarm.getTimeInMillis();
        if (currentTime > muteTime) {
            muteTime += AlarmManager.INTERVAL_DAY * 7;
        }
        Intent intent = new Intent(this.context, MuteDeviceReceiver.class);
        intent.putExtra("ROWID", Integer.toString(id));
        intent.putExtra("DAYOFTHEWEEK", Integer.toString(dayOfTheWeek));
        intent.putExtra("STARTTIMEINMINS", Integer.toString(startTimeInMins));
        intent.putExtra("ENDTIMEINMINS", Integer.toString(endTimeInMins));
        intent.putExtra("TYPE", type);  //used to know if to mute or vibrate
        int intentFlag;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            intentFlag = PendingIntent.FLAG_CANCEL_CURRENT;
        } else {
            intentFlag = PendingIntent.FLAG_IMMUTABLE;
        }

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.context, id, intent, intentFlag);
        alarmManager.set(AlarmManager.RTC_WAKEUP, muteTime, pendingIntent);
    }

    public void setSubsequentMuteOrVibrate(int id, String day, int startTimeInMins, int endTimeInMins, String type) {

        //do this shit only if automute or autovibrate is checked....will save your ass
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean auto_mute_status = this.sharedPreferences.getBoolean(context.getString(R.string.pref_checkbox_key), false);
        boolean auto_vibrate_status = this.sharedPreferences.getBoolean(context.getString(R.string.pref_checkbox_key_2), false);


        if (auto_mute_status || auto_vibrate_status) {
            int dayOfTheWeek = -1;
            if (day.equals("Sunday"))
                dayOfTheWeek = 1;
            else if (day.equals("Monday"))
                dayOfTheWeek = 2;
            else if (day.equals("Tuesday"))
                dayOfTheWeek = 3;
            else if (day.equals("Wednesday"))
                dayOfTheWeek = 4;
            else if (day.equals("Thursday"))
                dayOfTheWeek = 5;
            else if (day.equals("Friday"))
                dayOfTheWeek = 6;
            else if (day.equals("Saturday"))
                dayOfTheWeek = 7;

            AlarmManager alarmManager = (AlarmManager) this.context.getSystemService(Context.ALARM_SERVICE);
            Calendar calendarNow = Calendar.getInstance();
            calendarNow.setTimeInMillis(System.currentTimeMillis());
            long currentTime = calendarNow.getTimeInMillis();
            int muteHour = startTimeInMins / 60;
            int muteMin = startTimeInMins % 60;
            Calendar calendarAlarm = Calendar.getInstance();
            calendarAlarm.setTimeInMillis(System.currentTimeMillis());
            calendarAlarm.set(Calendar.DAY_OF_WEEK, dayOfTheWeek);
            calendarAlarm.set(Calendar.HOUR_OF_DAY, muteHour);
            calendarAlarm.set(Calendar.MINUTE, muteMin);
            calendarAlarm.set(Calendar.SECOND, 0);

            long muteTime = calendarAlarm.getTimeInMillis() + AlarmManager.INTERVAL_DAY * 7;
            if (currentTime > muteTime) {
                muteTime += AlarmManager.INTERVAL_DAY * 7;
            }
            Intent intent = new Intent(this.context, MuteDeviceReceiver.class);
            intent.putExtra("ROWID", Integer.toString(id));
            intent.putExtra("DAYOFTHEWEEK", Integer.toString(dayOfTheWeek));
            intent.putExtra("STARTTIMEINMINS", Integer.toString(startTimeInMins));
            intent.putExtra("ENDTIMEINMINS", Integer.toString(endTimeInMins));
            intent.putExtra("TYPE", type);
            int intentFlag;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
                intentFlag = PendingIntent.FLAG_CANCEL_CURRENT;
            } else {
                intentFlag = PendingIntent.FLAG_IMMUTABLE;
            }
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this.context, id, intent, intentFlag);
            alarmManager.set(AlarmManager.RTC_WAKEUP, muteTime, pendingIntent);
        }
    }

    public void cancelMute(String rowId) {

        Intent intent = new Intent(context, MuteDeviceReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, Integer.parseInt(rowId), intent, PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        alarmManager.cancel(pendingIntent);

    }

}
