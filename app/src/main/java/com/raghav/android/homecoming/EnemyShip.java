package com.raghav.android.homecoming;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;

/**
 * Created by Vighnesh on 11-05-2018.
 */

public class EnemyShip {
    private Bitmap mEnemyBitmap;
    private int speed = 1;
    private int x,y;

    private int maxX,minX;
    private int maxY,minY;

    public EnemyShip(Context context,int screenX,int screenY){
        mEnemyBitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.enemy);
        maxX=screenX; maxY=screenY;
        minX=0; minY=0;
        Random random = new Random();
        speed = random.nextInt(6)+10;

        x=screenX;
        y=random.nextInt(maxY)-mEnemyBitmap.getHeight();
    }
    public void update(int playerSpeed){
        x-=playerSpeed;
        x-=speed;
        if(x<minX-mEnemyBitmap.getWidth()){
            Random random = new Random();
            speed = random.nextInt(10)+10;
            x=maxX;
            y=random.nextInt(maxY)-mEnemyBitmap.getHeight();
        }
    }
    public Bitmap getEnemyBitmap() {
        return mEnemyBitmap;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
