package main;

import entity.NPC_1;
import object.OBJ_healPotion;

public class ClassSetter {
    
    GamePannel gp;

    public ClassSetter(GamePannel gp) {
        this.gp = gp;
        
    }

    public void setObject() {
        gp.obj[0] = new OBJ_healPotion(gp);
        gp.obj[0].worldX = 14 * gp.tileSize;
        gp.obj[0].worldY = 21 * gp.tileSize;

        gp.obj[1] = new OBJ_healPotion(gp);
        gp.obj[1].worldX = 14 * gp.tileSize;
        gp.obj[1].worldY = 35 * gp.tileSize;
    }

    public void setNPC() {
        gp.npc[0] = new NPC_1(gp);
        gp.npc[0].worldX = 14 * gp.tileSize;
        gp.npc[0].worldY = 25 * gp.tileSize;
    }
}
