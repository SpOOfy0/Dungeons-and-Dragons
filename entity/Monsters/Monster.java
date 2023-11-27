package entity.Monsters;

import java.util.Random;

import entity.Entity;
import entity.Player;
import main.GamePannel;

public class Monster extends Entity{
    
    public int dommage;

    public Monster(GamePannel gp) {
        super(gp);
        
        monsterDead();
  
    }

     @Override
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
        gp.player.life = gp.player.life - dommage;
    }

    public void monsterDead() {
        
        if(gp.player.monsterIndex != 999 && gp.monster[gp.player.monsterIndex].life <= 0) {

            gp.monster[gp.player.monsterIndex] = null;
            gp.player.monsterIndex = 999;
        }
    }

    
}
