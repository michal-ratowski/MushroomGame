package com.ratowski.GameObjects;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

public class Ammo {
    public int x,y,height,width,power;

    public Ammo (int x, int y, int width, int height, int power) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
        this.power = power;
    }

    public boolean collides(Player player) {
        Rectangle intersection=new Rectangle();
        Rectangle me = new Rectangle(x,y,width,height);
        Intersector.intersectRectangles(player.getBoundingRectangle(), me, intersection);

        if(intersection.area()>20) {
            return true;
        }

        else
        {
            return false;
        }
    }
}
