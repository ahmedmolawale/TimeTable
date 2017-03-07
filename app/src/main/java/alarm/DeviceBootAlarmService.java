package alarm;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;

import data.DataHandler;
import data.ModelConstants;

public class DeviceBootAlarmService extends IntentService {

    DataHandler dataHandler;

    public DeviceBootAlarmService() {
        super("name");
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // TODO Auto-generated method stub

        dataHandler = new DataHandler(getApplicationContext());
        dataHandler.openDatabase();
        Cursor cursor = dataHandler.retrieveAllCoursesForBootTimeAlarm();

        if (cursor.moveToFirst() == true) {

            cursor.moveToFirst();
            do {

                String id = cursor.getString(cursor.getColumnIndex(ModelConstants.ID));
                String day = cursor.getString(cursor.getColumnIndex(ModelConstants.DAY));
                String alarmTimeInMins = cursor.getString(cursor.getColumnIndex(ModelConstants.REAL_ALARM_TIME_MINS));

                Alarm alarm = new Alarm();
                alarm.setAlarm(getApplicationContext(), Integer.parseInt(id), day, Integer.parseInt(alarmTimeInMins));


            } while (cursor.moveToNext());

        }
    }
}
