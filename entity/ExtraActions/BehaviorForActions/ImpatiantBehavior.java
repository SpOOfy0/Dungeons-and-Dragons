package entity.ExtraActions.BehaviorForActions;

public class ImpatiantBehavior extends BehaviorForAction {

    public ImpatiantBehavior(int inputedAtkSpeed){
        super(inputedAtkSpeed);
    }
    
    //change these methods
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
        if (isRecharging) {
            if(executor.isBlocked){

            }
            actionDelay++;
        }
    }

    protected boolean canUseAction(){
        return actionDelay >= actionSpeed;
    }
}
