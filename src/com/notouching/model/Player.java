package com.notouching.model;

public class Player {
    private int level;
    private int experience;
    private int health;
    private int sanitizer;
    private int y;
    private int x;

    public Player(int y, int x) {
        this.level = 1;
        this.experience = 0;
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

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
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
}
