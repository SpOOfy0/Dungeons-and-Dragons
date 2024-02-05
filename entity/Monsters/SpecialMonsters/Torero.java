package entity.Monsters.SpecialMonsters;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;

import entity.Monsters.*;
import entity.ExtraActions.SlamAttack;
import entity.ExtraActions.BehaviorForActions.BasicBehavior;
import entity.ExtraActions.BehaviorForActions.ImpatiantBehavior;
import main.GamePannel;


public class Torero extends SpecialMonster implements MonsterInterface {

    BufferedImage   up3, down3, left3, right3,
                    attackUp1, attackUp2, attackUp3,
                    attackDown1, attackDown2, attackDown3,
                    attackLeft1, attackLeft2, attackLeft3,
                    attackRight1, attackRight2, attackRight3;

    ImpatiantBehavior slotForSlam;
    
    Random random = new Random();

    public Torero (GamePannel gp, String inputedDirection, int coordX, int coordY) {
        super(gp, inputedDirection, coordX, coordY);

        getThisMonsterImage();
        MonsterSetting();
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
    public void MonsterSetting() {

        gp.objSetter.setMonsterMatrix();

        noticeRange = 3;
        aggroRange = 20;
        initSpeed(1);
        maxLife = 100;
        life = maxLife;
        damage = 2;
        noKnockback = true;

        xp = 0;
        // objToDrop[0] = "key";
        // objToDrop[1] = "manaPotion";
        monsterSize = 5 * gp.tileSize;

        solidArea = new Rectangle(1,1, (tileSize*3) - 2, (tileSize*9/2) - 2);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        slotForSlam = new ImpatiantBehavior(120);
    }


    public boolean executeSpActions(){
        
        if(aggravated){
            ArmyCharge();
            return slotForSlam.executeAction();
        } else {
            return false;
        }
    }

    

    public void ArmyCharge(){
        // int randomWorldX = random.nextInt(30) - 30 + solidAreaDefaultX + worldX;
        // int randomWorldY = random.nextInt(30) - 30 + solidAreaDefaultY + worldY;
        int randomWorldX = random.nextInt(monsterSize) + worldX;
        int randomWorldY = random.nextInt(monsterSize) + worldY;
        summonArmy(randomWorldX, randomWorldY, 20);
        
    }

    public void summonArmy(int worldX, int worldY, int numberOfSolders){
        gp.objSetter.MonsterSpawner(worldX, worldY, numberOfSolders);
    }


    // @Override
    // public void update(){
    //     super.update();
    //     //setBossSolidArea();
    // }

    // public void setBossSolidArea(){
    //     if(facing == "up") solidArea = new Rectangle(50, 50, 46 * 5, 46 * 5);
    //     else if(facing == "down") solidArea = new Rectangle(50, 50, 46 * 5, 46 * 5);
    //     else if(facing == "left") solidArea = new Rectangle(50, 50, 46 * 3, 46 * 5);
    //     else if(facing == "right") solidArea = new Rectangle(50, 50, 46 * 3, 46 * 5);
    // }

    // int minute = 0;
    // int second = minute * 60;

    // public void ArmyCharge2(){
    //     if (second == 30){
   
    //         if (life < maxLife / 2){
    //             summonArmy(worldX, worldY, 10);
    //         }
    //         else{
    //             System.out.println("summon");
    //             summonArmy(worldX, worldY, 6);
    //         }
    //         second = 0;
    //     }
    //     second ++;
         
    // }



    @Override
    public void draw(Graphics2D g2){

        BufferedImage image = null;

        int screenX = worldX - gp.player.worldX + gp.player.screenX ;
        int screenY = worldY - gp.player.worldY + gp.player.screenY ;

        if( worldX + solidAreaDefaultX > gp.player.worldX - gp.player.screenX &&
            worldX < gp.player.worldX + gp.player.screenX &&
            worldY + solidAreaDefaultY > gp.player.worldY - gp.player.screenY &&
            worldY < gp.player.worldY + gp.player.screenY) { 
            
                switch(facing){
                    case "up":
                        if (spriteNum == 1) image = up1;
                        else if (spriteNum == 2) image = up2;
                        break;
                    case "down":
                        if (spriteNum == 1) image = down1;
                        else if (spriteNum == 2) image = down2;
                        break;
                    case "left":
                        if (spriteNum == 1) image = left1;
                        else if (spriteNum == 2) image = left2;
                        break;
                    case "right":
                        if (spriteNum == 1) image = right1;
                        else if (spriteNum == 2) image = right2;
                        break;                              
                }
            
            g2.drawImage(image, screenX, screenY, monsterSize, monsterSize, null);
            g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
            //System.out.println(direction[0] + " " + facing );
            //System.out.println(solidArea.x + " " + solidArea.y + " " + solidArea.width + " " + solidArea.height);
        }
    }

    public void paintComponent(Graphics2D g2) {
            
        // Draw the life bar
        int reamningLife = maxLife - life;
        int lifeBarWidth =  648 - (648 * reamningLife / maxLife);

        g2.setColor(Color.RED);
        g2.drawRect(60, gp.screenHeight - 50, 648, 20);
        g2.fillRect(60, gp.screenHeight - 50, lifeBarWidth, 20);
    }

}



