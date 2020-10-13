package libgdx.implementations.kidlearn.spec.cater;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.MainButtonSize;
import libgdx.controls.button.MyButton;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.game.Game;
import libgdx.graphics.GraphicUtils;
import libgdx.resources.MainResource;
import libgdx.resources.Res;
import libgdx.resources.dimen.MainDimen;
import libgdx.screen.AbstractScreen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;

public class KidLearnMathCaterCreator {

    static final float OPT_MOVE_DURATION = 0.2f;
    static final float UNK_FADE_DURATION = 0.1f;
    int nrOfCorrectUnknownNumbers;
    List<Float> allCorrectNumbers;
    List<Float> wrongNumbers;
    List<KidLearnImgInfo> correctUnknownNumberImg = new ArrayList<>();
    List<KidLearnImgInfo> optionsImg = new ArrayList<>();
    List<KidLearnImgInfo> alreadyFilledUnknownNumberImg = new ArrayList<>();
    List<KidLearnImgInfo> alreadyMovedOptionImg = new ArrayList<>();
    AbstractScreen screen;
    boolean asc;

    public KidLearnMathCaterCreator(KidLearnMathCaterConfig config) {
        this.allCorrectNumbers = config.allCorrectNumbers;
        this.wrongNumbers = config.wrongNumbers;
        this.nrOfCorrectUnknownNumbers = config.nrOfCorrectUnknownNumbers;
        this.asc = config.asc;
        this.screen = Game.getInstance().getAbstractScreen();
    }

    public void create() {
        createAllNrRow();
        createUnknownNr();
        createResetBtn();
        createVerifyBtn();
    }

