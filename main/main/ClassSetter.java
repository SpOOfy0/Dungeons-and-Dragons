package main;

import object.SuperObject;
import object.OBJ_healPotion;
import object.OBJ_manaPotion;
import entity.NPC;
import entity.NPC_1;
import entity.Monsters.Monster;
import entity.Monsters.NormalMonsters.*;


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


    public void setMonsterToSpown(Monster element) {
        gp.monsterToSpown.add(element);
    }
    
    int counter = 0;
    int index = 3;
    int j = 0;
    
    public void MonsterSpawner(int worldX, int worldY ,int numberOfMonsterToSpawn ,String typeOfMonster){
        if (counter == 30 &&  numberOfMonsterToSpawn > gp.monsterToSpown.size()) {
            j++;
            System.out.println("j = " + j);
            switch (typeOfMonster) {
                case "Orc":
                    setMonsterToSpown(new Orc(gp, typeOfMonster, worldX, worldY));
                    break;
                case "BlueOrc":
                    setMonsterToSpown(new BlueOrc(gp, typeOfMonster, worldX, worldY));
                    break;
                case "RedOrc":
                    setMonsterToSpown(new RedOrc(gp, typeOfMonster, worldX, worldY));
                    break;
                
            }
            counter = 0;
        }
        counter++;
    }


    // Functions to setup in setUpObject()
    public void setItems() {
        setItem(new OBJ_healPotion(gp, 26, 25));
        setItem(new OBJ_healPotion(gp, 26, 34));
        setItem(new OBJ_healPotion(gp, 35, 25));
        setItem(new OBJ_healPotion(gp, 35, 34));
        
        setItem(new OBJ_manaPotion(gp, 30, 8));
        setItem(new OBJ_manaPotion(gp, 10, 30));
        setItem(new OBJ_manaPotion(gp, 51, 31));
        setItem(new OBJ_manaPotion(gp, 31, 51));
    }

    public void setNPCs() {
        setNPC(new NPC_1(gp, "down", 28, 27));
    }

    public void setMonsters() {
        // upper row
        setMonster(new BlueOrc(gp, "right", 9, 8));
        setMonster(new Orc(gp, "down", 28, 8));
        setMonster(new Orc(gp, "up", 28, 8));
        setMonster(new RedOrc(gp, "left", 52, 8));

        // middle row
        setMonster(new Orc(gp, "up", 9, 32));
        setMonster(new Orc(gp, "down", 9, 32));
        setMonster(new Orc(gp, "up", 52, 32));
        setMonster(new Orc(gp, "down", 52, 32));

        // lower row
        setMonster(new RedOrc(gp, "right", 9, 51));
        setMonster(new Orc(gp, "up", 33, 51));
        setMonster(new Orc(gp, "down", 33, 51));
        setMonster(new BlueOrc(gp, "left", 52, 51));
    }

}
