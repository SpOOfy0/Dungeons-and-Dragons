package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import java.util.Vector;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import main.GamePannel;

public class Entity {

    public GamePannel gp;
    public int tileSize;

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
    public boolean isWantedMovement;
    public boolean didWantedMovement;

    public Rectangle solidArea;   // Est utilisé pour la position et pour toute interaction "subie"
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
    public boolean stopMvmentForSpAction = false;

    public ArrayList<String> dialogues = new ArrayList<String>();
    int dialogueIndex = 0;

    // CHARACTER STATUS
    public int maxLife;
    public int life;

    //CodeExp
    public int xp;
    public int maxXp; //xp needed to level up
    public int level;

    public int damage;

    //CodeMana
    public int mana;
    public int maxMana;

    public int registeredDMG;
    public boolean isDead = false;


    // pour appliquer des mouvements qui ne proviennent pas d'un comportement de base aux entités
    private class ForcedMovement {

        private String direction;       // only "up", "down", "left" or "right", never 'null'
        private int distance;           // distance the Entity will be moved per frame, will not be multiplied by tile size

        private boolean timebased;      // if true, the instance will exist until remainingDuration hits 0 
                                        // if false, will exist indefinitely until it gets destroyer through another mean
        protected int remainingDuration;  // remaining number of frames the movement will be applied on the entity before the instance gets destroyed

        private int id;
        private static int idToShare = 0;


        public ForcedMovement(int duration) {
            if(duration > 0) remainingDuration = duration;
            else remainingDuration = 1;
            timebased = true;
        }

