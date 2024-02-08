package entity.Monsters.SpecialMonsters;

import main.GamePannel;
import entity.Monsters.Monster;

public abstract class SpecialMonster extends Monster{
    
    public SpecialMonster (GamePannel gp, String inputedDirection, int coordX, int coordY) {
        super(gp, inputedDirection, coordX, coordY);
    }

    public void onDeath() {
        player.xp += xp;
        triggerEvent();
    }

    public void triggerEvent() {}

}
