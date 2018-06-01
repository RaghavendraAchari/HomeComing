package com.raghav.android.homecoming;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by Vighnesh on 11-05-2018.
 */

public class PlayerShip {


    private boolean boosting;
    private Bitmap mBitmap;
    private int x,y;
    private int speed = 0;
    private final int gravity=-12;
    private final int maxSpeed = 20;
    private final int minSpeed = 0;
    private int maxY;
    private int minY;

    PlayerShip(Context context,int maxx,int maxy){
        x = 50; y = 50;
        speed = 1;
        mBitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.ship);
        boosting=false;
        this.maxY=maxy-mBitmap.getHeight();
        minY=0;
    }
    public void update(){
        if(boosting){ speed+=2;}
        else { speed-=5;}

        if(speed > maxSpeed){ speed=maxSpeed;}
        if(speed<minSpeed){ speed=minSpeed;}
        y-=speed+gravity;
        if(y<minY){ y=minY;}
        if(y>maxY){ y=maxY;}
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
}
