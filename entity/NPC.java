package entity;

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

    // ici, bufferDirection enregistre la direction auquel le NPC fait face avant de "discuter" avec le joueur en sorte de lui redonner cette direction après discussion
    public void setAction(){

        actionCounter++;

        if(isBlocked){
            for(int i = 0; i < direction.length; i++){
                if(direction[i] != null){
                    decideRestrain(direction[i]);
                }
            }
            impatience++;
        } else {
            for(int i = 0; i < direction.length; i++){
                if(direction[i] != null){
                    decideLetGo(direction[i]);
                }
            }
            impatience = 0;
        }
        
        if(gp.gameState == gp.playState && bufferDirection != null){
            direction[0] = bufferDirection;
            bufferDirection = null;

        } else if(actionCounter >= actionTimer || impatience >= impatienceTolerance){ //WAIT 2 SECONDS (120 frames = 2 seconds)
            
            wander();
            actionCounter = 0;
            impatience = 0;
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
 