package com.ratowski.GameWorld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.ratowski.TweenAccessors.Value;
import com.ratowski.TweenAccessors.ValueAccessor;
import com.ratowski.gameobjects.Ammo;
import com.ratowski.gameobjects.Collector;
import com.ratowski.gameobjects.DrawnObject;
import com.ratowski.gameobjects.Monster;
import com.ratowski.gameobjects.Mushroom;
import com.ratowski.gameobjects.Obstacle;
import com.ratowski.gameobjects.Player;
import com.ratowski.gameobjects.Projectile;
import com.ratowski.Helpers.AssetManager;
import com.ratowski.Helpers.InputHandler;
import com.ratowski.Helpers.Joystick;
import com.ratowski.Helpers.SimpleButton;
import com.ratowski.Helpers.Tile;

import java.util.ArrayList;
import java.util.List;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;

public class GameRenderer {

    private GameWorld gameWorld;
    private OrthographicCamera camera;
    private ShapeRenderer shapeRenderer, shapeRenderer2;

    private ArrayList<Obstacle> obstacles;

    private TweenManager tweenManager;
    private Value alpha = new Value();

    private SpriteBatch dynamicSpriteBatch, staticSpriteBatch;
    private int gameHeight;
    private int gameWidth;

    // Game Objects
    private Player player;
    private List<SimpleButton> menuButtons;
    private SimpleButton collectButton, shootButton;
    private ArrayList<Tile> tiles;
    private ArrayList<DrawnObject> drawnObjects;
    private ArrayList<Ammo> ammos;
    private ArrayList<Mushroom> mushrooms;
    private ArrayList<Monster> monsters;
    private ArrayList<Collector> collectors;
    private ArrayList<Projectile> projectiles;
    private Joystick joystick;

    // Game Assets
    private TextureRegion tree, grass,sand,lava,water;
    private TextureRegion stone, monster,collector,heart;
    private Animation playerAnimation;
    private TextureRegion mushroom, collectedMushroom, mushroomButton, mushroomHUD,cone;
    private TextureRegion playerTextureRegions[] = new TextureRegion [11];

    public GameRenderer(GameWorld world, int gameHeight, int gameWidth) {
        gameWorld = world;

        this.menuButtons = ((InputHandler) Gdx.input.getInputProcessor()).getMenuButtons();
        collectButton = ((InputHandler) Gdx.input.getInputProcessor()).collectButton;
        shootButton = ((InputHandler) Gdx.input.getInputProcessor()).shootButton;
        this.joystick = ((InputHandler) Gdx.input.getInputProcessor()).getJoystick();

        this.gameHeight = gameHeight;
        this.gameWidth = gameWidth;

        camera = new OrthographicCamera();
        camera.setToOrtho(true, 200, gameHeight);

        dynamicSpriteBatch = new SpriteBatch();
        dynamicSpriteBatch.setProjectionMatrix(camera.combined);
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(camera.combined);

        shapeRenderer2 = new ShapeRenderer();
        shapeRenderer2.setProjectionMatrix(camera.combined);
        staticSpriteBatch = new SpriteBatch();
        staticSpriteBatch.setProjectionMatrix(camera.combined);

        // Call helper methods to initialize instance variables
        initGameObjects();
        initAssets();
        //setupTweens();

        tiles = world.tilearray;
        obstacles = world.obstacles;
        drawnObjects = world.objects;
        mushrooms = world.mushrooms;
        monsters = world.monsters;
        collectors=world.collectors;
        ammos=world.ammoArray;
        projectiles= gameWorld.projectileArray;
    }

    private void initGameObjects() {
        player = gameWorld.getPlayer();
    }

    private void initAssets() {

        tree = AssetManager.tree;
        grass = AssetManager.grass;
        stone = AssetManager.stone;
        sand = AssetManager.sand;
        mushroom = AssetManager.mushroom;
        mushroomHUD = AssetManager.mushroomhud;
        mushroomButton = AssetManager.mushroomblack;
        collectedMushroom = AssetManager.mushroomgreen;
        mushroomHUD = AssetManager.mushroomhud;
        cone= AssetManager.cone;
        lava = AssetManager.lava;
        water = AssetManager.water;
        heart = AssetManager.heart;
        monster = AssetManager.monster;
        collector = AssetManager.collector;
        playerAnimation = AssetManager.sebaAnimation;
        playerTextureRegions = AssetManager.seby;
    }


