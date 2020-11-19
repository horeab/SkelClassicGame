package libgdx.implementations.kidlearn.spec.sci;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

import org.apache.commons.lang3.StringUtils;

import libgdx.graphics.GraphicUtils;
import libgdx.implementations.kidlearn.spec.KidLearnGameContext;
import libgdx.implementations.kidlearn.spec.KidLearnMultipleAnswersConfig;
import libgdx.implementations.kidlearn.spec.KidLearnMultipleItemsGameCreator;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.model.RGBColor;

public class KidLearnSciRecyGameCreator extends KidLearnMultipleItemsGameCreator {

    public static final int TOTAL_QUESTIONS = 2;
    public static final int TOTAL_ITEMS_OF_TYPE = 1;

    public KidLearnSciRecyGameCreator(KidLearnGameContext gameContext, KidLearnMultipleAnswersConfig config) {
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
    protected float getResponsesRowY() {
        return ScreenDimensionsManager.getExternalDeviceHeightValue(36);
    }

    @Override
    protected void processLabelTable(Table labelTable, String text) {
        if (StringUtils.isNotBlank(text)) {
            labelTable.setBackground(GraphicUtils.getColorBackground(RGBColor.LIGHT_BLUE.toColor(0.8f)));
        }
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
