package libgdx.implementations.resourcewars.spec.util;

import libgdx.implementations.resourcewars.spec.logic.GameUtilManager;
import libgdx.implementations.resourcewars.spec.model.enums.ResourceType;

import java.util.List;
import java.util.Random;

public class RandomPriceGenerator {

    public static int getVariableStandardPrice(int standardPrice) {
        int differencePower = new Random().nextInt(getDifferencePowerByStandardPrice(standardPrice));
        int variablePrice = new Random().nextInt(standardPrice / (10 - differencePower));
        boolean plusOrMinus = Math.random() < 0.5;
        if (plusOrMinus) {
            return standardPrice + variablePrice;
        } else {
            if (standardPrice - variablePrice < 0) {
                return 0;
            } else {
                return standardPrice - variablePrice;
            }
        }
    }

    private static ResourceType getRandomResource() {
        return ResourceType.values()[new Random().nextInt(ResourceType.values().length)];
    }

    public static int getRandomMajorPrice(ResourceType resource, Boolean plus) {
        float price = 0;
        if (plus == null) {
            plus = Math.random() < 0.5;
        }
        if (plus) {
            float variablePercentage = new Random().nextInt(getPercentForMaxPrice(resource.getStandardPrice()));
            price = resource.getStandardPrice() + (variablePercentage / 100) * resource.getStandardPrice();
        } else {
            float variablePercentage = 30 - new Random().nextInt(15);
            price = resource.getStandardPrice() - (variablePercentage / 100) * resource.getStandardPrice();
        }
        return getVariableStandardPrice((int) price);
    }

    public static ResourceType getRandomMajorPriceResource(List<ResourceType> resources) {
        ResourceType randomResource = getRandomResource();
        ResourceType resourceToReturn = null;
        for (ResourceType resource : resources) {
            if (resource.equals(randomResource)) {
                resourceToReturn = resource;
            }
        }
        return resourceToReturn;
    }

    public static boolean isHigherPrice(int price, ResourceType resource) {
        return price > resource.getStandardPrice();
    }

    private static int getPercentForMaxPrice(int standardPrice) {
        if (standardPrice <= GameUtilManager.getPriceBasedOnStartingBudgetWithPercent(7)) {
            return 250;
        } else if (standardPrice <= GameUtilManager.getPriceBasedOnStartingBudgetWithPercent(16)) {
            return 250;
        } else if (standardPrice <= GameUtilManager.getPriceBasedOnStartingBudgetWithPercent(32)) {
            return 200;
        } else if (standardPrice <= GameUtilManager.getPriceBasedOnStartingBudgetWithPercent(60)) {
            return 150;
        } else if (standardPrice <= GameUtilManager.getPriceBasedOnStartingBudgetWithPercent(80)) {
            return 120;
        } else if (standardPrice <= GameUtilManager.getPriceBasedOnStartingBudgetWithPercent(120)) {
            return 80;
        } else if (standardPrice <= GameUtilManager.getPriceBasedOnStartingBudgetWithPercent(400)) {
            return 60;
        } else {
            return 60;
        }
    }

    private static int getDifferencePowerByStandardPrice(int standardPrice) {
        if (standardPrice <= GameUtilManager.getPriceBasedOnStartingBudgetWithPercent(7)) {
            return 9;
        } else if (standardPrice <= GameUtilManager.getPriceBasedOnStartingBudgetWithPercent(16)) {
            return 9;
        } else if (standardPrice <= GameUtilManager.getPriceBasedOnStartingBudgetWithPercent(32)) {
            return 8;
        } else if (standardPrice <= GameUtilManager.getPriceBasedOnStartingBudgetWithPercent(60)) {
            return 7;
        } else if (standardPrice <= GameUtilManager.getPriceBasedOnStartingBudgetWithPercent(80)) {
            return 7;
        } else if (standardPrice <= GameUtilManager.getPriceBasedOnStartingBudgetWithPercent(120)) {
            return 5;
        } else if (standardPrice <= GameUtilManager.getPriceBasedOnStartingBudgetWithPercent(400)) {
            return 4;
        } else {
            return 4;
        }
    }
}
