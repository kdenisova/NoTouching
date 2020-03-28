package com.notouching.view;

import com.notouching.controller.GameEngine;
import com.notouching.controller.PlayerMove;
import com.notouching.model.People;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Playground implements KeyListener {
    private GameEngine game;
    private List<RenderedEntity> renderedEntities;
    private int mapSize;
    private int iconSize;
    private int y;
    private int x;
    private JFrame frame;
    private JLabel[][] mapLabels;
    private JLabel experienceLabel;
    private JLabel playerLabel;
    private Image playerImage;
    private JCheckBox[] groceryBox;
    private List<People> people;
    private JLabel levelLabel;
    private JLabel healthLabel;
    private JLabel sanitizerLabel;

    public Playground(GameEngine game, List<People> people, int mapSize, int y, int x) {
        this.game = game;
        this.people = people;
        this.mapSize = mapSize;
        this.iconSize = 50;
        this.y = y;
        this.x = x;
    }

    public void render() {
        frame = new JFrame("No Touching!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        renderedEntities = new ArrayList<>();

        GridLayout grid = new GridLayout(mapSize, mapSize);

        JPanel mapPanel = new JPanel(grid);

        mapLabels = new JLabel[mapSize][mapSize];
        BufferedImage bufferedMapImage = null;

        try {
            bufferedMapImage = ImageIO.read(getClass().getResource("/img/background/bg7.png"));
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

        setPlayerLabel();

        mapLabels[y][x].add(playerLabel);
        mapLabels[y][x].setFocusable(true);
        mapLabels[y][x].addKeyListener(this);
        renderPeople();
        renderFood();

        frame.add(BorderLayout.WEST, mapPanel);


        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Credentials"));

        Dimension labelSize = new Dimension(300, 300);
        Border solidBorder = BorderFactory.createLineBorder(Color.BLACK, 1);

        JLabel pictureLabel = new JLabel(new ImageIcon(playerImage));
        pictureLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        levelLabel = new JLabel("Level: " + game.getPlayer().getLevel());
        levelLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        experienceLabel = new JLabel("Experience: " + game.getPlayer().getExperience());
        experienceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        healthLabel = new JLabel("Health: " + game.getPlayer().getHealth());
        healthLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        sanitizerLabel = new JLabel("Amount of sanitizers: " + game.getPlayer().getSanitizer());
        sanitizerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        String listText = "<html><br/><br/>Find all product from the grocery list. <br/>" +
                "Caution, some products may be infected!</html><br>";


        JLabel groceryListLabel = new JLabel(listText);
        //Dimension labelSize = new Dimension(80, 80);

        groceryListLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        groceryListLabel.setHorizontalAlignment(JLabel.CENTER);
        groceryListLabel.setPreferredSize(labelSize);

        //Border solidBorder = BorderFactory.createBevelBorder(1,Color.blue, Color.blue);
        groceryListLabel.setBorder(solidBorder);

        Font font = new Font("Verdana", Font.PLAIN, 15);
        groceryListLabel.setFont(font);

        //nameLabel.setFont(new Font("Serif", Font.BOLD, 22));
        infoPanel.add(pictureLabel);
        infoPanel.add(experienceLabel);
        infoPanel.add(healthLabel);
        infoPanel.add(sanitizerLabel);
        infoPanel.add(groceryListLabel);

        groceryBox = new JCheckBox[game.getGrocery().size()];

        for (int i = 0; i < game.getGrocery().size(); i++) {
            groceryBox[i] = new JCheckBox(String.valueOf(game.getGrocery().get(i).getType()));
            groceryBox[i].setEnabled(false);
            infoPanel.add(groceryBox[i]);
        }

        frame.add(BorderLayout.CENTER, infoPanel);

        frame.setBounds(50, 50, mapSize * (iconSize - 10) * 2, mapSize * iconSize);
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize(); //Set a window on center of screen
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);
        frame.setVisible(true);
    }

    public void updateFood(int experience, int y, int x) {
        experienceLabel.setText("Experience: " + experience);

        for (int i = 0; i < renderedEntities.size(); i++) {
            if (y == renderedEntities.get(i).getEntity().getY() && x == renderedEntities.get(i).getEntity().getX()) {
                mapLabels[y][x].remove(renderedEntities.get(i).getLabel());
                renderedEntities.remove(i);
                break;
            }
        }

        frame.revalidate();
        frame.repaint();

        mapLabels[y][x].revalidate();
        mapLabels[y][x].repaint();
    }

    public void updateGroceryList(int i) {
        groceryBox[i].setSelected(true);
    }

    public void updateHealth(int health) {
        healthLabel.setText("Health:  " + health);
    }

    public void updateSanitizer(int sanitizer) {
        sanitizerLabel.setText("Amount of sanitizers:  " + sanitizer);
    }

    public void setPlayerLabel() {
        BufferedImage bufferedPlayerImage = null;
        try {
            bufferedPlayerImage = ImageIO.read(getClass().getResource("/img/mainCharacter/walk.gif"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        playerImage = bufferedPlayerImage.getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH);
        playerLabel = new JLabel(new ImageIcon(playerImage));
    }

    public void renderPlayer(int oldY, int oldX, int newY, int newX) {
        mapLabels[oldY][oldX].remove(playerLabel);

        frame.revalidate();
        frame.repaint();

        mapLabels[oldY][oldX].revalidate();
        mapLabels[oldY][oldX].repaint();

        playerLabel = new JLabel(new ImageIcon(playerImage));
        mapLabels[newY][newX].add(playerLabel);
    }

    public void renderPeople() {
        BufferedImage bufferedImage = null;
        Image image;
        JLabel label;

        for (int i = 0; i < people.size(); i++) {

            try {
                bufferedImage = ImageIO.read(getClass().getResource("/img/characters/" + (i % 6) + ".gif"));
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            image = bufferedImage.getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH);
            label = new JLabel(new ImageIcon(image));

            mapLabels[game.getPeople().get(i).getY()][game.getPeople().get(i).getX()].add(label);
            renderedEntities.add(new RenderedEntity(label, game.getPeople().get(i)));
        }
    }

    public void renderFood() {
        BufferedImage bufferedImage = null;
        Image image;
        JLabel label;

        for (int i = 0; i < game.getFood().size(); i++) {

            try {
                bufferedImage = ImageIO.read(getClass().getResource("/img/food/" + game.getFood().get(i).getType() + ".png"));
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            image = bufferedImage.getScaledInstance(iconSize - 15, iconSize - 15, Image.SCALE_SMOOTH);
            label = new JLabel(new ImageIcon(image));

            mapLabels[game.getFood().get(i).getY()][game.getFood().get(i).getX()].add(label);
            renderedEntities.add(new RenderedEntity(label, game.getFood().get(i)));
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            game.playerMoved(PlayerMove.DOWN);
        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
            game.playerMoved(PlayerMove.UP);
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            game.playerMoved(PlayerMove.LEFT);
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            game.playerMoved(PlayerMove.RIGHT);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
