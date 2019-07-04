package com.raghav.android.homecoming;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
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
    private boolean boosting = false;

    private float distanceRemaining;
    private long timeTaken;
    private long timeStarted;
    private long fastestTime;

    public GameView(Context context,int x,int y){
        super(context);
        mSurfaceHolder = getHolder();
        mPaint = new Paint();

        int numStars = 70;

        for(int i=0; i < numStars; i++){
            SpaceDust dust = new SpaceDust(x,y);
            mStars.add(dust);
        }

        mPlayerShip = new PlayerShip(context,x,y);
        e1= new EnemyShip(context,x,y);
        e2= new EnemyShip(context,x,y);
        e3= new EnemyShip(context,x,y);
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
        Rect playerBox = mPlayerShip.getHitbox();
        if(Rect.intersects(playerBox, e1.getHitbox()))
            e1.setX(-200);
        if(Rect.intersects(playerBox, e2.getHitbox()))
            e2.setX(-200);
        if(Rect.intersects(playerBox, e3.getHitbox()))
            e3.setX(-200);

        mPlayerShip.update();

        for (SpaceDust d : mStars) {
            d.update(mPlayerShip.getSpeed());
        }

        e1.update(mPlayerShip.getSpeed());
        e2.update(mPlayerShip.getSpeed());
        e3.update(mPlayerShip.getSpeed());
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

            mSurfaceHolder.unlockCanvasAndPost(mCanvas);
        }
    }

    public void pause(){
        playing=false;
        try{
            gameThread.join();
        }
        catch (InterruptedException e){

        }
    }

    public void resume(){
        playing=true;
        gameThread = new Thread(this);
        gameThread.start();
    }
}
