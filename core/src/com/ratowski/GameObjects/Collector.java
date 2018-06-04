package com.ratowski.GameObjects;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.ratowski.GameWorld.GameWorld;

import java.util.Random;

public class Collector {

    public int height, width, health;
    private int chaseThreshold = 10000;
    public float x, y;
    private Vector2 position;
    public float rotation;
    public boolean rotate = true;
    private Random randX, randY;
    private boolean chasingMushroom = false;
    public double distanceToMushroom, velX, velY, angle;
    float deltaY, deltaX;
    private int randomX, randomY;
    public Vector2 velocity;
    public Mushroom nearestMushroom;
    private GameWorld myWorld;

    public Collector(float x, float y, int width, int height, int health, GameWorld myWorld) {
        this.x = x;
        this.y = y;
        this.health = health;
        this.width = width;
        this.height = height;
        this.myWorld = myWorld;
        position = new Vector2(x, y);
        velocity = setRandomVelocity();
        rotation = 0;
    }

    public void update(float delta) {
        position.add(velocity.cpy().scl(delta));
        findNearestMushroom();
        getDistanceToNearestMushroom();

        if (distanceToMushroom < chaseThreshold) {
            rotate = false;
            if (!chasingMushroom) {
                chasingMushroom = true;
            }
            angle = Math.atan2(deltaY, deltaX);
            velX = 20 * Math.cos(angle);
            velY = 20 * Math.sin(angle);
            velocity = new Vector2((float) velX, (float) velY);
        } else {
            rotate = true;
            if (chasingMushroom) {
                chasingMushroom = false;
                velocity = setRandomVelocity();
            }
        }

        x = position.x;
        y = position.y;

        if (x <= 160 || x > 588) {
            velocity.x = -velocity.x;
        }
        if (y <= 160 || y > 428) {
            velocity.y = -velocity.y;
        }
        rotateIfNecessary();

    }

    public boolean collides(Mushroom mushroom) {
        Rectangle intersection = getIntersectionRectangle(mushroom);
        if (intersection.area() > 30) {
            return true;
        }
        return false;
    }

    private void findNearestMushroom() {
        int id = 0;
        double smallest = 600;
        for (int i = 0; i < myWorld.mushrooms.size(); i++) {
            Mushroom mush = myWorld.mushrooms.get(i);
            double distance = Math.sqrt((mush.x - position.x) * (mush.x - position.x) + (mush.y - position.y) * (mush.y - position.y));
            if (distance < smallest) {
                smallest = distance;
                id = i;
            }
        }
        nearestMushroom = myWorld.mushrooms.get(id);
    }

    public Rectangle getBoundingRectangle() {
        Rectangle me = new Rectangle(x, y, width, height);
        return me;
    }

    public void addHealth(int howmuch) {
        health += howmuch;
    }

    private Rectangle getIntersectionRectangle(Mushroom mushroom) {
        Rectangle intersection = new Rectangle();
        Rectangle me = new Rectangle(x, y, width, height);
        Rectangle mushrectangle = new Rectangle(mushroom.x, mushroom.y, mushroom.width, mushroom.height);
        Intersector.intersectRectangles(mushrectangle, me, intersection);
        return intersection;
    }

    private void rotateIfNecessary() {
        if (rotate) {
            rotation = (float) Math.toDegrees(Math.atan2(velocity.y, velocity.x)) - 90;
        }
    }

    private void getDistanceToNearestMushroom() {
        deltaX = nearestMushroom.x - x;
        deltaY = nearestMushroom.y - y;
        distanceToMushroom = Math.sqrt((deltaX * deltaX) + (deltaY * deltaY));
    }

    private Vector2 setRandomVelocity() {
        randX = new Random();
        randY = new Random();
        randomX = randX.nextInt(10) + 5;
        randomY = randY.nextInt(10) + 5;
        if (randX.nextInt(10) < 5) {
            randomX = -randomX;
        }
        if (randY.nextInt(10) < 5) {
            randomY = -randomY;
        }
        return new Vector2(randomX, randomY);
    }

}
