package libgdx.implementations.memory;


import libgdx.campaign.CampaignLevel;
import libgdx.implementations.math.spec.MathLevel;

public enum MemoryCampaignLevelEnum implements CampaignLevel {

    //This order is displayed on the campaign screen
    LEVEL_0_0(MathLevel._0),
    LEVEL_0_1(MathLevel._1),
    LEVEL_0_2(MathLevel._2),
    LEVEL_0_3(MathLevel._3),
    LEVEL_0_4(MathLevel._4),
    LEVEL_0_5(MathLevel._5),
    LEVEL_0_6(MathLevel._6),
    LEVEL_0_7(MathLevel._7),
    ////
    ;

    private MathLevel mathLevel;

    MemoryCampaignLevelEnum(MathLevel mathLevel) {
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
