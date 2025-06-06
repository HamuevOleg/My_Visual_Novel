package com.oleg.visualnovel.lwjgl3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class MainMenuScreen implements Screen {
    private final Main game;
    private Stage stage;
    private Texture backgroundImage;
    private Skin skin;
    private Music menuMusic;

    private boolean isFadingOut = false;
    private float fadeOutDuration = 1.5f;
    private float fadeOutTimeLeft = 0;
    private Runnable afterFadeOut = null;
    private float initialVolume = 0.5f;

    public MainMenuScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        backgroundImage = new Texture("backg.jpg");

        loadMenuMusic();

        createCustomSkin();
        createUI();
    }

    private void loadMenuMusic() {
        menuMusic = Gdx.audio.newMusic(Gdx.files.internal("music/Gentle Path.mp3"));

        menuMusic.setLooping(true);

        menuMusic.setVolume(initialVolume);

        menuMusic.play();
    }

    private void fadeOutMusicAndChangeScreen(Runnable action) {
        afterFadeOut = action;
        fadeOutTimeLeft = fadeOutDuration;
        initialVolume = menuMusic.getVolume();
        isFadingOut = true;
    }

    private void createCustomSkin() {
        skin = new Skin();

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/PixelifySans-VariableFont_wght.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 86;
        parameter.color = Color.WHITE;
        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();

        skin.add("default-font", font);

        Texture buttonUpTexture = new Texture(Gdx.files.internal("ui/button.png"));
        Texture buttonOverTexture = new Texture(Gdx.files.internal("ui/button.png"));
        Texture buttonDownTexture = new Texture(Gdx.files.internal("ui/button.png"));

        TextureRegionDrawable buttonUpDrawable = new TextureRegionDrawable(buttonUpTexture);
        TextureRegionDrawable buttonOverDrawable = new TextureRegionDrawable(buttonOverTexture);
        TextureRegionDrawable buttonDownDrawable = new TextureRegionDrawable(buttonDownTexture);

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.up = buttonUpDrawable;
        style.over = buttonOverDrawable;
        style.down = buttonDownDrawable;
        style.font = font;
        style.fontColor = Color.WHITE;

        skin.add("default", style);
    }

    private void createUI() {
        Table table = new Table();
        table.setFillParent(true);
        table.center();

        TextButton startButton = createButton("Start New Game", () -> {
            game.storyManager.restart();
            fadeOutMusicAndChangeScreen(() -> {
                game.setScreen(new GameScreen(game));
            });
        });

        TextButton loadButton = createButton("Load Game", () -> {
            fadeOutMusicAndChangeScreen(() -> {
                game.setScreen(new GameScreen(game));
            });
        });

        TextButton exitButton = createButton("Exit", () -> Gdx.app.exit());

        table.add(startButton).width(1000).height(240).pad(15).row();
        table.add(loadButton).width(700).height(240).pad(15).row();
        table.add(exitButton).width(600).height(240).pad(15);

        stage.addActor(table);
    }

    private TextButton createButton(String text, Runnable onClick) {
        TextButton button = new TextButton(text, skin);

        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                onClick.run();
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                button.addAction(Actions.scaleTo(1.05f, 1.05f, 0.1f));
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                button.addAction(Actions.scaleTo(1f, 1f, 0.1f));
            }
        });

        return button;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        if (isFadingOut) {
            fadeOutTimeLeft -= delta;
            if (fadeOutTimeLeft <= 0) {
                menuMusic.stop();
                isFadingOut = false;

                if (afterFadeOut != null) {
                    afterFadeOut.run();
                    afterFadeOut = null;
                }
            } else {
                float progress = fadeOutTimeLeft / fadeOutDuration;
                menuMusic.setVolume(initialVolume * progress);
            }
        }

        game.batch.begin();
        game.batch.draw(backgroundImage, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void hide() {
        fadeOutMusicAndChangeScreen(null);
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        menuMusic.dispose();
        stage.dispose();
        backgroundImage.dispose();
        skin.dispose();
    }
}
