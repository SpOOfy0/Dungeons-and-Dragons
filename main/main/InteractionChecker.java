package main;

import java.awt.Rectangle;
import java.util.Vector;

import entity.Entity;
import entity.Player;
import entity.Monsters.Monster;


public class InteractionChecker {

    GamePannel gp;

    public InteractionChecker(GamePannel gp) {
        this.gp = gp;
    }





    public String goingTowards(Entity entity, Entity target){

        int entityMiddleX = entity.worldX + entity.solidAreaDefaultX + entity.solidArea.width/2;
        int entityMiddleY = entity.worldY + entity.solidAreaDefaultY + entity.solidArea.height/2;
        int targetMiddleX = target.worldX + target.solidAreaDefaultX + target.solidArea.width/2;
        int targetMiddleY = target.worldY + target.solidAreaDefaultY + target.solidArea.height/2;

        String valueToReturn = null;

        if (entityMiddleX < targetMiddleX) valueToReturn = "right";
        else if (targetMiddleX < entityMiddleX) valueToReturn = "left";
        
        if(Math.abs(entityMiddleX - targetMiddleX) <= Math.abs(entityMiddleY - targetMiddleY)){
            if (entityMiddleY < targetMiddleY) valueToReturn = "down";
            else if (targetMiddleY < entityMiddleY) valueToReturn = "up";
        }

        return valueToReturn;
    }
    
    
    public String goingAwayFrom(Entity entity, Entity target){

        int entityMiddleX = entity.worldX + entity.solidAreaDefaultX + entity.solidArea.width/2;
        int entityMiddleY = entity.worldY + entity.solidAreaDefaultY + entity.solidArea.height/2;
        int targetMiddleX = target.worldX + target.solidAreaDefaultX + target.solidArea.width/2;
        int targetMiddleY = target.worldY + target.solidAreaDefaultY + target.solidArea.height/2;

        String valueToReturn = null;

        if(entityMiddleX < targetMiddleX) valueToReturn = "left";
        else if (targetMiddleX < entityMiddleX) valueToReturn = "right";
        
        if(Math.abs(entityMiddleX - targetMiddleX) <= Math.abs(entityMiddleY - targetMiddleY)){
            if (entityMiddleY < targetMiddleY) valueToReturn = "up";
            else if (targetMiddleY < entityMiddleY) valueToReturn = "down";
        }

        return valueToReturn;
    }





    public int interactPossible(Entity entity, Vector<? extends Entity> target){

        entity.solidArea.x = entity.solidAreaDefaultX + entity.worldX;
        entity.solidArea.y = entity.solidAreaDefaultY + entity.worldY;

        int tileSize = gp.tileSize;

        // Création de la zone d'interaction, un rectangle avec la moitié d'une case en longueur et 2 blocs en largeur, toujours en face du joueur
        Rectangle zoneDetect;

        switch(entity.facing) {
            case "up":
                zoneDetect = new Rectangle(entity.solidArea.x - 1 + (entity.solidArea.width/2), entity.solidArea.y - (tileSize/2), 2, (tileSize/2));
                break;
            case "down":
                zoneDetect = new Rectangle(entity.solidArea.x - 1 + (entity.solidArea.width/2), entity.solidArea.y + entity.solidArea.height, 2, (tileSize/2));
                break;
            case "left":
                zoneDetect = new Rectangle(entity.solidArea.x - (tileSize/2), entity.solidArea.y - 1 + (entity.solidArea.height/2), (tileSize/2), 2);
                break;
            case "right":
                zoneDetect = new Rectangle(entity.solidArea.x + entity.solidArea.width, entity.solidArea.y - 1 + (entity.solidArea.height/2), (tileSize/2), 2);
                break;
            default:
                return 999;
        }

        // Retourne l'index de l'entité du tableau le plus proche du joueur étant détecté par la zone d'interaction créée précédement
        int index = 999;
        int distanceInBetween = tileSize;
        int closestDistance = (tileSize)/2;
        
        int i = 0;
        for(Entity studiedTarget : target){

            if (closestDistance > 0 && studiedTarget != null){

                //Get object's solid area position
                studiedTarget.solidArea.x = studiedTarget.worldX + studiedTarget.solidArea.x;
                studiedTarget.solidArea.y = studiedTarget.worldY + studiedTarget.solidArea.y;

                if (zoneDetect.intersects(studiedTarget.solidArea)){
                    switch(entity.facing) {
                        case "up":
                            distanceInBetween = entity.solidArea.y - studiedTarget.solidArea.y - studiedTarget.solidArea.height;
                            break;
                        case "down":
                            distanceInBetween = studiedTarget.solidArea.y - entity.solidArea.y - entity.solidArea.height;
                            break;
                        case "left":
                            distanceInBetween = entity.solidArea.x - studiedTarget.solidArea.x - studiedTarget.solidArea.width;
                            break;
                        case "right":
                            distanceInBetween = studiedTarget.solidArea.x - entity.solidArea.x - entity.solidArea.width;
                            break;
                    }
                    if  ((closestDistance > distanceInBetween && distanceInBetween >= 0) || distanceInBetween >= closestDistance*2){
                        index = i;
                        closestDistance = distanceInBetween;
                    }
                }
                
                //Reset object's solid area position
                studiedTarget.solidArea.x = studiedTarget.solidAreaDefaultX;
                studiedTarget.solidArea.y = studiedTarget.solidAreaDefaultY;
            }
            i++;
        }

        //Reset entity's solid area position
        entity.solidArea.x = entity.solidAreaDefaultX;
        entity.solidArea.y = entity.solidAreaDefaultY;
        
        return index;
    }





