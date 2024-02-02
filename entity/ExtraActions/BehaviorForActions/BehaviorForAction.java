package entity.ExtraActions.BehaviorForActions;

import entity.Entity;

public abstract class BehaviorForAction {

    protected int actionSpeed;
    protected int actionDelay;
    protected boolean isRecharging = false;

    protected Entity executor;

    public BehaviorForAction(int inputedAtkSpeed){
        actionSpeed = inputedAtkSpeed;
        actionDelay = actionSpeed;
    }

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

}
