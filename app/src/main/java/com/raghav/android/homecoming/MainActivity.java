package com.raghav.android.homecoming;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button mPlayButton = (Button)findViewById(R.id.playButton);
        mPlayButton.setOnClickListener(this);
        final TextView mText = (TextView)findViewById(R.id.textHighScore);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this,GameActivity.class);
        startActivity(intent);
        //finish();
    }
}
