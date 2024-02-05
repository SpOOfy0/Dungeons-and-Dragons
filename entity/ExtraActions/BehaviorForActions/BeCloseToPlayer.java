package entity.ExtraActions.BehaviorForActions;

import java.awt.Rectangle;

import entity.Entity;

public class BeCloseToPlayer extends BehaviorForAction {
    
    public int tileSize = executor.tileSize;
    public int impatienceIncrement;

    //must be tile coordonates
    public int distanceDetect;

    public BeCloseToPlayer(Entity inputedEntity, int inputedAtkSpeed, int inputedDistanceDetect){
        super(inputedEntity, inputedAtkSpeed);
        distanceDetect = inputedDistanceDetect;
        impatienceIncrement = 1;
    }
    
    
    protected void rechargeActionDelay(){
        if (isRecharging) {
            actionDelay++;
            if (super.canUseAction()) isRecharging = false;
        }
    }


    protected boolean canUseAction(){
        
        if(executor.life > executor.maxLife*2/3 && executor.registeredDMG > 0) lowerActionSpeed(2*executor.registeredDMG);
        else if (executor.life > executor.maxLife/3 && executor.registeredDMG > 0) lowerActionSpeed(executor.registeredDMG);

        boolean condition = false;
        if(executor.isBlocked && !executor.isWithPlayer){

            int currentActionDelay = actionDelay;
            actionDelay += impatienceIncrement;

            if(condition = super.canUseAction()) impatienceIncrement++;
            else actionDelay = currentActionDelay;
        }

        if(!condition){
            if(condition = super.canUseAction()){
                int leftLimit = executor.getLeftTile() - distanceDetect;
                int rightLimit = (executor.getRightTile() + distanceDetect - leftLimit)*tileSize;
                leftLimit = leftLimit*tileSize;
                int upLimit = executor.getUpperTile() - distanceDetect;
                int downLimit = (executor.getLowerTile() + distanceDetect - upLimit)*tileSize;
                upLimit = upLimit*tileSize;

                Rectangle triggerbox = new Rectangle(leftLimit, upLimit, rightLimit, downLimit);

                gp.player.solidArea.x += gp.player.worldX;
                gp.player.solidArea.y += gp.player.worldY;
                
                condition = triggerbox.contains(gp.player.solidArea);
                
                gp.player.solidArea.x = gp.player.solidAreaDefaultX;
                gp.player.solidArea.y = gp.player.solidAreaDefaultY;
            }
        }

        return condition;
    }

}