    public void noticePlayer(Entity entity){

        int cornerIndex = 0;        // corners of detection -->  0:(up, right)   1:(up, left)   2:(down, left)   3:(down, right)
        boolean isCorner = true;    // if "isCorner" is false,   0:up            1:left         2:down           3:right
        Rectangle detectZone1 = new Rectangle(0,0,0,0), detectZone2 = new Rectangle(0,0,0,0);
        boolean isDetected = false;

        entity.solidArea.x = entity.solidAreaDefaultX + entity.worldX;
        entity.solidArea.y = entity.solidAreaDefaultY + entity.worldY;

        int entityMiddleX = entity.solidArea.x + entity.solidArea.width/2;
        int entityMiddleY = entity.solidArea.y + entity.solidArea.height/2;

        int tileSize = gp.tileSize;

        int range = entity.noticeRange * tileSize;
        int leftLimitRange = entity.solidArea.x - range;
        int upLimitRange = entity.solidArea.y - range;


        // Création de la zone d'observation
        switch(entity.facing) {
            case "up":
                detectZone1 = new Rectangle(entityMiddleX,                    upLimitRange,
                                            entity.solidArea.width/2 + range, entity.solidArea.height/2 + range);
                detectZone2 = new Rectangle(leftLimitRange,                   upLimitRange,
                                            entity.solidArea.width/2 + range, entity.solidArea.height/2 + range);
                break;
            case "left":
                detectZone1 = new Rectangle(leftLimitRange,                   upLimitRange,
                                            entity.solidArea.width/2 + range, entity.solidArea.height/2 + range);
                detectZone2 = new Rectangle(leftLimitRange,                   entityMiddleY,
                                            entity.solidArea.width/2 + range, entity.solidArea.height/2 + range);
                cornerIndex = 1;
                break;
            case "down":
                detectZone1 = new Rectangle(leftLimitRange,                   entityMiddleY,
                                            entity.solidArea.width/2 + range, entity.solidArea.height/2 + range);
                detectZone2 = new Rectangle(entityMiddleX,                    entityMiddleY,
                                            entity.solidArea.width/2 + range, entity.solidArea.height/2 + range);
                cornerIndex = 2;
                break;
            case "right":
                detectZone1 = new Rectangle(entityMiddleX,                    entityMiddleY,
                                            entity.solidArea.width/2 + range, entity.solidArea.height/2 + range);
                detectZone2 = new Rectangle(entityMiddleX,                    upLimitRange,
                                            entity.solidArea.width/2 + range, entity.solidArea.height/2 + range);
                cornerIndex = 3;
                break;
        }


        //Get object's solid area position
        gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
        gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;

        // Vérification si le joueur est présent dans la zone d'observation
        if (detectZone1.intersects(gp.player.solidArea)){

            if (detectZone2.intersects(gp.player.solidArea)) isCorner = false;

            isDetected = true;

        } else if (detectZone2.intersects(gp.player.solidArea)){

            isDetected = true;
            cornerIndex = (cornerIndex+1)%4;
        }



        // Si le joueur est présent dans la zone d'observation, vérifier s'il n'est pas caché
        if (isDetected){
            
            int tileFocus;
            boolean repeatWhile = true;
            entity.aggravated = true;
            
            // Vérification pour un coin
            if (isCorner) {

                // Initialization des variables pour les iterations
                int iterX = 0, endIterX = 0, startIterY = 0, iterY, endIterY = 0;
                
                switch(cornerIndex){
                    case 0:
                        iterX = entityMiddleX / tileSize;
                        endIterX = gp.player.solidArea.x / tileSize;
                        startIterY = (gp.player.solidArea.y + gp.player.solidArea.height) / tileSize;
                        endIterY = entityMiddleY / tileSize;
                        break;
                    case 1:
                        iterX = (gp.player.solidArea.x + gp.player.solidArea.width) / tileSize;
                        endIterX = entityMiddleX / tileSize;
                        startIterY = (gp.player.solidArea.y + gp.player.solidArea.height) / tileSize;
                        endIterY = entityMiddleY / tileSize;
                        break;
                    case 2:
                        iterX = (gp.player.solidArea.x + gp.player.solidArea.width) / tileSize;
                        endIterX = entityMiddleX / tileSize;
                        startIterY = entityMiddleY / tileSize;
                        endIterY = gp.player.solidArea.y / tileSize;
                        break;
                    case 3:
                        iterX = entityMiddleX / tileSize;
                        endIterX = gp.player.solidArea.x / tileSize;
                        startIterY = entityMiddleY / tileSize;
                        endIterY = gp.player.solidArea.y / tileSize;
                        break;
                }
                iterY = startIterY;

                // Itérations pour trouver une case opaque dans le champ de vision
                while (repeatWhile) {
                    tileFocus = gp.tileM.mapTileNum[iterX][iterY];
                    if(gp.tileM.tile[tileFocus].opaque){
                        entity.aggravated = false;
                        repeatWhile = false;
                    }

                    if (repeatWhile){
                        iterY++;
                        if (iterY > endIterY){
                            iterY = startIterY;
                            iterX++;
                        }
                        if (iterX > endIterX) repeatWhile = false;
                    }
                }

            // Vérification si le joueur est en face 
            } else {
                
                // Initialization des variables pour les iterations
                int alignedIterStart = 0, alignedIter, alignedIterEnd = 0, acrossIter = 0, acrossIterEnd = 0;
                switch(cornerIndex){
                    case 0:
                        alignedIterStart = (gp.player.solidArea.y + gp.player.solidArea.height) / tileSize;
                        alignedIterEnd = entityMiddleY / tileSize;
                        acrossIter = gp.player.solidArea.x / tileSize;
                        acrossIterEnd = (gp.player.solidArea.x + gp.player.solidArea.width) / tileSize;
                        break;
                    case 1:
                        alignedIterStart = (gp.player.solidArea.x + gp.player.solidArea.width) / tileSize;
                        alignedIterEnd = entityMiddleX / tileSize;
                        acrossIter = gp.player.solidArea.y / tileSize;
                        acrossIterEnd = (gp.player.solidArea.y + gp.player.solidArea.height) / tileSize;
                        break;
                    case 2:
                        alignedIterStart = entityMiddleY / tileSize;
                        alignedIterEnd = gp.player.solidArea.y / tileSize;
                        acrossIter = gp.player.solidArea.x / tileSize;
                        acrossIterEnd = (gp.player.solidArea.x + gp.player.solidArea.width) / tileSize;
                        break;
                    case 3:
                        alignedIterStart = entityMiddleX / tileSize;
                        alignedIterEnd = gp.player.solidArea.x / tileSize;
                        acrossIter = gp.player.solidArea.y / tileSize;
                        acrossIterEnd = (gp.player.solidArea.y + gp.player.solidArea.height) / tileSize;
                        break;
                }

                // Iterations doubles pour vérifier s'il y a entre le joueur et l'entité au moins une case opaque par colonne ou ligne selon la direction observée                
                while (repeatWhile) {

                    alignedIter = alignedIterStart;

                    boolean repeatWhile2 = true;
                    while (repeatWhile2){

                        if (cornerIndex%2 == 0) tileFocus = gp.tileM.mapTileNum[acrossIter][alignedIter];
                        else                    tileFocus = gp.tileM.mapTileNum[alignedIter][acrossIter];

                        if(gp.tileM.tile[tileFocus].opaque) repeatWhile2 = false;
                        else {
                            alignedIter++;
                            if (alignedIter > alignedIterEnd) repeatWhile2 = false;
                        }
                    }

                    if (alignedIter > alignedIterEnd) repeatWhile = false;
                    else {
                        acrossIter++;
                        if(acrossIter > acrossIterEnd){
                            entity.aggravated = false;
                            repeatWhile = false;
                        }
                    } 
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

    public void meleeHitMonsters(Player player) {

        // Création de la zone d'interaction, un rectangle avec 41 de longueur dans la direction le joueur fait face et 54 de largeur
        Rectangle zoneDetect = null;

        switch(player.facing) {
            case "up":
                zoneDetect = new Rectangle(player.worldX - 2, player.worldY - 20, 52, 41);
                break;
            case "down":
                zoneDetect = new Rectangle(player.worldX - 2, player.worldY + 27, 52, 41);
                break;
            case "left":
                zoneDetect = new Rectangle(player.worldX - 20, player.worldY - 2, 41, 52);
                break;
            case "right":
                zoneDetect = new Rectangle(player.worldX + 27, player.worldY - 2, 41, 52);
                break;
        }

        if(zoneDetect != null){
            for(Monster studiedMonster : gp.monster){

                //Get object's solid area position
                studiedMonster.solidArea.x = studiedMonster.worldX + studiedMonster.solidArea.x;
                studiedMonster.solidArea.y = studiedMonster.worldY + studiedMonster.solidArea.y;

                if (zoneDetect.intersects(studiedMonster.solidArea)){
                    studiedMonster.receiveDmg(player.damage);
                    //System.out.println(goingAwayFrom(studiedMonster, player));
                    if(!studiedMonster.noKnockback) studiedMonster.giveForcedMovement(goingAwayFrom(studiedMonster, player), 2 + (player.damage/4), 15);
                }
                
                //Reset object's solid area position
                studiedMonster.solidArea.x = studiedMonster.solidAreaDefaultX;
                studiedMonster.solidArea.y = studiedMonster.solidAreaDefaultY;
            }
            
        }

    }

}
