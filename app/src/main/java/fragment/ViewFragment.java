package fragment;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import activity.AboutActivity;
import activity.CourseDetails;
import activity.FeedbackActivity;
import activity.HelpActivity;
import activity.MainActivity;
import activity.Settings;

import com.commonsware.cwac.merge.MergeAdapter;
import com.ahmedmolawale.timetablemanager.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;


import data.DataHandler;
import data.ModelConstants;

public class ViewFragment extends ListFragment {
    AdView mAdView;
    AdRequest adRequest, adRequest1;
    DataHandler dataHandler;
    Button day;
    MergeAdapter mergeAdapter;
    ArrayList<HashMap<String, String>> arrayList;
    InterstitialAd interstitial;

    TextView rowId;

    //public static final String ROW_ID = "com.google.timetablemanager.row_id"; Useless being, you wasted two days!
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Auto-generated method stub

        inflater.inflate(R.menu.menu_items, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case R.id.action_help:
                startHelpActivity();
                return true;
            case R.id.action_about:
                startAboutActivity();
                return true;
            case R.id.action_settings:
                startSettingsActivity();
                return true;

            case R.id.action_feedback:
                feedBackActivity();
                return true;

        }
        return false;

    }

    private void startHelpActivity() {
        Intent intent = new Intent(getActivity(), HelpActivity.class);
        startActivity(intent);
    }

    public void refresh() {

        MainActivity.fa.finish();
        Intent i = new Intent(getActivity(), MainActivity.class);
        startActivity(i);

    }

    public void startAboutActivity() {

        Intent intent = new Intent(getActivity(), AboutActivity.class);
        startActivity(intent);

    }

    public void startSettingsActivity() {

        Intent intent = new Intent(getActivity(), Settings.class);
        startActivity(intent);

    }

    public void feedBackActivity() {
        Intent intent = new Intent(getActivity(), FeedbackActivity.class);
        startActivity(intent);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub


        setHasOptionsMenu(true);
        getActivity().supportInvalidateOptionsMenu();
        View v;

        dataHandler = new DataHandler(getActivity());
        dataHandler.openDatabase();

        Cursor cursor = dataHandler.checkForCourses();   //this check if any course is present in the table
        if (cursor.moveToFirst()) {   //courses are present..i.e table is not empty
            v = inflater.inflate(R.layout.view_layout, container, false);
            mAdView = (AdView) v.findViewById(R.id.adView1);

            String[] days = getResources().getStringArray(R.array.days);
            mergeAdapter = new MergeAdapter();
            //dataHandler.openDatabase();
            for (int i = 0; i < days.length; i++) {

                Cursor cur = dataHandler.retrieveData(days[i]); //retrieving data for monday

                if (cur.moveToFirst()) {  //at least a course is registered for today
                    arrayList = new ArrayList<HashMap<String, String>>();
                    do {

                        HashMap<String, String> map = new HashMap<String, String>();
                        String id = cur.getString(cur.getColumnIndex(ModelConstants.ID));
                        String courseCode = cur.getString(cur.getColumnIndex(ModelConstants.COURSE_CODE));
                        String courseTitle = cur.getString(cur.getColumnIndex(ModelConstants.COURSE_TITLE));
                        String startTime = cur.getString(cur.getColumnIndex(ModelConstants.START_TIME_TEXT));
                        String endTime = cur.getString(cur.getColumnIndex(ModelConstants.END_TIME_TEXT));
                        map.put("Id", id);   //the row id comes from the database but its invisible. its used to implicitly identify the rows selected
                        map.put("CourseCode", courseCode);
                        map.put("CourseTitle", courseTitle);
                        map.put("Time", startTime + " - " + endTime);

                        arrayList.add(map);

                    } while (cur.moveToNext());

                    ListAdapter adapter = new SimpleAdapter(getActivity(), arrayList, R.layout.list_item, new String[]{"Id", "CourseCode", "CourseTitle", "Time"}, new int[]{R.id.rowId, R.id.message, R.id.courseTitle, R.id.time});
                    View header = getLayoutInflater(savedInstanceState).inflate(R.layout.view_header, null, false);
                    day = (Button) header.findViewById(R.id.header);
                    day.setText(days[i]);
                    day.setClickable(false);
                    mergeAdapter.addView(header);
                    mergeAdapter.addAdapter(adapter);


                } else {        //no course is registered

                    ArrayList<HashMap<String, String>> arrayList2;
                    arrayList2 = new ArrayList<HashMap<String, String>>();

                    HashMap<String, String> hashMap;
                    hashMap = new HashMap<>();
                    String message = "You have not registered any course for today.";
                    hashMap.put("NoCourse", message);

                    arrayList2.add(hashMap);

                    ListAdapter adapter = new SimpleAdapter(getActivity(), arrayList2, R.layout.list_item, new String[]{"NoCourse"}, new int[]{R.id.message});

                    View header = getLayoutInflater(savedInstanceState).inflate(R.layout.view_header, null, false);
                    day = (Button) header.findViewById(R.id.header);
                    day.setClickable(false);
                    day.setText(days[i]);
                    mergeAdapter.addView(header);
                    mergeAdapter.addAdapter(adapter);

                }
            }
            mergeAdapter.notifyDataSetChanged();
            setListAdapter(mergeAdapter);
            dataHandler.close();

        } else {


            v = inflater.inflate(R.layout.view_layout_nodata, container, false);
            mAdView = (AdView) v.findViewById(R.id.adView);

        }


        // mAdView.setAdSize(AdSize.BANNER);
        adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


//        interstitial = new InterstitialAd(getActivity());
//        interstitial.setAdUnitId(getResources().getString(R.string.course_details_interstitial));
//
//        // Create ad request.
//        adRequest1 = new AdRequest.Builder().build();
//
//
//        // Begin loading your interstitial.
//        interstitial.loadAd(adRequest1);


        return v;
    }

    public void display() {

//        interstitial.setAdListener(new AdListener() {
//            @Override
//            public void onAdLoaded() {
//                super.onAdLoaded();
//                interstitial.show();
//            }
//
//
//        });
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        super.onListItemClick(l, v, position, id);

        rowId = (TextView) v.findViewById(R.id.rowId);
        String row_id = rowId.getText().toString();
        if (Integer.parseInt(row_id) < 0) {

            displayMessage("Message", "Please use the 'Add' tab to add course for today.");


        } else {

            display();
            Intent intent = new Intent(getActivity(), CourseDetails.class);
            intent.putExtra("ROW_ID", row_id);

            startActivity(intent);
            //	startActivity(intent);*/
            //Toast.makeText(getActivity(), "Row id is: " + row_id,Toast.LENGTH_LONG).show();
        }
    }

    public void displayMessage(String type, String message) {

        Builder builder = new Builder(getActivity());
        builder.setTitle(type);
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.show();


    }


}
