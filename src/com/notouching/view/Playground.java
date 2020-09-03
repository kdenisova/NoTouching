package com.notouching.view;

import com.notouching.controller.GameEngine;
import com.notouching.controller.PlayerMove;
import com.notouching.model.VirusType;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

import static javax.swing.JOptionPane.*;

public class Playground implements KeyListener {
    private GameEngine game;
    private final int mapSize;
    private final int iconSize;
    private List<RenderedEntity> renderedEntities;
    private JFrame frame;
    private JLabel[][] mapLabels;
    private JLabel scoreLabel;
    private JLabel playerLabel;
    private Image playerImage;
    private JCheckBox[] groceryBox;
    private JLabel healthLabel;
    private JLabel sanitizerLabel;
    private JLabel[] virusesLabel;


    public Playground(GameEngine game, int mapSize) {
        this.game = game;
        this.mapSize = mapSize;
        this.iconSize = 50;
    }

    public void render() {
        frame = new JFrame("No Touching!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        renderedEntities = new ArrayList<>();

        //render map
        GridLayout grid = new GridLayout(mapSize, mapSize);

        JPanel mapPanel = new JPanel(grid);

        mapLabels = new JLabel[mapSize][mapSize];
        BufferedImage bufferedImage = null;

        try {
            bufferedImage = ImageIO.read(getClass().getResource("/img/background/" + game.randomGenerator(3) + ".png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        assert bufferedImage != null;
        Image image = bufferedImage.getScaledInstance(iconSize, iconSize + 10, Image.SCALE_SMOOTH);
        ImageIcon icon = new ImageIcon(image);

        for (int y = 0; y < mapSize; y++) {
            for (int x = 0; x < mapSize; x++) {
                mapLabels[y][x] = new JLabel();
                mapLabels[y][x].setSize(iconSize, iconSize);
                mapLabels[y][x].setLayout(new BorderLayout());
                mapLabels[y][x].setIcon(icon);
                mapPanel.add(mapLabels[y][x]);
            }
        }

        //render player
        setPlayerLabel();
        mapLabels[game.getPlayer().getY()][game.getPlayer().getX()].add(playerLabel);
        mapLabels[game.getPlayer().getY()][game.getPlayer().getX()].setFocusable(true);
        mapLabels[game.getPlayer().getY()][game.getPlayer().getX()].addKeyListener(this);

        //render game entities
        renderPeople();
        renderFood();
        renderSanitizers();

        //render panels
        frame.add(BorderLayout.WEST, mapPanel);

        //player info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Credentials"));

        Font font = new Font("Verdana", Font.PLAIN, 15);

        JLabel pictureLabel = new JLabel(new ImageIcon(playerImage));
        pictureLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel levelLabel = new JLabel("Level: " + game.getPlayer().getLevel());
        levelLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        levelLabel.setFont(font);

        scoreLabel = new JLabel("Score: " + game.getPlayer().getScore());
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        scoreLabel.setFont(font);

        healthLabel = new JLabel("Health: " + game.getPlayer().getHealth());
        healthLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        healthLabel.setFont(font);

        sanitizerLabel = new JLabel("Amount of sanitizers: " + game.getPlayer().getSanitizer());
        sanitizerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        sanitizerLabel.setFont(font);

        infoPanel.add(pictureLabel);
        infoPanel.add(levelLabel);
        infoPanel.add(scoreLabel);
        infoPanel.add(healthLabel);
        infoPanel.add(sanitizerLabel);

        //rules
        JPanel rulesPanel = new JPanel();
        rulesPanel.setLayout(new GridLayout(4, 1));
        rulesPanel.setBorder(BorderFactory.createTitledBorder("Rules"));

        JLabel rule1 = new JLabel("Find all product from the grocery list.", JLabel.CENTER);
        rule1.setFont(font);
        JLabel rule2 = new JLabel("Collect sanitizers to prevent infection", JLabel.CENTER);
        rule2.setFont(font);
        JLabel rule3 = new JLabel("And remember...", JLabel.CENTER);
        rule3.setFont(font);
        JLabel rule4 = new JLabel("NO TOUCHING!", JLabel.CENTER);
        rule4.setFont(font);

        rulesPanel.add(rule1);
        rulesPanel.add(rule2);
        rulesPanel.add(rule3);
        rulesPanel.add(rule4);

        infoPanel.add(rulesPanel);

        //grocery list
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

        //viruses
        JPanel virusPanel = new JPanel(new GridLayout());
        virusPanel.setBorder(BorderFactory.createTitledBorder("Virus Collection"));

        virusesLabel = new JLabel[5];

        try {
            bufferedImage = ImageIO.read(getClass().getResource("/img/viruses/EMPTY.png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        image = bufferedImage.getScaledInstance(iconSize, iconSize + 10, Image.SCALE_SMOOTH);

        for (int i = 0; i < 5; i++) {
            virusesLabel[i] = new JLabel();
            virusesLabel[i].setIcon(new ImageIcon(image));
            virusPanel.add(virusesLabel[i]);
        }

        infoPanel.add(virusPanel);
        frame.add(BorderLayout.CENTER, infoPanel);

        //frame settings
        frame.setBounds(50, 50, mapSize * (iconSize - 10) * 2, mapSize * iconSize);
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize(); //Set a window on center of screen
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);
        frame.setVisible(true);
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

        for (int i = 0; i < game.getPeople().size(); i++) {

            try {
                bufferedImage = ImageIO.read(getClass().getResource("/img/people/" + (i % 6) + ".png"));
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            assert bufferedImage != null;
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

            assert bufferedImage != null;
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

            assert bufferedImage != null;
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

        assert bufferedImage != null;
        Image virusImage = bufferedImage.getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH);
        ImageIcon virusIcon = new ImageIcon(virusImage);
        virusesLabel[i].setIcon(virusIcon);

        if (i == 3 && game.isStatus()) {
            game.setStatus(false);
            gameMessage(3);
        }
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

    public void movePeople(int oldY, int oldX, int newY, int newX) {
        for (RenderedEntity renderedEntity : renderedEntities) {
            if (oldY == renderedEntity.getEntity().getY() && oldX == renderedEntity.getEntity().getX()) {
                mapLabels[oldY][oldX].remove(renderedEntity.getLabel());

                frame.revalidate();
                frame.repaint();

                mapLabels[oldY][oldX].revalidate();
                mapLabels[oldY][oldX].repaint();

                renderedEntity.setLabel(new JLabel(new ImageIcon(renderedEntity.getImage())));
                mapLabels[newY][newX].add(renderedEntity.getLabel());

                break;
            }
        }
    }

    public void updateScore(int score) {
        scoreLabel.setText("Score: " + score);
    }

    public void updateGroceryList(int i) {
        if (!groceryBox[i].isSelected()) {
            groceryBox[i].setSelected(true);
            game.setFoundItems(game.getFoundItems() + 1);
        }
    }

    public void updateHealth(int health) {
        healthLabel.setText("Health:  " + health);
    }

    public void updateSanitizer(int sanitizer) {
        sanitizerLabel.setText("Amount of sanitizers: " + sanitizer);
    }

    public void setPlayerLabel() {
        BufferedImage bufferedPlayerImage = null;
        try {
            bufferedPlayerImage = ImageIO.read(getClass().getResource("/img/player/player.png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        assert bufferedPlayerImage != null;
        playerImage = bufferedPlayerImage.getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH);
        playerLabel = new JLabel(new ImageIcon(playerImage));
    }

    public void gameMessage(int flag) {
        ImageIcon icon1 = new ImageIcon(getClass().getResource("/img/background/virus.png"));
        ImageIcon icon2 = new ImageIcon(getClass().getResource("/img/background/paper.png"));
        ImageIcon icon3 = new ImageIcon(getClass().getResource("/img/player/player80.png"));

        if (flag == 1) {
            JOptionPane.showInternalMessageDialog(null,
                    "Level " + (game.getPlayer().getLevel() + 1),
                    "Level Up", PLAIN_MESSAGE, icon2);

            renderedEntities.clear();
            frame.dispose();
        } else if (flag == 2) {
            JOptionPane.showMessageDialog(null,
                    "Too many touching!",
                    "GAME OVER", JOptionPane.PLAIN_MESSAGE, icon1);
        } else if (flag == 3) {
            JOptionPane.showMessageDialog(null,
                    "Viruses win this round!",
                    "GAME OVER: Virus collection is full", INFORMATION_MESSAGE, icon1);
        } else
            JOptionPane.showMessageDialog(null,
                    "You win and became a symbol of quarantine!",
                    "Good job!", JOptionPane.PLAIN_MESSAGE, icon3);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (game.isStatus()) {
            if (game.getSkipCount() > 0)
                game.setSkipCount(0);
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
