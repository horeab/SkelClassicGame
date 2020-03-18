package libgdx.implementations.resourcewars.spec.model;

import libgdx.implementations.resourcewars.spec.model.enums.ResourceType;
import libgdx.implementations.resourcewars.spec.model.resource.ResourceInventory;

import java.util.ArrayList;
import java.util.List;

public class Inventory {

    public static final int STARTING_MAX_CONTAINER = 99;
    public static final int MAX_ITEMS_IN_INVENTORY = 6;

    private List<ResourceInventory> availableResources;

    private int budget;

    public Inventory() {
    }

    public List<ResourceInventory> getAvailableResources() {
        return availableResources;
    }

    public List<ResourceType> getAvailableResourcesByType() {
        List<ResourceType> availableResourcesTypes = new ArrayList<ResourceType>();
        for (ResourceInventory availableResource : availableResources) {
            availableResourcesTypes.add(availableResource.getResourceType());
        }
        return availableResourcesTypes;
    }

    public void setAvailableResources(List<ResourceInventory> availableResources) {
        this.availableResources = availableResources;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    private int getTotalAmountOfResources() {
        int total = 0;
        for (ResourceInventory resource : availableResources) {
            total = total + resource.getAmount();
        }
        return total;
    }

    public int getContainerSpaceLeft() {
        return STARTING_MAX_CONTAINER - getTotalAmountOfResources();
    }


}
