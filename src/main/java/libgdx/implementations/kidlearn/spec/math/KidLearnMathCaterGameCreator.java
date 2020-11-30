package libgdx.implementations.kidlearn.spec.math;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

import libgdx.implementations.kidlearn.KidLearnSpecificResource;
import libgdx.implementations.kidlearn.spec.KidLearnGameContext;
import libgdx.implementations.kidlearn.spec.KidLearnHorizontalDragDropCreator;
import libgdx.implementations.kidlearn.spec.KidLearnImgInfo;
import libgdx.implementations.kidlearn.spec.KidLearnLevel;
import libgdx.resources.Res;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;

public class KidLearnMathCaterGameCreator extends KidLearnHorizontalDragDropCreator {

    public static final int TOTAL_QUESTIONS = 2;
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
            if (!Float.valueOf(formatFloat(allCorrectNumbers.get(startPos + i))).equals(Float.valueOf(alreadyMovedOptionImg.get(i).val))) {
                isCorrect = false;
                break;
            }
        }
        return isCorrect;
    }

    @Override
    protected void processTextTableBeforeAddText(Table table) {
    }

    @Override
    protected float getOptionTextStandardFontSize(String text) {
        float size = 0.5f;
        if (text.length() > 5) {
            size = 1f;
        } else if (text.length() > 4) {
            size = 0.9f;
        } else if (text.length() > 3) {
            size = 0.75f;
        } else if (text.length() > 2) {
            size = 0.65f;
        }
        return size;
    }

    @Override
    protected float getOptionsAvailableScreenWidth() {
        return ScreenDimensionsManager.getScreenWidth() / 1.1f;
    }

    protected FontConfig getImgStackTextFontConfig(float fontSize) {
        return new FontConfig(FontColor.WHITE.getColor(), FontColor.BLUE.getColor(),
                Math.round(fontSize), 3f);
    }

    @Override
    protected void createAllItemsContainer() {
        for (int i = 0; i < allCorrectNumbers.size(); i++) {
            Pair<Float, Float> coord = getCoordsForResponseRow(i);
            Res res = KidLearnSpecificResource.cater_body;
            Float nr = allCorrectNumbers.get(i);
            if (i >= getStartUnknownNrPos() && unknownImg.size() < nrOfCorrectUnknownNumbers) {
                res = KidLearnSpecificResource.cater_unk;
                nr = null;
            }
            Table imgStack = addResponseImg(coord, res, getNrFromFloat(nr));
            if (nr == null) {
                unknownImg.add(new KidLearnImgInfo(coord, imgStack, String.valueOf(allCorrectNumbers.get(i))));
            }
        }
        addHead();
        addTail();
    }

    @Override
    protected String getLevelTitle() {
        if ((KidLearnMathCaterSeqLevel.class.isAssignableFrom(config.gameType))) {
            KidLearnMathCaterSeqLevel level = (KidLearnMathCaterSeqLevel) gameContext.level;
            Float interval = level.interval;
            if (interval == null) {
                interval = Math.abs(allCorrectNumbers.get(0) - allCorrectNumbers.get(1));
                interval = Float.valueOf(formatFloat(interval));
            }
            return level.title.getText(interval);
        } else {
            return ((KidLearnLevel) gameContext.level).title();
        }
    }

    @Override
    protected float getOptionWidth() {
        return ScreenDimensionsManager.getScreenWidthValue(16);
    }

    @Override
    protected float getOptionHeight() {
        return getOptionWidth();
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
    protected List<Pair<String, Res>> getAllOptions() {
        List<Float> allOptionNr = new ArrayList<>(wrongNumbers);
        for (int i = 0; i < allCorrectNumbers.size(); i++) {
            if (allOptionNr.size() < getTotalOptions() && i >= getStartUnknownNrPos()) {
                allOptionNr.add(allCorrectNumbers.get(i));
            }
        }
        List<Pair<String, Res>> res = new ArrayList<>();
        for (Float o : allOptionNr) {
            res.add(Pair.of(getNrFromFloat(o), (Res) KidLearnSpecificResource.cater_body));
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
        addResponseImg(getCoordsForResponseRow(-1), KidLearnSpecificResource.cater_head, "");
    }

    private void addTail() {
        addResponseImg(getCoordsForResponseRow(allCorrectNumbers.size()), KidLearnSpecificResource.cater_tail, "");
    }

    public static String getNrFromFloat(Float val) {
        return val == null ? "" : val % 1 == 0 ? String.valueOf(Math.round(val)) : formatFloat(val) + "";
    }

    private static String formatFloat(Float val) {
        return String.format("%.1f", val);
    }

}
