package main;

import java.awt.Rectangle;

import entity.Entity;

public class InteractionChecker {

    GamePannel gp;

    public InteractionChecker(GamePannel gp) {
        this.gp = gp;
    }

    public int interactPossible(Entity entity, Entity[] target){

        entity.solidArea.x = entity.solidAreaDefaultX + entity.worldX;
        entity.solidArea.y = entity.solidAreaDefaultY + entity.worldY;

        Rectangle zoneDetect;

        switch(entity.facing) {
            case "up":
                zoneDetect = new Rectangle(entity.solidArea.x - 1 + (entity.solidArea.width/2), entity.solidArea.y - (gp.tileSize/2), 2, (gp.tileSize/2));
                break;
            case "down":
                zoneDetect = new Rectangle(entity.solidArea.x - 1 + (entity.solidArea.width/2), entity.solidArea.y + entity.solidArea.height, 2, (gp.tileSize/2));
                break;
            case "left":
                zoneDetect = new Rectangle(entity.solidArea.x - (gp.tileSize/2), entity.solidArea.y - 1 + (entity.solidArea.height/2), (gp.tileSize/2), 2);
                break;
            case "right":
                zoneDetect = new Rectangle(entity.solidArea.x + entity.solidArea.width, entity.solidArea.y - 1 + (entity.solidArea.height/2), (gp.tileSize/2), 2);
                break;
            default:
                return 999;
        }

        int index = 999;
        int distanceToPlayer = gp.tileSize;
        int closestDistance = (gp.tileSize)/2;
        
        for(int i = 0; i < target.length; i++){

            if (closestDistance > 0 && target[i] != null){

                //Get object's solid area position
                target[i].solidArea.x = target[i].worldX + target[i].solidArea.x;
                target[i].solidArea.y = target[i].worldY + target[i].solidArea.y;

                if (zoneDetect.intersects(target[i].solidArea)){
                    switch(entity.facing) {
                        case "up":
                            distanceToPlayer = entity.solidArea.y - target[i].solidArea.y - target[i].solidArea.height;
                            break;
                        case "down":
                            distanceToPlayer = target[i].solidArea.y - entity.solidArea.y - entity.solidArea.height;
                            break;
                        case "left":
                            distanceToPlayer = entity.solidArea.x - target[i].solidArea.x - target[i].solidArea.width;
                            break;
                        case "right":
                            distanceToPlayer = target[i].solidArea.x - entity.solidArea.x - entity.solidArea.width;
                            break;
                    }
                    if  ((closestDistance > distanceToPlayer && distanceToPlayer >= 0) || distanceToPlayer >= closestDistance*2){
                        index = i;
                        closestDistance = distanceToPlayer;
                    }
                }
                
                //Reset object's solid area position
                target[i].solidArea.x = target[i].solidAreaDefaultX;
                target[i].solidArea.y = target[i].solidAreaDefaultY;
            }
        }
        //Reset entity's solid area position
        entity.solidArea.x = entity.solidAreaDefaultX;
        entity.solidArea.y = entity.solidAreaDefaultY;
        
        return index;
    }
}
