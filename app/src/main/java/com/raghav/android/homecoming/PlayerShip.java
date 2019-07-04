package com.raghav.android.homecoming;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.Log;

/**
 * Created by Vighnesh on 11-05-2018.
 */

public class PlayerShip {

    public static final String TAG = "Player Ship";
    private boolean boosting;
    private Bitmap mBitmap;
    private int x,y;
    private int speed = 0;
    private final int gravity = -12;
    private final int maxSpeed = 20;
    private final int minSpeed = 1;
    private int maxY;
    private int minY;

    private Rect hitbox;

    PlayerShip(Context context,int maxx,int maxy){
        x = 50; y = 50;
        speed = 1;
        mBitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.ship);
        boosting=false;
        this.maxY=maxy-mBitmap.getHeight();
        minY=0;
        hitbox = new Rect(x, y, mBitmap.getWidth(), mBitmap.getHeight());
    }
    public void update(){
        if(boosting){
            speed += 3;
        }
        else{
            speed -= 5;
        }

        if(speed > maxSpeed){ speed=maxSpeed;}

        if(speed < minSpeed){ speed=minSpeed;}

        y -= speed + gravity;

        if(y < minY){ y=minY;}
        if(y > maxY){ y=maxY;}
        hitbox.left = x;
        hitbox.top = y;
        hitbox.right = x + mBitmap.getWidth();
        hitbox.bottom = y + mBitmap.getHeight();
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public int getSpeed() {
        return speed;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    public void stopBoosting() {
         boosting=false;
    }

    public void setBoosting() {
        boosting=true;
    }

    public Rect getHitbox() {
        return hitbox;
    }
}
