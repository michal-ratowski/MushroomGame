package com.ratowski.GameObjects;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Projectile {

    public Vector2 position;
    public Vector2 velocity;
    private boolean visible;

    public Projectile(Vector2 position, Vector2 velocity){
        this.position = position;
        this.velocity = velocity;
        visible = true;
    }

    public void update(float delta){
        position.add(velocity.cpy().scl(delta));
    }

    public boolean isVisible() {
        return visible;
    }

    public boolean collidesObstacle(Obstacle ob) {
        Rectangle me = new Rectangle(position.x, position.y, 1, 1);
        if(Intersector.overlaps(ob.getBoundingRectangle(),me))
            return true;
        return false;
    }

    public boolean collidesMonster(Monster mon) {
        Rectangle me = new Rectangle(position.x, position.y, 1, 1);
        if (Intersector.overlaps(mon.getBoundingRectangle(), me))
            return true;
        return false;
    }

    public boolean collidesCollector(Collector col) {
        Rectangle me = new Rectangle(position.x, position.y, 1, 1);
        if (Intersector.overlaps(col.getBoundingRectangle(), me))
            return true;
        return false;
    }

}
