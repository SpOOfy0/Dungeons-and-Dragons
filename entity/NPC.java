package entity;

import java.util.Random;

import main.GamePannel;

public class NPC extends Entity{
    
    public NPC(GamePannel gp){

        super(gp);

        noticeRange = 0;
        
        direction[0] = "down";
        direction[1] = null;
        facing = direction[0];
        bufferDirection = null;
        speed = 1;

        getThisNPCImage();
        setDialogues();
    }

    public void getThisNPCImage(){}

    public void setDialogues() {}
    
    public void setAction(){

        actionCounter++;
        
        if(gp.gameState == gp.playState && bufferDirection != null){
            direction[0] = bufferDirection;
            bufferDirection = null;

        } else if(actionCounter >= 120){ //WAIT 2 SECONDS (120 frames = 2 seconds)
            Random random = new Random();
            int i = random.nextInt(100);

            if(i < 25){
                direction[0] = "up";
            } else if(i < 50){
                direction[0] = "down";
            } else if(i < 75){
                direction[0] = "left";
            } else if(i < 100){
                direction[0] = "right";
            }
            actionCounter = 0;
        }
    }

    public void speak(){

        if(dialogues.get(dialogueIndex) != null){
            bufferDirection = direction[0];
            gp.ui.currentDialogue = dialogues.get(dialogueIndex);
            dialogueIndex++;
            if(dialogueIndex >= dialogues.size()) dialogueIndex = 0;
            FacePlayer();
            gp.gameState = gp.dialogueState;
            //gp.player.npcIndex = 0;
        }

    }

    public void FacePlayer(){

        switch(gp.player.facing){
            case "up":
                direction[0] = "down";
                break;
            case "down":
                direction[0] = "up";
                break;
            case "left":
                direction[0] = "right";
                break;
            case "right":
                direction[0] = "left";
                break;
        }

    }

    
}
 