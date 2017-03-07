package model;

/**
 * Created by ahmed on 21/01/2017.
 */

public class Course {


    private String id;
    private String day;
    private String courseCode;
    private String courseTitle;
    private int startTimeInMins;
    private int endTimeInMins;
    private String startTimeText;
    private String endTimeText;
    private String lecturer;
    private String venue;
    private int alarmTimeInMins;

    public int getAlarmTimeIndex() {
        return alarmTimeIndex;
    }

    public void setAlarmTimeIndex(int alarmTimeIndex) {
        this.alarmTimeIndex = alarmTimeIndex;
    }

    public void setAlarmTimeInMins(int alarmTimeInMins) {
        this.alarmTimeInMins = alarmTimeInMins;
    }

    private int alarmTimeIndex;

    public String getStartTimeText() {
        return startTimeText;
    }

    public void setStartTimeText(String startTimeText) {
        this.startTimeText = startTimeText;
    }

    public String getEndTimeText() {
        return endTimeText;
    }

    public void setEndTimeText(String endTimeText) {
        this.endTimeText = endTimeText;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }
    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getAlarmTimeInMins() {
        return alarmTimeInMins;
    }


    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public int getStartTimeInMins() {
        return startTimeInMins;
    }

    public void setStartTimeInMins(int startTimeInMins) {
        this.startTimeInMins = startTimeInMins;
    }

    public int getEndTimeInMins() {
        return endTimeInMins;
    }

    public void setEndTimeInMins(int endTimeInMins) {
        this.endTimeInMins = endTimeInMins;
    }

    public String getLecturer() {
        return lecturer;
    }

    public void setLecturer(String lecturer) {
        this.lecturer = lecturer;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }



}
