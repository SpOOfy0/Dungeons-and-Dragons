package object;

import javax.imageio.ImageIO;

import main.GamePannel;

public class OBJ_manaPotion extends SuperObject {
    
    GamePannel gp;

    public OBJ_manaPotion(GamePannel gp){
        
        this.gp = gp;
        
        name = "manaPotion";
        
        try{
            image = ImageIO.read(getClass().getResourceAsStream("/Objects/ManaPotion.png"));
        }catch(Exception e){
            System.out.println("Error loading image");
        }
        
        newRectangle(0, 0, 48, 48);
    }

}
 