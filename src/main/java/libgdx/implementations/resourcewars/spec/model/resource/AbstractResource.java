package libgdx.implementations.resourcewars.spec.model.resource;

import libgdx.implementations.resourcewars.spec.model.enums.ResourceType;

public class AbstractResource {

	private ResourceType resourceType;

	private int price;
	
	public AbstractResource() {
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public ResourceType getResourceType() {
		return resourceType;
	}

	public void setResourceType(ResourceType resourceType) {
		this.resourceType = resourceType;
	}

	public boolean equalsResourceType(AbstractResource resourceToCompare) {
		return this.getResourceType().equals(resourceToCompare.getResourceType());
	}

}
