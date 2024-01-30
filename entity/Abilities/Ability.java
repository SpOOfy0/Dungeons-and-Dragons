package entity.Abilities;

import entity.Entity;
import entity.Monsters.Monster;
import main.GamePannel;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Ability extends Entity{

    public boolean abilityCollision = false;
    public int abilityCollisionIndex = 999;

    //CodeMana
    //Mana cost for each ability
    //public int fireManaCost = 20;

    protected int range;
    protected int distanceTraveled;

    

    public static int MagicAtkMaxSpeed = 0;
    public int MagicAtkSpeed;
    public static int MagicAtkDelay = 0;

    public void initiateMagicSpeed(int value){
        MagicAtkSpeed = value;
        if(MagicAtkMaxSpeed < value) MagicAtkMaxSpeed = value;
    }

    public boolean canUseMagic(){
        return MagicAtkDelay >= MagicAtkSpeed;
    }

    public boolean canUseAllMagic(){
        return MagicAtkDelay >= MagicAtkMaxSpeed;
    }

    public void startMagicDelay(){
        MagicAtkDelay = 0;
    }
    


    public Ability(GamePannel gp) {

        super(gp);
    }

    public void update() {

        if(!canUseAllMagic()) MagicAtkDelay++;

        if (gp.player.ballOn == 1) {
           
            if (direction[0] == null) {
                direction[0] = gp.player.facing;
                facing = gp.player.facing;
                worldX = gp.player.worldX;
                worldY = gp.player.worldY;
                distanceTraveled = 0;

            } else {
                actionWhileActive();

                if(direction[0] == "up") worldY -= speed;
                else if(direction[0] == "down") worldY += speed;
                else if(direction[0] == "left") worldX -= speed;
                else if(direction[0] == "right") worldX += speed;

                distanceTraveled += speed;
                
                abilityCollisionIndex = monsterCollision();
                if(abilityCollision) actionOnHit();
                rangeAbility();
            }
        }

    }
    
    public void actionWhileActive() {}

    public void actionOnHit() {
        gp.monster.get(abilityCollisionIndex).receiveDmg(damage);
    }
             
    public int monsterCollision() {

        solidArea.x += worldX;
        solidArea.y += worldY;
        
        int i = 0;
        for(Monster iterMonster : gp.monster){
            abilityCollision = false;
            if (iterMonster != null){

                iterMonster.solidArea.x += iterMonster.worldX;
                iterMonster.solidArea.y += iterMonster.worldY;

                if (solidArea.intersects(iterMonster.solidArea)){
                    abilityCollision = true;
                    direction[0] = null;
                    gp.player.ballOn = 0;

                    solidArea.x = solidAreaDefaultX;
                    solidArea.y = solidAreaDefaultY;
                    iterMonster.solidArea.x = iterMonster.solidAreaDefaultX;
                    iterMonster.solidArea.y = iterMonster.solidAreaDefaultY;

                    return i;
                }

                iterMonster.solidArea.x = iterMonster.solidAreaDefaultX;
                iterMonster.solidArea.y = iterMonster.solidAreaDefaultY;
            }

            i++;
        }

        solidArea.x = solidAreaDefaultX;
        solidArea.y = solidAreaDefaultY;
        
        return 999;
    }

    public void rangeAbility(){
        if(gp.ability != null){
            if(distanceTraveled >= range*tileSize){
                gp.player.ballOn = 0;
                direction[0] = null;
            }
        }

    }

    public void manaCost(){
        gp.player.mana -= mana;
    }

    public void draw(Graphics2D g2) {

        BufferedImage image = null;
        int screenX = worldX - gp.player.worldX + gp.player.screenX ;
        int screenY = worldY - gp.player.worldY + gp.player.screenY ;

        if(worldX + tileSize > gp.player.worldX - gp.player.screenX &&
        worldX - tileSize < gp.player.worldX + gp.player.screenX &&
        worldY + tileSize > gp.player.worldY - gp.player.screenY &&
        worldY - tileSize < gp.player.worldY + gp.player.screenY &&
        direction[0] != null) { 
        
            switch(direction[0]){
                case "up":
                    image = up1;
                    break;
                case "down":
                    image = down1;
                    break;
                case "left":
                    image = left1;
                    break;
                case "right":
                    image = right1;
                    break;
            }
        
            g2.drawImage(image, screenX, screenY, tileSize, tileSize, null);
        }
    }
    
}
