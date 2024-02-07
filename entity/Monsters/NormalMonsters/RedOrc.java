package entity.Monsters.NormalMonsters;

import main.GamePannel;
import entity.Monsters.*;

public class RedOrc extends Monster implements MonsterInterface {
    
    public RedOrc (GamePannel gp, String inputedDirection, int coordX, int coordY, boolean willDropItem) {
        super(gp, inputedDirection, coordX, coordY);

        getThisMonsterImage();
        MonsterSetting(willDropItem);
    }

    public RedOrc (GamePannel gp, String inputedDirection, int coordX, int coordY, boolean willDropItem, boolean startAggro) {
        super(gp, inputedDirection, coordX, coordY);

        aggravated = startAggro;
        getThisMonsterImage();
        MonsterSetting(willDropItem);
    }

    @Override
    public void getThisMonsterImage() {
        up1 = getImage("/Player/Monster/RedOrc/up1.png");
        up2 = getImage("/Player/Monster/RedOrc/up2.png");
        down1 = getImage("/Player/Monster/RedOrc/down1.png");
        down2 = getImage("/Player/Monster/RedOrc/down2.png");
        left1 = getImage("/Player/Monster/RedOrc/left1.png");
        left2 = getImage("/Player/Monster/RedOrc/left2.png");
        right1 = getImage("/Player/Monster/RedOrc/right1.png");
        right2 = getImage("/Player/Monster/RedOrc/right2.png");
    }

    @Override
    public void MonsterSetting(boolean willDropItem) {

        noticeRange = 4;
        aggroRange = 9;
        initSpeed(1);
        maxLife = 34;
        life = maxLife;
        damage = 2;
        xp = 700;

        noKnockback = true;
        
        if (willDropItem){
            objToDrop.add("healPotion");
        }
    }

}
