package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import java.util.Vector;

import javax.imageio.ImageIO;

import main.GamePannel;

public class Entity {

    public GamePannel gp;

    public int worldX, worldY;
    public int baseSpeed;
    public int speed;
    public void initSpeed(int inputSpeed){
        baseSpeed = inputSpeed;
        speed = baseSpeed;
    }

    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public String facing;       // direction dans laquelle l'entité regarde
    public String[] direction = new String[2]; // pour les entités qui peuvent se déplacer en diagonale [0: axe horizontal; 1: axe vertical]
    public String bufferDirection;

    public int spriteCounter = 0;
    public int spriteNum = 1;
    public int attackSpeed = 30;
    public int attackDelay = 0;

    public int[] storeMovement = {0, 0};  // "valeurs de déplacement {horizontal, vertical}"

    public Rectangle solidArea = new Rectangle(1, 1, 46, 46);   // Est utilisé pour la position et pour toute interaction "subie"
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collision = false;   // L'entité est affecté par les collisions ou pas?
    public boolean blockedUp;
    public boolean blockedDown;     // Si l'entité bouge, elle est bloquée dans la direction correspondante,
    public boolean blockedLeft;     // sinon, ça permet de détecter si l'Entité est proche de qlqchose qui peut la bloquer dans la direction correspondante
    public boolean blockedRight;    
    public boolean isBlocked;       // Permet de dire si l'Entité bouge dans un direction et se fait blocker dans cette même direction

    public boolean aggravated;
    public int aggroRange;
    public int noticeRange;
    public boolean isWithPlayer;    // Variables pour les non-joueurs, pour interagir avec le joueur
    public boolean[] stopDirections = {false, false, false, false}; // pour arrêter les non-joueurs à aller dans la direction: 0:up  1:down  2:left  3:right
    public int actionCounter = 0;
    public int actionTimer = 120;
    public int impatience = 0;
    public int impatienceTolerance = 30;

    public Vector<String> dialogues = new Vector<String>();
    int dialogueIndex = 0;

    // CHARACTER STATUS
    public int maxLife;
    public int life;


    // pour appliquer des mouvements qui ne proviennent pas d'un comportement de base aux entités
    private class ForcedMovement {

        private String direction;       // only "up", "down", "left" or "right", never 'null'
        private int distance;           // distance the Entity will be moved per frame, will not be multiplied by tile size

        private boolean timebased;      // if true, the instance will exist until remainingDuration hits 0 
                                        // if false, will exist indefinitely until it gets destroyer through another mean
        private int remainingDuration;  // remaining number of frames the movement will be applied on the entity before the instance gets destroyed


        public ForcedMovement(String inputedDirection, int amountPushed) {

            if(inputedDirection != null && amountPushed != 0){

                direction = inputedDirection;
                distance = amountPushed;
                timebased = false;

            } else {

                direction = "up";
                distance = 0;
                timebased = true;

            }
            remainingDuration = 1;
        }


        public ForcedMovement(String inputedDirection, int amountPushed, int duration) {

            if(inputedDirection != null && amountPushed != 0 && duration > 0){

                direction = inputedDirection;
                distance = amountPushed;
                remainingDuration = duration;

            } else {

                direction = "up";
                distance = 0;
                remainingDuration = 1;

            }
            timebased = true;
        }

        public void applyForcedMovement(){

            switch(direction){
                case "up":
                    storeMovement[1] -= distance;
                    break;
                case "down":
                    storeMovement[1] += distance;
                    break;
                case "left":
                    storeMovement[0] -= distance;
                    break;
                case "right":
                    storeMovement[0] += distance;
                    break;
            }
            
            verifyMovement(direction);

            if(timebased){
                remainingDuration--;
            }
        }

        public void applyForcedMovement(boolean consumeDuration){

            switch(direction){
                case "up":
                    storeMovement[1] -= distance;
                    break;
                case "down":
                    storeMovement[1] += distance;
                    break;
                case "left":
                    storeMovement[0] -= distance;
                    break;
                case "right":
                    storeMovement[0] += distance;
                    break;
            }
            
            verifyMovement(direction);
            
            if(consumeDuration){
                remainingDuration--;
            }
        }

        public boolean hasExpired(){
            return remainingDuration <= 0;
        }
    }

    public Vector<ForcedMovement> listForcedMovement = new Vector<ForcedMovement>();

    public void giveForcedMovement(String inputDirection, int inputDistance){
        listForcedMovement.add(new ForcedMovement(inputDirection, inputDistance));
    }

    public void giveForcedMovement(String inputDirection, int inputDistance, int inputDuration){
        listForcedMovement.add(new ForcedMovement(inputDirection, inputDistance, inputDuration));
    }

