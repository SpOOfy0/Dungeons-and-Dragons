package object;

import main.GamePannel;

public class OBJ_Key extends SuperObject {
     GamePannel gp;

    // pour avoir la potion chargé sur le terrain
    public OBJ_Key(GamePannel gp, int coordX, int coordY){
        
        this.gp = gp;

        int[] newCoords = gp.tileM.verifyAndCorrectPlacement(coordX, coordY);

        int tileSize = gp.tileSize;
        worldX = newCoords[0] * tileSize;
        worldY = newCoords[1] * tileSize;
        
        name = "key";
        
        image = getImage("/Objects/Key.png");
        
        newRectangle(0, 0, 48, 48);
    }


    // pour avoir la potion chargé sur un aure endroit que sur le terrain (comme l'inventaire)
    public OBJ_Key(GamePannel gp){
        
        this.gp = gp;
        
        name = "key";
        
        image = getImage("/Objects/Key.png");
        
        newRectangle(0, 0, 48, 48);
    }

}
