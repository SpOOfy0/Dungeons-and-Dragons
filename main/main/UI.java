package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

import object.*;


public class UI {

    private static UI instance;
    GamePannel gp;
    Graphics2D g2;
    Font arial_40;
    BufferedImage fullHeart ,halfHeart, emptyHeart; 
    BufferedImage HealPotionImage;
    BufferedImage ManaPotionImage;
    
    public int tileSize;

    public boolean messageOn = false;
    public String message = "";
    public int messageCounter = 0;

    public String currentDialogue = "";

    double timer = 0;
    DecimalFormat df = new DecimalFormat("#0.00");
    
    //new
    boolean showStatus = false;
    boolean isHoldingN = false;


    private UI(GamePannel gp) {
        this.gp = gp;
        tileSize = gp.tileSize;

        arial_40 = new Font("Arial", Font.PLAIN, 40);
        OBJ_healPotion HealPotion = new OBJ_healPotion(gp);
        HealPotionImage = HealPotion.image;
        OBJ_manaPotion ManaPotion = new OBJ_manaPotion(gp);
        ManaPotionImage = ManaPotion.image;


        // CREAT HUD OBJET 
        SuperObject heart = new OBJ_LifeHeart(gp);
        fullHeart = heart.image;
        halfHeart = heart.image2;
        emptyHeart = heart.image3;
    }

