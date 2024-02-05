package entity.Monsters.NormalMonsters;

import main.GamePannel;
import entity.Monsters.*;

public class Orc extends Monster implements MonsterInterface {
    
    public Orc (GamePannel gp, String inputedDirection, int coordX, int coordY, boolean willDropItem) {
        super(gp, inputedDirection, coordX, coordY);

        getThisMonsterImage();
        MonsterSetting(willDropItem);
    }

    @Override
    public void getThisMonsterImage() {
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
    public void MonsterSetting(boolean willDropItem) {

        noticeRange = 3;
        aggroRange = 5;
        initSpeed(1);
        maxLife = 16;
        life = maxLife;
        damage = 1;
        xp = 700;
        
        if (willDropItem){
            objToDrop.add("key");
        }
    }

}
