package ca.pandaaa.automaticbroadcast;

import org.bukkit.Sound;

import java.util.List;

public class Broadcast {
    private String id;
    private List<String> textLines, hoverLines;
    private String clickText;
    private Sound sound;

    public Broadcast(String id) {
        this.id = id;
    }

    public Broadcast(String id, List<String> textLines, List<String> hoverLines, String clickText, Sound sound) {
        this.id = id;
        this.textLines = textLines;
        this.hoverLines = hoverLines;
        this.clickText = clickText;
        this.sound = sound;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getTextLines() {
        return textLines;
    }

    public void setTextLines(List<String> textLines) {
        this.textLines = textLines;
    }

    public List<String> getHoverLines() {
        return hoverLines;
    }

    public void setHoverLines(List<String> hoverLines) {
        this.hoverLines = hoverLines;
    }

    public String getClickText() {
        return clickText;
    }

    public void setClickText(String clickText) {
        this.clickText = clickText;
    }

    public Sound getSound() {
        return sound;
    }

    public void setSound(Sound sound) {
        this.sound = sound;
    }
}