    public void render(float delta, float runTime) {

        // moving camera
        camera.position.x = player.getX()+ player.getWidth()/2;
        camera.position.y = player.getY()+ player.getHeight()/2 + 0.125f*gameHeight;
        camera.update();

        dynamicSpriteBatch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        // BATCHER 1 (tekstury i czcionki ruchome) //////////////////
        dynamicSpriteBatch.begin();
        drawTiles();
        dynamicSpriteBatch.enableBlending();

        drawMushrooms();
        drawAmmo();
        drawSeba(runTime);
        drawMonsters();
        drawCollectors();
        drawObjects();

        dynamicSpriteBatch.end();

        // SHAPE RENDERER 1 (ruchomy) i 2 (nieruchomy) (kształty) //////////////////
        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer2.begin(ShapeType.Filled);

        drawProjectiles();
        //drawObstaclesBounds();
        drawUI();
        drawMushroomState();
        //drawNearestMushrooms();

        shapeRenderer.end();
        shapeRenderer2.end();

        // BATCHER 2 (tekstury i czcionki nieruchome) //////////////////
        staticSpriteBatch.begin();
        drawScore();
        drawCollectButton();
        drawHUD();
        //drawSebaBounds();
        staticSpriteBatch.end();

    }

    private void setupTweens() {
        Tween.registerAccessor(Value.class, new ValueAccessor());
        tweenManager = new TweenManager();
        Tween.to(alpha, -1, .5f).target(0).ease(TweenEquations.easeInQuad).start(tweenManager);
    }


    private void drawSeba(float runTime) {

        if(player.isAlive())

            if(gameWorld.collectingMushroom==true) {
                dynamicSpriteBatch.draw(playerTextureRegions[3], player.getX(), player.getY(), player.getWidth() / 2.0f, player.getHeight() / 2.0f,
                        player.getWidth(), player.getHeight(), 1, 1, player.getRotation());
            }

            else {

                if (player.velocity.equals(Vector2.Zero))
                    dynamicSpriteBatch.draw(playerTextureRegions[0], player.getX(), player.getY(), player.getWidth() / 2.0f, player.getHeight() / 2.0f,
                            player.getWidth(), player.getHeight(), 1, 1, player.getRotation());

                else
                    dynamicSpriteBatch.draw(playerAnimation.getKeyFrame(runTime), player.getX(), player.getY(), player.getWidth() / 2.0f, player.getHeight() / 2.0f,
                            player.getWidth(), player.getHeight(), 1, 1, player.getRotation());

            }

        else
            dynamicSpriteBatch.draw(playerTextureRegions[1], player.getX(), player.getY(), player.getWidth() / 2.0f, player.getHeight() / 2.0f,
                    player.getWidth(), player.getHeight(), 1, 1, player.getRotation());

    }


    private void drawObjects() {
        for (DrawnObject ob : drawnObjects) {
            if(ob.type == DrawnObject.Type.TREE)
            dynamicSpriteBatch.draw(tree, ob.x, ob.y, ob.width / 2.0f, ob.height / 2.0f, ob.width, ob.height, 1, 1, 0);
        }
    }

    private void drawMushrooms() {
        for (Mushroom ob : mushrooms) {
            dynamicSpriteBatch.draw(mushroom, ob.x, ob.y, ob.width / 2.0f, ob.height / 2.0f, ob.width, ob.height, 1, 1, 0);
        }
    }

    private void drawMonsters() {
        for (Monster ob : monsters) {
            dynamicSpriteBatch.draw(monster, ob.x, ob.y, ob.width / 2.0f, ob.height / 2.0f, ob.width, ob.height, 1, 1, ob.rotation);
        }
    }

    private void drawCollectors() {
        for (Collector ob : collectors) {
            dynamicSpriteBatch.draw(collector, ob.x, ob.y, ob.width / 2.0f, ob.height / 2.0f, ob.width, ob.height, 1, 1, ob.rotation);
        }
    }

    private void drawAmmo() {
        for (Ammo ob : ammos) {
            dynamicSpriteBatch.draw(cone, ob.x, ob.y, ob.width / 2.0f, ob.height / 2.0f, ob.width, ob.height, 1, 1, 0);
        }
    }


    private void drawObstaclesBounds() {
        for (Obstacle ob : obstacles) {
            shapeRenderer.setColor(255 / 255.0f, 255 / 255.0f, 255 / 255.0f, 1);
            shapeRenderer.rect(ob.x, ob.y, ob.width, ob.height);
        }

    }

    private void drawScore() {
        AssetManager.font.draw(staticSpriteBatch, "" + player.health, gameWidth * 0.4f, gameHeight*0.8f);
        AssetManager.font.draw(staticSpriteBatch, "" + gameWorld.getScore(), gameWidth*0.4f, gameHeight * 0.86f);
        AssetManager.font.draw(staticSpriteBatch, "" + player.ammo, gameWidth * 0.4f, gameHeight*0.92f);
    }

