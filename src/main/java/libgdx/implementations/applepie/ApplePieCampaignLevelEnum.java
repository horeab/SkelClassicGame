package libgdx.implementations.applepie;


import libgdx.campaign.CampaignLevel;

public enum ApplePieCampaignLevelEnum implements CampaignLevel {

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
