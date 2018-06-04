package com.ratowski.GameObjects;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.ratowski.GameWorld.GameWorld;

import java.util.ArrayList;

public class Player {

    private Vector2 position;
    public Vector2 velocity;
    private Vector2 acceleration;
    private float rotation;
    private int height,width;
    public int joyX, joyY;
    public int health = 100;
    public int ammo = 5;
    private int joyDeadZone = 1;
    public int joyX2, joyY2;
    public boolean rotate=false;
    public boolean isCollecting=false;
    public boolean isAbove=false;
    public ArrayList<Mushroom> mushrooms= new ArrayList<Mushroom>();
    private GameWorld myWorld;

    private boolean isAlive;
    private Rectangle boundingRectangle;

    public Player(float x, float y, int width, int height, GameWorld myWorld) {
        this.width = width;
        this.height = height;
        position = new Vector2(x, y);
        velocity = new Vector2(0, 0);
        rotation=0;
        acceleration = new Vector2(0, 0);
        boundingRectangle = new Rectangle();
        isAlive=true;
        joyX=0;
        joyY=0;
        this.myWorld = myWorld;
        mushrooms=myWorld.mushrooms;
    }
    public void update(float delta) {

        velocity.add(acceleration.cpy().scl(delta));
        position.add(velocity.cpy().scl(delta));
        boundingRectangle.set(position.x, position.y, width,height);
    }

    public void onRestart(int y) {
        rotation = 0;
        position.y = y;
        velocity.x = 0;
        velocity.y = 0;
        acceleration.x = 0;
        acceleration.y = 460;
        isAlive = true;
    }

    public void onHold() {
        if (isAlive) {
            velocity.y = -140;
        }
    }

    public void decelerate() {
        acceleration.y = 0;
    }

    public void die() {
        isAlive = false;
        velocity.y = 0;
    }

    public float getX() {
        return position.x;
    }
    public float getY() {
        return position.y;
    }
    public float getWidth() {
        return width;
    }
    public float getHeight() {
        return height;
    }
    public float getRotation() {
        return rotation;
    }

    public boolean isFalling() {
        return velocity.y > 70;
    }

    public Rectangle getBoundingRectangle() {
        return boundingRectangle;
    }

    public boolean isAlive() {

        if(health>0) return true;
        return false;

    }

    public boolean shouldntFlap() {
        return velocity.y > 70 || !isAlive;
    }

    public void updateReady(float delta) {

        if(health<=0) health=0;
        if(health>=100) health=100;

        if(isCollecting) velocity=new Vector2(0,0);
        else velocity=new Vector2(joyX2*3,joyY2*3);

        position.add(velocity.cpy().scl(delta));
        //System.out.println("Position:  " + position.x+", "+position.y);
        boundingRectangle.set(position.x, position.y, width, height);
        //velocity.add(acceleration.cpy().scl(delta));

        if(Math.abs(joyX)<joyDeadZone) joyX2=0;
        else joyX2=joyX;

        if(Math.abs(joyY)<joyDeadZone)joyY2=0;
        else joyY2=joyY;

        if(rotate==true)
        {
            double angle = -Math.atan2(joyY, -joyX);
            rotation = (float) Math.toDegrees(angle) + 90;
        }



        //System.out.println("Rotation: "+rotation);
        //System.out.println("Rotation? "+rotate);

    }

    public void collideFromDown(Obstacle ob)
    {
        if(position.y<ob.y+ob.height && velocity.y<0) {
            position.y = ob.y+ob.height;
            velocity.y = 0;
        }
    }
    public void collideFromUp(Obstacle ob)
    {
        if(position.y+height>ob.y && velocity.y>0) {
            position.y = ob.y-height;
            velocity.y = 0;
        }
    }
    public void collideFromRight(Obstacle ob)
    {
        if(position.x<ob.x+ob.width && velocity.x<0) {
            position.x=ob.x+ob.width;
            velocity.x=0;
        }
    }

    public void collideFromLeft(Obstacle ob)
    {
        if(position.x+width>ob.x && velocity.x>0) {
            position.x=ob.x-width;
            velocity.x=0;
        }
    }

    public void changeHealth(int howmuch)
    {
        health+=howmuch;
        //System.out.println("Changing health by: "+howmuch+"! Current health is: "+health);
    }

    public void shoot() {

        if(isAlive() && ammo>0)
        {
            double velX = 100*Math.cos(Math.toRadians(rotation-90));
            double velY = 100*Math.sin(Math.toRadians(rotation-90));

           // System.out.println("Rotation: "+rotation+", Bullet velocity: ("+-velX+","+-velY+")");

            Projectile p = new Projectile(new Vector2(position.x+width,position.y+width/2),new Vector2((float)-velX,(float)-velY));
            myWorld.projectileArray.add(p);
            //ammo--;
        }
    }

    private Mushroom findNearestMushroom() {
        int id = 0;
        double smallest = 600;
        for (int i = 0; i < mushrooms.size(); i++) {
            Mushroom mush = mushrooms.get(i);
            double distance = Math.sqrt((mush.x-position.x)*(mush.x-position.x)+(mush.y-position.y)*(mush.y-position.y));
            if(distance<smallest) {
                smallest = distance;
                id=i;
            }
        }
        return mushrooms.get(id);
    }

}
