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
        int entityRightCol = entityRightWorldX / tileSize;
        int entityBottomRow = entityBottomWorldY / tileSize;
        int entityLeftCol = entityLeftWorldX / tileSize;

        int[] tilesAround = {   gp.tileM.mapTileNum[entityLeftCol][entityTopRow],
                                gp.tileM.mapTileNum[entityRightCol][entityTopRow],
                                gp.tileM.mapTileNum[entityRightCol][entityBottomRow],
                                gp.tileM.mapTileNum[entityLeftCol][entityBottomRow]};

        int tileNum1 = tilesAround[0], tileNum2 = tilesAround[1];

        boolean[] directionBlock = {false, false, false, false};

        // 0=Up, 1=Right, 2=Bottom, 3=Left

        for(int i = 0; i < entity.direction.length; i++){
            if(entity.direction[i] != null){
                switch(entity.direction[i]) {
                    case "up":
                        directionBlock[0] = true;
                        break;
                    case "right":
                        directionBlock[1] = true;
                        break;
                    case "down":
                        directionBlock[2] = true;
                        break;
                    case "left":
                        directionBlock[3] = true;
                        break;
                }
            }
        }

        boolean checkBlock;

        for(int i = 0; i < 4; i++){
            checkBlock = false;
            if(directionBlock[i]){
                checkBlock = true;
                switch(i){
                    case 0:
                        entityTopRow = (entityTopWorldY - entity.speed) / tileSize;
                        tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                        tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
                        entityTopRow = entityTopWorldY / tileSize;
                        break;
                    case 1:
                        entityRightCol = (entityRightWorldX + entity.speed) / tileSize;
                        tileNum1 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
                        tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];
                        entityRightCol = entityRightWorldX / tileSize;
                        break;
                    case 2:
                        entityBottomRow = (entityBottomWorldY + entity.speed) / tileSize;
                        tileNum1 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];
                        tileNum2 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
                        entityBottomRow = entityBottomWorldY / tileSize;
                        break;
                    case 3:
                        entityLeftCol = (entityLeftWorldX - entity.speed) / tileSize;
                        tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
                        tileNum2 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                        break;
                }
            } else {
                tileNum1 = tilesAround[i%4];
                tileNum2 = tilesAround[(i+1)%4];
            }

            if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision){
                switch(i){
                    case 0:
                        entity.blockedUp = true;
                        break;
                    case 1:
                        entity.blockedRight = true;
                        break;
                    case 2:
                        entity.blockedDown = true;
                        break;
                    case 3:
                        entity.blockedLeft = true;
                        break;
                }
                if(checkBlock) entity.collisionOn = true;
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

                Rectangle intersection;

                if(entity.direction[0] != null){
                    switch(entity.direction[0]) {
                        case "up":
                            if (entity.solidArea.intersects(target[i].solidArea)){
                                entity.collisionOn = true;
                                if(entity.solidArea.y > target[i].solidArea.y){
                                    intersection = entity.solidArea.intersection(target[i].solidArea);
                                    if(intersection.height < intersection.width) entity.blockedUp = true;
                                }
                                if(!index.contains(i)) index.add(i);
                            }
                            break;
                        case "down":
                            if (entity.solidArea.intersects(target[i].solidArea)){
                                entity.collisionOn = true;
                                if(entity.solidArea.y < target[i].solidArea.y){
                                    intersection = entity.solidArea.intersection(target[i].solidArea);
                                    if(intersection.height < intersection.width) entity.blockedDown = true;
                                }
                                if(!index.contains(i)) index.add(i);
                            }
                            break;
                        case "left":
                            if (entity.solidArea.intersects(target[i].solidArea)){
                                entity.collisionOn = true;
                                if(entity.solidArea.x > target[i].solidArea.x){
                                    intersection = entity.solidArea.intersection(target[i].solidArea);
                                    if(intersection.height > intersection.width) entity.blockedLeft = true;
                                }
                                if(!index.contains(i)) index.add(i);
                            }
                            break;
                        case "right":
                            if (entity.solidArea.intersects(target[i].solidArea)){
                                entity.collisionOn = true;
                                if(entity.solidArea.x < target[i].solidArea.x){
                                    intersection = entity.solidArea.intersection(target[i].solidArea);
                                    if(intersection.height > intersection.width) entity.blockedRight = true;
                                }
                                if(!index.contains(i)) index.add(i);
                            }
                            break;
                    }
                }
                
                if(entity.direction[1] != null){
                    switch(entity.direction[1]) {
                        case "left":
                            if (entity.solidArea.intersects(target[i].solidArea)){
                                entity.collisionOn = true;
                                if(entity.solidArea.x > target[i].solidArea.x){
                                    intersection = entity.solidArea.intersection(target[i].solidArea);
                                    if(intersection.height > intersection.width) entity.blockedLeft = true;
                                }
                                if(!index.contains(i)) index.add(i);
                            }
                            break;
                        case "right":
                            if (entity.solidArea.intersects(target[i].solidArea)){
                                entity.collisionOn = true;
                                if(entity.solidArea.x < target[i].solidArea.x){
                                    intersection = entity.solidArea.intersection(target[i].solidArea);
                                    if(intersection.height > intersection.width) entity.blockedRight = true;
                                }
                                if(!index.contains(i)) index.add(i);
                            }
                            break;
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

        Rectangle intersection;

        if(entity.direction[0] != null){
            switch(entity.direction[0]) {
                case "up":
                    if (entity.solidArea.intersects(gp.player.solidArea)){
                        entity.collisionOn = true;
                        if(entity.solidArea.y > gp.player.solidArea.y){
                            intersection = entity.solidArea.intersection(gp.player.solidArea);
                            if(intersection.height < intersection.width) entity.blockedUp = true;
                        }
                    }
                    break;
                case "down":
                    if (entity.solidArea.intersects(gp.player.solidArea)){
                        entity.collisionOn = true;
                        if(entity.solidArea.y < gp.player.solidArea.y){
                            intersection = entity.solidArea.intersection(gp.player.solidArea);
                            if(intersection.height < intersection.width) entity.blockedDown = true;
                        }
                    }
                    break;
                case "left":
                    if (entity.solidArea.intersects(gp.player.solidArea)){
                        entity.collisionOn = true;
                        if(entity.solidArea.x > gp.player.solidArea.x){
                            intersection = entity.solidArea.intersection(gp.player.solidArea);
                            if(intersection.height > intersection.width) entity.blockedLeft = true;
                        }
                    }
                    break;
                case "right":
                    if (entity.solidArea.intersects(gp.player.solidArea)){
                        entity.collisionOn = true;
                        if(entity.solidArea.x < gp.player.solidArea.x){
                            intersection = entity.solidArea.intersection(gp.player.solidArea);
                            if(intersection.height > intersection.width) entity.blockedRight = true;
                        }
                    }
                    break;
            }
        }

        if(entity.direction[1] != null){
            switch(entity.direction[1]) {
                case "left":
                    if (entity.solidArea.intersects(gp.player.solidArea)){
                        entity.collisionOn = true;
                        if(entity.solidArea.x > gp.player.solidArea.x){
                            intersection = entity.solidArea.intersection(gp.player.solidArea);
                            if(intersection.height > intersection.width) entity.blockedLeft = true;
                        }
                    }
                    break;
                case "right":
                    if (entity.solidArea.intersects(gp.player.solidArea)){
                        entity.collisionOn = true;
                        if(entity.solidArea.x < gp.player.solidArea.x){
                            intersection = entity.solidArea.intersection(gp.player.solidArea);
                            if(intersection.height > intersection.width) entity.blockedRight = true;
                        }
                    }
                    break;
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
