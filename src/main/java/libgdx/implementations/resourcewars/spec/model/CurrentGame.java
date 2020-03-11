package libgdx.implementations.resourcewars.spec.model;

import libgdx.implementations.resourcewars.spec.logic.GameUtilManager;
import libgdx.implementations.resourcewars.spec.model.enums.Location;
import libgdx.implementations.resourcewars.spec.model.enums.ResourceType;
import libgdx.implementations.resourcewars.spec.model.resource.ResourceInventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CurrentGame {

	private Inventory myInventory;

	private PlayerInfo playerInfo;

	private Market market;

	private int daysPassed;

	private Map<ResourceType, Integer> nrOfBoughtResources = new HashMap<ResourceType, Integer>();

	private Map<ResourceType, Integer> moneySpentOnBuyingResources = new HashMap<ResourceType, Integer>();
	private Map<ResourceType, Integer> moneyWonOnSellingResources = new HashMap<ResourceType, Integer>();

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

	public Map<ResourceType, Integer> getNrOfBoughtResources() {
		return nrOfBoughtResources;
	}

	public Integer getNrOfSoldResources(ResourceType resource) {
		Integer nrOfBoughtResource = nrOfBoughtResources.get(resource);
		if (nrOfBoughtResource != null) {
			nrOfBoughtResource = nrOfBoughtResource - myInventory.getByResourceType(resource).getAmount();
		}
		return nrOfBoughtResource;
	}

	public Map<ResourceType, Integer> getMoneySpentOnBuyingResources() {
		return moneySpentOnBuyingResources;
	}

	public Map<ResourceType, Integer> getMoneyWonOnSellingResources() {
		return moneyWonOnSellingResources;
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
