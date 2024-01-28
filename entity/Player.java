package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import entity.Monsters.Monster;
import main.GamePannel;
import main.KeyHandler;


public class Player extends Entity {

    private static Player instance;

    public KeyHandler keyHandler;

    //PLAYER SCREEN POSITION
    public final int screenX;
    public final int screenY;

    //PLAYER'S TYPE OF MAGIC
    public String typeOfMagic;

    //PLAYER'S OBJECT
    public int healPotion = 0;

    //INDEX OF THE OBJECT THAT THE PLAYER IS CURRENTLY COLLIDING WITH
    public ArrayList<Integer> objIndexes;
    //public ArrayList<Integer> npcIndexes;
    public int npcIndex;
    public ArrayList<Integer> monsterIndexes;

    public boolean didMeleeAttack;
    public int ballOn = 0;
    public int positionXActivityOn;
    public int positionYActivityOn;
    
    public boolean isPreviousStateMove;

    public BufferedImage    moveUp1, moveUp2, moveUp3, moveUp4,
                            moveDown1, moveDown2, moveDown3, moveDown4,
                            moveLeft1, moveLeft2, moveLeft3, moveLeft4, moveLeft5, moveLeft6,
                            moveRight1, moveRight2, moveRight3, moveRight4, moveRight5, moveRight6,
                            attackUp1, attackUp2, attackDown1, attackDown2, attackLeft1, attackLeft2, attackRight1, attackRight2;

    //PLAYER INVENTORY
    public ArrayList<BufferedImage> inventoryImage = new ArrayList<BufferedImage>();
    public ArrayList<Integer> inventoryQuantity = new ArrayList<Integer>();
    public Map<String, Integer> inventory = new HashMap<String, Integer>();

    public int index = 0;


  
    private Player(GamePannel gp, KeyHandler keyH){
        super(gp);

        this.keyHandler = keyH;

        //PLAYER SCREEN POSITION
        screenX = gp.screenWidth/2 - tileSize/2;
        screenY = gp.screenHeight/2 - tileSize/2;

        solidArea = new Rectangle(15, 24, 16, 12);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        setDefaultValues();
        getPlayerImage();
        
        int[] newCoords = gp.tileM.verifyAndCorrectPlacement(worldX/tileSize, worldY/tileSize);

        worldX = newCoords[0] * tileSize;
        worldY = newCoords[1] * tileSize;
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
        worldX = tileSize * 31; 
        worldY = tileSize * 30; 
        initSpeed(4);
        facing = "down";

        //PLAYER STATUS
        maxLife = 8;
        life = maxLife;
        attackSpeed = 30;
        damage = 1;

        typeOfMagic = "fire";

        maxMana = 100;
        mana = maxMana;

        level = 1;
        maxXp = 1000;
    }

