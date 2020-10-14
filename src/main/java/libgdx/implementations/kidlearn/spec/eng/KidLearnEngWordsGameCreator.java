package libgdx.implementations.kidlearn.spec.eng;

import com.badlogic.gdx.scenes.scene2d.ui.Stack;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

import libgdx.implementations.kidlearn.spec.KidLearnDragDropCreator;
import libgdx.implementations.kidlearn.spec.KidLearnGameContext;
import libgdx.implementations.kidlearn.spec.KidLearnImgInfo;
import libgdx.resources.MainResource;
import libgdx.resources.Res;

public class KidLearnEngWordsGameCreator extends KidLearnDragDropCreator {

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
        return 1;
    }

    @Override
    protected boolean isResponseCorrect() {
        boolean isCorrect = true;
        for (int i = 0; i < alreadyMovedOptionImg.size(); i++) {
            if (!config.words.get(i).equals(alreadyMovedOptionImg.get(i).val)) {
                isCorrect = false;
                break;
            }
        }
        return isCorrect;
    }

    @Override
    protected void createAllItemsRow() {
        for (int i = 0; i < config.words.size(); i++) {
            Pair<Float, Float> coord = getCoordsForNumberRow(i);
            Res res = MainResource.heart_full;
            String word = config.words.get(i);
            Stack imgStack = addImg(coord, res, word);
            unknownImg.add(new KidLearnImgInfo(coord, imgStack, word));
        }
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
        return config.words;
    }


}
