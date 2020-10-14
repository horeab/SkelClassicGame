package libgdx.implementations.kidlearn.spec.cater;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import libgdx.controls.ScreenRunnable;
import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.ButtonSize;
import libgdx.controls.button.MyButton;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.game.Game;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.SkelClassicButtonSize;
import libgdx.implementations.kidlearn.KidLearnScreenManager;
import libgdx.implementations.kidlearn.spec.KidLearnGameContext;
import libgdx.implementations.kidlearn.spec.KidLearnPreferencesManager;
import libgdx.resources.MainResource;
import libgdx.resources.Res;
import libgdx.resources.dimen.MainDimen;
import libgdx.screen.AbstractScreen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.Utils;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;

public class KidLearnMathCaterGameCreator {

    public static final int TOTAL_QUESTIONS = 5;

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
    MyWrappedLabel scoreLabel;
    KidLearnMathCaterConfig config;
    KidLearnGameContext gameContext;
    MyButton verifyBtn;

    public KidLearnMathCaterGameCreator(KidLearnGameContext gameContext, KidLearnMathCaterConfig config) {
        this.allCorrectNumbers = config.allCorrectNumbers;
        this.wrongNumbers = config.wrongNumbers;
        this.nrOfCorrectUnknownNumbers = config.nrOfCorrectUnknownNumbers;
        this.asc = config.asc;
        this.screen = Game.getInstance().getAbstractScreen();
        this.config = config;
        this.gameContext = gameContext;
    }

    public void create() {
        createAllNrRow();
        createUnknownNr();
        createResetBtn();
        createTitle();
        createScoreLabel();
    }

    private void createTitle() {
        MyWrappedLabel title = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setFontConfig(new FontConfig(FontColor.WHITE.getColor(), FontColor.GREEN.getColor(),
                        Math.round(FontConfig.FONT_SIZE), 8f)).setText("1 to 10 in 10").build());
        title.setY(getHeaderY());
        title.setX(ScreenDimensionsManager.getScreenWidth() / 2);
        screen.addActor(title);
    }

    private void createScoreLabel() {
        scoreLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setFontConfig(new FontConfig(FontColor.WHITE.getColor(), FontColor.GREEN.getColor(),
                        Math.round(FontConfig.FONT_SIZE), 8f)).setText(getScoreLabelText()).build());
        scoreLabel.setY(getHeaderY());
        scoreLabel.setX(ScreenDimensionsManager.getScreenWidth() - MainDimen.horizontal_general_margin.getDimen() * 5);
        screen.addActor(scoreLabel);
    }

    private String getScoreLabelText() {
        return gameContext.score + "/" + TOTAL_QUESTIONS;
    }

    private Table createCorrectAnswerPopup() {
        Table table = new Table();
        float popupWidth = ScreenDimensionsManager.getScreenWidthValue(60);
        table.setWidth(popupWidth);
        table.setHeight(ScreenDimensionsManager.getScreenHeightValue(30));
        table.setX(ScreenDimensionsManager.getScreenWidth() / 2 - popupWidth / 2);
        table.setY(ScreenDimensionsManager.getScreenHeight() / 2 - table.getHeight() / 2);
        table.setBackground(GraphicUtils.getNinePatch(MainResource.popup_background));
        MyWrappedLabel msg = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setWidth(popupWidth / 1.1f)
                .setFontConfig(new FontConfig(FontColor.WHITE.getColor(), FontColor.GREEN.getColor(),
                        Math.round(FontConfig.FONT_SIZE), 8f)).setText("That's correct, well done!").build());
        table.add(msg);
        return table;
    }

    private float getHeaderY() {
        return getNumberRowY() + ScreenDimensionsManager.getScreenHeightValue(10) + getNumberImgSideDimen();
    }

    private void createResetBtn() {
        ButtonSize btnSize = SkelClassicButtonSize.KIDLEARN_MATH_CATER_RESET;
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
        if (verifyBtn != null) {
            verifyBtn.setVisible(false);
        }
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
        if (verifyBtn == null) {
            ButtonSize btnSize = SkelClassicButtonSize.KIDLEARN_MATH_CATER_VERIFY;
            verifyBtn = new ButtonBuilder("Verify")
                    .setDefaultButton()
                    .setFixedButtonSize(btnSize)
                    .build();
            verifyBtn.addListener(new ClickListener() {
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
                    } else {
                        verifyBtn.setTouchable(Touchable.disabled);
                        verifyBtn.addAction(Actions.fadeOut(0.2f));
                        gameContext.score = gameContext.score + 1;
                        scoreLabel.setText(getScoreLabelText());
                        new KidLearnPreferencesManager().putLevelScore(gameContext.level, gameContext.score);
                        Table correctPopup = createCorrectAnswerPopup();
                        correctPopup.setVisible(false);
                        screen.addActor(correctPopup);
                        float duration = 0.2f;
                        AlphaAction fadeOut = Actions.fadeOut(duration);
                        fadeOut.setAlpha(0f);
                        correctPopup.addAction(Actions.sequence(fadeOut, Utils.createRunnableAction(new ScreenRunnable(screen) {
                            @Override
                            public void executeOperations() {
                                correctPopup.setVisible(true);
                            }
                        }), Actions.fadeIn(duration), Actions.delay(1.5f), Utils.createRunnableAction(new Runnable() {
                            @Override
                            public void run() {
                                KidLearnScreenManager screenManager = (KidLearnScreenManager) screen.getScreenManager();
                                if (gameContext.score == TOTAL_QUESTIONS) {
                                    screenManager.showLevelScreen((Class<? extends KidLearnMathCaterLevel>) gameContext.level.getClass());
                                } else {
                                    screen.addAction(Actions.sequence(Actions.fadeOut(0.5f), Utils.createRunnableAction(new Runnable() {
                                        @Override
                                        public void run() {
                                            screenManager.showGameScreen(gameContext);
                                        }
                                    })));
                                }
                            }
                        })));
                    }
                }
            });
            verifyBtn.setX(ScreenDimensionsManager.getScreenWidth() / 2 - verifyBtn.getWidth() / 2);
            verifyBtn.setY(getUnknownNumberY());
            screen.addActor(verifyBtn);
        } else {
            verifyBtn.setVisible(true);
        }
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
                    } else if (alreadyMovedOptionImg.size() == nrOfCorrectUnknownNumbers) {
                        createVerifyBtn();
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
