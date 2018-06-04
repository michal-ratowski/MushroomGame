package com.ratowski.Helpers;

import com.badlogic.gdx.InputProcessor;
import com.ratowski.com.ratowski.gameworld.GameWorld;
import com.ratowski.gameobjects.Player;

import java.util.List;

public class InputHandler implements InputProcessor {

    private Player myPlayer;
    private GameWorld myWorld;

    private float scaleFactorX;
    private float scaleFactorY;

    private boolean beenDragging=false;

    private List<com.ratowski.Helpers.SimpleButton> menuButtons;
    private com.ratowski.Helpers.SimpleButton playButton;
    private com.ratowski.Helpers.SimpleButton backButton;

    boolean already=false;

    public com.ratowski.Helpers.SimpleButton collectButton, shootButton;

    private com.ratowski.Helpers.Joystick joy;

    public InputHandler(GameWorld myWorld, float scaleFactorX, float scaleFactorY) {
        this.myWorld = myWorld;
        myPlayer = myWorld.getPlayer();

        int midPointY = myWorld.getMidPointY();

        this.scaleFactorX = scaleFactorX;
        this.scaleFactorY = scaleFactorY;

        collectButton = new com.ratowski.Helpers.SimpleButton(10,280,32,32, AssetManager.mushroom, AssetManager.mushroom);
        shootButton = new com.ratowski.Helpers.SimpleButton(10,316,32,32, AssetManager.gun, AssetManager.gunshooting);

        joy = new com.ratowski.Helpers.Joystick(155,311,35);
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        screenX = scaleX(screenX);
        screenY = scaleY(screenY);

        if (myWorld.isMenu()) {
            shootButton.isTouchDown(screenX, screenY);
        }

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        if(beenDragging==true)
        {
            beenDragging=false;
            myPlayer.rotate=false;
        }

        myPlayer.isCollecting=false;
        myPlayer.joyX=0;
        myPlayer.joyY=0;

        screenX = scaleX(screenX);
        screenY = scaleY(screenY);

        if (myWorld.isMenu()) {
            if (collectButton.isTouchUp(screenX, screenY)) {
                return true;
            }
            else if (shootButton.isTouchUp(screenX, screenY)) {
                myPlayer.shoot();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {

        beenDragging=true;

        screenX = scaleX(screenX);
        screenY = scaleY(screenY);

        if(joy.isInside(screenX,screenY))
        {
            myPlayer.joyX=joy.x_dist;
            myPlayer.joyY=joy.y_dist;
            myPlayer.rotate=true;
        }

        if(collectButton.isTouchDown(screenX, screenY)){
            myPlayer.isCollecting=true;
        }

        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    private int scaleX(int screenX) {
        return (int) (screenX / scaleFactorX);
    }

    private int scaleY(int screenY) {
        return (int) (screenY / scaleFactorY);
    }

    public List<com.ratowski.Helpers.SimpleButton> getMenuButtons() {
        return menuButtons;
    }

    public com.ratowski.Helpers.Joystick getJoystick() {
        return joy;
    }
}
