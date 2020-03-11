package libgdx.implementations.resourcewars.spec.model.enums;

import libgdx.implementations.resourcewars.spec.logic.GameUtilManager;
import libgdx.resources.gamelabel.SpecificPropertiesUtils;

public enum OtherType {

    //@formatter:off
    HEALTH_POTION_10(1, 5, 350),
    HEALTH_INCREASE_50(2, 50, 10000),
    ;
    //@formatter:on

    private int index;
    private int value;
    private int startingPricePercent;

    private OtherType(int index, int value, int startingPricePercent) {
        this.index = index;
        this.value = value;
        this.startingPricePercent = startingPricePercent;
    }

    public static OtherType getEnumForName(String name) {
        for (OtherType otherType : values()) {
            if (name.equals(otherType.toString())) {
                return otherType;
            }
        }
        return null;
    }

    public String getDisplayName() {
        return SpecificPropertiesUtils.getText("other" + index);
    }

    public String getDescription() {
        return SpecificPropertiesUtils.getText("otherDescr" + index);
    }

    public int getStartingPricePercent() {
        return startingPricePercent;
    }

    public int getValue() {
        return value;
    }

    public int getStartingPrice() {
        return GameUtilManager.getPriceBasedOnStartingBudgetWithPercent(startingPricePercent);
    }

    public int getCurrentPrice(int daysPassed) {
        int startingPrice = getStartingPrice();
        if (this == HEALTH_INCREASE_50) {
            return startingPrice;
        }
        int percentToIncreasePrice = daysPassed / GameUtilManager.DAYS_TO_CHANGE;

        int priceToIncrease = (int) ((float) startingPrice * ((float) percentToIncreasePrice / 100));
        return startingPrice + priceToIncrease;
    }


}
