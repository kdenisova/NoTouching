package com.notouching.controller;

import com.notouching.model.*;
import com.notouching.view.Playground;

import java.util.ArrayList;
import java.util.List;

public class GameEngine implements Visitor {
    private List<GameEntity> entities = new ArrayList<>();
    private Player player;
    private List<People> people;
    private List<Food> food;
    private List<Food> grocery;
    private Playground playground;
    private int mapSize;


    public void Play() {
        setMapSize(15);
        player = new Player(mapSize / 2, mapSize / 2);
        setPeople();
        setFood();
        setGrocery();
        playground = new Playground(this, people, mapSize, player.getY(), player.getX());
        playground.render();
//        for (Food f : food) {
//            System.out.println(String.format("type: %s, y = %d, x = %d", f.getType(), f.getY(), f.getX()));
//        }

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

        int y, x, points;

        for (int i = 0; i < 20; i++) {
            y = randomGenerator(mapSize);
            x = randomGenerator(mapSize);

            while ((y == player.getY() && x == player.getX()) || isOccupied(y, x)) {
                x = randomGenerator(mapSize);
                y = randomGenerator(mapSize);
            }

            points = (int) (20 + Math.random()*10);

            Food f = new Food(FoodType.getRandomFood(), points, y, x);
            this.food.add(f);
            entities.add(f);
        }
    }

    public boolean checkGrocery(Food food) {
        for (Food f : grocery) {
            if (f.getType() == food.getType())
                return true;
        }

        return false;
    }

    public void setGrocery() {
        this.grocery = new ArrayList<>();
        int n;

        for (int i = 0; i < mapSize / 3 + player.getLevel(); i++) {
            n = randomGenerator(food.size());

            while (checkGrocery(food.get(n))) {
                n = randomGenerator(food.size());
            }

            this.grocery.add(food.get(n));
        }
    }

    public void checkEntity(int y, int x) {
        //EntityType type;

        for (GameEntity entity : entities) {
            if (entity.getY() == y && entity.getX() == x) {
                switch (entity.getEntityType()) {
                    case VIRUS:
                        interact((Virus)entity);
                        return;
                    case PEOPLE:
                        interact((People)entity);
                        return;
                    case FOOD:
                        interact((Food)entity);
                        return;
                }
            }
        }

    }

    public boolean isOccupied(int y, int x) {
        for (GameEntity entity : entities) {
            if (entity.getY() == y && entity.getX() == x) {
                return true;
            }
        }
        return false;
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

        if (isOccupied(y, x))
            checkEntity(y, x);

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

    public List<Food> getGrocery() {
        return grocery;
    }

    @Override
    public void interact(Virus virus) {

    }

    @Override
    public void interact(People people) {
        if (player.getSanitizer() == 0) {
            if (player.getHealth() - people.getVirus().getDamage() > 0) {
                player.setHealth(player.getHealth() - people.getVirus().getDamage());
                player.setViruses(people.getVirus());
                playground.updateHealth(player.getHealth());
            }
            else {
                player.setHealth(0);
                player.setViruses(people.getVirus());
                playground.updateHealth(0);
            }
        }
        else {
            player.setSanitizer(player.getSanitizer() - 1);
            playground.updateSanitizer(player.getSanitizer());
        }
    }

    @Override
    public void interact(Food food) {
        player.setExperience(player.getExperience() + food.getPoints());
        playground.updateFood(player.getExperience(), food.getY(), food.getX());

        for (int i = 0; i < grocery.size(); i++) {
            if (grocery.get(i).getType() == food.getType()) {
                playground.updateGroceryList(i);
                //grocery.remove(i);
                break;
            }
        }

        this.food.remove(food);
        this.entities.remove(food);
        //System.out.println("size = " + this.entities.size());
    }
}
