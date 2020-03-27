package com.notouching.model;

public class Food {
    private FoodType type;
    private int y;
    private int x;


    public Food(FoodType type, int x, int y) {
        this.type = type;
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

    public FoodType getType() {
        return type;
    }

    public void setType(FoodType type) {
        this.type = type;
    }
}