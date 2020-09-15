package libgdx.implementations.buylow;


import libgdx.campaign.CampaignLevel;

public enum BuyLowCampaignLevelEnum implements CampaignLevel {

    ;

    @Override
    public int getIndex() {
        return ordinal();
    }

    @Override
    public String getName() {
        return name();
    }

}
