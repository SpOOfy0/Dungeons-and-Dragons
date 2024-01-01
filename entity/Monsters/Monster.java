package entity.Monsters;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

import entity.Entity;
import entity.Player;
import main.GamePannel;

public class Monster extends Entity{
    
    
    public Monster(GamePannel gp) {
        super(gp);
        
        monsterDead();
  
    }

    
     public void setAction() {

        if(Math.abs(gp.player.worldX - worldX) <= 3*gp.tileSize && Math.abs(gp.player.worldY - worldY) <= 3*gp.tileSize) {
            GoToPlayer(gp.player);
        }
        else{
            actionCounter++;

            if(actionCounter == 120){ //WAIT 2 SECONDS (120 frames = 2 seconds)
                Random random = new Random();
                int i = random.nextInt(100)+1;

                if(i <= 25){
                    direction = "up";
                }
                else if(i <= 50){
                    direction = "down";
                }
                else if(i <= 75){
                    direction = "left";
                }
                else if(i <= 100){
                    direction = "right";
                }
                actionCounter = 0;
        }
        }
    
     }

    public void GoToPlayer(Player player) {

       
            if(player.worldX > worldX) {
                direction = "right";
            }
            else if(player.worldX < worldX) {
                direction = "left";
            }
            else if(player.worldY > worldY) {
                direction = "down";
            }
            else if(player.worldY < worldY) {
                direction = "up";
            }
        
    }

    public void attackPlayer() {
        gp.player.life = gp.player.life - damage;
    }

    public void monsterDead() {
        
        for(int i = 0; i < gp.monster.length; i++) {
            if(gp.monster[i] != null) {
                if(gp.monster[i].life <= 0) {
                    gp.monster[i].xpGained();
                    gp.monster[i] = null;
                }
            }
        }
        
    }
    public void xpGained() {
        gp.player.xp = gp.player.xp + xp;
    }

    public void paintComponent(Graphics2D g2) {
            
        // Draw the life bar
        int reamningLife = maxLife - life;
        int lifeBarWidth =  gp.tileSize - (gp.tileSize * reamningLife / maxLife);
        int screenX = worldX - gp.player.worldX + gp.player.screenX ;
        int screenY = worldY - gp.player.worldY + gp.player.screenY ;

        g2.setColor(Color.RED);
        g2.fillRect(screenX, screenY - 10, lifeBarWidth, 8);
    }

    
}
