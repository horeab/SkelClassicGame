package libgdx.implementations.resourcewars.spec.model;

import libgdx.implementations.resourcewars.spec.model.enums.ResourceType;
import libgdx.implementations.resourcewars.spec.model.resource.ResourceInventory;

import java.util.ArrayList;
import java.util.List;

public class Inventory {

    private static final int STARTING_MAX_CONTAINER = 100;

    private List<ResourceInventory> availableResources;

    private int budget;

    private int containerMax = STARTING_MAX_CONTAINER;

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

    public ResourceInventory getByResourceType(ResourceType resourceType) {
        for (ResourceInventory resourceInventory : availableResources) {
            if (resourceInventory.getResourceType() == resourceType) {
                return resourceInventory;
            }
        }
        return null;
    }

    private int getTotalAmountOfResources() {
        int total = 0;
        for (ResourceInventory resource : availableResources) {
            total = total + resource.getAmount();
        }
        return total;
    }

    public int getContainerSpaceLeft() {
        return containerMax - getTotalAmountOfResources();
    }

    public int getContainerMax() {
        return containerMax;
    }

    public void setContainerMax(int containerMax) {
        this.containerMax = containerMax;
    }

}
