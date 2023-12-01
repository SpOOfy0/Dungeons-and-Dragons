package main;

import entity.NPC_1;
import entity.Monsters.NormalMonsters.Orc;
import entity.Abilities.FireBall.FireBall;
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
    }

    /*public void setAbility() {
        gp.ability[0] = new FireBall(gp);
        gp.ability[0].worldX = gp.player.worldX + gp.tileSize;
        gp.ability[0].worldY = gp.player.worldY + gp.tileSize;
    }*/
}
