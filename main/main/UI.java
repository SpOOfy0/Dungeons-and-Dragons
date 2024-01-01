package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

import object.OBJ_LifeHeart;
import object.OBJ_healPotion;
import object.SuperObject;

public class UI {

    private static UI instance;
    GamePannel gp;
    Graphics2D g2;
    Font arial_40;
    BufferedImage fullHeart ,halfHeart, emptyHeart; 
    BufferedImage HealPotionImage;

    public boolean messageOn = false;
    public String message = "";
    public int messageCounter = 0;

    public String currentDialogue = "";

    double timer = 0;
    DecimalFormat df = new DecimalFormat("#0.00");


    private UI(GamePannel gp) {
        this.gp = gp;

        arial_40 = new Font("Arial", Font.PLAIN, 40);
        OBJ_healPotion HealPotion = new OBJ_healPotion(gp);
        HealPotionImage = HealPotion.image;

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
            if(messageOn == true ){
                g2.setFont(new Font("Arial", Font.PLAIN, 20));
                g2.setColor(Color.white);
                g2.drawString(message, gp.screenWidth / 2 - gp.tileSize * 2 , gp.screenHight / 4 - gp.tileSize);
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
        g2.drawImage(HealPotionImage, 20, 5, gp.tileSize, gp.tileSize, null);
        g2.drawString("x" + gp.player.healPotion, gp.tileSize+15, 35);

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
        int y = gp.screenHight/2;
        
        g2.drawString(text, x, y);
    }

    public void drawDialogueScreen(){

        int x = gp.tileSize*2;
        int y = gp.tileSize/2;
        int width = gp.screenWidth - gp.tileSize*4;
        int height = gp.tileSize*4;

        drawSubWindow(x, y, width, height);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN,25F));
        x += gp.tileSize;
        y += gp.tileSize;

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

        int x = gp.tileSize/2;
        int y = gp.tileSize/2;
        int life = gp.player.life;

        // DRAW PLAYER MAXLIFE
        for(int i = 0; i < gp.player.maxLife/2; i++){
            g2.drawImage(emptyHeart, x, y, gp.tileSize, gp.tileSize, null);
            x += gp.tileSize;
        }

        //RESET
        x = gp.tileSize/2;

        // DRAW PLAYER LIFE
        while (life >= 2){
            g2.drawImage(fullHeart, x, y, gp.tileSize, gp.tileSize, null);
            life -= 2;
            x += gp.tileSize;
        }

        if(life == 1){
            g2.drawImage(halfHeart, x, y, gp.tileSize, gp.tileSize, null);
        }
        
    }

    public void drawPlayerXp() {
        if (gp.player.maxXp > gp.player.xp){
            int maxBarWidth = 200;
            double percentage = (double) gp.player.xp / gp.player.maxXp;
            double xpBarWidth = maxBarWidth * percentage;
            int screenX = gp.tileSize / 2;
            int screenY = gp.tileSize * 3 - 35;
        
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
            int screenX = gp.tileSize / 2;
            int screenY = gp.tileSize * 2 - 10;
            
            g2.setColor(Color.BLUE);
            g2.fillRect(screenX, screenY - 10, (int) manaBarWidth, 8);
            colorBorder(screenX, screenY - 10, maxBarWidth, 8);
        }
    }
    

    public void drawInventory() {
        int startX = gp.screenWidth / 2 + gp.tileSize;
        int startY = gp.screenHight / 2 - gp.tileSize * 4;
        int itemsPerRow = 5;
        int spacing = 10;

        BufferedImage image;
        int count;
        String Count;
    
        // Taille d'une case
        int cellSize = gp.tileSize + spacing;
    
        // Parcourir les emplacements de l'inventaire
        int itemIndex = 0;
        for (int i = 0; i < 30; i++) {
            int row = itemIndex / itemsPerRow;
            int col = itemIndex % itemsPerRow;
    
            int x = startX + col * cellSize;
            int y = startY + row * cellSize;
    
            // Draw the background of the inventory slot
            g2.setColor(new Color(100, 100, 100));
            g2.fillRect(x, y, gp.tileSize, gp.tileSize);
            
            if (gp.player.index == i){
                gp.player.selectOPbject(x, y,gp.tileSize, gp.tileSize);
                gp.player.useObject(i);
                colorBorder(x, y, gp.tileSize, gp.tileSize);
            }
            // If there is an item in this slot, draw it
            if (itemIndex < gp.player.inventory.size()) {
                String objName = gp.player.inventory.keySet().toArray(new String[0])[itemIndex];
    
                switch (objName) {
                    case "healPotion":
                        image = gp.healPotion.image;
                        count = gp.player.inventory.get(objName);
                        Count = String.valueOf(count);
                        g2.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);
                        //Add count
                        g2.setFont(new Font("Arial", Font.PLAIN, gp.tileSize / 4));
                        g2.setColor(Color.white);
                        g2.drawString(Count, x + 5, y + 15);
                        break;
                    case "manaPotion":
                        image = gp.manaPotion.image;
                        count = gp.player.inventory.get(objName);
                        Count = String.valueOf(count);
                        g2.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);
                        //Add count
                        g2.setFont(new Font("Arial", Font.PLAIN, gp.tileSize / 4));
                        g2.setColor(Color.white);
                        g2.drawString(Count, x + 5, y + 15);
                        break;
                    
    
                    // Add other cases for objName
    
                    default:
                        break;
                }
            }
    
            itemIndex++;
        }
    }

    public void drawPlayerStatus(){
        if (gp.keyHandler.nPressed == true){
            int x = gp.screenWidth / 8 - gp.tileSize;
            int y = gp.screenHight / 2 - gp.tileSize * 4;
            int width = gp.screenWidth / 2 - gp.tileSize * 2;
            int height = gp.screenHight - gp.tileSize * 4;
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
                "Strengh: " , "" + gp.player.damage,
                "Mana" , "" + gp.player.mana,
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
        return tailX - length - gp.tileSize / 2;
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

        drawPlayerXp();
        drawPlayerMana();

        // PLAY STATE
        if (gp.gameState == gp.playState) {
            drawPlayerLife();
            drawMessage();
            drawPlayerStatus();
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


