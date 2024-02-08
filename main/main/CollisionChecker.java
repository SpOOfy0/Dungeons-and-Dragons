package main;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Vector;

import entity.Entity;
import entity.Player;
import object.SuperObject;


public class CollisionChecker {

    GamePannel gp;

    public CollisionChecker(GamePannel gp) {
        this.gp = gp;
    }

    public void checkTile(Entity entity){

        int entityLeftWorldX = entity.worldX + entity.solidArea.x;
        int entityRightWorldX = entityLeftWorldX + entity.solidArea.width;
        int entityTopWorldY = entity.worldY + entity.solidArea.y;
        int entityBottomWorldY = entityTopWorldY + entity.solidArea.height;

        int tileSize = gp.tileSize;

        int entityTopRow = entityTopWorldY / tileSize;
        int entityLeftCol = entityLeftWorldX / tileSize;
        int entityBottomRow = entityBottomWorldY / tileSize;
        int entityRightCol = entityRightWorldX / tileSize;

        boolean[] isGoingToDirection = {entity.storeMovement[1] < 0, entity.storeMovement[0] < 0, entity.storeMovement[1] > 0, entity.storeMovement[0] > 0};

        // 0=Up, 1=Left, 2=Bottom, 3=Right

        int iter;
        boolean blocker = false;
        int newDistance = 0;

        for(int i = 0; i < 4; i++){
            switch(i){
                case 0: //up
                    entityTopRow = (entityTopWorldY - entity.speed) / tileSize;

                    iter = entityLeftCol;
                    while(iter <= entityRightCol){
                        blocker = gp.tileM.isCollision(iter, entityTopRow);
                        if(blocker) iter += entityRightCol;
                        iter++;
                    }

                    if(blocker){
                        // S'il y a de la marge pour se déplacer, ça ne bloque pas mais ça change le déplacement pour aller jusqu'à l'endroit bloqué
                        if(isGoingToDirection[i] && entityTopRow != entityTopWorldY / tileSize){
                            newDistance = (entityTopRow+1)*tileSize - entityTopWorldY;
                            if(newDistance < 0){
                                if(entity.storeMovement[1] < newDistance)
                                    entity.storeMovement[1] = newDistance;
                            } else {
                                entity.blockedUp = true;
                                if(isGoingToDirection[i]) entity.isBlocked = true;
                            }

                        } else {
                            entity.blockedUp = true;
                            if(isGoingToDirection[i]) entity.isBlocked = true;
                        }
                    }
                    entityTopRow = entityTopWorldY / tileSize;
                    break;
                    
                    
                case 1: //left
                    entityLeftCol = (entityLeftWorldX - entity.speed) / tileSize;

                    iter = entityTopRow;
                    while(iter <= entityBottomRow){
                        blocker = gp.tileM.isCollision(entityLeftCol, iter);
                        if(blocker) iter += entityBottomRow;
                        iter++;
                    }

                    if(blocker){
                        // S'il y a de la marge pour se déplacer, ça ne bloque pas mais ça change le déplacement pour aller jusqu'à l'endroit bloqué
                        if(isGoingToDirection[i] && entityLeftCol != entityLeftWorldX / tileSize){
                            newDistance = (entityLeftCol+1)*tileSize - entityLeftWorldX;
                            if(newDistance < 0){
                                if(entity.storeMovement[0] < newDistance)
                                    entity.storeMovement[0] = newDistance;
                            } else {
                                entity.blockedLeft = true;
                                if(isGoingToDirection[i]) entity.isBlocked = true;
                            }

                        } else {
                            entity.blockedLeft = true;
                            if(isGoingToDirection[i]) entity.isBlocked = true;
                        }
                    }
                    entityLeftCol = entityLeftWorldX / tileSize;
                    break;


                case 2: //down
                    entityBottomRow = (entityBottomWorldY + entity.speed) / tileSize;

                    iter = entityLeftCol;
                    while(iter <= entityRightCol){
                        blocker = gp.tileM.isCollision(iter, entityBottomRow);
                        if(blocker) iter += entityRightCol;
                        iter++;
                    }

                    if(blocker){
                        // S'il y a de la marge pour se déplacer, ça ne bloque pas mais ça change le déplacement pour aller jusqu'à l'endroit bloqué
                        if(isGoingToDirection[i] && entityBottomRow != entityBottomWorldY / tileSize){
                            newDistance = entityBottomRow*tileSize - entityBottomWorldY - 1;
                            if(0 < newDistance){
                                if(newDistance < entity.storeMovement[1])
                                    entity.storeMovement[1] = newDistance;
                            } else {
                                entity.blockedDown = true;
                                if(isGoingToDirection[i]) entity.isBlocked = true;
                            }
                            
                        } else {
                            entity.blockedDown = true;
                            if(isGoingToDirection[i]) entity.isBlocked = true;
                        }
                    }
                    entityBottomRow = entityBottomWorldY / tileSize;
                    break;


                case 3: //right
                    entityRightCol = (entityRightWorldX + entity.speed) / tileSize;

                    iter = entityTopRow;
                    while(iter <= entityBottomRow){
                        blocker = gp.tileM.isCollision(entityRightCol, iter);
                        if(blocker) iter += entityBottomRow;
                        iter++;
                    }

                    if(blocker){
                        // S'il y a de la marge pour se déplacer, ça ne bloque pas mais ça change le déplacement pour aller jusqu'à l'endroit bloqué
                        // pour une raison non recherchée et faute de temps, il faut incrémenter entityRightWorldX de 3 pour que ça marche comme on veut (mettez des prints pour les "newDistance", ça ne va pas vérifier les collisions en continu)
                        // à regarder plus en détail à l'avenir
                        if(isGoingToDirection[i] && entityRightCol != (entityRightWorldX+3) / tileSize){
                            newDistance = (entityRightCol)*tileSize - entityRightWorldX - 1;
                            if(0 < newDistance){
                                if(newDistance < entity.storeMovement[0])
                                    entity.storeMovement[0] = newDistance;
                            } else {
                                entity.blockedDown = true;
                                if(isGoingToDirection[i]) entity.isBlocked = true;
                            }

                        } else {
                            entity.blockedRight = true;
                            if(isGoingToDirection[i]) entity.isBlocked = true;
                        }
                    }
                    break; 
            }
        }
    }



