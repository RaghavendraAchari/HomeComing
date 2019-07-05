package com.raghav.android.homecoming;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements View.OnClickListener {
    public static final String PREF_TAG = "HighScores";
    public static final String TIME_TAG = "fastestTime";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences pref = getSharedPreferences(PREF_TAG, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        final Button mPlayButton = (Button)findViewById(R.id.playButton);

        mPlayButton.setOnClickListener(this);

        final TextView mText = (TextView)findViewById(R.id.textHighScore);
        long fastestTime = pref.getLong(TIME_TAG,-1);
        if (fastestTime == -1) {
            mText.setText("Fastest Time: " + " - ");
        }else {
            mText.setText("Fastest Time: " + fastestTime +"s");
        }

    }
    
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this,GameActivity.class);
        startActivity(intent);
        //finish();
    }
}
