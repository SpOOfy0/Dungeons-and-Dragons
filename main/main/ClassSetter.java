package main;

import entity.NPC_1;
import entity.Monsters.NormalMonsters.Orc;
import object.OBJ_healPotion;
import object.OBJ_manaPotion;


public class ClassSetter {
    
    GamePannel gp;
    int counter = 0;
    int s = 0;
    int index  = 3;

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

        gp.obj[2] = new OBJ_healPotion(gp);
        gp.obj[2].worldX = 14 * gp.tileSize;
        gp.obj[2].worldY = 36 * gp.tileSize;

        gp.obj[3] = new OBJ_healPotion(gp);
        gp.obj[3].worldX = 14 * gp.tileSize;
        gp.obj[3].worldY = 37 * gp.tileSize;

        gp.obj[4] = new OBJ_manaPotion(gp);
        gp.obj[4].worldX = 14 * gp.tileSize;
        gp.obj[4].worldY = 22 * gp.tileSize;

        gp.obj[5] = new OBJ_manaPotion(gp);
        gp.obj[5].worldX = 14 * gp.tileSize;
        gp.obj[5].worldY = 38 * gp.tileSize;

        gp.obj[6] = new OBJ_manaPotion(gp);
        gp.obj[6].worldX = 14 * gp.tileSize;
        gp.obj[6].worldY = 39 * gp.tileSize;
        
    }



    public void setNPC() {
        gp.npc[0] = new NPC_1(gp);
        gp.npc[0].worldX = 14 * gp.tileSize;
        gp.npc[0].worldY = 25 * gp.tileSize;
    }

    public void setMonster() {
        
        for (int i = 0; i < gp.monster.length; i++) {
            gp.monsters[i] = new Orc(gp);
        }

        gp.monster[0] = new Orc(gp);
        gp.monster[0].worldX = 14 * gp.tileSize;
        gp.monster[0].worldY = 20 * gp.tileSize;
        
        gp.monster[1] = new Orc(gp);
        gp.monster[1].worldX = 14 * gp.tileSize;
        gp.monster[1].worldY = 23 * gp.tileSize;

        gp.monster[2] = new Orc(gp);
        gp.monster[2].worldX = 10 * gp.tileSize;
        gp.monster[2].worldY = 20 * gp.tileSize;
    
    }

    public void MonsterSpawner(int worldX, int worldY){
        if (counter == 30 && index < gp.monster.length) {
            
            gp.monsters[index].worldX = worldX;
            gp.monsters[index].worldY = worldY;
            gp.monster[index] = gp.monsters[index];
            index++;
            counter = 0;
        
        }

        counter++;

    }
}
