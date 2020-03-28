package com.notouching.model;

import com.notouching.controller.Interaction;
import com.notouching.controller.Visitor;

public class Food extends GameEntity implements Interaction {
    private FoodType type;
    int points;

    public Food(FoodType type, int points, int y, int x) {
        super(EntityType.FOOD, y, x);
        this.type = type;
        this.points = points;
    }

    public FoodType getType() {
        return type;
    }

    public void setType(FoodType type) {
        this.type = type;
    }

    public int getPoints() { return points; }

    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.interact(this);
    }
}