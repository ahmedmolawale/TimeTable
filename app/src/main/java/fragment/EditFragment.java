package fragment;

import alarm.Alarm;

import android.app.AlertDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import activity.AboutActivity;
import activity.FeedbackActivity;
import activity.HelpActivity;
import activity.Settings;

import com.commonsware.cwac.merge.MergeAdapter;
import com.domainname.timetablemanager.R;
import com.google.android.gms.ads.AdRequest.Builder;
import com.google.android.gms.ads.AdView;


import data.DataHandler;
import data.ModelConstants;
import model.Course;
import mute.MuteDevice;

public class EditFragment extends Fragment {
    int alarmTimeIndex;
    EditText courseCode;
    EditText courseTitle;
    DataHandler dataHandler;
    String daySelected;

    int endHourIn12HrFormat;
    int endMin;
    Button endTime;
    TextView endTimeText;
    EditText lecturer;
    int realEndTimeInMins = 8 * 60;
    int realStartTimeInMins = 7 * 60;
    Button save;
    SharedPreferences sharedPreferences;
    Spinner spinner;
    String[] spinnerData;
    Spinner spinnerForAlarm;
    String[] spinnerForAlarmData;
    int startHourIn12HrFormat;
    int startMin;
    Button startTime;
    TextView startTimeText;
    View v;
    EditText venue;

    private class EndTimePickerHandler implements OnTimeSetListener {
        private EndTimePickerHandler() {
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            if (hourOfDay > 12) {
                EditFragment.this.endHourIn12HrFormat = hourOfDay - 12;
                EditFragment.this.realEndTimeInMins = (hourOfDay * 60) + minute;
                EditFragment.this.endMin = minute;
                if (EditFragment.this.endMin < 10) {
                    EditFragment.this.endTimeText.setText(EditFragment.this.endHourIn12HrFormat + ":0" + EditFragment.this.endMin + " PM");
                } else {
                    EditFragment.this.endTimeText.setText(EditFragment.this.endHourIn12HrFormat + ":" + EditFragment.this.endMin + " PM");
                }
            } else if (hourOfDay == 12) {
                EditFragment.this.endHourIn12HrFormat = hourOfDay;
                EditFragment.this.realEndTimeInMins = (hourOfDay * 60) + minute;
                EditFragment.this.endMin = minute;
                if (EditFragment.this.endMin < 10) {
                    EditFragment.this.endTimeText.setText(EditFragment.this.endHourIn12HrFormat + ":0" + EditFragment.this.endMin + " PM");
                } else {
                    EditFragment.this.endTimeText.setText(EditFragment.this.endHourIn12HrFormat + ":" + EditFragment.this.endMin + " PM");
                }
            } else if (hourOfDay == 0) {
                EditFragment.this.endHourIn12HrFormat = hourOfDay + 12;
                EditFragment.this.realEndTimeInMins = (hourOfDay * 60) + minute;
                EditFragment.this.endMin = minute;
                if (EditFragment.this.endMin < 10) {
                    EditFragment.this.endTimeText.setText(EditFragment.this.endHourIn12HrFormat + ":0" + EditFragment.this.endMin + " AM");
                } else {
                    EditFragment.this.endTimeText.setText(EditFragment.this.endHourIn12HrFormat + ":" + EditFragment.this.endMin + " AM");
                }
            } else {
                EditFragment.this.endHourIn12HrFormat = hourOfDay;
                EditFragment.this.realEndTimeInMins = (hourOfDay * 60) + minute;
                EditFragment.this.endMin = minute;
                if (EditFragment.this.endMin < 10) {
                    EditFragment.this.endTimeText.setText(EditFragment.this.endHourIn12HrFormat + ":0" + EditFragment.this.endMin + " AM");
                } else {
                    EditFragment.this.endTimeText.setText(EditFragment.this.endHourIn12HrFormat + ":" + EditFragment.this.endMin + " AM");
                }
            }
        }
    }

