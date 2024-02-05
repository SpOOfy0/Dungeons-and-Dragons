package main;

import entity.NPC;
import entity.NPC_1;
import entity.Monsters.Monster;
import entity.Monsters.NormalMonsters.*;
import entity.Monsters.SpecialMonsters.Torero;
import object.OBJ_healPotion;
import object.OBJ_manaPotion;
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


    public int counter = 0;
    public int monsterNumber = 0;

    Monster monsterMatrix[] = new Monster[3];
    public void setMonsterMatrix() {
        monsterMatrix[0] = new Orc(gp, "down", 27, 25, false);
        monsterMatrix[1] = new RedOrc(gp, "left",  27, 25, false);
        monsterMatrix[2] = new BlueOrc(gp, "Right",  27, 25, false);
    }

    
    public void setMonsterWorld(int worldX, int worldY, Monster element){
        element.worldX = worldX;
        element.worldY = worldY;
    }
    
    
    public void MonsterSpawner(int worldX, int worldY, int numberOfMonsterToSpawn){
        if (numberOfMonsterToSpawn > monsterNumber) {
            
            if(counter == 180) {
                setMonsterWorld(worldX, worldY, monsterMatrix[0]);
                setMonster(monsterMatrix[0]);
                monsterNumber++;
            }
            else if (counter == 360) {
                setMonsterWorld(worldX, worldY, monsterMatrix[1]);
                setMonster(monsterMatrix[1]);
                monsterNumber++;
            }
            else if (counter == 540) {
                setMonsterWorld(worldX, worldY, monsterMatrix[2]);
                setMonster(monsterMatrix[2]);
                monsterNumber++;
                counter = 0;
            }
        }
        counter++;
    }


    public void setItems(){
        setItem(new OBJ_healPotion(gp, 26, 25));
        setItem(new OBJ_healPotion(gp, 26, 34));
        setItem(new OBJ_healPotion(gp, 35, 25));
        setItem(new OBJ_healPotion(gp, 35, 34));

        setItem(new OBJ_manaPotion(gp, 26, 27));
        setItem(new OBJ_manaPotion(gp, 26, 32));
        setItem(new OBJ_manaPotion(gp, 35, 27));
        setItem(new OBJ_manaPotion(gp, 35, 32));
    }

    public void setNPCs(){
        setNPC(new NPC_1(gp, "down", 28, 27));
    }

    public void setMonsters(){
        // upper row
        setMonster(new BlueOrc(gp, "right", 9, 8, true));
        setMonster(new Orc(gp, "down", 28, 8, true));
        setMonster(new Orc(gp, "up", 28, 8, true));
        setMonster(new RedOrc(gp, "left", 52, 8, true));

        // middle row
        setMonster(new Orc(gp, "up", 9, 32, true));
        setMonster(new Orc(gp, "down", 9, 32, true));
        setMonster(new Orc(gp, "up", 52, 32, true));
        setMonster(new Orc(gp, "down", 52, 32, true));

        // lower row
        setMonster(new RedOrc(gp, "right", 9, 51, true));
        setMonster(new Orc(gp, "up", 33, 51, true));
        setMonster(new Orc(gp, "down", 33, 51, true));
        setMonster(new BlueOrc(gp, "left", 52, 51, true));
    }

    public void setBoss(){
        setMonster(new Torero(gp, "down", 25, 24));
    }

}



