package main;

import entity.NPC;
import entity.Monsters.Monster;
import object.SuperObject;


public class ClassSetter {
    
    GamePannel gp;

    //CodeMonsterSpawner
    // int counter = 0;
    // int s = 0;
    // int index = 3;

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
    
    //CodeMonsterSpawner
    // public void MonsterSpawner(int worldX, int worldY){
    //     if (counter == 30 && index < gp.monster.length) {
    //         gp.monsters[index].worldX = worldX;
    //         gp.monsters[index].worldY = worldY;
    //         gp.monster[index] = gp.monsters[index];
    //         index++;
    //         counter = 0;
    //     }
    //     counter++;
    // }

}
