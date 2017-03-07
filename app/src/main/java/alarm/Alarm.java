package alarm;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import android.os.Build;

public class Alarm {

    public void setAlarm(Context context, int id, String day, int alarmTimeInMins) {

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

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Calendar calendarNow = Calendar.getInstance();
        calendarNow.setTimeInMillis(System.currentTimeMillis());
        long currentTime = calendarNow.getTimeInMillis();
        //to the alarmTimeInMins

        int alarmHour = alarmTimeInMins / 60; //this gives the alarm hour from the alarm in mins
        int alarmMin = alarmTimeInMins % 60;    //this gives the seconds

        Calendar calendarAlarm = Calendar.getInstance();
        calendarAlarm.setTimeInMillis(System.currentTimeMillis());
        calendarAlarm.set(Calendar.DAY_OF_WEEK, dayOfTheWeek);
        calendarAlarm.set(Calendar.HOUR_OF_DAY, alarmHour);
        calendarAlarm.set(Calendar.MINUTE, alarmMin);
        calendarAlarm.set(Calendar.SECOND, 0);
        long alarmTime = calendarAlarm.getTimeInMillis();

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("ROW_ID", Integer.toString(id));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        if (currentTime > alarmTime) {
            alarmTime += AlarmManager.INTERVAL_DAY * 7;
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent);
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent);
        }
    }

    public void cancelAlarm(Context context, String rowId) {

        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, Integer.parseInt(rowId), intent, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        alarmManager.cancel(pendingIntent);


    }
}
