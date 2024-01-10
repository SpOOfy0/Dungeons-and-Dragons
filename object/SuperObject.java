package object;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import main.GamePannel;


public class SuperObject {
    
    public BufferedImage image, image2, image3;
    public String name;
    public boolean collision = false;
    public int worldX, worldY;
    public Rectangle solidArea;
    public int solidAreaDefaultX = 0, solidAreaDefaultY = 0;
    
    public void newRectangle(int x, int y, int width, int height){
        solidArea = new Rectangle(x, y, width, height);
    }

    public void draw(Graphics2D g2, GamePannel gp){
    
        int tileSize = gp.tileSize;
        
        int screenX = worldX - gp.player.worldX + gp.player.screenX ;
        int screenY = worldY - gp.player.worldY + gp.player.screenY ;
        if(worldX + tileSize > gp.player.worldX - gp.player.screenX &&
            worldX - tileSize < gp.player.worldX + gp.player.screenX &&
            worldY + tileSize > gp.player.worldY - gp.player.screenY &&
            worldY - tileSize < gp.player.worldY + gp.player.screenY) { 
            
            g2.drawImage(image, screenX, screenY, tileSize, tileSize, null);
        }
    }
}
