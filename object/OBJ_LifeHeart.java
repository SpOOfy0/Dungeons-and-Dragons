package object;

import javax.imageio.ImageIO;

import main.GamePannel;


public class OBJ_LifeHeart extends SuperObject{

    GamePannel gp;

    public OBJ_LifeHeart(GamePannel gp){
        
        this.gp = gp;
        
        name = "LifeHeart";
        
        try{
            image = ImageIO.read(getClass().getResourceAsStream("/Objects/LifeHeart/fullHp.png"));
            image2 = ImageIO.read(getClass().getResourceAsStream("/Objects/LifeHeart/halfHp.png"));
            image3 = ImageIO.read(getClass().getResourceAsStream("/Objects/LifeHeart/NoHp.png"));
        } catch(Exception e){
            System.out.println("Error loading image");
        }
        
        newRectangle(0, 0, 48, 48);
    }
}
