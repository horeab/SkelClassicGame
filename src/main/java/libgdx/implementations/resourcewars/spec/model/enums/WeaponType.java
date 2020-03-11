package libgdx.implementations.resourcewars.spec.model.enums;

import libgdx.implementations.resourcewars.spec.logic.GameUtilManager;
import libgdx.resources.gamelabel.SpecificPropertiesUtils;

public enum WeaponType {

    //@formatter:off
    GRENADE(1, 50, 1),
    UZZI(2, 200, 5), //
    TANK(3, 1000, 30), //
    ;
    //@formatter:on

    public static final int MAX_THREAT = 100;

    public static final int MAX_WEAPON_THREAT = MAX_THREAT / 3;

    public static final int MIN_WEAPON_THREAT = MAX_THREAT / 99;

    private int index;
    private int standardPricePercent;
    private int threatToReduce;

    private WeaponType(int index, int standardPricePercent, int threatToReduce) {
        this.index = index;
        this.standardPricePercent = standardPricePercent;
        this.threatToReduce = threatToReduce;
    }

    public String getDisplayName() {
        return SpecificPropertiesUtils.getText("weapon" + index);
    }

    public int getCurrentPrice(int daysPassed) {
        return GameUtilManager.getPriceBasedOnStartingBudgetWithPercentAndDaysPassed(standardPricePercent, daysPassed);
    }

    public static WeaponType getEnumForName(String name) {
        for (WeaponType weaponType : values()) {
            if (name.equals(weaponType.toString())) {
                return weaponType;
            }
        }
        return null;
    }

    public int getThreatReduce() {
        return threatToReduce;
    }


}
