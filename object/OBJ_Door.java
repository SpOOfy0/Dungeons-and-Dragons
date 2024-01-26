package object;

import main.GamePannel;

public class OBJ_Door extends SuperObject {
     GamePannel gp;

    // pour avoir la potion chargé sur le terrain
    public OBJ_Door(GamePannel gp, int coordX, int coordY){
        
        this.gp = gp;

        int[] newCoords = gp.tileM.verifyAndCorrectPlacement(coordX, coordY);

        int tileSize = gp.tileSize;
        worldX = newCoords[0] * tileSize;
        worldY = newCoords[1] * tileSize;
        
        name = "door";
        
        image = getImage("/Objects/LifeHeart/fullHp.png");
        
        newRectangle(0, 0, 48, 48);
    }


    // pour avoir la potion chargé sur un aure endroit que sur le terrain (comme l'inventaire)
    public OBJ_Door(GamePannel gp){
        
        this.gp = gp;
        
        name = "door";
        
        image = getImage("/Objects/LifeHeart/fullHp.png");
        
        newRectangle(0, 0, 48, 48);
    }

}
