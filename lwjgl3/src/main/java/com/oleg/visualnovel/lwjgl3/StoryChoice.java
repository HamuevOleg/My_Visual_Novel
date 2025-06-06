package com.oleg.visualnovel.lwjgl3;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

/**
 * A class representing a single choice in a visual novel
 */

public class StoryChoice {
    private String text;

    private int targetSceneIndex;

    /**
     * Choice constructor
     * @param text Choice text
     * @param targetSceneIndex Target scene index
     */
    public StoryChoice(String text, int targetSceneIndex) {
        this.text = text;
        this.targetSceneIndex = targetSceneIndex;
    }
    public String getText() {
        return text;
    }

    public int getTargetSceneIndex() {
        return targetSceneIndex;
    }
}