    private void drawHighScore() {
        AssetManager.font.draw(dynamicSpriteBatch, "High score: " + AssetManager.getHighScore(), 10, 20);
    }

    private void drawBackButton() {
        menuButtons.get(1).draw(dynamicSpriteBatch);
    }

    private void drawCollectButton() {

        shootButton.draw(staticSpriteBatch);

        if(gameWorld.collectingMushroom)
            staticSpriteBatch.draw(collectedMushroom, collectButton.x,collectButton.y,collectButton.width,collectButton.height);

        else
            staticSpriteBatch.draw(mushroomButton, collectButton.x,collectButton.y,collectButton.width,collectButton.height);
    }

    private void drawSebaBounds() {
        shapeRenderer.setColor(1, 0, 0, 1);
        shapeRenderer.rect(player.getBoundingRectangle().x, player.getBoundingRectangle().y, player.getBoundingRectangle().width, player.getBoundingRectangle().height);
    }

    private void drawMushroomState() {

        shapeRenderer2.setColor(0, 0, 0, 1);
        shapeRenderer2.rect(10, 275, 32, 5);

        if(gameWorld.collectingMushroom || gameWorld.aboveMushroom) {
            //System.out.println("Above mushroom! Health: "+(gameWorld.currentMushroom.health/gameWorld.currentMushroom.maxhealth));
            shapeRenderer2.setColor(0, 0.5f, 0, 1);
            shapeRenderer2.rect(10, 275, 32*((gameWorld.currentMushroom.maxhealth- gameWorld.currentMushroom.health)/ gameWorld.currentMushroom.maxhealth), 5);
        }

        else{
            //System.out.println("No mushroom!");
            shapeRenderer2.setColor(1, 1, 1, 1);
            shapeRenderer2.rect(10, 275, 32, 5);
        }

    }

    private void drawUI() {

        shapeRenderer2.setColor(255 / 255.0f, 255 / 255.0f, 255 / 255.0f, 1);
        shapeRenderer2.rect(0, gameHeight * 0.75f, gameWidth, gameHeight);
        joystick.draw(shapeRenderer2);
    }

    private void drawHUD() {
        staticSpriteBatch.draw(heart, gameWidth * 0.3f, gameHeight * 0.78f, 16, 16);
        staticSpriteBatch.draw(mushroomHUD, gameWidth * 0.3f, gameHeight * 0.84f, 16, 16);
        staticSpriteBatch.draw(cone, gameWidth * 0.3f, gameHeight * 0.9f, 16, 16);
    }

    private void drawHighScoreMonit() {
        AssetManager.font.draw(dynamicSpriteBatch, "HIGH SCORE!!!", 5 , 200);
    }

    private void drawTiles() {
        for (int i = 0; i < tiles.size(); i++) {
            Tile t = tiles.get(i);

            if(t.getTileType()==1)
                dynamicSpriteBatch.draw(stone, t.getTileX(), t.getTileY(), 32/2.0f, 32/2.0f, 32, 32, 1, 1,0);

            else if(t.getTileType()==0)
                dynamicSpriteBatch.draw(grass, t.getTileX(), t.getTileY(), 32/2.0f, 32/2.0f, 32, 32, 1, 1,0);

            else if(t.getTileType()==2)
                dynamicSpriteBatch.draw(grass, t.getTileX(), t.getTileY(), 32/2.0f, 32/2.0f, 32, 32, 1, 1,0);

            else if(t.getTileType()==3)
                dynamicSpriteBatch.draw(sand, t.getTileX(), t.getTileY(), 32/2.0f, 32/2.0f, 32, 32, 1, 1,0);

            else if(t.getTileType()==4)
                dynamicSpriteBatch.draw(lava, t.getTileX(), t.getTileY(), 32/2.0f, 32/2.0f, 32, 32, 1, 1,0);

            else if(t.getTileType()==5)
                dynamicSpriteBatch.draw(water, t.getTileX(), t.getTileY(), 32/2.0f, 32/2.0f, 32, 32, 1, 1,0);

        }
    }

    private void drawProjectiles() {

        for (int i = 0; i < projectiles.size(); i++) {
            Projectile p = projectiles.get(i);
            shapeRenderer.setColor(119/255.0f, 73/255.0f, 49/255.0f, 1);
            shapeRenderer.rect(p.position.x, p.position.y, 2, 2);
        }

    }

}
