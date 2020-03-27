package com.notouching.controller;

import com.notouching.model.*;
import com.notouching.view.Playground;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class GameEngine {
    private List<GameEntity> entities = new ArrayList<>();
    private Player player;
    private List<People> people;
    private List<Food> food;
    private Playground playground;
    private int mapSize;


    public void Play() {
        setMapSize(15);
        player = new Player(mapSize / 2, mapSize / 2);
        setPeople();
        setFood();
        playground = new Playground(this, people, mapSize, player.getY(), player.getX());
        playground.render();
        for (Food f : food) {
            System.out.println(String.format("type: %s, y = %d, x = %d", f.getType(), f.getY(), f.getX()));
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
        for (GameEntity entity : entities) {
            if (entity.getY() == y && entity.getX() == x) {
                return true;
            }
        }
        return false;
    }

    public Virus chooseVirus(int y, int x, int n) {
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
        return new Virus(type, y, x);
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
            People person = new People(new Virus(VirusType.getRandomVirus(), y, x), y, x);
            this.people.add(person);
            entities.add(person);
        }
    }

    public void setFood() {
        this.food = new ArrayList<>();

        int y, x;
        int score;

        score = (int) (20 + Math.random()*100);

        for (int i = 0; i < 20; i++) {
            y = randomGenerator(mapSize);
            x = randomGenerator(mapSize);

            while ((y == player.getY() && x == player.getX()) || isOccupied(y, x)) {
                x = randomGenerator(mapSize);
                y = randomGenerator(mapSize);
            }
            Food f = new Food(FoodType.getRandomFood(), score, y, x);
            this.food.add(f);
            entities.add(f);
        }
    }

    public void playerMoved(PlayerMove move) {
        int y, x;
        y = player.getY();
        x = player.getX();
        switch (move) {
            case UP:
                if (y != 0)
                    y--;
                break;
            case DOWN:
                if (y < mapSize - 1)
                    y++;
                break;
            case LEFT:
                if (x != 0)
                    x--;
                break;
            case RIGHT:
                if (x < mapSize - 1)
                    x++;
                break;
        }

//        if (isOccupied(x, y)) {
//            // int result = game.showMessageDialog();
//            playground.showMessageDialog();
//            return;
//        }


        int oldY = player.getY();
        int oldX = player.getX();

        player.setY(y);
        player.setX(x);

        playground.renderPlayer(oldY, oldX, y, x);
    }

    public Player getPlayer() {
        return player;
    }

    public List<GameEntity> getEntities() {
        return entities;
    }

    public List<People> getPeople() {
        return people;
    }

    public List<Food> getFood() {
        return food;
    }
}
