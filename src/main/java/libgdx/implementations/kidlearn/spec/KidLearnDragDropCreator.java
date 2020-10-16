package libgdx.implementations.kidlearn.spec;

import com.badlogic.gdx.scenes.scene2d.Actor;
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
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import libgdx.controls.ScreenRunnable;
import libgdx.controls.animations.ActorAnimation;
import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.ButtonSize;
import libgdx.controls.button.MyButton;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.game.Game;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.SkelClassicButtonSize;
import libgdx.implementations.kidlearn.KidLearnScreenManager;
import libgdx.resources.MainResource;
import libgdx.resources.Res;
import libgdx.resources.dimen.MainDimen;
import libgdx.screen.AbstractScreen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.Utils;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;

public abstract class KidLearnDragDropCreator {

    private static final float OPT_MOVE_DURATION = 0.2f;
    private static final float UNK_FADE_DURATION = 0.1f;
    public AbstractScreen screen;
    private MyWrappedLabel scoreLabel;
    private KidLearnGameContext gameContext;
    private MyButton verifyBtn;
    protected List<KidLearnImgInfo> unknownImg = new ArrayList<>();
    private List<KidLearnImgInfo> optionsImg = new ArrayList<>();
    private List<KidLearnImgInfo> alreadyFilledUnknownImg = new ArrayList<>();
    private List<KidLearnImgInfo> alreadyMovedOptionImg = new ArrayList<>();


    public KidLearnDragDropCreator(KidLearnGameContext gameContext) {
        this.screen = Game.getInstance().getAbstractScreen();
        this.gameContext = gameContext;
    }

    protected abstract int getTotalQuestions();

    protected abstract boolean isResponseCorrect();

    protected abstract int getTotalOptions();

    protected abstract List<String> getAllOptions();

    protected abstract int getTotalItems();

    protected abstract double getNumberOfCorrectUnknownItems();

    protected abstract void createAllItemsRow();

    public void create() {
        createAllItemsRow();
        createResetBtn();
        createOptionsRow();
        createTitle();
        createScoreLabel();
    }

