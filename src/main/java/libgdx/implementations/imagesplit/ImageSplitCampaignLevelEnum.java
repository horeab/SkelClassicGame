package libgdx.implementations.imagesplit;


import libgdx.campaign.CampaignLevel;

public enum ImageSplitCampaignLevelEnum implements CampaignLevel {

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
