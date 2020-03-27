package com.notouching.model;

public class Food extends GameEntity{
    private FoodType type;

    public Food(FoodType type, int y, int x) {
        super(EntityType.FOOD, y, x);
        this.type = type;
    }

    public FoodType getType() {
        return type;
    }

    public void setType(FoodType type) {
        this.type = type;
    }
}