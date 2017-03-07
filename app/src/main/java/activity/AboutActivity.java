package activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.domainname.timetablemanager.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


public class AboutActivity extends AppCompatActivity {

	ImageButton facebook, youtube, twitter;
    public static Activity about;



    @Override
    protected void onDestroy() {
        super.onDestroy();
        AboutActivity.about.finish();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        assistOnCreate();
    }

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
        about = this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        assistOnCreate();


	}
    private void assistOnCreate(){


        setTitle("About");
        final AdView mAdView = (AdView) findViewById(R.id.adView5);
        final AdRequest adRequest1;
        adRequest1 = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest1);


        facebook = (ImageButton) findViewById(R.id.facebook);
        youtube = (ImageButton) findViewById(R.id.youtube);
        twitter = (ImageButton) findViewById(R.id.twitter);

        facebook.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Uri uri = Uri.parse("http://www.facebook.com/ahmedmolawale");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        youtube.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Uri uri = Uri.parse("http://www.youtube.com/channel/UC9KuUBZ8HmOw1QSIMB8murQ/discussion");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        twitter.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Uri uri = Uri.parse("http://www.twitter.com/ahmedmolawale");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });


    }
}
