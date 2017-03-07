package alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


public class AlarmReceiver extends BroadcastReceiver {


	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		Bundle bundle = intent.getExtras();
	      String id = bundle.getString("ROW_ID");
	      
	      Intent serviceIntent = new Intent(context, NotificationService.class);
	      serviceIntent.putExtra("DATA", id);
	      context.startService(serviceIntent);
		
	      

	}
	

}
