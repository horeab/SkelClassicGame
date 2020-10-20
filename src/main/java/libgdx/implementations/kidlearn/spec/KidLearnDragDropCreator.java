package libgdx.implementations.kidlearn.spec;

import com.badlogic.gdx.scenes.scene2d.Action;
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
import java.util.List;

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
import libgdx.implementations.kidlearn.screens.KidLearnEngWordsGameScreen;
import libgdx.implementations.kidlearn.screens.KidLearnMathCaterGameScreen;
import libgdx.implementations.kidlearn.screens.KidLearnSciGameScreen;
import libgdx.implementations.kidlearn.spec.cater.KidLearnMathCaterLevel;
import libgdx.implementations.kidlearn.spec.sci.KidLearnSciLevel;
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
    public static final float SCALE = 0.3f;
    public static final float SCALE_DURATION = 0.3f;
    public static final String SCALED_MARKER = "scaled";
    public AbstractScreen screen;
    private MyWrappedLabel scoreLabel;
    private KidLearnGameContext gameContext;
    private boolean allowMultipleItemsPerResponse;
    private MyButton verifyBtn;
    protected List<KidLearnImgInfo> unknownImg = new ArrayList<>();
    protected List<KidLearnImgInfo> optionsImg = new ArrayList<>();
    protected List<KidLearnImgInfo> alreadyFilledUnknownImg = new ArrayList<>();
    protected List<KidLearnImgInfo> alreadyMovedOptionImg = new ArrayList<>();


    public KidLearnDragDropCreator(KidLearnGameContext gameContext,
                                   boolean allowMultipleItemsPerResponse) {
        this.screen = Game.getInstance().getAbstractScreen();
        this.gameContext = gameContext;
        this.allowMultipleItemsPerResponse = allowMultipleItemsPerResponse;
    }

    protected abstract int getTotalQuestions();

    protected abstract boolean isResponseCorrect();

    protected abstract int getTotalOptions();

    protected abstract List<String> getAllOptions();

    protected abstract int getTotalItems();

    protected abstract double getNumberOfCorrectUnknownItems();

    protected abstract void createAllItemsContainer();

    protected abstract void sortAlreadyMovedOptionImg();

    protected abstract Pair<Float, Float> getCoordsForOptionRow(int index);

    protected abstract Pair<Float, Float> getCoordsForResponseRow(int index);

    public void create() {
        createAllItemsContainer();
        createResetBtn();
        createOptionsContainer();
        createTitle();
        createScoreLabel();
    }

    private void createTitle() {
        MyWrappedLabel title = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setFontConfig(new FontConfig(FontColor.WHITE.getColor(), FontColor.GREEN.getColor(),
                        Math.round(FontConfig.FONT_SIZE), 8f)).setText(((KidLearnLevel) gameContext.level).title()).build());
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

    protected Stack addOptionImg(Pair<Float, Float> coord, Res res, String text) {
        Stack img = addImg(coord, res, getOptionWidth(), text);
        img.setWidth(getOptionWidth());
        img.setHeight(getOptionHeight());
        return img;
    }

    protected Stack addResponseImg(Pair<Float, Float> coord, Res res, String text) {
        Stack img = addImg(coord, res, getResponseWidth(), text);
        img.setWidth(getResponseWidth());
        img.setHeight(getResponseHeight());
        return img;
    }

    private Stack addImg(Pair<Float, Float> coord, Res res, float labelWidth, String text) {
        Stack img = createImgTextStack(text, labelWidth, res);
        img.setX(coord.getLeft());
        img.setY(coord.getRight());
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

    protected void addActorToScreen(Actor actor) {
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
            if (SCALED_MARKER.equals(learnImgInfo.img.getUserObject())) {
                learnImgInfo.img.addAction(Actions.scaleBy(SCALE, SCALE, SCALE_DURATION));
                learnImgInfo.img.setUserObject(null);
            }
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

    private void createOptionsContainer() {
        List<String> allOptions = new ArrayList<>(getAllOptions());
        Collections.shuffle(allOptions);
        for (String option : allOptions) {
            int itemsAlreadyAdded = optionsImg.size();
            Pair<Float, Float> coord = getCoordsForOptionRow(itemsAlreadyAdded);
            Stack img = addOptionImg(coord, getOptionRes(), option);
            KidLearnImgInfo opt = new KidLearnImgInfo(coord, img, String.valueOf(option));
            optionsImg.add(opt);
            img.addListener(new DragListener() {
                @Override
                public void drag(InputEvent event, float x, float y, int pointer) {
                    if (!alreadyMovedOptionImg.contains(opt)) {
                        img.toFront();
                        img.moveBy(x - img.getWidth() / 2, y - img.getHeight() / 2);
                    }
                }

                @Override
                public void dragStop(InputEvent event, float x, float y, int pointer) {
                    float responseSideDimenWidth = getResponseWidth();
                    float responseSideDimenHeight = getResponseHeight();
                    float optionWidth = getOptionWidth();
                    float optionHeight = getOptionHeight();
                    float acceptedDistWidth = getAcceptedDistanceForDropWidth();
                    float acceptedDistHeight = getAcceptedDistanceForDropHeight();
                    boolean noMatch = true;
                    for (KidLearnImgInfo unkInfo : unknownImg) {
                        Stack unk = unkInfo.img;
                        if ((unk.getX() - acceptedDistWidth < img.getX() && unk.getX() + responseSideDimenWidth + acceptedDistWidth > (img.getX() + optionWidth))
                                &&
                                (unk.getY() - acceptedDistHeight < img.getY() && unk.getY() + responseSideDimenHeight + acceptedDistHeight > (img.getY() + optionHeight))
                        ) {
                            if (!allowMultipleItemsPerResponse && alreadyFilledUnknownImg.contains(unkInfo)) {
                                break;
                            }
                            float moveToY = allowMultipleItemsPerResponse ? unk.getY() + unk.getHeight() / 3
                                    : unk.getY();
                            opt.img.addAction(Actions.moveTo(unk.getX(), moveToY, 0.3f));
                            if (allowMultipleItemsPerResponse && !alreadyMovedOptionImg.isEmpty()) {
                                Stack lastAddedImg = getLastAddedImgInContainer(unk.getX());
                                if (lastAddedImg != null) {
                                    opt.img.addAction(Actions.moveTo(lastAddedImg.getX(),
                                            lastAddedImg.getY() - lastAddedImg.getHeight() / 2, 0.3f));
                                }
                            }
                            noMatch = false;
                            unk.addAction(Actions.sequence(getActionsToExecuteForResponseAfterCorrect()));
                            opt.img.setTouchable(Touchable.disabled);
                            alreadyMovedOptionImg.add(opt);
                            alreadyFilledUnknownImg.add(unkInfo);

                            if (allowMultipleItemsPerResponse && !alreadyMovedOptionImg.isEmpty()) {
                                for (KidLearnImgInfo info : alreadyMovedOptionImg) {
                                    Stack stack = info.img;
                                    if (!SCALED_MARKER.equals(stack.getUserObject())) {
                                        stack.setTransform(true);
                                        stack.addAction(Actions.scaleBy(-SCALE, -SCALE, SCALE_DURATION));
                                        stack.setUserObject(SCALED_MARKER);
                                    }
                                }
                            }
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

    protected Res getOptionRes() {
        return MainResource.heart_full;
    }

    protected Action[] getActionsToExecuteForResponseAfterCorrect() {
        return new AlphaAction[]{Actions.fadeOut(UNK_FADE_DURATION)};
    }

    private Stack getLastAddedImgInContainer(float containerX) {
        Stack res = null;
        for (KidLearnImgInfo info : alreadyMovedOptionImg) {
            if (info.img.getX() == containerX) {
                res = info.img;
            }
        }
        return res;
    }

    void createVerifyBtn() {
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
            verifyBtn.setY(getVerifyBtnY());
            addActorToScreen(verifyBtn);
        } else {
            verifyBtn.setVisible(true);
        }
        verifyBtn.toFront();
    }

    private Stack createImgTextStack(String text, float labelWidth, Res res) {
        Stack stack = new Stack();
        stack.add(GraphicUtils.getImage(res));
        MyWrappedLabel textLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder().setWidth(labelWidth)
                .setFontConfig(new FontConfig(FontColor.WHITE.getColor(), FontColor.GREEN.getColor(),
                        Math.round(FontConfig.FONT_SIZE), 8f)).setText(text).build());
        stack.add(textLabel);
        return stack;
    }

    private float getHeaderY() {
        return ScreenDimensionsManager.getScreenHeightValue(93);
    }

    protected float getVerifyBtnY() {
        return ScreenDimensionsManager.getScreenHeightValue(25);
    }

    protected float getOptionWidth() {
        return ScreenDimensionsManager.getScreenWidthValue(12);
    }

    protected float getOptionHeight() {
        return ScreenDimensionsManager.getScreenWidthValue(12);
    }

    protected float getResponseWidth() {
        return getOptionWidth();
    }

    protected float getResponseHeight() {
        return getOptionHeight();
    }

    private String getScoreLabelText() {
        return gameContext.score + "/" + getTotalQuestions();
    }


    protected float getAcceptedDistanceForDropWidth() {
        return getResponseWidth() / 4;
    }

    protected float getAcceptedDistanceForDropHeight() {
        return getResponseHeight() / 4;
    }


    public List<KidLearnImgInfo> getAlreadyMovedOptionImg() {
        sortAlreadyMovedOptionImg();
        return alreadyMovedOptionImg;
    }

}
