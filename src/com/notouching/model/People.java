package com.notouching.model;

import com.notouching.controller.Interaction;
import com.notouching.controller.Visitor;

public class People extends GameEntity implements Interaction {
    private Virus virus;

    public People(Virus virus, int y, int x) {
        super(EntityType.PEOPLE, y, x);
        this.virus = virus;
    }

    public Virus getVirus() {
        return virus;
    }

    public void setVirus(Virus virus) {
        this.virus = virus;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.interact(this);
    }
}
