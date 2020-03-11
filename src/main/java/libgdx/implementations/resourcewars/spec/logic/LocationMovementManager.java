package libgdx.implementations.resourcewars.spec.logic;

import libgdx.implementations.resourcewars.spec.model.CurrentGame;
import libgdx.implementations.resourcewars.spec.model.enums.Location;

public class LocationMovementManager {

	private CurrentGame currentGame;
	private InGameStoreManager storeManager;
	private HealthManager healthManager;

	public LocationMovementManager(CurrentGame currentGame) {
		this.currentGame = currentGame;
		storeManager = new InGameStoreManager();
		healthManager = new HealthManager(currentGame);
	}

	public void passDayButStayInSameLocation() {
		Location currentLocation = currentGame.getMarket().getCurrentLocation();
		increaseDaysPassed();
		healthManager.processHealth();
		currentGame.getMarket().setCurrentLocation(currentLocation, currentGame.getMyInventory().getAvailableResourcesByType());
	}

	public void processHealthAndThreat(Location currentLocation) {
		healthManager.processHealth();
		increaseThreat(currentLocation);
	}

	public void passDayAndMoveLocation(Location newLocation) {
		if (storeManager.isLocationUnlocked(newLocation)) {
			spendMoneyOnTravel(newLocation);
		} else {
			unlockLocation(newLocation);
		}
		processHealthAndThreat(newLocation);
		increaseDaysPassed();
		currentGame.getMarket().setCurrentLocation(newLocation, currentGame.getMyInventory().getAvailableResourcesByType());
	}

	private void spendMoneyOnTravel(Location newLocation) {
		currentGame.getMyInventory().setBudget(currentGame.getMyInventory().getBudget() - newLocation.getTravelPrice(currentGame.getDaysPassed()));
	}

	private void increaseThreat(Location location) {
		currentGame.getPlayerInfo().setThreat(currentGame.getPlayerInfo().getThreat() + location.getThreatIncrease());
	}

	private void increaseDaysPassed() {
		currentGame.setDaysPassed(currentGame.getDaysPassed() + 1);
	}

	private void unlockLocation(Location location) {
		storeManager.unlockLocation(location);
		currentGame.getMyInventory().setBudget(currentGame.getMyInventory().getBudget() - location.getUnlockPrice());
	}
}
