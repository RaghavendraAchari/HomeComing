package com.raghav.android.homecoming;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

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
    private Rect hitbox;

    public EnemyShip(Context context,int screenX,int screenY){
        mEnemyBitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.enemy);
        maxX=screenX; maxY=screenY;
        minX=0; minY=0;
        Random random = new Random();
        speed = random.nextInt(15)+10;

        x=screenX;
        y=random.nextInt(maxY)-mEnemyBitmap.getHeight();
        hitbox = new Rect(x, y, mEnemyBitmap.getWidth(), mEnemyBitmap.getHeight());
    }
    public void update(int playerSpeed){
        x -= playerSpeed;
        x -= speed;
        if(x < minX - mEnemyBitmap.getWidth()){
            Random random = new Random();
            speed = random.nextInt(18)+10;
            x = maxX;
            y = random.nextInt(maxY) - mEnemyBitmap.getHeight();
        }
        hitbox.left = x;
        hitbox.top = y;
        hitbox.right = x + mEnemyBitmap.getWidth();
        hitbox.bottom = y + mEnemyBitmap.getHeight();
    }
    public Bitmap getEnemyBitmap() {
        return mEnemyBitmap;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public Rect getHitbox() {
        return hitbox;
    }
}
