package entity.ExtraActions;

import java.awt.Rectangle;

import entity.Entity;
import entity.Monsters.Monster;
import entity.ExtraActions.BehaviorForActions.BehaviorForAction;
import tile.TileManager;

public class SlamAttack extends ExtraAction {

    public int tileSize;
    public TileManager tileM;

    private int damage;
    private int range;

    private int leftLimit;
    private int rightLimit;
    private int upLimit;
    private int downLimit;

    public SlamAttack(Entity inputedEntity, int dmg, int inputedRange, int numberFramesActive, BehaviorForAction firstBehavior){
        super(inputedEntity, numberFramesActive, firstBehavior);
        tileSize = executor.tileSize;
        tileM = gp.tileM;
        damage = dmg;
        range = inputedRange;
    }

    public void setupTimeFrame(int numberFramesActive){
        minTimeFrame = 5;
        super.setupTimeFrame(numberFramesActive);
    }

    public boolean execute(){

        if(!isExecuting() && getCurrentBehavior().executeAction()) chronology = 0;

        if(isExecuting()){
            chronology++;

            if(chronology == timeFrame*3/5){
                // tile coordonates for hitbox
                leftLimit = executor.getLeftTile() - range;
                rightLimit = executor.getRightTile() + range;
                upLimit = executor.getUpperTile() - range;
                downLimit = executor.getLowerTile() + range;

                for (int x = leftLimit ; x <= rightLimit ; x++){
                    for (int y = upLimit ; y <= downLimit ; y++){
                        tileM.changeTile(x, y, 4);
                    }
                }

                int leftHitbox = leftLimit * tileSize;
                int rightHitbox = (rightLimit + 1 - leftLimit) * tileSize;
                int upHitbox = upLimit * tileSize;
                int downHitbox = (downLimit + 1 - upLimit) * tileSize;
                Rectangle hitbox = new Rectangle(leftHitbox, upHitbox, rightHitbox, downHitbox);

                if(isPlayer){
                    synchronized (gp.monster){
                        for(Monster iterMonster : gp.monster){
                            iterMonster.solidArea.x += iterMonster.worldX;
                            iterMonster.solidArea.y += iterMonster.worldY;
                            if(iterMonster.solidArea.intersects(hitbox)){
                                iterMonster.receiveDmg(damage);
                                if(!iterMonster.noKnockback) iterMonster.giveForcedMovement(interactionChecker.goingAwayFrom(iterMonster, executor), 8, 25);
                            }
                            iterMonster.solidArea.x = iterMonster.solidAreaDefaultX;
                            iterMonster.solidArea.y = iterMonster.solidAreaDefaultY;
                        }
                    }
                } else {
                    player.solidArea.x += player.worldX;
                    player.solidArea.y += player.worldY;
                    if(player.solidArea.intersects(hitbox)){
                        player.receiveDmg(damage);
                        player.giveForcedMovement(interactionChecker.goingAwayFrom(player, executor), 8, 25);
                    }
                    player.solidArea.x = player.solidAreaDefaultX;
                    player.solidArea.y = player.solidAreaDefaultY;
                }
            }

            if(chronology == timeFrame){
                for (int x = leftLimit ; x <= rightLimit ; x++){
                    for (int y = upLimit ; y <= downLimit ; y++){
                        tileM.changeTile(x, y, 5);
                    }
                }
            }

            return true;

        } else return false;
    }
}
