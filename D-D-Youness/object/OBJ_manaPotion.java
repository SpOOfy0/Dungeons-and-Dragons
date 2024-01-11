package object;

import javax.imageio.ImageIO;

import main.GamePannel;

public class OBJ_manaPotion extends SuperObject {

    GamePannel gp;

    public OBJ_manaPotion(GamePannel gp){

        this.gp = gp;

        name = "manaPotion";

        //image = getImage("/Objects/ManaPotion.png");

        newRectangle(0, 0, 48, 48);
    }

}
