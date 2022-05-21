package com.company;
import entity.Player;
import tile.TileManager;

import javax.swing.JPanel;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {
    //  SCREEN SETTING
    final int originalTileSize =16; // 16X16 tile
    final int scale = 3;
    public  int tileSize = originalTileSize * scale; // 48X48 tile
    public  int maxScreenCol = 16;
    public  int maxScreenRow = 12;
    public  int screenWidth = tileSize*maxScreenCol; //768 pixel
    public  int screenHieght = tileSize*maxScreenRow;

    //WORLD SETTINGS
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;

    // FPS
    int FPS = 60;

    TileManager tileM = new TileManager(this);


    KeyHandler keyH = new KeyHandler(this);
    Thread gameThread;

    //entity player
    public Player player = new Player(this,keyH);
    // Set players default position

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHieght));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }
    //For zooming in and out

    public void zoominout(int i){
        int oldWorldWidth = tileSize+maxWorldCol;

        tileSize=tileSize+i;

        int newWorldWidth = tileSize+maxWorldCol;

        player.speed=(double)newWorldWidth/600;

        double multiplier = (double)newWorldWidth/oldWorldWidth;

        System.out.println("tileSize: "+tileSize);
        System.out.println("worldWidth: "+newWorldWidth);
        System.out.println("player worldx: "+ player.worldX);

        double newplayerWorldX = player.worldX + multiplier;
        double newplayerWorldY = player.worldY + multiplier;


    }
    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        // delta method to add restriction to allow the movement to be smooth
        double drawInterval = 1000000000 /FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        // diplays fps
        long timer = 0;
        int drawCount = 0;
        while (gameThread != null){
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) /drawInterval;
            timer += (currentTime-lastTime);
            lastTime = currentTime;
            if(delta >=1){

                // 1. UPDATE:  update information such as character position
                update();
                // 2. DRAW: draw the screen with the updated information
                repaint();
                delta--;
                drawCount++;
            }
            if (timer >= 1000000000){
                System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }



        }

    }
    public void update(){
        player.update();

    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        tileM.draw(g2);
        player.draw(g2);

        g2.dispose();
    }
}
