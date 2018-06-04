package com.ratowski.Game;

import com.badlogic.gdx.Game;
import com.ratowski.GameScreens.GameScreen;
import com.ratowski.Helpers.AssetManager;

public class MushroomGame extends Game {

	@Override
	public void create () {
		AssetManager.load();
		//setScreen(new SplashScreen(this));
		setScreen(new GameScreen());
	}

	@Override
	public void dispose() {
		super.dispose();
		AssetManager.dispose();
	}
}
