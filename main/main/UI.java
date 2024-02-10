package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.DecimalFormat;

import javax.imageio.ImageIO;

import entity.Player;


public class UI {

    private static UI instance;
    GamePannel gp;
    Graphics2D g2;
    Player player;
    Font arial_40;
    BufferedImage fullHeart, halfHeart, emptyHeart; 
    BufferedImage HealPotionImage, ManaPotionImage, KeyImage;
    BufferedImage chooseCharacter , gameOver, theEnd, title, arrow, exit, start, quit, retry, mage, fighter, allRounded;
    
    public int tileSize;
    public int screenWidth;
    public int screenHeight;

    public boolean messageOn = false;
    public String message = "";
    public int messageCounter = 0;

    public String currentDialogue = "";
    public String playerType = "";

    double timer = 0;
    DecimalFormat df = new DecimalFormat("#0.00");

    int gameStartCommand = 0, gameOverCommand = 0;


    boolean showStatus = false;
    boolean isHoldingN = false;


    private UI(GamePannel GP) {
        gp = GP;
        tileSize = gp.tileSize;
        screenWidth = gp.screenWidth;
        screenHeight = gp.screenHeight;

        arial_40 = new Font("Arial", Font.PLAIN, 40);
        
        HealPotionImage = gp.healPotion.image;
        ManaPotionImage = gp.manaPotion.image;
        KeyImage = gp.key.image;

        // CREAT HUD OBJET 
        fullHeart = gp.heart.image;
        halfHeart = gp.heart.image2;
        emptyHeart = gp.heart.image3;

        getThisUiImage();
    }

    public static UI getInstance(GamePannel gp) {

        if (instance == null) {
            instance = new UI(gp);
        }

        return instance;
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

    public void getThisUiImage() {
        chooseCharacter = getImage("/Objects/GameUI/ChooseYourCharacter.png");
        theEnd = getImage("/Objects/GameUI/TheEnd.png");
        gameOver = getImage("/Objects/GameUI/GameOver.png");
        title = getImage("/Objects/GameUI/Title.png");
        arrow = getImage("/Objects/GameUI/Command/Arrow.png");
        exit = getImage("/Objects/GameUI/Command/Exit.png");
        start = getImage("/Objects/GameUI/Command/Start.png");
        quit = getImage("/Objects/GameUI/Command/Quit.png");
        retry = getImage("/Objects/GameUI/Command/Retry.png");
        mage = getImage("/Objects/GameUI/Characters/Mage.png");
        fighter = getImage("/Objects/GameUI/Characters/Fighter.png");
        allRounded = getImage("/Objects/GameUI/Characters/All_Rounded.png");
    }

    public double setTimer(double time){

        time += (double)1/60;
        return time;
    }

    public void showMessage(String text){

        message = text;
        messageOn = true;
    }

    public void drawMessage(){
        if(messageOn){
            g2.setFont(new Font("Arial", Font.PLAIN, 20));
            g2.setColor(Color.white);
            g2.drawString(message, screenWidth / 2 - tileSize * 2 , screenHeight / 4 - tileSize);
            messageCounter++;
        
            if(messageCounter > 100){
                messageOn = false;
                messageCounter = 0;
            }   
        }
    }

    

    public int getXScreenCenterText(String text){
        int textLength = g2.getFontMetrics().stringWidth(text);
        int x = (screenWidth - textLength)/2;
        return x;
    }

    public void drawPauseScreen() {
        
        String text = "PAUSED";
        int x = getXScreenCenterText(text);
        int y = gp.worldHeight/2;
        
        g2.drawString(text, x, y);
    }

    public void drawDialogueScreen(){
        int x = tileSize*2;
        int y = tileSize/2;
        int width = screenWidth - tileSize*4;
        int height = tileSize*4;

        drawSubWindow(x, y, width, height);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN,25F));
        x += tileSize;
        y += tileSize;