    //We add player to be able to seperate player from the other entities like NPC's and monsters
    public ArrayList<Integer> checkObject(Entity entity, boolean player){

        ArrayList<Integer> index = new ArrayList<Integer>();
        int i = 0;
        
        for(SuperObject iter : gp.item){

            if (iter != null){

                boolean[] isGoingToDirection = {entity.storeMovement[1] < 0, entity.storeMovement[0] < 0, entity.storeMovement[1] > 0, entity.storeMovement[0] > 0};

                // 0=Up, 1=Left, 2=Bottom, 3=Right

                int newDistance = 0;

                //Get object's solid area position
                iter.solidArea.x = iter.worldX + iter.solidArea.x;
                iter.solidArea.y = iter.worldY + iter.solidArea.y;

                for(int j = 0; j < 4; j++){

                    //Get entity's solid area position 
                    entity.solidArea.x = entity.solidAreaDefaultX + entity.worldX;
                    entity.solidArea.y = entity.solidAreaDefaultY + entity.worldY;

                    switch(j){
                        case 0: //up
                            entity.solidArea.y -= entity.speed;
                            break;
                        case 1: //left
                            entity.solidArea.x -= entity.speed;
                            break;
                        case 2: //down
                            entity.solidArea.y += entity.speed;
                            break;
                        case 3: //right
                            entity.solidArea.x += entity.speed;
                            break;
                    }

                    if (entity.solidArea.intersects(iter.solidArea)){
                        if (iter.collision){
                            switch(j){
                                case 0: //up
                                    newDistance = iter.solidArea.y + iter.solidArea.height - (entity.solidArea.y + entity.speed);
                                    if(newDistance < 0 && entity.storeMovement[1] < newDistance)
                                        entity.storeMovement[1] = newDistance;
                                    else{
                                        entity.blockedUp = true;
                                        if(isGoingToDirection[j]) entity.isBlocked = true;
                                    }
                                    break;
                                case 1: //left
                                    newDistance = iter.solidArea.x + iter.solidArea.width - (entity.solidArea.x + entity.speed);
                                    if(newDistance < 0 && entity.storeMovement[0] < newDistance)
                                        entity.storeMovement[0] = newDistance;
                                    else {
                                        entity.blockedLeft = true;
                                        if(isGoingToDirection[j]) entity.isBlocked = true;
                                    }
                                    break;
                                case 2: //down
                                    newDistance = iter.solidArea.y - (entity.solidArea.y + entity.solidArea.height - entity.speed) - 1;
                                    if(0 < newDistance && newDistance < entity.storeMovement[1])
                                        entity.storeMovement[1] = newDistance;
                                    else {
                                        entity.blockedDown = true;
                                        if(isGoingToDirection[j]) entity.isBlocked = true;
                                    }
                                    break;
                                case 3: //right
                                    newDistance = iter.solidArea.x - (entity.solidArea.x + entity.solidArea.width - entity.speed) - 1;
                                    if(0 < newDistance && newDistance < entity.storeMovement[0])
                                        entity.storeMovement[0] = newDistance;
                                    else {
                                        entity.blockedRight = true;
                                        if(isGoingToDirection[j]) entity.isBlocked = true;
                                    }
                                    break;
                            }
                        }
                        if(player && !index.contains(i)) index.add(i);
                    }

                }

                //Reset entity's solid area position
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                //Reset object's solid area position
                iter.solidArea.x = iter.solidAreaDefaultX;
                iter.solidArea.y = iter.solidAreaDefaultY;
            }
            
            i++;
        }
        return index;
    }



