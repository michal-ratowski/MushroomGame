package com.ratowski.GameObjects;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

public class Mushroom {

    public int x, y, height, width;
    private int final_height, final_width, final_x, final_y;
    public boolean collected = false;
    public float health, maxhealth;
    public boolean ready = false;
    public int growing = 0;

    public Mushroom(int x, int y, int width, int height, int maxhealth) {
        this.final_x = x;
        this.final_y = y;
        this.final_height = height;
        this.final_width = width;
        this.maxhealth = maxhealth;
        this.health = maxhealth;
        this.height = 0;
        this.width = 0;
        this.x = final_x + final_width / 2;
        this.y = final_y + final_height;
    }

    public boolean collides(Player player) {
        Rectangle intersection = new Rectangle();
        Rectangle me = new Rectangle(x, y, width, height);
        Intersector.intersectRectangles(player.getBoundingRectangle(), me, intersection);

        if (intersection.area() > 50) {
            return true;
        } else {
            return false;
        }
    }

    public void addHealth(int howMany) {
        health = health + howMany;
    }

    public void update() {
        if (!ready) {
            if (growing < final_height) {

                growing++;
                height = growing;
                width = growing;
                x = (int) (final_x + (final_width - growing) / 2);
                y = (int) (final_y + final_height - growing);
            } else ready = true;
        }
    }
}
