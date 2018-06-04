package com.ratowski.GameObjects;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

public class Ground {

    public int x,y,height,width;
    private final float collisionThreshold = 50f;

    public Ground(int x, int y, int width, int height, int type)
    {
        this.x=x;
        this.y=y;
        this.height=height;
        this.width=width;
    }

    public boolean collides(Player player) {
        Rectangle intersection=new Rectangle();
        Rectangle me = new Rectangle(x,y,width,height);
        Intersector.intersectRectangles(player.getBoundingRectangle(), me, intersection);

        if(intersection.area() > collisionThreshold) {
            return true;
        }
        return false;
    }
}