    public ArrayList<Integer> checkEntity(Entity entity, Vector<? extends Entity> target){
        ArrayList<Integer> index = new ArrayList<Integer>();
        int i = 0;

        for(Entity studiedTarget : target){

            if (target.get(i) != null){

                boolean[] isGoingToDirection = {entity.storeMovement[1] < 0, entity.storeMovement[0] < 0, entity.storeMovement[1] > 0, entity.storeMovement[0] > 0};

                // 0=Up, 1=Left, 2=Bottom, 3=Right

                //Get object's solid area position
                studiedTarget.solidArea.x = studiedTarget.worldX + studiedTarget.solidArea.x;
                studiedTarget.solidArea.y = studiedTarget.worldY + studiedTarget.solidArea.y;

                int pos1 = 0, pos2 = 1;

                Rectangle intersection;

                int newDistance = 0;

                for(int j = 0; j < 4; j++){

                    //Get entity's solid area position
                    entity.solidArea.x = entity.solidAreaDefaultX + entity.worldX;
                    entity.solidArea.y = entity.solidAreaDefaultY + entity.worldY;

                    switch(j){
                        case 0: //up
                            entity.solidArea.y -= entity.speed;
                            pos1 = entity.solidArea.y + entity.solidArea.height/2;
                            pos2 = studiedTarget.solidArea.y + studiedTarget.solidArea.height/2;
                            break;
                        case 1: //left
                            entity.solidArea.x -= entity.speed;
                            pos1 = entity.solidArea.x + entity.solidArea.width/2;
                            pos2 = studiedTarget.solidArea.x + studiedTarget.solidArea.width/2;
                            break;
                        case 2: //down
                            entity.solidArea.y += entity.speed;
                            pos1 = studiedTarget.solidArea.y + studiedTarget.solidArea.height/2;
                            pos2 = entity.solidArea.y + entity.solidArea.height/2;
                            break;
                        case 3: //right
                            entity.solidArea.x += entity.speed;
                            pos1 = studiedTarget.solidArea.x + studiedTarget.solidArea.width/2;
                            pos2 = entity.solidArea.x + entity.solidArea.width/2;
                            break;
                    }

                    if (entity.solidArea.intersects(studiedTarget.solidArea)){
                        if(pos1 > pos2){
                            intersection = entity.solidArea.intersection(studiedTarget.solidArea);
                            if (j%2 == 0){
                                if(intersection.height < intersection.width){
                                    if(j == 0){
                                        newDistance = studiedTarget.solidArea.y + studiedTarget.solidArea.height - (entity.solidArea.y + entity.speed);
                                        if(newDistance < 0 && entity.storeMovement[1] < newDistance)
                                            entity.storeMovement[1] = newDistance;
                                        else {
                                            entity.blockedUp = true;
                                            if(isGoingToDirection[j]) entity.isBlocked = true;
                                        }
                                    } else {
                                        newDistance = studiedTarget.solidArea.y - (entity.solidArea.y + entity.solidArea.height - entity.speed);
                                        if(0 < newDistance && newDistance < entity.storeMovement[1])
                                            entity.storeMovement[1] = newDistance;
                                        else {
                                            entity.blockedDown = true;
                                            if(isGoingToDirection[j]) entity.isBlocked = true;
                                        }
                                    }
                                }
                            } else {
                                if(intersection.height > intersection.width){
                                    if(j == 1){
                                        newDistance = studiedTarget.solidArea.x + studiedTarget.solidArea.width - (entity.solidArea.x + entity.speed);
                                        if(newDistance < 0 && entity.storeMovement[0] < newDistance)
                                            entity.storeMovement[0] = newDistance;
                                        else {
                                            entity.blockedLeft = true;
                                            if(isGoingToDirection[j]) entity.isBlocked = true;
                                        }
                                    } else{
                                        newDistance = studiedTarget.solidArea.x - (entity.solidArea.x + entity.solidArea.width - entity.speed);
                                        if(0 < newDistance && newDistance < entity.storeMovement[0])
                                            entity.storeMovement[0] = newDistance;
                                        else {
                                            entity.blockedRight = true;
                                            if(isGoingToDirection[j]) entity.isBlocked = true;
                                        }
                                    }
                                }
                            }
                        }
                        if(!index.contains(i)) index.add(i);
                    }
                }

                //Reset entity's solid area position
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                //Reset object's solid area position
                studiedTarget.solidArea.x = studiedTarget.solidAreaDefaultX;
                studiedTarget.solidArea.y = studiedTarget.solidAreaDefaultY;
            }

            i++;
        }
        
        return index;
    }


    
    public void checkPlayer(Entity entity){
        
        entity.isWithPlayer = false;

        boolean[] isGoingToDirection = {entity.storeMovement[1] < 0, entity.storeMovement[0] < 0, entity.storeMovement[1] > 0, entity.storeMovement[0] > 0};

        // 0=Up, 1=Left, 2=Bottom, 3=Right

        Player player = Player.getInstance(gp, gp.keyHandler);
        
        //Get object's solid area position
        player.solidArea.x = player.worldX + player.solidArea.x;
        player.solidArea.y = player.worldY + player.solidArea.y;

        int pos1 = 0, pos2 = 1;

        Rectangle intersection;

        int newDistance = 0;

        for(int j = 0; j < 4; j++){

            //Get entity's solid area position
            entity.solidArea.x = entity.solidAreaDefaultX + entity.worldX;
            entity.solidArea.y = entity.solidAreaDefaultY + entity.worldY;

            switch(j){
                case 0: //up
                    entity.solidArea.y -= entity.speed;
                    pos1 = entity.solidArea.y + entity.solidArea.height/2;
                    pos2 = player.solidArea.y + player.solidArea.height/2;
                    break;
                case 1: //left
                    entity.solidArea.x -= entity.speed;
                    pos1 = entity.solidArea.x + entity.solidArea.width/2;
                    pos2 = player.solidArea.x + player.solidArea.width/2;
                    break;
                case 2: //down
                    entity.solidArea.y += entity.speed;
                    pos1 = player.solidArea.y + player.solidArea.height/2;
                    pos2 = entity.solidArea.y + entity.solidArea.height/2;
                    break;
                case 3: //right
                    entity.solidArea.x += entity.speed;
                    pos1 = player.solidArea.x + player.solidArea.width/2;
                    pos2 = entity.solidArea.x + entity.solidArea.width/2;
                    break;
            }

            if (entity.solidArea.intersects(player.solidArea)){
                if(pos1 > pos2){
                    intersection = entity.solidArea.intersection(player.solidArea);
                    if (j%2 == 0){
                        if(intersection.height <= intersection.width){
                            if(j == 0){
                                newDistance = player.solidArea.y + player.solidArea.height - (entity.solidArea.y + entity.speed);
                                if(newDistance < 0 && entity.storeMovement[1] < newDistance)
                                    entity.storeMovement[1] = newDistance;
                                else {
                                    entity.blockedUp = true;
                                    if(isGoingToDirection[j]){
                                        entity.isBlocked = true;
                                        if(entity != player) entity.isWithPlayer = true;
                                    }
                                }
                            } else {
                                newDistance = player.solidArea.y - (entity.solidArea.y + entity.solidArea.height - entity.speed);
                                if(0 < newDistance && newDistance < entity.storeMovement[1])
                                    entity.storeMovement[1] = newDistance;
                                else {
                                    entity.blockedDown = true;
                                    if(isGoingToDirection[j]){
                                        entity.isBlocked = true;
                                        if(entity != player) entity.isWithPlayer = true;
                                    }
                                }
                            }
                        }
                    } else {
                        if(intersection.height >= intersection.width){
                            if(j == 1){
                                newDistance = player.solidArea.x + player.solidArea.width - (entity.solidArea.x + entity.speed);
                                if(newDistance < 0 && entity.storeMovement[0] < newDistance)
                                    entity.storeMovement[0] = newDistance;
                                else {
                                    entity.blockedLeft = true;
                                    if(isGoingToDirection[j]){
                                        entity.isBlocked = true;
                                        if(entity != player) entity.isWithPlayer = true;
                                    }
                                }
                            } else {
                                newDistance = player.solidArea.x - (entity.solidArea.x + entity.solidArea.width - entity.speed);
                                if(0 < newDistance && newDistance < entity.storeMovement[0])
                                    entity.storeMovement[0] = newDistance;
                                else {
                                    entity.blockedRight = true;
                                    if(isGoingToDirection[j]){
                                        entity.isBlocked = true;
                                        if(entity != player) entity.isWithPlayer = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
                
        //Reset entity's solid area position
        entity.solidArea.x = entity.solidAreaDefaultX;
        entity.solidArea.y = entity.solidAreaDefaultY;
        //Reset object's solid area position
        player.solidArea.x = player.solidAreaDefaultX;
        player.solidArea.y = player.solidAreaDefaultY;
    } 
    
}
