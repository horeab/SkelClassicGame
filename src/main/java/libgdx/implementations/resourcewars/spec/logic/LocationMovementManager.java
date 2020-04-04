package libgdx.implementations.resourcewars.spec.logic;

import libgdx.game.Game;
import libgdx.implementations.resourcewars.spec.creator.ContainerManager;
import libgdx.implementations.resourcewars.spec.model.CurrentGame;
import libgdx.implementations.resourcewars.spec.model.enums.Location;

public class LocationMovementManager {

    private CurrentGame currentGame;
    private GamePreferencesManager storeManager;
    private GamePreferencesManager gamePreferencesManager = new GamePreferencesManager();

    public LocationMovementManager(CurrentGame currentGame) {
        this.currentGame = currentGame;
        storeManager = new GamePreferencesManager();
    }

    public void passDayAndMoveLocation(Location newLocation) {
        if (storeManager.isLocationUnlocked(newLocation)) {
            spendMoneyOnTravel(newLocation);
        } else {
            unlockLocation(newLocation);
        }
        new ResourceTransactionsManager(currentGame).calculateReputation();
        increaseDaysPassed();
        currentGame.getMarket().setCurrentLocation(newLocation, currentGame.getMyInventory().getAvailableResourcesByType());
    }

    private void spendMoneyOnTravel(Location newLocation) {
        currentGame.getMyInventory().setBudget(currentGame.getMyInventory().getBudget() - newLocation.getTravelPrice(currentGame.getDaysPassed()));
    }

    public boolean areAllLocationsUnlocked() {
        return nrOfLocationsUnlocked() == Location.values().length;
    }

    public int nrOfLocationsUnlocked() {
        int nrOfLocationsUnlocked = 0;
        for (Location location : Location.values()) {
            if (gamePreferencesManager.isLocationUnlocked(location)) {
                nrOfLocationsUnlocked++;
            }
        }
        return nrOfLocationsUnlocked;
    }

    public Location nextLocationToUnlock() {
        Location locationToUnlock = null;
        for (Location location : Location.values()) {
            if (!gamePreferencesManager.isLocationUnlocked(location)) {
                locationToUnlock = location;
                break;
            }
        }
        return locationToUnlock;
    }

    public void increaseDaysPassed() {
        new GamePreferencesManager().saveGame(currentGame);
        if (currentGame.getDaysPassed() == 10
                || currentGame.getDaysPassed() == 25
                || currentGame.getDaysPassed() == 40
                || currentGame.getDaysPassed() == 58
        ) {
            Game.getInstance().getAppInfoService().showPopupAd(new Runnable() {
                @Override
                public void run() {
                    currentGame.setDaysPassed(currentGame.getDaysPassed() + 1);
                    ContainerManager.gameOver(currentGame);
                }
            });
        } else {
            currentGame.setDaysPassed(currentGame.getDaysPassed() + 1);
            ContainerManager.gameOver(currentGame);
        }
    }

    private void unlockLocation(Location location) {
        storeManager.unlockLocation(location);
        currentGame.getMyInventory().setBudget(currentGame.getMyInventory().getBudget() - location.getUnlockPrice());
    }
}
