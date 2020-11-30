package libgdx.implementations.kidlearn.spec;

import org.apache.commons.lang3.tuple.Pair;

import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import libgdx.utils.ScreenDimensionsManager;

public abstract class KidLearnHorizontalDragDropCreator extends KidLearnDragDropCreator {


    public KidLearnHorizontalDragDropCreator(KidLearnGameContext gameContext) {
        super(gameContext);
    }

    @Override
    protected Pair<Float, Float> getCoordsForResponseRow(int index) {
        float variableY = getVariableResponseY();
        variableY = new Random().nextBoolean() ? variableY : -variableY;
        float y = getResponsesRowY() + variableY;
        return createImgCoord(index, getTotalItems(), y, getResponseWidth(), getResponseAvailableScreenWidth());
    }

    protected float getVariableResponseY() {
        return ScreenDimensionsManager.getScreenHeightValue(new Random().nextInt(5));
    }

    @Override
    protected Pair<Float, Float> getCoordsForOptionRow(int index) {
        return createImgCoord(index, getTotalOptions(), getOptionsRowY(), getOptionWidth(), getOptionsAvailableScreenWidth());
    }

    private Pair<Float, Float> createImgCoord(int index, int totalNr, float y, float imgWidth, float availableScreenWidth) {
        int screenWidth = ScreenDimensionsManager.getScreenWidth();
        float partWidth = availableScreenWidth / totalNr;
        float x = (screenWidth - availableScreenWidth) / 2
                + partWidth / 2
                - imgWidth / 2
                + partWidth * index;
        return Pair.of(x, y);
    }

    protected float getOptionsAvailableScreenWidth() {
        return ScreenDimensionsManager.getScreenWidth() / 1.5f;
    }

    protected float getResponseAvailableScreenWidth() {
        return ScreenDimensionsManager.getScreenWidth() / 1.5f;
    }

    protected float getResponsesRowY() {
        return ScreenDimensionsManager.getExternalDeviceHeightValue(60);
    }

    protected float getOptionsRowY() {
        return ScreenDimensionsManager.getExternalDeviceHeightValue(20);
    }

    @Override
    protected void sortAlreadyMovedOptionImg() {
        Collections.sort(alreadyMovedOptionImg, new Comparator<KidLearnImgInfo>() {
            @Override
            public int compare(final KidLearnImgInfo o1, KidLearnImgInfo o2) {
                return Float.compare(o1.img.getX(), o2.img.getX());
            }
        });
    }
}
