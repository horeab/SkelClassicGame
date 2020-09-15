package libgdx.implementations.buylow.spec;

public enum BuyLowResource {

    WOOD(10),
    IRON(50),
    GOLD(500),
    DIAMOND(1000),
    ;

    private int price;

    BuyLowResource(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }
}
