package com.company;
import entity.Entity;
import entity.Player;
import object.SuperObject;
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


    // FPS
    int FPS = 60;

    TileManager tileM = new TileManager(this);


    public KeyHandler keyH = new KeyHandler(this);
    Sound music = new Sound();
    Sound se = new Sound();
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public UI ui = new UI(this);
    Thread gameThread;

    //entity player
    public Player player = new Player(this,keyH);
    // Set players default position

    // creating objects
    public SuperObject obj[] = new SuperObject[10];

    //NPC object
    public Entity npc[] = new Entity[10];

    //Game state
    public int playState;
    public int gameState=1;
    public int pauseState=2;
    public int dialogueState = 3;


    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHieght));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void setupGame(){

        aSetter.setObject();
        aSetter.setNPC();

        playMusic(0);
        stopMusic();
        gameState=playState;
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
        if (gameState==playState){
            // for player
            player.update();
            //for NPC
            for(int i = 0 ; i< npc.length;i++){
                if(npc[i] != null){
                    npc[i].update();
                }

            }
        }
        if (gameState==pauseState){
            //nothing
        }

    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        //debug
        long drawStart = 0;
        if(keyH.checkDrawTime == true){
            drawStart = System.nanoTime();
        }
        //tile
        tileM.draw(g2);

        //object
        for(int i = 0;i<obj.length;i++){
            if(obj[i] != null){
                obj[i].draw(g2,this);
            }
        }

        //player
        player.draw(g2);

        //NPC
        for(int i =0 ;i<npc.length ; i++){
            if(npc[i]!= null){
                npc[i].draw(g2 );
            }
        }

        //UI
        ui.draw(g2);

        //debug
        if(keyH.checkDrawTime == true){
            long drawEnd =  System.nanoTime();
            long passed = drawEnd - drawStart;
            g2.setColor(Color.white);
            g2.drawString("Draw Time: "+passed,10,400);
            System.out.println("Draw Time: "+passed);  
        }


        g2.dispose();
    }
    public void playMusic(int i) {

        music.setFile(i);
        music.play();
        music.loop();
    }
    public void stopMusic() {
        music.stop();
    }
    public void playSE(int i) {
            se.setFile(i);
            se.play();
        }

}
