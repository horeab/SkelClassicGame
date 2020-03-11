package libgdx.implementations.resourcewars.spec.model;

import libgdx.implementations.resourcewars.spec.model.enums.Location;
import libgdx.implementations.resourcewars.spec.model.enums.ResourceType;
import libgdx.implementations.resourcewars.spec.model.resource.ResourceMarket;
import libgdx.implementations.resourcewars.spec.util.RandomPriceGenerator;
import libgdx.implementations.resourcewars.spec.util.RandomResourceGenerator;

import java.util.ArrayList;
import java.util.List;

public class Market {

    private List<ResourceMarket> availableResources;

    private Location currentLocation;

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public Market() {
    }

    public void setCurrentLocation(Location currentLocation, List<ResourceType> resourcesInInventory) {
        this.currentLocation = currentLocation;
        List<ResourceMarket> availableResources = new ArrayList<ResourceMarket>();
        List<ResourceType> randomResources = new ArrayList<ResourceType>();
        randomResources = RandomResourceGenerator.getAvailableResources(randomResources, resourcesInInventory);
        for (ResourceType drgType : randomResources) {
            ResourceMarket resource = new ResourceMarket();
            resource.setResourceType(drgType);
            int resourcePrice = RandomPriceGenerator.getVariableStandardPrice(drgType.getStandardPrice());
            if (currentLocation.isResourceCheapInThisLocation(drgType)) {
                resourcePrice = RandomPriceGenerator.getRandomMajorPrice(drgType, false);
            } else if (currentLocation.isResourceExpensiveInThisLocation(drgType)) {
                resourcePrice = RandomPriceGenerator.getRandomMajorPrice(drgType, true);
            }
            resource.setPrice(resourcePrice);
            availableResources.add(resource);
        }
        this.availableResources = availableResources;
    }

    public List<ResourceMarket> getAvailableResources() {
        return availableResources;
    }

}
