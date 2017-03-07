package data;

public abstract class ModelConstants {
	
	//DATABASE CONSTANTS
	public static final String DATABASE_NAME = "studentData.db";
	public static final int DATABASE_VERSION = 1;
	//TABLE CONNSTANTS
	public static final String TABLE_NAME = "timetable";
	//COLUMN NAMES
	public static final String ID = "_id";
	public static final String DAY = "day";
	public static final String COURSE_CODE = "coursecode";
	public static final String COURSE_TITLE = "coursetitle";
	public static final String REAL_START_TIME_MINS = "realstarttime";
	public static final String REAL_END_TIME_MINS = "realendtime";
	public static final String START_TIME_TEXT = "starttime";
	public static final String END_TIME_TEXT = "endtime";
	public static final String LECTURER = "lecturer";
	public static final String VENUE = "venue";
	public static final String REAL_ALARM_TIME_MINS = "alarmtime";
	public static final String ALARM_TIME_INDEX = "alarmtimeindex";

	
	//TABLE CREATION QUERY
	public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
									"("+ID + " INTEGER PRIMARY KEY," + DAY + " TEXT NOT NULL," + COURSE_CODE + " TEXT NOT NULL,"
									+ COURSE_TITLE + " TEXT NOT NULL," + REAL_START_TIME_MINS + " INTEGER NOT NULL," + 
									REAL_END_TIME_MINS + " INTEGER NOT NULL," + START_TIME_TEXT + " TEXT NOT NULL," + END_TIME_TEXT + " TEXT NOT NULL,"
									+ LECTURER + " TEXT,"
									+ VENUE + " TEXT," + REAL_ALARM_TIME_MINS + " INTEGER," + ALARM_TIME_INDEX + " INTEGER);";

}