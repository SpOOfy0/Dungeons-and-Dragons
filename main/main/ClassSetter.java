package main;

import entity.NPC;
import entity.Monsters.Monster;
import entity.Monsters.NormalMonsters.BlueOrc;
import object.SuperObject;
import entity.Monsters.NormalMonsters.Orc;
import entity.Monsters.NormalMonsters.RedOrc;


public class ClassSetter {
    
    GamePannel gp;

    int counter = 0;
    int index = 3;
    int monsterIndex = 0;
    Monster monsterMatrix[][] = new Monster[35][3];
    
    Orc orc;

    
    
    public ClassSetter(GamePannel gp) {
        this.gp = gp;
    }

    public void setMonsterMatrix() {
        for(int i = 0; i < 20; i++){
            monsterMatrix[i][0] = new Orc(gp, "down", 27, 25);
            monsterMatrix[i][1] = new RedOrc(gp, "left",  27, 25);
            monsterMatrix[i][2] = new BlueOrc(gp, "Right",  27, 25);

        }
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
}


