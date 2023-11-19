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


    public UI(GamePannel gp) {
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

    public double setTimer(double time){

        time += (double)1/60;
        return time;
    
    }

    public void showMessage(String text){

        message = text;
        messageOn = true;
    
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

    public void draw(Graphics2D g2){

        this.g2 = g2;

        g2.setFont(arial_40);
        g2.setColor(Color.white);

        //PLAY STATE
        if(gp.gameState == gp.playState){
            drawPlayerLife();
        }

        //PAUSE STATE
        if(gp.gameState == gp.pauseState){
            drawPlayerLife();
            drawPauseScreen();
        }

        //DIALOGUE STATE
        if(gp.gameState == gp.dialogueState){
            drawPlayerLife();
            drawDialogueScreen();
        }

        


    }

   

    
}
