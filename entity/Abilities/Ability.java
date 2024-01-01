package entity.Abilities;

import entity.Entity;
import main.GamePannel;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Ability extends Entity{

    protected static String ballDirection = "none";

    public boolean abilityCollision = false;
    public int abilityCollisionIndex = 999;
    
    //Mana cost for each ability
    public int fireManaCost = 20;   
    

    protected int range;
    protected int remaningDistance;
    int rangeChecked = 0;
    
    public Ability(GamePannel gp) {

        super(gp);
    }

    public void update() {
        if (gp.player.ballOn == 1) {
            if (ballDirection == "none") {
                ballDirection = gp.player.direction;
                direction = gp.player.direction;
                worldX = gp.player.worldX;
                worldY = gp.player.worldY;
            }
            else if(ballDirection == "up"){
                worldY = worldY - speed;
                ballDirection = "up";
            }
            else if(ballDirection == "down"){
                worldY = worldY + speed;
                ballDirection = "down";           
            }
            else if(ballDirection == "left"){
                worldX = worldX - speed;
                ballDirection = "left";
            }
            else if(ballDirection == "right"){
                worldX = worldX + speed;
                ballDirection = "right";
 
            }
            solidArea.x = worldX;
            solidArea.y = worldY; 
            abilityCollisionIndex = monsterCollision();
            rangeAbility();
            
        }
        }
    
    public void AbilityManaCosr() {
        
    }
             
    public int monsterCollision() {
        for(int i = 0; i < gp.monster.length; i++){
            if (gp.monster[i] != null){

                for (int j = 0 ; j < 30; j++){

                    for (int k = 0 ; k < 30; k++){
                        
                        if (gp.monster[i].worldX + j == worldX && gp.monster[i].worldY + k == worldY || gp.monster[i].worldX - j == worldX && gp.monster[i].worldY - k == worldY) {
                                //Collision detected
                                abilityCollision = true;
                                ballDirection = "none";
                                return i;
                        }
                        
                    }
                }
            }
        }
        return 999;        
    }

    public void rangeAbility(){
        if(gp.ability != null){
            
            if((ballDirection == "up" || ballDirection == "down")){
                remaningDistance = Math.abs(worldY - gp.player.positionYActivityOn);
                rangeChecked = 1;
            }
            else if((ballDirection == "left" || ballDirection == "right")){
                remaningDistance = Math.abs(worldX - gp.player.positionXActivityOn);
                rangeChecked = 1;
            }
            if(remaningDistance >= range*gp.tileSize){
                gp.player.ballOn = 0;
                ballDirection = "none";   
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

        if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
        worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
        worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
        worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) { 
        
                switch(ballDirection){
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
