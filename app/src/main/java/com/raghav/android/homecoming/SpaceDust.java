package com.raghav.android.homecoming;

import java.util.Random;

public class SpaceDust {


    private int x,y;

    private int speed;
    private int maxX, maxY;
    private int minX, minY;

    public SpaceDust(int screenX, int screenY){
        maxX = screenX;
        maxY = screenY;
        minX = minY = 0;

        Random random = new Random();
        speed = random.nextInt(10);
        x = random.nextInt(maxX);
        y = random.nextInt(maxY);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void update(int playerSpeed){
        x -= playerSpeed;
        x -= speed;

        if(x < minX){
            x = maxX;
            Random random = new Random();
            speed = random.nextInt(15);
            y = random.nextInt(maxY);
        }
    }
}
