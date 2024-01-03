package entity.Monsters;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

import entity.Entity;
import entity.Player;
import main.GamePannel;

public class Monster extends Entity{
    
    public int dommage;
    public boolean blockRight = false, blockLeft = false, blockDown = false, blockUp = false;
    
    public Monster(GamePannel gp) {
        super(gp);
        
        aggravated = false;
        noticeRange = 2;
        monsterDead();
  
    }

    
     public void setAction() {

        if(attackDelay < attackSpeed) attackDelay++;

        if(!aggravated) gp.interactionChecker.noticePlayer(this);

        if (aggravated && (Math.abs(gp.player.worldX - worldX) <= aggroRange*gp.tileSize) && (Math.abs(gp.player.worldY - worldY) <= aggroRange*gp.tileSize)) {
            
            if(!isWithPlayer && isBlocked){
                bufferDirection = null;
                for(int i = 0; i < direction.length; i++){
                    if(direction[i] != null){
                        bufferDirection = direction[i];
                        direction[i] = null;
                        switch(bufferDirection) {
                            case "down":
                                if(blockedDown) blockDown = true;
                                break;
                            case "up":
                                if(blockedUp) blockUp = true;
                                break;
                            case "right":
                                if(blockedRight) blockRight = true;
                                break;
                            case "left":
                                if(blockedLeft) blockLeft = true;
                                break;
                        }
                    }
                }
            } else {
                if (bufferDirection != null){
                    switch(bufferDirection) {
                        case "down":
                            if(!blockedDown) blockDown = false;
                            break;
                        case "up":
                            if(!blockedUp) blockUp = false;
                            break;
                        case "right":
                            if(!blockedRight) blockRight = false;
                            break;
                        case "left":
                            if(!blockedLeft) blockLeft = false;
                            break;
                    }
                }
            }

            GoToPlayer(gp.player);

        } else {
            aggravated = false;
            actionCounter++;

            if(actionCounter >= 120){ //WAIT 2 SECONDS (120 frames = 2 seconds)
                Random random = new Random();
                int i = random.nextInt(100);

                if(i < 25){
                    direction[0] = "up";
                } else if(i < 50){
                    direction[0] = "down";
                } else if(i < 75){
                    direction[0] = "left";
                } else if(i < 100){
                    direction[0] = "right";
                }
                actionCounter = 0;
            }
        }

    }


    public void GoToPlayer(Player player) {

        if(worldY + solidAreaDefaultY + solidArea.height <= player.worldY + player.solidAreaDefaultY + 1){
            if(!blockDown) direction[0] = "down";
        } else if(player.worldY + player.solidAreaDefaultY + player.solidArea.height <= worldY + solidAreaDefaultY + 1){
            if(!blockUp) direction[0] = "up";
        }

        if(worldX + solidAreaDefaultX + solidArea.width <= player.worldX + player.solidAreaDefaultX + 1){
            if(!blockRight) direction[0] = "right";
        } else if(player.worldX + player.solidAreaDefaultX + player.solidArea.width <= worldX + solidAreaDefaultX + 1){
            if(!blockLeft) direction[0] = "left";
        }

        if(direction[0] == null){

            int tileSize = gp.tileSize;

            // to follow the player when the player is in a 1-tile gap
            switch(bufferDirection) {
                case "down":
                case "up":
                    if(worldX + solidAreaDefaultX < (player.worldX + player.solidAreaDefaultX)/tileSize){
                        if(!blockRight) direction[0] = "right";
                    } else if((player.worldX + player.solidAreaDefaultX)/tileSize < worldX + solidAreaDefaultX){
                        if(!blockLeft) direction[0] = "left";
                    }
                    break;
                case "right":
                case "left":
                    if(worldY + solidAreaDefaultY < (player.worldY + player.solidAreaDefaultY)/tileSize){
                        if(!blockDown) direction[0] = "down";
                    } else if((player.worldY + player.solidAreaDefaultY)/tileSize < worldY + solidAreaDefaultY){
                        if(!blockUp) direction[0] = "up";
                    }
                    break;
            }
        }
    }
    

    public void attackPlayer() {
        gp.player.life -= dommage;
        attackDelay = 0;
    }

    public void receiveDmg(int dmg) {
        life -= dmg;
        aggravated = true;
    }

    public void monsterDead() {
        
        for(int i = 0; i < gp.monster.length; i++) {
            if(gp.monster[i] != null) {
                if(gp.monster[i].life <= 0) {
                    gp.monster[i] = null;
                }
            }
        }
        
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
