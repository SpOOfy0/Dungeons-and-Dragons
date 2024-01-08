package main;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.util.Vector;

import entity.NPC;
import entity.NPC_1;
import entity.Player;
import entity.Monsters.Monster;
import entity.Monsters.NormalMonsters.Orc;
import entity.Abilities.Ability;
import entity.Abilities.FireBall.FireBall;
import object.SuperObject;
import object.OBJ_healPotion;
import tile.TileManager;


public class GamePannel extends JPanel implements Runnable {

    //SCREEN SETTINGS
    final int originalTileSize = 16; //16x16 is the original size of the player character or any other character in the game
    final int scale = 3;

    public final int tileSize = originalTileSize * scale; //48x48 tile is new  size of the player character or any other character in the game
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol; //768 pixels
    public final int screenHeight = tileSize * maxScreenRow; //576 pixels

    //WORLD SETTINGS
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int worldWidth = tileSize * maxWorldCol; //800 pixels
    public final int worldHeight = tileSize * maxWorldRow; //800 pixels

    public int FPS = 60;
    public int index = 0;

    Thread  gameThread;

    //INITIAT AUTHER OBJECT
    public TileManager tileM = new TileManager(this);
    public KeyHandler keyHandler = new KeyHandler(this);
    public CollisionChecker collisionChecker = new CollisionChecker(this);
    public InteractionChecker interactionChecker = new InteractionChecker(this);
    public ClassSetter objSetter = new ClassSetter(this);
    public Player player = Player.getInstance(this, keyHandler);
    public Vector<SuperObject> item = new Vector<SuperObject>();
    public Vector<NPC> npc = new Vector<NPC>();
    public Vector<Monster> monster = new Vector<Monster>();
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


    public void setUpObject(){
        
        objSetter.setItem(new OBJ_healPotion(this, 14, 21));
        objSetter.setItem(new OBJ_healPotion(this, 14, 35));
        objSetter.setItem(new OBJ_healPotion(this, 14, 36));
        objSetter.setItem(new OBJ_healPotion(this, 14, 37));
        objSetter.setNPC(new NPC_1(this, "down", 14, 21));
        objSetter.setMonster(new Orc(this, "up", 14, 20));
        objSetter.setMonster(new Orc(this, "up", 14, 23));
        objSetter.setMonster(new Orc(this, "up", 10, 20));
    }

    public GamePannel(){
        
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.startGameThread(); 
    }


    public void startGameThread(){
        
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void run(){

        double drawInterval = 1000000000/FPS; //0.01666 seconds
        double nextDrawTime = System.nanoTime() + drawInterval;

        while(gameThread != null){

            //UPDATE THE CHARACTER POSITION
            update();

            //DRAW THE SCREEN WITH THE NEW CHARACTER POSITION
            repaint();

            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime / 1000000;  

                if(remainingTime < 0) remainingTime = 0;

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
            for(int i = 0; i < npc.size(); i++){
                if(npc.get(i) != null) npc.get(i).update();
            }
            for(int i = 0; i < monster.size(); i++){
                if(monster.get(i) != null){
                    if(monster.get(i).life <= 0) monster.remove(i);
                    else monster.get(i).update();
                }
            }
            if(ability != null) ability.update();
        }

    }

    public void paint(Graphics g){

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        //TILE
        tileM.draw(g2);

        //OBJECT
        for(int i = 0; i < item.size(); i++){
            if(item.get(i) != null) item.get(i).draw(g2, this);
        }

        //NPC
        for(int i = 0; i < npc.size(); i++){
            if(npc.get(i) != null) npc.get(i).draw(g2);
        }

        //MONSTER
        for(int i = 0; i < monster.size(); i++){
            if(monster.get(i) != null){
                monster.get(i).draw(g2);
                monster.get(i).paintComponent(g2);
            }
        }

        //ABILITY
        /*for(int i = 0; i < ability.length; i++){
            if(ability[i] != null){
            ability[i].draw(g2);
            }
        }*/
        if(ability != null) ability.draw(g2);

        //PLAYER
        player.draw(g2);
       
        //UI
        ui.draw(g2);

        g2.dispose();
    }
    
}