    public static UI getInstance(GamePannel gp) {

        if (instance == null) {
            instance = new UI(gp);
        }

        return instance;
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
            g2.drawString(message, gp.screenWidth / 2 - tileSize * 2 , gp.screenHeight / 4 - tileSize);
            messageCounter++;
        
            if(messageCounter > 100){
                messageOn = false;
                messageCounter = 0;
            }   
        }
    }

    /*public void draw(Graphics2D g2) {

        timer = setTimer(timer);

        //Set the font
        g2.setFont(arial_40);
        g2.setColor(Color.white);

        //Draw timer
        g2.drawString("Time: " + df.format(timer), 650, 35);

        //Draw Heal Potion
        g2.drawImage(HealPotionImage, 20, 5, tileSize, tileSize, null);
        g2.drawString("x" + gp.player.healPotion, tileSize+15, 35);

        // IF PLAYER GOT A HEAL POTION
        if(messageOn == true ){
            
            g2.drawString(message, 20, 100);
            messageCounter++;
        
            if(messageCounter > 100){
                messageOn = false;
                messageCounter = 0;
            }   
        }
    }*/

    

    public int getXScreenCenterText(String text){
        int textLength = g2.getFontMetrics().stringWidth(text);
        int x = (gp.screenWidth - textLength)/2;
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
        int width = gp.screenWidth - tileSize*4;
        int height = tileSize*4;

        drawSubWindow(x, y, width, height);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN,25F));
        x += tileSize;
        y += tileSize;

        for(String line : currentDialogue.split("\n")){
            g2.drawString(line, x, y);
            y += 40;
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

       //gp.player.life = 5; //for testing

        int x = tileSize/2;
        int y = tileSize/2;
        int life = gp.player.life;

        // DRAW PLAYER MAXLIFE
        for(int i = 0; i < gp.player.maxLife/2; i++){
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
        if (gp.player.maxXp > gp.player.xp){
            int maxBarWidth = 200;
            double percentage = (double) gp.player.xp / gp.player.maxXp;
            double xpBarWidth = maxBarWidth * percentage;
            int screenX = tileSize / 2;
            int screenY = tileSize * 3 - 35;

            g2.setColor(Color.WHITE);
            g2.fillRect(screenX, screenY - 10, (int) xpBarWidth, 8);
            colorBorder(screenX, screenY - 10, maxBarWidth, 8);
        }
    }

    public void drawPlayerMana() {
        if (gp.player.maxMana >= gp.player.mana){
            int maxBarWidth = 200;
            double percentage = (double) gp.player.mana / gp.player.maxMana;
            double manaBarWidth = maxBarWidth * percentage;
            int screenX = tileSize / 2;
            int screenY = tileSize * 2 - 10;

            g2.setColor(Color.BLUE);
            g2.fillRect(screenX, screenY - 10, (int) manaBarWidth, 8);
            colorBorder(screenX, screenY - 10, maxBarWidth, 8);
        }
    }


    public void drawInventory() {
        int startX = gp.screenWidth / 2 + tileSize;
        int startY = gp.screenHeight / 2 - tileSize * 4;
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
            
            if (gp.player.index == i){
                gp.player.selectOPbject(x, y,tileSize, tileSize);
                gp.player.useObject(i);
                colorBorder(x, y, tileSize, tileSize);
            }
            // Si l'inventaire contient un objet à cet emplacement, dessinez-le
            if (itemIndex < gp.player.inventory.size()) {
                String objName = gp.player.inventory.keySet().toArray(new String[0])[itemIndex];

                BufferedImage image;
                int count;
                String countInString;

                switch (objName) {
                    case "healPotion":
                        image = HealPotionImage;
                        count = gp.player.inventory.get(objName);
                        countInString = String.valueOf(count);
                        g2.drawImage(image, x, y, tileSize, tileSize, null);
                        //Add count
                        g2.setFont(new Font("Arial", Font.PLAIN, tileSize / 4));
                        g2.setColor(Color.white);
                        g2.drawString(countInString, x + 5, y + 15);
                        break;
                    case "manaPotion":
                        image = ManaPotionImage;
                        count = gp.player.inventory.get(objName);
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

        //new
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
            int x = gp.screenWidth / 8 - tileSize;
            int y = gp.screenHeight / 2 - tileSize * 4;
            int width = gp.screenWidth / 2 - tileSize * 2;
            int height = gp.screenHeight - tileSize * 4;
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
                "Level: " , "" + gp.player.level,
                "XP: " , "" + gp.player.xp + "/" + gp.player.maxXp,
                "Life: " , "" + gp.player.life + "/" + gp.player.maxLife,
                "Strengh: " , "" + gp.player.damage,
                "Mana" , "" + gp.player.mana + "/" + gp.player.maxMana,
                "Speed Attack: " , "" + gp.player.attackSpeed,
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


    private void drawGameOverScreen() {
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        int textX;
        int textY;
        String text;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 110f));

        // TITLE TEXT
        text = "Game Over";

        // Shadow
        g2.setColor(Color.black);
        textX = getXForCenterOfText(text, gp, g2);
        textY = tileSize * 4;
        g2.drawString(text, textX, textY);

        // Text
        g2.setColor(Color.WHITE);
        g2.drawString(text, textX - 4, textY - 4);

        // RETRY
        g2.setFont(g2.getFont().deriveFont(50f));
        text = "Retry";
        textX = getXForCenterOfText(text, gp, g2);
        textY += tileSize * 4;
        g2.drawString(text, textX, textY);
        // if (commandNumber == 0) {
        //     g2.drawString(">", textX - 40, textY);
        //     if (gp.getKeyHandler().isEnterPressed()) {
        //         commandNumber = 0;
        //         gp.retry();
        //     }
        // }

        // BACK TO TITLE
        text = "Quit";
        textX = getXForCenterOfText(text, gp, g2);
        textY += 55;
        g2.drawString(text, textX, textY);
        // if (commandNumber == 1) {
        //     g2.drawString(">", textX - 40, textY);
        //     if (gp.getKeyHandler().isEnterPressed()) {
        //         commandNumber = 0;
        //         gp.restart();
        //     }
        // }

        // gp.setEnterPressed(false);
    }



    public void colorBorder(int x ,int y, int width, int height){
        g2.setColor(Color.BLACK);
        g2.drawRect(x, y, width, height);
    }

    
    // Modifier la méthode draw pour appeler drawInventory
    public void draw(Graphics2D g2) {
        this.g2 = g2;

        g2.setFont(arial_40);
        g2.setColor(Color.white);

        drawPlayerMana();
        drawPlayerXp();
        

        // PLAY STATE
        if (gp.gameState == gp.playState) {
            drawPlayerLife();
            drawMessage();
            drawPlayerStatus();
        }

        if (gp.gameState == gp.gameOverState){
            drawGameOverScreen();
        }

        // PAUSE STATE
        if (gp.gameState == gp.pauseState) {
            drawPlayerLife();
            drawPauseScreen();
        }

        // DIALOGUE STATE
        if (gp.gameState == gp.dialogueState) {
            drawPlayerLife();
            drawDialogueScreen();
        }

        // INVENTORY STATE
        if (gp.gameState == gp.inventoryState) {
            drawPlayerLife();
            drawInventory(); // Appeler la nouvelle méthode drawInventory
            
        }
    }
    
}