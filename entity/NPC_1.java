package entity;

import main.GamePannel;

public class NPC_1 extends NPC{
    
    public NPC_1(GamePannel gp){

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

    @Override
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

    @Override
    public void setDialogues() {

        dialogues.add("Congratulations! you found the one piece!");
        dialogues.add("To get the one piece you need to defeat all \n the monsters");
        dialogues.add("Each one is stronger than the other");
        dialogues.add("Beat them all and you will get the one piece");
        dialogues.add("Nice outfit by the way");
    }
    
}
 