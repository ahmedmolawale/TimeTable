package activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.ahmedmolawale.timetablemanager.R;


public class SevenPointGradeActivity extends ActionBarActivity {
	
	
	private Spinner spinnerForUnits, spinnerForGrades;
	private String[] spinnerForUnitsData, spinnerForGradesData;
	private  ViewGroup viewGroup;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grade_point_layout);
        setTitle("Seven Point GP System");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		viewGroup = (ViewGroup) findViewById(R.id.container);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.grade_point, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch(item.getItemId()){
		
		case R.id.action_add_item:
			findViewById(R.id.empty).setVisibility(View.GONE);
            addNewChild();
            return true;
			
		case R.id.action_compute:
			computeGradePoint();
			return true;
		
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void addNewChild(){

		 // Instantiate a new "row" view.
        final ViewGroup newView = (ViewGroup) LayoutInflater.from(this).inflate(
                R.layout.gp_list_item, viewGroup, false);
        
        spinnerForUnits = (Spinner)newView.findViewById(R.id.spinner_units);
		spinnerForUnitsData = getResources().getStringArray(R.array.units);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(spinnerForUnits.getContext(),
				R.layout.spinner_item, spinnerForUnitsData);
	
	
		spinnerForUnits.setAdapter(adapter);
		
		 spinnerForGrades = (Spinner)newView.findViewById(R.id.spinner_grades);
			spinnerForGradesData = getResources().getStringArray(R.array.gradesForSevenPointsScale);
			
			ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(spinnerForGrades.getContext(),
					R.layout.spinner_item,spinnerForGradesData);
		
			spinnerForGrades.setAdapter(adapter1);
       
        newView.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Remove the row from its parent (the container view).
                // Because viewGroup has android:animateLayoutChanges set to true,
                // this removal is automatically animated.
                viewGroup.removeView(newView);

                // If there are no rows remaining, show the empty view.
                if (viewGroup.getChildCount() == 0) {
                    findViewById(R.id.empty).setVisibility(View.VISIBLE);
                }
            }
        });
        
        // Because mContainerView has android:animateLayoutChanges set to true,
        // adding this view is automatically animated.
       viewGroup.addView(newView, 0);
       
	}

	
		private void computeGradePoint(){
			
			if(viewGroup.getChildCount() == 0){
				
				displayMessage("Message","Please add at least a course.");
				
			}else{
				boolean isCorrect = checkIfCoursesInputedCorrectly();
				
				
				if(isCorrect){
					confirmationDialog("Calculate", "Do you want to calculate Grade Point Average now?");
				
				}else{
					
					displayMessage("Error", "Data not inputed correctly!. Please check through.");
					
				}
			}
			
		}
		private void confirmationDialog(String type, String message) {
			// here is working with the when lecturer or venue is empty
			Builder builder = new Builder(this);
			builder.setTitle(type);
			
			builder.setMessage(message);
			builder.setCancelable(true);
			// builder.show();
			builder.setIcon(android.R.drawable.ic_dialog_info);

			builder.setPositiveButton(android.R.string.yes,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// continue with save
							calculateGradePoint();

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
		private void calculateGradePoint(){
			
			int noOfCourses = viewGroup.getChildCount();
			int[] pointsArray ={7,6,5,4,3,2,1,0};
			int totalUnits = 0;
			double totalPoints = 0.0;
			double gradePoint;
			
			for(int i = 0; i < noOfCourses; i++ ){
			View view = viewGroup.getChildAt(i);
			
			Spinner unit = (Spinner) view.findViewById(R.id.spinner_units);
			Spinner grade = (Spinner) view.findViewById(R.id.spinner_grades);
			
			int unitSelected = unit.getSelectedItemPosition();
			int pointScored = pointsArray[grade.getSelectedItemPosition()-1];   //reason this wella, its awesome
			
			totalPoints += unitSelected * pointScored;
			totalUnits +=  unitSelected; 
			
			}
			gradePoint = totalPoints/totalUnits;
			String classOfGrade;
			if(gradePoint >= 6.0)
				classOfGrade = "First Class.\nCongratulations!!!";
				else if(gradePoint >= 4.6 && gradePoint <=5.9)
					classOfGrade = "Second Class (Upper Division).";
				else if(gradePoint >= 2.6 && gradePoint <= 4.5)
					classOfGrade = "Second Class (Lower Division).";
				else if(gradePoint >= 1.6 && gradePoint <=2.5)
					classOfGrade = "Third Class.";
				else
					classOfGrade = "Pass";
			String gradePointMessage = String.format("Your Grade Point is  %.1f\nClass of Grade: %s", gradePoint,classOfGrade);
			displayMessage("Grade Point", gradePointMessage);
			
		}
		private boolean checkIfCoursesInputedCorrectly(){
			
			int noOfCourses = viewGroup.getChildCount();
			for(int i = 0; i < noOfCourses; i++ ){
				View view = viewGroup.getChildAt(i);
				
				Spinner unit = (Spinner) view.findViewById(R.id.spinner_units);
				Spinner grade = (Spinner) view.findViewById(R.id.spinner_grades);
				if(unit.getSelectedItemPosition() == 0 || grade.getSelectedItemPosition() == 0){
					return false;
				}else{
					continue;
				}
			}
			return true;
		}
		
		private void displayMessage(String type, String message){
			
			Builder builder = new Builder(this);
			builder.setTitle(type);
			builder.setMessage(message);
			builder.setCancelable(true); // although, default is true
			builder.show();
			}
}
