package com.raghav.android.homecoming;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Vighnesh on 11-05-2018.
 */

public class GameView extends SurfaceView implements Runnable {
    volatile boolean playing;
    Thread gameThread = null;
    private PlayerShip mPlayerShip;
    private Canvas mCanvas;
    private Paint mPaint;
    private SurfaceHolder mSurfaceHolder;
    private EnemyShip e1,e2,e3;

    public GameView(Context context,int x,int y) {
        super(context);
        mSurfaceHolder = getHolder();
        mPaint = new Paint();
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
                break;
            case MotionEvent.ACTION_DOWN:
                mPlayerShip.setBoosting();
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
            gameThread.sleep(17);
        }
        catch (InterruptedException e){
        }

    }
    private void update() {
        mPlayerShip.update();

        e1.update(mPlayerShip.getSpeed());
        e2.update(mPlayerShip.getSpeed());
        e3.update(mPlayerShip.getSpeed());
    }
    private void draw(){
        if(mSurfaceHolder.getSurface().isValid()){
            mCanvas = mSurfaceHolder.lockCanvas();//lock the canvas
            mCanvas.drawColor(Color.argb(255,0,0,0));//rub the canvas by filling the color
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
