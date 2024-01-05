package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.Vector;

import javax.imageio.ImageIO;

import main.GamePannel;

public class Entity {

    public GamePannel gp;

    public int worldX, worldY;
    public int speed;
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

    // public Rectangle interactionArea = new Rectangle(-1, -1, 50, 50);   // Est utilisé pour toute interaction "initiée"
    // public int interactionAreaDefaultX, interactionAreaDefaultY;
    // public boolean interactUp = false;
    // public boolean interactDown = false;
    // public boolean interactLeft = false;
    // public boolean interactRight = false;
    // public boolean isInteracting;                   // Non-utilisés pour l'instant
    

    public Vector<String> dialogues = new Vector<String>();
    int dialogueIndex = 0;

    // CHARACTER STATUS
    public int maxLife;
    public int life;


    private class ForcedMovement {

        private String direction;       // only "up", "down", "left" or "right"
        private int distance;           // distance the Entity will be moved per frame, will not be multiplied by tile size

        private boolean timebased;      // if true, the instance will exist until remainingDuration hits 0 
                                        // if false, will exist indefinitely until it gets destroyer through another mean
        private int remainingDuration;  // remaining number of frames before the instance gets destroyed

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

        public void applyForcedMovement(boolean consume){
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
            
            if(consume){
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
        isWithPlayer = false;
    }

    public void setAction(){} 

    public void speak(){}

    public void monsterDead(){}


    public void update(){
        
        setAction();
        monsterDead();

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

    public void verifyMovement(String direction){
        isBlocked = false;
        blockedUp = false;
        blockedDown = false;
        blockedLeft = false;
        blockedRight = false;
        gp.collisionChecker.checkTile(this);
        gp.collisionChecker.checkPlayer(this);

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
        storeMovement[0] = 0;
        storeMovement[1] = 0;
    }

    public void verifyMovement(String[] direction){
        isBlocked = false;
        blockedUp = false;
        blockedDown = false;
        blockedLeft = false;
        blockedRight = false;
        gp.collisionChecker.checkTile(this);
        gp.collisionChecker.checkPlayer(this);

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
        decideRestrain("up");
        decideRestrain("down");
        decideRestrain("left");
        decideRestrain("right");
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

    public void decideLetGoAll (){
        decideLetGo("up");
        decideLetGo("down");
        decideLetGo("left");
        decideLetGo("right");
    }

    public void wander(){

        int numberFree = 0;
        for(int i = 0; i < stopDirections.length; i++){
            if(!stopDirections[i]) numberFree++;
        }
        if(numberFree > 0){
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
                numberFree = 0;
                int i = 0;
                while(numberFree < slotDirections.length){
                    if(stopDirections[i] == false){
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

                Random random = new Random();
                int randomMove = random.nextInt(120);

                switch(slotDirections.length) {
                    case 2:
                        if(randomMove < 60) direction[0] = slotDirections[0];
                        else direction[0] = slotDirections[1];
                        break;
                    case 3:
                        if(randomMove < 40) direction[0] = slotDirections[0];
                        else if(randomMove < 80) direction[0] = slotDirections[1];
                        else direction[0] = slotDirections[2];
                        break;
                    case 4:
                        if(randomMove < 30) direction[0] = slotDirections[0];
                        else if(randomMove < 60) direction[0] = slotDirections[1];
                        else if(randomMove < 90) direction[0] = slotDirections[2];
                        else direction[0] = slotDirections[3];
                        break;
                }
            }
        }
    }


    public BufferedImage getImage(String ImagePath){

        BufferedImage image = null;

        try{
            image = ImageIO.read(getClass().getResourceAsStream(ImagePath));
        }catch(Exception e){
            System.out.println("Error loading image (" + ImagePath + ")");
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
