package com.notouching.model;

public class People extends GameEntity{
    private Virus virus;

    public People(Virus virus, int y, int x) {
        super(y, x);
        this.virus = virus;
    }

    public Virus getVirus() {
        return virus;
    }

    public void setVirus(Virus virus) {
        this.virus = virus;
    }
}
