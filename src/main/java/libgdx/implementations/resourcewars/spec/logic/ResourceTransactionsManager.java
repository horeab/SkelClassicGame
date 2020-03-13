package libgdx.implementations.resourcewars.spec.logic;

import libgdx.implementations.resourcewars.spec.model.CurrentGame;
import libgdx.implementations.resourcewars.spec.model.Inventory;
import libgdx.implementations.resourcewars.spec.model.enums.ResourceType;
import libgdx.implementations.resourcewars.spec.model.resource.AbstractResource;
import libgdx.implementations.resourcewars.spec.model.resource.ResourceInventory;

import java.util.List;

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

        moneySpentOnResource(resourceToBuy, priceToPayForThisAmount);
        amountBoughtOfThisResource(resourceToBuy, boughtAmount);
    }

    private void moneySpentOnResource(AbstractResource resourceToBuy, int priceToPayForThisAmount) {
        Integer moneySpentOnResource = currentGame.getMoneySpentOnBuyingResources()
                .get(resourceToBuy.getResourceType());
        if (moneySpentOnResource == null) {
            currentGame.getMoneySpentOnBuyingResources().put(resourceToBuy.getResourceType(), priceToPayForThisAmount);
        } else {
            currentGame.getMoneySpentOnBuyingResources().put(resourceToBuy.getResourceType(),
                    priceToPayForThisAmount + moneySpentOnResource);
        }
    }

    private void amountBoughtOfThisResource(AbstractResource resourceToBuy, int boughtAmount) {
        Integer amount = currentGame.getNrOfBoughtResources().get(resourceToBuy.getResourceType());
        if (amount == null) {
            currentGame.getNrOfBoughtResources().put(resourceToBuy.getResourceType(), boughtAmount);
        } else {
            currentGame.getNrOfBoughtResources().put(resourceToBuy.getResourceType(), amount + boughtAmount);
        }
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

        moneyWonOnResource(resourceToSell, moneyWonOnSellgingThisResource);

        calculateReputation();

        locationMovementManager.processHealthAndThreat(currentGame.getMarket().getCurrentLocation());
    }

    private void moneyWonOnResource(AbstractResource resourceToSell, int moneyWonOnSellgingThisResource) {
        Integer moneyWonOnResource = currentGame.getMoneyWonOnSellingResources().get(resourceToSell.getResourceType());
        if (moneyWonOnResource == null) {
            currentGame.getMoneyWonOnSellingResources().put(resourceToSell.getResourceType(),
                    moneyWonOnSellgingThisResource);
        } else {
            currentGame.getMoneyWonOnSellingResources().put(resourceToSell.getResourceType(),
                    moneyWonOnSellgingThisResource + moneyWonOnResource);
        }
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

    public static boolean affordResource(int resourcePrice, int budget) {
        return budget > resourcePrice;
    }

    public static int getResourceAmountYouAfford(int resourcePrice, int budget) {
        return budget / resourcePrice;
    }

    public static boolean isResourceAvailableForSelling(ResourceType resource,
                                                        List<? extends AbstractResource> availableResources) {
        boolean resourceAvailable = false;
        for (AbstractResource marketResource : availableResources) {
            if (resource.equals(marketResource.getResourceType())) {
                resourceAvailable = true;
                break;
            }
        }
        return resourceAvailable;
    }

    private void calculateReputation() {
        int wonMoney = totalMoneyWonSelling();
        int reputation = currentGame.getPlayerInfo().getReputation();
        int increaseReputationWithPoints = increaseReputationWithPoints(wonMoney,
                wonMoney - currentGame.getPlayerInfo().getReputationIncreasedForThisWonMoney());
        if (increaseReputationWithPoints > 0) {
            currentGame.getPlayerInfo().setReputationIncreasedForThisWonMoney(wonMoney);
        }
        reputation = reputation + increaseReputationWithPoints;
        currentGame.getPlayerInfo().setReputation(reputation);
        if (currentGame.getPlayerInfo().getReputation() >= 100) {
            GameUtilManager gameUtilManager = new GameUtilManager();
            gameUtilManager.gameFinishedSuccessFully(currentGame.getDaysPassed());
        }
    }

    private int increaseReputationWithPoints(int moneyWon, int moneyWonFromLastIncrease) {
        int increaseReputation = 0;

        int STEP_ONE = 20000;
        int STEP_ONE_MONEY_WON_FROM_LAST = 1000;

        int STEP_TWO = 200000;
        int STEP_TWO_MONEY_WON_FROM_LAST = 10000;

        int STEP_THREE = 500000;
        int STEP_THREE_MONEY_WON_FROM_LAST = 25000;

        int STEP_FOUR = 1000000;
        int STEP_FOUR_MONEY_WON_FROM_LAST = 50000;

        int STEP_FIVE_MONEY_WON_FROM_LAST = 100000;

        if (moneyWon < STEP_ONE) {
            if (moneyWonFromLastIncrease > STEP_ONE_MONEY_WON_FROM_LAST) {
                increaseReputation = moneyWonFromLastIncrease / STEP_ONE_MONEY_WON_FROM_LAST;
            }
        } else if (moneyWon < STEP_TWO) {
            if (moneyWonFromLastIncrease > STEP_TWO_MONEY_WON_FROM_LAST) {
                increaseReputation = moneyWonFromLastIncrease / STEP_TWO_MONEY_WON_FROM_LAST;
            }
        } else if (moneyWon < STEP_THREE) {
            if (moneyWonFromLastIncrease > STEP_THREE_MONEY_WON_FROM_LAST) {
                increaseReputation = moneyWonFromLastIncrease / STEP_THREE_MONEY_WON_FROM_LAST;
            }
        } else if (moneyWon < STEP_FOUR) {
            if (moneyWonFromLastIncrease > STEP_FOUR_MONEY_WON_FROM_LAST) {
                increaseReputation = moneyWonFromLastIncrease / STEP_FOUR_MONEY_WON_FROM_LAST;
            }
        } else {
            if (moneyWonFromLastIncrease > STEP_FIVE_MONEY_WON_FROM_LAST) {
                increaseReputation = moneyWonFromLastIncrease / STEP_FIVE_MONEY_WON_FROM_LAST;
            }
        }

        return increaseReputation;
    }

    private int totalMoneyWonSelling() {
        int wonMoney = 0;
        for (java.util.Map.Entry<ResourceType, Integer> entry : currentGame.getMoneyWonOnSellingResources()
                .entrySet()) {
            wonMoney = wonMoney + entry.getValue();
        }
        return wonMoney;
    }
}
