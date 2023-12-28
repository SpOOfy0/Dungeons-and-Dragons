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
    public String facing;       // direction dans laquelle regarde
    public String[] direction = new String[2]; // pour les entités qui peuvent se déplacer en diagonale [0: axe vertical; 1: axe horizontal]

    public int spriteCounter = 0;
    public int spriteNum = 1;
    public int actionCounter = 0;
    public int monsterDommageCounter = 0;
    public int attackSpeed = 30;
    public int attackDelay = 0;

    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collision = false;   // L'entité est affecté par les collisions ou pas?
    public boolean collisionOn;     // L'entité est-elle bloquée dans son mouvement? (ne servira plus pour les interaction)
    public boolean blockedUp;
    public boolean blockedDown;
    public boolean blockedLeft;
    public boolean blockedRight;  // L'entité est bloquée dans la direction correspondante


    public Rectangle interactionArea = new Rectangle(0, 0, 48, 48);
    public int interactionAreaDefaultX, interactionAreaDefaultY;
    public boolean interactUp = false;
    public boolean interactDown = false;
    public boolean interactLeft = false;
    public boolean interactRight = false;
    public boolean interactionOn;                   // Non-utilisés pour l'instant
    

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

        switch(direction[0]){
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

        facing = direction[0];

        collisionOn = false;
        blockedUp = false;
        blockedDown = false;
        blockedLeft = false;
        blockedRight = false;
        gp.collisionChecker.checkTile(this);
        //gp.collisionChecker.checkObject(this,false);
        gp.collisionChecker.checkPlayer(this);

        if(blockedUp) worldY += speed;
        if(blockedDown) worldY -= speed;
        if(blockedLeft) worldX += speed;
        if(blockedRight) worldX -= speed;

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
            
                switch(facing){
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
