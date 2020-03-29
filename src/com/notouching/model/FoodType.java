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

//public enum FoodType {
//    APPLE,
//    CARROT,
//    CHEESE,
//    ORANGE,
//    TOMATO,
//    GRAPE,
//    BROCCOLI,
//    FISH,
//    ANANAS,
//    WATEMELON,
//    SANDWICH,
//    LIMON,
//    CHICKEN,
//    FISHSTAKE,
//    AVOCADO,
//    SAUSAGE,
//    MEET,
//    CROISSANT,
//    FRUIT,
//    PIZZA
//}