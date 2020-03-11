package libgdx.implementations.resourcewars;


import libgdx.campaign.CampaignLevel;
import libgdx.implementations.math.spec.MathLevel;

public enum ResourceWarsCampaignLevelEnum implements CampaignLevel {

    //This order is displayed on the campaign screen
    ////
    ;

    private MathLevel mathLevel;

    ResourceWarsCampaignLevelEnum(MathLevel mathLevel) {
        this.mathLevel = mathLevel;
    }

    public MathLevel getMathLevel() {
        return mathLevel;
    }

    @Override
    public int getIndex() {
        return ordinal();
    }

    @Override
    public String getName() {
        return name();
    }

}
