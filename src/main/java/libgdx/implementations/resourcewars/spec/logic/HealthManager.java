package libgdx.implementations.resourcewars.spec.logic;

import libgdx.implementations.resourcewars.spec.model.CurrentGame;
import libgdx.implementations.resourcewars.spec.model.Inventory;
import libgdx.implementations.resourcewars.spec.model.PlayerInfo;
import libgdx.implementations.resourcewars.spec.model.enums.OtherType;

import java.util.Random;

public class HealthManager {

    private static final int HEALTH_MINUS = 5;

    private CurrentGame currentGame;

    public HealthManager(CurrentGame currentGame) {
        this.currentGame = currentGame;
    }

    public void increaseMaxHealthContainerBought() {
        OtherType otherType = OtherType.HEALTH_INCREASE_50;
        PlayerInfo playerInfo = currentGame.getPlayerInfo();
        Inventory inventory = currentGame.getMyInventory();

        playerInfo.setHealthContainerMax(playerInfo.getHealthContainerMax() + otherType.getValue());
        inventory.setBudget(inventory.getBudget() - otherType.getCurrentPrice(currentGame.getDaysPassed()));
    }

    public void increaseHealthBought() {
        OtherType otherType = OtherType.HEALTH_POTION_10;
        PlayerInfo playerInfo = currentGame.getPlayerInfo();
        Inventory inventory = currentGame.getMyInventory();

        playerInfo.setHealth(playerInfo.getHealth() + otherType.getValue());
        inventory.setBudget(inventory.getBudget() - otherType.getCurrentPrice(currentGame.getDaysPassed()));
    }

    public void processHealth() {
        int randValue = new Random().nextInt(101);
        if (randValue < currentGame.getPlayerInfo().getThreat()) {
            currentGame.getPlayerInfo().setHealth(currentGame.getPlayerInfo().getHealth() - HEALTH_MINUS);
            if (currentGame.getPlayerInfo().getHealth() <= 0) {
                GameUtilManager gameUtilManager = new GameUtilManager();
                gameUtilManager.gameOver();
            }
        }
    }
}
