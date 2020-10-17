package com.notouching.model;

import com.notouching.controller.PlayerInteraction;

public class Sanitizer extends GameEntity {
    public Sanitizer(EntityType entityType, int y, int x) {
        super(entityType, y, x);
    }

    @Override
    public void accept(PlayerInteraction interaction) {
        interaction.interact(this);
    }
}