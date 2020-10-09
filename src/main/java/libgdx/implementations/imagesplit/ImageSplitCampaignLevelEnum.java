package libgdx.implementations.imagesplit;


import libgdx.campaign.CampaignLevel;

public enum ImageSplitCampaignLevelEnum implements CampaignLevel {

    LEVEL_0_0(3, 3, ImageSplitSpecificResource.i1),
    LEVEL_0_1(4, 4, ImageSplitSpecificResource.i1),
    LEVEL_0_2(5, 5, ImageSplitSpecificResource.i1),
    LEVEL_0_3(6, 6, ImageSplitSpecificResource.i1),
    LEVEL_0_4(7, 7, ImageSplitSpecificResource.i1),
    ;

    private int cols;
    private int rows;
    private ImageSplitSpecificResource res;

    ImageSplitCampaignLevelEnum(int cols, int rows, ImageSplitSpecificResource res) {
        this.cols = cols;
        this.rows = rows;
        this.res = res;
    }

    public int getCols() {
        return cols;
    }

    public int getRows() {
        return rows;
    }

    public ImageSplitSpecificResource getRes() {
        return res;
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
