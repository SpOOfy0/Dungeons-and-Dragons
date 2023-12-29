package entity;

import java.util.Random;

import main.GamePannel;

public class NPC_1 extends Entity{
    
    public NPC_1(GamePannel gp){

        super(gp);

        direction[0] = "down";
        direction[1] = null;
        speed = 1;

        getThisNPCImage();
        setDialogues();
    }

    public void getThisNPCImage(){

        up1 = getImage("/Player/up1.png");
        up2 = getImage("/Player/up2.png");
        down1 = getImage("/Player/down1.png");
        down2 = getImage("/Player/down2.png");
        left1 = getImage("/Player/left1.png");
        left2 = getImage("/Player/left2.png");
        right1 = getImage("/Player/right1.png");
        right2 = getImage("/Player/right2.png");
    }
    
    public void setAction(){

        actionCounter++;
        if(actionCounter >= 120){ //WAIT 2 SECONDS (120 frames = 2 seconds)
            Random random = new Random();
            int i = random.nextInt(100)+1;

            if(i <= 25){
                direction[0] = "up";
            } else if(i <= 50){
                direction[0] = "down";
            } else if(i <= 75){
                direction[0] = "left";
            } else if(i <= 100){
                direction[0] = "right";
            }
            actionCounter = 0;
        }
    }

    public void setDialogues() {

        dialogues[0] = "Congratulations! you found the one piece!";
        dialogues[1] = "To get the one piece you need to defeat all \n the monsters";
        dialogues[2] = "Each one is stronger than the other";
        dialogues[3] = "Beat them all and you will get the one piece";
        dialogues[4] = "Nice outfit by the way";


    }

    public void speak(){

        if(dialogues[dialogueIndex] != null){
            gp.ui.currentDialogue = dialogues[dialogueIndex];
            dialogueIndex++;
            if(dialogueIndex >= dialogues.length) dialogueIndex = 0;
            FacePlayer();
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
 