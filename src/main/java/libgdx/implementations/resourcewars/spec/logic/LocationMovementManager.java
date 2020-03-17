package libgdx.implementations.resourcewars.spec.logic;

import libgdx.implementations.resourcewars.spec.model.CurrentGame;
import libgdx.implementations.resourcewars.spec.model.enums.Location;

public class LocationMovementManager {

	private CurrentGame currentGame;
	private InGameStoreManager storeManager;

	public LocationMovementManager(CurrentGame currentGame) {
		this.currentGame = currentGame;
		storeManager = new InGameStoreManager();
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

	public boolean areAllLocationsUnlocked(){
        return nrOfLocationsUnlocked()==Location.values().length;
    }

	public int nrOfLocationsUnlocked(){
		int nrOfLocationsUnlocked = 0;
		InGameStoreManager inGameStoreManager = new InGameStoreManager();
		for (Location location : Location.values()) {
			if (inGameStoreManager.isLocationUnlocked(location)) {
				nrOfLocationsUnlocked++;
			}
		}
		return nrOfLocationsUnlocked;
	}

	private void increaseDaysPassed() {
		new InGameStoreManager().saveGame(currentGame);
		currentGame.setDaysPassed(currentGame.getDaysPassed() + 1);
	}

	private void unlockLocation(Location location) {
		storeManager.unlockLocation(location);
		currentGame.getMyInventory().setBudget(currentGame.getMyInventory().getBudget() - location.getUnlockPrice());
	}
}
