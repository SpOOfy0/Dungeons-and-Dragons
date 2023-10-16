package tile;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import main.GamePannel;

public class TileManager {
    
    GamePannel gp;
    public Tile[] tile;
    public int mapTileNum[][];

    public TileManager(GamePannel gp){

        this.gp = gp;

        tile = new Tile[10];
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];

        getTileImage();
        loadMap("/Maps/map.txt");
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

        }catch(Exception e){
            System.out.println("Error loading image");
        }
    }

    public void loadMap(String filePath){
        try{
            InputStream is = getClass().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is)); //Lire le fichier

            int col = 0;  
            int row = 0; 
            while(col < gp.maxWorldCol && row < gp.maxWorldRow){
                String line = br.readLine(); //Lire la ligne
                while(col < gp.maxWorldCol){
                    String numbers[] = line.split(" "); //nubers[] = {0,0,0,0,0,0,0,0,0,0}
                    int num = Integer.parseInt(numbers[col]); 
                    mapTileNum[col][row] = num;
                    col++;
                }
                if (col == gp.maxWorldCol){
                    col = 0;
                    row++;
                }
            }
            br.close();
        }catch(Exception e){
            System.out.println("Error loading map");
        }
    }

    public void draw(Graphics2D g2){
//      g2.drawImage(tile[0].image, 0, 0,30,30, null);
//      g2.drawImage(tile[1].image, 30, 0,30,30, null);
//      g2.drawImage(tile[2].image, 0, 30,30,30, null);
        int worldCol = 0;
        int worldRow = 0;

        while(worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow){

            int tileNum = mapTileNum[worldCol][worldRow];

            //POSITION OF THE TILE IN THE WORLD
            int worldX = worldCol * gp.tileSize;  
            int worldY = worldRow * gp.tileSize;

            //POSITION OF THE TILE ON THE SCREEN
            int screenX = worldX - gp.player.worldX + gp.player.screenX ;
            int screenY = worldY - gp.player.worldY + gp.player.screenY ;
            if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                    worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                    worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                    worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) { 
                
                g2.drawImage(tile[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
            }
            
            worldCol++;
            
            if(worldCol == gp.maxWorldCol){
                worldCol = 0;
                worldRow++;
            }
        }
    }
}
 