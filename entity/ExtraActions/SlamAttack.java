package entity.ExtraActions;

//import java.awt.image.BufferedImage;

import entity.Entity;
import entity.ExtraActions.BehaviorForActions.BehaviorForAction;

public class SlamAttack extends ExtraAction {

    //BufferedImage[] listFrames = new BufferedImage[8]; // (0-1) up, (2-3) down, (4-5) left, (6-7) right  ;  (x%2 == 0) first frame, (x%2 == 1) second frame
    
    public SlamAttack(Entity inputedEntity, BehaviorForAction firstBehavior){
        
        super(inputedEntity, firstBehavior);

        //listFrames = inputedFrames;
    }

    public boolean execute(){
        return false;
    }
}
