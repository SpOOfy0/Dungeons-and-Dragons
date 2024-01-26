package object;

import main.GamePannel;


public class OBJ_LifeHeart extends SuperObject{

    GamePannel gp;

    public OBJ_LifeHeart(GamePannel gp){
        
        this.gp = gp;
        
        name = "LifeHeart";
        
        image = getImage("/Objects/LifeHeart/fullHp.png");
        image2 = getImage("/Objects/LifeHeart/HalfHp.png");
        image3 = getImage("/Objects/LifeHeart/NoHp.png");

        newRectangle(0, 0, 48, 48);
    }
}
