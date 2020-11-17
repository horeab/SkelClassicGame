package libgdx.implementations.kidlearn.spec.sci;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

import libgdx.implementations.kidlearn.spec.KidLearnGameContext;
import libgdx.implementations.kidlearn.spec.KidLearnHorizontalDragDropCreator;
import libgdx.implementations.kidlearn.spec.KidLearnImgInfo;
import libgdx.implementations.kidlearn.spec.KidLearnWordImgConfig;
import libgdx.resources.Res;
import libgdx.utils.ScreenDimensionsManager;

public class KidLearnSciFeedGameCreator extends KidLearnHorizontalDragDropCreator {

    private static final float SCALE = 0.2f;
    private static final float SCALE_DURATION = 0.3f;
    private static final String SCALE_MARKER = "Scale";
    public static final int TOTAL_QUESTIONS = 2;
    KidLearnSciFeedConfig config;

    public KidLearnSciFeedGameCreator(KidLearnGameContext gameContext, KidLearnSciFeedConfig config) {
        super(gameContext);
        this.config = config;
    }

    @Override
    protected float dragStopMoveToY(Table unk) {
        return super.dragStopMoveToY(unk) - getOptionHeight() / 2;
    }

    @Override
    protected float dragStopMoveToX(Table unk) {
        return super.dragStopMoveToX(unk) + getOptionWidth() / 10;
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
            if (!config.words.get(i).getRight().word.equals(alreadyMovedOptionImg.get(i).val)) {
                isCorrect = false;
                break;
            }
        }
        return isCorrect;
    }

    @Override
    protected void createAllItemsContainer() {
        for (int i = 0; i < config.words.size(); i++) {
            Pair<Float, Float> coord = getCoordsForResponseRow(i);
            Pair<KidLearnWordImgConfig, KidLearnWordImgConfig> config = this.config.words.get(i);
            Res res = config.getLeft().img;
            String word = config.getLeft().word;
            Table imgStack = addResponseImg(coord, res, word);
            unknownImg.add(new KidLearnImgInfo(coord, imgStack, word));
        }
    }

    @Override
    protected float getResponsesRowY() {
        return ScreenDimensionsManager.getExternalDeviceHeightValue(60);
    }

    @Override
    protected float getVariableResponseY() {
        return 0;
    }

    @Override
    protected float getAcceptedDistanceForDropWidth() {
        return getResponseWidth() / 3;
    }

    @Override
    protected float getAcceptedDistanceForDropHeight() {
        return getResponseHeight() / 3;
    }

    @Override
    protected float getOptionsAvailableScreenWidth() {
        return ScreenDimensionsManager.getScreenWidthValue(90);
    }

    @Override
    protected int getTotalItems() {
        return config.words.size();
    }

    @Override
    protected int getTotalOptions() {
        return config.words.size();
    }

    @Override
    protected void executeAnimationAfterDragStop(Table opt, Table unk) {
        unk.toFront();
        if (!SCALE_MARKER.equals(opt.getUserObject())) {
            opt.setTransform(true);
            opt.setUserObject(SCALE_MARKER);
            opt.addAction(Actions.scaleBy(-SCALE, -SCALE, SCALE_DURATION));
        }
    }

    @Override
    protected void executeOptionResetAnimation(Table img) {
        if (SCALE_MARKER.equals(img.getUserObject())) {
            img.setUserObject(null);
            img.addAction(Actions.scaleBy(SCALE, SCALE, SCALE_DURATION));
        }
    }

    @Override
    protected List<Pair<String, Res>> getAllOptions() {
        List<Pair<String, Res>> opt = new ArrayList<>();
        for (Pair<KidLearnWordImgConfig, KidLearnWordImgConfig> word : config.words) {
            opt.add(Pair.of(word.getRight().word, word.getRight().img));
        }
        return opt;
    }

    @Override
    protected float getOptionHeight() {
        return getOptionWidth();
    }

    @Override
    protected float getOptionWidth() {
        return ScreenDimensionsManager.getScreenWidthValue(11);
    }
}
