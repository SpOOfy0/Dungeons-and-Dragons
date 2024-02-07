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
    
    public void MonsterSpawner(int worldX, int worldY, int numberOfMonsterToSpawn){
        if (numberOfMonsterToSpawn > monsterNumber) {
            
            if(counter == 180) {
                setMonster(new BlueOrc(gp, "down", worldX, worldY, false, true));
                monsterNumber++;
            }
            else if (counter == 360) {
                setMonster(new Orc(gp, "down", worldX, worldY, false, true));
                monsterNumber++;
            }
            else if (counter == 540) {
                setMonster(new RedOrc(gp, "down", worldX, worldY, false, true));
                monsterNumber++;
                counter = 0;
            }
            counter++;
        }
    }


    public void setItems(){
        setItem(new OBJ_healPotion(gp, 27, 26));
        setItem(new OBJ_healPotion(gp, 27, 35));
        setItem(new OBJ_healPotion(gp, 36, 26));
        setItem(new OBJ_healPotion(gp, 36, 35));

        setItem(new OBJ_manaPotion(gp, 27, 28));
        setItem(new OBJ_manaPotion(gp, 27, 33));
        setItem(new OBJ_manaPotion(gp, 36, 28));
        setItem(new OBJ_manaPotion(gp, 36, 33));
    }

    public void secret(){
        setMonster(new Orc(gp, "down", 3, 1, false, false));
        gp.monster.get(gp.monster.size()-1).xp = 70000;
    }

    public void setNPCs(){
        setNPC(new NPC_1(gp, "down", 29, 28));
    }

    public void setMonsters(){
        // upper row
        setMonster(new BlueOrc(gp, "right", 10, 9, true));
        setMonster(new Orc(gp, "down", 29, 9, true));
        setMonster(new Orc(gp, "up", 29, 9, true));
        setMonster(new RedOrc(gp, "left", 53, 9, true));

        // middle row
        setMonster(new Orc(gp, "up", 10, 33, true));
        setMonster(new Orc(gp, "down", 10, 33, true));
        setMonster(new Orc(gp, "up", 53, 33, true));
        setMonster(new Orc(gp, "down", 53, 33, true));

        // lower row
        setMonster(new RedOrc(gp, "right", 10, 52, true));
        setMonster(new Orc(gp, "up", 34, 52, true));
        setMonster(new Orc(gp, "down", 34, 52, true));
        setMonster(new BlueOrc(gp, "left", 53, 52, true));
    }

    public void setBoss(){
        setMonster(new Torero(gp, "down", 26, 25));
    }

}



