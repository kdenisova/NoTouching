package com.notouching.model;

import java.util.Random;

public enum FoodType {
    Apple,
    Carrot,
    Cheese,
    Orange,
    Tomato,
    Grape,
    Broccoli,
    Fish,
    Ananas,
    Watermelon,
    Sandwich,
    Lemon,
    Chicken,
    Fishstake,
    Avocado,
    Sausage,
    Meat,
    Croissant,
    Raspberry,
    Pizza,
    Paper;

    public static FoodType getRandomFood() {
        Random random = new Random();

        return values()[random.nextInt(values().length)];
    }
}