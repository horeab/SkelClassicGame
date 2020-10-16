package libgdx.implementations.kidlearn.spec.cater;

import com.badlogic.gdx.scenes.scene2d.ui.Stack;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

import libgdx.implementations.kidlearn.spec.KidLearnDragDropCreator;
import libgdx.implementations.kidlearn.spec.KidLearnGameContext;
import libgdx.implementations.kidlearn.spec.KidLearnImgInfo;
import libgdx.resources.MainResource;
import libgdx.resources.Res;

public class KidLearnMathCaterGameCreator extends KidLearnDragDropCreator {

    public static final int TOTAL_QUESTIONS = 5;
    int nrOfCorrectUnknownNumbers;
    List<Float> allCorrectNumbers;
    List<Float> wrongNumbers;
    boolean asc;
    KidLearnMathCaterConfig config;

    public KidLearnMathCaterGameCreator(KidLearnGameContext gameContext, KidLearnMathCaterConfig config) {
        super(gameContext);
        this.allCorrectNumbers = config.allCorrectNumbers;
        this.wrongNumbers = config.wrongNumbers;
        this.nrOfCorrectUnknownNumbers = config.nrOfCorrectUnknownNumbers;
        this.asc = config.asc;
        this.config = config;
    }

    @Override
    protected double getNumberOfCorrectUnknownItems() {
        return nrOfCorrectUnknownNumbers;
    }

    @Override
    protected int getTotalQuestions() {
        return TOTAL_QUESTIONS;
    }

    @Override
    protected boolean isResponseCorrect() {
        int startPos = getStartUnknownNrPos();
        boolean isCorrect = true;
        List<KidLearnImgInfo> alreadyMovedOptionImg = getAlreadyMovedOptionImg();
        for (int i = 0; i < alreadyMovedOptionImg.size(); i++) {
            if (!allCorrectNumbers.get(startPos + i).equals(Float.valueOf(alreadyMovedOptionImg.get(i).val))) {
                isCorrect = false;
                break;
            }
        }
        return isCorrect;
    }

    @Override
    protected void createAllItemsRow() {
        for (int i = 0; i < allCorrectNumbers.size(); i++) {
            Pair<Float, Float> coord = getCoordsForNumberRow(i);
            Res res = MainResource.heart_full;
            Float nr = allCorrectNumbers.get(i);
            if (i >= getStartUnknownNrPos() && unknownImg.size() < nrOfCorrectUnknownNumbers) {
                res = MainResource.crown;
                nr = null;
            }
            Stack imgStack = addImg(coord, res, getNr(nr));
            if (nr == null) {
                unknownImg.add(new KidLearnImgInfo(coord, imgStack, String.valueOf(allCorrectNumbers.get(i))));
            }
        }
        addHead();
        addTail();
    }

    @Override
    protected int getTotalItems() {
        return allCorrectNumbers.size();
    }

    @Override
    protected int getTotalOptions() {
        return nrOfCorrectUnknownNumbers + wrongNumbers.size();
    }

    @Override
    protected List<String> getAllOptions() {
        List<Float> allOptionNr = new ArrayList<>(wrongNumbers);
        for (int i = 0; i < allCorrectNumbers.size(); i++) {
            if (allOptionNr.size() < getTotalOptions() && i >= getStartUnknownNrPos()) {
                allOptionNr.add(allCorrectNumbers.get(i));
            }
        }
        List<String> res = new ArrayList<>();
        for (Float o : allOptionNr) {
            res.add(getNr(o));
        }
        return res;
    }

    private int getStartUnknownNrPos() {
        int pos = 1;
        if (nrOfCorrectUnknownNumbers == 3) {
            pos = 2;
        }
        return pos;
    }

    private void addHead() {
        addImg(getCoordsForNumberRow(-1), MainResource.error, "");
    }

    private void addTail() {
        addImg(getCoordsForNumberRow(allCorrectNumbers.size()), MainResource.error, "");
    }

    private String getNr(Float val) {
        return val == null ? "" : val % 1 == 0 ? String.valueOf(Math.round(val)) : String.format("%.1f", val) + "";
    }

}
