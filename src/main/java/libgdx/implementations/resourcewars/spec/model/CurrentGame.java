package libgdx.implementations.resourcewars.spec.model;

import libgdx.implementations.resourcewars.spec.logic.GameUtilManager;
import libgdx.implementations.resourcewars.spec.model.enums.Location;
import libgdx.implementations.resourcewars.spec.model.resource.ResourceInventory;

import java.util.ArrayList;
import java.util.List;

public class CurrentGame {

	private Inventory myInventory;

	private PlayerInfo playerInfo;

	private Market market;

	private int daysPassed;

	public CurrentGame() {
		myInventory = new Inventory();
		myInventory.setBudget(GameUtilManager.STARTING_BUDGET);
		List<ResourceInventory> availableResources = new ArrayList<ResourceInventory>();
		myInventory.setAvailableResources(availableResources);
		market = new Market();
		market.setCurrentLocation(Location.LOC1, myInventory.getAvailableResourcesByType());
		daysPassed = 0;
		playerInfo = new PlayerInfo();
	}

	public Inventory getMyInventory() {
		return myInventory;
	}

	public void setMyInventory(Inventory myInventory) {
		this.myInventory = myInventory;
	}

	public Market getMarket() {
		return market;
	}

	public void setMarket(Market market) {
		this.market = market;
	}

	public int getDaysPassed() {
		return daysPassed;
	}

	public void setDaysPassed(int daysPassed) {
		this.daysPassed = daysPassed;
	}

	public PlayerInfo getPlayerInfo() {
		return playerInfo;
	}

	public void setPlayerInfo(PlayerInfo playerInfo) {
		this.playerInfo = playerInfo;
	}

}