    public void getPlayerImage(){
        up1 = getImage("/Player/Hero/Hero_Toggle_Up-0.png");
        up2 = getImage("/Player/Hero/Hero_Toggle_Up-1.png");
        down1 = getImage("/Player/Hero/Hero_Toggle_Down-0.png");
        down2 = getImage("/Player/Hero/Hero_Toggle_Down-1.png");
        left1 = getImage("/Player/Hero/Hero_Toggle_Left-0.png");
        left2 = getImage("/Player/Hero/Hero_Toggle_Left-1.png");
        right1 = getImage("/Player/Hero/Hero_Toggle_Right-0.png");
        right2 = getImage("/Player/Hero/Hero_Toggle_Right-1.png");
        
        moveUp1 = getImage("/Player/Hero/Hero_walk_up-1.png");
        moveUp2 = getImage("/Player/Hero/Hero_walk_up-2.png");
        moveUp3 = getImage("/Player/Hero/Hero_walk_up-3.png");
        moveUp4 = getImage("/Player/Hero/Hero_walk_up-0.png");

        moveDown1 = getImage("/Player/Hero/Hero_walk_down-1.png");
        moveDown2 = getImage("/Player/Hero/Hero_walk_down-2.png");
        moveDown3 = getImage("/Player/Hero/Hero_walk_down-3.png");
        moveDown4 = getImage("/Player/Hero/Hero_walk_down-0.png");
        
        moveLeft1 = getImage("/Player/Hero/Hero_walk_left-1.png");
        moveLeft2 = getImage("/Player/Hero/Hero_walk_left-2.png");
        moveLeft3 = getImage("/Player/Hero/Hero_walk_left-3.png");
        moveLeft4 = getImage("/Player/Hero/Hero_walk_left-4.png");
        moveLeft5 = getImage("/Player/Hero/Hero_walk_left-5.png");
        moveLeft6 = getImage("/Player/Hero/Hero_walk_left-0.png");
        
        moveRight1 = getImage("/Player/Hero/Hero_walk_right-1.png");
        moveRight2 = getImage("/Player/Hero/Hero_walk_right-2.png");
        moveRight3 = getImage("/Player/Hero/Hero_walk_right-3.png");
        moveRight4 = getImage("/Player/Hero/Hero_walk_right-4.png");
        moveRight5 = getImage("/Player/Hero/Hero_walk_right-5.png");
        moveRight6 = getImage("/Player/Hero/Hero_walk_right-0.png");
        
        attackUp1 = getImage("/Player/Hero/Hero_attack_up-0.png");
        attackUp2 = getImage("/Player/Hero/Hero_attack_up-1.png");
        attackDown1 = getImage("/Player/Hero/Hero_attack_down-0.png");
        attackDown2 = getImage("/Player/Hero/Hero_attack_down-1.png");
        attackLeft1 = getImage("/Player/Hero/Hero_attack_left-0.png");
        attackLeft2 = getImage("/Player/Hero/Hero_attack_left-1.png");
        attackRight1 = getImage("/Player/Hero/Hero_attack_right-0.png");
        attackRight2 = getImage("/Player/Hero/Hero_attack_right-1.png");
    }


    public void update(){

        isPreviousStateMove = (direction[0] != null || direction[1] != null);

        didWantedMovement = false;

        direction[0] = null;
        direction[1] = null;


        if(attackDelay < attackSpeed) attackDelay++;
        else didMeleeAttack = baseAttack();
        
        // si le joueur fait une attaque de mêlée, il restera sur place
        if(!didMeleeAttack) {
            if(keyHandler.leftPressed){
                direction[0] = "left";
                storeMovement[0] -= speed;
            } else if(keyHandler.rightPressed){
                direction[0] = "right";
                storeMovement[0] += speed;
            }
    
            if(keyHandler.upPressed){
                direction[1] = "up";
                storeMovement[1] -= speed;
            } else if(keyHandler.downPressed){
                direction[1] = "down";
                storeMovement[1] += speed;
            }
        }

        // fonction pour décider la direction dans laquelle le joueur regardera
        if(direction[0] != null || direction[1] != null){
            if(direction[0] == null) facing = direction[1];
            else if(direction[1] == null) facing = direction[0];
            else if(facing != direction[0] && facing != direction[1]) facing = direction[0];
        }

        // les sorts ne dépendent que sur la direction dans laquelle le joueur fait face
        magicAttack();


        levelUp();
        playerDeath();


        didWantedMovement = verifyMovement(direction);

        applyForcedMovement();

        // s'occuper des intéractions entre le joueur et son environnement
        for(int i = 0; i < objIndexes.size(); i++)
            pickUpObject(objIndexes.get(i) - i);

        npcIndex = gp.interactionChecker.interactPossible(this, gp.npc);
        if(npcIndex != 999) interactNPC(npcIndex);

        for(int i = 0; i < monsterIndexes.size(); i++)
            interactMonster(monsterIndexes.get(i));
        


        if(direction[0] != null || direction[1] != null){
            if(!isPreviousStateMove){
                spriteCounter = 0;
                spriteNum = 1;
            }
            if(spriteCounter < 23) spriteCounter++;
            else spriteCounter = 0;
            // à ce stade, spriteCounter a une valeur entière entre 0 et 23, donnant 24 possible états

            if (facing == "up" || facing == "down") spriteNum = (spriteCounter/6) + 1;
            else spriteNum = (spriteCounter/4) + 1;

        } else if (didMeleeAttack){

            if (spriteCounter < 6){
                spriteCounter++;
                if(spriteCounter <= 1 && spriteNum != 1) spriteNum = 1;
                else if(spriteNum != 2) spriteNum = 2;
            } else {
                spriteCounter = 0;
                spriteNum = 1;
                didMeleeAttack = false;
            }
        } else {
            if(isPreviousStateMove){
                spriteCounter = 0;
                spriteNum = 1;
            }
            if(spriteCounter < 30) spriteCounter++;
            else {
                if (spriteNum == 1) spriteNum = 2;
                else spriteNum = 1;
                spriteCounter = 0;
            }
        }
    }


