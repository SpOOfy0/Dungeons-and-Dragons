package entity;

import main.GamePannel;

public class NPC extends Entity{
    
    public NPC(GamePannel gp, String inputedDirection, int coordX, int coordY){

        super(gp);

        noticeRange = 0;

        int[] newCoords = gp.tileM.verifyAndCorrectPlacement(coordX, coordY);

        worldX = newCoords[0] * tileSize;
        worldY = newCoords[1] * tileSize;
        
        if(inputedDirection != null) direction[0] = inputedDirection;
        else direction[0] = "down";
        direction[1] = null;
        facing = direction[0];
        bufferDirection = null;
        initSpeed(1);
    }
    

    public void getThisNPCImage(){}

    public void setDialogues(){}



    // ici, "bufferDirection" enregistre la direction auquel le NPC fait face avant de "discuter" avec le joueur en sorte de lui redonner cette direction après discussion
    public void setAction(){

        actionCounter++;

        // si l'entité est bloquée dans son mouvement, on récupère les dernières directions prises,
        // et on enlève individuellement le choix de prendre ces directions si l'entité est bloquée dans les directions correspondantes
        // de plus, on incrémente la variable d'impatience
        if(isBlocked){
            for(int i = 0; i < direction.length; i++){
                if(direction[i] != null) decideRestrain(direction[i]);
            }
            impatience++;

        // si l'entité n'est pas bloquée dans son mouvement, on redonne le choix de prendre les directions précédemment bloquées
        // du moment que l'entité n'est pas bloquée dans les directions correspondantes
        // de plus, on réinitialise la variable d'impatience
        } else {
            decideLetGoAll();
            impatience = 0;
        }
        
        // "bufferDirection" est non-null seulement si l'entité "discute" avec le joueur, et redonne la direction qu'avait l'entité avant de discuter avec le joueur
        if(gp.gameState == gp.playState && bufferDirection != null){
            direction[0] = bufferDirection;
            bufferDirection = null;

        // l'entité change de direction quand son compteur associée est a atteint la limite ou si elle est restée sans bouger trop longtemps
        } else if(actionCounter >= actionTimer || impatience >= impatienceTolerance){
            
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
 