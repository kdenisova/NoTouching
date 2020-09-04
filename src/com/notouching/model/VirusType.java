package com.notouching.model;

import java.util.Random;

public enum VirusType {
    COVID19,
    INFLUENZA,
    ROTAVIRUS;

    public static VirusType getRandomVirus() {
        Random random = new Random();

        return values()[random.nextInt(values().length)];
    }
}