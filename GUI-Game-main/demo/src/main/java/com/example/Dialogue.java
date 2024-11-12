package com.example;

public class Dialogue {
    private String characterName;
    private String text;

    public Dialogue(String characterName, String text) {
        this.characterName = characterName;
        this.text = text;
    }

    public String getCharacterName() {
        return characterName;
    }

    public String getText() {
        return text;
    }
}
