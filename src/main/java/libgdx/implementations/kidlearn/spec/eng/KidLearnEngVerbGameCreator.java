package libgdx.implementations.kidlearn.spec.eng;

import libgdx.implementations.kidlearn.spec.KidLearnGameContext;
import libgdx.implementations.kidlearn.spec.KidLearnImgInfo;
import libgdx.implementations.kidlearn.spec.KidLearnMultipleAnswersConfig;
import libgdx.implementations.kidlearn.spec.KidLearnMultipleItemsGameCreator;
import libgdx.implementations.kidlearn.spec.KidLearnUtils;
import libgdx.utils.ScreenDimensionsManager;

public class KidLearnEngVerbGameCreator extends KidLearnMultipleItemsGameCreator {

    public static final int TOTAL_QUESTIONS = 2;
    public static final int TOTAL_ITEMS_OF_TYPE = 3;

    public KidLearnEngVerbGameCreator(KidLearnGameContext gameContext, KidLearnMultipleAnswersConfig config) {
        super(gameContext, config);
    }

    @Override
    protected int getTotalQuestions() {
        return TOTAL_QUESTIONS;
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
        return super.getResponseWidth() * 1.6f;
    }

    @Override
    protected float getResponseHeight() {
        return super.getResponseHeight() * 2.5f;
    }

    @Override
    protected void executeOnDragStart(KidLearnImgInfo opt) {
        KidLearnUtils.playSoundForEnum(opt.val);
    }
}
