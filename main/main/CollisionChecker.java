package main;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Vector;

import entity.Entity;
import object.SuperObject;


public class CollisionChecker {

    GamePannel gp;
    int s = 0;

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

        boolean[] isGoingToDirection = {false, false, false, false};

        // 0=Up, 1=Left, 2=Bottom, 3=Right

        for(int i = 0; i < entity.direction.length; i++){
            if(entity.direction[i] != null){
                switch(entity.direction[i]) {
                    case "up":
                        isGoingToDirection[0] = true;
                        break;
                    case "left":
                        isGoingToDirection[1] = true;
                        break;
                    case "down":
                        isGoingToDirection[2] = true;
                        break;
                    case "right":
                        isGoingToDirection[3] = true;
                        break;
                }
            }
        }

        int iter;
        boolean blocker = false;

        for(int i = 0; i < 4; i++){
            switch(i){
                case 0: //up
                    entityTopRow = (entityTopWorldY - entity.baseSpeed*2) / tileSize;

                    iter = entityLeftCol;
                    while(iter <= entityRightCol){
                        blocker = gp.tileM.isCollision(iter, entityTopRow);
                        if(blocker) iter += entityRightCol;
                        else iter++;
                    }
                    
                    entityTopRow = entityTopWorldY / tileSize;
                    break;
                case 1: //left
                    entityLeftCol = (entityLeftWorldX - entity.baseSpeed*2) / tileSize;

                    iter = entityTopRow;
                    while(iter <= entityBottomRow){
                        blocker = gp.tileM.isCollision(entityLeftCol, iter);
                        if(blocker) iter += entityBottomRow;
                        else iter++;
                    }

                    entityLeftCol = entityLeftWorldX / tileSize;
                    break;
                case 2: //down
                    entityBottomRow = (entityBottomWorldY + entity.baseSpeed*2) / tileSize;

                    iter = entityLeftCol;
                    while(iter <= entityRightCol){
                        blocker = gp.tileM.isCollision(iter, entityBottomRow);
                        if(blocker) iter += entityRightCol;
                        else iter++;
                    }

                    entityBottomRow = entityBottomWorldY / tileSize;
                    break;
                case 3: //right
                    entityRightCol = (entityRightWorldX + entity.baseSpeed*2) / tileSize;

                    iter = entityTopRow;
                    while(iter <= entityBottomRow){
                        blocker = gp.tileM.isCollision(entityRightCol, iter);
                        if(blocker) iter += entityBottomRow;
                        else iter++;
                    }
                    break; 
            }

            if (blocker){
                switch(i){
                    case 0: //up
                        entity.blockedUp = true;
                        break;
                    case 1: //left
                        entity.blockedLeft = true;
                        break;
                    case 2: //down
                        entity.blockedDown = true;
                        break;
                    case 3: //right
                        entity.blockedRight = true;
                        break;
                }
                if(isGoingToDirection[i]) entity.isBlocked = true;
            }
        }
    }



    //We add player to be able to seperate player from the other entities like NPC's and monsters
    public ArrayList<Integer> checkObject(Entity entity, boolean player){

        ArrayList<Integer> index = new ArrayList<Integer>();
        int i = 0;
        
        for(SuperObject iter : gp.item){

            if (iter != null){

                boolean[] isGoingToDirection = {false, false, false, false};

                // 0=Up, 1=Left, 2=Bottom, 3=Right

                for(int j = 0; j < entity.direction.length; j++){
                    if(entity.direction[j] != null){
                        switch(entity.direction[j]) {
                            case "up":
                                isGoingToDirection[0] = true;
                                break;
                            case "left":
                                isGoingToDirection[1] = true;
                                break;
                            case "down":
                                isGoingToDirection[2] = true;
                                break;
                            case "right":
                                isGoingToDirection[3] = true;
                                break;
                        }
                    }
                }

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
                                    entity.blockedUp = true;
                                    break;
                                case 1: //left
                                    entity.blockedLeft = true;
                                    break;
                                case 2: //down
                                    entity.blockedDown = true;
                                    break;
                                case 3: //right
                                    entity.blockedRight = true;
                                    break;
                            }
                            if(isGoingToDirection[j]) entity.isBlocked = true;
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

                boolean[] isGoingToDirection = {false, false, false, false};

                // 0=Up, 1=Left, 2=Bottom, 3=Right

                for(int j = 0; j < entity.direction.length; j++){
                    if(entity.direction[j] != null){
                        switch(entity.direction[j]) {
                            case "up":
                                isGoingToDirection[0] = true;
                                break;
                            case "left":
                                isGoingToDirection[1] = true;
                                break;
                            case "down":
                                isGoingToDirection[2] = true;
                                break;
                            case "right":
                                isGoingToDirection[3] = true;
                                break;
                        }
                    }
                }

                //Get object's solid area position
                studiedTarget.solidArea.x = studiedTarget.worldX + studiedTarget.solidArea.x;
                studiedTarget.solidArea.y = studiedTarget.worldY + studiedTarget.solidArea.y;

                int pos1 = 0, pos2 = 1;

                Rectangle intersection;

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
                                    if(j == 0) entity.blockedUp = true;
                                    else entity.blockedDown = true;
                                }
                            } else {
                                if(intersection.height > intersection.width){
                                    if(j == 1) entity.blockedLeft = true;
                                    else entity.blockedRight = true;
                                }
                            }
                        }
                        if(isGoingToDirection[j]) entity.isBlocked = true;
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

        boolean[] isGoingToDirection = {false, false, false, false};

        // 0=Up, 1=Left, 2=Bottom, 3=Right

        for(int j = 0; j < entity.direction.length; j++){
            if(entity.direction[j] != null){
                switch(entity.direction[j]) {
                    case "up":
                        isGoingToDirection[0] = true;
                        break;
                    case "left":
                        isGoingToDirection[1] = true;
                        break;
                    case "down":
                        isGoingToDirection[2] = true;
                        break;
                    case "right":
                        isGoingToDirection[3] = true;
                        break;
                }
            }
        }
        
        //Get object's solid area position
        gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
        gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;

        int pos1 = 0, pos2 = 1;

        Rectangle intersection;

        for(int j = 0; j < 4; j++){

            //Get entity's solid area position
            entity.solidArea.x = entity.solidAreaDefaultX + entity.worldX;
            entity.solidArea.y = entity.solidAreaDefaultY + entity.worldY;

            switch(j){
                case 0: //up
                    entity.solidArea.y -= entity.speed;
                    pos1 = entity.solidArea.y + entity.solidArea.height/2;
                    pos2 = gp.player.solidArea.y + gp.player.solidArea.height/2;
                    break;
                case 1: //left
                    entity.solidArea.x -= entity.speed;
                    pos1 = entity.solidArea.x + entity.solidArea.width/2;
                    pos2 = gp.player.solidArea.x + gp.player.solidArea.width/2;
                    break;
                case 2: //down
                    entity.solidArea.y += entity.speed;
                    pos1 = gp.player.solidArea.y + gp.player.solidArea.height/2;
                    pos2 = entity.solidArea.y + entity.solidArea.height/2;
                    break;
                case 3: //right
                    entity.solidArea.x += entity.speed;
                    pos1 = gp.player.solidArea.x + gp.player.solidArea.width/2;
                    pos2 = entity.solidArea.x + entity.solidArea.width/2;
                    break;
            }

            if (entity.solidArea.intersects(gp.player.solidArea)){
                if(pos1 > pos2){
                    intersection = entity.solidArea.intersection(gp.player.solidArea);
                    if (j%2 == 0){
                        if(intersection.height <= intersection.width){
                            if(j == 0) entity.blockedUp = true;
                            else entity.blockedDown = true;
                        }
                    } else {
                        if(intersection.height >= intersection.width){
                            if(j == 1) entity.blockedLeft = true;
                            else entity.blockedRight = true;
                        }
                    }
                }
                if(isGoingToDirection[j]){
                    entity.isBlocked = true;
                    if(entity != gp.player) entity.isWithPlayer = true;
                }
            }
        }
                
        //Reset entity's solid area position
        entity.solidArea.x = entity.solidAreaDefaultX;
        entity.solidArea.y = entity.solidAreaDefaultY;
        //Reset object's solid area position
        gp.player.solidArea.x = gp.player.solidAreaDefaultX;
        gp.player.solidArea.y = gp.player.solidAreaDefaultY;
    } 
    
}
