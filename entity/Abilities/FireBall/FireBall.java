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
        damage = 2;
        initSpeed(5);
        range = 8;
        mana = 4;

        attackSpeed = 40;
        attackDelay = attackSpeed;

        direction[0] = null;
    }

}
