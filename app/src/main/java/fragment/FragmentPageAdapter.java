package fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


public class FragmentPageAdapter extends FragmentStatePagerAdapter {

	public FragmentPageAdapter(FragmentManager fm) {
		super(fm);
		
	}


	@Override
	public int getItemPosition(Object object) {
		return -2;
	}

	/*public int getItemPosition(Object object) {
            // TODO Auto-generated method stub

        }


    */	@Override

	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		
		switch(arg0){
		
		case 0:
			//display loading progress
			return new ViewFragment();
		case 1:
			return new EditFragment();
		case 2: 
			return new GpFragment();
		}
		return null;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 3;
	}

}
