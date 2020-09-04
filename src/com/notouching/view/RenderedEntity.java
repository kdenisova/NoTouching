package com.notouching.view;

import com.notouching.model.GameEntity;

import javax.swing.*;
import java.awt.*;

public class RenderedEntity {
    private JLabel label;
    private final Image image;
    private final GameEntity entity;

    public RenderedEntity(JLabel label, Image image, GameEntity entity) {
        this.label = label;
        this.image = image;
        this.entity = entity;
    }

    public JLabel getLabel() {
        return label;
    }

    public void setLabel(JLabel label) {
        this.label = label;
    }

    public GameEntity getEntity() {
        return entity;
    }

    public Image getImage() {
        return image;
    }
}