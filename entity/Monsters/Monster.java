package entity.Monsters;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Vector;

import entity.Entity;
import entity.Player;
import main.GamePannel;
import object.OBJ_Key;
import object.OBJ_healPotion;
import object.OBJ_manaPotion;

public abstract class Monster extends Entity{
    
    public int monsterSize;

    public boolean noKnockback = false;

    public boolean stopForSpAction = false;

    public Vector<String> objToDrop = new Vector<String>();
    
    
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
    

    // À modifier si on veut implémenter des actions supplémentaires
    public boolean executeSpActions(){
        return false;
    }
    
    // ici, seulement quand le monstre poursuit le joueur, "bufferDirection" enregistre la direction auquel le monstre veut se déplaceer pour aller
    // vers le joueur seulement quand il est bloqué dans cette direction, en sorte de lui redonner cette direction après débloquage de cette direction
    // exemple: Le monstre veut aller à droite pour aller vers le joueur et se trouve face à un mur.
    //          "bufferDirection" enregistre "Right", et après fait en sorte que le monstre ne peut plus se déplacer vers la droite, pour qu'il prenne une autre direction.
    //          Seulement quand le monstre pourra se déplacer vers la droite que "bufferDirection" redonnera la direction
     public boolean setAction() {

        stopForSpAction = executeSpActions();

        if(!stopForSpAction){


            if(attackDelay < attackSpeed) attackDelay++;

            if(!aggravated){
                interactionChecker.noticePlayer(this);
                // si l'entité vient de changer de la non-traque à la traque, tous les blocages de choix de direction sont retirés
                if(aggravated){
                    bufferDirection = null;
                    forceLetGoAll();
                    if(speed != baseSpeed) speed = baseSpeed;
                }
            }

            if (aggravated && (Math.abs(player.worldX - worldX) <= aggroRange*tileSize) && (Math.abs(player.worldY - worldY) <= aggroRange*tileSize)) {

                
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
                    
                    GoToPlayer(player);
                    
                // si l'entité est bloquée contre le joueur, elle ira en sa direction
                } else {
                    bufferDirection = null;
                    direction[0] = interactionChecker.goingTowards(this, player);
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

        registeredDMG = 0;

        return stopForSpAction;

    }



    public void GoToPlayer(Player player) {

        int leftM = worldX + solidAreaDefaultX;
        int rightM = leftM + solidArea.width;
        int upM = worldY + solidAreaDefaultY;
        int downM = upM + solidArea.height;

        int leftP = player.worldX + player.solidAreaDefaultX;
        int rightP = leftP + player.solidArea.width;
        int upP = player.worldY + player.solidAreaDefaultY;
        int downP = upP + player.solidArea.height;

        
        // System.out.println("before 0 direction: " + direction[0]);

        // to follow the player
        if ((leftM/tileSize <= leftP/tileSize && rightP/tileSize <= rightM/tileSize) || (leftP/tileSize <= leftM/tileSize && rightM/tileSize <= rightP/tileSize)) {
            int iterLeft = Math.min(leftM/tileSize, leftP/tileSize);
            int iterHori;
            int iterRight = Math.max(rightM/tileSize, rightP/tileSize);
            int iterVert = Math.min(upM/tileSize, upP/tileSize);
            int iterDown = Math.max(downM/tileSize, downP/tileSize);
            boolean condition = true;
            while(iterVert <= iterDown){
                iterHori = iterLeft;
                while(iterHori <= iterRight){
                    condition = gp.tileM.isCollision(iterHori, iterVert);
                    if(condition){
                        iterVert += iterDown;
                        iterHori += iterRight;
                    }
                    iterHori++;
                }
                iterVert++;
            }

            if(!condition){
                forceLetGoVertical();
                direction[0] = interactionChecker.goingTowards(this, player);
            }

        } else if ((upM/tileSize <= upP/tileSize && downP/tileSize <= downM/tileSize) || (upP/tileSize <= upM/tileSize && downM/tileSize <= downP/tileSize)){
            int iterHori = Math.min(leftM/tileSize, leftP/tileSize);
            int iterRight = Math.max(rightM/tileSize, rightP/tileSize);
            int iterUp = Math.min(upM/tileSize, upP/tileSize);
            int iterVert;
            int iterDown = Math.max(downM/tileSize, downP/tileSize);
            boolean condition = true;
            while(iterHori <= iterRight){
                iterVert = iterUp;
                while(iterVert <= iterDown){
                    condition = gp.tileM.isCollision(iterHori, iterVert);
                    if(condition){
                        iterVert += iterDown;
                        iterHori += iterRight;
                    }
                    iterVert++;
                }
                iterHori++;
            }

            if(!condition){
                
                forceLetGoHorizontal();
                direction[0] = interactionChecker.goingTowards(this, player);
            }
        }
        

        // System.out.println("before 1 direction: " + direction[0]);


        // to follow the player 2
        if(rightM <= leftP + 1){
            if(!stopDirections[3]){
                direction[0] = "right";
                if(speed != baseSpeed) speed = baseSpeed;
            }
        } else if(rightP <= leftM + 1){
            if(!stopDirections[2]){
                direction[0] = "left";
                if(speed != baseSpeed) speed = baseSpeed;
            }
        } else if(downM <= upP + 1){
            if(!stopDirections[1]){
                direction[0] = "down";
                if(speed != baseSpeed) speed = baseSpeed;
            }
        } else if(downP <= upM + 1){
            if(!stopDirections[0]){
                direction[0] = "up";
                if(speed != baseSpeed) speed = baseSpeed;
            }
        }
        

        //System.out.println("before 2 direction: " + direction[0]);


        if(direction[0] == null){

            // normal coordonates
            int resDistanceMini, distanceMini;

            // tile coordonates
            int tilePlayer1, tilePlayer2;
            int resTileDistancePlayer, tileDistancePlayer;
            int tileMonster1, tileMonster2;
            int minGapSize;
            int distanceAllowed;
            int fixCoord = 0;
            int limit1, limit2;
            int numberOfFreeSpaces;
            int tileDestination;
            
            boolean resIsLeftOrUp, isLeftOrUp = true;

            // to follow the player when can't be followed with the previous functions (like in gaps)
            switch(bufferDirection) {
                case "down":
                    fixCoord = (downM/tileSize) + 1;
                case "up":
                    if(bufferDirection == "up") fixCoord = (upM/tileSize) - 1;
                    
                    tilePlayer1 = leftP/tileSize;
                    tilePlayer2 = (rightP/tileSize) + 1;
                    
                    tileMonster1 = leftM/tileSize;
                    tileMonster2 = (rightM/tileSize) + 1;

                    minGapSize = ((solidArea.width+1)/tileSize)+1;
                    distanceAllowed = Math.max(minGapSize, aggroRange);
                    limit1 = Math.max(tileMonster1 - distanceAllowed, 0);
                    limit2 = Math.min(tileMonster2 + distanceAllowed, gp.maxWorldCol - 1);
                    
                    numberOfFreeSpaces = 0;
                    distanceMini = 2*distanceAllowed*tileSize;
                    tileDistancePlayer = 2*minGapSize;
                    for(int i = limit1; i <= limit2; i++){

                        if(!gp.tileM.isCollision(i, fixCoord)){

                            numberOfFreeSpaces++;
                            if(numberOfFreeSpaces >= minGapSize){

                                tileDestination = i+1;
                                resDistanceMini = rightM - (tileDestination*tileSize);
                                resTileDistancePlayer = Math.abs(tileDestination - tilePlayer2);
                                resIsLeftOrUp = true;
                                if(resDistanceMini < 0){
                                    tileDestination -= minGapSize;
                                    resDistanceMini = (tileDestination*tileSize) - leftM;
                                    resTileDistancePlayer = Math.abs(tilePlayer1 - tileDestination);
                                    resIsLeftOrUp = false;
                                }

                                if((resDistanceMini < distanceMini && resDistanceMini >= 0) || (resDistanceMini == distanceMini && resTileDistancePlayer < tileDistancePlayer)){

                                    //System.out.println("vert distanceMini: " + resDistanceMini + "  isLeftOrUp: " + resIsLeftOrUp + "  tileDistancePlayer: " + resTileDistancePlayer);

                                    distanceMini = resDistanceMini;
                                    isLeftOrUp = resIsLeftOrUp;
                                    tileDistancePlayer = resTileDistancePlayer;
                                }
                            }
                        } else numberOfFreeSpaces = 0;

                    }

                    if(distanceMini < distanceAllowed*tileSize){

                        if(isLeftOrUp){
                            decideLetGo("left");
                            if(!stopDirections[2]){
                                direction[0] = "left";
                                speed = baseSpeed;
                                if (distanceMini < speed*2 && speed > 1) speed = 1;
                            }
                        } else {
                            decideLetGo("right");
                            if(!stopDirections[3]){
                                direction[0] = "right";
                                speed = baseSpeed;
                                if (distanceMini < speed*2 && speed > 1) speed = 1;
                            }
                        }

                    }
        
                    break;
                case "right":
                    fixCoord = (rightM/tileSize) + 1;
                case "left":
                    if(bufferDirection == "left") fixCoord = (leftM/tileSize) - 1;
                        
                    tilePlayer1 = upP/tileSize;
                    tilePlayer2 = (downP/tileSize) + 1;

                    tileMonster1 = upM/tileSize;
                    tileMonster2 = (downM/tileSize) + 1;

                    minGapSize = ((solidArea.height+1)/tileSize)+1;
                    distanceAllowed = Math.max(minGapSize, aggroRange);
                    limit1 = Math.max(tileMonster1 - distanceAllowed, 0);
                    limit2 = Math.min(tileMonster2 + distanceAllowed, gp.maxWorldRow - 1);

                    numberOfFreeSpaces = 0;
                    distanceMini = 2*distanceAllowed*tileSize;
                    tileDistancePlayer = 2*minGapSize;
                    for(int i = limit1; i <= limit2; i++){

                        if(!gp.tileM.isCollision(fixCoord, i)){

                            numberOfFreeSpaces++;
                            if(numberOfFreeSpaces >= minGapSize){

                                tileDestination = i+1;
                                resDistanceMini = downM - (tileDestination)*tileSize;
                                resTileDistancePlayer = Math.abs(tileDestination - tilePlayer2);
                                resIsLeftOrUp = true;
                                if(resDistanceMini < 0){
                                    tileDestination -= minGapSize;
                                    resDistanceMini = (tileDestination*tileSize) - upM;
                                    resTileDistancePlayer = Math.abs(tilePlayer1 - tileDestination);
                                    resIsLeftOrUp = false;
                                }

                                if((resDistanceMini < distanceMini && resDistanceMini >= 0) || (resDistanceMini == distanceMini && resTileDistancePlayer < tileDistancePlayer)){

                                    distanceMini = resDistanceMini;
                                    isLeftOrUp = resIsLeftOrUp;
                                    tileDistancePlayer = resTileDistancePlayer;
                                }
                            }
                        } else numberOfFreeSpaces = 0;

                    }

                    if(distanceMini < distanceAllowed*tileSize){

                        //System.out.println("hori distanceMini: " + distanceMini + "  isLeftOrUp: " + isLeftOrUp + "  tileDistancePlayer: " + tileDistancePlayer);

                        if(isLeftOrUp){
                            decideLetGo("up");
                            if(!stopDirections[0]){
                                direction[0] = "up";
                                speed = baseSpeed;
                                if (distanceMini < speed*2 && speed > 1) speed = 1;
                            }
                        } else {
                            decideLetGo("down");
                            if(!stopDirections[1]){
                                direction[0] = "down";
                                speed = baseSpeed;
                                if (distanceMini < speed*2 && speed > 1) speed = 1;
                            }
                        }

                    }

                    break;
            }
        }

        
        // System.out.println("before 3 direction: " + direction[0]);


        if(direction[0] == null) direction[0] = bufferDirection;

        
        // System.out.println("after direction: " + direction[0]);
        // System.out.println("stopDirections  up: " + stopDirections[0] + "  down: " + stopDirections[1] + "  left: " + stopDirections[2] + "  right: " + stopDirections[3]);
        // System.out.println(" ");
    }
    


    public void attackPlayer() {
        if(player.life > 0) player.receiveDmg(damage);
        attackDelay = 0;
    }

    public void receiveDmg(int dmg) {
        aggravated = true;
        registeredDMG = dmg;
        life -= registeredDMG;
        if(life <= 0) isDead = true;
    }

    public void onDeath(){
        player.xp += xp;
        if(gp.objSetter.monsterNumber > 0) gp.objSetter.monsterNumber--;
        DropObject();
    }

    public void DropObject(){

        int posItemX = worldX/tileSize;
        if(worldX % tileSize >= tileSize/2) posItemX++;
        int posItemY = worldY/tileSize;
        if(worldY % tileSize >= tileSize/2) posItemY++;

        for(String obj : objToDrop){
            switch (obj){
                case "key":
                    gp.objSetter.setItem(new OBJ_Key(gp, posItemX, posItemY));
                    break;
                case "healPotion":
                    gp.objSetter.setItem(new OBJ_healPotion(gp, posItemX, posItemY));
                    break;
                case "manaPotion":
                    gp.objSetter.setItem(new OBJ_manaPotion(gp, posItemX, posItemY));
                    break;
            }
        }
    }


    public void paintComponent(Graphics2D g2) {
            
        // Draw the life bar
        int reamningLife = maxLife - life;
        int lifeBarWidth =  tileSize - (tileSize * reamningLife / maxLife);
        int screenX = worldX - player.worldX + player.screenX ;
        int screenY = worldY - player.worldY + player.screenY ;

        g2.setColor(Color.RED);
        g2.fillRect(screenX, screenY - 10, lifeBarWidth, 8);
    }

}
