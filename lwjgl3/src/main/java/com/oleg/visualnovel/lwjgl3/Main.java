package com.oleg.visualnovel.lwjgl3;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;


/**
 * The main class of the game. Inherits from Game, which allows
 * to use the LibGDX screen system.
 */
public class Main extends Game {
    public SpriteBatch batch;
    public BitmapFont font;
    public StoryManager storyManager;

    @Override
    public void create() {
        batch = new SpriteBatch();

        font = new BitmapFont();
        font.getData().setScale(2f);

        storyManager = new StoryManager();
        storyManager.loadStory();

        setScreen(new MainMenuScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();

        if (screen != null) {
            screen.dispose();
        }
    }
}
