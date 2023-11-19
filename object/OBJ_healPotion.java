package object;

import javax.imageio.ImageIO;

import main.GamePannel;

public class OBJ_healPotion extends SuperObject {
    
    GamePannel gp;

    public OBJ_healPotion(GamePannel gp){
        
        this.gp = gp;
        
        name = "healPotion";
        
        try{
            image = ImageIO.read(getClass().getResourceAsStream("/Objects/HealPotion.png"));
        }catch(Exception e){
            System.out.println("Error loading image");
        }
        
        newRectangle(0, 0, 48, 48);
    }

}
 