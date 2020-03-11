package libgdx.implementations.resourcewars.spec.model.resource;

import libgdx.implementations.resourcewars.spec.model.enums.ResourceType;

public class ResourceInventory extends AbstractResource {

    private int amount;

    public ResourceInventory(ResourceType resourceType, int amount, int price) {
        this.setResourceType(resourceType);
        this.amount = amount;
        this.setPrice(price);
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

}
