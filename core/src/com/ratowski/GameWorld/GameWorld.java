package com.ratowski.GameWorld;

import com.badlogic.gdx.Gdx;
import com.ratowski.GameObjects.Ammo;
import com.ratowski.GameObjects.Collector;
import com.ratowski.GameObjects.DrawnObject;
import com.ratowski.GameObjects.Ground;
import com.ratowski.GameObjects.Monster;
import com.ratowski.GameObjects.Mushroom;
import com.ratowski.GameObjects.Obstacle;
import com.ratowski.GameObjects.Player;
import com.ratowski.GameObjects.Projectile;
import com.ratowski.Helpers.Tile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;


public class GameWorld {

    private Player player;
    private int score = 0;
    public boolean collectingMushroom = false;
    public boolean aboveMushroom = false;
    public Mushroom currentMushroom;

    public ArrayList<Tile> tilearray = new ArrayList<Tile>();
    public ArrayList<Tile> grasses = new ArrayList<Tile>();
    public ArrayList<Obstacle> obstacles = new ArrayList<Obstacle>();
    public ArrayList<Monster> monsters = new ArrayList<Monster>();
    public ArrayList<Collector> collectors = new ArrayList<Collector>();
    public ArrayList<Mushroom> mushrooms = new ArrayList<Mushroom>();
    public ArrayList<DrawnObject> objects = new ArrayList<DrawnObject>();
    public ArrayList<Ground> lavas = new ArrayList<Ground>();
    public ArrayList<Ammo> ammoArray = new ArrayList<Ammo>();
    public ArrayList<Ground> waters = new ArrayList<Ground>();
    public ArrayList<Projectile> projectileArray = new ArrayList<Projectile>();

    public enum GameState {MENU, READY, RUNNING, GAMEOVER, HIGHSCORE}

    private GameState currentState;
    public int midPointY;

