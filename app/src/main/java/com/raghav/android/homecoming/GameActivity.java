package com.raghav.android.homecoming;

import android.app.Activity;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;

public class GameActivity extends Activity {
    private GameView mGameView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        mGameView = new GameView(this,size.x,size.y);
        setContentView(mGameView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGameView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGameView.resume();
    }
}
