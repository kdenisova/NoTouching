package com.notouching.view;

import com.notouching.controller.GameEngine;
import com.notouching.model.People;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class Playground {
    private GameEngine game;
    private int mapSize;
    private int iconSize;
    private JFrame frame;
    private JLabel[][] mapLabels;
    private JLabel experienceLabel;
    private JLabel playerLabel;
    private Image playerImage;
    private List<People> people;

    public Playground(GameEngine game, int mapSize) {
        this.game = game;
        this.mapSize = mapSize;
        this.iconSize = 50;
    }

    public void render() {
        frame = new JFrame("No Touching!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        GridLayout grid = new GridLayout(mapSize, mapSize);

        JPanel mapPanel = new JPanel(grid);

        mapLabels = new JLabel[mapSize][mapSize];
        BufferedImage bufferedMapImage = null;

        try {
            bufferedMapImage = ImageIO.read(new File("/Users/angrynimfa/projects/NoTouching/src/resources/img/background/bg7.png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        Image mapImage = bufferedMapImage.getScaledInstance(iconSize, iconSize + 10, Image.SCALE_SMOOTH);
        ImageIcon mapIcon = new ImageIcon(mapImage);

        for (int y = 0; y < mapSize; y++) {
            for (int x = 0; x < mapSize; x++) {
                mapLabels[y][x] = new JLabel();
                mapLabels[y][x].setSize(iconSize, iconSize);
                mapLabels[y][x].setLayout(new BorderLayout());
                mapLabels[y][x].setIcon(mapIcon);
                mapPanel.add(mapLabels[y][x]);
            }
        }

        frame.add(BorderLayout.WEST, mapPanel);

        frame.setBounds(50, 50, mapSize * (iconSize - 10) * 2, mapSize * iconSize);
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize(); //Set a window on center of screen
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);
        frame.setVisible(true);
    }

    public void setPlayerLabel() {
        BufferedImage bufferedPlayerImage = null;
        try {
            bufferedPlayerImage = ImageIO.read(new File("/Users/angrynimfa/projects/NoTouching/src/resources/img/" +
                    "mainCharacter/walk.gif"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        playerImage = bufferedPlayerImage.getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH);
        playerLabel = new JLabel(new ImageIcon(playerImage));
    }

}
