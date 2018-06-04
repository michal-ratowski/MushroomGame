package com.ratowski.GameScreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.ratowski.GameWorld.GameRenderer;
import com.ratowski.GameWorld.GameWorld;
import com.ratowski.Helpers.InputHandler;


public class GameScreen implements Screen {

    private GameWorld world;
    private GameRenderer renderer;
    private float runTime = 0;

    public GameScreen() {

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        float gameWidth = 200;
        float gameHeight = 356;
        int midPointY = (int) (gameHeight / 2);

        world = new GameWorld(midPointY);
        Gdx.input.setInputProcessor(new InputHandler(world, screenWidth / gameWidth, screenHeight / gameHeight));
        renderer = new GameRenderer(world, (int) gameHeight, (int) gameWidth);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        runTime += delta;
        world.update(delta);
        renderer.render(delta,runTime);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
