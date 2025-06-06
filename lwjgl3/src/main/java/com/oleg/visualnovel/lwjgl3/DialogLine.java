package com.oleg.visualnovel.lwjgl3;

import com.badlogic.gdx.graphics.Texture;

/**
 * Class for storing a single line of dialogue in a visual novel.
 * Contains information about the speaking character and the text of the line.
 */
public class DialogLine {
    private String speaker;
    private String text;
    private Texture characterImage;
    private Texture backgroundImage;

    /**
     * Constructor for creating a dialogue line
     * @param speaker Name of the speaking character
     * @param text Text of the line
     */
    public DialogLine(String speaker, String text) {
        this.speaker = speaker;
        this.text = text;
    }

    /**
     * Constructor with character image
     * @param speaker Name of the speaking character
     * @param text Text of the line
     * @param characterImage Character image
     */
    public DialogLine(String speaker, String text, Texture characterImage) {
        this.speaker = speaker;
        this.text = text;
        this.characterImage = characterImage;
    }

    /**
     * Constructor with character image and background
     * @param speaker Name of the speaking character
     * @param text Text of the line
     * @param characterImage Character image
     * @param backgroundImage Background image
     */
    public DialogLine(String speaker, String text, Texture characterImage, Texture backgroundImage) {
        this.speaker = speaker;
        this.text = text;
        this.characterImage = characterImage;
        this.backgroundImage = backgroundImage;
    }


    public String getSpeaker() {
        return speaker;
    }

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Texture getCharacterImage() {
        return characterImage;
    }

    public void setCharacterImage(Texture characterImage) {
        this.characterImage = characterImage;
    }

    public Texture getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(Texture backgroundImage) {
        this.backgroundImage = backgroundImage;
    }
}
