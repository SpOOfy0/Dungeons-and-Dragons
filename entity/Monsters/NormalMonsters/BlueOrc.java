package entity.Monsters.NormalMonsters;

import main.GamePannel;
import entity.Monsters.*;

public class BlueOrc extends Monster implements MonsterInterface {
    
    public BlueOrc (GamePannel gp, String inputedDirection, int coordX, int coordY, boolean willDropItem) {
        super(gp, inputedDirection, coordX, coordY);

        getThisMonsterImage();
        MonsterSetting(willDropItem);
    }

    public BlueOrc (GamePannel gp, String inputedDirection, int coordX, int coordY, boolean willDropItem, boolean startAggro) {
        super(gp, inputedDirection, coordX, coordY);

        aggravated = startAggro;
        getThisMonsterImage();
        MonsterSetting(willDropItem);
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
    public void MonsterSetting(boolean willDropItem) {

        noticeRange = 3;
        aggroRange = 7;
        initSpeed(2);
        maxLife = 22;
        life = maxLife;
        damage = 1;
        xp = 700;
        
        if (willDropItem){
            objToDrop.add("manaPotion");
        }
    }

}
