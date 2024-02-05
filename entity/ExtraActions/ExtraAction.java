package entity.ExtraActions;

import java.util.Vector;

import entity.Entity;
import entity.ExtraActions.BehaviorForActions.BehaviorForAction;
import main.GamePannel;

public abstract class ExtraAction {

    GamePannel gp;

    protected Entity executor; // Entity using the action
    protected boolean isPlayer;

    // number of frames the move is active
    protected int minTimeFrame;
    public int timeFrame;
    public int chronology;


    // list of behaviors for when to use the action
    private Vector<BehaviorForAction> listBehaviors = new Vector<BehaviorForAction>();

    public BehaviorForAction getCurrentBehavior(){
        return listBehaviors.get(0);
    }

    public void addPriorityBehavior(BehaviorForAction newBehavior){
        newBehavior.correctExecutor(executor);
        listBehaviors.add(0, newBehavior);
    }

    public void addNotPriorityBehavior(BehaviorForAction newBehavior){
        newBehavior.correctExecutor(executor);
        listBehaviors.add(newBehavior);
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

    
    
    public ExtraAction(Entity inputedEntity, int numberFramesActive, BehaviorForAction firstBehavior){
        executor = inputedEntity;
        gp = executor.gp;
        isPlayer = (executor == gp.player);

        setupTimeFrame(numberFramesActive);
        addPriorityBehavior(firstBehavior);
    }

    public void setupTimeFrame(int numberFramesActive){
        if(numberFramesActive <= minTimeFrame) timeFrame = minTimeFrame;
        else timeFrame = numberFramesActive;
        chronology = timeFrame;
    }

    public boolean isExecuting(){
        return chronology < timeFrame;
    }

    // to define depending on the entity
    public boolean execute(){
        return false;
    }
}