package main;

import java.awt.Rectangle;
import java.util.Vector;

import entity.Entity;

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

        int tileNum1 = 0, tileNum2 = 1;
        boolean checkMove;

        for(int i = 0; i < 4; i++){
            checkMove = false;
            switch(i){
                case 0:
                    if(isGoingToDirection[i]){
                        checkMove = true;
                        entityTopRow = (entityTopWorldY - entity.speed) / tileSize;
                    } else entityTopRow = (entityTopWorldY - entity.speed - 1) / tileSize;
                    tileNum1 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
                    tileNum2 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                    entityTopRow = entityTopWorldY / tileSize;
                    break;
                case 1:
                    if(isGoingToDirection[i]){
                        checkMove = true;
                        entityLeftCol = (entityLeftWorldX - entity.speed) / tileSize;
                    } else entityLeftCol = (entityLeftWorldX - entity.speed - 1) / tileSize;
                    tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                    tileNum2 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
                    entityLeftCol = entityLeftWorldX / tileSize;
                    break;
                case 2:
                    if(isGoingToDirection[i]){
                        checkMove = true;
                        entityBottomRow = (entityBottomWorldY + entity.speed) / tileSize;
                    } else entityBottomRow = (entityBottomWorldY + entity.speed + 4) / tileSize;
                    tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
                    tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];
                    entityBottomRow = entityBottomWorldY / tileSize;
                    break;
                case 3:
                    if(isGoingToDirection[i]){
                        checkMove = true;
                        entityRightCol = (entityRightWorldX + entity.speed) / tileSize;
                    } else entityRightCol = (entityRightWorldX + entity.speed + 4) / tileSize;
                    tileNum1 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];
                    tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
                    break; 
            }

            if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision){
                switch(i){
                    case 0:
                        entity.blockedUp = true;
                        break;
                    case 1:
                        entity.blockedLeft = true;
                        break;
                    case 2:
                        entity.blockedDown = true;
                        break;
                    case 3:
                        entity.blockedRight = true;
                        break;
                }
                if(checkMove) entity.collisionOn = true;
            }
        }
    }

    public Vector<Integer> checkObject(Entity entity, boolean player){ //We add player to be able to seperate player from the other entities like NPC's and monsters

        Vector<Integer> index = new Vector<Integer>();
        
        for(int i = 0; i < gp.obj.length; i++){

            if (gp.obj[i] != null){
                //Get entity's solid area position 
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;
                //Get object's solid area position
                gp.obj[i].solidArea.x = gp.obj[i].worldX + gp.obj[i].solidArea.x;
                gp.obj[i].solidArea.y = gp.obj[i].worldY + gp.obj[i].solidArea.y;

                if(entity.direction[0] != null){
                    switch(entity.direction[0]) {
                        case "up":
                            if (entity.solidArea.intersects(gp.obj[i].solidArea)){
                                if (gp.obj[i].collision){
                                    entity.collisionOn = true;
                                    entity.blockedUp = true;
                                }
                                if(player && !index.contains(i)) index.add(i);
                            }
                            break;
                        case "down":
                            if (entity.solidArea.intersects(gp.obj[i].solidArea)){
                                if (gp.obj[i].collision){
                                    entity.collisionOn = true;
                                    entity.blockedDown = true;
                                }
                                if(player && !index.contains(i)) index.add(i);
                            }
                            break;
                        case "left":
                            if (entity.solidArea.intersects(gp.obj[i].solidArea)){
                                if (gp.obj[i].collision){
                                    entity.collisionOn = true;
                                    entity.blockedLeft = true;
                                }
                                if(player && !index.contains(i)) index.add(i);
                            }
                            break;
                        case "right":
                            if (entity.solidArea.intersects(gp.obj[i].solidArea)){
                                if (gp.obj[i].collision){
                                    entity.collisionOn = true;
                                    entity.blockedRight = true;
                                }
                                if(player && !index.contains(i)) index.add(i);
                            }
                            break;
                    }
                }

                if(entity.direction[1] != null){
                    switch(entity.direction[1]) {
                        case "left":
                            if (entity.solidArea.intersects(gp.obj[i].solidArea)){
                                if (gp.obj[i].collision){
                                    entity.collisionOn = true;
                                    entity.blockedLeft = true;
                                }
                                if(player && !index.contains(i)) index.add(i);
                            }
                            break;
                        case "right":
                            if (entity.solidArea.intersects(gp.obj[i].solidArea)){
                                if (gp.obj[i].collision){
                                    entity.collisionOn = true;
                                    entity.blockedRight = true;
                                }
                                if(player && !index.contains(i)) index.add(i);
                            }
                            break;
                    }
                }

                //Reset entity's solid area position
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                //Reset object's solid area position
                gp.obj[i].solidArea.x = gp.obj[i].solidAreaDefaultX;
                gp.obj[i].solidArea.y = gp.obj[i].solidAreaDefaultY;
            }
            

        }
        return index;
    }

    public Vector<Integer> checkEntity(Entity entity, Entity[] target){
        Vector<Integer> index = new Vector<Integer>();
        
        for(int i = 0; i < target.length; i++){

            if (target[i] != null){
                //Get entity's solid area position
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;
                //Get object's solid area position
                target[i].solidArea.x = target[i].worldX + target[i].solidArea.x;
                target[i].solidArea.y = target[i].worldY + target[i].solidArea.y;

                int pos1 = 0, pos2 = 1;

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

                Rectangle intersection;

                for(int j = 0; j < 4; j++){
                    switch(j){
                        case 0:
                            pos1 = entity.solidArea.y;
                            pos2 = target[i].solidArea.y;
                            break;
                        case 1:
                            pos1 = entity.solidArea.x;
                            pos2 = target[i].solidArea.x;
                            break;
                        case 2:
                            pos1 = target[i].solidArea.y;
                            pos2 = entity.solidArea.y;
                            break;
                        case 3:
                            pos1 = target[i].solidArea.x;
                            pos2 = entity.solidArea.x;
                            break;
                    }

                    if (entity.solidArea.intersects(target[i].solidArea)){
                        if(pos1 > pos2){
                            intersection = entity.solidArea.intersection(target[i].solidArea);
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
                        if(isGoingToDirection[j]) entity.collisionOn = true;
                        if(!index.contains(i)) index.add(i);
                    }
                }

                //Reset entity's solid area position
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                //Reset object's solid area position
                target[i].solidArea.x = target[i].solidAreaDefaultX;
                target[i].solidArea.y = target[i].solidAreaDefaultY;
            }
        }
        return index;
    }

    public void checkPlayer(Entity entity){
        //Get entity's solid area position 
        entity.solidArea.x = entity.worldX + entity.solidArea.x;
        entity.solidArea.y = entity.worldY + entity.solidArea.y;
        //Get object's solid area position
        gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
        gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;


        int pos1 = 0, pos2 = 1;

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

        Rectangle intersection;

        for(int j = 0; j < 4; j++){
            switch(j){
                case 0:
                    pos1 = entity.solidArea.y;
                    pos2 = gp.player.solidArea.y;
                    break;
                case 1:
                    pos1 = entity.solidArea.x;
                    pos2 = gp.player.solidArea.x;
                    break;
                case 2:
                    pos1 = gp.player.solidArea.y;
                    pos2 = entity.solidArea.y;
                    break;
                case 3:
                    pos1 = gp.player.solidArea.x;
                    pos2 = entity.solidArea.x;
                    break;
            }

            if (entity.solidArea.intersects(gp.player.solidArea)){
                if(pos1 > pos2){
                    intersection = entity.solidArea.intersection(gp.player.solidArea);
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
                if(isGoingToDirection[j]) entity.collisionOn = true;
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
