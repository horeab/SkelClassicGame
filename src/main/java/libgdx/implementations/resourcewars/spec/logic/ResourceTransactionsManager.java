package libgdx.implementations.resourcewars.spec.logic;

import libgdx.implementations.resourcewars.spec.creator.ContainerManager;
import libgdx.implementations.resourcewars.spec.model.CurrentGame;
import libgdx.implementations.resourcewars.spec.model.Inventory;
import libgdx.implementations.resourcewars.spec.model.resource.AbstractResource;
import libgdx.implementations.resourcewars.spec.model.resource.ResourceInventory;

public class ResourceTransactionsManager {

    private CurrentGame currentGame;
    private Inventory inventory;
    private LocationMovementManager locationMovementManager;

    public ResourceTransactionsManager(CurrentGame currentGame) {
        this.currentGame = currentGame;
        this.locationMovementManager = new LocationMovementManager(currentGame);
        this.inventory = currentGame.getMyInventory();
    }

    public void buyResource(AbstractResource resourceToBuy, int boughtAmount) {
        int priceToPayForThisAmount = getAmountToModifyBudget(boughtAmount, resourceToBuy.getPrice());
        inventory.setBudget(inventory.getBudget() - priceToPayForThisAmount);
        ResourceInventory currentResource = new ResourceInventory(resourceToBuy.getResourceType(), boughtAmount,
                resourceToBuy.getPrice());
        addBoughtResource(currentResource, boughtAmount);
    }


    private void addBoughtResource(ResourceInventory boughtResource, int boughtAmount) {
        boolean resourceFound = false;
        for (ResourceInventory resource : inventory.getAvailableResources()) {
            if (resource.equalsResourceType(boughtResource)) {
                resource.setAmount(resource.getAmount() + boughtAmount);
                resourceFound = true;
                break;
            }
        }
        if (!resourceFound) {
            inventory.getAvailableResources().add(boughtResource);
        }
    }

    private int getAmountToModifyBudget(int resourceAmount, int resourcePrice) {
        return resourceAmount * resourcePrice;
    }

    private int getMarketPriceForResource(AbstractResource resource) {
        int marketPrice = 0;
        if (resource != null) {
            for (AbstractResource marketResource : currentGame.getMarket().getAvailableResources()) {
                if (marketResource.getResourceType() == resource.getResourceType()) {
                    marketPrice = marketResource.getPrice();
                }
            }
        }
        return marketPrice;
    }

    public void sellResource(AbstractResource resourceToSell, int soldAmount) {
        int moneyWonOnSellgingThisResource = getAmountToModifyBudget(soldAmount,
                getMarketPriceForResource(resourceToSell));
        inventory.setBudget(inventory.getBudget() + moneyWonOnSellgingThisResource);
        ResourceInventory currentResource = new ResourceInventory(resourceToSell.getResourceType(), soldAmount,
                getMarketPriceForResource(resourceToSell));
        removeSoldResource(inventory, currentResource, soldAmount);

        calculateReputation();
    }

    private void removeSoldResource(Inventory inventory, ResourceInventory soldResource, int soldAmount) {
        for (ResourceInventory resource : inventory.getAvailableResources()) {
            if (resource.equalsResourceType(soldResource)) {
                if (resource.getAmount() - soldAmount <= 0) {
                    inventory.getAvailableResources().remove(resource);
                    break;
                } else {
                    resource.setAmount(resource.getAmount() - soldAmount);
                    break;
                }
            }
        }
    }

    public static int getResourceAmountYouAfford(int resourcePrice, int budget) {
        return budget / resourcePrice;
    }

    public void calculateReputation() {
        int reputation;
        LocationMovementManager locationMovementManager = new LocationMovementManager(currentGame);
        int nrOfLocationsUnlocked = locationMovementManager.nrOfLocationsUnlocked();
        reputation = (nrOfLocationsUnlocked - 1) * ContainerManager.LOCATION_UNLOCK_REPUTATION;
        if (locationMovementManager.areAllLocationsUnlocked() && currentGame.getMyInventory().getBudget() >= ContainerManager.FINAL_BUDGET_TO_REACH) {
            reputation = reputation + ContainerManager.FINAL_BUDGET_TO_REACH_REPUTATION;
        }
        currentGame.getPlayerInfo().setReputation(reputation);
    }

}
