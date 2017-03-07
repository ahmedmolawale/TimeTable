package alarm;

import mute.MuteDevice;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;






public class SecondModificationService extends IntentService {


    public SecondModificationService() {
        super("name4");
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // TODO Auto-generated method stub
        Bundle bundle = intent.getExtras();
        String id = bundle.getString("DATAW");
        String day = bundle.getString("DAYW");
        String startTimeInMins = bundle.getString("STARTTIMEW");
        String endTimeInMins = bundle.getString("ENDTIMEW");
        //this is actually for a one time mute setup
        MuteDevice muteDevice = new MuteDevice(getApplicationContext());
        muteDevice.setMuteOrVibrate(Integer.parseInt(id), day, Integer.parseInt(startTimeInMins), Integer.parseInt(endTimeInMins), "Mute");

        cancelNotification(id);
    }

    private void cancelNotification(String id) {
        String s = Context.NOTIFICATION_SERVICE;
        NotificationManager mNM = (NotificationManager) getApplicationContext().getSystemService(s);
        mNM.cancel(Integer.parseInt(id));

    }

}
