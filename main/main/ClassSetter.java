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
    int j = 0;

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

}
