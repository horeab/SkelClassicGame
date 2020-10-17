package libgdx.implementations.kidlearn.spec.sci;

import com.badlogic.gdx.scenes.scene2d.ui.Stack;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

import libgdx.implementations.kidlearn.spec.KidLearnDragDropCreator;
import libgdx.implementations.kidlearn.spec.KidLearnGameContext;
import libgdx.implementations.kidlearn.spec.KidLearnImgInfo;
import libgdx.resources.Res;
import libgdx.utils.ScreenDimensionsManager;

public class KidLearnSciVerticalGameCreator extends KidLearnDragDropCreator {

    public static final int TOTAL_QUESTIONS = 2;
    KidLearnSciConfig config;

    public KidLearnSciVerticalGameCreator(KidLearnGameContext gameContext, KidLearnSciConfig config) {
        super(gameContext, true, false);
        this.config = config;
    }

    @Override
    protected double getNumberOfCorrectUnknownItems() {
        return config.parts.size();
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
            if (!config.parts.get(i).word.equals(alreadyMovedOptionImg.get(i).val)) {
                isCorrect = false;
                break;
            }
        }
        return isCorrect;
    }

    @Override
    protected Pair<Float, Float> getCoordsForResponseRow(int index) {
        return createImgCoord(index, getTotalItems(), getResponsesRowX());
    }

    @Override
    protected Pair<Float, Float> getCoordsForOptionRow(int index) {
        int screenHeight = ScreenDimensionsManager.getScreenHeight();
        float availableScreenHeight = screenHeight / 1.2f;
        float partHeight = availableScreenHeight / getTotalOptions();
        float halfScreen = ScreenDimensionsManager.getScreenWidthValue(50);
        float halfQ = halfScreen / 4;
        float x = halfScreen + halfQ / 1.3f;
        if (index % 2 != 0) {
            x = x + halfQ * 1.3f;
        }
        float y = (screenHeight - availableScreenHeight)
                + partHeight * 1.3f * Math.floorDiv(index + 1, 2);
        y = y + screenHeight / 10;
        return Pair.of(x, y);
    }

    private Pair<Float, Float> createImgCoord(int index, int totalNr, float x) {
        int screenHeight = ScreenDimensionsManager.getScreenHeight();
        float availableScreenHeight = screenHeight / 1.2f;
        float partHeight = availableScreenHeight / totalNr;
        float y = (screenHeight - availableScreenHeight) / 2
                + partHeight / 2
                - getImgHeight() / 2
                + partHeight * index;
        return Pair.of(x, y);
    }

    @Override
    protected void createAllItemsContainer() {
        for (int i = 0; i < config.parts.size(); i++) {
            Pair<Float, Float> coord = getCoordsForResponseRow(i);
            KidLearnSciPreDefConfig config = this.config.parts.get(i);
            Res res = config.img;
            String word = config.word;
            Stack imgStack = addImg(coord, res, word);
            unknownImg.add(new KidLearnImgInfo(coord, imgStack, word));
        }
    }

    @Override
    protected void sortAlreadyMoverOptionImg() {

    }

    @Override
    protected float getAcceptedDistanceForDropHeight() {
        return getImgHeight();
    }

    private float getResponsesRowX() {
        return ScreenDimensionsManager.getScreenWidthValue(35);
    }

    @Override
    protected int getTotalItems() {
        return config.parts.size();
    }

    @Override
    protected int getTotalOptions() {
        return config.parts.size();
    }

    @Override
    protected List<String> getAllOptions() {
        List<String> opt = new ArrayList<>();
        for (KidLearnSciPreDefConfig word : config.parts) {
            opt.add(word.word);
        }
        return opt;
    }

    @Override
    protected float getImgWidth() {
        return ScreenDimensionsManager.getScreenWidthValue(20);
    }

    @Override
    protected float getImgHeight() {
        return ScreenDimensionsManager.getScreenHeightValue(5);
    }
}
