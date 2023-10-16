package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import main.GamePannel;
import main.KeyHandler;

public class Player extends Entity {

    GamePannel gp;
    KeyHandler keyHandler;

    //PLAYER SCREEN POSITION
    public final int screenX;
    public final int screenY;

  
    public Player(GamePannel gp, KeyHandler keyH){
        this.gp = gp;
        this.keyHandler = keyH;

        //PLAYER SCREEN POSITION
        screenX = gp.screenWidth/2 - gp.tileSize/2;
        screenY = gp.screenHight/2 - gp.tileSize/2;

        solidArea = new Rectangle(8, 16, 16, 20);

        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues(){

        //PLAYER WORLD POSITION
        worldX = gp.tileSize * 23; 
        worldY = gp.tileSize * 21; 
        speed = 4;
        direction = "down";
    }

    public void getPlayerImage(){
        try{
            up1 = ImageIO.read(getClass().getResourceAsStream("/Player/up1.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/Player/up2.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("/Player/down1.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/Player/down2.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/Player/left1.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/Player/left2.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/Player/right1.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/Player/right2.png"));
        }catch(Exception e){
            System.out.println("Error loading image");
        }
    }

    public void update(){
        if(keyHandler.upPressed == true){
            direction = "up";
            worldY = worldY - speed;
        }
        else if(keyHandler.downPressed == true){
            direction = "down";
            worldY = worldY + speed;
        }
        else if(keyHandler.leftPressed == true){
            direction = "left";
            worldX = worldX - speed;
        }
        else if(keyHandler.rightPressed == true){
            direction = "right";
            worldX = worldX + speed;
        }

        //CHECK COLLISION
        collisionOn = false;
        gp.collisionChecker.checkTile(this); //Player is considered as an entity beacause it extends Entity

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
     
    public void draw(Graphics2D g2){
//       g2.setColor(Color.WHITE);
//       g2.fillRect(x, y, 30, 30);

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
