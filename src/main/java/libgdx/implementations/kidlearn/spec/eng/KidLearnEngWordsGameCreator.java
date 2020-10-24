package libgdx.implementations.kidlearn.spec.eng;

import com.badlogic.gdx.scenes.scene2d.ui.Stack;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

import libgdx.implementations.kidlearn.KidLearnSpecificResource;
import libgdx.implementations.kidlearn.spec.KidLearnGameContext;
import libgdx.implementations.kidlearn.spec.KidLearnHorizontalDragDropCreator;
import libgdx.implementations.kidlearn.spec.KidLearnImgInfo;
import libgdx.implementations.kidlearn.spec.KidLearnWordImgConfig;
import libgdx.resources.Res;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.model.FontConfig;

public class KidLearnEngWordsGameCreator extends KidLearnHorizontalDragDropCreator {

    public static final int TOTAL_QUESTIONS = 2;
    KidLearnEngWordsConfig config;

    public KidLearnEngWordsGameCreator(KidLearnGameContext gameContext, KidLearnEngWordsConfig config) {
        super(gameContext);
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
        for (int i = 0; i < config.words.size(); i++) {
            Pair<Float, Float> coord = getCoordsForResponseRow(i);
            KidLearnWordImgConfig config = this.config.words.get(i);
            Res res = config.img;
            String word = config.word;
            Stack imgStack = addResponseImg(coord, res, "");
            unknownImg.add(new KidLearnImgInfo(coord, imgStack, word));
        }
    }

    @Override
    protected float getOptionWidth() {
        return super.getOptionWidth() * 1.4f;
    }

    @Override
    protected int getOptionFontSize(String text) {
        int standardFontSize = Math.round(FontConfig.FONT_SIZE / 1.0f);
        int fontSize = Math.round(StringUtils.isNotBlank(text) && text.length() > 8 ? standardFontSize / 1.18f :
                standardFontSize);
        return fontSize;
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
    protected float getAvailableScreenWidth() {
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
}
