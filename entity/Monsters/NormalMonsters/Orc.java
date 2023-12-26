package entity.Monsters.NormalMonsters;



import main.GamePannel;
import entity.Monsters.Monster;
import entity.Monsters.MonsterInterface;

public class Orc extends Monster implements MonsterInterface {
    
    public Orc (GamePannel gp) {
        super(gp);

        getThisMonsterImage();
        MonsterSetting();
   
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
    public void MonsterSetting() {

        direction[0] = "up";
        direction[1] = null;
        speed = 1;
        maxLife = 4;
        life = maxLife;
        dommage = 1;
        
    }

}
