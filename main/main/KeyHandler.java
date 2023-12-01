package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements ActionListener, KeyListener{

    GamePannel gp;
    public boolean upPressed, downPressed, leftPressed, rightPressed, xPressed, sPressed, dPressed;
    public int x = 100;
    public int y = 100;

    public KeyHandler(GamePannel gp) {
        this.gp = gp;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Handle action event
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Handle key typed event
    }

    @Override
    public void keyPressed(KeyEvent e) {

        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_LEFT) {
            leftPressed = true;
        } 
        else if (keyCode == KeyEvent.VK_RIGHT) {
            rightPressed = true;
        }
        else if (keyCode == KeyEvent.VK_UP) {
            upPressed = true;
        }
        else if (keyCode == KeyEvent.VK_DOWN) {
            downPressed = true;
        }
        else if (keyCode == KeyEvent.VK_X) {
            xPressed = true;
        }
        else if (keyCode == KeyEvent.VK_P) {
            if(gp.gameState == gp.playState){
                gp.gameState = gp.pauseState;
            }
            else if(gp.gameState == gp.pauseState){
                gp.gameState = gp.playState;
            }
        }
        else if (keyCode == KeyEvent.VK_SPACE) {
            if(gp.gameState == gp.dialogueState){
                gp.gameState = gp.playState;
            }
        }
        else if (keyCode == KeyEvent.VK_S) {
            sPressed = true;
        }
        else if (keyCode == KeyEvent.VK_D) {
            dPressed = true;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_LEFT) {
            leftPressed = false;
        }
        else if (keyCode == KeyEvent.VK_RIGHT) {
            rightPressed = false;
        }
        else if (keyCode == KeyEvent.VK_UP) {
            upPressed = false;
        }
        else if (keyCode == KeyEvent.VK_DOWN) {
            downPressed = false;
        }
        else if (keyCode == KeyEvent.VK_X) {
            xPressed = false;
        }
        else if (keyCode == KeyEvent.VK_S) {
            sPressed = false;
        }
        
    }

    /*public static void main(String agrs[]){
        //test keyhandler
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Donjons & Dragons");

        KeyHandler keyHandler = new KeyHandler();
        window.addKeyListener(keyHandler);

        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);



    }*/

    
    

}