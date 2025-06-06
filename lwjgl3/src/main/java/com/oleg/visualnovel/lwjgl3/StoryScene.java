package com.oleg.visualnovel.lwjgl3;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.oleg.visualnovel.lwjgl3.DialogLine;
import com.oleg.visualnovel.lwjgl3.StoryChoice;

public class StoryScene {
    private String backgroundPath;
    private Texture backgroundTexture;
    private String description;
    private Array<String> characterPaths;
    private Array<Texture> characterTextures;
    private Array<DialogLine> dialogs;
    private Array<StoryChoice> choices;

    /**
     * Scene constructor
     * @param backgroundPath Path to background file
     * @param description Description of scene
     * @param characterPaths Paths to character image files
     */
    public StoryScene(String backgroundPath, String description, String[] characterPaths) {
        this.backgroundPath = backgroundPath;
        this.description = description;
        this.characterPaths = new Array<>();
        this.characterTextures = new Array<>();
        this.dialogs = new Array<>();
        this.choices = new Array<>();

        if (characterPaths != null) {
            for (String path : characterPaths) {
                this.characterPaths.add(path);
            }
        }

        if (description != null && !description.isEmpty()) {
            dialogs.add(new DialogLine("", description));
        }
    }

    /**
     * Add a dialog to the scene
     * @param speaker Speaker name
     * @param text Dialog text
     */
    public void addDialog(String speaker, String text) {
        dialogs.add(new DialogLine(speaker, text));
    }

    /**
     * Add a choice
     * @param text Choice text
     * @param targetSceneIndex Target scene index
     */
    public void addChoice(String text, int targetSceneIndex) {
        choices.add(new StoryChoice(text, targetSceneIndex));
    }

    public String getBackgroundPath() {
        return backgroundPath;
    }

    public void setBackgroundTexture(Texture texture) {
        this.backgroundTexture = texture;
        for (DialogLine dialog : dialogs) {
            dialog.setBackgroundImage(texture);
        }
    }

    public Texture getBackgroundTexture() {
        return backgroundTexture;
    }

    public Array<String> getCharacterPaths() {
        return characterPaths;
    }

    public void addCharacterTexture(Texture texture) {
        characterTextures.add(texture);

        if (!characterTextures.isEmpty() && !dialogs.isEmpty()) {
            for (DialogLine dialog : dialogs) {
                if (dialog.getCharacterImage() == null) {
                    dialog.setCharacterImage(characterTextures.first());
                }
            }
        }
    }

    public Array<Texture> getCharacterTextures() {
        return characterTextures;
    }

    public Array<DialogLine> getDialogs() {
        return dialogs;
    }

    public Array<StoryChoice> getChoices() {
        return choices;
    }

    public boolean hasChoices() {
        return !choices.isEmpty();
    }
}
