package entity;

import java.util.Random;

import main.GamePannel;

public class NPC_1 extends Entity{

    int stop = 0;
    
    public NPC_1(GamePannel gp){

        super(gp);

        direction = "down";
        speed = 1;

        getTihsNPCImage();
        setDialogues();
    }

    public void getTihsNPCImage(){

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

        if(stop == 0)
            actionCounter++;

            if(actionCounter == 120){ //WAIT 2 SECONDS (120 frames = 2 seconds)
                Random random = new Random();
                int i = random.nextInt(100)+1;

                if(i <= 25){
                    direction = "up";
                }
                else if(i <= 50){
                    direction = "down";
                }
                else if(i <= 75){
                    direction = "left";
                }
                else if(i <= 100){
                    direction = "right";
                }
                actionCounter = 0;
        }
    }

    public void setDialogues() {

        dialogues[0] = "Congratulations! you found the one piece!";
        dialogues[1] = "To get the one piece you need to defat all \n tghe monters";
        dialogues[2] = "Each one is stronger than the other";
        dialogues[3] = "Beat them all and you will get the one piece";
        dialogues[4] = "Nice outfit by the way";


    }

    public void speak(){

        if(dialogues[dialogueIndex] != null){
            gp.ui.currentDialogue = dialogues[dialogueIndex];
            dialogueIndex++;
            FacePlayer();
            stop = 1;
            //gp.player.npcIndex = 0;
        }

        else{
            stop = 0;
            dialogueIndex = 0;
        }
        /*else{
            //gp.ui.currentDialogue = null;
            dialogueIndex = 0;
            gp.gameState = gp.playState;
            //gp.player.npcIndex = 999;
        }*/
    
    }

    public void FacePlayer(){

        switch(gp.player.direction){
            case "up":
                direction = "down";
                break;
            case "down":
                direction = "up";
                break;
            case "left":
                direction = "right";
                break;
            case "right":
                direction = "left";
                break;
        }

    }

    
}
 