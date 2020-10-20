package libgdx.implementations.kidlearn.spec;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

import libgdx.graphics.GraphicUtils;
import libgdx.implementations.SkelClassicButtonSize;
import libgdx.implementations.kidlearn.KidLearnSpecificResource;
import libgdx.implementations.kidlearn.spec.sci.KidLearnSingleLabelConfig;
import libgdx.resources.MainResource;
import libgdx.utils.ScreenDimensionsManager;

public class KidLearnVerticalGameCreator extends KidLearnDragDropCreator {

    public static final int TOTAL_QUESTIONS = 2;
    KidLearnSingleLabelConfig config;

    public KidLearnVerticalGameCreator(KidLearnGameContext gameContext, KidLearnSingleLabelConfig config) {
        super(gameContext, false);
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
            if (!config.words.get(i).equals(alreadyMovedOptionImg.get(i).val)) {
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
                - getOptionHeight() / 2
                + partHeight * index;
        return Pair.of(x, y);
    }

    @Override
    protected void createAllItemsContainer() {
        for (int i = 0; i < config.words.size(); i++) {
            Pair<Float, Float> coord = getCoordsForResponseRow(i);
            String word = this.config.words.get(i);
            Stack imgStack = addResponseImg(coord, MainResource.heart_full, word);
            unknownImg.add(new KidLearnImgInfo(coord, imgStack, word));
            addRespArrow(coord);
        }
    }

    protected void addRespArrow(Pair<Float, Float> coord) {
        Image image = GraphicUtils.getImage(KidLearnSpecificResource.arrow_left);
        image.setWidth(SkelClassicButtonSize.KIDLEARN_RESPONSE_ARROW.getWidth());
        image.setHeight(SkelClassicButtonSize.KIDLEARN_RESPONSE_ARROW.getHeight());
        image.setX(coord.getLeft()-image.getWidth());
        image.setY(coord.getRight());
        addActorToScreen(image);
    }

    @Override
    protected void sortAlreadyMovedOptionImg() {

    }

    @Override
    protected float getAcceptedDistanceForDropHeight() {
        return getOptionHeight();
    }

    private float getResponsesRowX() {
        return ScreenDimensionsManager.getScreenWidthValue(35);
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
    protected List<String> getAllOptions() {
        return new ArrayList<>(config.words);
    }

    @Override
    protected float getOptionWidth() {
        return ScreenDimensionsManager.getScreenWidthValue(20);
    }

    @Override
    protected float getOptionHeight() {
        return ScreenDimensionsManager.getScreenHeightValue(5);
    }
}
