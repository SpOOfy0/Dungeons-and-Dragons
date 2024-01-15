package entity.Monsters;

import java.awt.Color;
import java.awt.Graphics2D;

import entity.Entity;
import entity.Player;
import main.GamePannel;

public class Monster extends Entity{

    public boolean noKnockback = false;
    
    public Monster(GamePannel gp, String inputedDirection, int coordX, int coordY) {
        super(gp);
        
        aggravated = false;
        noticeRange = 2;

        int[] newCoords = gp.tileM.verifyAndCorrectPlacement(coordX, coordY);

        worldX = newCoords[0] * tileSize;
        worldY = newCoords[1] * tileSize;

        if(inputedDirection != null) direction[0] = inputedDirection;
        else direction[0] = "up";
        direction[1] = null;
        facing = direction[0];
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
            if(aggravated){
                bufferDirection = null;
                forceLetGoAll();
                if(speed != baseSpeed) speed = baseSpeed;
            }
        }

        if (aggravated && (Math.abs(gp.player.worldX - worldX) <= aggroRange*tileSize) && (Math.abs(gp.player.worldY - worldY) <= aggroRange*tileSize)) {

            
            if (!isWithPlayer){

                // si l'entité est bloquée dans son mouvement mais qu'elle n'est pas bloquée contre le joueur,
                // on récupère les dernières directions prises, enregistre ces directions dans "bufferFirection",
                // et on enlève individuellement le choix de prendre ces directions si l'entité est bloquée dans les directions correspondantes
                if (isBlocked){
                    bufferDirection = null;
                    for (int i = 0; i < direction.length; i++){
                        if (direction[i] != null){
                            bufferDirection = direction[i];
                            direction[i] = null;
                            decideRestrain(bufferDirection);
                        }
                    }

                // si l'entité n'est bloquée dans son mouvement et non bloquée contre le joueur,
                // on redonne le choix de prendre la direction que "bufferDirection" a enregistré,
                // et on effectue de même pour les dernières directions prises par l'entité sur l'image précédente
                } else {
                    if (bufferDirection != null) decideLetGo(bufferDirection);
                    for (int i = 0; i < direction.length; i++){
                        if (direction[i] != null){
                            decideLetGo(direction[i]);
                        }
                    }
                }
                
                GoToPlayer(gp.player);
                
            // si l'entité est bloquée contre le joueur, elle ira en sa direction
            } else {
                bufferDirection = null;
                direction[0] = gp.interactionChecker.towardsPlayer(this);
            }

        } else {

            // si l'entité vient de changer de la traque à la non-traque, tous les blocages de choix de direction sont retirés
            if(aggravated){
                forceLetGoAll();
                bufferDirection = null;
                if(speed != baseSpeed) speed = baseSpeed;
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

            // si l'entité n'est pas bloquée dans son mouvement, on redonne le choix de prendre les directions précédemment bloquées
            // du moment que l'entité n'est pas bloquée dans les directions correspondantes
            // de plus, on réinitialise la variable d'impatience
            } else {
                decideLetGoAll();
                impatience = 0;
            }

            // l'entité change de direction quand son compteur associée est a atteint la limite ou si elle est restée sans bouger trop longtemps
            if(actionCounter >= actionTimer || impatience >= impatienceTolerance){
                
                wander();
                actionCounter = 0;
                impatience = 0;
                if(speed != baseSpeed) speed = baseSpeed;
            }
            
        }

    }



    public void GoToPlayer(Player player) {

        // to follow the player
        if(worldX + solidAreaDefaultX + solidArea.width <= player.worldX + player.solidAreaDefaultX + 1){
            if(!stopDirections[3]){
                direction[0] = "right";
                if(speed != baseSpeed) speed = baseSpeed;
            }
        } else if(player.worldX + player.solidAreaDefaultX + player.solidArea.width <= worldX + solidAreaDefaultX + 1){
            if(!stopDirections[2]){
                direction[0] = "left";
                if(speed != baseSpeed) speed = baseSpeed;
            }
        } else if(worldY + solidAreaDefaultY + solidArea.height <= player.worldY + player.solidAreaDefaultY + 1){
            if(!stopDirections[1]){
                direction[0] = "down";
                if(speed != baseSpeed) speed = baseSpeed;
            }
        } else if(player.worldY + player.solidAreaDefaultY + player.solidArea.height <= worldY + solidAreaDefaultY + 1){
            if(!stopDirections[0]){
                direction[0] = "up";
                if(speed != baseSpeed) speed = baseSpeed;
            }
        }


        if(direction[0] == null){

            int posTilePlayer;
            int posMonster;

            // to follow the player when can't be followed with the previous functions (like in gaps)
            switch(bufferDirection) {
                case "down":
                case "up":
                    posTilePlayer = player.getLeftTileBorder();
                    posMonster = worldX + solidAreaDefaultX;

                    if (posMonster < posTilePlayer){
                        decideLetGo("right");
                        if(!stopDirections[3]){
                            direction[0] = "right";
                            if (posMonster + solidArea.width > player.worldX + player.solidAreaDefaultX + 1 && speed > 1) speed--;
                        }
                    } else if (posTilePlayer < posMonster){
                        decideLetGo("left");
                        if(!stopDirections[2]){
                            direction[0] = "left";
                            if (player.worldX + player.solidAreaDefaultX + player.solidArea.width > posMonster + 1 && speed > 1) speed--;
                        }
                    }
                    break;
                case "right":
                case "left":
                    posTilePlayer = player.getUpperTileBorder();
                    posMonster = worldY + solidAreaDefaultY;

                    if (posMonster < posTilePlayer){
                        decideLetGo("down");
                        if(!stopDirections[1]){
                            direction[0] = "down";
                            if (posMonster + solidArea.height > player.worldY + player.solidAreaDefaultY + 1 && speed > 1) speed--;
                        }
                    } else if (posTilePlayer < posMonster ){
                        decideLetGo("up");
                        if(!stopDirections[0]){
                            direction[0] = "up";
                            if (player.worldY + player.solidAreaDefaultY + player.solidArea.height > posMonster + 1 && speed > 1) speed--;
                        }
                    }
                    break;
            }
        }

    }
    


    public void attackPlayer() {
        if(gp.player.life > 0) gp.player.life -= damage;
        attackDelay = 0;
    }

    public void receiveDmg(int dmg) {
        life -= dmg;
        aggravated = true;
    }


    public void paintComponent(Graphics2D g2) {
            
        // Draw the life bar
        int reamningLife = maxLife - life;
        int lifeBarWidth =  tileSize - (tileSize * reamningLife / maxLife);
        int screenX = worldX - gp.player.worldX + gp.player.screenX ;
        int screenY = worldY - gp.player.worldY + gp.player.screenY ;

        g2.setColor(Color.RED);
        g2.fillRect(screenX, screenY - 10, lifeBarWidth, 8);
    }

}
