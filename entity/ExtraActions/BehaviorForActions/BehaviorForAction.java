package entity.ExtraActions.BehaviorForActions;

import entity.Entity;
import main.GamePannel;

public abstract class BehaviorForAction {

    protected final int OriginalActionSpeed;
    protected int actionSpeed;
    protected int actionDelay;
    protected boolean isRecharging = false;

    protected Entity executor;
    protected GamePannel gp;

    // initialize
    public BehaviorForAction(Entity inputedEntity, int inputedAtkSpeed){
        executor = inputedEntity;
        gp = executor.gp;
        actionDelay = actionSpeed = OriginalActionSpeed = inputedAtkSpeed;
    }

    // initialize
    public void correctExecutor(Entity newExecutor){
        executor = newExecutor;
    }

    // runs on every frame, returned value indicates if the move is usable
    public boolean executeAction(){
        if (canUseAction()) {
            actionDelay = 0;
            isRecharging = true;
            return true;
        } else {
            rechargeActionDelay();
            return false;
        }
    }


    protected void rechargeActionDelay(){
        if (isRecharging){
            actionDelay++;
            if (canUseAction()) isRecharging = false;
        }
    }

    protected boolean canUseAction(){
        return actionDelay >= actionSpeed;
    }

    protected void lowerActionSpeed(int difference){
        if(actionSpeed - difference < 1) actionSpeed = 1;
        else actionSpeed -= difference;
    }

}