    private void createTitle() {
        MyWrappedLabel title = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setFontConfig(new FontConfig(FontColor.WHITE.getColor(), FontColor.GREEN.getColor(),
                        Math.round(FontConfig.FONT_SIZE), 8f)).setText("1 to 10 in 10").build());
        title.setY(getHeaderY());
        title.setX(ScreenDimensionsManager.getScreenWidth() / 2);
        addActorToScreen(title);
    }

    private void createScoreLabel() {
        scoreLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setFontConfig(new FontConfig(FontColor.WHITE.getColor(), FontColor.GREEN.getColor(),
                        Math.round(FontConfig.FONT_SIZE), 8f)).setText(getScoreLabelText()).build());
        scoreLabel.setY(getHeaderY());
        scoreLabel.setX(ScreenDimensionsManager.getScreenWidth() - MainDimen.horizontal_general_margin.getDimen() * 5);
        addActorToScreen(scoreLabel);
    }

    protected Stack addImg(Pair<Float, Float> coord, Res res, String text) {
        Stack img = createImgTextStack(text, res);
        img.setX(coord.getLeft());
        img.setY(coord.getRight());
        img.setWidth(getImgSideDimen());
        img.setHeight(getImgSideDimen());
        addActorToScreen(img);
        return img;
    }

    protected void createResetBtn() {
        ButtonSize btnSize = SkelClassicButtonSize.KIDLEARN_RESET;
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
        addActorToScreen(btn);
    }

    private void addActorToScreen(Actor actor) {
        new ActorAnimation(actor, screen).animateFastFadeIn();
        screen.addActor(actor);
    }

    private void executeReset() {
        if (verifyBtn != null) {
            verifyBtn.setVisible(false);
        }
        alreadyFilledUnknownImg.clear();
        alreadyMovedOptionImg.clear();
        for (KidLearnImgInfo unkInfo : unknownImg) {
            unkInfo.img.addAction(Actions.fadeIn(UNK_FADE_DURATION));
        }
        for (KidLearnImgInfo learnImgInfo : optionsImg) {
            Pair<Float, Float> initialCoord = learnImgInfo.initialCoord;
            learnImgInfo.img.addAction(Actions.moveTo(initialCoord.getLeft(), initialCoord.getRight(), OPT_MOVE_DURATION));
            learnImgInfo.img.setTouchable(Touchable.enabled);
        }
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

    private void createVerifyBtn() {
        for (KidLearnImgInfo learnImgInfo : optionsImg) {
            learnImgInfo.img.setTouchable(Touchable.disabled);
        }
        if (verifyBtn == null) {
            ButtonSize btnSize = SkelClassicButtonSize.KIDLEARN_VERIFY;
            verifyBtn = new ButtonBuilder("Verify")
                    .setDefaultButton()
                    .setFixedButtonSize(btnSize)
                    .build();
            verifyBtn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (!isResponseCorrect()) {
                        executeReset();
                    } else {
                        verifyBtn.setTouchable(Touchable.disabled);
                        verifyBtn.addAction(Actions.fadeOut(0.2f));
                        gameContext.score = gameContext.score + 1;
                        scoreLabel.setText(getScoreLabelText());
                        new KidLearnPreferencesManager().putLevelScore(gameContext.level, gameContext.score);
                        Table correctPopup = createCorrectAnswerPopup();
                        correctPopup.setVisible(false);
                        addActorToScreen(correctPopup);
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
                                if (gameContext.score == getTotalQuestions()) {
                                    screenManager.showLevelScreen(gameContext.level.getClass());
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
            verifyBtn.setY(getOptionsRowY());
            addActorToScreen(verifyBtn);
        } else {
            verifyBtn.setVisible(true);
        }
    }

    protected Pair<Float, Float> getCoordsForNumberRow(int index) {
        float variableY = ScreenDimensionsManager.getScreenHeightValue(new Random().nextInt(5));
        variableY = new Random().nextBoolean() ? variableY : -variableY;
        float y = getResponsesRowY() + variableY;
        return createImgCoord(index, getTotalItems(), y);
    }

    private Pair<Float, Float> getCoordsForOption(int index) {
        return createImgCoord(index, getTotalOptions(), getOptionsRowY());
    }

    private Pair<Float, Float> createImgCoord(int index, int totalNr, float y) {
        int screenWidth = ScreenDimensionsManager.getScreenWidth();
        float availableScreenWidth = getAvailableScreenWidth();
        float partWidth = availableScreenWidth / totalNr;
        float x = (screenWidth - availableScreenWidth) / 2
                + partWidth / 2
                - getImgSideDimen() / 2
                + partWidth * index;
        return Pair.of(x, y);
    }

    protected float getAvailableScreenWidth() {
        return ScreenDimensionsManager.getScreenWidth() / 1.5f;
    }

    private Stack createImgTextStack(String text, Res res) {
        Stack stack = new Stack();
        stack.add(GraphicUtils.getImage(res));
        MyWrappedLabel textLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder().setWidth(getImgSideDimen())
                .setFontConfig(new FontConfig(FontColor.WHITE.getColor(), FontColor.GREEN.getColor(),
                        Math.round(FontConfig.FONT_SIZE), 8f)).setText(text).build());
        stack.add(textLabel);
        return stack;
    }

    private float getHeaderY() {
        return getResponsesRowY() + ScreenDimensionsManager.getScreenHeightValue(10) + getImgSideDimen();
    }

    protected float getImgSideDimen() {
        return ScreenDimensionsManager.getScreenWidthValue(12);
    }

    private float getResponsesRowY() {
        return ScreenDimensionsManager.getExternalDeviceHeightValue(60);
    }

    private float getOptionsRowY() {
        return ScreenDimensionsManager.getExternalDeviceHeightValue(20);
    }

    private String getScoreLabelText() {
        return gameContext.score + "/" + getTotalQuestions();
    }

    private void createOptionsRow() {
        List<String> allOptions = new ArrayList<>(getAllOptions());
        Collections.shuffle(allOptions);
        for (String option : allOptions) {
            Pair<Float, Float> coord = getCoordsForOption(optionsImg.size());
            Stack img = addImg(coord, MainResource.heart_full, option);
            KidLearnImgInfo opt = new KidLearnImgInfo(coord, img, String.valueOf(option));
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
                    float numberImgSideDimen = getImgSideDimen();
                    float acceptedDist = getAcceptedDistanceForDrop();
                    boolean noMatch = true;
                    for (KidLearnImgInfo unkInfo : unknownImg) {
                        Stack unk = unkInfo.img;
                        if ((unk.getX() - acceptedDist < img.getX() && unk.getX() + numberImgSideDimen + acceptedDist > (img.getX() + numberImgSideDimen))
                                &&
                                (unk.getY() - acceptedDist < img.getY() && unk.getY() + numberImgSideDimen + acceptedDist > (img.getY() + numberImgSideDimen))
                                &&
                                !alreadyFilledUnknownImg.contains(unkInfo)) {
                            noMatch = false;
                            unk.addAction(Actions.fadeOut(UNK_FADE_DURATION));
                            opt.img.setTouchable(Touchable.disabled);
                            alreadyMovedOptionImg.add(opt);
                            alreadyFilledUnknownImg.add(unkInfo);
                        }
                    }
                    if (noMatch) {
                        img.addAction(Actions.moveTo(coord.getLeft(), coord.getRight(), OPT_MOVE_DURATION));
                    } else if (alreadyMovedOptionImg.size() == getNumberOfCorrectUnknownItems()) {
                        createVerifyBtn();
                    }
                }
            });
        }
    }

    protected float getAcceptedDistanceForDrop() {
        return getImgSideDimen() / 4;
    }

    public List<KidLearnImgInfo> getAlreadyMovedOptionImg() {
        alreadyMovedOptionImg.sort(new CustomComparator());
        return alreadyMovedOptionImg;
    }

    private static class CustomComparator implements Comparator<KidLearnImgInfo> {
        @Override
        public int compare(KidLearnImgInfo o1, KidLearnImgInfo o2) {
            return Float.compare(o1.img.getX(), o2.img.getX());
        }
    }
}
