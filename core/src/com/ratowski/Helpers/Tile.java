package com.ratowski.Helpers;

public class Tile {

    private int tileX, tileY, type;

    public Tile(int x, int y, int typeInt) {
        tileX = x;
        tileY = y;
        type = typeInt;
    }

    public int getTileX() {
        return tileX;
    }

    public int getTileY() {
        return tileY;
    }

    public int getTileType() {
        return type;
    }
}