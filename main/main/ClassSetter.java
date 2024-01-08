package main;

import entity.NPC;
import entity.Monsters.Monster;
import object.SuperObject;


public class ClassSetter {
    
    GamePannel gp;

    public ClassSetter(GamePannel gp) {
        this.gp = gp;
    }

    public void setItem(SuperObject element) {
        gp.item.add(element);
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
