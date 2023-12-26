package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import main.GamePannel;

public class Entity {

    public GamePannel gp;

    public int worldX, worldY;
    public int speed;
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public String facing;
    public String direction;
    public String direction2;

    public int spriteCounter = 0;
    public int spriteNum = 1;
    public int actionCounter = 0;
    public int monsterDommageCounter = 0;
    public int attackSpeed = 30;
    public int attackDelay = 0;

    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collision = false;
    public boolean collisionOn;


    public Rectangle interactionArea = new Rectangle(0, 0, 48, 48);
    public int interactionAreaDefaultX, interactionAreaDefaultY;
    public boolean interactionAreaUp = false;
    public boolean interactionAreaDown = false;
    public boolean interactionAreaLeft = false;
    public boolean interactionAreaRight = false;
    public boolean interactionOn;
    

    public String dialogues[] = new String[15];
    int dialogueIndex = 0;

    // CHARACTER STATUS
    public int maxLife;
    public int life;

    public Entity(GamePannel gp) {
        this.gp = gp;
    }

    public void setAction(){} 

    public void speak(){}

    public void monsterDead(){}


    public void update(){
        
        setAction();
        monsterDead();

        switch(direction){
            case "up":
                worldY -= speed;
                break;
            case "down":
                worldY += speed;
                break;
            case "left":
                worldX -= speed;
                break;
            case "right":
                worldX += speed;
                break;
        }

        collisionOn = false;
        gp.collisionChecker.checkTile(this);
        //gp.collisionChecker.checkObject(this,false);
        gp.collisionChecker.checkPlayer(this);

        if(collisionOn){
            switch(direction){
                case "up":
                    worldY += speed;
                    break;
                case "down":
                    worldY -= speed;
                    break;
                case "left":
                    worldX += speed;
                    break;
                case "right":
                    worldX -= speed;
                    break;
            }
        }

        spriteCounter++;
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


    public BufferedImage getImage(String ImagePath){

        BufferedImage image = null;

        try{
            image = ImageIO.read(getClass().getResourceAsStream(ImagePath));
        }catch(Exception e){
            System.out.println("Error loading image (" + ImagePath + ")");
        }

        return image;
 
    }

    public void draw(Graphics2D g2){

        BufferedImage image = null;
        int screenX = worldX - gp.player.worldX + gp.player.screenX ;
        int screenY = worldY - gp.player.worldY + gp.player.screenY ;

        if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
            worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
            worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
            worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) { 
            
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
    
}