    public boolean baseAttack(){

        boolean didAttack = false;

        if(gp.keyHandler.sPressed && attackDelay >= attackSpeed){
            didAttack = true;
            gp.keyHandler.sPressed = false;
            attackDelay = 0;

            gp.interactionChecker.meleeHitMonsters(this);
            
            //to reset sprite Counter
            spriteCounter = 0;
        }

        return didAttack;
    }


    public void levelUp(){
        if (xp >= maxXp){
            level++;
            gp.ui.showMessage("     You leveled up!");
            xp -= maxXp;
            maxXp += 250;
            
            // maxMana += 10;
            // mana += 10;
            // damage++;
        }
    }


    // vérifie et applique le mouvement dans "storeMovement" seulement dans la direction entrée comme variable
    // aussi étudie les collisions entre le joueur et son environnement
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
        objIndexes = gp.collisionChecker.checkObject(this, true);
        gp.collisionChecker.checkEntity(this, gp.npc);
        monsterIndexes = gp.collisionChecker.checkEntity(this, gp.monster);

        // Mouvement effectué si la direction est non bloqué
        if(direction != null){
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
    // aussi étudie les collisions entre le joueur et son environnement
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
        objIndexes = gp.collisionChecker.checkObject(this, true);
        gp.collisionChecker.checkEntity(this, gp.npc);
        monsterIndexes = gp.collisionChecker.checkEntity(this, gp.monster);

        // Mouvements effectués individuellement si les directions sont non bloqués
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

        // Réinitialisation des variables de mouvements
        storeMovement[0] = 0;
        storeMovement[1] = 0;

        return didMovement;
    }