    public void applyForcedMovement(){
        for(int i = 0; i < listForcedMovement.size(); i++){
            ForcedMovement currentFMovement = listForcedMovement.get(i);
            currentFMovement.applyForcedMovement();
            if(currentFMovement.hasExpired()){
                listForcedMovement.remove(i);
                i--;
            }
        }
    }




    public Entity(GamePannel gp) {
        this.gp = gp;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        bufferDirection = null;
        isWithPlayer = false;
    }


    public void setAction(){} 

    public void speak(){}


    public void update(){
        
        setAction();

        // pour les non-joueurs, "direction[0]" ne doit pas avoir de valeur "null" vu qu'ils se déplacent constamment
        if(direction[0] == null) direction[0] = bufferDirection;

        switch(direction[0]){
            case "up":
                storeMovement[1] -= speed;
                break;
            case "down":
                storeMovement[1] += speed;
                break;
            case "left":
                storeMovement[0] -= speed;
                break;
            case "right":
                storeMovement[0] += speed;
                break;
        }

        // les non-joueurs feront toujours face à la direction où ils veulent aller (sauf exception comme lors d'une "discussion" avec le joueur)
        facing = direction[0];
            
        verifyMovement(direction);

        applyForcedMovement();


        spriteCounter++;
        if(spriteCounter >= 12){
            if(spriteNum == 1){
                spriteNum = 2;
            }
            else if(spriteNum == 2){
                spriteNum = 1;
            }
            spriteCounter = 0;
        }   
    }


    // vérifie et applique le mouvement dans "storeMovement" seulement dans la direction entrée comme variable
    // aussi étudie les collisions entre l'entité et son environnement
    public void verifyMovement(String direction){

        // Initialization des variables
        isBlocked = false;
        blockedUp = false;
        blockedDown = false;
        blockedLeft = false;
        blockedRight = false;

        // Vérifiaction des collisions
        gp.collisionChecker.checkTile(this);
        gp.collisionChecker.checkPlayer(this);

        // Mouvement effectué si la direction est non bloqué
        if(direction != null){
            switch(direction){
                case "up":
                    if(!blockedUp) worldY += storeMovement[1];
                    break;
                case "down":
                    if(!blockedDown) worldY += storeMovement[1];
                    break;
                case "left":
                    if(!blockedLeft) worldX += storeMovement[0];
                    break;
                case "right":
                    if(!blockedRight) worldX += storeMovement[0];
                    break;
            }
        }

        // Réinitialisation des variables de mouvements
        storeMovement[0] = 0;
        storeMovement[1] = 0;
    }

    
    // vérifie et applique les mouvements dans "storeMovement" dans les directions entrées comme variable
    // aussi étudie les collisions entre l'entité et son environnement
    public void verifyMovement(String[] direction){

        // Initialization des variables
        isBlocked = false;
        blockedUp = false;
        blockedDown = false;
        blockedLeft = false;
        blockedRight = false;

        // Vérifiaction des collisions
        gp.collisionChecker.checkTile(this);
        gp.collisionChecker.checkPlayer(this);

        // Mouvements effectués individuellement si les directions sont non bloqués
        for(int i = 0; i < direction.length; i++){
            if(direction[i] != null){
                switch(direction[i]){
                    case "up":
                        if(!blockedUp) worldY += storeMovement[1];
                        break;
                    case "down":
                        if(!blockedDown) worldY += storeMovement[1];
                        break;
                    case "left":
                        if(!blockedLeft) worldX += storeMovement[0];
                        break;
                    case "right":
                        if(!blockedRight) worldX += storeMovement[0];
                        break;
                }
            }
        }

        // Réinitialisation des variables de mouvements
        storeMovement[0] = 0;
        storeMovement[1] = 0;
    }


    public void decideRestrain(String direction){
        switch(direction) {
            case "up":
                if(blockedUp) stopDirections[0] = true;
                break;
            case "down":
                if(blockedDown) stopDirections[1] = true;
                break;
            case "left":
                if(blockedLeft) stopDirections[2] = true;
                break;
            case "right":
                if(blockedRight) stopDirections[3] = true;
                break;
            default:
        }
    }

    public void decideRestrainAll(){
        if(blockedUp) stopDirections[0] = true;
        if(blockedDown) stopDirections[1] = true;
        if(blockedLeft) stopDirections[2] = true;
        if(blockedRight) stopDirections[3] = true;
    }

    public void decideLetGo(String direction){
        switch(direction) {
            case "up":
                if(!blockedUp) stopDirections[0] = false;
                break;
            case "down":
                if(!blockedDown) stopDirections[1] = false;
                break;
            case "left":
                if(!blockedLeft) stopDirections[2] = false;
                break;
            case "right":
                if(!blockedRight) stopDirections[3] = false;
                break;
            default:
        }
    }

