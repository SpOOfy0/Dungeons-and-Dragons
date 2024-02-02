package entity.ExtraActions;

import java.util.Vector;

import entity.Entity;
import entity.ExtraActions.BehaviorForActions.BehaviorForAction;

public abstract class ExtraAction {

    protected Entity executor; // Entity using the action

    // list of behaviors for when to use the action
    protected Vector<BehaviorForAction> listBehaviors = new Vector<BehaviorForAction>();

    public BehaviorForAction getCurrentBehavior(){
        return listBehaviors.get(0);
    }

    public void addPriorityBehavior(BehaviorForAction newBehavior){
        listBehaviors.add(0, newBehavior);
    }

    public void nextBehavior(){
        if(listBehaviors.size() > 1){
            listBehaviors.add(listBehaviors.get(0));
            forgetCurrentBehavior();
        }
    }

    public void previousBehavior(){
        int size = listBehaviors.size();
        if(size > 1){
            listBehaviors.add(0, listBehaviors.get(size-1));
            listBehaviors.remove(size);
        }
    }

    public void forgetCurrentBehavior(){
        if(listBehaviors.size() > 1) listBehaviors.remove(0);
    }

    
    
    public ExtraAction(Entity inputedEntity, BehaviorForAction firstBehavior){
        executor = inputedEntity;
        listBehaviors.add(firstBehavior);
    }

    // to define depending on the entity
    public boolean execute(){
        return false;
    }
}