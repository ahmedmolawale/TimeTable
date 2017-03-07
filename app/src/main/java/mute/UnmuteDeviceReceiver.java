package mute;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;

public class UnmuteDeviceReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		
		
		audio.setRingerMode(AudioManager.RINGER_MODE_NORMAL);

		switch( audio.getRingerMode() ){
		case AudioManager.RINGER_MODE_NORMAL:
		   break;
		case AudioManager.RINGER_MODE_SILENT:
			audio.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
		   break;
		case AudioManager.RINGER_MODE_VIBRATE:
			audio.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
		   break;
		}

		//call the set mute again
		
	}

}