    public void decideLetGoAll(){
        if(!blockedUp) stopDirections[0] = false;
        if(!blockedDown) stopDirections[1] = false;
        if(!blockedLeft) stopDirections[2] = false;
        if(!blockedRight) stopDirections[3] = false;
    }

    public void forceLetGoAll(){
        stopDirections[0] = false;
        stopDirections[1] = false;
        stopDirections[2] = false;
        stopDirections[3] = false;
    }

    public void wander(){

        // on récupère combien il y a de directions libres
        int numberFree = 0;
        for(int i = 0; i < stopDirections.length; i++){
            if(!stopDirections[i]) numberFree++;
        }

        // s'il n'y a pas de directions libres, on garde la direction précédente
        if(numberFree > 0){

            // s'il n'y a qu'une direction libre, on cherche la direction correspondante pour l'attribuer comme la prochaine direction à prendre
            if(numberFree == 1){

                boolean notFound = true;
                numberFree = 0;
                while(notFound){
                   notFound = stopDirections[numberFree];
                   numberFree++;
                }
                switch(numberFree) {
                    case 1:
                        direction[0] = "up";
                        break;
                    case 2:
                        direction[0] = "down";
                        break;
                    case 3:
                        direction[0] = "left";
                        break;
                    case 4:
                        direction[0] = "right";
                        break;
                }

            } else {

                String[] slotDirections = new String[numberFree];

                // sinon, on récupère les directions encore libres...
                // (si numberFree == 4, on récupère toutes les directions, pas besoin d'utiliser "slotDirections")
                if(numberFree < 4){
                    numberFree = 0;
                    int i = 0;
                    while(numberFree < slotDirections.length){
                        if(!stopDirections[i]){
                            switch(i) {
                                case 0:
                                    slotDirections[numberFree] = "up";
                                    break;
                                case 1:
                                    slotDirections[numberFree] = "down";
                                    break;
                                case 2:
                                    slotDirections[numberFree] = "left";
                                    break;
                                case 3:
                                    slotDirections[numberFree] = "right";
                                    break;
                            }
                            numberFree++;
                        }
                        i++;
                    }
                }

                // ...et on donne une direction selon la rng de manière équitable entre les directions disponibles
                // ("numberFree" sera aussi grand que le nombre de choix disponibles dans tous les cas)
                Random random = new Random();
                int randomMove = random.nextInt(12);

                switch(numberFree) {
                    case 2:
                        if(randomMove < 6) direction[0] = slotDirections[0];
                        else direction[0] = slotDirections[1];
                        break;
                    case 3:
                        if(randomMove < 4) direction[0] = slotDirections[0];
                        else if(randomMove < 8) direction[0] = slotDirections[1];
                        else direction[0] = slotDirections[2];
                        break;
                    case 4:
                        if(randomMove < 3) direction[0] = "up";
                        else if(randomMove < 6) direction[0] = "down";
                        else if(randomMove < 9) direction[0] = "left";
                        else direction[0] = "right";
                        break;
                }
            }

        }
    }


    public int getLeftTileBorder(){
        int tileSize = gp.tileSize;
        return ((worldX + solidAreaDefaultX)/tileSize)*tileSize;
    }

    public int getRightTileBorder(){
        int tileSize = gp.tileSize;
        return ( ((worldX + solidAreaDefaultX + solidArea.width)/tileSize) + 1)*tileSize;
    }

    public int getUpperTileBorder(){
        int tileSize = gp.tileSize;
        return ((worldY + solidAreaDefaultY)/tileSize)*tileSize;
    }

    public int getLowerTileBorder(){
        int tileSize = gp.tileSize;
        return ( ((worldY + solidAreaDefaultY + solidArea.height)/tileSize) + 1)*tileSize;
    }



    public BufferedImage getImage(String ImagePath){

        BufferedImage image = null;

        try{
            image = ImageIO.read(getClass().getResourceAsStream(ImagePath));
        }catch(IOException e){
            System.out.println(e + " -> Error loading image (" + ImagePath + ")");
        }

        return image;
 
    }

    public void draw(Graphics2D g2){

        BufferedImage image = null;
        int screenX = worldX - gp.player.worldX + gp.player.screenX ;
        int screenY = worldY - gp.player.worldY + gp.player.screenY ;

        if( worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
            worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
            worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
            worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) { 
            
                switch(facing){
                    case "up":
                        if (spriteNum == 1) image = up1;
                        else if (spriteNum == 2) image = up2;
                        break;
                    case "down":
                        if (spriteNum == 1) image = down1;
                        else if (spriteNum == 2) image = down2;
                        break;
                    case "left":
                        if (spriteNum == 1) image = left1;
                        else if (spriteNum == 2) image = left2;
                        break;
                    case "right":
                        if (spriteNum == 1) image = right1;
                        else if (spriteNum == 2) image = right2;
                        break;                              
                }
                
            g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
            
        }
    }
    
}