    public GameWorld(int midPointY) {
        currentState = GameState.MENU;
        player = new Player(160, 160, 14, 19, this);
        this.midPointY = midPointY;

        try {
            loadMap("data/map2.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        setupTileArray();
        setupObjects();
    }

    public void update(float delta) {
        switch (currentState) {
            case READY:
                break;
            case MENU:
                updateReady(delta);
                break;
            case RUNNING:
                updateRunning(delta);
                break;
            default:
                break;
        }
    }

    private void updateReady(float delta) {
        addObjectsIfNecessary();
        updateObjects(delta);
    }

    public void updateMushrooms() {
        if (player.isAlive()) {
            Iterator<Mushroom> it = mushrooms.iterator();
            while (it.hasNext()) {
                Mushroom mushroom = it.next();
                mushroom.update();
                if (mushroom.collides(player)) {
                    currentMushroom = mushroom;
                    if (player.isCollecting) {
                        collectingMushroom = true;
                        if (mushroom.health == 0) {
                            if (!mushroom.collected) {
                                addScore(1);
                                mushroom.collected = true;
                                it.remove();
                            }
                        } else {
                            mushroom.addHealth(-1);
                        }
                        break;
                    }
                    aboveMushroom = true;
                    collectingMushroom = false;
                    break;
                }
                player.isAbove = false;
                aboveMushroom = false;
                collectingMushroom = false;
            }
        }
    }


    public void updateRunning(float delta) {

        if (delta > .15f) {
            delta = .15f;
        }
        player.update(delta);


    }

    public int getScore() {
        return score;
    }

    public void addScore(int increment) {
        score += increment;
    }

    public Player getPlayer() {
        return player;
    }

    public int getMidPointY() {
        return midPointY;
    }

    public void start() {
        currentState = GameState.RUNNING;
    }

    public void menu() {
        currentState = GameState.MENU;
    }

    public void ready() {
        currentState = GameState.READY;
    }

    public void restart() {
        score = 0;
        player.onRestart(midPointY - 5);
    }

    public void addMushrooms(int howMany) {
        for (int i = 0; i < howMany; i++) {
            Random random = new Random();
            int j = random.nextInt(grasses.size());
            mushrooms.add(new Mushroom(grasses.get(j).getTileX() + 6, grasses.get(j).getTileY() + 6, 20, 20, 30));
        }
    }

    public void addAmmos(int howMany) {
        for (int i = 0; i < howMany; i++) {
            Random random = new Random();
            int j = random.nextInt(grasses.size());
            ammoArray.add(new Ammo(grasses.get(j).getTileX(), grasses.get(j).getTileY(), 10, 10, 10));
        }
    }

    public void addMonsters(int howMany) {
        for (int i = 0; i < howMany; i++) {
            Random random = new Random();
            int x = random.nextInt(322) + 190;
            int y = random.nextInt(226) + 190;
            monsters.add(new Monster(x, y, 20, 20, 4, player));
        }
    }

    public void addCollectors(int howMany) {
        for (int i = 0; i < howMany; i++) {
            Random random = new Random();
            int x = random.nextInt(322) + 190;
            int y = random.nextInt(226) + 190;
            collectors.add(new Collector(x, y, 20, 20, 4, this));
        }
    }

    public boolean isReady() {
        return currentState == GameState.READY;
    }

    public boolean isGameOver() {
        return currentState == GameState.GAMEOVER;
    }

    public boolean isHighScore() {
        return currentState == GameState.HIGHSCORE;
    }

    public boolean isMenu() {
        return currentState == GameState.MENU;
    }

    public boolean isRunning() {
        return currentState == GameState.RUNNING;
    }


    public Mushroom findNearestMushroom(Monster mon) {
        Mushroom nearest = mushrooms.get(0);
        double distance;
        double smallestdistance = 1000;
        for (Mushroom mush : mushrooms) {
            distance = Math.sqrt((mush.x - mon.x) * (mush.x - mon.x) + (mush.y - mon.y) * (mush.y - mon.y));
            if (distance < smallestdistance) nearest = mush;
        }
        return nearest;
    }

    private void loadMap(String filename) throws IOException {

        ArrayList lines = new ArrayList();
        int width = 0;
        int height = 0;

        BufferedReader reader = new BufferedReader(new InputStreamReader(Gdx.files.internal(filename).read()), 2048);
        //System.out.println(filename);

        while (true) {
            String line = reader.readLine();
            // no more lines to read
            if (line == null) {
                reader.close();
                break;
            }
            if (!line.startsWith("!")) {
                lines.add(line);
                width = Math.max(width, line.length());
            }
        }
        height = lines.size();

        for (int j = 0; j < height; j++) {
            String line = (String) lines.get(j);
            for (int i = 0; i < width; i++) {

                if (i < line.length()) {
                    char ch = line.charAt(i);
                    Tile t = new Tile(i * 32, j * 32, Character.getNumericValue(ch));
                    tilearray.add(t);
                }

            }
        }

    }

    private void setupTileArray() {
        for (Tile tile : tilearray) {
            if (tile.getTileType() == 0) {
                grasses.add(tile);
            } else if (tile.getTileType() == 1) {
                obstacles.add(new Obstacle(tile.getTileX(), tile.getTileY(), 32, 32));
            } else if (tile.getTileType() == 2) {
                obstacles.add(new Obstacle(tile.getTileX() + 8, tile.getTileY() + 20, 16, 12));
                objects.add(new DrawnObject(tile.getTileX(), tile.getTileY(), 32, 32, true, DrawnObject.Type.TREE));
            } else if (tile.getTileType() == 4) {
                lavas.add(new Ground(tile.getTileX(), tile.getTileY(), 32, 32, 1));
            } else if (tile.getTileType() == 5) {
                waters.add(new Ground(tile.getTileX(), tile.getTileY(), 32, 32, 1));
            }
        }
    }

    private void setupObjects() {
        for (int i = 0; i < 5; i++) {
            addMushrooms(1);
        }
        for (int i = 0; i < 3; i++) {
            addMonsters(1);
        }
        addCollectors(1);
    }


    private void addObjectsIfNecessary() {
        if (mushrooms.size() < 5) {
            addMushrooms(3);
        }
        if (monsters.size() < 3) {
            addMonsters(1);
        }
        if (ammoArray.size() < 1) {
            addAmmos(1);
        }
    }

    private void updateObjects(float delta) {
        player.updateReady(delta);
        updateObstacles();
        updateLava();
        updateWater();
        updateAmmo();
        updateCollectors(delta);
        updateMonsters(delta);
        updateProjectiles(delta);
        updateMushrooms();
    }

    private void updateObstacles() {
        for (Obstacle ob : obstacles) {
            if (ob.collidesDown(player)) {
                player.collideFromDown(ob);
            }
            if (ob.collidesUp(player)) {
                player.collideFromUp(ob);
            }
            if (ob.collidesRight(player)) {
                player.collideFromRight(ob);
            }
            if (ob.collidesLeft(player)) {
                player.collideFromLeft(ob);
            }
        }
    }

    private void updateMonsters(float delta) {
        for (Monster monster : monsters) {
            if (monster.health <= 0) {
                monsters.remove(monster);
                break;
            }
            monster.update(delta);
            if (monster.collides(player) && player.health > 0) {
                player.changeHealth(-1);
            }
        }
    }

    private void updateLava() {
        for (Ground ground : lavas) {
            if (ground.collides(player) && player.health > 0) {
                player.changeHealth(-1);
            }
        }
    }

    private void updateWater() {
        for (Ground ground : waters) {
            if (ground.collides(player) && player.health < 100) {
                player.changeHealth(+1);
            }
        }
    }

    private void updateAmmo() {
        for (Ammo ammo : ammoArray) {
            if (ammo.collides(player)) {
                player.ammo += ammo.power;
                ammoArray.remove(ammo);
            }
        }
    }

    private void updateCollectors(float delta) {
        for (Collector collector : collectors) {
            if (collector.health <= 0) {
                collectors.remove(collector);
                break;
            }
            for (int i = 0; i < mushrooms.size() - 1; i++) {
                Mushroom mush = mushrooms.get(i);
                if (collector.collides(mush)) {
                    mushrooms.remove(i);
                    break;
                }
            }
            collector.update(delta);
        }
    }

    private void updateProjectiles(float delta) {
        for (int i = 0; i < projectileArray.size(); i++) {
            Projectile projectile = projectileArray.get(i);
            if (projectile.isVisible()) {
                projectile.update(delta);
                for (Obstacle obstacle : obstacles) {
                    if (projectile.collidesObstacle(obstacle)) {
                        projectileArray.remove(i);
                        break;
                    }
                }
                for (Monster monster : monsters) {
                    if (projectile.collidesMonster(monster)) {
                        monster.addHealth(-1);
                        projectileArray.remove(i);
                        break;
                    }
                }

                for (Collector collector : collectors) {
                    if (projectile.collidesCollector(collector)) {
                        collector.addHealth(-1);
                        projectileArray.remove(i);
                        break;
                    }
                }
            } else {
                projectileArray.remove(i);
            }
        }
    }
}
