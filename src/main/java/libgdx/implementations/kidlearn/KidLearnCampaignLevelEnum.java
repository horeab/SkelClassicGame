package libgdx.implementations.kidlearn;


import libgdx.campaign.CampaignLevel;

public enum KidLearnCampaignLevelEnum implements CampaignLevel {

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
