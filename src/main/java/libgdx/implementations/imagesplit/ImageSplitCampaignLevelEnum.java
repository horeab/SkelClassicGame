package libgdx.implementations.imagesplit;


import java.util.Arrays;
import java.util.List;

import libgdx.campaign.CampaignLevel;
import libgdx.implementations.imagesplit.spec.ImageSplitGameType;

public enum ImageSplitCampaignLevelEnum implements CampaignLevel {

    LEVEL_0_0(2, 2, 1, ImageSplitSpecificResource.i0, Arrays.asList(ImageSplitGameType.SWAP, ImageSplitGameType.SLIDE)),
    LEVEL_0_1(2, 2, 1, ImageSplitSpecificResource.i1, Arrays.asList(ImageSplitGameType.SWAP, ImageSplitGameType.SLIDE)),
    LEVEL_0_2(3, 3, 2, ImageSplitSpecificResource.i2, Arrays.asList(ImageSplitGameType.SWAP, ImageSplitGameType.SLIDE, ImageSplitGameType.PUSH)),
    LEVEL_0_3(3, 3, 2, ImageSplitSpecificResource.i3, Arrays.asList(ImageSplitGameType.SWAP, ImageSplitGameType.SLIDE, ImageSplitGameType.PUSH)),
    LEVEL_0_4(4, 4, 2, ImageSplitSpecificResource.i4, Arrays.asList(ImageSplitGameType.SWAP, ImageSplitGameType.SLIDE, ImageSplitGameType.PUSH)),
    LEVEL_0_5(4, 4, 2, ImageSplitSpecificResource.i5, Arrays.asList(ImageSplitGameType.SWAP, ImageSplitGameType.SLIDE, ImageSplitGameType.PUSH)),
    LEVEL_0_6(5, 5, 4, ImageSplitSpecificResource.i6, Arrays.asList(ImageSplitGameType.SWAP, ImageSplitGameType.SLIDE, ImageSplitGameType.PUSH)),
    LEVEL_0_7(6, 6, 4, ImageSplitSpecificResource.i7, Arrays.asList(ImageSplitGameType.SWAP, ImageSplitGameType.SLIDE, ImageSplitGameType.PUSH)),
    LEVEL_0_8(7, 7, 4, ImageSplitSpecificResource.i8, Arrays.asList(ImageSplitGameType.SWAP, ImageSplitGameType.SLIDE, ImageSplitGameType.PUSH)),;

    private int cols;
    private int rows;
    private int showPopupAdIncrementValue;
    private ImageSplitSpecificResource res;
    private List<ImageSplitGameType> gameTypes;

    ImageSplitCampaignLevelEnum(int cols, int rows, int showPopupAdIncrementValue, ImageSplitSpecificResource res, List<ImageSplitGameType> gameTypes) {
        this.cols = cols;
        this.rows = rows;
        this.showPopupAdIncrementValue = showPopupAdIncrementValue;
        this.res = res;
        this.gameTypes = gameTypes;
    }

    public int getShowPopupAdIncrementValue() {
        return showPopupAdIncrementValue;
    }

    public int getCols() {
        return cols;
    }

    public int getRows() {
        return rows;
    }

    public List<ImageSplitGameType> getGameTypes() {
        return gameTypes;
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
