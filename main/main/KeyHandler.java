package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class KeyHandler implements ActionListener, KeyListener{

    GamePannel gp;
    public boolean upPressed, downPressed, leftPressed, rightPressed, xPressed, sPressed, dPressed, enterPressed, nPressed, lPressed, aPressed, fPressed;
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
                case KeyEvent.VK_ENTER:
                case KeyEvent.VK_B:
                    gp.gameState = gp.inventoryState;
                    break;
                case KeyEvent.VK_N:
                    nPressed = true;
                    break;
                case KeyEvent.VK_L:
                    lPressed = true;
                    break;
                case KeyEvent.VK_A:
                    aPressed = true;
                    break;
                case KeyEvent.VK_F:
                    fPressed = true;
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

        else if(gp.gameState == gp.gameOverState){
            switch (keyCode) {
                case KeyEvent.VK_DOWN:
                    downPressed = true;
                    break;
                case KeyEvent.VK_UP:
                    upPressed = true;
                    break;
                case KeyEvent.VK_ENTER:
                    enterPressed = true;
                    break;
            }
        }

        else if(gp.gameState == gp.startState){
            switch (keyCode) {
                case KeyEvent.VK_DOWN:
                    downPressed = true;
                    break;
                case KeyEvent.VK_UP:
                    upPressed = true;
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
            case KeyEvent.VK_N:
                nPressed = false;
                break;
        }
        
    }

}