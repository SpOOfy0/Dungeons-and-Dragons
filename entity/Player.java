package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

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
    public Vector<Integer> objIndexes;
    //public Vector<Integer> npcIndexes;
    public int npcIndex;
    public Vector<Integer> monsterIndexes;

    public int ballOn = 0;

    public int positionXActivityOn;
    public int positionYActivityOn;

    //PLAYER INVENTORY
    public ArrayList<BufferedImage> inventoryImage = new ArrayList<BufferedImage>();
    public ArrayList<Integer> inventoryQuantity = new ArrayList<Integer>();
    public Map<String, Integer> inventory = new HashMap<String, Integer>();

    public int index = 0;


  
    private Player(GamePannel gp, KeyHandler keyH){
        super(gp);

        this.keyHandler = keyH;

        //PLAYER SCREEN POSITION
        screenX = gp.screenWidth/2 - gp.tileSize/2;
        screenY = gp.screenHeight/2 - gp.tileSize/2;

        solidArea = new Rectangle(15, 24, 16, 12);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        setDefaultValues();
        getPlayerImage();
    }

    public static Player getInstance(GamePannel gp, KeyHandler keyH) {

        if(instance == null){
            instance = new Player(gp, keyH);
        }

        return new Player(gp, keyH);
    }

    public void setDefaultValues(){

        noticeRange = 0;

        //PLAYER WORLD POSITION
        worldX = gp.tileSize * 23; 
        worldY = gp.tileSize * 21; 
        speed = 4;
        facing = "down";

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

        if(attackDelay < attackSpeed) attackDelay++;
        fireAttaque();
        
        direction[0] = null;

        if(keyHandler.leftPressed){
            direction[0] = "left";
            storeMovement[0] -= speed;
        } else if(keyHandler.rightPressed){
            direction[0] = "right";
            storeMovement[0] += speed;
        }
        
        direction[1] = null;

        if(keyHandler.upPressed){
            direction[1] = "up";
            storeMovement[1] -= speed;
        } else if(keyHandler.downPressed){
            direction[1] = "down";
            storeMovement[1] += speed;
        }

        if(direction[0] != null || direction[1] != null){
            if(direction[0] == null) facing = direction[1];
            else if(direction[1] == null) facing = direction[0];
            else if(facing != direction[0] && facing != direction[1]) facing = direction[0];
        }

        verifyMovement(direction);

        applyForcedMovement();

        for(int i = 0; i < objIndexes.size(); i++)
            pickUpObject(objIndexes.get(i));

        npcIndex = gp.interactionChecker.interactPossible(this, gp.npc);
        interactNPC(npcIndex);

        for(int i = 0; i < monsterIndexes.size(); i++)
            interactMonster(monsterIndexes.get(i));

        if(spriteCounter < 12) spriteCounter++;
        else if (keyHandler.upPressed || keyHandler.downPressed || keyHandler.leftPressed || keyHandler.rightPressed){
            if (spriteNum == 1) spriteNum = 2;
            else spriteNum = 1;
            spriteCounter = 0;
        }
    }

    public void verifyMovement(String[] direction){
        isBlocked = false;
        blockedUp = false;
        blockedDown = false;
        blockedLeft = false;
        blockedRight = false;

        //CHECK OBJECT COLLISION
        gp.collisionChecker.checkTile(this); //Player is considered as an entity beacause it extends Entity
        objIndexes = gp.collisionChecker.checkObject(this, true);

        //CHECK NPC COLLISION
        gp.collisionChecker.checkEntity(this, gp.npc);

        //CHECK MONSTER COLLISION
        monsterIndexes = gp.collisionChecker.checkEntity(this, gp.monster);


        //IF COLLISION IS DETECTED, DON'T MOVE THE PLAYER
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
    
    
    //ArrayList version
    /*public void pickUpObject(int objIndex){
        
        if(objIndex != 999){
            
            String objectName = gp.obj[objIndex].name;
            BufferedImage objectImage = gp.obj[objIndex].image;
            int index = indexOfImage(objectImage);
            
            switch(objectName){
                case "healPotion":
                    if(index == -1){
                        System.out.println("New item");
                        inventoryImage.add(objectImage);
                        inventoryQuantity.add(1);
                    }
                    else{
                        System.out.println("Item already in inventory");
                        inventoryQuantity.set(index, inventoryQuantity.get(index) + 1);
                    }
                    gp.obj[objIndex] = null;
                    gp.ui.showMessage("You got a heal potion!");
                    break;
                    // healPotion++;
                    // life += 1; //Create a method for that 
                    // gp.obj[objIndex] = null;
                    // gp.ui.showMessage("You got a heal potion!");
                    // break;
            }
        }
    }*/

    /*private int indexOfImage(BufferedImage image) {
        for (int i = 0; i < inventoryImage.size(); i++) {
            System.out.println("loop");
            //the conditon in the if statement is always false
            if (inventoryImage.get(i).equals(image)) {
                System.out.println("found");
                return i;
            }
        }
        return -1;  // Retourne -1 si l'image n'est pas trouvée
    }*/

    public void pickUpObject(int objIndex){
        
        if(objIndex != 999){
            
            String objName = gp.obj[objIndex].name;
            
            switch(objName){
                case "healPotion":
                    if (inventory.containsKey(objName)) {
                        //Existing object
                        inventory.put(objName, inventory.get(objName) + 1);
                    } else {
                        //New object
                        inventory.put(objName, 1);
                    }
                    gp.obj[objIndex] = null;
                    gp.ui.showMessage("You got a heal potion!");
                    break;
            }            
        }
    }

    public void selectOPbject(int x ,int y, int width, int height){
        
        if (gp.keyHandler.leftPressed){
            if (index == 0) index = 29;
            else index --;
            gp.keyHandler.leftPressed = false; 
        }
        else if (gp.keyHandler.rightPressed){
            if (index == 29) index = 0;
            else index ++;
            gp.keyHandler.rightPressed = false;
        }
        else if (gp.keyHandler.upPressed){
            if (index < 5) index += 25;
            else index -= 5;
            gp.keyHandler.upPressed = false;
        }
        else if (gp.keyHandler.downPressed){
            if (index > 24) index -= 25;
            else index += 5;
            gp.keyHandler.downPressed = false;
        }
    }

    public void useObject(int index){
        if (gp.keyHandler.enterPressed){
            gp.keyHandler.enterPressed = false;

            // Get the name of the object at the index position
            if (index < inventory.size()){

                String objName = inventory.keySet().toArray(new String[0])[index];
                switch(objName){
                    case "healPotion":
                        if(life < maxLife){
                            life += 1;
                            inventory.put(objName, inventory.get(objName) - 1);
                            if(inventory.get(objName) == 0) inventory.remove(objName);
                        }
                        break;
                }
            }
        }
    }

    

    public void interactNPC(int npcIndex){
        
        if(npcIndex != 999 && gp.keyHandler.xPressed){
            gp.npc[npcIndex].speak();
        }
        gp.keyHandler.xPressed = false;
    }

    public int fireAttaque(){

        if(gp.keyHandler.dPressed && attackDelay >= attackSpeed && ballOn == 0){
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

    public void baseAttack(int monsterIndex){

        if(gp.keyHandler.sPressed && attackDelay >= attackSpeed){
            gp.monster[monsterIndex].receiveDmg(1);
            gp.monster[monsterIndex].giveForcedMovement(gp.interactionChecker.awayFromPlayer(gp.monster[monsterIndex]), 2, 30);
            gp.keyHandler.sPressed = false;

            attackDelay = 0;
            //add the image of the player attacking with the sword

        }
    }
    
    public void interactMonster(int monsterIndex){

        baseAttack(monsterIndex);

        if(gp.monster[monsterIndex].attackDelay >= gp.monster[monsterIndex].attackSpeed){
            gp.monster[monsterIndex].attackPlayer();
        }
    }

    // public int abilityDommage(int Index){

    //     if(Index != 999 && ballOn == 1){
    //         gp.monster[Index].life -= 1;
    //         gp.ability = null;
    //         ballOn = 0;
    //     }
    //     return ballOn;
    // }
    

    public void draw(Graphics2D g2){

        BufferedImage image = null;

        switch(facing){
            case "up":
                if (spriteNum == 1) image = up1;
                else image = up2;
                break;
            case "down":
                if (spriteNum == 1) image = down1;
                else image = down2;
                break;
            case "left":
                if (spriteNum == 1) image = left1;
                else image = left2;
                break;
            case "right":
                if (spriteNum == 1) image = right1;
                else image = right2;
                break;                              
        }
        g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
    }
    
}
