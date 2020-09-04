package com.notouching.model;

public class GameEntity {
    private EntityType entityType;
    private int y;
    private int x;

    public GameEntity(EntityType entityType, int y, int x) {
        this.entityType = entityType;
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

    public EntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }
}