    public void pickUpObject(int objIndex){
        
        if(objIndex != 999){
            
            String objName = gp.item.get(objIndex).name;
            
            switch(objName){
                case "healPotion":
                    if (inventory.containsKey(objName)) {
                        //Existing object
                        inventory.put(objName, inventory.get(objName) + 1);
                    } else {
                        //New object
                        inventory.put(objName, 1);
                    }
                    gp.item.remove(objIndex);
                    gp.ui.showMessage("You got a heal potion!");
                    break;
                case "manaPotion":
                    if (inventory.containsKey(objName)) {
                        //Existing object
                        inventory.put(objName, inventory.get(objName) + 1);
                    } else {
                        //New object
                        inventory.put(objName, 1);
                    }
                    gp.item.remove(objIndex);
                    gp.ui.showMessage("You got a mana potion!");
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
                    case "manaPotion":
                        if(mana < maxMana){
                            mana = Math.min(mana + 30, maxMana);
                            inventory.put(objName, inventory.get(objName) - 1);
                            if(inventory.get(objName) == 0){
                                inventory.remove(objName);
                            }
                        }
                        break;
                }
            }
        }
    }

    

    public void interactNPC(int npcIndex){

        NPC studiedNPC = gp.npc.get(npcIndex);
        
        if(gp.keyHandler.xPressed){
            studiedNPC.speak();
        }
        gp.keyHandler.xPressed = false;
    }


    //method to load the magic attack
    public void magicAttack(){

        if (ballOn == 0 && gp.keyHandler.dPressed){
            switch(typeOfMagic){
                case "fire":
                    if(gp.ability != gp.fireBall) gp.ability = gp.fireBall;
                    break;
                case "electric":
                    if(gp.ability != gp.electroBall) gp.ability = gp.electroBall;
                    break;
                //Add other type of magic attack
            }
            if(gp.ability != null && gp.ability.attackDelay >= gp.ability.attackSpeed && gp.ability.mana <= mana){
                gp.keyHandler.dPressed = false;
                gp.ability.attackDelay = 0;
                positionXActivityOn = worldX;
                positionYActivityOn = worldY;
                gp.ability.manaCost();
                ballOn = 1;
            }
        }
    }
    

    public void interactMonster(int monsterIndex){

        Monster studiedMonster = gp.monster.get(monsterIndex);

        if(studiedMonster.isWithPlayer && studiedMonster.attackDelay >= studiedMonster.attackSpeed){
            studiedMonster.attackPlayer();
        }
    }


    public void playerDeath(){
        if(life <= 0) gp.gameState = gp.gameOverState;
    }
    
    

    public void draw(Graphics2D g2){

        BufferedImage image = null;

        if(didMeleeAttack){

            switch(facing){
                case "up":
                    if (spriteNum == 1) image = attackUp1;
                    else image = attackUp2;

                    g2.drawImage(image, screenX, screenY - (tileSize/2), tileSize, 3*tileSize/2, null);
                    break;
                case "down":
                    if (spriteNum == 1) image = attackDown1;
                    else image = attackDown2;

                    g2.drawImage(image, screenX, screenY, tileSize, 3*tileSize/2, null);
                    break;
                case "left":
                    if (spriteNum == 1) image = attackLeft1;
                    else image = attackLeft2;

                    g2.drawImage(image, screenX - (tileSize/2), screenY, 3*tileSize/2, tileSize, null);
                    break;
                case "right":
                    if (spriteNum == 1) image = attackRight1;
                    else image = attackRight2;

                    g2.drawImage(image, screenX, screenY, 3*tileSize/2, tileSize, null);
                    break;
            }

        } else {

            if(direction[0] != null || direction[1] != null){
                switch(facing){
                    case "up":
                        switch(spriteNum){
                            case 1:
                                image = moveUp1;
                                break;
                            case 2:
                                image = moveUp2;
                                break;
                            case 3:
                                image = moveUp3;
                                break;
                            case 4:
                                image = moveUp4;
                                break;
                        }
                        break;
                    case "down":
                        switch(spriteNum){
                            case 1:
                                image = moveDown1;
                                break;
                            case 2:
                                image = moveDown2;
                                break;
                            case 3:
                                image = moveDown3;
                                break;
                            case 4:
                                image = moveDown4;
                                break;
                        }
                        break;
                    case "left":
                        switch(spriteNum){
                            case 1:
                                image = moveLeft1;
                                break;
                            case 2:
                                image = moveLeft2;
                                break;
                            case 3:
                                image = moveLeft3;
                                break;
                            case 4:
                                image = moveLeft4;
                                break;
                            case 5:
                                image = moveLeft5;
                                break;
                            case 6:
                                image = moveLeft6;
                                break;
                        }
                        break;
                    case "right":
                        switch(spriteNum){
                            case 1:
                                image = moveRight1;
                                break;
                            case 2:
                                image = moveRight2;
                                break;
                            case 3:
                                image = moveRight3;
                                break;
                            case 4:
                                image = moveRight4;
                                break;
                            case 5:
                                image = moveRight5;
                                break;
                            case 6:
                                image = moveRight6;
                                break;
                        }
                        break;                              
                }

            } else {
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
            }

            g2.drawImage(image, screenX, screenY, tileSize, tileSize, null);
        }
    }

    
}