    private void createResetBtn() {
        MainButtonSize btnSize = MainButtonSize.ONE_ROW_BUTTON_SIZE;
        MyButton btn = new ButtonBuilder("Reset")
                .setDefaultButton()
                .setFixedButtonSize(btnSize)
                .build();
        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                executeReset();
            }
        });
        btn.setX(ScreenDimensionsManager.getScreenWidth() - btnSize.getWidth() - MainDimen.horizontal_general_margin.getDimen());
        btn.setY(MainDimen.vertical_general_margin.getDimen());
        screen.addActor(btn);
    }

    private void executeReset() {
        alreadyFilledUnknownNumberImg.clear();
        alreadyMovedOptionImg.clear();
        for (KidLearnImgInfo unkInfo : correctUnknownNumberImg) {
            unkInfo.img.addAction(Actions.fadeIn(UNK_FADE_DURATION));
        }
        for (KidLearnImgInfo learnImgInfo : optionsImg) {
            Pair<Float, Float> initialCoord = learnImgInfo.initialCoord;
            learnImgInfo.img.addAction(Actions.moveTo(initialCoord.getLeft(), initialCoord.getRight(), OPT_MOVE_DURATION));
        }
    }


    private void createVerifyBtn() {
        MainButtonSize btnSize = MainButtonSize.ONE_ROW_BUTTON_SIZE;
        MyButton btn = new ButtonBuilder("Verify")
                .setDefaultButton()
                .setFixedButtonSize(btnSize)
                .build();
        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                int startPos = getStartUnknownNrPos();
                boolean isCorrect = true;
                for (int i = 0; i < alreadyMovedOptionImg.size(); i++) {
                    if (allCorrectNumbers.get(startPos + i) != alreadyMovedOptionImg.get(i).nr) {
                        isCorrect = false;
                        break;
                    }
                }
                if (!isCorrect) {
                    executeReset();
                }
            }
        });
        btn.setX(MainDimen.horizontal_general_margin.getDimen());
        btn.setY(MainDimen.vertical_general_margin.getDimen());
        screen.addActor(btn);
    }

    private void createAllNrRow() {
        for (int i = 0; i < allCorrectNumbers.size(); i++) {
            Pair<Float, Float> coord = getCoordsForNumberRow(i);
            Res res = MainResource.heart_full;
            Float nr = allCorrectNumbers.get(i);
            if (i >= getStartUnknownNrPos() && correctUnknownNumberImg.size() < nrOfCorrectUnknownNumbers) {
                res = MainResource.crown;
                nr = null;
            }
            Stack imgStack = addImgNr(coord, res, nr);
            if (nr == null) {
                correctUnknownNumberImg.add(new KidLearnImgInfo(coord, imgStack, allCorrectNumbers.get(i)));
            }
        }
        addHead();
        addTail();
    }

    private int getTotalOptions() {
        return nrOfCorrectUnknownNumbers + wrongNumbers.size();
    }

    private int getStartUnknownNrPos() {
        int pos = 1;
        if (nrOfCorrectUnknownNumbers == 3) {
            pos = 2;
        }
        return pos;
    }

    private void addHead() {
        addImgNr(getCoordsForNumberRow(-1), MainResource.error, null);
    }

    private void addTail() {
        addImgNr(getCoordsForNumberRow(allCorrectNumbers.size()), MainResource.error, null);
    }

    private void createUnknownNr() {
        List<Float> allOptionNr = new ArrayList<>(wrongNumbers);
        for (int i = 0; i < allCorrectNumbers.size(); i++) {
            if (allOptionNr.size() < getTotalOptions() && i >= getStartUnknownNrPos()) {
                allOptionNr.add(allCorrectNumbers.get(i));
            }
        }
        Collections.shuffle(allOptionNr);
        for (Float nr : allOptionNr) {
            Pair<Float, Float> coord = getCoordsForUnknownNumbers(optionsImg.size());
            Stack img = addImgNr(coord, MainResource.heart_full, nr);
            KidLearnImgInfo opt = new KidLearnImgInfo(coord, img, nr);
            optionsImg.add(opt);
            img.addListener(new DragListener() {
                @Override
                public void drag(InputEvent event, float x, float y, int pointer) {
                    if (!alreadyMovedOptionImg.contains(opt)) {
                        img.moveBy(x - img.getWidth() / 2, y - img.getHeight() / 2);
                    }
                }

                @Override
                public void dragStop(InputEvent event, float x, float y, int pointer) {
                    float numberImgSideDimen = getNumberImgSideDimen();
                    float acceptedDist = numberImgSideDimen / 4;
                    boolean noMatch = true;
                    for (KidLearnImgInfo unkInfo : correctUnknownNumberImg) {
                        Stack unk = unkInfo.img;
                        if ((unk.getX() - acceptedDist < img.getX() && unk.getX() + numberImgSideDimen + acceptedDist > (img.getX() + numberImgSideDimen))
                                &&
                                (unk.getY() - acceptedDist < img.getY() && unk.getY() + numberImgSideDimen + acceptedDist > (img.getY() + numberImgSideDimen))
                                &&
                                !alreadyFilledUnknownNumberImg.contains(unk)) {
                            noMatch = false;
                            unk.addAction(Actions.fadeOut(UNK_FADE_DURATION));
                            alreadyMovedOptionImg.add(opt);
                            alreadyFilledUnknownNumberImg.add(unkInfo);
                        }
                    }
                    if (noMatch) {
                        img.addAction(Actions.moveTo(coord.getLeft(), coord.getRight(), OPT_MOVE_DURATION));
                    }
                }
            });
        }
    }

    private Stack addImgNr(Pair<Float, Float> coord, Res res, Float nr) {
        Stack img = createNumberStack(nr, res);
        img.setX(coord.getLeft());
        img.setY(coord.getRight());
        img.setWidth(getNumberImgSideDimen());
        img.setHeight(getNumberImgSideDimen());
        screen.addActor(img);
        return img;
    }

    private Pair<Float, Float> getCoordsForNumberRow(int index) {
        float variableY = ScreenDimensionsManager.getScreenHeightValue(new Random().nextInt(5));
        variableY = new Random().nextBoolean() ? variableY : -variableY;
        float y = getNumberRowY() + variableY;
        return createNrImgCoord(index, allCorrectNumbers.size(), y);
    }

    private Pair<Float, Float> getCoordsForUnknownNumbers(int index) {
        return createNrImgCoord(index, getTotalOptions(), getUnknownNumberY());
    }

    private Pair<Float, Float> createNrImgCoord(int index, int totalNr, float y) {
        int screenWidth = ScreenDimensionsManager.getScreenWidth();
        float availableScreenWidth = screenWidth / 1.5f;
        float partWidth = availableScreenWidth / totalNr;
        float x = (screenWidth - availableScreenWidth) / 2
                + partWidth / 2
                - getNumberImgSideDimen() / 2
                + partWidth * index;
        return Pair.of(x, y);
    }

    private float getUnknownNumberY() {
        return ScreenDimensionsManager.getExternalDeviceHeightValue(20);
    }

    private float getNumberRowY() {
        return ScreenDimensionsManager.getExternalDeviceHeightValue(60);
    }

    private Stack createNumberStack(Float number, Res res) {
        Stack stack = new Stack();
        stack.add(GraphicUtils.getImage(res));
        String text = number == null ? "" : getNr(number) + "";
        MyWrappedLabel nrLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder().setWidth(getNumberImgSideDimen())
                .setFontConfig(new FontConfig(FontColor.WHITE.getColor(), FontColor.GREEN.getColor(),
                        Math.round(FontConfig.FONT_SIZE), 8f)).setText(text).build());
        stack.add(nrLabel);
        return stack;
    }


    private String getNr(float val) {
        return val % 1 == 0 ? String.valueOf(Math.round(val)) : String.format("%.1f", val);
    }

    private float getNumberImgSideDimen() {
        return ScreenDimensionsManager.getScreenWidthValue(12);
    }

}