        for(String line : currentDialogue.split("\n")){
            g2.drawString(line, x, y);
            y += 40;
        }

        if(gp.keyHandler.enterPressed){
            gp.keyHandler.enterPressed = false;
            gp.interactingNPC.getNextDialogue();
        }
    }


    public void drawSubWindow(int x, int y, int width, int height){

        Color color = new Color(0, 0, 0, 200);
        g2.setColor(color);
        g2.fillRoundRect(x, y, width, height, 20, 20);
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x, y, width, height, 20, 20);
    }


    public void drawPlayerLife(){

       //player.life = 5; //for testing

        int x = tileSize/2;
        int y = tileSize/2;
        int life = player.life;

        // DRAW PLAYER MAXLIFE
        for(int i = 0; i < player.maxLife/2; i++){
            g2.drawImage(emptyHeart, x, y, tileSize, tileSize, null);
            x += tileSize;
        }

        //RESET
        x = tileSize/2;

        // DRAW PLAYER LIFE
        while (life >= 2){
            g2.drawImage(fullHeart, x, y, tileSize, tileSize, null);
            life -= 2;
            x += tileSize;
        }

        if(life == 1){
            g2.drawImage(halfHeart, x, y, tileSize, tileSize, null);
        }
    }

    public void drawPlayerXp() {
        if (player.maxXp > player.xp){
            int maxBarWidth = 200;
            double percentage = (double) player.xp / player.maxXp;
            double xpBarWidth = maxBarWidth * percentage;
            int screenX = tileSize / 2;
            int screenY = tileSize * 3 - 35;

            g2.setColor(Color.WHITE);
            g2.fillRect(screenX, screenY - 10, (int) xpBarWidth, 8);
            colorBorder(screenX, screenY - 10, maxBarWidth, 8);
        }
    }

    public void drawPlayerMana() {
        if (player.maxMana >= player.mana){
            int maxBarWidth = 200;
            double percentage = (double) player.mana / player.maxMana;
            double manaBarWidth = maxBarWidth * percentage;
            int screenX = tileSize / 2;
            int screenY = tileSize * 2 - 10;

            g2.setColor(Color.BLUE);
            g2.fillRect(screenX, screenY - 10, (int) manaBarWidth, 8);
            colorBorder(screenX, screenY - 10, maxBarWidth, 8);
        }
    }


    public void drawInventory() {
        int startX = screenWidth / 2 + tileSize;
        int startY = screenHeight / 2 - tileSize * 4;
        int itemsPerRow = 5;
        int spacing = 10;

        // Taille d'une case
        int cellSize = tileSize + spacing;
    
        // Parcourir les emplacements de l'inventaire
        int itemIndex = 0;
        for (int i = 0; i < 30; i++) {
            int row = itemIndex / itemsPerRow;
            int col = itemIndex % itemsPerRow;
    
            // Calculez la position de l'objet dans la grille
            int x = startX + col * cellSize;
            int y = startY + row * cellSize;
    
            // Colorer le fond de la case où notre curseur se trouve
            g2.setColor(new Color(100, 100, 100));
            g2.fillRect(x, y, tileSize, tileSize);
            
            if (player.index == i){
                player.selectOPbject(x, y,tileSize, tileSize);
                player.useObject(i);
                colorBorder(x, y, tileSize, tileSize);
            }
            // Si l'inventaire contient un objet à cet emplacement, dessinez-le
            if (itemIndex < player.inventory.size()) {
                String objName = player.inventory.keySet().toArray(new String[0])[itemIndex];

                BufferedImage image;
                int count;
                String countInString;

                switch (objName) {
                    case "healPotion":
                        image = HealPotionImage;
                        count = player.inventory.get(objName);
                        countInString = String.valueOf(count);
                        g2.drawImage(image, x, y, tileSize, tileSize, null);
                        //Add count
                        g2.setFont(new Font("Arial", Font.PLAIN, tileSize / 4));
                        g2.setColor(Color.white);
                        g2.drawString(countInString, x + 5, y + 15);
                        break;

                    case "manaPotion":
                        image = ManaPotionImage;
                        count = player.inventory.get(objName);
                        countInString = String.valueOf(count);
                        g2.drawImage(image, x, y, tileSize, tileSize, null);
                        //Add count
                        g2.setFont(new Font("Arial", Font.PLAIN, tileSize / 4));
                        g2.setColor(Color.white);
                        g2.drawString(countInString, x + 5, y + 15);
                        break;
                    
                    case "key":
                        image = KeyImage;
                        count = player.inventory.get(objName);
                        countInString = String.valueOf(count);
                        g2.drawImage(image, x, y, tileSize, tileSize, null);
                        //Add count
                        g2.setFont(new Font("Arial", Font.PLAIN, tileSize / 4));
                        g2.setColor(Color.white);
                        g2.drawString(countInString, x + 5, y + 15);
                        break;
    
                    // Add other cases for objName
    
                    default:
                        break;
                }
            }
    
            // Incrémentez l'index d'emplacement
            itemIndex++;
        }
    }

    public void drawPlayerStatus(){

        if (!isHoldingN){
            if (gp.keyHandler.nPressed){
                isHoldingN = true;
                if(showStatus) showStatus = false;
                else showStatus = true;
            }
        } else {
            if (!gp.keyHandler.nPressed) isHoldingN = false;
        }

        if (showStatus){
            int x = screenWidth / 8 - tileSize;
            int y = screenHeight / 2 - tileSize * 4;
            int width = screenWidth / 2 - tileSize * 2;
            int height = screenHeight - tileSize * 4;
            int arcWidth = 20;
            int arcHeight = 20; 

            g2.setColor(new Color(100, 100, 100, 200));
            g2.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
            g2.setColor(Color.white);
            g2.drawRoundRect(x, y, width, height, arcWidth, arcHeight);

            int lineHeight = g2.getFontMetrics().getHeight();
            int textX = x + 20; 
            int valueX;    // The x-coordinate of the value text
            int textY = y + 40; 

            // Add lines of text
            String[] lines = {
                "Level: " , "" + player.level,
                "XP: " , "" + player.xp + "/" + player.maxXp,
                "Life: " , "" + player.life + "/" + player.maxLife,
                "Strengh: " , "" + player.damage,
                "Mana:" , "" + player.mana + "/" + player.maxMana,
                "Fireball Power: " , "" + gp.fireBall.damage,
                "Electroball Power: " , "" + gp.electroBall.damage,
                "Attack Speed: " , "" + player.attackSpeed,
                // Add other lines of text
            };

            // Draw each line of text
            for (int i = 0; i < lines.length; i += 2) {
                g2.setFont(new Font("Arial", Font.PLAIN, 20));
                g2.setColor(Color.white);
                g2.drawString(lines[i], textX, textY);
                valueX = getXForAlightToRightOfText(lines[i + 1], textX + width, g2);
                g2.drawString(lines[i + 1], valueX, textY);
                textY += lineHeight; 
            }
        }
    }

    public int getXForAlightToRightOfText(String text, int tailX, Graphics2D graphics2D) {
        int length = (int) graphics2D.getFontMetrics().getStringBounds(text, graphics2D).getWidth();
        return tailX - length - tileSize / 2;
    }

    public static int getXForCenterOfText(String text, GamePannel gamePanel, Graphics2D graphics2D) {
        int length = (int) graphics2D.getFontMetrics().getStringBounds(text, graphics2D).getWidth();
        return gamePanel.screenWidth / 2 - length / 2;
    }

    public static int getXForCenterOfImage(BufferedImage image, GamePannel gamePanel) {
        int imageWidth = image.getWidth();
        return gamePanel.screenWidth / 2 - imageWidth / 2;
    }

    public int selectedCommand(int commandNumber ,int maxCommandNumber){
        if(gp.keyHandler.downPressed){
            commandNumber++;
            gp.keyHandler.downPressed = false;
        }
        if(gp.keyHandler.upPressed){
            commandNumber--;
            gp.keyHandler.upPressed = false;
        }
        if(gp.keyHandler.enterPressed){
            gp.keyHandler.enterPressed = false;

            if(commandNumber == 0){
                if(gp.gameState == gp.startState){
                    gp.gameState = gp.playerTypeState;
                }
                else if(gp.gameState == gp.playerTypeState){
                    playerType = "Fighter";
                    gp.gameState = gp.playState;
                }
                else if(gp.gameState == gp.gameOverState || gp.gameState == gp.winState){
                    gp.restartheGame();
                }
            }

            if(commandNumber == 1){
                if(gp.gameState == gp.startState || gp.gameState == gp.gameOverState || gp.gameState == gp.winState){
                    System.exit(0);
                }
                if(gp.gameState == gp.playerTypeState){
                    playerType = "Mage";
                    gp.gameState = gp.playState;
                }
            }
            
            if(commandNumber == 2){
                if(gp.gameState == gp.playerTypeState){
                    playerType = "AllRounded";
                    gp.gameState = gp.playState;
                }
            }
        }

        if (commandNumber < 0) {
            commandNumber = (maxCommandNumber - Math.abs(commandNumber % maxCommandNumber)) % maxCommandNumber;
        } 
        else {
            commandNumber = commandNumber % maxCommandNumber;
        }
        return commandNumber;
    }

    public void drawGameStartScreen(){
        gameStartCommand = selectedCommand(gameStartCommand, 2);
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, screenWidth, screenHeight);
    
        int textX;
        int textY;
        BufferedImage image;
    
        // Title Image
        image = title; 
        g2.setColor(Color.black);
        textX = getXForCenterOfImage(image, gp);
        textY = tileSize / 3;
        g2.drawImage(image, textX, textY, null);
    
        // START
        image = start; 
        textX = getXForCenterOfImage(image, gp);
        textY += tileSize * 6 + 20;
        g2.drawImage(image, textX, textY, null);
        if (gameStartCommand == 0) {
            g2.drawImage(arrow, textX - (tileSize *  3), textY + 15, null);
        }
    
        // QUIT
        image = exit;  
        textY += tileSize * 2;
        g2.drawImage(image, textX, textY, null);
        if (gameStartCommand == 1) {
            g2.drawImage(arrow, textX - (tileSize *  3), textY + 15, null);
        }
    }

    public void selectPlayer() {
        gameStartCommand = selectedCommand(gameStartCommand, 3);
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, screenWidth, screenHeight);
    
        int textX;
        int textY;
        BufferedImage image;
    
        // Choose Character Pannel
        image = chooseCharacter;
        // Shadow
        g2.setColor(Color.black);
        textX = getXForCenterOfImage(image, gp);
        textY = 0;
        g2.drawImage(image, textX, textY, null);
    
        // Fighter
        image = fighter;
        textX = getXForCenterOfImage(image, gp);
        textY += tileSize * 5;
        g2.drawImage(image, textX + 15, textY, null);
        if (gameStartCommand == 0) {
            g2.drawImage(arrow, textX - (tileSize *  2), textY + 20, null);
        }
    
        // Mage
        image = mage;
        textX = getXForCenterOfImage(image, gp);
        textY += tileSize * 2;
        g2.drawImage(image, textX + 15, textY, null);
        if (gameStartCommand == 1) {
            g2.drawImage(arrow, textX - (tileSize *  2) , textY + 15, null);
        }

        // AllRounded
        image = allRounded;  
        textX = getXForCenterOfImage(image, gp);
        textY += tileSize * 2 - 15;
        g2.drawImage(image, textX + 15, textY, null);
        if (gameStartCommand == 2) {
            g2.drawImage(arrow, textX - (tileSize *  2) + 10, textY + 20, null);
        }
    }
    

    private void drawGameOverScreen() {
        gameStartCommand = selectedCommand(gameStartCommand, 2);
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, screenWidth, screenHeight);
    
        int textX;
        int textY;
        BufferedImage image;
    
        // GameOver Image
        image = gameOver;
        // Shadow
        g2.setColor(Color.black);
        textX = getXForCenterOfImage(image, gp);
        textY = tileSize / 3;
        g2.drawImage(image, textX, textY, null);
    
        // RETRY
        image = retry; 
        textX = getXForCenterOfImage(image, gp);
        textY += tileSize * 6 + 20;
        g2.drawImage(image, textX, textY, null);
        if (gameStartCommand == 0) {
            g2.drawImage(arrow, textX - (tileSize *  3), textY, null);
        }
    
        // Exit
        image = exit; 
        textY += tileSize * 2;
        g2.drawImage(image, textX, textY, null);
        if (gameStartCommand == 1) {
            g2.drawImage(arrow, textX - (tileSize *  3), textY, null);
        }
    }

    public void drawTheEndScreen(){
        gameStartCommand = selectedCommand(gameStartCommand, 2);
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, screenWidth, screenHeight);
    
        int textX;
        int textY;
        BufferedImage image;
    
        // TheEnd Image
        image = theEnd;
        g2.setColor(Color.black);
        textX = getXForCenterOfImage(image, gp);
        textY = tileSize / 3;
        g2.drawImage(image, textX, textY, null);
    
        // RETRY
        image = retry; 
        textX = getXForCenterOfImage(image, gp);
        textY += tileSize * 6 + 20;
        g2.drawImage(image, textX, textY, null);
        if (gameStartCommand == 0) {
            g2.drawImage(arrow, textX - (tileSize *  2), textY + 15, null);
        }
    
        // Exit
        image = exit; 
        textY += tileSize * 2;
        g2.drawImage(image, textX, textY, null);
        if (gameStartCommand == 1) {
            g2.drawImage(arrow, textX - (tileSize *  2), textY + 15, null);
        }
    }

    public void colorBorder(int x ,int y, int width, int height){
        g2.setColor(Color.BLACK);
        g2.drawRect(x, y, width, height);
    }


    
    // Modifier la méthode draw pour appeler drawInventory
    public void draw(Graphics2D g2) {
        this.g2 = g2;

        if(!Player.isInstanceNull()) player = Player.getInstance(gp, gp.keyHandler);

        g2.setFont(arial_40);
        g2.setColor(Color.white);

        //START STATE
        if (gp.gameState == gp.startState) {
            drawGameStartScreen();
        }

        //PLAYER TYPE STATE
        if (gp.gameState == gp.playerTypeState) {
            selectPlayer();
        }

        // PLAY STATE
        if (gp.gameState == gp.playState) {
            drawPlayerLife();
            drawPlayerXp();
            drawPlayerMana();
            drawMessage();
            drawPlayerStatus();
        }

        if (gp.gameState == gp.gameOverState){
            drawGameOverScreen();
        }

        // WIN STATE
        if (gp.gameState == gp.winState) {
            drawTheEndScreen();
        }

        // PAUSE STATE
        if (gp.gameState == gp.pauseState) {
            drawPlayerLife();
            drawPlayerXp();
            drawPlayerMana();
            drawPauseScreen();
        }

        // DIALOGUE STATE
        if (gp.gameState == gp.dialogueState) {
            drawPlayerLife();
            drawPlayerXp();
            drawPlayerMana();
            drawDialogueScreen();
        }

        // INVENTORY STATE
        if (gp.gameState == gp.inventoryState) {
            drawPlayerLife();
            drawPlayerXp();
            drawPlayerMana();
            drawInventory();
            
        }
    }
    
}