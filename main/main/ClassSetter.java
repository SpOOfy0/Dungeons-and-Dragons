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


    // c'est une bêtise, on doit laisser de la liberté sur les monstres qu'on peut spawn
    int counter = 0;
    int monsterIndex = 0;

    Monster monsterMatrix[][] = new Monster[20][3];
    public void setMonsterMatrix() {
        for(int i = 0; i < 20; i++){
            monsterMatrix[i][0] = new Orc(gp, "down", 27, 25);
            monsterMatrix[i][1] = new RedOrc(gp, "left",  27, 25);
            monsterMatrix[i][2] = new BlueOrc(gp, "Right",  27, 25);

        }
    }

    
    public void setMonsterWorld(int worldX, int worldY, Monster element){
        element.worldX = worldX;
        element.worldY = worldY;
    }
    
    
    public void MonsterSpawner(int worldX, int worldY ,int numberOfMonsterToSpawn){
        if (numberOfMonsterToSpawn / 3 > monsterIndex) {
            
            System.out.println(monsterIndex);
            if(counter == 30) {
                setMonsterWorld(worldX, worldY, monsterMatrix[monsterIndex][0]);
                setMonster(monsterMatrix[monsterIndex][0]);
            }
            else if (counter == 60) {
                setMonsterWorld(worldX, worldY, monsterMatrix[monsterIndex][1]);
                setMonster(monsterMatrix[monsterIndex][1]);
            }
            else if (counter == 90) {
                setMonsterWorld(worldX, worldY, monsterMatrix[monsterIndex][2]);
                setMonster(monsterMatrix[monsterIndex][2]);
                monsterIndex++;
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

        //setMonster(new Torero(gp, "down", 27, 25));
    }
}



