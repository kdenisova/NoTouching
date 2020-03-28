package com.notouching.model;

import com.notouching.controller.Interaction;
import com.notouching.controller.Visitor;

public class Sanitizer extends GameEntity implements Interaction {
    public Sanitizer(EntityType entityType, int y, int x) {
        super(entityType, y, x);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.interact(this);
    }
}
