package monster;

import com.company.GamePanel;
import entity.Entity;

import java.util.Random;

public class MON_GreenSlime extends Entity {

    public MON_GreenSlime(GamePanel gp) {
        super(gp);

        type = 2;
        name = "Green Slime";
        speed = 1;
        maxLife = 4;
        life = maxLife;

        solidArea.x = 3;
        solidArea.y = 18;
        solidArea.width = 42;
        solidArea.height = 30;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImage();
    }
    public void getImage() {
        up1 = setup("/monster/greenslime_down_1.png");
        up2 = setup("/monster/greenSlime_down_2.png");
        down1 = setup("/monster/greenslime_down_1.png");
        down2 = setup("/monster/greenSlime_down_2.png");
        left1 = setup("/monster/greenslime_down_1.png");
        left2 = setup("/monster/greenSlime_down_2.png");
        right1 = setup("/monster/greenslime_down_1.png");
        right2 = setup("/monster/greenSlime_down_2.png");
    }
    public void setAction() {

        actionLockCounter++;
        if(actionLockCounter == 120){
            Random random = new Random();
            int i = random.nextInt(100)+1; // pickup a number from 1 to 100

            if(i<=25){
                direction = "up";
            }
            if(i > 25 && i<= 50){
                direction = "down";
            }
            if(i>50 && i<= 75){
                direction = "left";
            }
            if (i>75 && i<=100){
                direction = "right";
            }
            actionLockCounter = 0;

        }
    }
}

