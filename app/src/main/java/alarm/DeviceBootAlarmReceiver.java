package alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import data.DataHandler;

public class DeviceBootAlarmReceiver extends BroadcastReceiver {


	@Override
	public void onReceive(Context context, Intent intent) {
		
		 if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
	            /* Setting the alarm here */
			 	Intent serviceIntent = new Intent(context, DeviceBootAlarmService.class);
			 	context.startService(serviceIntent);
			 
					
				}
			 
	            
	}

}
