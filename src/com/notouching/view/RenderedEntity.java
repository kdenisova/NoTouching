package com.notouching.view;

import com.notouching.model.GameEntity;

import javax.swing.*;

public class RenderedEntity {
    private JLabel label;
    private GameEntity entity;

    public RenderedEntity(JLabel label, GameEntity entity) {
        this.label = label;
        this.entity = entity;
    }

    public JLabel getLabel() {
        return label;
    }

    public GameEntity getEntity() {
        return entity;
    }
}
