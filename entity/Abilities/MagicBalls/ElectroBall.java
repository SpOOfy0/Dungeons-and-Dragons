package entity.Abilities.MagicBalls;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import entity.Abilities.*;
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
        damage = 6;
        initSpeed(2);
        range = 5;
        mana = 18;

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
            int screenX = worldX - player.worldX + player.screenX ;
            int screenY = worldY - player.worldY + player.screenY ;

            if(worldX + tileSize > player.worldX - player.screenX &&
            worldX - tileSize < player.worldX + player.screenX &&
            worldY + tileSize > player.worldY - player.screenY &&
            worldY - tileSize < player.worldY + player.screenY) { 
            
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
