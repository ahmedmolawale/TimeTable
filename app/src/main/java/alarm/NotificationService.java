package alarm;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

import com.ahmedmolawale.timetablemanager.R;

import activity.CourseDetails;
import activity.MainActivity;
import data.DataHandler;
import data.ModelConstants;

public class NotificationService extends IntentService {

    SharedPreferences sharedPreferences;
    DataHandler dataHandler;
    public NotificationService() {
        super("name2");
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // TODO Auto-generated method stub

        Bundle bundle = intent.getExtras();
        String id = bundle.getString("DATA");

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        dataHandler = new DataHandler(getApplicationContext());
        dataHandler.openDatabase();

        //once the notification is fired, we set alarm for the next week
        Cursor cursor = dataHandler.getDataForNotification(id);
        if (cursor != null && cursor.moveToFirst()) {
            String day = cursor.getString(cursor.getColumnIndex(ModelConstants.DAY));
            String alarmTimeInMins = cursor.getString(cursor.getColumnIndex(ModelConstants.REAL_ALARM_TIME_MINS));
            new Alarm().setAlarm(getApplicationContext(), Integer.parseInt(id), day, Integer.parseInt(alarmTimeInMins));
        }


        boolean auto_mute_status = this.sharedPreferences.getBoolean(getString(R.string.pref_checkbox_key), false);
        boolean auto_vibrate_status = this.sharedPreferences.getBoolean(getString(R.string.pref_checkbox_key_2), false);

//		//doing something tremendously interesting here
//    	Cursor cursorInTableTwo = dataHandler.getStatusFromTableTwo("1");
//        boolean checkBoxStatus = false;
//
//        if(cursorInTableTwo.moveToFirst() == false){  //the user have not open the settings activity at all
//            checkBoxStatus = false;
//        }
//        else{
//            cursorInTableTwo.moveToFirst();
//            String status = cursorInTableTwo.getString(cursorInTableTwo.getColumnIndex(ModelConstants.CHECKBOXSTATUS));
//            switch(Integer.parseInt(status)){
//                case 0:
//                    checkBoxStatus =false;
//                    break;
//                case 1:
//                    checkBoxStatus = true;
//            }
//        }

        if (cursor != null && cursor.moveToFirst()) {

            String day = cursor.getString(cursor.getColumnIndex(ModelConstants.DAY));
            String courseCode = cursor.getString(cursor.getColumnIndex(ModelConstants.COURSE_CODE));
            String startTime = cursor.getString(cursor.getColumnIndex(ModelConstants.START_TIME_TEXT));
            String endTime = cursor.getString(cursor.getColumnIndex(ModelConstants.END_TIME_TEXT));
            String startTimeInMins = cursor.getString(cursor.getColumnIndex(ModelConstants.REAL_START_TIME_MINS));
            String endTimeInMins = cursor.getString(cursor.getColumnIndex(ModelConstants.REAL_END_TIME_MINS));
            String time = startTime + " - " + endTime;
            String message = "";
            String msg = "";

            if (auto_mute_status || auto_vibrate_status) {
                if (auto_mute_status) {
                    message = "You have " + courseCode + " from " + time + "\n Device will be muted during lecture.";
                    msg = "Do not Mute?";
                } else if (auto_vibrate_status) {
                    message = "You have " + courseCode + " from " + time + "\n Device will be in vibrate mode during lecture.";
                    msg = "Do not put in Vibrate Mode?";
                }
                NotificationCompat.Builder builder =
                        new NotificationCompat.Builder(getApplicationContext())
                                .setSmallIcon(com.ahmedmolawale.timetablemanager.R.drawable.ic_launcher)
                                .setContentTitle("Reminder!")
                                .setContentText(message);

                builder.setLights(Color.BLUE, 500, 500);
                long[] pattern = {500, 500, 500, 500, 500, 500, 500, 500, 500};
                builder.setVibrate(pattern);

                Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                builder.setSound(alarmSound);

                // Creates an explicit intent for an Activity in your app
                Intent resultIntent = new Intent(getApplicationContext(), CourseDetails.class);
                resultIntent.putExtra("ROW_ID", id);

                // The stack builder object will contain an artificial back stack for the
                // started Activity.
                // This ensures that navigating backward from the Activity leads out of
                // your application to the Home screen.
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
                // Adds the back stack for the Intent (but not the Intent itself)
                stackBuilder.addParentStack(CourseDetails.class);
                // Adds the Intent that starts the Activity to the top of the stack
                stackBuilder.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent =
                        stackBuilder.getPendingIntent(
                                0,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );


                builder.setContentIntent(resultPendingIntent);
                Intent intentModify = new Intent(getApplicationContext(), ModificationService.class);
                intentModify.putExtra("DATAZ", id);
                intentModify.putExtra("DAYZ", day);
                intentModify.putExtra("STARTTIMEZ", startTimeInMins);
                intentModify.putExtra("ENDTIMEZ", endTimeInMins);

                PendingIntent pendingIntentModify = PendingIntent.getService(getApplicationContext(), Integer.parseInt(id), intentModify, PendingIntent.FLAG_UPDATE_CURRENT);

                builder.setStyle(new NotificationCompat.BigTextStyle().bigText(message));

                builder.addAction(R.drawable.ic_action_unmute, msg, pendingIntentModify);
                builder.setAutoCancel(true);

                NotificationManager mNotificationManager =
                        (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

                mNotificationManager.notify(Integer.parseInt(id), builder.build());


            } else {

                message = "You have " + courseCode + " from " + time;
                msg = "Mute device during lecture?";

                NotificationCompat.Builder builder =
                        new NotificationCompat.Builder(getApplicationContext())
                                .setSmallIcon(com.ahmedmolawale.timetablemanager.R.drawable.ic_launcher)
                                .setContentTitle("Reminder!")
                                .setContentText(message);
                builder.setAutoCancel(true);
                builder.setLights(Color.BLUE, 500, 500);
                long[] pattern = {500, 500, 500, 500, 500, 500, 500, 500, 500};
                builder.setVibrate(pattern);

                Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                builder.setSound(alarmSound);
                // Creates an explicit intent for an Activity in your app
                Intent resultIntent = new Intent(getApplicationContext(), CourseDetails.class);
                resultIntent.putExtra("ROW_ID", id);
                // The stack builder object will contain an artificial back stack for the
                // started Activity.
                // This ensures that navigating backward from the Activity leads out of
                // your application to the Home screen.
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
                // Adds the back stack for the Intent (but not the Intent itself)
                stackBuilder.addParentStack(MainActivity.class);
                // Adds the Intent that starts the Activity to the top of the stack
                stackBuilder.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent =
                        stackBuilder.getPendingIntent(
                                0,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );

                builder.setContentIntent(resultPendingIntent);
                //builder.addAction(R.drawable.ic_action_mute, "Mute device during lecture?", );

                Intent intentModify1 = new Intent(getApplicationContext(), SecondModificationService.class);
                intentModify1.putExtra("DATAW", id);
                intentModify1.putExtra("DAYW", day);
                intentModify1.putExtra("STARTTIMEW", startTimeInMins);
                intentModify1.putExtra("ENDTIMEW", endTimeInMins);

                PendingIntent pendingIntentModify = PendingIntent.getService(getApplicationContext(), Integer.parseInt(id), intentModify1, 0);
                builder.setStyle(new NotificationCompat.BigTextStyle().bigText(message));

                builder.addAction(R.drawable.ic_action_mute, msg, pendingIntentModify);

                NotificationManager mNotificationManager =
                        (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

                mNotificationManager.notify(Integer.parseInt(id), builder.build());

            }
        }

    }
}