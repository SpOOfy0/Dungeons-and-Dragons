package entity.Abilities.FireBall;

import entity.Abilities.Ability;
import entity.Abilities.AbilityInterface;
import main.GamePannel;

public class FireBall extends Ability implements AbilityInterface {

    public FireBall(GamePannel gp) {
        super(gp);
        getThisAbilityImage();
        AbilitySetting();
        
    }

   

    @Override
    public void getThisAbilityImage() {
        up1 = getImage("/Player/Ability/FireBall/Up.png");
        down1 = getImage("/Player/Ability/FireBall/Down.png");
        left1 = getImage("/Player/Ability/FireBall/Left.png");
        right1 = getImage("/Player/Ability/FireBall/Right.png");
        
    }

    @Override
    public void AbilitySetting() {
        dmg = 1;
        speed = 4;
        range = 8;  
        worldX = gp.player.worldX ;
        worldY = gp.player.worldY ;
        direction[0] = gp.player.facing;
        
        
    }
        

    
}
