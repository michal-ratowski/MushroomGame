package com.ratowski.GameObjects;

public class DrawnObject{

    public boolean inFront;
    public int x,y,height,width;
    public Type type;
    public enum Type{
        TREE, MUSHROOM
    }

    public DrawnObject (int x, int y, int width, int height, boolean inFront, Type type) {
        this.x=x;
        this.y=y;
        this.height=height;
        this.width=width;
        this.inFront = inFront;
        this.type=type;
    }

}

