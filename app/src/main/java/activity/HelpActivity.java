package activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.ahmedmolawale.timetablemanager.R;


public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
