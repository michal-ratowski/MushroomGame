package com.ratowski.GameObjects;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

public class Monster {

    public int height, width, x_max, x_min, y_min, y_max, health;
    private int chaseThreshold = 100;
    public float x, y;
    private Vector2 position;
    private Random randX, randY;
    private boolean wasChasing = false;
    public float rotation;
    public boolean rotate = true;
    public double distance, velX, velY, angle;
    float deltay, deltax;
    private int randomX, randomY;
    public Vector2 velocity;
    private Player player;

    public Monster(float x, float y, int width, int height, int health, Player player) {
        this.x = x;
        this.y = y;
        this.health = health;
        this.width = width;
        this.height = height;
        this.player = player;
        position = new Vector2(x, y);
        rotation = 0;

        randX = new Random();
        randY = new Random();

        randomX = randX.nextInt(10) + 5;
        randomY = randY.nextInt(10) + 5;

        if (randX.nextInt(10) < 5) randomX = -randomX;
        if (randY.nextInt(10) < 5) randomY = -randomY;

        velocity = new Vector2(randomX, randomY);
    }

    public void update(float delta) {
        position.add(velocity.cpy().scl(delta));

        deltax = player.getX() - position.x;
        deltay = player.getY() - position.y;

        distance = Math.sqrt((deltax * deltax) + (deltay * deltay));

        if (distance < chaseThreshold) {
            if (!wasChasing) {
                wasChasing = true;
            }
            angle = Math.atan2(deltay, deltax);
            velX = 20 * Math.cos(angle);
            velY = 20 * Math.sin(angle);
            velocity = new Vector2((float) velX, (float) velY);
        } else {
            if (wasChasing) {
                wasChasing = false;
                randomX = randX.nextInt(10) + 5;
                randomY = randY.nextInt(10) + 5;
                if (randX.nextInt(10) < 5) randomX = -randomX;
                if (randY.nextInt(10) < 5) randomY = -randomY;
                velocity = new Vector2(randomX, randomY);
            }
        }
        x = position.x;
        y = position.y;

        if (rotate) {
            rotation = (float) Math.toDegrees(Math.atan2(velocity.y, velocity.x)) - 90;
        }

        if (x <= 160 || x > 588) velocity.x = -velocity.x;
        if (y <= 160 || y > 428) velocity.y = -velocity.y;

    }

    public boolean collides(Player player) {
        Rectangle intersection = new Rectangle();
        Rectangle me = new Rectangle(x, y, width, height);
        Intersector.intersectRectangles(player.getBoundingRectangle(), me, intersection);

        if (intersection.area() > 40) {
            rotate = false;
            System.out.println("Gotcha! Area: " + intersection.area());
            return true;
        } else {
            rotate = true;
            return false;
        }
    }

    public Rectangle getBoundingRectangle() {
        Rectangle me = new Rectangle(x, y, width, height);
        return me;
    }

    public void addHealth(int howmuch) {
        health += howmuch;
    }

}
