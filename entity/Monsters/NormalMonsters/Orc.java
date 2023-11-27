package entity.Monsters.NormalMonsters;

import java.util.Random;
import java.lang.Math;

import main.GamePannel;
import entity.Player;
import entity.Monsters.Monster;
import entity.Monsters.MonsterInterface;

public class Orc extends Monster implements MonsterInterface {
    
    public Orc (GamePannel gp) {
        super(gp);

        direction = "up";
        speed = 1;

        getTihsMonsterImage();
        MonsterSetting();
    
        
    }

    @Override
    public void getTihsMonsterImage() {
        up1 = getImage("/Player/Monster/Orc/up1.png");
        up2 = getImage("/Player/Monster/Orc/up2.png");
        down1 = getImage("/Player/Monster/Orc/down1.png");
        down2 = getImage("/Player/Monster/Orc/down2.png");
        left1 = getImage("/Player/Monster/Orc/left1.png");
        left2 = getImage("/Player/Monster/Orc/left2.png");
        right1 = getImage("/Player/Monster/Orc/right1.png");
        right2 = getImage("/Player/Monster/Orc/right2.png");
    }

    @Override
    public void MonsterSetting() {

        direction = "up";
        speed = 1;
        life = 4;
        dommage = 1;
        
    }

   




}
