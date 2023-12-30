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
    public String facing;       // direction dans laquelle l'entité regarde
    public String[] direction = new String[2]; // pour les entités qui peuvent se déplacer en diagonale [0: axe vertical; 1: axe horizontal]

    public int spriteCounter = 0;
    public int spriteNum = 1;
    public int actionCounter = 0;
    public int monsterDommageCounter = 0;
    public int attackSpeed = 30;
    public int attackDelay = 0;

    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);   // Est utilisé pour la position et pour toute interaction "subie"
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collision = false;   // L'entité est affecté par les collisions ou pas?
    public boolean blockedUp;
    public boolean blockedDown;     // Si l'entité bouge, elle est bloquée dans la direction correspondante,
    public boolean blockedLeft;     // sinon, ça permet de détecter si l'Entité est proche de qlqchose qui peut la bloquer dans la direction correspondante
    public boolean blockedRight;    
    public boolean isBlocked;       // Permet de dire si l'Entité bouge dans un direction et se fait blocker dans cette même direction


    public Rectangle interactionArea = new Rectangle(-1, -1, 50, 50);   // Est utilisé pour toute interaction "initiée"
    public int interactionAreaDefaultX, interactionAreaDefaultY;
    public boolean interactUp = false;
    public boolean interactDown = false;
    public boolean interactLeft = false;
    public boolean interactRight = false;
    public boolean isInteracting;                   // Non-utilisés pour l'instant
    

    public String dialogues[] = new String[15];
    int dialogueIndex = 0;

    // CHARACTER STATUS
    public int maxLife;
    public int life;

    public Entity(GamePannel gp) {
        this.gp = gp;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
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

        isBlocked = false;
        blockedUp = false;
        blockedDown = false;
        blockedLeft = false;
        blockedRight = false;
        gp.collisionChecker.checkTile(this);
        //gp.collisionChecker.checkObject(this,false);
        gp.collisionChecker.checkPlayer(this);

        switch(direction[0]){
            case "up":
                if(blockedUp) worldY += speed;
                break;
            case "down":
                if(blockedDown) worldY -= speed;
                break;
            case "left":
                if(blockedLeft) worldX += speed;
                break;
            case "right":
                if(blockedRight) worldX -= speed;
                break;
        }

        spriteCounter++;
            if(spriteCounter >= 12){
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

        if( worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
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
