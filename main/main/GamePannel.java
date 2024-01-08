package main;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.awt.Color;

import entity.Entity;
import entity.Player;
import entity.Monsters.Monster;
import entity.Abilities.Ability;
import entity.Abilities.FireBall.FireBall;
import object.OBJ_healPotion;
import object.OBJ_manaPotion;
import object.SuperObject;
import tile.TileManager;


public class GamePannel extends JPanel implements Runnable {

    //SCREEN SETTINGS
    final int originalTileSize = 16; //16x16 is the original size of the player character or any other character in the game
    final int scale = 3;

    public final int tileSize = originalTileSize * scale; //48x48 tile is new  size of the player character or any other character in the game
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol; //768 pixels
    public final int screenHight = tileSize * maxScreenRow; //576 pixels

    //WORLD SETTINGS
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int worldWidth = tileSize * maxWorldCol; //800 pixels
    public final int worldHight = tileSize * maxWorldRow; //800 pixels

    public int FPS = 60;
    public int index = 0;

    Thread  gameThread;

    //INITIAT AUTHER OBJECT
    public TileManager tileM = new TileManager(this);
    public KeyHandler keyHandler = new KeyHandler(this);
    public CollisionChecker collisionChecker = new CollisionChecker(this);
    public ClassSetter objSetter = new ClassSetter(this);
    public Player player = Player.getInstance(this, keyHandler);
    public SuperObject healPotion = new OBJ_healPotion(this);
    public SuperObject manaPotion = new OBJ_manaPotion(this);
    public SuperObject obj[] = new SuperObject[10];
    public Entity npc[] = new Entity[10];
    public Monster monster[] = new Monster[20];
    public Monster monsters[] = new Monster[20];
    //public ArrayList<Monster> Monster = new ArrayList<Monster>();
    //public Ability ability[] = new Ability[10];
    public Ability ability = new Ability(this);
    public FireBall fireBall ;
    //public List<FireBall> fireBall = new ArrayList<>();
    public UI ui = UI.getInstance(this);
    //GAME STATE
    public int gameState;
    public int playState = 1;
    public int pauseState = 2;
    public int dialogueState = 3;
    public int inventoryState = 4;
    public int gameOverState = 5;
    


    public GamePannel(){
        
        this.setPreferredSize(new Dimension(screenWidth, screenHight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.startGameThread();
        
    }

    public void setUpObject(){
        
        objSetter.setObject();
        objSetter.setNPC();
        objSetter.setMonster();
        gameState = playState;
    }

    public void startGameThread(){
        
        gameThread = new Thread(this);
        gameThread.start();
        
    }

    public void run(){

        double drawInterval = 1000000000/FPS; //0.01666 seconds
        double nextDrawTime = System.nanoTime() + drawInterval;

        while(gameThread != null){

            //UPDATE THE CHARACHTER POSITION
            update();

            //DRAW THE SCREEN WITH THE NEW CGARACHTER POSITION
            repaint();

            

            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime / 1000000;  

                if(remainingTime < 0){
                    remainingTime = 0;
                }

                Thread.sleep((long) remainingTime);

                nextDrawTime += drawInterval;
                
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        
        }
    }
    

    public void update(){
        
        if(gameState == playState){
            player.update();
            for(int i = 0; i < npc.length; i++){
                if(npc[i] != null){
                    npc[i].update();
                }
            }
            for(int i = 0; i < monster.length; i++){
                if(monster[i] != null){
                    monster[i].update();
                }
            }
            if(ability != null){
                ability.update();
            }
            objSetter.MonsterSpawner(14 * tileSize, 25 * tileSize);
        }
        if(gameState == pauseState){
            //nothing
        }
        
        
    }

    public void paint(Graphics g){

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        //TILE
        tileM.draw(g2);

        //OBJECT
        for(int i = 0; i < obj.length; i++){
            if(obj[i] != null){
            obj[i].draw(g2, this);
            }
        }

        //NPC
        for(int i = 0; i < npc.length; i++){
            if(npc[i] != null){
            npc[i].draw(g2);
            }
        }

        //MONSTER
        for(int i = 0; i < monster.length; i++){
            if(monster[i] != null){
            monster[i].draw(g2);
            monster[i].paintComponent(g2);
            }
        }

        //ABILITY
        if(ability != null){
            ability.draw(g2);
        }

        //PLAYER
        player.draw(g2);
       

        //UI
        ui.draw(g2);

        g2.dispose();

    }

    
}
