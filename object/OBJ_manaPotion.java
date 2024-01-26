package object;

import main.GamePannel;

public class OBJ_manaPotion extends SuperObject {

    GamePannel gp;

    public OBJ_manaPotion(GamePannel gp, int coordX, int coordY){
        
        this.gp = gp;

        int[] newCoords = gp.tileM.verifyAndCorrectPlacement(coordX, coordY);

        int tileSize = gp.tileSize;
        worldX = newCoords[0] * tileSize;
        worldY = newCoords[1] * tileSize;
        
        name = "healPotion";
        
        image = getImage("/Objects/ManaPotion.png");
        
        newRectangle(0, 0, 48, 48);
    }

    public OBJ_manaPotion(GamePannel gp){

        this.gp = gp;

        name = "manaPotion";

        //image = getImage("/Objects/ManaPotion.png");
        image = getImage("/Objects/ManaPotion.png");

        newRectangle(0, 0, 48, 48);
    }

}
