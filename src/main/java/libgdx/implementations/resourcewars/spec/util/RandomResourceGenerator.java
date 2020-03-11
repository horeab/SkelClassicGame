package libgdx.implementations.resourcewars.spec.util;

import libgdx.implementations.resourcewars.spec.model.enums.ResourceType;

import java.util.List;
import java.util.Random;

public class RandomResourceGenerator {

	public static List<ResourceType> getAvailableResources(List<ResourceType> availableResources, List<ResourceType> resourcesInInventory) {
		for (ResourceType resource : ResourceType.values()) {
			int randomNr = new Random().nextInt(100);
			int range = 50;
			if (resourcesInInventory.contains(resource)) {
				range = 75;
			}
			if (randomNr < range) {
				availableResources.add(resource);
			}
			if (availableResources.size() > 5) {
				break;
			}
		}
		if (availableResources.size() < 4) {
			getAvailableResources(availableResources, resourcesInInventory);
		}
		return availableResources;
	}
}
