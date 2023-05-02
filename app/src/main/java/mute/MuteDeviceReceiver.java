package mute;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;


public class MuteDeviceReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        //grabbing information passed to this intent
        Bundle bundle = intent.getExtras();
        String id = bundle.getString("ROWID");
        String dayOfTheWeek = bundle.getString("DAYOFTHEWEEK");
        int dayInInt = Integer.parseInt(dayOfTheWeek);
        String startTimeInMins = bundle.getString("STARTTIMEINMINS");
        String endTimeInMins = bundle.getString("ENDTIMEINMINS");
        String type = bundle.getString("TYPE");

        if (type.equals("Vibrate")) {
            switch (audio.getRingerMode()) {
                case AudioManager.RINGER_MODE_NORMAL:
                    audio.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                    break;
                case AudioManager.RINGER_MODE_SILENT:
                    audio.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                    break;
                case AudioManager.RINGER_MODE_VIBRATE:
                    break;
            }
        } else if (type.equals("Mute")) {
            switch (audio.getRingerMode()) {
                case AudioManager.RINGER_MODE_NORMAL:
                    audio.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                    break;
                case AudioManager.RINGER_MODE_SILENT:
                    break;
                case AudioManager.RINGER_MODE_VIBRATE:
                    audio.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                    break;
            }
        }
        //call setmute again here

        MuteDevice muteDevice = new MuteDevice(context);
        muteDevice.setSubsequentMuteOrVibrate(Integer.parseInt(id),dayOfTheWeek,Integer.parseInt(startTimeInMins),Integer.parseInt(endTimeInMins),type);

        //condition to check if the end time is the next day
        if (Integer.parseInt(endTimeInMins) < Integer.parseInt(startTimeInMins)) {

            if (dayInInt == 7)
                dayInInt = 1;
            else
                dayInInt += 1;


        }

        //this is not very important but the user may have tampered with his phone time somehow
        Calendar calendarNow = Calendar.getInstance();
        calendarNow.setTimeInMillis(System.currentTimeMillis());
        long currentTime = calendarNow.getTimeInMillis();


        int unMuteHour = Integer.parseInt(endTimeInMins) / 60; //this gives the mute hour from the endtime in mins
        int unMuteMin = Integer.parseInt(endTimeInMins) % 60;

        Calendar calendarAlarm = Calendar.getInstance();
        calendarAlarm.setTimeInMillis(System.currentTimeMillis());
        calendarAlarm.set(Calendar.DAY_OF_WEEK, dayInInt);
        calendarAlarm.set(Calendar.HOUR_OF_DAY, unMuteHour);
        calendarAlarm.set(Calendar.MINUTE, unMuteMin);
        calendarAlarm.set(Calendar.SECOND, 0);
        long unmuteTime = calendarAlarm.getTimeInMillis();
        Intent intentForUnmute = new Intent(context, UnmuteDeviceReceiver.class);

        int intentFlag;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            intentFlag = PendingIntent.FLAG_CANCEL_CURRENT;
        } else {
            intentFlag = PendingIntent.FLAG_IMMUTABLE;
        }
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, Integer.parseInt(id), intentForUnmute, intentFlag);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (currentTime > unmuteTime) {
            unmuteTime = unmuteTime + AlarmManager.INTERVAL_DAY * 7;
        }
        alarmManager.set(AlarmManager.RTC_WAKEUP, unmuteTime, pendingIntent);
    }
}

