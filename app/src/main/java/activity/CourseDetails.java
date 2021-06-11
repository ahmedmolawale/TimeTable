package activity;

import mute.MuteDevice;
import alarm.Alarm;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.ahmedmolawale.timetablemanager.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


import data.DataHandler;
import data.ModelConstants;

public class CourseDetails extends AppCompatActivity {

	public static Activity activity;
	String rowId;
	TextView courseCode, courseTitle, time, lecturer, venue, alarmTime;
	DataHandler dataHandler;
	Button editButton, deleteButton;


	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_course_details, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {

		case R.id.action_edit:
			openEdit();
			return true;
		case R.id.action_delete:
			deleteCourse();
			return true;
		default:
			return false;

		}

	}

	public void openEdit() {
		Intent intent = new Intent("CourseEdit");
		intent.putExtra("ROW_ID", rowId); // passing the rowid to this activity
											// to know where to update in the// table
		startActivity(intent);
	}

	public void deleteCourse() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setCancelable(true);
		builder.setTitle("Confirmation");
		builder.setMessage("Do you want to delete this course?");
		builder.setIcon(android.R.drawable.ic_dialog_alert);

		builder.setPositiveButton(android.R.string.yes,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

						dataHandler.openDatabase();
						dataHandler.deleteACourse(rowId);
						Alarm alarm = new Alarm();

						alarm.cancelAlarm(getApplicationContext(), rowId);
						MuteDevice muteDevice = new MuteDevice(
								getApplicationContext());
						muteDevice.cancelMute(rowId);

						displayMessage("Success",
								"Course deleted successfully.");

						dataHandler.close();

					}
				});
		builder.setNegativeButton(android.R.string.no,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}
				});
		AlertDialog alert11 = builder.create(); // very important
		alert11.show();

	}

    @Override
    protected void onRestart() {
        super.onRestart();
        assistOnCreate();
    }

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.course_details);

		activity = this;
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        assistOnCreate();

	}

	private void assistOnCreate(){
        final AdView mAdView = (AdView) findViewById(R.id.adView7);
        final AdRequest adRequest1;
        adRequest1 = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest1);

        courseCode = (TextView) findViewById(R.id.course_code_in_details);
        courseTitle = (TextView) findViewById(R.id.course_title_in_details);
        time = (TextView) findViewById(R.id.time_in_details);
        lecturer = (TextView) findViewById(R.id.lecturer_in_details);
        venue = (TextView) findViewById(R.id.venue_in_details);
        alarmTime = (TextView) findViewById(R.id.alarm_in_details);

        editButton = (Button) findViewById(R.id.edit_button_in_details);
        deleteButton = (Button) findViewById(R.id.delete_button_in_details);

        Intent intent = getIntent();
        rowId = intent.getStringExtra("ROW_ID");
        int row_Id = Integer.parseInt(rowId);
        fetchAndPopulateFields(row_Id);

        editButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                openEdit();
            }
        });

        deleteButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // delete the course from the database using the row id
                deleteCourse();
            }
        });
    }


	public void fetchAndPopulateFields(int rowIdToFetch) {

		dataHandler = new DataHandler(getBaseContext());
		dataHandler.openDatabase();

		Cursor cursor = dataHandler.retrieveDataForDetails(rowIdToFetch);

		cursor.moveToFirst();

		String day = cursor
				.getString(cursor.getColumnIndex(ModelConstants.DAY));
		String courseCode = cursor.getString(cursor
				.getColumnIndex(ModelConstants.COURSE_CODE));
		String courseTitle = cursor.getString(cursor
				.getColumnIndex(ModelConstants.COURSE_TITLE));
		String startTime = cursor.getString(cursor
				.getColumnIndex(ModelConstants.START_TIME_TEXT));
		String endTime = cursor.getString(cursor
				.getColumnIndex(ModelConstants.END_TIME_TEXT)); // use with the alarm
															// time cos its
															// always in 24 hr
															// format
		String lecturer = cursor.getString(cursor
				.getColumnIndex(ModelConstants.LECTURER));
		String venue = cursor.getString(cursor
				.getColumnIndex(ModelConstants.VENUE));
		String alarmIndex = cursor.getString(cursor
				.getColumnIndex(ModelConstants.ALARM_TIME_INDEX));
		dataHandler.close();

		String time = startTime + " - " + endTime;
		switch (Integer.parseInt(alarmIndex)) {
		case 0:
			alarmTime.setText("Alarm not set.");
			break;
		case 1:
			alarmTime.setText("15 mins before lecture.");
			break;
		case 2:
			alarmTime.setText("30 mins before lecture");
			break;
		case 3:
			alarmTime.setText("1 hour before lecture.");
			break;
		case 4:
			alarmTime.setText("2 hours before lecture");
			break;
		case 5:
			alarmTime.setText("3 hours before lecture");
			break;
		default:
			break;

		}

		setTitle(day);
		this.courseCode.setText(courseCode);

		this.courseTitle.setText(courseTitle);

		this.time.setText(time);
		if (!lecturer.equals("")) {
			this.lecturer.setText(lecturer);
		} else {
			this.lecturer.setText("Not yet inputed.");
		}
		if (!venue.equals("")) {
			this.venue.setText(venue);
		} else {
			this.venue.setText("Not yet inputed.");
		}

	}



	public void displayMessage(String type, String message) {
		Builder builder = new Builder(this);
		builder.setCancelable(true);
		builder.setTitle(type);
		builder.setMessage(message);
		builder.show();
		
		refresh();

	}

	public void refresh() {
		
		if(MainActivity.fa !=null){
		MainActivity.fa.finish();
		}
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();

	}
}
