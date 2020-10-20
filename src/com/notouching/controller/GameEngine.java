package com.notouching.controller;

import com.notouching.model.*;
import com.notouching.view.Playground;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GameEngine implements PlayerInteraction, ViewInteraction {
    private final List<GameEntity> entities = new ArrayList<>();
    private final Player player;
    private final int mapSize = 15;

    private boolean status;
    private int speed;
    private int skipCount;

    private List<Customer> customers;
    private List<Food> food;
    private List<Food> groceryList;
    private List<Sanitizer> sanitizers;
    private List<Virus> viruses;
    private Set<Food> foundItems;
    private Playground playground;

    public GameEngine() {
        player = new Player(mapSize / 2, mapSize / 2);
    }

    public void play() {
        setStatus(true);
        setCustomers();
        setFood();
        setSanitizers();
        setGroceryList();
        setFoundItems();
        setSpeed(1000 / player.getLevel());

        playground = new Playground(this, mapSize);
        playground.render(player, groceryList, customers, food, sanitizers);

        PlayerInteraction i = this;

        new Thread(new Runnable() {
            @Override
            public void run() {

                while (status) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            int y, x;

                            if (--skipCount > 0) {
                                return;
                            }

                            for (Customer customer : customers) {
                                y = randomGenerator(2);
                                x = randomGenerator(2);

                                if (y == 0)
                                    y = customer.getY() - 1;
                                else
                                    y = customer.getY() + 1;

                                if (x == 0)
                                    x = customer.getX() - 1;
                                else
                                    x = customer.getX() + 1;

                                if (!isOccupied(y, x) && (x >= 0 && x < mapSize) && (y >= 0 && y < mapSize)) {
                                    int oldY = customer.getY();
                                    int oldX = customer.getX();

                                    playground.moveCustomer(oldY, oldX, y, x);

                                    customer.setY(y);
                                    customer.setX(x);

                                    if (y == player.getY() && x == player.getX()) {
                                        customer.accept(i);
                                    }
                                }
                            }
                        }
                    });

                    try {
                        Thread.sleep(speed);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void clear() {
        player.setLevel(player.getLevel() + 1);
        player.setY(mapSize / 2);
        player.setX(mapSize / 2);

        entities.clear();
        customers.clear();
        food.clear();
        groceryList.clear();
        sanitizers.clear();
        foundItems.clear();

        if (viruses != null)
            viruses.clear();

        play();
    }

    public void doAction(int y, int x) {
        GameEntity toBeInteractedWith = null;

        for (GameEntity entity : entities) {
            if (entity.getY() == y && entity.getX() == x) {
                toBeInteractedWith = entity;
                break;
            }
        }

        if (toBeInteractedWith != null) {
            // we found somebody to be interacted with
            toBeInteractedWith.accept(this);
        }
    }

    public void isWin() {
        if (foundItems.size() == getGroceryList().size()) {
            setStatus(false);

            if (player.getLevel() + 1 <= 10) {
                playground.showMessageLevelUp(player.getLevel() + 1);
                clear();
            }
            else
                playground.showMessageWin();
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

    public int randomGenerator(int n) {
        return (int) (Math.random() * n);
    }

    public void setCustomers() {
        customers = new ArrayList<>();

        int y, x;

        for (int i = 0; i < mapSize; i++) {
            y = randomGenerator(mapSize);
            x = randomGenerator(mapSize);

            while ((y == player.getY() && x == player.getX()) || isOccupied(y, x)) {
                x = randomGenerator(mapSize);
                y = randomGenerator(mapSize);
            }

            Customer customer = new Customer(new Virus(VirusType.getRandomVirus()), y, x);
            customers.add(customer);
            entities.add(customer);
        }
    }

    public void setFood() {
        food = new ArrayList<>();

        int y, x, score;

        for (int i = 0; i < FoodType.values().length + player.getLevel(); i++) {
            y = randomGenerator(mapSize);
            x = randomGenerator(mapSize);

            while ((y == player.getY() && x == player.getX()) || isOccupied(y, x)) {
                x = randomGenerator(mapSize);
                y = randomGenerator(mapSize);
            }

            score = (int) (20 + Math.random() * 10);

            Food f = new Food(FoodType.getRandomFood(), score, y, x);
            food.add(f);
            entities.add(f);
        }
    }

    public void setGroceryList() {
        groceryList = new ArrayList<>();
        int n;

        for (int i = 0; i < mapSize / 3 + player.getLevel(); i++) {
            n = randomGenerator(food.size());

            while (groceryList.contains(food.get(n))) {
                n = randomGenerator(food.size());
            }

            groceryList.add(food.get(n));
        }
    }

    public void setSanitizers() {
        sanitizers = new ArrayList<>();

        int y, x;

        for (int i = 0; i < player.getLevel() % 5 + 1; i++) {
            y = randomGenerator(mapSize);
            x = randomGenerator(mapSize);

            while ((y == player.getY() && x == player.getX()) || isOccupied(y, x)) {
                x = randomGenerator(mapSize);
                y = randomGenerator(mapSize);
            }

            Sanitizer sanitizer = new Sanitizer(EntityType.SANITIZER, y, x);
            sanitizers.add(sanitizer);
            entities.add(sanitizer);
        }
    }

    public void setViruses(Virus virus) {
        if (viruses == null) {
            viruses = new ArrayList<>();
        }

        if (!viruses.contains(virus)) {
            viruses.add(virus);
            playground.renderVirus(virus.getType(), viruses.size());

            if (viruses.size() == 3) {
                setStatus(false);
                playground.showMessageVirusesWin();
            }
        }
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setFoundItems() {
        foundItems = new HashSet<>();
    }

    public Player getPlayer() {
        return player;
    }

    public List<GameEntity> getEntities() {
        return entities;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public List<Food> getFood() {
        return food;
    }

    public List<Food> getGroceryList() {
        return groceryList;
    }

    public List<Sanitizer> getSanitizers() {
        return sanitizers;
    }

    public List<Virus> getViruses() {
        return viruses;
    }

    public int getSpeed() {
        return speed;
    }

    public Set<Food> getFoundItems() {
        return foundItems;
    }

    public void removeFood(Food f) {
        for (int i = 0; i < food.size(); i++) {
            if (food.get(i).equals(f) && food.get(i).getY() == f.getY() && food.get(i).getX() == f.getX()) {

                food.remove(i);

                for (int j = 0; j < entities.size(); j++) {
                    if (entities.get(j).getEntityType().equals(EntityType.FOOD)
                            && entities.get(j).getY() == f.getY() && entities.get(j).getX() == f.getX()) {

                        entities.remove(j);

                        break;
                    }
                }

                break;
            }
        }
    }

    public void removeSanitizer(Sanitizer sanitizer) {
        for (int i = 0; i < sanitizers.size(); i++) {
            if (sanitizers.get(i).getY() == sanitizer.getY() && sanitizers.get(i).getX() == sanitizer.getX()) {

                sanitizers.remove(i);

                for (int j = 0; j < entities.size(); j++) {
                    if (entities.get(j).getEntityType().equals(EntityType.SANITIZER)
                            && entities.get(j).getY() == sanitizer.getY() && entities.get(j).getX() == sanitizer.getX()) {

                        entities.remove(j);

                        break;
                    }
                }

                break;
            }
        }
    }

    @Override
    public void onPlayerMoved(PlayerMove move) {
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
            doAction(y, x);

        if (status) {
            int oldY = player.getY();
            int oldX = player.getX();

            player.setY(y);
            player.setX(x);

            playground.renderPlayer(oldY, oldX, y, x);
            isWin();
        }
    }

    @Override
    public boolean isRunning() {
        return status;
    }

    @Override
    public int getSkipCount() {
        return skipCount;
    }

    @Override
    public void setSkipCount(int skipCount) {
        this.skipCount = skipCount;
    }

    @Override
    public void interact(Customer customer) {
        if (player.getSanitizer() == 0) {
            if (player.getHealth() - customer.getVirus().getDamage() > 0) {
                player.setHealth(player.getHealth() - customer.getVirus().getDamage());
                playground.updateHealth(player.getHealth());
                setViruses(customer.getVirus());
            }
            else {
                player.setHealth(0);
                setViruses(customer.getVirus());
                playground.updateHealth(0);

                if (status) {
                    setStatus(false);
                    playground.showMessageLose();
                }
            }
        }
        else {
            player.setSanitizer(player.getSanitizer() - 1);
            playground.updateSanitizer(player.getSanitizer());
        }
    }

    @Override
    public void interact(Food food) {
        player.setScore(player.getScore() + food.getScore());
        playground.updateScore(player.getScore());
        playground.removeEntity(food.getY(), food.getX());
        skipCount = 2;

        for (int i = 0; i < groceryList.size(); i++) {
            if (groceryList.get(i).equals(food)) {
                foundItems.add(food);
                playground.updateGroceryList(i);

                break;
            }
        }

        removeFood(food);
    }

    @Override
    public void interact(Sanitizer sanitizer) {
        player.setSanitizer(player.getSanitizer() + 1);
        playground.updateSanitizer(player.getSanitizer());
        playground.removeEntity(sanitizer.getY(), sanitizer.getX());
        removeSanitizer(sanitizer);
    }
}