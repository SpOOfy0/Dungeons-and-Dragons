package entity.Monsters.BossMonster;
import main.GamePannel;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;

import entity.Monsters.Monster;
import entity.Monsters.MonsterInterface;


public class Torero extends Monster implements MonsterInterface {
    
    Random random = new Random();

    public Torero (GamePannel gp, String inputedDirection, int coordX, int coordY) {
        super(gp, inputedDirection, coordX, coordY);

        getThisMonsterImage();
        MonsterSetting();
    }

    int minute = 0;
    int second = minute * 60;

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
        gp.objSetter.setMonsterMatrix();
        noticeRange = 3;
        aggroRange = 20;
        initSpeed(1);
        maxLife = 8;
        life = maxLife;
        damage = 1;
        xp = 700;
        objToDrop[0] = "key";
        objToDrop[1] = "manaPotion";
        monsterSize = 5 * gp.tileSize;
        solidAreaDefaultX = 20;
        solidAreaDefaultY = 11;
        solidArea = new Rectangle(50, 50, 46 * 4 + 20, 45 * 5);
    }

    public void setBossSolidArea(){
        if(facing == "up") solidArea = new Rectangle(50, 50, 46 * 5, 46 * 5);
        else if(facing == "down") solidArea = new Rectangle(50, 50, 46 * 5, 46 * 5);
        else if(facing == "left") solidArea = new Rectangle(50, 50, 46 * 3, 46 * 5);
        else if(facing == "right") solidArea = new Rectangle(50, 50, 46 * 3, 46 * 5);
    }

    public void summonArmy(int worldX, int worldY, int numberOfSolders){
        gp.objSetter.MonsterSpawner(worldX, worldY, numberOfSolders);
    }

    public void ArmyCharge2(){
        if (second == 30){
   
            if (life < maxLife / 2){
                summonArmy(worldX, worldY, 10);
            }
            else{
                System.out.println("summon");
                summonArmy(worldX, worldY, 6);
            }
            second = 0;
        }
        second ++;
         
    }

    public void ArmyCharge(){
        // int randomWorldX = random.nextInt(30) - 30 + solidAreaDefaultX + worldX;
        // int randomWorldY = random.nextInt(30) - 30 + solidAreaDefaultY + worldY;
        int randomWorldX = random.nextInt(monsterSize) + worldX;
        int randomWorldY = random.nextInt(monsterSize) + worldY;
        summonArmy(randomWorldX, randomWorldY, 20);
        
    }


    @Override
    public void update(){
        super.update();
        ArmyCharge();
        //setBossSolidArea();
    }

    @Override
    public void draw(Graphics2D g2){
        BufferedImage image = null;
        int screenX = worldX - gp.player.worldX + gp.player.screenX ;
        int screenY = worldY - gp.player.worldY + gp.player.screenY ;

        if( worldX + tileSize > gp.player.worldX - gp.player.screenX &&
            worldX - tileSize < gp.player.worldX + gp.player.screenX &&
            worldY + tileSize > gp.player.worldY - gp.player.screenY &&
            worldY - tileSize < gp.player.worldY + gp.player.screenY) { 
            
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



