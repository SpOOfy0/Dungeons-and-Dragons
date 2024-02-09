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
        up1 = getImage("/Player/oldman_up_1.png");
        up2 = getImage("/Player/oldman_up_2.png");
        down1 = getImage("/Player/oldman_down_1.png");
        down2 = getImage("/Player/oldman_down_2.png");
        left1 = getImage("/Player/oldman_left_1.png");
        left2 = getImage("/Player/oldman_left_2.png");
        right1 = getImage("/Player/oldman_right_1.png");
        right2 = getImage("/Player/oldman_right_2.png");
    }

    @Override
    public void setDialogues() {
        dialogues.add("Hello!\nAnd welcome to the magic dungeon island!");
        dialogues.add("Remember what you've been taught,\nuse 'S' to hit in front of you, and\nlaunch fireballs and electroballs with 'D' and 'F'.");
        dialogues.add("Also, you can look what's in your inventory\nwith 'B', not that I know what that could mean.");
        dialogues.add("To get out of here, you'll need to defeat all\nthe monsters, and then...");
        dialogues.add("...You'll have to fight the Torero,\nthe strongest monster of the dungeon.");
        dialogues.add("Beat them all and you will be able to claim\nthe One Piece for yourself.\nAnd perhaps your own freedom with it...\nI pray that you'll be up to the challenge.");
        dialogues.add("Nice outfit by the way.");
    }
    
}
 