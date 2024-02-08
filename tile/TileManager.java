package tile;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import entity.Player;
import main.GamePannel;


public class TileManager {

    public GamePannel gp;
    public Player player;
    public int tileSize;

    public int numberCol;
    public int numberRow;

    public Tile[] tile;
    public int mapTileNum[][];

    public TileManager(GamePannel GP){

        gp = GP;
        tileSize = gp.tileSize;

        numberCol = gp.maxWorldCol;
        numberRow = gp.maxWorldRow;

        tile = new Tile[10];
        mapTileNum = new int[numberCol][numberRow];

        getTileImage();
        loadMap("/Maps/map2.txt");
    }

    public void getTileImage(){
        try {
            tile[0] = new Tile();
            tile[0].image = ImageIO.read(getClass().getResourceAsStream("/Maps/Grass.png"));

            tile[1] = new Tile();
            tile[1].image = ImageIO.read(getClass().getResourceAsStream("/Maps/Water.png"));
            tile[1].collision = true;

            tile[2] = new Tile();
            tile[2].image = ImageIO.read(getClass().getResourceAsStream("/Maps/Ground.jpg"));
            tile[2].collision = true;
            tile[2].opaque = true;

            tile[3] = new Tile();
            tile[3].image = ImageIO.read(getClass().getResourceAsStream("/Maps/Gate/UpGate.png"));
            tile[3].collision = true;
            tile[3].opaque = true;

            tile[4] = new Tile();
            tile[4].image = ImageIO.read(getClass().getResourceAsStream("/Maps/AridEarth.jpg"));

            tile[5] = new Tile();
            tile[5].image = ImageIO.read(getClass().getResourceAsStream("/Maps/Earth.jpg"));
            
            tile[6] = new Tile();
            tile[6].image = ImageIO.read(getClass().getResourceAsStream("/Maps/Steel.png"));
            tile[6].collision = true;
            tile[6].opaque = true;
            tile[6].changeable = false;
            
            tile[7] = new Tile();
            tile[7].image = ImageIO.read(getClass().getResourceAsStream("/Maps/Grass.png"));
            tile[7].changeable = false;
            
            tile[8] = new Tile();
            tile[8].image = ImageIO.read(getClass().getResourceAsStream("/Maps/Earth.jpg"));
            tile[8].changeable = false;

            tile[9] = new Tile();
            tile[9].image = ImageIO.read(getClass().getResourceAsStream("/Maps/Steel.png"));
            tile[9].collision = true;
            tile[9].opaque = true;

        }catch(Exception e){
            System.out.println("Error loading image");
        }
    }



    public boolean isCollision(int typeTile){
        return tile[typeTile].collision;
    }
    
    public boolean isCollision(int coordX, int coordY){
        return tile[mapTileNum[coordX][coordY]].collision;
    }

    public int[] verifyAndCorrectPlacement(int coordX, int coordY){

        int[] finalCoord = {coordX, coordY};
        
        int offset = 1, limit = Math.min(coordX, coordY);
        if(isCollision(coordX, coordY)){
            while (finalCoord[0] == coordX && finalCoord[1] == coordY || offset <= limit){
                finalCoord = iteratorWithOffset(coordX, coordY, offset, limit);
                offset++;
            }
        }
        return finalCoord;
    }

    public int[] iteratorWithOffset(int baseCoordX, int baseCoordY, int offset, int limit){

        int coordX = baseCoordX, coordY = baseCoordY;
        int i = -offset, j;

        while(i <= offset){

            if(Math.abs(i) <= limit){
                coordX += i;
                j = offset - Math.abs(i);
                if(j <= limit){

                    if (j == 0){
                        if (!isCollision(coordX, coordY)) i = offset;
                        else {
                            coordX = baseCoordX;
                            coordY = baseCoordY;
                        }

                    } else {
                        coordY -= j;
                        if (!isCollision(coordX, coordY)) i = offset;
                        else {
                            coordY += j*2;
                            if (!isCollision(coordX, coordY)) i = offset;
                            else {
                                coordX = baseCoordX;
                                coordY = baseCoordY;
                            }
                        }
                    }
                } else coordX = baseCoordX;
            }
            i++;
        }

        int[] finalCoord = {coordX, coordY};
        return finalCoord;
    }



    public void loadMap(String filePath){
        try{
            InputStream is = getClass().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is)); //Lire le fichier

            int col = 0;  
            int row = 0; 
            while(row < numberRow){
                String line = br.readLine(); //Lire la ligne
                while(col < numberCol){
                    String numbers[] = line.split(" "); //numbers[] = {0,0,0,0,0,0,0,0,0,0}
                    int num = Integer.parseInt(numbers[col]); 
                    mapTileNum[col][row] = num;
                    col++;
                }
                if (col >= numberCol){
                    col = 0;
                    row++;
                }
            }
            br.close();
        }catch(Exception e){
            System.out.println("Error loading map");
        }
    }

    public void reloadMap(){
        loadMap("/Maps/map2.txt");
    }

    public boolean isChangeable(int coordX, int coordY){
        return tile[mapTileNum[coordX][coordY]].changeable;
    }

    public void changeTile(int coordX, int coordY, int tileNum){
        if(isChangeable(coordX, coordY)) mapTileNum[coordX][coordY] = tileNum;
    }

    public boolean closeToGate(int tileWorldX, int tileWorldY){
        
        if(!Player.isInstanceNull()) player = Player.getInstance(gp, gp.keyHandler);
        int worldX = player.worldX / tileSize;
        int worldY = player.worldY / tileSize;
        if (Math.abs(worldX - tileWorldX) <= 1 && Math.abs(worldY - tileWorldY) <= 1) return true;
        return false;
    }

    public void draw(Graphics2D g2){

        if(!Player.isInstanceNull()) player = Player.getInstance(gp, gp.keyHandler);
        
        int worldCol = 0;
        int worldRow = 0;

        while(worldRow < numberRow){

            int tileNum = mapTileNum[worldCol][worldRow];

            //POSITION OF THE TILE IN THE WORLD
            int worldX = worldCol * tileSize;  
            int worldY = worldRow * tileSize;

            //POSITION OF THE TILE ON THE SCREEN
            int screenX = worldX - player.worldX + player.screenX ;
            int screenY = worldY - player.worldY + player.screenY ;
            
            if(worldX + tileSize > player.worldX - player.screenX &&
               worldX - tileSize < player.worldX + player.screenX &&
               worldY + tileSize > player.worldY - player.screenY &&
               worldY - tileSize < player.worldY + player.screenY) { 
                
                g2.drawImage(tile[tileNum].image, screenX, screenY, tileSize, tileSize, null);
            }
            
            worldCol++;
            
            if(worldCol >= numberCol){
                worldCol = 0;
                worldRow++;
            }
        }
    }

}
 