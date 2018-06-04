package com.ratowski.Helpers;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;

public class Joystick {

    private int x, y, r;
    private int x_touch, y_touch;
    public int x_dist, y_dist;

    private Circle bounds;
    public boolean active = false;

    public Joystick(int x, int y, int r) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.x_touch = x;
        this.y_touch = y;
        bounds = new Circle(x, y, r);
    }

    public boolean isInside(int screenX, int screenY) {

        if (bounds.contains(screenX, screenY)) {
            active = true;
            x_touch = screenX;
            y_touch = screenY;
            getCoordinates();
            return true;
        }
        active = false;
        return false;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void draw(ShapeRenderer renderer) {
        renderer.setColor(0 / 255.0f, 0 / 255.0f, 255 / 255.0f, 1);
        renderer.circle(x, y, r);
        renderer.setColor(255 / 255.0f, 0 / 255.0f, 0 / 255.0f, 1);
        renderer.circle(x_touch, y_touch, r / 5);
    }

    public void getCoordinates() {
        x_dist = x_touch - x;
        y_dist = y_touch - y;
    }

}
