package object;

import com.company.GamePanel;
import entity.Entity;

import java.io.IOException;

public class OBJ_Boots extends Entity {

    public OBJ_Boots(GamePanel gp){
        super(gp);

        name = "Boots";
        down1 = setup("/objects/boots");

    }

}
