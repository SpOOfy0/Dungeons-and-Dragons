package entity;

import main.GamePannel;

public class NPC_1 extends NPC{
    
    public NPC_1(GamePannel gp, String inputedDirection, int coordX, int coordY){

        super(gp, inputedDirection, coordX, coordY);

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
        dialogues.add("Hello!\nAnd welcome to the magic dungeon island!");
        dialogues.add("To get out of here, you need to defeat all\nthe monsters.");
        dialogues.add("Then you will fight the Torero,\nthe strongest monster of all.");
        dialogues.add("Beat them all and you will get the one piece.");
        dialogues.add("Nice outfit by the way.");
    }
    
}
 