    private class StartTimePickerHandler implements OnTimeSetListener {
        private StartTimePickerHandler() {
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            if (hourOfDay > 12) {
                startHourIn12HrFormat = hourOfDay - 12;
                realStartTimeInMins = (hourOfDay * 60) + minute;
                startMin = minute;
                if (startMin < 10) {
                    startTimeText.setText(startHourIn12HrFormat + ":0" + startMin + " PM");
                } else {
                    startTimeText.setText(startHourIn12HrFormat + ":" + startMin + " PM");
                }
            } else if (hourOfDay == 12) {
                startHourIn12HrFormat = hourOfDay;
                realStartTimeInMins = (hourOfDay * 60) + minute;
                startMin = minute;
                if (startMin < 10) {
                    startTimeText.setText(startHourIn12HrFormat + ":0" + startMin + " PM");
                } else {
                    startTimeText.setText(startHourIn12HrFormat + ":" + startMin + " PM");
                }
            } else if (hourOfDay == 0) {
                startHourIn12HrFormat = hourOfDay + 12;
                realStartTimeInMins = (hourOfDay * 60) + minute;
                startMin = minute;
                if (startMin < 10) {
                    startTimeText.setText(startHourIn12HrFormat + ":0" + startMin + " AM");
                } else {
                    startTimeText.setText(startHourIn12HrFormat + ":" + startMin + " AM");
                }
            } else {
                startHourIn12HrFormat = hourOfDay;
                realStartTimeInMins = (hourOfDay * 60) + minute;
                startMin = minute;
                if (startMin < 10) {
                    startTimeText.setText(startHourIn12HrFormat + ":0" + startMin + " AM");
                } else {
                    startTimeText.setText(startHourIn12HrFormat + ":" + startMin + " AM");
                }
            }
        }
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_add_items, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save: /*2131689631*/
                saveCourse();
                return true;
            case R.id.action_settings1: /*2131689633*/
                startSettingsActivity();
                return true;
            case R.id.action_feedback1: /*2131689634*/
                feedBackActivity();
                return true;
            case R.id.action_about1: /*2131689635*/
                startAboutActivity();
                return true;
//            case R.id.action_help1: /*2131689642*/
//				helpActivity();
//				return true;
            default:
                return false;
        }
    }

    private void helpActivity() {
        startActivity(new Intent(getActivity(), HelpActivity.class));
    }

    public void startAboutActivity() {
        startActivity(new Intent(getActivity(), AboutActivity.class));
    }

    public void startSettingsActivity() {
        startActivity(new Intent(getActivity(), Settings.class));
    }

    public void feedBackActivity() {
        startActivity(new Intent(getActivity(), FeedbackActivity.class));
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.v = inflater.inflate(R.layout.edit_fragment_layout, container, false);
        setHasOptionsMenu(true);
        getActivity().supportInvalidateOptionsMenu();
        ((AdView) this.v.findViewById(R.id.adView2)).loadAd(new Builder().build());

        this.spinner = (Spinner) this.v.findViewById(R.id.spinner);
        this.spinnerData = getResources().getStringArray(R.array.days);
        this.spinner.setAdapter(new ArrayAdapter(this.spinner.getContext(), R.layout.spinner_item, this.spinnerData));
        this.spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int index = parent.getSelectedItemPosition();
                EditFragment.this.daySelected = EditFragment.this.spinnerData[index];
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        this.spinnerForAlarm = (Spinner) this.v.findViewById(R.id.spinnerForAlarm);
        this.spinnerForAlarmData = getResources().getStringArray(R.array.alarm);
        this.spinnerForAlarm.setAdapter(new ArrayAdapter(this.spinnerForAlarm.getContext(), R.layout.spinner_item, this.spinnerForAlarmData));
        this.spinnerForAlarm.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                alarmTimeIndex = parent.getSelectedItemPosition();
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        this.startTimeText = (TextView) this.v.findViewById(R.id.start_time_text);
        this.endTimeText = (TextView) this.v.findViewById(R.id.end_time_text);
        this.startTimeText.setText("7:00 AM");
        this.endTimeText.setText("8:00 AM");
        this.startTime = (Button) this.v.findViewById(R.id.start_time_button);
        this.endTime = (Button) this.v.findViewById(R.id.end_time_button);
        this.startTime.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                TimeDialog timePicker = new TimeDialog();
                timePicker.setDetails(getActivity(), new StartTimePickerHandler(), 7, 0);
                timePicker.show(getFragmentManager(), null);
            }
        });
        this.endTime.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                TimeDialog timePicker = new TimeDialog();
                timePicker.setDetails(getActivity(), new EndTimePickerHandler(), 8, 0);
                timePicker.show(getFragmentManager(), null);
            }
        });

        this.courseCode = (EditText) this.v.findViewById(R.id.course_code);
        this.courseTitle = (EditText) this.v.findViewById(R.id.course_title);
        this.lecturer = (EditText) this.v.findViewById(R.id.lecturer);
        this.venue = (EditText) this.v.findViewById(R.id.venue);
        this.courseTitle.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && !courseCode.getText().toString().equals("") && !courseTitle.getText().toString().equals("")) {
                    dataHandler = new DataHandler(getActivity());
                    dataHandler.openDatabase();
                    Cursor cursor = EditFragment.this.dataHandler.getDataToUpdateField(EditFragment.this.courseCode.getText().toString());
                    if (cursor.moveToFirst()) {
                        EditFragment.this.confirmationDialogToAutoComplete(cursor, "Course Found", "Update other fields from database?");
                    }
                }
            }
        });
        this.courseTitle.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (!EditFragment.this.courseCode.getText().toString().equals("") && EditFragment.this.courseTitle.getText().toString().equals("")) {
                    EditFragment.this.dataHandler = new DataHandler(getActivity());
                    EditFragment.this.dataHandler.openDatabase();
                    Cursor cursor = EditFragment.this.dataHandler.getDataToUpdateField(EditFragment.this.courseCode.getText().toString());
                    if (cursor.moveToFirst()) {
                        EditFragment.this.confirmationDialogToAutoComplete(cursor, "Course Found", "Update other fields from database?");
                    }
                }
            }
        });
        this.save = (Button) this.v.findViewById(R.id.save);
        this.save.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                saveCourse();
            }
        });
        return this.v;
    }

    private void saveCourse() {
        if (this.courseCode.getText().toString().isEmpty()) {
            this.courseCode.setError("Enter Course Code");
        } else if (this.courseTitle.getText().toString().isEmpty()) {
            this.courseTitle.setError("Enter Course Title");
        } else if (this.realStartTimeInMins == realEndTimeInMins) {
            displayMessage("Timing not correct", "Lecture's start time cannot be the same with end time.");
        } else if (this.realStartTimeInMins > this.realEndTimeInMins) {
            confirmationDialog("Alert", "The time seems not to be correct. Do you want to continue?");
        } else if (this.lecturer.getText().toString().isEmpty()) {
            confirmationDialog("Confirmation", "Do you want to leave the Lecturer's name empty and save data ?");
        } else if (this.venue.getText().toString().isEmpty()) {
            confirmationDialog("Confirmation", "Do you want to leave the Venue empty and save data?");
        } else {
            saveData();
        }
    }

    public void displayMessage(String type, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(type);
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.show();
    }

    public void confirmationDialog(String type, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(type);
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                EditFragment.this.saveData();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create().show();
    }

    public void saveData() {
        String daySelected = this.daySelected;
        String courseCode = this.courseCode.getText().toString();
        String courseTitle = this.courseTitle.getText().toString();
        String startTimeText = this.startTimeText.getText().toString();
        String endTimeText = this.endTimeText.getText().toString();
        int realStartTimeInMins = this.realStartTimeInMins;
        int realEndTimeInMins = this.realEndTimeInMins;
        String lecturer = this.lecturer.getText().toString();
        String venue = this.venue.getText().toString();

        Course course = new Course();
        course.setDay(daySelected);
        course.setCourseCode(courseCode);
        course.setCourseTitle(courseTitle);
        course.setStartTimeInMins(realStartTimeInMins);
        course.setEndTimeInMins(realEndTimeInMins);
        course.setStartTimeText(startTimeText);
        course.setEndTimeText(endTimeText);
        course.setLecturer(lecturer);
        course.setVenue(venue);

        int alarmTimeInMins = 0;
        int alarmTimeIndex = 0;
        switch (this.alarmTimeIndex) {
            case 0:
                alarmTimeInMins = -1;
                alarmTimeIndex = 0;
                break;
            case 1:
                alarmTimeInMins = realStartTimeInMins - 15;//start time minus 15 mins
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
        }
        course.setAlarmTimeInMins(alarmTimeInMins);
        course.setAlarmTimeIndex(alarmTimeIndex);

        ContentValues contentValues = new ContentValues();
        contentValues.put(ModelConstants.DAY, course.getDay());
        contentValues.put(ModelConstants.COURSE_CODE, course.getCourseCode());
        contentValues.put(ModelConstants.COURSE_TITLE, course.getCourseTitle());
        contentValues.put(ModelConstants.REAL_START_TIME_MINS, course.getStartTimeInMins());
        contentValues.put(ModelConstants.REAL_END_TIME_MINS, course.getEndTimeInMins());
        contentValues.put(ModelConstants.START_TIME_TEXT, course.getStartTimeText());
        contentValues.put(ModelConstants.END_TIME_TEXT, course.getEndTimeText());
        contentValues.put(ModelConstants.LECTURER, course.getLecturer());
        contentValues.put(ModelConstants.VENUE, course.getVenue());
        contentValues.put(ModelConstants.REAL_ALARM_TIME_MINS, course.getAlarmTimeInMins());
        contentValues.put(ModelConstants.ALARM_TIME_INDEX, course.getAlarmTimeIndex());
        this.dataHandler = new DataHandler(getActivity());
        this.dataHandler.openDatabase();
        Cursor cursor = dataHandler.checkIfCourseTimeIsSaved(daySelected, realStartTimeInMins);
        Cursor cursor2 = dataHandler.CheckIfTimeIsAvailable(daySelected, realStartTimeInMins, realEndTimeInMins);
        if (cursor.moveToFirst() || cursor2.moveToFirst()) {
            String courseCodeHere = null;
            if (cursor.moveToFirst()) {
                courseCodeHere = cursor.getString(cursor.getColumnIndex(ModelConstants.COURSE_CODE));
            }
            if (cursor2.moveToFirst()) {
                courseCodeHere = cursor2.getString(cursor2.getColumnIndex(ModelConstants.COURSE_CODE));
            }
            displayMessage("Error", "You have " + courseCodeHere + " during that time. Please enter the appropriate time.");
            return;
        }
        this.dataHandler.insertData(contentValues);
        this.dataHandler.close();
        displayMessage("Data saved", "Data saved successfully");
        clearText();
        prepareAlarm();
        prepareMute();
    }

    public void prepareAlarm() {

        dataHandler = new DataHandler(getActivity());
        dataHandler.openDatabase();
        Cursor cursorForAlarm = dataHandler.getDataToPrepareAlarmAndMute();
        cursorForAlarm.moveToLast();
        String rowId = cursorForAlarm.getString(cursorForAlarm.getColumnIndex(ModelConstants.ID));
        String day = cursorForAlarm.getString(cursorForAlarm.getColumnIndex(ModelConstants.DAY));
        String alarmTimeInMins = cursorForAlarm.getString(cursorForAlarm.getColumnIndex(ModelConstants.REAL_ALARM_TIME_MINS));
        this.dataHandler.close();
        if (Integer.parseInt(alarmTimeInMins) >= 0) {
            new Alarm().setAlarm(getActivity(), Integer.parseInt(rowId), day, Integer.parseInt(alarmTimeInMins));
        } else if (Integer.parseInt(alarmTimeInMins) < -1) {
            int hrIn24temp = 24 * 60;
            int alarmTimeInMinsHere = hrIn24temp + Integer.parseInt(alarmTimeInMins);
            String dayHere = "";
            if (day.equals("Sunday")) {
                dayHere = "Saturday";
            } else if (day.equals("Monday")) {
                dayHere = "Sunday";
            } else if (day.equals("Tuesday")) {
                dayHere = "Monday";
            } else if (day.equals("Wednesday")) {
                dayHere = "Tuesday";
            } else if (day.equals("Thursday")) {
                dayHere = "Wednesday";
            } else if (day.equals("Friday")) {
                dayHere = "Thursday";
            } else if (day.equals("Saturday")) {
                dayHere = "Friday";
            }
            new Alarm().setAlarm(getActivity(), Integer.parseInt(rowId), dayHere, alarmTimeInMinsHere);
        }
    }

    public void prepareMute() {
        this.dataHandler = new DataHandler(getActivity());
        this.dataHandler.openDatabase();
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean auto_mute_status = this.sharedPreferences.getBoolean(getString(R.string.pref_checkbox_key), false);
        boolean auto_vibrate_status = this.sharedPreferences.getBoolean(getString(R.string.pref_checkbox_key_2), false);
        if (auto_mute_status || auto_vibrate_status) {
            Cursor cursor2 = dataHandler.getDataToPrepareAlarmAndMute();
            cursor2.moveToLast();
            String id = cursor2.getString(cursor2.getColumnIndex(ModelConstants.ID));
            String day = cursor2.getString(cursor2.getColumnIndex(ModelConstants.DAY));
            String startTimeInMins = cursor2.getString(cursor2.getColumnIndex(ModelConstants.REAL_START_TIME_MINS));
            String endTimeInMins = cursor2.getString(cursor2.getColumnIndex(ModelConstants.REAL_END_TIME_MINS));
            dataHandler.close();
            MuteDevice muteDevice = new MuteDevice(getActivity());
            if (auto_mute_status) {
                muteDevice.setMuteOrVibrate(Integer.parseInt(id), day, Integer.parseInt(startTimeInMins), Integer.parseInt(endTimeInMins), "Mute");
            } else if (auto_vibrate_status) {
                muteDevice.setMuteOrVibrate(Integer.parseInt(id), day, Integer.parseInt(startTimeInMins), Integer.parseInt(endTimeInMins), "Vibrate");
            }
        }
    }

    public void clearText() {
        courseCode.setText("");
        courseTitle.setText("");
        lecturer.setText("");
        venue.setText("");
        courseCode.requestFocus();
        ;
    }

    public void confirmationDialogToAutoComplete(final Cursor cursor, String type, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(type);
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.setPositiveButton("YES!", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                EditFragment.this.updateField(cursor);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create().show();
    }

    public void updateField(Cursor cursor) {
        cursor.moveToFirst();
        String title = cursor.getString(cursor.getColumnIndex(ModelConstants.COURSE_TITLE));
        String lecturer = cursor.getString(cursor.getColumnIndex(ModelConstants.LECTURER));
        String venue = cursor.getString(cursor.getColumnIndex(ModelConstants.VENUE));
        courseTitle.setText(title);
        this.lecturer.setText(lecturer);
        this.venue.setText(venue);
        cursor.close();
    }
}
