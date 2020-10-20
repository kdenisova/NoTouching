package com.notouching.model;

import com.notouching.controller.PlayerInteraction;

import java.util.Objects;

public class Food extends GameEntity {
    private FoodType type;
    private int score;

    public Food(FoodType type, int score, int y, int x) {
        super(EntityType.FOOD, y, x);
        this.type = type;
        this.score = score;
    }

    public FoodType getType() {
        return type;
    }

    public void setType(FoodType type) {
        this.type = type;
    }

    public int getScore() { return score; }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public void accept(PlayerInteraction interaction) {
        interaction.interact(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Food food = (Food) o;

        return type == food.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }
}