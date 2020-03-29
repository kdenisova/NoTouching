package com.notouching.model;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private List<Virus> viruses;
    private int level;
    private int score;
    private int health;
    private int sanitizer;
    private int y;
    private int x;

    public Player(int y, int x) {
        this.level = 1;
        this.score = 0;
        this.health = 100;
        this.sanitizer = 1;
        this.y = y;
        this.x = x;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getSanitizer() {
        return sanitizer;
    }

    public void setSanitizer(int sanitizer) {
        this.sanitizer = sanitizer;
    }

    public List<Virus> getViruses() {
        return viruses;
    }

    public void setViruses(Virus virus) {
        if (this.viruses == null) {
            this.viruses = new ArrayList<>();
        }

        this.viruses.add(virus);
    }
}
