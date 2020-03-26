package com.notouching.model;

public class People {
    private Virus virus;
    private int y;
    private int x;

    public People(Virus virus, int y, int x) {
        this.virus = virus;
        this.y = y;
        this.x = x;
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

    public Virus getVirus() {
        return virus;
    }

    public void setVirus(Virus virus) {
        this.virus = virus;
    }
}
