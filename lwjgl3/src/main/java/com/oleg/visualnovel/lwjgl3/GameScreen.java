package com.oleg.visualnovel.lwjgl3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * Main game screen for the visual novel
 */
public class GameScreen implements Screen {
    private final Main game;
    private Stage stage;
    private Texture defaultBackground;
    private Texture currentBackground;
    private com.badlogic.gdx.scenes.scene2d.ui.Skin skin;
    private Table choiceTable;
    private Texture dialogBoxBackground;
    private boolean showingChoices = false;
    private boolean isTyping = false;
    private String currentDisplayText = "";
    private float typeTimer = 0;
    private float typeSpeed = 30;
    private int currentCharIndex = 0;
    private String fullDialogText = "";
    private boolean isMenuOpen = false;
    private Table menuTable;
    private boolean shouldSlowDown = false;
    private float slowdownFactor = 0.3f;
    private Color dimColor = new Color(0, 0, 0, 0.5f);
    private BitmapFont dialogFont;
    private static final float DIALOG_BOX_WIDTH = 1500;
    private static final float DIALOG_BOX_HEIGHT = 500;
    private static final float DIALOG_BOX_X = (1920 - DIALOG_BOX_WIDTH) / 2;
    private static final float DIALOG_BOX_Y = 50;

    public GameScreen(Main game) {
        Gdx.app.log("GameScreen", "Constructor called");
        this.game = game;
    }

    @Override
    public void show() {
        Gdx.app.log("GameScreen", "Show method called");

        stage = new Stage(new FitViewport(1920, 1080));
        Gdx.input.setInputProcessor(stage);

        try {
            defaultBackground = new Texture("background.jpg");
            currentBackground = defaultBackground;
            dialogBoxBackground = new Texture("dialog_box5.png");
            skin = new com.badlogic.gdx.scenes.scene2d.ui.Skin(Gdx.files.internal("uiskin.json"));
        } catch (Exception e) {
            Gdx.app.error("GameScreen", "Error loading resources: " + e.getMessage());
            if (defaultBackground == null) {
                defaultBackground = new Texture(1, 1, Pixmap.Format.RGB888);
                currentBackground = defaultBackground;
            }
            if (dialogBoxBackground == null) {
                dialogBoxBackground = new Texture(1, 1, Pixmap.Format.RGBA8888);
            }
            if (skin == null) {
                skin = new com.badlogic.gdx.scenes.scene2d.ui.Skin();
                BitmapFont font = new BitmapFont();
                com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle textButtonStyle = new com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle();
                textButtonStyle.font = font;
                textButtonStyle.fontColor = Color.WHITE;
                skin.add("default", textButtonStyle);
            }
        }

        createChoiceTable();
        createMenuTable();
        createDialogFont();
        startStory();
    }

