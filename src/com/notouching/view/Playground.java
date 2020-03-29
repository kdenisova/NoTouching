package com.notouching.view;

import com.notouching.controller.GameEngine;
import com.notouching.controller.PlayerMove;
import com.notouching.model.People;
import com.notouching.model.VirusType;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

import static javax.swing.JOptionPane.PLAIN_MESSAGE;
import static javax.swing.JOptionPane.WARNING_MESSAGE;

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
    private JLabel[] virusesLabel;

    private int groceryItem = 0;

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

        //gameMessage(2);

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
        renderSanitizers();

        frame.add(BorderLayout.WEST, mapPanel);


        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Credentials"));

        Dimension labelSize = new Dimension(300, 300);

        //Border solidBorder = BorderFactory.createLineBorder(Color.BLACK, 1);

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

        JPanel rulesPanel = new JPanel();
        rulesPanel.setBorder(BorderFactory.createTitledBorder("Rules"));

        String listText = "<html>Find all product from the grocery list. <br/>" +
                "Caution, some products may be infected!</html><br>";

        //Border solidBorder = BorderFactory.createRaisedSoftBevelBorder();
        JLabel rulesLabel = new JLabel(listText);
        rulesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        rulesLabel.setHorizontalAlignment(JLabel.CENTER);
        rulesLabel.setVerticalAlignment(JLabel.CENTER);
        //rulesLabel.setPreferredSize(labelSize);


        //rulesLabel.setBorder(solidBorder);

        Font font = new Font("Verdana", Font.PLAIN, 15);
        rulesLabel.setFont(font);
        rulesPanel.add(rulesLabel);
        infoPanel.add(pictureLabel);
        infoPanel.add(experienceLabel);
        infoPanel.add(healthLabel);
        infoPanel.add(sanitizerLabel);
        infoPanel.add(rulesPanel);

        JPanel groceryPanel = new JPanel();
        groceryPanel.setBorder(BorderFactory.createTitledBorder("Grocery List"));

        groceryBox = new JCheckBox[game.getGrocery().size()];

        for (int i = 0; i < game.getGrocery().size(); i++) {
            groceryBox[i] = new JCheckBox(String.valueOf(game.getGrocery().get(i).getType()));
            groceryBox[i].setEnabled(false);
            groceryBox[i].setFont(font);
            groceryPanel.add(groceryBox[i]);
        }

        infoPanel.add(groceryPanel);

        JPanel virusPanel = new JPanel(new GridLayout());
        virusPanel.setBorder(BorderFactory.createTitledBorder("Virus Collections"));

        virusesLabel = new JLabel[5];

        for (int i = 0; i < 5; i++) {
            virusesLabel[i] = new JLabel();
            virusesLabel[i].setIcon(null);
            virusPanel.add(virusesLabel[i]);
        }

        infoPanel.add(virusPanel);
        frame.add(BorderLayout.CENTER, infoPanel);


        frame.setBounds(50, 50, mapSize * (iconSize - 10) * 2, mapSize * iconSize);
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize(); //Set a window on center of screen
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);
        frame.setVisible(true);
    }

    public void removeEntity(int y, int x) {
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

    public void renderEntity(int oldY, int oldX, int newY, int newX) {
        for (int i = 0; i < renderedEntities.size(); i++) {
            if (oldY == renderedEntities.get(i).getEntity().getY() && oldX == renderedEntities.get(i).getEntity().getX()) {
                mapLabels[oldY][oldX].remove(renderedEntities.get(i).getLabel());

                frame.revalidate();
                frame.repaint();

                mapLabels[oldY][oldX].revalidate();
                mapLabels[oldY][oldX].repaint();

                renderedEntities.get(i).setLabel(new JLabel(new ImageIcon(renderedEntities.get(i).getImage())));
                mapLabels[newY][newX].add(renderedEntities.get(i).getLabel());

                break;
            }
        }
    }

    public void updateFood(int experience) {
        experienceLabel.setText("Experience: " + experience);
    }

    public void updateGroceryList(int i) {
        if (!groceryBox[i].isSelected()) {
            groceryBox[i].setSelected(true);
            groceryItem += 1;
            if (groceryItem == game.getGrocery().size()) {
                game.setStatus(false);
                gameMessage(1);
            }
        }
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
            bufferedPlayerImage = ImageIO.read(getClass().getResource("/img/mainCharacter/walk.png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        playerImage = bufferedPlayerImage.getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH);
        playerLabel = new JLabel(new ImageIcon(playerImage));
    }

    public void gameMessage(int flag) {

        if (flag == 1) {
            JOptionPane.showInternalMessageDialog(null, "You win! Level UP", "WIN", PLAIN_MESSAGE);
        } else if (flag == 2) {
            JOptionPane.showMessageDialog(null, "GAME OVER", "Oh no!", WARNING_MESSAGE);
        }
        else
            JOptionPane.showMessageDialog(null, "You have compiled the entire collection of viruses. You lose!",
                    "Oops", WARNING_MESSAGE);
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
                bufferedImage = ImageIO.read(getClass().getResource("/img/characters/" + (i % 6) + ".png"));
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            image = bufferedImage.getScaledInstance(iconSize - 10, iconSize - 10, Image.SCALE_SMOOTH);
            label = new JLabel(new ImageIcon(image));

            mapLabels[game.getPeople().get(i).getY()][game.getPeople().get(i).getX()].add(label);
            renderedEntities.add(new RenderedEntity(label, image, game.getPeople().get(i)));
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
            renderedEntities.add(new RenderedEntity(label, image, game.getFood().get(i)));
        }
    }

    public void renderSanitizers() {
        BufferedImage bufferedImage = null;
        Image image;
        JLabel label;

        for (int i = 0; i < game.getSanitizers().size(); i++) {

            try {
                bufferedImage = ImageIO.read(getClass().getResource("/img/sanitizers/" + (i % 6) + ".png"));
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            image = bufferedImage.getScaledInstance(iconSize - 15, iconSize - 15, Image.SCALE_SMOOTH);
            label = new JLabel(new ImageIcon(image));

            mapLabels[game.getSanitizers().get(i).getY()][game.getSanitizers().get(i).getX()].add(label);
            renderedEntities.add(new RenderedEntity(label, image, game.getSanitizers().get(i)));
        }
    }

    public void renderVirus(VirusType type, int i) {
        BufferedImage bufferedImage = null;

        try {
            bufferedImage = ImageIO.read(getClass().getResource("/img/viruses/" + type + ".png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        Image virusImage = bufferedImage.getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH);
        ImageIcon virusIcon = new ImageIcon(virusImage);
        virusesLabel[i].setIcon(virusIcon);

        if (i == 3) {
            gameMessage(3);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (game.isStatus()) {
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
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
