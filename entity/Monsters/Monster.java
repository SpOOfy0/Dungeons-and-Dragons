package entity.Monsters;

import java.awt.Color;
import java.awt.Graphics2D;

import entity.Entity;
import entity.Player;
import main.GamePannel;

public class Monster extends Entity{
    
    public int dommage;
    
    public Monster(GamePannel gp) {
        super(gp);
        
        aggravated = false;
        noticeRange = 2;
        monsterDead();
  
    }

    
    // ici, bufferDirection enregistre la direction auquel le monstre veut aller pour aller vers le joueur
    // seulement quand il est bloqué dans cette direction, en sorte de lui redonner cette direction après débloquage de cette direction
     public void setAction() {

        if(attackDelay < attackSpeed) attackDelay++;

        if(!aggravated){
            gp.interactionChecker.noticePlayer(this);
            if(aggravated) decideLetGoAll();
        }

        if (aggravated && (Math.abs(gp.player.worldX - worldX) <= aggroRange*gp.tileSize) && (Math.abs(gp.player.worldY - worldY) <= aggroRange*gp.tileSize)) {

            if(!isWithPlayer && isBlocked){
                bufferDirection = null;
                for(int i = 0; i < direction.length; i++){
                    if(direction[i] != null){
                        bufferDirection = direction[i];
                        direction[i] = null;
                        decideRestrain(bufferDirection);
                    }
                }
            } else {
                if (bufferDirection != null) decideLetGo(bufferDirection);
                for(int i = 0; i < direction.length; i++){
                    if(direction[i] != null){
                        decideLetGo(direction[i]);
                    }
                }
            }

            GoToPlayer(gp.player);

        } else {

            if(aggravated){
                decideLetGoAll();
                aggravated = false;
            }

            actionCounter++;

            if(isBlocked){
                for(int i = 0; i < direction.length; i++){
                    if(direction[i] != null){
                        decideRestrain(direction[i]);
                    }
                }
                impatience++;
            } else {
                for(int i = 0; i < direction.length; i++){
                    if(direction[i] != null){
                        decideLetGo(direction[i]);
                    }
                }
                impatience = 0;
            }

            if(actionCounter >= actionTimer || impatience >= impatienceTolerance){ //WAIT 2 SECONDS (120 frames = 2 seconds)
                
                wander();
                actionCounter = 0;
                impatience = 0;
            }
        }

    }


    public void GoToPlayer(Player player) {

        if(worldY + solidAreaDefaultY + solidArea.height <= player.worldY + player.solidAreaDefaultY + 1){
            if(!stopDirections[1]) direction[0] = "down";
        } else if(player.worldY + player.solidAreaDefaultY + player.solidArea.height <= worldY + solidAreaDefaultY + 1){
            if(!stopDirections[0]) direction[0] = "up";
        }

        if(worldX + solidAreaDefaultX + solidArea.width <= player.worldX + player.solidAreaDefaultX + 1){
            if(!stopDirections[3]) direction[0] = "right";
        } else if(player.worldX + player.solidAreaDefaultX + player.solidArea.width <= worldX + solidAreaDefaultX + 1){
            if(!stopDirections[2]) direction[0] = "left";
        }

        if(direction[0] == null){

            int tileSize = gp.tileSize;

            // to follow the player when the player is in a 1-tile gap
            switch(bufferDirection) {
                case "down":
                case "up":
                    if(worldX + solidAreaDefaultX < (player.worldX + player.solidAreaDefaultX)/tileSize){
                        if(!stopDirections[3]) direction[0] = "right";
                    } else if((player.worldX + player.solidAreaDefaultX)/tileSize < worldX + solidAreaDefaultX){
                        if(!stopDirections[2]) direction[0] = "left";
                    }
                    break;
                case "right":
                case "left":
                    if(worldY + solidAreaDefaultY < (player.worldY + player.solidAreaDefaultY)/tileSize){
                        if(!stopDirections[1]) direction[0] = "down";
                    } else if((player.worldY + player.solidAreaDefaultY)/tileSize < worldY + solidAreaDefaultY){
                        if(!stopDirections[0]) direction[0] = "up";
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
