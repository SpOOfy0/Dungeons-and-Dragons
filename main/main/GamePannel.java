package main;

import javax.sound.sampled.Clip;
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
import entity.Abilities.MagicBalls.*;
import object.SuperObject;
import object.OBJ_LifeHeart;
import object.OBJ_healPotion;
import object.OBJ_manaPotion;
import object.OBJ_Key;
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
    public final int maxWorldCol = 64;
    public final int maxWorldRow = 62;
    public final int worldWidth = tileSize * maxWorldCol; //800 pixels
    public final int worldHeight = tileSize * maxWorldRow; //800 pixels

    public int FPS = 60;
    public int index = 0;

    Thread gameThread;

    //INITIATE AUTHER OBJECT
    public KeyHandler keyHandler = new KeyHandler(this);
    public CollisionChecker collisionChecker = new CollisionChecker(this);
    public InteractionChecker interactionChecker = new InteractionChecker(this);
    public ClassSetter objSetter = new ClassSetter(this);
    public TileManager tileM = new TileManager(this);
    
    public SuperObject healPotion = new OBJ_healPotion(this);
    public SuperObject manaPotion = new OBJ_manaPotion(this);
    public SuperObject key = new OBJ_Key(this);
    public SuperObject heart = new OBJ_LifeHeart(this);
    public UI ui = UI.getInstance(this);

    public Player player = Player.getInstance(this, keyHandler);
    public Vector<SuperObject> item = new Vector<SuperObject>();
    public Vector<NPC> npc = new Vector<NPC>();
    public NPC interactingNPC = null;
    public Vector<Monster> monster = new Vector<Monster>();
    
    public Ability ability = new Ability(this);
    public FireBall fireBall = new FireBall(this);
    public ElectroBall electroBall = new ElectroBall(this);
    
    
    public SoundManager soundManger = new SoundManager(this);

    //GAME STATE
    public int gameState;
    public int startState = 0;
    public int playState = 1;
    public int pauseState = 2;
    public int dialogueState = 3;
    public int inventoryState = 4;
    public int gameOverState = 5;
    public int winState = 6;
    public int playerTypeState = 7;
    Clip clip = soundManger.loadAudio("Sounds/WalkSound.wav");

    public boolean isBossSummoned;


    public void setUpObject(){
        
        objSetter.setItems();
        objSetter.setNPCs();
        objSetter.setMonsters();

        isBossSummoned = false;
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

    public void clear(){
        item.clear();
        npc.clear();
        interactingNPC = null;
        monster.clear();
    }

    public void restartheGame(){
        clear();
        setUpObject();
        tileM.reloadMap();
        player.restart();
        player.isDead = false;
        fireBall = new FireBall(this);
        electroBall = new ElectroBall(this);
        gameState = playState;
    }

    public void run(){

        double drawInterval = 1000000000/FPS; //0.01666 seconds
        double nextDrawTime = System.nanoTime() + drawInterval;

        while(gameThread != null){
            //SoundManager.play(clip);
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
            if(ability != null) ability.update();

            for(int i = 0; i < npc.size(); i++){
                NPC iterNPC = npc.get(i);
                if(iterNPC != null){
                    if(iterNPC.isDead){
                        iterNPC.onDeath();
                        if(!npc.isEmpty()) npc.remove(i);
                    }
                    else iterNPC.update();
                }
            }
            
            for(int i = 0; i < monster.size(); i++){
                Monster iterMonster = monster.get(i);
                if(iterMonster != null){
                    if(iterMonster.isDead){
                        iterMonster.onDeath();
                        if(!monster.isEmpty()) monster.remove(i);
                    }
                    else iterMonster.update();
                }
            }
            if (monster.size() == 0 && !isBossSummoned){
                isBossSummoned = true;
                npc.clear();
                objSetter.setBoss();
                objSetter.secret();
            }

            if(player.isDead) gameState = gameOverState;
        }
        if(gameState != dialogueState) interactingNPC = null;

    }


    public void paint(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        //TILE
        tileM.draw(g2);

        //OBJECT
        synchronized (item) {
            for(SuperObject iterItem : item){
                if(iterItem != null) iterItem.draw(g2, this);
            }
        }
        
        //NPC
        synchronized (npc) {
            for(NPC iterNPC : npc){
                if(iterNPC != null) iterNPC.draw(g2);
            }
        }
        

        //MONSTER
        synchronized (monster) {
            for(Monster iterMonster : monster){
                if(iterMonster != null){
                    iterMonster.draw(g2);
                    iterMonster.paintComponent(g2);
                }
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
