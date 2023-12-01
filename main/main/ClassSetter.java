package main;

import entity.NPC_1;
import entity.Monsters.NormalMonsters.Orc;
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

    public void setMonster() {
        gp.monster[0] = new Orc(gp);
        gp.monster[0].worldX = 14 * gp.tileSize;
        gp.monster[0].worldY = 20 * gp.tileSize;

        //More monsters
        
        /*gp.monster[1] = new Orc(gp);
        gp.monster[1].worldX = 14 * gp.tileSize;
        gp.monster[1].worldY = 23 * gp.tileSize;*/

        /*gp.monster[2] = new Orc(gp);
        gp.monster[2].worldX = 10 * gp.tileSize;
        gp.monster[2].worldY = 20 * gp.tileSize;*/

    }
}
