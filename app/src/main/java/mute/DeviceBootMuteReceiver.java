package mute;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class DeviceBootMuteReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            /* Setting the alarm here */
		 	Intent serviceIntent = new Intent(context, DeviceBootMuteService.class);
		 	context.startService(serviceIntent);
		 
				
			}
		
		

	}

}
