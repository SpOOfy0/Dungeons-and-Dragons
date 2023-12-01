package entity.Abilities.FireBall;

import java.awt.Rectangle;

import entity.Abilities.Ability;
import entity.Abilities.AbilityInterface;
import main.GamePannel;

public class FireBall extends Ability implements AbilityInterface {

    public FireBall(GamePannel gp) {
        super(gp);
        getTihsAbilityImage();
        AbilitySetting();
        
    }

   

    @Override
    public void getTihsAbilityImage() {
        up1 = getImage("/Player/Ability/FireBall/Up.png");
        down1 = getImage("/Player/Ability/FireBall/Down.png");
        left1 = getImage("/Player/Ability/FireBall/Left.png");
        right1 = getImage("/Player/Ability/FireBall/Right.png");
    }

    @Override
    public void AbilitySetting() {
        speed = 4;
        worldX = gp.player.worldX ;
        worldY = gp.player.worldY ;
        direction = gp.player.direction;
        
        
    }
        

    
}
