package fragment;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

public class TimeDialog extends DialogFragment {
	OnTimeSetListener timeListener;
	Context ctx;
	int hour, min;

    public TimeDialog(){



    }

    public void setDetails(Context ctx,OnTimeSetListener listener, int hour, int min){
        this.ctx = ctx;
        timeListener = listener;
        this.hour = hour;
        this.min = min;


    }

	@Override
	@NonNull
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		Dialog timeDialog = new TimePickerDialog(ctx,timeListener,hour, min, false);
		return timeDialog;
	}
	



}
