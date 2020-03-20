package libgdx.implementations.resourcewars.spec.model.enums;

import libgdx.implementations.resourcewars.spec.logic.GameUtilManager;
import libgdx.resources.gamelabel.SpecificPropertiesUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Location {

    //@formatter:off
    LOC1(1, Arrays.asList(ResourceType.RES5, ResourceType.RES6), Arrays.asList(ResourceType.RES1), 2, null, 3),
    LOC2(2, Arrays.asList(ResourceType.RES12, ResourceType.RES10), Arrays.asList(ResourceType.RES1, ResourceType.RES2, ResourceType.RES3), 2, 75, 3),
    LOC3(3, Arrays.asList(ResourceType.RES9), Arrays.asList(ResourceType.RES7, ResourceType.RES8, ResourceType.RES10), 2, 150, 3),
    LOC4(4, Arrays.asList(ResourceType.RES7, ResourceType.RES9, ResourceType.RES10), Arrays.asList(ResourceType.RES12, ResourceType.RES11), 2, 250, 3),
    LOC5(5, Arrays.asList(ResourceType.RES2, ResourceType.RES11), Arrays.asList(ResourceType.RES10, ResourceType.RES8), 2, 350, 3),
    LOC6(6, Arrays.asList(ResourceType.RES1, ResourceType.RES5, ResourceType.RES10), Arrays.asList(ResourceType.RES11, ResourceType.RES12), 2, 800, 3),
    ;
    //@formatter:on

    private int index;

    private List<ResourceType> cheapResources;
    private List<ResourceType> expensiveResources;

    private Integer threatIncrease;
    private Integer unlockPricePercent;
    private Integer travelPricePercent;

    private Location(int index, List<ResourceType> cheapResources, List<ResourceType> expensiveResources, Integer threatIncrease, Integer unlockPricePercent,
                     Integer travelPricePercent) {
        this.index = index;
        this.cheapResources = cheapResources;
        this.expensiveResources = expensiveResources;
        this.threatIncrease = threatIncrease;
        this.unlockPricePercent = unlockPricePercent;
        this.travelPricePercent = travelPricePercent;
    }

    public Integer getTravelPrice(int daysPassed) {
        return GameUtilManager.getPriceBasedOnStartingBudgetWithPercentAndDaysPassed(travelPricePercent, daysPassed);
    }

    public Integer getThreatIncrease() {
        return threatIncrease;
    }

    public int getIndex() {
        return index;
    }

    public Integer getUnlockPrice() {
        return GameUtilManager.getPriceBasedOnStartingBudgetWithPercent(unlockPricePercent);
    }

    public List<ResourceType> getCheapResources() {
        if (cheapResources == null) {
            return new ArrayList<ResourceType>();
        }
        return cheapResources;
    }

    public List<ResourceType> getExpensiveResources() {
        if (expensiveResources == null) {
            return new ArrayList<ResourceType>();
        }
        return expensiveResources;
    }

    public String getDisplayName() {
        return SpecificPropertiesUtils.getText("loc" + index);
    }

    public boolean isResourceCheapInThisLocation(ResourceType resourceType) {
        for (ResourceType resource : getCheapResources()) {
            if (resourceType == resource) {
                return true;
            }
        }
        return false;
    }

    public boolean isResourceExpensiveInThisLocation(ResourceType resourceType) {
        for (ResourceType resource : getExpensiveResources()) {
            if (resourceType == resource) {
                return true;
            }
        }
        return false;
    }

}
