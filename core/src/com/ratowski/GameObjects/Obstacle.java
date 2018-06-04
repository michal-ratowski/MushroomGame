package com.ratowski.GameObjects;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

public class Obstacle {

    public int height,width;
    public float x,y;
    public Rectangle[] sides;
    private float collisionThreshold = 1.5f;

    public Obstacle (float x, float y, int width, int height)
    {
        this.x=x;
        this.y=y;
        this.height=height;
        this.width=width;

        sides = new Rectangle[4];

        sides[0]=new Rectangle(x,y,width,1);
        sides[1]=new Rectangle(x,y+height-1,width,1);
        sides[2]=new Rectangle(x,y,1,height);
        sides[3]=new Rectangle(x+width-1,y,1,height);

    }

    public boolean collidesUp(Player player) {
        Rectangle intersection=new Rectangle();
        Intersector.intersectRectangles(player.getBoundingRectangle(),sides[0],intersection);
        if(intersection.area()>collisionThreshold) return true;
        else return false;
    }

    public boolean collidesDown(Player player) {
        Rectangle intersection=new Rectangle();
        Intersector.intersectRectangles(player.getBoundingRectangle(),sides[1],intersection);
        if(intersection.area()>collisionThreshold) return true;
        else return false;
    }

    public boolean collidesLeft(Player player) {
        Rectangle intersection=new Rectangle();
        Intersector.intersectRectangles(player.getBoundingRectangle(),sides[2],intersection);
        if(intersection.area()>collisionThreshold) return true;
        else return false;
    }

    public boolean collidesRight(Player player) {
        Rectangle intersection=new Rectangle();
        Intersector.intersectRectangles(player.getBoundingRectangle(),sides[3],intersection);
        if(intersection.area()>collisionThreshold) return true;
        else return false;
    }

    public Rectangle getBoundingRectangle() {
        Rectangle me = new Rectangle (x,y,width,height);
        return me;
    }

}
