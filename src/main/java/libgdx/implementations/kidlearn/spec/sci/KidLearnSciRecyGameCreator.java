package libgdx.implementations.kidlearn.spec.sci;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

import libgdx.implementations.kidlearn.spec.KidLearnGameContext;
import libgdx.implementations.kidlearn.spec.KidLearnHorizontalDragDropCreator;
import libgdx.implementations.kidlearn.spec.KidLearnImgInfo;
import libgdx.implementations.kidlearn.spec.KidLearnWordImgConfig;
import libgdx.resources.Res;
import libgdx.resources.dimen.MainDimen;
import libgdx.utils.ScreenDimensionsManager;

public class KidLearnSciRecyGameCreator extends KidLearnHorizontalDragDropCreator {

    public static final int TOTAL_QUESTIONS = 2;
    KidLearnSciRecyConfig config;

    public KidLearnSciRecyGameCreator(KidLearnGameContext gameContext, KidLearnSciRecyConfig config) {
        super(gameContext, true, true);
        this.config = config;
    }

    @Override
    protected double getNumberOfCorrectUnknownItems() {
        return config.words.size();
    }

    @Override
    protected int getTotalQuestions() {
        return TOTAL_QUESTIONS;
    }

    @Override
    protected boolean isResponseCorrect() {
        boolean isCorrect = true;
        List<KidLearnImgInfo> alreadyMovedOptionImg = getAlreadyMovedOptionImg();
        for (int i = 0; i < alreadyMovedOptionImg.size(); i++) {
            if (!config.words.get(i).word.equals(alreadyMovedOptionImg.get(i).val)) {
                isCorrect = false;
                break;
            }
        }
        return isCorrect;
    }

    @Override
    protected void createAllItemsContainer() {
        for (int i = 0; i < config.responses.size(); i++) {
            Pair<Float, Float> coord = getCoordsForResponseRow(i);
            Res res = config.responses.get(i);
            Stack imgStack = addResponseImg(coord, res, "");
            unknownImg.add(new KidLearnImgInfo(coord, imgStack, ""));
        }
    }

    @Override
    protected float getResponsesRowY() {
        return ScreenDimensionsManager.getExternalDeviceHeightValue(50);
    }

    @Override
    protected float getResponseHeight() {
        return super.getResponseHeight() * 1.6f;
    }

    @Override
    protected float getResponseWidth() {
        return super.getResponseWidth() * 1.6f;
    }

    @Override
    protected float getVariableResponseY() {
        return 0;
    }

    @Override
    protected Action[] getActionsToExecuteForResponseAfterCorrect() {
        return new Action[0];
    }

    @Override
    protected float getAcceptedDistanceForDropWidth() {
        return getResponseWidth() / 10;
    }

    @Override
    protected float getAcceptedDistanceForDropHeight() {
        return getResponseHeight() / 10;
    }

    @Override
    protected float getAvailableScreenWidth() {
        return ScreenDimensionsManager.getScreenWidthValue(90);
    }

    @Override
    protected int getTotalItems() {
        return config.responses.size();
    }

    @Override
    protected int getTotalOptions() {
        return config.words.size();
    }

    @Override
    protected List<String> getAllOptions() {
        List<String> opt = new ArrayList<>();
        for (KidLearnWordImgConfig word : config.words) {
            opt.add(word.word);
        }
        return opt;
    }

    @Override
    protected float getVerifyBtnY() {
        return MainDimen.vertical_general_margin.getDimen();
    }
}
