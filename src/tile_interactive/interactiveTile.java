package tile_interactive;

import entity.Entity;
import com.company.GamePanel;

public class interactiveTile extends Entity{

    GamePanel gp;
    public boolean destructible = false;

    public interactiveTile(GamePanel gp, int col, int row) {
        super(gp);
        this.gp = gp;
    }
    public boolean isCorrectItem(Entity entity) {
        boolean isCorrectItem = false;
        if(entity.currentWeapon.type == type_axe) {
            isCorrectItem = true;
        }
        return isCorrectItem;
    }
    public void playSE() {}
    public interactiveTile getDestroyedForm() {
        interactiveTile tile = null;
        return tile;
    }
    public void update() {

    }
}
