package libgdx.implementations.resourcewars.spec.model.enums;

import libgdx.implementations.resourcewars.spec.logic.GameUtilManager;
import libgdx.resources.gamelabel.SpecificPropertiesUtils;
import org.apache.commons.lang3.StringUtils;

public enum ResourceType {

    //@formatter:off
    RES1(1, 600), //~30.000
    RES2(2, 240), //~12.000
    RES3(3, 100), //~5.000
    RES4(4, 60), //~3.000
    RES5(5, 40), //~2.000
    RES6(6, 22), //~1.100
    RES7(7, 20), //~1.000
    RES8(8, 18), //~800
    RES9(9, 8), //~400
    RES10(10, 5), //~250
    RES11(11, 4), //~200
    RES12(12, 3), //~150
    ;
    //@formatter:on

    private int index;

    private float standardPricePercent;

    private ResourceType(int index, float standardPricePercent) {
        this.index = index;
        this.standardPricePercent = standardPricePercent;
    }

    public String getDisplayName() {
        return StringUtils.capitalize(SpecificPropertiesUtils.getText("resource" + index));
    }

    public int getStandardPrice() {
        return GameUtilManager.getPriceBasedOnStartingBudgetWithPercent(standardPricePercent);
    }

}
