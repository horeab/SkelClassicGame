package libgdx.implementations.kidlearn.spec.eng;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

import libgdx.graphics.GraphicUtils;
import libgdx.implementations.kidlearn.KidLearnSpecificResource;
import libgdx.implementations.kidlearn.spec.KidLearnDragDropCreator;
import libgdx.implementations.kidlearn.spec.KidLearnGameContext;
import libgdx.implementations.kidlearn.spec.KidLearnHorizontalDragDropCreator;
import libgdx.implementations.kidlearn.spec.KidLearnImgInfo;
import libgdx.implementations.kidlearn.spec.KidLearnUtils;
import libgdx.implementations.kidlearn.spec.KidLearnWordImgConfig;
import libgdx.resources.MainResource;
import libgdx.resources.Res;
import libgdx.utils.ScreenDimensionsManager;

public class KidLearnEngWordsGameCreator extends KidLearnHorizontalDragDropCreator {

    public static final int TOTAL_QUESTIONS = 2;
    KidLearnEngWordsConfig config;

    public KidLearnEngWordsGameCreator(KidLearnGameContext gameContext, KidLearnEngWordsConfig config) {
        super(gameContext);
        this.config = config;
    }

    @Override
    protected void executeOptionResetAnimation(Table img) {
        super.executeOptionResetAnimation(img);
        img.findActor(KidLearnDragDropCreator.IMG_TEXT_STACK_IMAGE).addAction(Actions.sequence(
                new AlphaAction[]{Actions.fadeIn(UNK_FADE_DURATION)}));
    }

    @Override
    protected void executeAnimationAfterDragStop(Table opt, Table unk) {
        opt.findActor(KidLearnDragDropCreator.IMG_TEXT_STACK_IMAGE).addAction(Actions.sequence(
                new AlphaAction[]{Actions.fadeOut(UNK_FADE_DURATION)}));
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
        for (int i = 0; i < config.words.size(); i++) {
            Pair<Float, Float> coord = getCoordsForResponseRow(i);
            KidLearnWordImgConfig config = this.config.words.get(i);
            Res res = config.img;
            String word = config.word;
            Table imgStack = addResponseImg(coord, res, "");
            unknownImg.add(new KidLearnImgInfo(coord, imgStack, word));
        }
    }

    @Override
    protected float getOptionWidth() {
        return super.getOptionWidth() * 1.2f;
    }

    @Override
    protected float getOptionHeight() {
        return getOptionWidth();
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
    protected List<Pair<String, Res>> getAllOptions() {
        List<Pair<String, Res>> opt = new ArrayList<>();
        for (KidLearnWordImgConfig word : config.words) {
            opt.add(Pair.of(word.word, KidLearnSpecificResource.word_unk));
        }
        return opt;
    }

    @Override
    protected void executeOnDragStart(KidLearnImgInfo opt) {
        super.executeOnDragStart(opt);
        KidLearnUtils.playSoundForEnum(opt.val);
    }
}
