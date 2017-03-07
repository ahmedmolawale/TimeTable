package activity;

import mute.MuteDevice;
import alarm.Alarm;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.domainname.timetablemanager.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


import data.DataHandler;
import data.ModelConstants;


public class CourseEdit extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private Spinner daySpinner, alarmSpinner;
    private String[] daySpinnerData, alarmSpinnerData;
    private EditText courseCode, courseTitle, lecturer, venue;
    private Button startTimeButton, endTimeButton, update;
    private DataHandler dataHandler;
    private String daySelected;
    private int alarmTimeIndex;

    private static final int startTimeId = 1;
    private static final int endTimeId = 2;

    private int startHourIn12HrFormat; // same way as used in editFragment
    private int realStartTimeInMins;
    private int startMin; // same way as used in editFragment

    private int endHourIn12HrFormat; // same way as used in editFragment
    private int realEndTimeInMins;
    private int endMin; // same way as used in editFragment

    private String rowIdToFetch;

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_course_edit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {

            case R.id.action_update:
                update();
                return true;
            default:
                return false;
        }
    }

    private void update() {

        if (courseCode.getText().toString().equals("")
                || courseTitle.getText().toString().equals("")) {

            displayMessage("Error",
                    "Course code or Course title cannot be empty.");
        } else {

            if (realStartTimeInMins == realEndTimeInMins) { // checking if the
                // time are the same
                displayMessage("Timing not correct",
                        "Lecture's start time cannot be the same with end time.");
            } else if (realStartTimeInMins > realEndTimeInMins) {
                confirmationDialog("Alert",
                        "The time seems not to be correct. Do you want to continue?");

            } else if (lecturer.getText().toString().equals("")) {

                confirmationDialog("Confirmation",
                        "Do you want to leave the Lecturer's name empty and save data ?");

            } else if (venue.getText().toString().equals("")) {
                confirmationDialog("Confirmation",
                        "Do you want to leave the Venue empty and save data?");

            } else {
                updateRelevantFields(rowIdToFetch);
            }
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        assistOnCreate();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_edit);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        assistOnCreate();

    }

    private void assistOnCreate() {

        setTitle("Edit Course");
        final AdView mAdView = (AdView) findViewById(R.id.adView8);
        final AdRequest adRequest1;
        adRequest1 = new AdRequest.Builder().build();

        mAdView.loadAd(adRequest1);


        daySpinner = (Spinner) findViewById(R.id.day_spinner_on_edit);
        alarmSpinner = (Spinner) findViewById(R.id.alarm_spinner_on_edit);

        daySpinnerData = getResources().getStringArray(R.array.days);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                daySpinner.getContext(), R.layout.spinner_item, daySpinnerData);

        daySpinner.setAdapter(adapter);

        daySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                int index = parent.getSelectedItemPosition();
                daySelected = daySpinnerData[index];

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        alarmSpinnerData = getResources().getStringArray(R.array.alarm);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(
                alarmSpinner.getContext(), R.layout.spinner_item,
                alarmSpinnerData);
        alarmSpinner.setAdapter(adapter1);
        alarmSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                int index = parent.getSelectedItemPosition();
                alarmTimeIndex = index;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        courseCode = (EditText) findViewById(R.id.course_code_in_edit);
        courseTitle = (EditText) findViewById(R.id.course_title_in_edit);
        lecturer = (EditText) findViewById(R.id.lecturer_in_edit);
        venue = (EditText) findViewById(R.id.venue_in_edit);

        startTimeButton = (Button) findViewById(R.id.start_time_in_edit);
        endTimeButton = (Button) findViewById(R.id.end_time_in_edit);
        update = (Button) findViewById(R.id.update_in_edit);

        Intent intent = getIntent();
        rowIdToFetch = intent.getStringExtra("ROW_ID");
        final int rowId = Integer.parseInt(rowIdToFetch);

        fetchAndPopulateFields(rowId);

        startTimeButton.setOnClickListener(new OnClickListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showDialog(startTimeId);

            }
        });

        endTimeButton.setOnClickListener(new OnClickListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showDialog(endTimeId);
            }
        });

        update.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                update();
            }
        });


    }

    public void confirmationDialog(String type, String message) {
        // here is working with the when lecturer or venue is empty
        Builder builder = new Builder(this);
        builder.setTitle(type);
        builder.setMessage(message);
        builder.setCancelable(true);
        // builder.show();
        builder.setIcon(android.R.drawable.ic_dialog_alert);

        builder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with save
                        updateRelevantFields(rowIdToFetch);
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
    @Deprecated
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case startTimeId:
                int hour = 7;
                int min = 0;
                return new TimePickerDialog(this, new StartTimeHandler(), hour,
                        min, false);

            case endTimeId:
                hour = 8;
                min = 0;
                return new TimePickerDialog(this, new EndTimeHandler(), hour, min,
                        false);

        }

        return null;
    }

    private class StartTimeHandler implements OnTimeSetListener {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // TODO Auto-generated method stub

            if (hourOfDay > 12) {
                startHourIn12HrFormat = hourOfDay - 12;
                realStartTimeInMins = (hourOfDay * 60) + minute; // in mins
                startMin = minute;
                // check if mins is less than 10, if yes, affix 0 before mins
                if (startMin < 10) {
                    startTimeButton.setText(startHourIn12HrFormat + ":0"
                            + startMin + " PM");
                } else {
                    startTimeButton.setText(startHourIn12HrFormat + ":"
                            + startMin + " PM");
                }

            } else if (hourOfDay == 12) {
                startHourIn12HrFormat = hourOfDay;
                realStartTimeInMins = (hourOfDay * 60) + minute;
                startMin = minute;
                if (startMin < 10) {
                    startTimeButton.setText(startHourIn12HrFormat + ":0"
                            + startMin + " PM");
                } else {
                    startTimeButton.setText(startHourIn12HrFormat + ":"
                            + startMin + " PM");
                }

            } else if (hourOfDay == 0) {// midnite
                startHourIn12HrFormat = hourOfDay + 12;
                realStartTimeInMins = (hourOfDay * 60) + minute;
                startMin = minute;
                if (startMin < 10) {
                    startTimeButton.setText(startHourIn12HrFormat + ":0"
                            + startMin + " AM");
                } else {
                    startTimeButton.setText(startHourIn12HrFormat + ":"
                            + startMin + " AM");
                }

            } else {
                startHourIn12HrFormat = hourOfDay;
                realStartTimeInMins = (hourOfDay * 60) + minute;
                startMin = minute;
                if (startMin < 10) {
                    startTimeButton.setText(startHourIn12HrFormat + ":0"
                            + startMin + " AM");
                } else {
                    startTimeButton.setText(startHourIn12HrFormat + ":"
                            + startMin + " AM");
                }
            }
        }
    }

    private class EndTimeHandler implements OnTimeSetListener {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // TODO Auto-generated method stub

            if (hourOfDay > 12) { // checking for afternoon
                endHourIn12HrFormat = hourOfDay - 12;
                realEndTimeInMins = (hourOfDay * 60) + minute;
                endMin = minute;
                // check if mins is less than 10, if yes, affix 0 before mins
                if (endMin < 10) {
                    endTimeButton.setText(endHourIn12HrFormat + ":0" + endMin
                            + " PM");
                } else {
                    endTimeButton.setText(endHourIn12HrFormat + ":" + endMin
                            + " PM");
                }
            } else if (hourOfDay == 12) { // check if at noon
                endHourIn12HrFormat = hourOfDay;
                realEndTimeInMins = (hourOfDay * 60) + minute;
                endMin = minute;
                if (endMin < 10) {
                    endTimeButton.setText(endHourIn12HrFormat + ":0" + endMin
                            + " PM");
                } else {
                    endTimeButton.setText(endHourIn12HrFormat + ":" + endMin
                            + " PM");
                }

            } else if (hourOfDay == 0) {// check if at midnight
                endHourIn12HrFormat = hourOfDay + 12;
                realEndTimeInMins = (hourOfDay * 60) + minute;
                endMin = minute;
                if (endMin < 10) {
                    endTimeButton.setText(endHourIn12HrFormat + ":0" + endMin
                            + " AM");
                } else {
                    endTimeButton.setText(endHourIn12HrFormat + ":" + endMin
                            + " AM");
                }

            } else { // checking for morning
                endHourIn12HrFormat = hourOfDay;
                realEndTimeInMins = (hourOfDay * 60) + minute;
                endMin = minute;
                if (endMin < 10) {
                    endTimeButton.setText(endHourIn12HrFormat + ":0" + endMin
                            + " AM");
                } else {
                    endTimeButton.setText(endHourIn12HrFormat + ":" + endMin
                            + " AM");
                }
            }

        }

    }

    public void fetchAndPopulateFields(int rowIdToFetch) {

        dataHandler = new DataHandler(getBaseContext());
        dataHandler.openDatabase();

        Cursor cursor = dataHandler.retrieveDataForEdit(rowIdToFetch);

        cursor.moveToFirst();

        String day = cursor
                .getString(cursor.getColumnIndex(ModelConstants.DAY));
        String courseCode = cursor.getString(cursor
                .getColumnIndex(ModelConstants.COURSE_CODE));
        String courseTitle = cursor.getString(cursor
                .getColumnIndex(ModelConstants.COURSE_TITLE));
        String startTimeInMins = cursor.getString(cursor
                .getColumnIndex(ModelConstants.REAL_START_TIME_MINS));
        String endTimeInMins = cursor.getString(cursor
                .getColumnIndex(ModelConstants.REAL_END_TIME_MINS));
        String startTime = cursor.getString(cursor
                .getColumnIndex(ModelConstants.START_TIME_TEXT));
        String endTime = cursor.getString(cursor
                .getColumnIndex(ModelConstants.END_TIME_TEXT));
        String lecturer = cursor.getString(cursor
                .getColumnIndex(ModelConstants.LECTURER));
        String venue = cursor.getString(cursor
                .getColumnIndex(ModelConstants.VENUE));
        String alarmIndex = cursor.getString(cursor
                .getColumnIndex(ModelConstants.ALARM_TIME_INDEX));

        // to set the day for the spinner, do this
        int indexOfSpinner = 0;
        for (int i = 0; i < daySpinnerData.length; i++) {

            if (day.equals(daySpinnerData[i])) {
                indexOfSpinner = i;
                break;

            } else {
                continue;
            }
        }

        daySpinner.setSelection(indexOfSpinner);

        // to set the alarm for the alarm spinner, you can easily do this

        switch (Integer.parseInt(alarmIndex)) {
            case 0:
                alarmSpinner.setSelection(0);
                break;
            case 1:
                alarmSpinner.setSelection(1);
                break;
            case 2:
                alarmSpinner.setSelection(2);
                break;
            case 3:
                alarmSpinner.setSelection(3);
                break;
            case 4:
                alarmSpinner.setSelection(4);
            case 5:
                alarmSpinner.setSelection(5);
            default:
                break;
        }

        this.courseCode.setText(courseCode.toUpperCase());

        this.courseTitle.setText(courseTitle);

        this.startTimeButton.setText(startTime);

        this.endTimeButton.setText(endTime);
        if (!lecturer.equals("")) {
            this.lecturer.setText(lecturer);
        }
        if (!venue.equals("")) {
            this.venue.setText(venue);
        }
        realStartTimeInMins = Integer.parseInt(startTimeInMins);
        realEndTimeInMins = Integer.parseInt(endTimeInMins);

    }

    public void updateRelevantFields(String rowId) {

        String daySelected = this.daySelected;
        String courseCode = this.courseCode.getText().toString();
        String courseTitle = this.courseTitle.getText().toString();
        String startTime = this.startTimeButton.getText().toString();
        String endTime = this.endTimeButton.getText().toString();
        int startTimeInMins = this.realStartTimeInMins;
        int endTimeInMins = this.realEndTimeInMins;
        String lecturer = this.lecturer.getText().toString();
        String venue = this.venue.getText().toString();

        int alarmTimeInMins = 0;
        int alarmTimeIndex = 0;
        switch (this.alarmTimeIndex) {

            case 0:
                alarmTimeInMins = -1; // no alarm. good to have a small negative int
                // here
                alarmTimeIndex = 0;
                break;
            case 1:
                alarmTimeInMins = realStartTimeInMins - 15;
                alarmTimeIndex = 1;
                break;
            case 2:
                alarmTimeInMins = realStartTimeInMins - 30;
                alarmTimeIndex = 2;
                break;
            case 3:
                alarmTimeInMins = realStartTimeInMins - 60;
                alarmTimeIndex = 3;
                break;
            case 4:
                alarmTimeInMins = realStartTimeInMins - 120;
                alarmTimeIndex = 4;
                break;
            case 5:
                alarmTimeInMins = realStartTimeInMins - 180;
                alarmTimeIndex = 5;
                break;
            default:
                break;
        }

        // work on the alarm again since there is a change
        ContentValues contentValues = new ContentValues();
        contentValues.put(ModelConstants.DAY, daySelected);
        contentValues.put(ModelConstants.COURSE_CODE, courseCode);
        contentValues.put(ModelConstants.COURSE_TITLE, courseTitle);
        contentValues.put(ModelConstants.REAL_START_TIME_MINS, startTimeInMins);
        contentValues.put(ModelConstants.REAL_END_TIME_MINS, endTimeInMins);
        contentValues.put(ModelConstants.START_TIME_TEXT, startTime);
        contentValues.put(ModelConstants.END_TIME_TEXT, endTime);
        contentValues.put(ModelConstants.LECTURER, lecturer);
        contentValues.put(ModelConstants.VENUE, venue);
        contentValues.put(ModelConstants.REAL_ALARM_TIME_MINS, alarmTimeInMins);
        contentValues.put(ModelConstants.ALARM_TIME_INDEX, alarmTimeIndex);

        dataHandler = new DataHandler(getApplicationContext());
        dataHandler.openDatabase();

        // before update, check if the user has save the particular course for
        // the day and time
        // also check if user wanna save into time that is not available
        Cursor cursor = dataHandler.checkIfCourseTimeIsSaved(rowId,
                daySelected, startTimeInMins);
        Cursor cursor2 = dataHandler.CheckIfTimeIsAvailable(rowId, daySelected,
                startTimeInMins, endTimeInMins);

        if (cursor.moveToFirst() || cursor2.moveToFirst()) {

            // String course =
            // cursor.getString(cursor.getColumnIndex(ModelConstants.COURSE_CODE));
            displayMessage("Error",
                    "You have a course during that time. Please enter the appropriate time.");

        } else {

            dataHandler.updateData(rowIdToFetch, contentValues);

            if (alarmTimeInMins >= 0) {

                Alarm alarm = new Alarm();
                alarm.setAlarm(getBaseContext(), Integer.parseInt(rowId),
                        daySelected, alarmTimeInMins);

                // note that this will cancel the previous alarm because the we
                // are using the same row id in the pendingIntent creation.
                // remember that this will make the Intent.filterEquals() to
                // return true and the pendingIntent will look same
            } else if (alarmTimeInMins < -1) { // since -1 is for none in alarm
                // spinner

                int hrIn24temp = 24 * 60;
                int alarmTimeInMinsHere = hrIn24temp + alarmTimeInMins;
                String dayHere = "";
                if (daySelected.equals("Sunday"))
                    dayHere = "Saturday";
                else if (daySelected.equals("Monday"))
                    dayHere = "Sunday";
                else if (daySelected.equals("Tuesday"))
                    dayHere = "Monday";
                else if (daySelected.equals("Wednesday"))
                    dayHere = "Tuesday";
                else if (daySelected.equals("Thursday"))
                    dayHere = "Wednesday";
                else if (daySelected.equals("Friday"))
                    dayHere = "Thursday";
                else if (daySelected.equals("Saturday"))
                    dayHere = "Friday";
                Alarm alarm = new Alarm();
                alarm.setAlarm(this, Integer.parseInt(rowId), dayHere,
                        alarmTimeInMinsHere);

            }

            // we need to know the status of the checkboxes first to work on
            // muting or vibrate

            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            boolean auto_mute_status = this.sharedPreferences.getBoolean(getString(R.string.pref_checkbox_key), false);
            boolean auto_vibrate_status = this.sharedPreferences.getBoolean(getString(R.string.pref_checkbox_key_2), false);

            MuteDevice muteDevice = new MuteDevice(getBaseContext());
            if (auto_mute_status) {

                muteDevice.setMuteOrVibrate(Integer.parseInt(rowId), daySelected,
                        startTimeInMins, endTimeInMins, "Mute");
            } else if (auto_vibrate_status) {
                muteDevice.setMuteOrVibrate(Integer.parseInt(rowId), daySelected,
                        startTimeInMins, endTimeInMins, "Vibrate");
            }
            displayMessage2("Data Updated", "Data Updated successfully.");
        }
    }

    public void displayMessage(String type, String message) {
        Builder builder = new Builder(this);
        builder.setCancelable(true);
        builder.setTitle(type);
        builder.setMessage(message);
        builder.show();

        // CourseDetails.this.finish();

    }

    public void displayMessage2(String type, String message) {

        Builder builder = new Builder(this);
        builder.setCancelable(true);
        builder.setTitle(type);
        builder.setMessage(message);
        builder.show();

        refresh();

    }

    public void refresh() {
        if (MainActivity.fa != null) {
            MainActivity.fa.finish();
        }
        CourseDetails.activity.finish();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        this.finish();
    }
}
