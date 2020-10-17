package com.notouching.model;

import com.notouching.controller.PlayerInteraction;

public class Customer extends GameEntity {
    private Virus virus;

    public Customer(Virus virus, int y, int x) {
        super(EntityType.CUSTOMER, y, x);
        this.virus = virus;
    }

    public Virus getVirus() {
        return virus;
    }

    public void setVirus(Virus virus) {
        this.virus = virus;
    }

    @Override
    public void accept(PlayerInteraction interaction) {
        interaction.interact(this);
    }
}