    private void createDialogFont() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Jersey10-Regular.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 48;
        parameter.minFilter = Texture.TextureFilter.Linear;
        parameter.magFilter = Texture.TextureFilter.Linear;
        dialogFont = generator.generateFont(parameter);
        generator.dispose();
    }

    private void createChoiceTable() {
        choiceTable = new Table();
        choiceTable.setFillParent(true);
        choiceTable.align(Align.center);
        choiceTable.setVisible(false);
        stage.addActor(choiceTable);
    }

    private void createMenuTable() {
        menuTable = new Table();
        menuTable.setFillParent(true);
        menuTable.align(Align.center);
        menuTable.setVisible(false);
        stage.addActor(menuTable);

        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Jersey10-Regular.ttf"));
        FreeTypeFontParameter fontParameter = new FreeTypeFontParameter();
        fontParameter.size = 36;
        fontParameter.color = Color.WHITE;
        fontParameter.borderWidth = 2;
        fontParameter.borderColor = Color.BLACK;
        BitmapFont menuFont = fontGenerator.generateFont(fontParameter);
        fontGenerator.dispose();

        TextButton.TextButtonStyle continueButtonStyle = new TextButton.TextButtonStyle();
        continueButtonStyle.font = menuFont;
        continueButtonStyle.up = new com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable(
            new Texture(Gdx.files.internal("ui/button.png")));
        continueButtonStyle.down = new com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable(
            new Texture(Gdx.files.internal("ui/button.png")));
        continueButtonStyle.over = new com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable(
            new Texture(Gdx.files.internal("ui/button.png")));

        TextButton.TextButtonStyle mainMenuButtonStyle = new TextButton.TextButtonStyle(continueButtonStyle);
        TextButton.TextButtonStyle exitButtonStyle = new TextButton.TextButtonStyle(continueButtonStyle);

        TextButton continueButton = new TextButton("Continue", continueButtonStyle);
        continueButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isMenuOpen = false;
                menuTable.setVisible(false);
                shouldSlowDown = false;
                Gdx.input.setInputProcessor(stage);
            }
        });
        menuTable.add(continueButton).width(300).height(80).padBottom(20);
        menuTable.row();

        TextButton mainMenuButton = new TextButton("Main menu", mainMenuButtonStyle);
        mainMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });
        menuTable.add(mainMenuButton).width(300).height(80).padBottom(20);
        menuTable.row();

        TextButton exitButton = new TextButton("Exit", exitButtonStyle);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        menuTable.add(exitButton).width(300).height(80);
    }

    private void startStory() {
        if (game.storyManager != null) {
            Gdx.app.log("GameScreen", "Starting story");
            DialogLine currentDialog = game.storyManager.getCurrentDialog();
            if (currentDialog != null) {
                Gdx.app.log("GameScreen", "Current dialog: " + currentDialog.getText());
                startTypingEffect(currentDialog.getText());
                if (currentDialog.getBackgroundImage() != null) {
                    currentBackground = currentDialog.getBackgroundImage();
                } else {
                    currentBackground = defaultBackground;
                }
            }
        } else {
            Gdx.app.error("GameScreen", "StoryManager is null");
        }
    }

    @Override
    public void render(float delta) {
        float deltaTime = shouldSlowDown ? delta * slowdownFactor : delta;

        ScreenUtils.clear(0, 0, 0, 1);

        stage.act(deltaTime);

        if (isTyping) {
            updateTypingEffect(deltaTime);
        }

        DialogLine currentDialog = null;
        if (game.storyManager != null) {
            currentDialog = game.storyManager.getCurrentDialog();
        }

        game.batch.begin();

        if (currentBackground != null) {
            game.batch.draw(currentBackground, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }

        if (currentDialog != null && currentDialog.getCharacterImage() != null) {
            Texture charImg = currentDialog.getCharacterImage();
            float scale = 0.8f;
            float charWidth = charImg.getWidth() * scale;
            float charHeight = charImg.getHeight() * scale;
            float x = Gdx.graphics.getWidth() / 2 - charWidth / 2;
            float y = Gdx.graphics.getHeight() / 2 - charHeight / 2 + 50;

            game.batch.draw(charImg, x, y, charWidth, charHeight);
        }

        if (dialogBoxBackground != null) {
            game.batch.draw(dialogBoxBackground, DIALOG_BOX_X, DIALOG_BOX_Y, DIALOG_BOX_WIDTH, DIALOG_BOX_HEIGHT);
        }

        if (currentDialog != null && dialogFont != null) {
            dialogFont.setColor(Color.RED);
            dialogFont.draw(game.batch, currentDialog.getSpeaker(),
                DIALOG_BOX_X + 100, DIALOG_BOX_Y + DIALOG_BOX_HEIGHT - 140);

            dialogFont.setColor(Color.BLACK);
            dialogFont.draw(game.batch, currentDisplayText,
                DIALOG_BOX_X + 100, DIALOG_BOX_Y + DIALOG_BOX_HEIGHT - 200,
                DIALOG_BOX_WIDTH - 200, Align.left, true);
        }

        if (isMenuOpen) {
            game.batch.setColor(dimColor);
            game.batch.setColor(Color.WHITE);
        }

        game.batch.end();

        stage.draw();

        handleInput();
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            isMenuOpen = !isMenuOpen;
            menuTable.setVisible(isMenuOpen);
            shouldSlowDown = isMenuOpen;
            if (isMenuOpen) {
                Gdx.input.setInputProcessor(stage);
            } else {
                Gdx.input.setInputProcessor(stage);
            }
        }

        if (isMenuOpen) {
            return;
        }

        if (showingChoices) {
            return;
        }

        boolean advanceDialogKeyPressed = Gdx.input.isKeyJustPressed(Input.Keys.SPACE) ||
            Gdx.input.isKeyJustPressed(Input.Keys.ENTER) ||
            Gdx.input.justTouched();

        if (!isTyping) {
            if (advanceDialogKeyPressed) {
                if (game.storyManager != null) {
                    boolean hasNextDialog = game.storyManager.nextDialog();

                    if (hasNextDialog) {
                        DialogLine nextDialog = game.storyManager.getCurrentDialog();
                        if (nextDialog != null) {
                            startTypingEffect(nextDialog.getText());
                            if (nextDialog.getBackgroundImage() != null) {
                                currentBackground = nextDialog.getBackgroundImage();
                            }
                        }
                    } else {
                        Array<String> choices = game.storyManager.getCurrentChoices();
                        if (choices != null && choices.size > 0) {
                            showChoices(choices);
                        } else {
                            Gdx.app.log("GameScreen", "End of story, returning to main menu");
                            game.setScreen(new MainMenuScreen(game));
                        }
                    }
                }
            }
        } else if (advanceDialogKeyPressed) {
            skipTypingAnimation();
        }
    }

    private void showChoices(Array<String> choices) {
        showingChoices = true;
        choiceTable.clear();
        choiceTable.setVisible(true);

        for (final String choice : choices) {
            TextButton choiceButton = new TextButton(choice, skin);
            choiceButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (game.storyManager != null) {
                        game.storyManager.makeChoice(choice);
                        showingChoices = false;
                        choiceTable.setVisible(false);
                        DialogLine nextDialog = game.storyManager.getCurrentDialog();
                        if (nextDialog != null) {
                            startTypingEffect(nextDialog.getText());
                            if (nextDialog.getBackgroundImage() != null) {
                                currentBackground = nextDialog.getBackgroundImage();
                            }
                        }
                    }
                }
            });

            choiceTable.add(choiceButton).width(400).pad(10);
            choiceTable.row();
        }
    }

    private void startTypingEffect(String text) {
        fullDialogText = text;
        currentDisplayText = "";
        currentCharIndex = 0;
        isTyping = true;
        typeTimer = 0;
    }

    private void updateTypingEffect(float delta) {
        if (!isTyping) return;

        typeTimer += delta;

        int charsToAdd = (int)(typeTimer * typeSpeed);

        if (charsToAdd > 0) {
            typeTimer = 0;

            for (int i = 0; i < charsToAdd && currentCharIndex < fullDialogText.length(); i++) {
                currentCharIndex++;
                currentDisplayText = fullDialogText.substring(0, currentCharIndex);
            }

            if (currentCharIndex >= fullDialogText.length()) {
                isTyping = false;
            }
        }
    }

    private void skipTypingAnimation() {
        currentDisplayText = fullDialogText;
        currentCharIndex = fullDialogText.length();
        isTyping = false;
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
        if (stage != null) stage.dispose();
        if (defaultBackground != null) defaultBackground.dispose();
        if (dialogBoxBackground != null && dialogBoxBackground != defaultBackground) {
            dialogBoxBackground.dispose();
        }
        if (skin != null) skin.dispose();
        if (dialogFont != null) dialogFont.dispose();
    }
}
