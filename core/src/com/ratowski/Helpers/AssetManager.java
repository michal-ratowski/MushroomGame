package com.ratowski.Helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AssetManager {

    public static Texture texture,guns,mushrooms,seba,enemy,hud;

    public static TextureRegion grass,sand,lava,stone,water,tree,monster,collector;
    public static TextureRegion gun,gunshooting;
    public static TextureRegion mushroom,mushroomblack,mushroomgreen,mushroomyellow;
    public static TextureRegion mushroomhud,heart,cone;

    public static TextureRegion[] seby = new TextureRegion[11];

    public static BitmapFont font;
    public static Animation sebaAnimation;

    public static Preferences prefs;

    public static void load() {

        prefs = Gdx.app.getPreferences("Player");
        if (!prefs.contains("highScore")) {
            prefs.putInteger("highScore", 0);
        }

        // pliki

        texture = new Texture(Gdx.files.internal("data/texture.png"));
        texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

        seba = new Texture(Gdx.files.internal("data/seba.png"));
        seba.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

        guns = new Texture(Gdx.files.internal("data/gun.png"));
        guns.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

        mushrooms = new Texture(Gdx.files.internal("data/mushroom.png"));
        mushrooms.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

        hud = new Texture(Gdx.files.internal("data/hud.png"));
        hud.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

        // regiony - tekstury

        grass = new TextureRegion(texture, 0, 0, 32,32);
        grass.flip(false, true);

        sand = new TextureRegion(texture, 32, 0, 32,32);
        sand.flip(false, true);

        lava = new TextureRegion(texture, 64, 0, 32,32);
        lava.flip(false, true);

        stone = new TextureRegion(texture, 96, 0, 32,32);
        stone.flip(false, true);

        water = new TextureRegion(texture, 128, 0, 32,32);
        water.flip(false, true);

        tree = new TextureRegion(texture, 0, 32, 32,32);
        tree.flip(false, true);

        monster = new TextureRegion(texture, 32, 32, 32,32);
        monster.flip(false, true);

        collector = new TextureRegion(texture, 64, 32, 32,32);
        collector.flip(false, true);

        // regiony - gun

        gun = new TextureRegion(guns, 0, 0, 32,32);
        gun.flip(false, true);

        gunshooting = new TextureRegion(guns, 32, 0, 32,32);
        gunshooting.flip(false, true);

        // regiony - hud

        mushroomhud = new TextureRegion(hud, 0, 0, 16,16);
        mushroomhud.flip(false, true);

        heart = new TextureRegion(hud, 16, 0, 16,16);
        heart.flip(false, true);

        cone = new TextureRegion(hud, 32, 0, 16,16);
        cone.flip(false, true);

        // regiony - mushroom

        mushroom= new TextureRegion(mushrooms, 0, 0, 32,32);
        mushroom.flip(false, true);

        mushroomblack = new TextureRegion(mushrooms, 32, 0, 32,32);
        mushroomblack.flip(false, true);

        mushroomgreen = new TextureRegion(mushrooms, 64, 0, 32,32);
        mushroomgreen.flip(false, true);

        mushroomyellow = new TextureRegion(mushrooms, 96, 0, 32,32);
        mushroomyellow.flip(false, true);

        // regiony - seba

        for(int i=0;i<11;i++) {
            seby[i] = new TextureRegion(seba, i * 70, 0, 70, 108);
            seby[i].flip(false, true);
        }

        TextureRegion[] seby_walking = { seby[0],seby[4],seby[5],seby[6],seby[7],seby[8],seby[9],seby[10]};
        sebaAnimation = new Animation(0.03f, seby_walking);
        sebaAnimation.setPlayMode(Animation.PlayMode.LOOP);

        // czcionki

        font = new BitmapFont(Gdx.files.internal("data/berlin.fnt"));
        font.getData().setScale(.25f, -.25f);
    }

    public static void dispose() {
        // We must dispose of the texture when we are finished.
        texture.dispose();
        font.dispose();
    }

    public static void setHighScore(int val) {
        prefs.putInteger("highScore", val);
        prefs.flush();
    }

    // Retrieves the current high score
    public static int getHighScore() {
        return prefs.getInteger("highScore");
    }

}