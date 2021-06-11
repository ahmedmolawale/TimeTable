package fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import activity.AboutActivity;
import activity.FeedbackActivity;
import activity.FivePointGradeActivity;
import activity.FourPointGradeActivity;
import activity.Settings;
import activity.SevenPointGradeActivity;

import com.ahmedmolawale.timetablemanager.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;


public class GpFragment extends Fragment {

	private TextView clickHere;
    private InterstitialAd interstitial;
    private AdRequest adRequest;

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub

		inflater.inflate(R.menu.menu_on_gpfragment, menu);

		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method sd.atub

		super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub

		switch (item.getItemId()) {

		// deal with the ones of the menu button
		case R.id.action_about2:
			startAboutActivity();
			return true;
		case R.id.action_settings2:
			startSettingsActivity();
			return true;

		case R.id.action_feedback2:
			feedBackActivity();
			return true;
		default:
			return false;
		}

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
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		setHasOptionsMenu(true);
		getActivity().supportInvalidateOptionsMenu();
		View v;
		v = inflater.inflate(R.layout.gp_fragment_layout, container, false);
        final AdView mAdView = (AdView) v.findViewById(R.id.adView3);
        final AdRequest adRequest1;
         adRequest1 = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest1);
		clickHere = (TextView) v.findViewById(R.id.clickHere);
		clickHere.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//                interstitial = new InterstitialAd(getActivity());
//                interstitial.setAdUnitId(getResources().getString(R.string.gp_system_interstitial));
//
//                // Create ad request.
//                adRequest = new AdRequest.Builder().build();
//
//
//
//                // Begin loading your interstitial.
//                interstitial.loadAd(adRequest);


				displayGradePointScaleChoice();

			}
		});


		return v;
	}

    public void display(){

//        interstitial.setAdListener(new AdListener() {
//            @Override
//            public void onAdLoaded() {
//                super.onAdLoaded();
//                interstitial.show();
//            }
//        });
    }


	public void displayGradePointScaleChoice() {

		final CharSequence[] scales = { "4 - Point Scale", "5 - Point Scale",
				"7 - Point Scale" };

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Grade Point Scales");
		builder.setItems(scales, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				switch (item) {

				case 0:
                  display();
					    Intent intentForFour = new Intent(getActivity(),
							FourPointGradeActivity.class);

					         startActivity(intentForFour);

					break;
				case 1:
                  display();
					Intent intentForFive = new Intent(getActivity(),
							FivePointGradeActivity.class);

					startActivity(intentForFive);
					break;
				case 2:
                    display();
					Intent intentForSeven = new Intent(getActivity(),
							SevenPointGradeActivity.class);

					startActivity(intentForSeven);
					break;

				}

			}
		}).show();

	}

}
