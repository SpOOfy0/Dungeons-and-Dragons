package main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements ActionListener, KeyListener{

    public boolean upPressed, downPressed, leftPressed, rightPressed;
    public int x = 100;
    public int y = 100;

    public KeyHandler() {
        Timer timer = new Timer(10, this);
        timer.start();
        
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