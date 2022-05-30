package entity;

import com.company.GamePanel;
import com.company.KeyHandler;
import com.company.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity {


    KeyHandler keyH;

    public final int screenX;
    public final int screenY;
    public Player(GamePanel gp ,KeyHandler keyH){
        super(gp);

        this.keyH = keyH;

        screenX = gp.screenWidth/2 - (gp.tileSize/2);
        screenY = gp.screenHieght/2 - (gp.tileSize/2);

        solidArea = new Rectangle(8,16, 32,32);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.x = 8;
        solidArea.y= 16;
        solidArea.width = 32;
        solidArea.height = 32;

        attackArea.width = 36;
        attackArea.height = 36;


        setDefaultValues();
        getPlayerImage();
        getPlayerAttackImage();
    }
    public void getPlayerImage(){

        up1 = setup("/player/boy_up_1", gp.tileSize,gp.tileSize );
        up2 = setup("/player/boy_up_2", gp.tileSize,gp.tileSize);
        down1 = setup("/player/boy_down_1", gp.tileSize,gp.tileSize);
        down2 = setup("/player/boy_down_2", gp.tileSize,gp.tileSize);
        left1 = setup("/player/boy_left_1", gp.tileSize,gp.tileSize);
        left2 = setup("/player/boy_left_2", gp.tileSize,gp.tileSize);
        right1 = setup("/player/boy_right_1", gp.tileSize,gp.tileSize);
        right2 = setup("/player/boy_right_2", gp.tileSize,gp.tileSize);

    }
    public void getPlayerAttackImage(){
        attackUp1 = setup("/player/boy_attack_up_1", gp.tileSize,gp.tileSize*2);
        attackUp2 = setup("/player/boy_attack_up_2",gp.tileSize,gp.tileSize*2);
        attackDown1 = setup("/player/boy_attack_down_1",gp.tileSize,gp.tileSize*2);
        attackDown2 = setup("/player/boy_attack_down_2",gp.tileSize,gp.tileSize*2);
        attackLeft1 = setup("/player/boy_attack_left_1",gp.tileSize*2,gp.tileSize);
        attackLeft2 = setup("/player/boy_attack_left_2",gp.tileSize*2,gp.tileSize);
        attackRight1 = setup("/player/boy_attack_right_1",gp.tileSize*2,gp.tileSize);
        attackRight2 = setup("/player/boy_attack_right_2",gp.tileSize*2,gp.tileSize);
    }

    public void setDefaultValues(){
        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 21;
        //worldX = gp.tileSize * 10;
        //worldY = gp.tileSize * 13;
        speed = 5;
        direction = "down";

        //player status
        maxLife = 6;
        life = maxLife;
    }
    public void update(){
        if(attacking == true){
            attacking();

        }else if (keyH.rightPressed == true || keyH.leftPressed == true || keyH.upPressed == true || keyH.downPressed == true
        ||keyH.enterPressed == true){
            if (keyH.upPressed == true){
                direction = "up";
            }
            else if (keyH.downPressed == true){
                direction = "down";
            }else if(keyH.leftPressed == true){
                direction = "left";
            }else if(keyH.rightPressed == true){
                direction = "right";
            }
            // checks tile collision
            collisionOn = false;
            gp.cChecker.checkTile(this);

            //CHECK OBJECT COLLISION
            int objIndex = gp.cChecker.checkObject(this, true);
            pickUpObject(objIndex);

            //CHECK MONSTER COLLISION
            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            contactMonster(monsterIndex);

            //CHECK NPC COLLISION
            int npcIndex = gp.cChecker.checkEntity(this,gp.npc);
            interactNPC(npcIndex);

            //Check Event
            gp.eHandler.checkEvent();

            gp.keyH.enterPressed = false;

            //if collision is false player can move
            if(collisionOn == false && keyH.enterPressed == false){
                switch (direction){
                    case "up":worldY -= speed;break;
                    case "down":worldY += speed;break;
                    case "left":worldX -= speed;break;
                    case "right":worldX += speed;break;
                }
            }
            gp.keyH.enterPressed = false;
            spriteCounter++;
            if(spriteCounter > 13){
                if (spriteNum == 1){
                    spriteNum = 2;
                }else if ( spriteNum == 2){
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }


        }
        //This needs tobe outside of the key ifstatement!
        if(invincible == true) {
            invincibleCounter++;
            if(invincibleCounter > 60) {
                invincible = false;
                invincibleCounter = 0;
            }
        }
    }
    //attcking animations
    public void attacking(){
        spriteCounter++;
        if(spriteCounter <=5 ){
            spriteNum = 1;
        }
        if(spriteCounter > 5 && spriteCounter <= 25){
            spriteNum = 2;

            // Save the current worldX,worldY,solidArea
            int currentWorldX = worldX;
            int currentWorldY = worldY;
            int solidAreaWidth = solidArea.width;
            int solidAreaHeight = solidArea.height;

            //Adjust player's worldX/Y for the attackArea
            switch (direction){
                case "up": worldY -= attackArea.height;break;
                case "down": worldY += attackArea.height;break;
                case "left": worldX -= attackArea.width;break;
                case "right": worldX += attackArea.width;break;
            }
            // attackArea becomes solidArea
            solidArea.width = attackArea.width;
            solidArea.height = attackArea.height;
            // check monster collision with the weapon attackArea
            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            damageMonster(monsterIndex);
            //After checking collision,restore the original data
            worldX = currentWorldX;
            worldY = currentWorldY;
            solidArea.width = solidAreaWidth;
            solidArea.height =solidAreaHeight;

        }
        if(spriteCounter>25){
            spriteNum = 1;
            spriteCounter = 0;
            attacking = false;
        }
    }
    public void pickUpObject(int i){

        if(i != 999){

        }

    }
    public void interactNPC(int i){
        if(gp.keyH.enterPressed == true){
            if(i != 999){
                gp.gameState = gp.dialogueState;
                gp.npc[i].speak();
            }else{
                gp.playSE(7);
                attacking = true;

            }

        }
    }
    public void contactMonster(int i) {

        if(i != 999) {

            if(invincible == false) {
                gp.playSE(6);
                life -= 1;
                invincible = true;
            }

        }
    }
    public void  damageMonster(int i){
            if(i!=999){
                if(gp.monster[i].invincible == false){
                    gp.playSE(5);
                    gp.monster[i].life -=1;
                    gp.monster[i].invincible = true;
                    gp.monster[i].damageReaction();

                    if(gp.monster[i].life <= 0 ){
                        gp.monster[i].dying = true;
                    }
                }
            }

    }

    public void draw(Graphics2D g2){
        BufferedImage image = null;
        int tempScreenX = screenX;
        int tempScreenY = screenY;
        switch (direction){
            case "up":
                if(attacking == false){
                    if(spriteNum == 1){image = up1;}
                    if(spriteNum == 2){image =up2;}
                }
                if(attacking == true){
                    tempScreenY = screenY - gp.tileSize;
                    if(spriteNum == 1){image = attackUp1;}
                    if(spriteNum == 2){image =attackUp2;}
                }

                break;
            case "down":
                if(attacking == false){
                    if(spriteNum == 1){image = down1;}
                    if(spriteNum == 2){image =down2;}
                }
                if(attacking == true){
                    if(spriteNum == 1){image = attackDown1;}
                    if(spriteNum == 2){image =attackDown2;}
                }


                break;
            case "left":
                if(attacking == false){
                    if(spriteNum == 1){image = left1;}
                    if(spriteNum == 2){image = left2;}
                }
                if(attacking == true){
                    tempScreenX = screenX - gp.tileSize;
                    if(spriteNum == 1){image = attackLeft1;}
                    if(spriteNum == 2){image =attackLeft2;}
                }
                break;
            case "right":
                if(attacking == false){
                    if(spriteNum == 1){image = right1;}
                    if(spriteNum == 2){image = right2;}
                }
                if(attacking == true){
                    if(spriteNum == 1){image = attackRight1;}
                    if(spriteNum == 2){image = attackRight2;}
                }
                break;
        }
        if(invincible == true) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        }
        g2.drawImage(image, tempScreenX, tempScreenY, null);

        //Reset alpha
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

        //DEBUG
        //g2.setFont(new Font("Arial", Font.PLAIN, 26));
        //g2.setColor(Color.white);
        //g2.drawString("Invincible:"+invincibleCounter, 10, 400);
    }
}
