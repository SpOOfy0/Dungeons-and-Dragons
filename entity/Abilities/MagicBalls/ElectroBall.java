package entity.Abilities.MagicBalls;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import entity.Abilities.Ability;
import entity.Abilities.AbilityInterface;
import entity.Monsters.Monster;
import main.GamePannel;

public class ElectroBall extends Ability implements AbilityInterface {

    public int frameCount;

    public ElectroBall(GamePannel gp) {
        super(gp);

        getThisAbilityImage();
        AbilitySetting();
    }

   
    @Override
    public void getThisAbilityImage() {
        up1 = getImage("/Player/Ability/ElectroBall/Electroball_frame0.png");
        down1 = getImage("/Player/Ability/ElectroBall/Electroball_frame1.png");
        left1 = getImage("/Player/Ability/ElectroBall/Electroball_frame2.png");
        right1 = getImage("/Player/Ability/ElectroBall/Electroball_frame3.png");
    }

     @Override
    public void AbilitySetting() {
        damage = 3;
        initSpeed(2);
        range = 5;
        mana = 9;

        initiateMagicSpeed(50);

        frameCount = 0;
    }


    public void actionWhileActive() {
        frameCount++;
        if(frameCount >= 8) frameCount = 0;
    }

    public void actionOnHit() {
        Monster victim = gp.monster.get(abilityCollisionIndex);
        victim.receiveDmg(damage);
        victim.giveBlockMovement(35, false);
    }


    public void draw(Graphics2D g2) {

        if(direction[0] != null){

            BufferedImage image = null;
            int screenX = worldX - gp.player.worldX + gp.player.screenX ;
            int screenY = worldY - gp.player.worldY + gp.player.screenY ;

            if(worldX + tileSize > gp.player.worldX - gp.player.screenX &&
            worldX - tileSize < gp.player.worldX + gp.player.screenX &&
            worldY + tileSize > gp.player.worldY - gp.player.screenY &&
            worldY - tileSize < gp.player.worldY + gp.player.screenY) { 
            
                switch(frameCount){
                    case 0:
                    case 1:
                        image = up1;
                        break;
                    case 2:
                    case 3:
                        image = down1;
                        break;
                    case 4:
                    case 5:
                        image = left1;
                        break;
                    case 6:
                    case 7:
                        image = right1;
                        break;
                }
            
                g2.drawImage(image, screenX, screenY, tileSize, tileSize, null);
            }
        }
    }
    
}
