package entity.Abilities;

import entity.Entity;
import main.GamePannel;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Ability extends Entity{

    protected static String ballDirection = "none";
    public boolean abilityCollision = false;
    public int abilityCollisionIndex = 999;
    
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
            
        }
           //System.out.println(worldX + " " + worldY);
            //monsterCollision();
        }
             
    



    public void setAction() {

    }

    public int monsterCollision() {
        for(int i = 0; i < gp.monster.length; i++){
            if (gp.monster[i] != null){

                for (int j = 0 ; j < 30; j++){

                    for (int k = 0 ; k < 30; k++){
                        
                        if (gp.monster[i].worldX + j == worldX && gp.monster[i].worldY + k == worldY || gp.monster[i].worldX - j == worldX && gp.monster[i].worldY - k == worldY) {
                            // Collision détectée, faites quelque chose (par exemple, détruire le monstre, retirer la boule de feu, etc.)
                                //System.out.println("collision");
                                abilityCollision = true;
                                return i;
                        }
                        
                    }
                }
            }
        }
        return 999;        
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
