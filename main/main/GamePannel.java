package main;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.util.Vector;

import entity.NPC;
import entity.Player;
import entity.Monsters.Monster;
import entity.Abilities.Ability;
import entity.Abilities.FireBall.FireBall;
import entity.Abilities.ElectroBall.ElectroBall;
import object.SuperObject;
import tile.TileManager;


public class GamePannel extends JPanel implements Runnable {

    //SCREEN SETTINGS
    final int originalTileSize = 16; //16x16 is the original size of the player character or any other character in the game
    final int scale = 3;

    public final int tileSize = originalTileSize * scale; //48x48 tile is new size of the player character or any other character in the game
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol; // 16 * 48 = 768 pixels
    public final int screenHeight = tileSize * maxScreenRow; // 12 * 48 = 576 pixels

    //WORLD SETTINGS
    public final int maxWorldCol = 62;
    public final int maxWorldRow = 60;
    public final int worldWidth = tileSize * maxWorldCol; // 62 * 48 = 2976 pixels
    public final int worldHeight = tileSize * maxWorldRow; // 60 * 48 = 2880 pixels

    public int FPS = 60;
    public int index = 0;

    Thread  gameThread;

    //INITIATE OTHER OBJECTS
    public TileManager tileM = new TileManager(this);
    public KeyHandler keyHandler = new KeyHandler(this);
    public CollisionChecker collisionChecker = new CollisionChecker(this);
    public InteractionChecker interactionChecker = new InteractionChecker(this);
    public ClassSetter objSetter = new ClassSetter(this);
    public Player player = Player.getInstance(this, keyHandler);
    public Vector<SuperObject> item = new Vector<SuperObject>();
    public Vector<NPC> npc = new Vector<NPC>();
    public Vector<Monster> monster = new Vector<Monster>();
    public Vector<Monster> monsterToSpown = new Vector<Monster>();
    //public Ability ability[] = new Ability[10];
    public Ability ability = null;
    public FireBall fireBall = new FireBall(this);
    public ElectroBall electroBall = new ElectroBall(this);
    //public List<FireBall> fireBall = new ArrayList<>();
    public UI ui = UI.getInstance(this);
    //GAME STATE
    public int gameState;
    public int playState = 1;
    public int pauseState = 2;
    public int dialogueState = 3;
    public int inventoryState = 4;
    public int gameOverState = 5;


    public void setUpObject(){

        objSetter.setItems();
        objSetter.setNPCs();
        objSetter.setMonsters();
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
            for(NPC iterNPC : npc){
                if(iterNPC != null) iterNPC.update();
            }
            for(int i = 0; i < monster.size(); i++){
                Monster iterMonster = monster.get(i);
                if(iterMonster != null){
                    if(iterMonster.life <= 0){
                        monster.remove(i);
                        player.xp += iterMonster.xp;
                    }
                    else iterMonster.update();
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
        for(SuperObject iterItem : item){
            if(iterItem != null) iterItem.draw(g2, this);
        }

        //NPC
        for(NPC iterNPC : npc){
            if(iterNPC != null) iterNPC.draw(g2);
        }

        //MONSTER
        for(Monster iterMonster : monster){
            if(iterMonster != null){
                iterMonster.draw(g2);
                iterMonster.paintComponent(g2);
            }
        }

        //ABILITY
        if(ability != null) ability.draw(g2);

        //PLAYER
        player.draw(g2);
       
        //UI
        ui.draw(g2);

        g2.dispose();
    }
    
}
