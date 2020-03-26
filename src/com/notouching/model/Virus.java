package com.notouching.model;

public class Virus {
    private VirusType type;
    private int damage;
    private int y;
    private int x;

    public Virus(VirusType type) {
        this.type = type;
        setDamage();
    }

    public VirusType getType() {
        return type;
    }

    public void setType(VirusType type) {
        this.type = type;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage() {
        switch (type) {
            case COVID19:
               this.damage = 30;
                break;
            case INFLUENZA:
                this.damage = 15;
                break;
            case ROTAVIRUS:
                this.damage = 10;
                break;
        }
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }
}
