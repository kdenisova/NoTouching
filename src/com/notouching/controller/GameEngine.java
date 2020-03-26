package com.notouching.controller;

import com.notouching.model.People;
import com.notouching.model.Player;
import com.notouching.model.Virus;
import com.notouching.model.VirusType;

import java.util.ArrayList;
import java.util.List;

public class GameEngine {
    private Player player;
    private List<People> people;
    private int mapSize;

    public void Play() {
        setMapSize(15);
        player = new Player(mapSize / 2, mapSize / 2);
        setPeople();

        for (People person : people) {
            System.out.println(String.format("type: %s, y = %d, x = %d", person.getVirus().getType(), person.getY(), person.getX()));
        }

    }

    public int getMapSize() {
        return mapSize;
    }

    public void setMapSize(int mapSize) {
        this.mapSize = mapSize;
    }

    public int randomGenerator(int n) {
        return (int) (Math.random() * n);
    }

    public boolean isOccupied(int y, int x) {
        for (People person : people) {
            if (person.getY() == y && person.getX() == x) {
                return true;
            }
        }
        return false;
    }

    public Virus chooseVirus(int n) {
        VirusType type;
        switch (n) {
            case 1:
                type = VirusType.COVID19;
                break;
            case 2:
                type = VirusType.INFLUENZA;
                break;
            default:
                type = VirusType.ROTAVIRUS;
                break;
        }
        return new Virus(type);
    }

    public void setPeople() {
        this.people = new ArrayList<>();

        int y, x;

        for (int i = 0; i < mapSize; i++) {
            y = randomGenerator(mapSize);
            x = randomGenerator(mapSize);

            while ((y == player.getY() && x == player.getX()) || isOccupied(x, y)) {
                x = randomGenerator(mapSize);
                y = randomGenerator(mapSize);
            }

            this.people.add(new People(chooseVirus(randomGenerator(3)), y, x));
        }
    }
}
