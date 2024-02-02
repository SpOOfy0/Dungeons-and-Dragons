package entity.Monsters.NormalMonsters;

import main.GamePannel;
import entity.Monsters.*;

public class BlueOrc extends Monster implements MonsterInterface {
    
    public BlueOrc (GamePannel gp, String inputedDirection, int coordX, int coordY) {
        super(gp, inputedDirection, coordX, coordY);

        getThisMonsterImage();
        MonsterSetting();
    }

    @Override
    public void getThisMonsterImage() {
        up1 = getImage("/Player/Monster/BlueOrc/up1.png");
        up2 = getImage("/Player/Monster/BlueOrc/up2.png");
        down1 = getImage("/Player/Monster/BlueOrc/down1.png");
        down2 = getImage("/Player/Monster/BlueOrc/down2.png");
        left1 = getImage("/Player/Monster/BlueOrc/left1.png");
        left2 = getImage("/Player/Monster/BlueOrc/left2.png");
        right1 = getImage("/Player/Monster/BlueOrc/right1.png");
        right2 = getImage("/Player/Monster/BlueOrc/right2.png");
    }

    @Override
    public void MonsterSetting() {

        noticeRange = 3;
        aggroRange = 7;
        initSpeed(2);
        maxLife = 11;
        life = maxLife;
        damage = 1;
        xp = 700;
        objToDrop[0] = "key";
        objToDrop[1] = "manaPotion";
    }

}
