package object;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import entity.Player;
import main.GamePannel;


public abstract class SuperObject {
    
    public BufferedImage image, image2, image3;
    public String name;
    public boolean collision = false;
    public int worldX, worldY;
    public Rectangle solidArea;
    public int solidAreaDefaultX = 0, solidAreaDefaultY = 0;

    public int tileSize = 0;
    public Player player;
    
    public void newRectangle(int x, int y, int width, int height){
        solidArea = new Rectangle(x, y, width, height);
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

    public void draw(Graphics2D g2, GamePannel gp){
    
        if(tileSize == 0) tileSize = gp.tileSize;

        if(player == null && !Player.isInstanceNull()) player = Player.getInstance(gp, gp.keyHandler);
        
        int screenX = worldX - player.worldX + player.screenX ;
        int screenY = worldY - player.worldY + player.screenY ;
        if(worldX + tileSize > player.worldX - player.screenX &&
            worldX - tileSize < player.worldX + player.screenX &&
            worldY + tileSize > player.worldY - player.screenY &&
            worldY - tileSize < player.worldY + player.screenY) { 
            
            g2.drawImage(image, screenX, screenY, tileSize, tileSize, null);
        }
    }
}
