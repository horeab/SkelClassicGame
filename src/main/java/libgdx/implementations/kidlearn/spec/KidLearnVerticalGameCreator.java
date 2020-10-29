package libgdx.implementations.kidlearn.spec;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

import libgdx.graphics.GraphicUtils;
import libgdx.implementations.SkelClassicButtonSize;
import libgdx.implementations.kidlearn.KidLearnSpecificResource;
import libgdx.implementations.kidlearn.spec.sci.KidLearnArrowConfig;
import libgdx.implementations.kidlearn.spec.sci.KidLearnArrowsConfig;
import libgdx.resources.MainResource;
import libgdx.resources.Res;
import libgdx.resources.dimen.MainDimen;
import libgdx.utils.ScreenDimensionsManager;

public class KidLearnVerticalGameCreator extends KidLearnDragDropCreator {

    public static final int TOTAL_QUESTIONS = 2;
    KidLearnArrowsConfig config;

    public KidLearnVerticalGameCreator(KidLearnGameContext gameContext, KidLearnArrowsConfig config) {
        super(gameContext);
        this.config = config;
    }

    @Override
    public void create() {
        addMainImg();
        super.create();
    }

    private void addMainImg() {
        Image img = GraphicUtils.getImage(config.mainImg);
        float availableScreenHeight = getAvailableScreenHeight();
        float newWidthForNewHeight = ScreenDimensionsManager.getNewWidthForNewHeight(availableScreenHeight, img);
        img.setHeight(availableScreenHeight);
        img.setWidth(newWidthForNewHeight);

        img.setX((getResponsesRowX() - newWidthForNewHeight) / 2);
        img.setY((ScreenDimensionsManager.getScreenHeight() - availableScreenHeight) / 2);
        addActorToScreen(img);
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
        Pair<Float, Float> imgCoord = createImgCoord(index, getTotalItems(), getResponsesRowX());
        return Pair.of(imgCoord.getLeft(), config.words.get(index).wordY * getAvailableScreenHeight() - getOptionHeight() / 2);
    }

    @Override
    protected Pair<Float, Float> getCoordsForOptionRow(int index) {
        int screenHeight = ScreenDimensionsManager.getScreenHeight();
        float availableScreenHeight = getAvailableScreenHeight();
        float partHeight = availableScreenHeight / getTotalOptions();
        float halfScreen = ScreenDimensionsManager.getScreenWidthValue(50);
        float halfQ = halfScreen / 4;
        float x = halfScreen + halfQ / 1.3f;
        if (index % 2 != 0) {
            x = x + halfQ * 1.3f;
        }
        float y = (screenHeight - availableScreenHeight)
                + partHeight / 1.3f * Math.floorDiv(index + 1, 2);
        y = y + screenHeight / 6f;
        return Pair.of(x, y);
    }

    private Pair<Float, Float> createImgCoord(int index, int totalNr, float x) {
        int screenHeight = ScreenDimensionsManager.getScreenHeight();
        float availableScreenHeight = getAvailableScreenHeight();
        float partHeight = availableScreenHeight / totalNr;
        float y = (screenHeight - availableScreenHeight) / 2
                + partHeight / 2
                - getOptionHeight() / 2
                + partHeight * index;
        return Pair.of(x, y);
    }

    private float getAvailableScreenHeight() {
        int screenHeight = ScreenDimensionsManager.getScreenHeight();
        return screenHeight / 1.2f;
    }

    @Override
    protected void createAllItemsContainer() {
        for (int i = 0; i < config.words.size(); i++) {
            Pair<Float, Float> coord = getCoordsForResponseRow(i);
            KidLearnArrowConfig config = this.config.words.get(i);
            String word = config.word;
            Table imgStack = addResponseImg(coord, KidLearnSpecificResource.arrow_left, "");
            unknownImg.add(new KidLearnImgInfo(coord, imgStack, word));
            addRespArrow(coord, config.arrowX);
        }
    }

    private void addRespArrow(Pair<Float, Float> coord, float arrowX) {
        float arrowWidth = getResponsesRowX() * arrowX;
        Image image = GraphicUtils.getImage(KidLearnSpecificResource.arrow_left);
        image.setWidth(arrowWidth);
        image.setHeight(SkelClassicButtonSize.KIDLEARN_RESPONSE_ARROW.getHeight());
        image.setX(coord.getLeft() - image.getWidth());
        image.setY(coord.getRight() + image.getHeight() / 2);
        addActorToScreen(image);
    }

    @Override
    protected void sortAlreadyMovedOptionImg() {
    }

    @Override
    protected float getAcceptedDistanceForDropHeight() {
        return getOptionHeight() / 3;
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
    protected List<Pair<String, Res>> getAllOptions() {
        List<Pair<String, Res>> opt = new ArrayList<>();
        for (KidLearnArrowConfig word : config.words) {
            opt.add(Pair.of(word.word, KidLearnSpecificResource.word_unk));
        }
        return opt;
    }

    @Override
    protected float getOptionWidth() {
        return ScreenDimensionsManager.getScreenWidthValue(17);
    }

    @Override
    protected float getOptionHeight() {
        return ScreenDimensionsManager.getScreenHeightValue(9);
    }
}
