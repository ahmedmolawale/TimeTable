package activity;



import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;
import androidx.appcompat.app.ActionBar;

import com.ahmedmolawale.timetablemanager.R;

import fragment.FragmentPageAdapter;



@SuppressWarnings("deprecation")
public class MainActivity extends AppCompatActivity implements ActionBar.TabListener {
	
	public static Activity fa;
	ActionBar actionBar;
	ViewPager viewPager;
	FragmentPageAdapter fragmentPageAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fa = this;
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        assistOnCreate();
    }


    private void assistOnCreate(){

        viewPager = (ViewPager) findViewById(R.id.pager);
        fragmentPageAdapter = new FragmentPageAdapter(getSupportFragmentManager());
        supportInvalidateOptionsMenu();
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setLogo(R.drawable.ic_launcher);
        //actionBar.setIcon(R.drawable.ic_launcher);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.addTab(actionBar.newTab().setText("View").setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText("Add").setTabListener(this).setContentDescription("Use here to add a new course."));
        actionBar.addTab(actionBar.newTab().setText("GP").setTabListener(this));
        viewPager.setAdapter(fragmentPageAdapter);
        //viewPager.getAdapter().notifyDataSetChanged();
        viewPager.setOffscreenPageLimit(0);

        viewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {

                actionBar.setSelectedNavigationItem(position);
                if(position ==0 ){
                    fragmentPageAdapter.notifyDataSetChanged();
                }
				/*FragmentInterface fragment = (FragmentInterface) fragmentPageAdapter.instantiateItem(viewPager, position);
				if(fragment != null){

					fragment.fragmentResume();

				}*/

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }
            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub
            }
        });

    }
	@Override
	public void onTabReselected(androidx.appcompat.app.ActionBar.Tab arg0,
                                FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabSelected(androidx.appcompat.app.ActionBar.Tab tab,
                              FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		viewPager.getCurrentItem();
		viewPager.setCurrentItem(tab.getPosition());
		
	}




	@Override
	public void onTabUnselected(androidx.appcompat.app.ActionBar.Tab arg0,
                                FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}
	
	}
