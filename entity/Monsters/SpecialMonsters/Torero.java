package entity.Monsters.SpecialMonsters;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;

import entity.Monsters.*;
import entity.ExtraActions.SlamAttack;
import entity.ExtraActions.BehaviorForActions.BeCloseToPlayer;
import main.GamePannel;


public class Torero extends SpecialMonster implements MonsterInterface {

    private int screenHeight;

    BufferedImage   up3, down3, left3, right3,
                    attackUp1, attackUp2, attackUp3,
                    attackDown1, attackDown2, attackDown3,
                    attackLeft1, attackLeft2, attackLeft3,
                    attackRight1, attackRight2, attackRight3;

    private int healing;
    private int triggerHeal;

    public SlamAttack slam;

    public boolean isUsingSlam = false;
    private boolean isPreviousWalk2;
    
    Random random = new Random();

    public Torero (GamePannel gp, String inputedDirection, int coordX, int coordY) {
        super(gp, inputedDirection, coordX, coordY);

        getThisMonsterImage();
        MonsterSetting(false);
        screenHeight = gp.screenHeight;
    }

    @Override
    public void getThisMonsterImage() {
        attackDown1 = attackUp1 = attackLeft1 = attackRight1 = getImage("/Player/Monster/Torero/Boss_SL_Attack1.png");
        attackDown2 = attackUp2 = attackLeft2 = attackRight2 = getImage("/Player/Monster/Torero/Boss_SL_Attack2.png");
        attackDown3 = attackUp3 = attackLeft3 = attackRight3 = getImage("/Player/Monster/Torero/Boss_SL_Attack3.png");
        down1 = up1 = left1 = right1 = getImage("/Player/Monster/Torero/Boss_SL_walk_1.png");
        down2 = up2 = left2 = right2 = getImage("/Player/Monster/Torero/Boss_SL_walk_2.png");
        down3 = up3 = left3 = right3 = getImage("/Player/Monster/Torero/Boss_SL_walk_mid.png");
    }

    @Override
    public void MonsterSetting(boolean willDropItem) {

        noticeRange = 3;
        aggroRange = 20;
        initSpeed(1);
        maxLife = 200;
        life = maxLife;
        damage = 2;
        noKnockback = true;

        xp = 0;
        monsterSize = 4 * gp.tileSize;

        solidArea = new Rectangle(1,1, (tileSize*2) - 2, (tileSize*7/2) - 2);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        isPreviousWalk2 = true;

        slam = new SlamAttack(this, damage, 2, 90, new BeCloseToPlayer(this, 300, 2));
        healing = 0;
        triggerHeal = 60;
    }


    public boolean executeSpActions(){
        
        if(aggravated){

            healing = 0;
            triggerHeal = 300;

            ArmyCharge();
            isUsingSlam = slam.execute();
            return isUsingSlam;
        } else {

            if(healing < triggerHeal) healing++;
            else if(life < maxLife){
                healing = 0;
                triggerHeal = 90;
                life++;
            }
            
            return false;
        }
    }

    public void triggerEvent() {
        gp.monster.clear();
        gp.gameState = gp.winState;
    }

    

    public void ArmyCharge(){
        int randomWorldX = random.nextInt(monsterSize) + worldX + solidAreaDefaultX + solidArea.width - monsterSize/2/tileSize;
        if(randomWorldX < 7) randomWorldX = 7;
        else if(56 < randomWorldX) randomWorldX = 56;
        int randomWorldY = (random.nextInt(monsterSize) + worldY + solidAreaDefaultY + solidArea.height - monsterSize/2)/tileSize;
        if(randomWorldY < 6) randomWorldX = 6;
        else if(55 < randomWorldY) randomWorldX = 55;
        summonArmy(randomWorldX, randomWorldY, 3);
        
    }

    public void summonArmy(int worldX, int worldY, int numberOfSolders){
        gp.objSetter.MonsterSpawner(worldX, worldY, numberOfSolders);
    }



    public void spriteCounting(){

        if(isUsingSlam){
            if(slam.chronology == 0) spriteNum = 1;
            else if(slam.chronology < slam.timeFrame*3/5) spriteNum = 2;
            else {
                spriteNum = 3;
                spriteCounter = 0;
            }
        } else {
            spriteCounter++;
            if(spriteCounter >= 24){
                if(isPreviousWalk2){
                    spriteNum = 1;
                    isPreviousWalk2 = false;
                } else {
                    spriteNum = 2;
                    isPreviousWalk2 = true;
                }
                spriteCounter = 0;
            } else if(spriteCounter == 14) spriteNum = 3;
        }

        
    }


    @Override
    public void draw(Graphics2D g2){

        BufferedImage image = null;

        int screenX = worldX - player.worldX + player.screenX ;
        int screenY = worldY - player.worldY + player.screenY ;

        if( worldX + tileSize*5/2 > player.worldX - player.screenX &&
            worldX - tileSize/2 - tileSize < player.worldX + player.screenX &&
            worldY + (tileSize*7/2) > player.worldY - player.screenY &&
            worldY - tileSize - tileSize < player.worldY + player.screenY) {
            
            if (isUsingSlam) {
                switch(facing){
                    case "up":
                        if (spriteNum == 1) image = attackUp1;
                        else if (spriteNum == 2) image = attackUp2;
                        else image = attackUp3;
                        break;
                    case "down":
                        if (spriteNum == 1) image = attackDown1;
                        else if (spriteNum == 2) image = attackDown2;
                        else image = attackDown3;
                        break;
                    case "left":
                        if (spriteNum == 1) image = attackLeft1;
                        else if (spriteNum == 2) image = attackLeft2;
                        else image = attackLeft3;
                        break;
                    case "right":
                        if (spriteNum == 1) image = attackRight1;
                        else if (spriteNum == 2) image = attackRight2;
                        else image = attackRight3;
                        break;                              
                }
            } else {
                switch(facing){
                    case "up":
                        if (spriteNum == 1) image = up1;
                        else if (spriteNum == 2) image = up2;
                        else image = up3;
                        break;
                    case "down":
                        if (spriteNum == 1) image = down1;
                        else if (spriteNum == 2) image = down2;
                        else image = down3;
                        break;
                    case "left":
                        if (spriteNum == 1) image = left1;
                        else if (spriteNum == 2) image = left2;
                        else image = left3;
                        break;
                    case "right":
                        if (spriteNum == 1) image = right1;
                        else if (spriteNum == 2) image = right2;
                        else image = right3;
                        break;                              
                }
            }
            
            g2.drawImage(image, screenX - tileSize/2, screenY - tileSize + 7, tileSize*3, tileSize*9/2, null);
            //g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);

            // System.out.println("leftLimit: " + leftLimit + "  rightLimit: " + rightLimit + "  upLimit: " + upLimit + "  downLimit: " + downLimit);
        }
    }

    public void paintComponent(Graphics2D g2) {
            
        // Draw the life bar
        int reamningLife = maxLife - life;
        int lifeBarWidth =  648 - (648 * reamningLife / maxLife);

        g2.setColor(Color.RED);
        g2.drawRect(60, screenHeight - 50, 648, 20);
        g2.fillRect(60, screenHeight - 50, lifeBarWidth, 20);
    }

}



