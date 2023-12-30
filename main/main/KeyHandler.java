package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements ActionListener, KeyListener{

    GamePannel gp;
    public boolean upPressed, downPressed, leftPressed, rightPressed, xPressed, sPressed, dPressed, enterPressed;
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
        if (gp.gameState == gp.playState){
            switch (keyCode) {
                case KeyEvent.VK_LEFT:
                    leftPressed = true;
                    break;
                case KeyEvent.VK_RIGHT:
                    rightPressed = true;
                    break;
                case KeyEvent.VK_UP:
                    upPressed = true;
                    break;
                case KeyEvent.VK_DOWN:
                    downPressed = true;
                    break;
                case KeyEvent.VK_X:
                    xPressed = true;
                    break;
                case KeyEvent.VK_S:
                    sPressed = true;
                    break;
                case KeyEvent.VK_D:
                    dPressed = true;
                    break;
                case KeyEvent.VK_P:
                    gp.gameState = gp.pauseState;
                    break;
                case KeyEvent.VK_SPACE:
                    gp.gameState = gp.playState;
                    break;
                case KeyEvent.VK_B:
                    gp.gameState = gp.inventoryState;
                    break;
            }
        }

        else if(gp.gameState == gp.pauseState){
            switch (keyCode) {
                case KeyEvent.VK_SPACE:
                case KeyEvent.VK_P:
                    gp.gameState = gp.playState;
                    break;
            }
        }

        else if(gp.gameState == gp.dialogueState){
            switch (keyCode) {
                case KeyEvent.VK_SPACE:
                case KeyEvent.VK_X:
                    gp.gameState = gp.playState;
                    break;
            }
        }

        else if(gp.gameState == gp.inventoryState){
            switch (keyCode) {
                case KeyEvent.VK_SPACE:
                case KeyEvent.VK_B:
                    gp.gameState = gp.playState;
                    break;
                case KeyEvent.VK_LEFT:
                    leftPressed = true;
                    break;
                case KeyEvent.VK_RIGHT:
                    rightPressed = true;
                    break;
                case KeyEvent.VK_UP:
                    upPressed = true;
                    break;
                case KeyEvent.VK_DOWN:
                    downPressed = true;
                    break;
                case KeyEvent.VK_ENTER:
                    enterPressed = true;
                    break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();

        switch(keyCode){
            case KeyEvent.VK_LEFT:
                leftPressed = false;
                break;
            case KeyEvent.VK_RIGHT:
                rightPressed = false;
                break;
            case KeyEvent.VK_UP:
                upPressed = false;
                break;
            case KeyEvent.VK_DOWN:
                downPressed = false;
                break;
            case KeyEvent.VK_X:
                xPressed = false;
                break;
            case KeyEvent.VK_S:
                sPressed = false;
                break;
            case KeyEvent.VK_D:
                dPressed = false;
                break;
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