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
    int rangeChecked = 0;
    
    public Ability(GamePannel gp) {

        super(gp);
    }

    public void update() {
        if (gp.player.ballOn == 1) {
            if (direction[0] == null) {
                direction[0] = gp.player.facing;
                facing = gp.player.facing;
                worldX = gp.player.worldX;
                worldY = gp.player.worldY;
            }
            else if(direction[0] == "up") worldY -= speed;
            else if(direction[0] == "down") worldY += speed;
            else if(direction[0] == "left") worldX -= speed;
            else if(direction[0] == "right") worldX += speed;
            
            abilityCollisionIndex = monsterCollision();
            if(abilityCollision){
                gp.monster.get(abilityCollisionIndex).receiveDmg(damage);
            }
            rangeAbility();
            
        }
    }

    //CodeMana
    // public void AbilityManaCosr() {

    // }
             
    public int monsterCollision() {

        int i = 0;
        for(Monster iterMonster : gp.monster){
            abilityCollision = false;
            if (iterMonster != null){

                solidArea.x += worldX;
                solidArea.y += worldY;
                iterMonster.solidArea.x += iterMonster.worldX;
                iterMonster.solidArea.y += iterMonster.worldY;

                if (solidArea.intersects(iterMonster.solidArea)){
                    abilityCollision = true;
                    direction[0] = null;
                    gp.player.ballOn = 0;
                    gp.ability = null;

                    solidArea.x = solidAreaDefaultX;
                    solidArea.y = solidAreaDefaultY;
                    iterMonster.solidArea.x = iterMonster.solidAreaDefaultX;
                    iterMonster.solidArea.y = iterMonster.solidAreaDefaultY;

                    return i;
                }

                solidArea.x = solidAreaDefaultX;
                solidArea.y = solidAreaDefaultY;
                iterMonster.solidArea.x = iterMonster.solidAreaDefaultX;
                iterMonster.solidArea.y = iterMonster.solidAreaDefaultY;
            }

            i++;
        }
        return 999;
    }

    public void rangeAbility(){
        if(gp.ability != null){
            
            if(direction[0] == "up" || direction[0] == "down"){
                distanceTraveled = Math.abs(worldY - gp.player.positionYActivityOn);
                rangeChecked = 1;
            }
            else if(direction[0] == "left" || direction[0] == "right"){
                distanceTraveled = Math.abs(worldX - gp.player.positionXActivityOn);
                rangeChecked = 1;
            }
            if(distanceTraveled >= range*gp.tileSize){
                gp.player.ballOn = 0;
                direction[0] = null;
                gp.ability = null;
            }
        }

    }

    //CodeMana
    // public void manaCost(){
    //     gp.player.mana -= mana;
    // }

    public void draw(Graphics2D g2) {

        BufferedImage image = null;
        int screenX = worldX - gp.player.worldX + gp.player.screenX ;
        int screenY = worldY - gp.player.worldY + gp.player.screenY ;

        if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
        worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
        worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
        worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) { 
        
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
        
            g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
        }
    }
    
}