        public ForcedMovement(String inputedDirection, int amountPushed) {

            if(inputedDirection != null && amountPushed != 0){

                direction = inputedDirection;
                distance = amountPushed;
                timebased = false;
                id = idToShare;
                idToShare++;

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


        public void movementOperation(){

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
        }

        public void applyForcedMovement(){

            movementOperation();
            if(timebased) remainingDuration--;
        }

        public void applyForcedMovement(boolean consumeDuration){
            
            movementOperation();
            if(consumeDuration) remainingDuration--;
        }

        public int getID(){
            if(!timebased) return id;
            return -1;
        }

        public boolean hasExpired(){
            return remainingDuration <= 0;
        }
    }


    private class BlockMovement extends ForcedMovement {

        public BlockMovement(int duration){
            super(duration);
        }

        public void movementOperation(){

            if(didWantedMovement) {

                for(int i = 0; i < Entity.this.direction.length; i++){
                    String intendedDirection = Entity.this.direction[i];
                    if(intendedDirection != null){
                        switch(intendedDirection){
                            case "up":
                                storeMovement[1] += speed;
                                break;
                            case "down":
                                storeMovement[1] -= speed;
                                break;
                            case "left":
                                storeMovement[0] += speed;
                                break;
                            case "right":
                                storeMovement[0] -= speed;
                                break;
                        }
                        verifyMovement(intendedDirection);
                    }
                }
            }
        }

        public int getDuration(){
            return remainingDuration;
        }

        public void setDuration(int duration){
            remainingDuration = duration;
        }
    }



    public Vector<ForcedMovement> listForcedMovement = new Vector<ForcedMovement>();

    public void giveForcedMovement(String inputDirection, int inputDistance){
        listForcedMovement.add(new ForcedMovement(inputDirection, inputDistance));
    }

    public void giveForcedMovement(String inputDirection, int inputDistance, int inputDuration){
        listForcedMovement.add(new ForcedMovement(inputDirection, inputDistance, inputDuration));
    }


    private BlockMovement instanceBlockEntity;

    public BlockMovement getInstanceBlockEntity(int duration) {

        if(instanceBlockEntity == null)
            instanceBlockEntity = new BlockMovement(duration);
        
        return instanceBlockEntity;
    }

    public void giveBlockMovement(int inputDuration, boolean replaceDuration){
        // creates new instance if there is no instance of block
        BlockMovement newBlocker = getInstanceBlockEntity(inputDuration);

        // if replaceDuration is true, then, if there is already an instance of BlockMovement, it'll replace the amount of time it'll block the entity
        if(replaceDuration && inputDuration > newBlocker.getDuration()) newBlocker.setDuration(inputDuration);
    }



    public void applyForcedMovement(){

        if(instanceBlockEntity != null){
            instanceBlockEntity.applyForcedMovement();
            if(instanceBlockEntity.hasExpired()) instanceBlockEntity = null;
        }
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
        bufferDirection = null;
        isWithPlayer = false;
        tileSize = gp.tileSize;
        solidArea = new Rectangle(1, 1, tileSize-2, tileSize-2);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }

    // À modifier si on veut rajouter des actions supplémentaires
    public boolean setAction(){
        return false;
    } 

    public void speak(){}

    public void update(){
        
        stopMvmentForSpAction = setAction();

        if(!stopMvmentForSpAction){
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
        }

        isWantedMovement = true;
        didWantedMovement = verifyMovement(direction);
        isWantedMovement = false;

        applyForcedMovement();

        spriteCounting();
    }

    public void spriteCounting(){
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
    public boolean verifyMovement(String direction){

        boolean didMovement = false;

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
        if((storeMovement[0] != 0 || storeMovement[1] != 0) && direction != null){
            switch(direction){
                case "up":
                    if(!blockedUp){
                        worldY += storeMovement[1];
                        didMovement = true;
                    }
                    break;
                case "down":
                    if(!blockedDown){
                        worldY += storeMovement[1];
                        didMovement = true;
                    }
                    break;
                case "left":
                    if(!blockedLeft){
                        worldX += storeMovement[0];
                        didMovement = true;
                    }
                    break;
                case "right":
                    if(!blockedRight){
                        worldX += storeMovement[0];
                        didMovement = true;
                    }
                    break;
            }
        }

        // Réinitialisation des variables de mouvements
        storeMovement[0] = 0;
        storeMovement[1] = 0;

        return didMovement;
    }

    
    // vérifie et applique les mouvements dans "storeMovement" dans les directions entrées comme variable
    // aussi étudie les collisions entre l'entité et son environnement
    public boolean verifyMovement(String[] direction){

        boolean didMovement = false;

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
        if(storeMovement[0] != 0 || storeMovement[1] != 0){
            for(int i = 0; i < direction.length; i++){
                if(direction[i] != null){
                    switch(direction[i]){
                        case "up":
                            if(!blockedUp){
                                worldY += storeMovement[1];
                                didMovement = true;
                            }
                            break;
                        case "down":
                            if(!blockedDown){
                                worldY += storeMovement[1];
                                didMovement = true;
                            }
                            break;
                        case "left":
                            if(!blockedLeft){
                                worldX += storeMovement[0];
                                didMovement = true;
                            }
                            break;
                        case "right":
                            if(!blockedRight){
                                worldX += storeMovement[0];
                                didMovement = true;
                            }
                            break;
                    }
                }
            }   
        }

        // Réinitialisation des variables de mouvements
        storeMovement[0] = 0;
        storeMovement[1] = 0;

        return didMovement;
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

    public void forceLetGoHorizontal(){
        stopDirections[0] = false;
        stopDirections[1] = false;
    }

    public void forceLetGoVertical(){
        stopDirections[2] = false;
        stopDirections[3] = false;
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


    // the next 4 methods return tile coordonates
    public int getLeftTile(){
        return (worldX + solidAreaDefaultX)/tileSize;
    }

    public int getRightTile(){
        return (worldX + solidAreaDefaultX + solidArea.width)/tileSize;
    }

    public int getUpperTile(){
        return (worldY + solidAreaDefaultY)/tileSize;
    }

    public int getLowerTile(){
        return (worldY + solidAreaDefaultY + solidArea.height)/tileSize;
    }

    // the next 4 methods return normal coordonates
    public int getLeftTileBorder(){
        return getLeftTile()*tileSize;
    }

    public int getRightTileBorder(){
        return getRightTile()*tileSize;
    }

    public int getUpperTileBorder(){
        return getUpperTile()*tileSize;
    }

    public int getLowerTileBorder(){
        return getLowerTile()*tileSize;
    }



    public void onDeath() {}



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

        if( worldX + tileSize > gp.player.worldX - gp.player.screenX &&
            worldX - tileSize < gp.player.worldX + gp.player.screenX &&
            worldY + tileSize > gp.player.worldY - gp.player.screenY &&
            worldY - tileSize < gp.player.worldY + gp.player.screenY) { 
            
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
            
            g2.drawImage(image, screenX, screenY, tileSize, tileSize, null);
            //g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
            
        }
    }
    
}
