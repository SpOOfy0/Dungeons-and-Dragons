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
    }


    
    // ici, seulement quand le monstre poursuit le joueur, "bufferDirection" enregistre la direction auquel le monstre veut se déplaceer pour aller
    // vers le joueur seulement quand il est bloqué dans cette direction, en sorte de lui redonner cette direction après débloquage de cette direction
    // exemple: Le monstre veut aller à droite pour aller vers le joueur et se trouve face à un mur.
    //          "bufferDirection" enregistre "Right", et après fait en sorte que le monstre ne peut plus se déplacer vers la droite, pour qu'il prenne une autre direction.
    //          Seulement quand le monstre pourra se déplacer vers la droite que "bufferDirection" redonnera la direction
     public void setAction() {

        if(attackDelay < attackSpeed) attackDelay++;

        if(!aggravated){
            gp.interactionChecker.noticePlayer(this);
            // si l'entité vient de changer de la non-traque à la traque, tous les blocages de choix de direction sont retirés
            if(aggravated) decideLetGoAll();
        }

        if (aggravated && (Math.abs(gp.player.worldX - worldX) <= aggroRange*gp.tileSize) && (Math.abs(gp.player.worldY - worldY) <= aggroRange*gp.tileSize)) {

            // si l'entité est bloquée dans son mouvement mais qu'elle n'est pas bloquée contre le joueur,
            // on récupère les dernières directions prises, enregistre ces directions dans "bufferFirection",
            // et on enlève individuellement le choix de prendre ces directions si l'entité est bloquée dans les directions correspondantes
            if(!isWithPlayer && isBlocked){
                bufferDirection = null;
                for(int i = 0; i < direction.length; i++){
                    if(direction[i] != null){
                        bufferDirection = direction[i];
                        direction[i] = null;
                        decideRestrain(bufferDirection);
                    }
                }

            // si l'entité n'est bloquée dans son mouvement ou qu'elle l'est mais est bloquée contre le joueur,
            // on redonne le choix de prendre la direction que "bufferDirection" a enregistré,
            // et on effectue de même pour les dernières directions prises par l'entité sur l'image précédente
            } else {
                if (bufferDirection != null) decideLetGo(bufferDirection);
                for(int i = 0; i < direction.length; i++){
                    if(direction[i] != null) decideLetGo(direction[i]);
                }
            }

            GoToPlayer(gp.player);

        } else {

            // si l'entité vient de changer de la traque à la non-traque, tous les blocages de choix de direction sont retirés
            if(aggravated){
                decideLetGoAll();
                aggravated = false;
            }

            actionCounter++;

            // si l'entité est bloquée dans son mouvement, on récupère les dernières directions prises,
            // et on enlève individuellement le choix de prendre ces directions si l'entité est bloquée dans les directions correspondantes
            // de plus, on incrémente la variable d'impatience
            if(isBlocked){
                for(int i = 0; i < direction.length; i++){
                    if(direction[i] != null) decideRestrain(direction[i]);
                }
                impatience++;

            // si l'entité est bloquée dans son mouvement, on récupère les dernières directions prises,
            // et on redonne indivduellement le choix de prendre ces directions si l'entité n'est pas bloquée dans les directions correspondantes
            // de plus, on réinitialise la variable d'impatience
            } else {
                for(int i = 0; i < direction.length; i++){
                    if(direction[i] != null) decideLetGo(direction[i]);
                }
                impatience = 0;
            }

            // l'entité change de direction quand son compteur associée est a atteint la limite ou si elle est restée sans bouger trop longtemps
            if(actionCounter >= actionTimer || impatience >= impatienceTolerance){
                
                wander();
                actionCounter = 0;
                impatience = 0;
            }
        }

    }



    public void GoToPlayer(Player player) {

        // to follow the player
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

            // to follow the player when can't be followed with the previous functions
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
