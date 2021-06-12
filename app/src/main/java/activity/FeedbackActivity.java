package activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.ahmedmolawale.timetablemanager.R;
import com.google.android.gms.ads.AdView;


public class FeedbackActivity extends AppCompatActivity {

	private EditText username, email, message;
	private Spinner feedBackTypeSpinner;
	private CheckBox feedBackCheckBox;
	private Button sendFeedBack;

    @Override
    protected void onRestart() {
        super.onRestart();
        assistOnCreate();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feedback_layout);

        assistOnCreate();
	}
    private void assistOnCreate(){

        setTitle("Feedback");
        final AdView mAdView = (AdView) findViewById(R.id.adView6);
        final com.google.android.gms.ads.AdRequest adRequest = new com.google.android.gms.ads.AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        username = (EditText) findViewById(R.id.EditTextName);
        email = (EditText) findViewById(R.id.EditTextEmail);
        message = (EditText) findViewById(R.id.EditTextFeedbackBody);
        feedBackTypeSpinner = (Spinner) findViewById(R.id.SpinnerFeedback);
        feedBackCheckBox = (CheckBox) findViewById(R.id.CheckBoxResponse);
        sendFeedBack = (Button) findViewById(R.id.ButtonSendFeedback);
        String[] spinnerItems = getResources().getStringArray(
                R.array.feedbacktypelist);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getApplicationContext(), R.layout.spinner_item, spinnerItems);

        feedBackTypeSpinner.setAdapter(adapter);

        sendFeedBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                collectAndSendData();

            }
        });






    }
	public void collectAndSendData() {

		String name = username.getText().toString();
		String mail = email.getText().toString();
		String bodyOfMessage = message.getText().toString()
				+ "\n Sender Mail: " + mail + "\n Sender Name: " + name;
		String feedBackType = feedBackTypeSpinner.getSelectedItem().toString();
		boolean needResponse = feedBackCheckBox.isChecked();
		if (needResponse) {
			feedBackType = feedBackType + " - Response Needed.";
		}
		Intent emailIntent = new Intent(Intent.ACTION_SEND);
		emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {"ahmedmolawale@gmail.com"});
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, feedBackType);
		emailIntent.putExtra(Intent.EXTRA_TEXT, bodyOfMessage);
		//emailIntent.putExtra(Intent.E, value)
		emailIntent.setType("message/rfc822");
		startActivity(Intent.createChooser(emailIntent,
				"Choose an Email Provider"));

	}

}
