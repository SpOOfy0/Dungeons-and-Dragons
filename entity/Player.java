package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import main.GamePannel;
import main.KeyHandler;

public class Player extends Entity {

    public KeyHandler keyHandler;

    //PLAYER SCREEN POSITION
    public final int screenX;
    public final int screenY;

    //PLAYER'S OBJECT
    public int healPotion = 0;

  
    public Player(GamePannel gp, KeyHandler keyH){
        super(gp);

        this.keyHandler = keyH;

        //PLAYER SCREEN POSITION
        screenX = gp.screenWidth/2 - gp.tileSize/2;
        screenY = gp.screenHight/2 - gp.tileSize/2;

        solidArea = new Rectangle(8, 24, 16, 12);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues(){

        //PLAYER WORLD POSITION
        worldX = gp.tileSize * 23; 
        worldY = gp.tileSize * 21; 
        speed = 4;
        direction = "down";

        //PLAYER STATUS
        maxLife = 8;
        life = maxLife;
    }

    public void getPlayerImage(){

            up1 = getImage("/Player/up1.png");
            up2 = getImage("/Player/up2.png");
            down1 = getImage("/Player/down1.png");
            down2 = getImage("/Player/down2.png");
            left1 = getImage("/Player/left1.png");
            left2 = getImage("/Player/left2.png");
            right1 = getImage("/Player/right1.png");
            right2 = getImage("/Player/right2.png");
    }

    public void update(){
        //Todo: seperate methods
        //Todo: switch case
        if(keyHandler.upPressed){
            direction = "up";
            worldY = worldY - speed;
        }
        else if(keyHandler.downPressed){
            direction = "down";
            worldY = worldY + speed;
        }
        else if(keyHandler.leftPressed){
            direction = "left";
            worldX = worldX - speed;
        }
        else if(keyHandler.rightPressed){
            direction = "right";
            worldX = worldX + speed;
        }

        //CHECK TILE COLLISION
        collisionOn = false;

        //CHECK OBJECT COLLISION
        gp.collisionChecker.checkTile(this); //Player is considered as an entity beacause it extends Entity
        int objIndex = gp.collisionChecker.checkObject(this, true);
        pickUpObject(objIndex);

        //CHECK NPC COLLISION
        int npcIndex = gp.collisionChecker.checkEntity(this,gp.npc);
        interactNPC(npcIndex);

        //IF COLLISION IS DETECTED, STOP MOVING THE PLAYER
        if(collisionOn == true){
            if(keyHandler.upPressed == true){
                worldY = worldY + speed;
            }
            else if(keyHandler.downPressed == true){
                worldY = worldY - speed;
            }
            else if(keyHandler.leftPressed == true){
                worldX = worldX + speed;
            }
            else if(keyHandler.rightPressed == true){
                worldX = worldX - speed;
            }
        }

        spriteCounter++;
        if (keyHandler.upPressed == true || keyHandler.downPressed == true || keyHandler.leftPressed == true || keyHandler.rightPressed == true){
            
            if(spriteCounter > 12){
                if(spriteNum == 1){
                    spriteNum = 2;
                }
                else if(spriteNum == 2){
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }   
        }
    }
    
    public void pickUpObject(int objIndex){
        
        if(objIndex != 999){

            String objectName = gp.obj[objIndex].name;
            
            switch(objectName){
                case "healPotion":
                    healPotion++;
                    gp.obj[objIndex] = null;
                    gp.ui.showMessage("You got a heal potion!");
                    break;
            }
        }
    }

    public void interactNPC(int npcIndex){
        
        if(npcIndex != 999){

            if(gp.keyHandler.xPressed == true){
                gp.gameState = gp.dialogueState;
                gp.npc[npcIndex].speak();
            }
        }
        gp.keyHandler.xPressed = false;
    }

    public void draw(Graphics2D g2){

        BufferedImage image = null;

        switch(direction){
            case "up":
                if (spriteNum == 1)
                    image = up1;
                if (spriteNum == 2)
                    image = up2;
                break;
            case "down":
                if (spriteNum == 1)
                    image = down1;
                if (spriteNum == 2)
                    image = down2;
                break;
            case "left":
                if (spriteNum == 1)
                    image = left1;
                if (spriteNum == 2)
                    image = left2;
                break;
            case "right":
                if (spriteNum == 1)
                    image = right1;
                if (spriteNum == 2)
                    image = right2;
                break;                              
        }
        g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);

    }

}
