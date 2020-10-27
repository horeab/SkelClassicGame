package libgdx.implementations.kidlearn.spec.sci;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

import libgdx.implementations.kidlearn.spec.KidLearnGameContext;
import libgdx.implementations.kidlearn.spec.KidLearnImgInfo;
import libgdx.implementations.kidlearn.spec.KidLearnMultipleAnswersConfig;
import libgdx.implementations.kidlearn.spec.KidLearnMultipleItemsGameCreator;
import libgdx.implementations.kidlearn.spec.KidLearnUtils;
import libgdx.utils.ScreenDimensionsManager;

public class KidLearnSciRecyGameCreator extends KidLearnMultipleItemsGameCreator {

    public static final int TOTAL_ITEMS_OF_TYPE = 1;

    public KidLearnSciRecyGameCreator(KidLearnGameContext gameContext, KidLearnMultipleAnswersConfig config) {
        super(gameContext, config);
    }

    @Override
    protected float getOptionsRowY() {
        return ScreenDimensionsManager.getExternalDeviceHeightValue(13);
    }

    @Override
    protected float getOptionWidth() {
        return super.getOptionWidth() * 1.2f;
    }

    @Override
    protected float getResponsesRowY() {
        return ScreenDimensionsManager.getExternalDeviceHeightValue(36);
    }

    @Override
    protected float getResponseWidth() {
        return super.getResponseWidth() * 1.2f;
    }

    @Override
    protected float getResponseHeight() {
        return super.getResponseHeight() * 2.2f;
    }

    @Override
    protected float getUnkOptionX(Table unk) {
        return unk.getX() + unk.getWidth() / 5f;
    }

}