package com.notouching.controller;

import com.notouching.model.*;
import com.notouching.view.Playground;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GameEngine implements Visitor, ViewInteraction {
    private final List<GameEntity> entities = new ArrayList<>();
    private final Player player;
    private final int mapSize = 15;

    private boolean status;
    private int speed;
    private int skipCount;

    private List<People> people;
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
        setPeople();
        setFood();
        setSanitizers();
        setGroceryList();
        setFoundItems();
        setSpeed(1000 / player.getLevel());

        playground = new Playground(this, mapSize);
        playground.render(player, groceryList, people, food, sanitizers);

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

                            for (People person : people) {
                                y = randomGenerator(2);
                                x = randomGenerator(2);

                                if (y == 0)
                                    y = person.getY() - 1;
                                else
                                    y = person.getY() + 1;

                                if (x == 0)
                                    x = person.getX() - 1;
                                else
                                    x = person.getX() + 1;

                                if (!isOccupied(y, x) && (x >= 0 && x < mapSize) && (y >= 0 && y < mapSize)) {
                                    int oldY = person.getY();
                                    int oldX = person.getX();

                                    playground.movePeople(oldY, oldX, y, x);

                                    person.setY(y);
                                    person.setX(x);

                                    if (y == player.getY() && x == player.getX())
                                        interact(person);
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
        people.clear();
        food.clear();
        groceryList.clear();
        sanitizers.clear();
        foundItems.clear();

        if (viruses != null)
            viruses.clear();

        play();
    }

    public void doAction(int y, int x) {
        for (GameEntity entity : entities) {
            if (entity.getY() == y && entity.getX() == x) {
                switch (entity.getEntityType()) {
                    case PEOPLE:
                        interact((People)entity);
                        return;
                    case FOOD:
                        interact((Food)entity);
                        return;
                    case SANITIZER:
                        interact((Sanitizer)entity);
                        return;
                }
            }
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

    public void setPeople() {
        people = new ArrayList<>();

        int y, x;

        for (int i = 0; i < mapSize; i++) {
            y = randomGenerator(mapSize);
            x = randomGenerator(mapSize);

            while ((y == player.getY() && x == player.getX()) || isOccupied(y, x)) {
                x = randomGenerator(mapSize);
                y = randomGenerator(mapSize);
            }

            People person = new People(new Virus(VirusType.getRandomVirus(), y, x), y, x);
            people.add(person);
            entities.add(person);
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
        if (viruses == null)
            viruses = new ArrayList<>();

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

    public List<People> getPeople() {
        return people;
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

    public void removeFood(Food current) {
        for (int i = 0; i < food.size(); i++) {
            if (food.get(i).equals(current) && food.get(i).getY() == current.getY() && food.get(i).getX() == current.getX()) {
                food.remove(i);

                for (int j = 0; j < entities.size(); j++) {
                    if (entities.get(j).getEntityType().equals(EntityType.FOOD)
                            && entities.get(j).getY() == current.getY() && entities.get(j).getX() == current.getX()) {

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
    public void interact(People people) {
        if (player.getSanitizer() == 0) {
            if (player.getHealth() - people.getVirus().getDamage() > 0) {
                player.setHealth(player.getHealth() - people.getVirus().getDamage());
                playground.updateHealth(player.getHealth());
                setViruses(people.getVirus());
            }
            else {
                player.setHealth(0);
                setViruses(people.getVirus());
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
    public void interact(Food f) {
        player.setScore(player.getScore() + f.getScore());
        playground.updateScore(player.getScore());
        playground.removeEntity(f.getY(), f.getX());
        skipCount = 2;

        for (int i = 0; i < groceryList.size(); i++) {
            if (groceryList.get(i).equals(f)) {
                foundItems.add(f);
                playground.updateGroceryList(i);
                break;
            }
        }

        removeFood(f);
    }

    @Override
    public void interact(Sanitizer sanitizer) {
        player.setSanitizer(player.getSanitizer() + 1);
        playground.updateSanitizer(player.getSanitizer());
        playground.removeEntity(sanitizer.getY(), sanitizer.getX());
    }
}