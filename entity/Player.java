package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import entity.Abilities.FireBall.FireBall;
import main.GamePannel;
import main.KeyHandler;


public class Player extends Entity {

    private static Player instance;

    public KeyHandler keyHandler;

    //PLAYER SCREEN POSITION
    public final int screenX;
    public final int screenY;

    //PLAYER'S OBJECT
    public int healPotion = 0;

    //INDEX OF THE OBJECT THAT THE PLAYER IS CURRENTLY COLLIDING WITH
    public int objIndex = 999;
    public int npcIndex = 999;
    public int monsterIndex = 999;

    public int ballOn = 0;

    public int positionXActivityOn;
    public int positionYActivityOn;


  
    private Player(GamePannel gp, KeyHandler keyH){
        super(gp);

        this.keyHandler = keyH;

        //PLAYER SCREEN POSITION
        screenX = gp.screenWidth/2 - gp.tileSize/2;
        screenY = gp.worldHeight/2 - gp.tileSize/2;

        solidArea = new Rectangle(8, 24, 16, 12);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        setDefaultValues();
        getPlayerImage();
        swordAttaque(monsterIndex);
    }

    public static Player getInstance(GamePannel gp, KeyHandler keyH) {

        if(instance == null){
            instance = new Player(gp, keyH);
        }

        return new Player(gp, keyH);
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
        attackSpeed = 30;
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
        } else if(keyHandler.downPressed){
            direction = "down";
            worldY = worldY + speed;
        }

        if(keyHandler.leftPressed){
            direction = "left";
            worldX = worldX - speed;
        } else if(keyHandler.rightPressed){
            direction = "right";
            worldX = worldX + speed;
        }

        //CHECK TILE COLLISION
        collisionOn = false;

        //CHECK OBJECT COLLISION
        gp.collisionChecker.checkTile(this); //Player is considered as an entity beacause it extends Entity
        objIndex = gp.collisionChecker.checkObject(this, true);
        pickUpObject(objIndex);

        //CHECK NPC COLLISION
        npcIndex = gp.collisionChecker.checkEntity(this, gp.npc);
        interactNPC(npcIndex);

        //CHECK MONSTER COLLISION
        monsterIndex = gp.collisionChecker.checkEntity(this, gp.monster);
        interactMonster(monsterIndex);


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
                    life += 1; //Create a method for that 
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

    public void swordAttaque(int monsterIndex){

        if(monsterIndex != 999 && gp.keyHandler.sPressed == true && attackDelay > attackSpeed){
            gp.monster[monsterIndex].life -= 1;
            gp.keyHandler.sPressed = false;

            attackDelay = 0;
            //add the image of the player attacking with the sword

        }
    }

    public int fireAttaque(int monsterIndex){

        if(gp.keyHandler.dPressed == true && attackDelay > attackSpeed && ballOn == 0){
            gp.fireBall = new FireBall(gp);
            gp.ability = gp.fireBall;
            gp.keyHandler.dPressed = false;
            attackDelay = 0;
            positionXActivityOn = worldX;
            positionYActivityOn = worldY;
            ballOn = 1;
            
        }
        return ballOn;
    }

    public int abilityDommage(int Index){

        if(Index != 999 && ballOn == 1){
            gp.monster[Index].life -= 1;
            gp.ability = null;
            ballOn = 0;
        }
        return ballOn;
    }
    
    public void interactMonster(int monsterIndex){

        swordAttaque(monsterIndex);
        fireAttaque(monsterIndex);
        if(gp.ability != null){
            abilityDommage(gp.ability.abilityCollisionIndex);
        }
        monsterDommageCounter ++;
        attackDelay ++;
        
            
        if(monsterIndex != 999 && monsterDommageCounter > 30){
            gp.monster[monsterIndex].attackPlayer();
            monsterDommageCounter = 0;
        }
        
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
