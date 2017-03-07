package data;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;


public class DataHandler extends SQLiteOpenHelper {

	SQLiteDatabase database;
	public DataHandler(Context context) {
		super(context, ModelConstants.DATABASE_NAME, null, ModelConstants.DATABASE_VERSION);
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		try {
			db.execSQL(ModelConstants.CREATE_TABLE);
		} catch (Exception e) {

			e.printStackTrace();


		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		try{
		db.execSQL("DROP TABLE IF EXIST " + ModelConstants.TABLE_NAME);
		//onCreate(db);

		}catch(SQLiteException e){
			e.printStackTrace();

			
		}
	}

    public Cursor checkDay(String day, long timeInMins){

        String columns[] = {ModelConstants.DAY, ModelConstants.REAL_ALARM_TIME_MINS};
        String selection = ModelConstants.DAY + " =? AND " +ModelConstants.REAL_ALARM_TIME_MINS +" >= ?";
        String selectionArgs[] = {day,Long.toString(timeInMins)};

       Cursor cursor = database.query(ModelConstants.DAY,columns,selection,selectionArgs,null,null,ModelConstants.REAL_ALARM_TIME_MINS);
        return cursor;
    }
	
	public DataHandler openDatabase(){
		
		database = this.getWritableDatabase();

		return this;
	}

	public long insertData(ContentValues contentValues){
		
		long value = database.insert(ModelConstants.TABLE_NAME, null, contentValues);

		return value;
		
	}
	public Cursor checkIfCourseTimeIsSaved(String day, int startTimeInMins){
		
		String[] columns = {ModelConstants.COURSE_CODE};
		String selection = ModelConstants.DAY + " = ? AND " + ModelConstants.REAL_START_TIME_MINS + " = ?";
		String[] selectionArgs = {day, Integer.toString(startTimeInMins)};
		return database.query(ModelConstants.TABLE_NAME, columns, selection, selectionArgs, null, null, null);
		
	}
	public Cursor checkForCourses(){
		String query = "SELECT * FROM " + ModelConstants.TABLE_NAME;
		Cursor cursor = database.rawQuery(query, null);
		return cursor;
	}
	public Cursor retrieveData(String day){
		String[] columns = {ModelConstants.ID, ModelConstants.COURSE_CODE ,ModelConstants.COURSE_TITLE,ModelConstants.REAL_START_TIME_MINS,ModelConstants.START_TIME_TEXT, ModelConstants.END_TIME_TEXT,ModelConstants.REAL_START_TIME_MINS};
		String selection = ModelConstants.DAY + " = ?";
		String[] selectionArgs = {day};
		Cursor cursor = database.query(ModelConstants.TABLE_NAME, columns, selection, selectionArgs, null, null, ModelConstants.REAL_START_TIME_MINS + " ASC");

		return cursor;
	}
	public Cursor retrieveDataForDetails(int rowIdToFetch){
		
		String selectionArgs[] = {Integer.toString(rowIdToFetch)};
		String[] columns = {ModelConstants.DAY,ModelConstants.COURSE_CODE ,ModelConstants.COURSE_TITLE,ModelConstants.START_TIME_TEXT,ModelConstants.END_TIME_TEXT,
						ModelConstants.LECTURER, ModelConstants.VENUE, ModelConstants.REAL_ALARM_TIME_MINS,ModelConstants.ALARM_TIME_INDEX};
		return database.query(ModelConstants.TABLE_NAME, columns,ModelConstants.ID+ " = ?", selectionArgs, null, null, null);
		//String search_query = "SELECT * FROM timetable WHERE _id ='"+rowId.toString()+"'";
		//return  database.rawQuery(search_query,null);
		
		
	}
	public Cursor retrieveDataForEdit(int rowIdToFetch){
		
		
		String selectionArgs[] = {Integer.toString(rowIdToFetch)};
		String[] columns = {ModelConstants.DAY,ModelConstants.COURSE_CODE ,ModelConstants.COURSE_TITLE,ModelConstants.REAL_START_TIME_MINS,ModelConstants.REAL_END_TIME_MINS,ModelConstants.START_TIME_TEXT,ModelConstants.END_TIME_TEXT,
						ModelConstants.LECTURER, ModelConstants.VENUE, ModelConstants.ALARM_TIME_INDEX};
		return database.query(ModelConstants.TABLE_NAME, columns,ModelConstants.ID+ " = ?", selectionArgs, null, null, null);
		
		
		
	}
	public Cursor getDataToPrepareAlarmAndMute(){
		
		String[] columns = {ModelConstants.ID, ModelConstants.DAY,ModelConstants.REAL_START_TIME_MINS, ModelConstants.REAL_END_TIME_MINS, ModelConstants.REAL_ALARM_TIME_MINS};
	return database.query( ModelConstants.TABLE_NAME, columns, null, null, null, null, null);
		
	}
	
	public Cursor getDataForNotification(String rowId){
		
		String[] columns = {ModelConstants.DAY,ModelConstants.COURSE_CODE, ModelConstants.START_TIME_TEXT,ModelConstants.END_TIME_TEXT,ModelConstants.REAL_START_TIME_MINS,ModelConstants.REAL_END_TIME_MINS,ModelConstants.REAL_ALARM_TIME_MINS};
		String[] selectionArgs = {rowId}; 
		return database.query( ModelConstants.TABLE_NAME, columns, ModelConstants.ID+ " = ?", selectionArgs, null, null, null);
		
		
	}
	
	
	public void deleteACourse(String rowId){
		
			database.delete(ModelConstants.TABLE_NAME, ModelConstants.ID + " = ?", new String[] {rowId});
		
	}
	public void deleteAllCourses(){
		
		database.delete(ModelConstants.TABLE_NAME, null,null);
		
		
	}
	public void updateData(String rowId, ContentValues contentValues){
		
		database.update(ModelConstants.TABLE_NAME, contentValues, ModelConstants.ID + " = ?", new String[] {rowId});

	}
	
	public Cursor retrieveAllCoursesForBootTimeAlarm(){
		
		
		String columns[] = {ModelConstants.ID, ModelConstants.DAY, ModelConstants.REAL_ALARM_TIME_MINS}; 
		
		return database.query(ModelConstants.TABLE_NAME, columns, null, null, null, null, null);
		
		
	}
	public Cursor retrieveAllCoursesForMutingDevice(){
		
		
		String columns[] = {ModelConstants.ID, ModelConstants.DAY, ModelConstants.REAL_START_TIME_MINS,ModelConstants.REAL_END_TIME_MINS}; 
		
		return database.query(ModelConstants.TABLE_NAME, columns, null, null, null, null, null);
		
	}
	public Cursor retrieveAllCoursesForAlarmDelete(){
		
		String columns[] = {ModelConstants.ID, ModelConstants.REAL_ALARM_TIME_MINS};
		return database.query(ModelConstants.TABLE_NAME, columns, null, null, null, null, null);
	}
	public void updateAfterAlarmDelete(ContentValues contentValues){
		
		database.update(ModelConstants.TABLE_NAME, contentValues, null, null);
		
		
	}
	
	public Cursor retrieveDataToUnmuteACourse(String rowId){
		
		String[] columns = {ModelConstants.ID, ModelConstants.DAY,ModelConstants.REAL_START_TIME_MINS, ModelConstants.REAL_END_TIME_MINS};
		return database.query( ModelConstants.TABLE_NAME, columns, ModelConstants.ID + " = ?", new String[] {rowId}, null, null, null);
		
	}

	public Cursor CheckIfTimeIsAvailable(String daySelected,
			int realStartTimeInMins, int realEndTimeInMins) {
		// TODO Auto-generated method stub
		String columns[] = {ModelConstants.COURSE_CODE};
		String selection = ModelConstants.DAY + " = ? AND ((" + ModelConstants.REAL_START_TIME_MINS + " < ? AND " + ModelConstants.REAL_END_TIME_MINS + " > ? )OR (" + ModelConstants.REAL_START_TIME_MINS + " < ? AND " + ModelConstants.REAL_END_TIME_MINS + " > ?))";
		String[] selectionArgs = {daySelected,Integer.toString(realStartTimeInMins),Integer.toString(realStartTimeInMins),Integer.toString(realEndTimeInMins),Integer.toString(realEndTimeInMins)};
		return database.query( ModelConstants.TABLE_NAME, columns,selection, selectionArgs, null, null, null);
		
	}
	//the two below method are overloaded. these are the ones used in the courseedit activity to update
public Cursor checkIfCourseTimeIsSaved(String rowId,String day, int startTimeInMins){
		
		String[] columns = {ModelConstants.COURSE_CODE};
		String selection = ModelConstants.ID + " != ? AND " + ModelConstants.DAY + " = ? AND " + ModelConstants.REAL_START_TIME_MINS + " = ?";
		String[] selectionArgs = {rowId,day, Integer.toString(startTimeInMins)};
		return database.query(ModelConstants.TABLE_NAME, columns, selection, selectionArgs, null, null, null);
		
	}
public Cursor CheckIfTimeIsAvailable(String rowId,String daySelected,
		int realStartTimeInMins, int realEndTimeInMins) {
	// TODO Auto-generated method stub
	String columns[] = {ModelConstants.COURSE_CODE};
	String selection = ModelConstants.ID + " != ? AND "+ModelConstants.DAY + " = ? AND ((" + ModelConstants.REAL_START_TIME_MINS + " < ? AND " + ModelConstants.REAL_END_TIME_MINS + " > ? )OR (" + ModelConstants.REAL_START_TIME_MINS + " < ? AND " + ModelConstants.REAL_END_TIME_MINS + " > ?))";
	String[] selectionArgs = {rowId,daySelected,Integer.toString(realStartTimeInMins),Integer.toString(realStartTimeInMins),Integer.toString(realEndTimeInMins),Integer.toString(realEndTimeInMins)};
	return database.query( ModelConstants.TABLE_NAME, columns,selection, selectionArgs, null, null, null);
	
}

public Cursor getDataToUpdateField(String courseCode){
	
		String[] columns = {ModelConstants.COURSE_TITLE, ModelConstants.LECTURER, ModelConstants.VENUE};
		
		String selection = ModelConstants.COURSE_CODE + " = ?";
		String selectionArgs[] = {courseCode};
		
		return database.query(ModelConstants.TABLE_NAME, columns, selection, selectionArgs, null, null, null);

	
}

}
