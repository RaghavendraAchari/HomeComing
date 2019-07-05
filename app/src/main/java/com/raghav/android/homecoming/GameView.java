package com.raghav.android.homecoming;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

/**
 * Created by Vighnesh on 11-05-2018.
 */

public class GameView extends SurfaceView implements Runnable {
    public static final String TAG = "View";

    volatile boolean playing;
    Thread gameThread = null;
    private PlayerShip mPlayerShip;
    private Canvas mCanvas;
    private Paint mPaint;
    private SurfaceHolder mSurfaceHolder;
    private EnemyShip e1,e2,e3;
    private ArrayList<SpaceDust> mStars  = new ArrayList<>();
    private boolean boosting;
    private int screenX, screenY;
    private float distanceRemaining;
    private long timeTaken ;
    private long timeStarted ;
    private long fastestTime;

    private Context mContext;
    private boolean gameEnded;
    private SoundPool mSoundPool;
    int start = -1;
    int bump = -1;
    int destroyed = -1;
    int win = -1;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;


    public GameView(Context context,int x,int y){
        super(context);
        mContext = context;
        mSoundPool = new SoundPool(10, AudioManager.STREAM_MUSIC,0);
        mSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                if(sampleId==start){
                    mSoundPool.play(start ,1,1,0,0,1);
                }
            }
        });
        try{
            AssetManager assetManager = context.getAssets();
            AssetFileDescriptor descriptor ;

            descriptor = assetManager.openFd("win.ogg");
            win = mSoundPool.load(descriptor,0);

            descriptor = assetManager.openFd("bump.ogg");
            bump = mSoundPool.load(descriptor,0);

            descriptor = assetManager.openFd("destroyed.ogg");
            destroyed = mSoundPool.load(descriptor,0);

            descriptor = assetManager.openFd("start.ogg");
            start = mSoundPool.load(descriptor,0);
            Log.i(TAG, "Start value"+start);

        }catch (Exception e){
            Log.i(TAG, "GameView: Error");
        }
        screenX = x;
        screenY = y;
        mSurfaceHolder = getHolder();
        mPaint = new Paint();
        preferences = context.getSharedPreferences(MainActivity.PREF_TAG,Context.MODE_PRIVATE);
        editor = preferences.edit();
        startGame();
    }

    private void startGame() {
        int numStars = 70;

        for(int i=0; i < numStars; i++){
            SpaceDust dust = new SpaceDust(screenX,screenY);
            mStars.add(dust);
        }

        mPlayerShip = new PlayerShip(mContext,screenX,screenY);
        e1= new EnemyShip(mContext,screenX,screenY);
        e2= new EnemyShip(mContext,screenX,screenY);
        e3= new EnemyShip(mContext,screenX,screenY);

        distanceRemaining = 10000;
        timeTaken = 0;
        timeStarted = System.currentTimeMillis();
        fastestTime = preferences.getLong(MainActivity.TIME_TAG,-1);
        if(fastestTime==-1){
            fastestTime=1000000;
        }
        gameEnded = false;
        boosting = false;
        mSoundPool.play(start ,1,1,0,0,1);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_UP :
                mPlayerShip.stopBoosting();
                boosting = false;
                break;
            case MotionEvent.ACTION_DOWN:
                mPlayerShip.setBoosting();
                boosting = true;
                if(gameEnded){
                    startGame();
                }
                break;
        }
        return  true;
    }

    @Override
    public void run() {
        while (playing){
            update();
            draw();
            control();
        }
    }

    private void control() {
        try{
            Thread.sleep(17);
        }
        catch (InterruptedException e){
            Log.i(TAG, "Thread Error");
        }

    }

    private void update() {
        mPlayerShip.update();

        for (SpaceDust d : mStars) {
            d.update(mPlayerShip.getSpeed());
        }

        e1.update(mPlayerShip.getSpeed());
        e2.update(mPlayerShip.getSpeed());
        e3.update(mPlayerShip.getSpeed());

        Rect playerBox = mPlayerShip.getHitbox();
        boolean hitDetected = false;
        if(Rect.intersects(playerBox, e1.getHitbox())) {
            e1.setX(-200);
            hitDetected = true;
        }
        if(Rect.intersects(playerBox, e2.getHitbox())) {
            e2.setX(-200);
            hitDetected = true;
        }
        if(Rect.intersects(playerBox, e3.getHitbox())) {
            e3.setX(-200);
            hitDetected = true;
        }

        if(hitDetected){
            mSoundPool.play(bump,1,1,0,0,1);
            mPlayerShip.reduceShieldStrength();
            if(mPlayerShip.getShieldStrength()<0){
                //End Game
                mSoundPool.play(destroyed,1,1,0,0,1);
                gameEnded = true;
            }
        }

        if(!gameEnded){
            distanceRemaining -= mPlayerShip.getSpeed();
            timeTaken = System.currentTimeMillis() - timeStarted;
        }

        if(distanceRemaining < 0){
            mSoundPool.play(win,1,1,0,0,1);
            if(timeTaken < fastestTime){
                fastestTime = timeTaken;
                editor.putLong(MainActivity.TIME_TAG,fastestTime);
                editor.commit();
            }
            distanceRemaining = 0;
            gameEnded = true;
        }
    }

    private void draw(){
        if(mSurfaceHolder.getSurface().isValid()){
            mCanvas = mSurfaceHolder.lockCanvas();//lock the canvas

            mCanvas.drawColor(Color.argb(255,0,0,0));//rub the canvas by filling the color
            //draw the space stars
            mPaint.setColor(Color.argb(255,255,255,255));
            if(boosting){
                for (SpaceDust d : mStars) {
                    mCanvas.drawLine(d.getX(),d.getY(),d.getX()-5,d.getY(),mPaint);
                }
            }else {
                for (SpaceDust d : mStars) {
                    mCanvas.drawPoint(d.getX(), d.getY(), mPaint);
                }
            }
            //draw player ship
            mCanvas.drawBitmap(mPlayerShip.getBitmap(),mPlayerShip.getX(),mPlayerShip.getY(),mPaint);//draw the player ship by drawBitmap()
            mCanvas.drawBitmap(e1.getEnemyBitmap(),e1.getX(),e1.getY(),mPaint);
            mCanvas.drawBitmap(e2.getEnemyBitmap(),e2.getX(),e2.getY(),mPaint);
            mCanvas.drawBitmap(e3.getEnemyBitmap(),e3.getX(),e3.getY(),mPaint);

            mPaint.setColor(Color.argb(255,255,255,255));

            if(!gameEnded) {
                mPaint.setTextAlign(Paint.Align.LEFT);
                mPaint.setTextSize(25);
                mCanvas.drawText("Fastest: " + fastestTime + "s", 10, 20, mPaint);
                mCanvas.drawText("Time: " + timeTaken + "s", screenX / 2, 20, mPaint);
                mCanvas.drawText("Distance: " + distanceRemaining / 1000 + " KM", screenX / 3, screenY - 20, mPaint);
                mCanvas.drawText("Shield: " + mPlayerShip.getShieldStrength(), 10, screenY - 20, mPaint);
                mCanvas.drawText("Speed: " + mPlayerShip.getSpeed() * 60 * 20 + " KM/h", (screenX / 3) * 2, screenY - 20, mPaint);
            }else {
                mPaint.setTextAlign(Paint.Align.CENTER);
                mPaint.setTextSize(80);
                mCanvas.drawText("Game Over: ", screenX/2, 100, mPaint);
                mPaint.setTextSize(25);
                mCanvas.drawText("Fastest: " + fastestTime + "s", screenX/2, 160, mPaint);
                mCanvas.drawText("Time: " + timeTaken + "s", screenX / 2, 200, mPaint);
                mCanvas.drawText("Distance: " + distanceRemaining / 1000 + " KM", screenX / 2, 240, mPaint);

                mPaint.setTextSize(80);
                mCanvas.drawText("Tap To Replay", screenX / 2, 350, mPaint);
            }

            mSurfaceHolder.unlockCanvasAndPost(mCanvas);
        }
    }

    public void pause(){
        playing=false;
        try{
            gameThread.join();
        }
        catch (InterruptedException e){
            Log.i(TAG, "Error");
        }
    }

    public void resume(){
        playing=true;
        gameThread = new Thread(this);
        gameThread.start();

    }
}
