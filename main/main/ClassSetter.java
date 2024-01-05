package main;

import entity.NPC;
import entity.Monsters.Monster;
import object.OBJ_healPotion;


public class ClassSetter {
    
    GamePannel gp;

    public ClassSetter(GamePannel gp) {
        this.gp = gp;
        
    }

    public void setItems() {
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
        
    }

    public void setNPC(NPC element) {
        gp.npc.add(element);
    }

    public void setMonster(Monster element) {
        gp.monster.add(element);
    }
    
    /*public void spwanMonster(int worldX, int worldY){
        for(int i = 0; i < gp.monster.length; i++){
            if(gp.monster[i] != null){
                gp.monster[i] = new Orc(gp);
                gp.monster[i].worldX = worldX * gp.tileSize;
                gp.monster[i].worldY = worldY * gp.tileSize;
            }
        }
    }*/
}
