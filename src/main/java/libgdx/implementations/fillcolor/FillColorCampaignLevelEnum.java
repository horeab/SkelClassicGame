package libgdx.implementations.fillcolor;


import libgdx.campaign.CampaignLevel;

public enum FillColorCampaignLevelEnum implements CampaignLevel {

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
