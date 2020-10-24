package libgdx.implementations.kidlearn.spec;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import libgdx.resources.Res;
import libgdx.resources.dimen.MainDimen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.model.FontConfig;

public class KidLearnMultipleItemsGameCreator extends KidLearnHorizontalDragDropCreator {

    private static final int TOTAL_QUESTIONS = 2;
    private KidLearnMultipleAnswersConfig config;
    private static final float SCALE = 0.2f;
    private static final float SCALE_DURATION = 0.3f;
    private static final String SCALED_MARKER = "scaled";

    public KidLearnMultipleItemsGameCreator(KidLearnGameContext gameContext, KidLearnMultipleAnswersConfig config) {
        super(gameContext);
        this.config = config;
    }

    @Override
    protected double getNumberOfCorrectUnknownItems() {
        return getTotalOptions();
    }

    @Override
    protected int getTotalQuestions() {
        return TOTAL_QUESTIONS;
    }

    @Override
    protected boolean isResponseCorrect() {
        boolean isCorrect = true;
        List<KidLearnImgInfo> alreadyMovedOptionImg = getAlreadyMovedOptionImg();
        for (KidLearnImgInfo unk : unknownImg) {
            List<String> correctAnswersForUnk = new ArrayList<>();
            for (Map.Entry<KidLearnWordImgConfig, List<KidLearnWordImgConfig>> e : config.responseWithAnswers.entrySet()) {
                if (e.getKey().word.equals(unk.val)) {
                    for (KidLearnWordImgConfig c : e.getValue()) {
                        correctAnswersForUnk.add(c.word);
                    }
                }
            }
            for (KidLearnImgInfo opt : alreadyMovedOptionImg) {
                if (opt.img.getX() == getUnkOptionX(unk.img) && !correctAnswersForUnk.contains((opt.val))) {
                    isCorrect = false;
                    break;
                }
            }
        }
        return isCorrect;
    }

    @Override
    protected void createAllItemsContainer() {
        int i = 0;
        for (Map.Entry<KidLearnWordImgConfig, List<KidLearnWordImgConfig>> e : config.responseWithAnswers.entrySet()) {
            Pair<Float, Float> coord = getCoordsForResponseRow(i);
            Stack imgStack = addResponseImg(coord, e.getKey().img, e.getKey().word);
            unknownImg.add(new KidLearnImgInfo(coord, imgStack, e.getKey().word));
            i++;
        }
    }

    @Override
    protected float getResponsesRowY() {
        return ScreenDimensionsManager.getExternalDeviceHeightValue(50);
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
    protected float getOptionWidth() {
        return super.getOptionWidth() * 1.1f;
    }

    @Override
    protected int getOptionFontSize(String text) {
        int standardFontSize = Math.round(FontConfig.FONT_SIZE / 1.0f);
        int fontSize = Math.round(StringUtils.isNotBlank(text) && text.length() > 7 ? standardFontSize / 1.15f :
                standardFontSize);
        fontSize = Math.round(StringUtils.isNotBlank(text) && text.length() > 8 ? standardFontSize / 1.18f :
                fontSize);
        fontSize = Math.round(StringUtils.isNotBlank(text) && text.length() > 9 ? standardFontSize / 1.21f :
                fontSize);
        return fontSize;
    }

    @Override
    protected float getAcceptedDistanceForDropWidth() {
        return getOptionWidth() / 2;
    }

    @Override
    protected float getAcceptedDistanceForDropHeight() {
        return getOptionWidth() / 2;
    }

    @Override
    protected float getAvailableScreenWidth() {
        return ScreenDimensionsManager.getScreenWidthValue(95);
    }

    @Override
    protected int getTotalItems() {
        return config.responseWithAnswers.size();
    }

    @Override
    protected int getTotalOptions() {
        int totalOptions = 0;
        for (Map.Entry<KidLearnWordImgConfig, List<KidLearnWordImgConfig>> e : config.responseWithAnswers.entrySet()) {
            totalOptions = totalOptions + e.getValue().size();
        }
        return totalOptions;
    }

    @Override
    protected List<Pair<String, Res>> getAllOptions() {
        List<Pair<String, Res>> opt = new ArrayList<>();
        for (Map.Entry<KidLearnWordImgConfig, List<KidLearnWordImgConfig>> e : config.responseWithAnswers.entrySet()) {
            for (KidLearnWordImgConfig config : e.getValue()) {
                opt.add(Pair.of(config.word, config.img));
            }
        }
        return opt;
    }

    @Override
    protected float dragStopMoveToY(Stack unk) {
        return unk.getY() + unk.getHeight() / 1.6f;
    }

    @Override
    protected float dragStopMoveToX(Stack unk) {
        return getUnkOptionX(unk);
    }

    private float getUnkOptionX(Stack unk) {
        return unk.getX() + unk.getWidth() / 5.75f;
    }

    protected boolean optionDragStopValidExtraCondition(KidLearnImgInfo unkInfo) {
        return true;
    }

    @Override
    protected void executeResetAnimation(Stack img) {
        if (SCALED_MARKER.equals(img.getUserObject())) {
            img.addAction(Actions.scaleBy(SCALE, SCALE, SCALE_DURATION));
            img.setUserObject(null);
        }
    }


    @Override
    protected void executeAnimationAfterDragStop(Stack opt, Stack unk) {
        if (!SCALED_MARKER.equals(opt.getUserObject())) {
            opt.setTransform(true);
            opt.addAction(Actions.scaleBy(-SCALE, -SCALE, SCALE_DURATION));
            opt.setUserObject(SCALED_MARKER);
        }
        Stack lastAddedImg = getLastAddedImgInContainer(getUnkOptionX(unk));
        if (lastAddedImg != null) {
            opt.addAction(Actions.moveTo(lastAddedImg.getX(),
                    lastAddedImg.getY() - lastAddedImg.getHeight() / 3, 0.3f));
        }
    }

    @Override
    protected float getVerifyBtnY() {
        return MainDimen.vertical_general_margin.getDimen();
    }
}
