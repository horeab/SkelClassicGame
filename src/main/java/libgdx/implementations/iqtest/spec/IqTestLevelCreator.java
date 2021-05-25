package libgdx.implementations.iqtest.spec;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import libgdx.controls.animations.ActorAnimation;
import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.MainButtonSize;
import libgdx.controls.button.MyButton;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.controls.popup.ProVersionPopup;
import libgdx.game.Game;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.SkelClassicButtonSize;
import libgdx.implementations.SkelClassicButtonSkin;
import libgdx.implementations.iqtest.IqTestGame;
import libgdx.implementations.iqtest.screens.IqTestGameOverScreen;
import libgdx.resources.MainResource;
import libgdx.resources.ResourceService;
import libgdx.resources.dimen.MainDimen;
import libgdx.utils.Utils;
import libgdx.utils.model.FontConfig;
import libgdx.utils.model.RGBColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class IqTestLevelCreator {


    protected ResourceService resourceService = Game.getInstance().getMainDependencyManager().createResourceService();

    protected final static String MAIN_TABLE_NAME = "MAIN_TABLE_NAME";

    protected IqTestCurrentGame iqTestCurrentGame;

    protected MyWrappedLabel scoreLabel;

    protected IqTestLevelCreator(IqTestCurrentGame iqTestCurrentGame) {
        this.iqTestCurrentGame = iqTestCurrentGame;
    }

    public abstract void addQuestionScreen(int currentQuestion);

    protected abstract String getScore();

    private void refreshLevel() {
        Group root = Game.getInstance().getAbstractScreen().getStage().getRoot();
        root.findActor(MAIN_TABLE_NAME).remove();
        addQuestionScreen(iqTestCurrentGame.getCurrentQuestion());
//        saveCurrentState();
    }

    protected Table createHeader() {
        Table table = new Table();
        MyButton newGame = createNewGameBtn();
        MyButton skip = createSkipBtn();
        float horizDimen = MainDimen.horizontal_general_margin.getDimen();

        float mugDimen = horizDimen * 7;
        final Image mug = createMugImg(mugDimen);

        Table firstRow = new Table();
        firstRow.add(mug).padLeft(MainButtonSize.BACK_BUTTON.getWidth() * 1.2f).growX().width(mugDimen).height(mugDimen);
        firstRow.add().growX();
        firstRow.add().growX();
        firstRow.add(newGame).pad(horizDimen).width(newGame.getWidth()).height(newGame.getHeight());
        firstRow.add(skip).pad(horizDimen).width(skip.getWidth()).height(skip.getHeight());

        float infoLabelWidth = horizDimen * 5;
        Table secondRow = createHeaderSecondRow();

        table.add(firstRow).growX();
        table.row();
        table.add(secondRow).growX();
        return table;
    }

    private MyButton createNewGameBtn() {
        MyButton newGame = new ButtonBuilder()
                .setButtonSkin(SkelClassicButtonSkin.IQTEST_NEW_GAME_BTN)
                .setFixedButtonSize(SkelClassicButtonSize.IQTEST_HEADER_IMG_BUTTON).build();
        newGame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                startNewGame();
            }

        });
        return newGame;
    }

    private MyButton createSkipBtn() {
        final MyButton skip = new ButtonBuilder().setButtonSkin(SkelClassicButtonSkin.IQTEST_SKIP_BTN)
                .setFixedButtonSize(SkelClassicButtonSize.IQTEST_HEADER_IMG_BUTTON).build();
        skip.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                skip.setDisabled(true);
                goToNextLevel();
            }
        });
        return skip;
    }

    private Table createHeaderSecondRow() {

        scoreLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setText(getScore())
                .setFontConfig(new FontConfig(RGBColor.LIGHT_GREEN.toColor(), RGBColor.GREEN.toColor(),
                        FontConfig.FONT_SIZE * 2.1f, FontConfig.STANDARD_BORDER_WIDTH * 8.5f))
                .setSingleLineLabel().build());
        scoreLabel.setTransform(true);

        MyWrappedLabel currentQLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setText(iqTestCurrentGame.getCurrentQuestionToDisplay() + "/" + iqTestCurrentGame.getQuestions().size())
                .setFontConfig(new FontConfig(Color.WHITE, Color.BLACK,
                        FontConfig.FONT_SIZE * 1.5f, FontConfig.STANDARD_BORDER_WIDTH * 8.5f))
                .setSingleLineLabel().build());

        Table secondRow = new Table();
        secondRow.add(scoreLabel).growX();
        secondRow.add().growX();
        secondRow.add().growX();
        secondRow.add().growX();
        secondRow.add(currentQLabel).growX();
        return secondRow;
    }

    private Image createMugImg(float mugDimen) {
        final Image mug = GraphicUtils.getImage(MainResource.mug_black_border);
        mug.setWidth(mugDimen);
        mug.setHeight(mugDimen);
        new ActorAnimation(Game.getInstance().getAbstractScreen()).animateZoomInZoomOut(mug);
        mug.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                IqTestGameOverScreen.displayInAppPurchasesPopup(new Runnable() {
                    @Override
                    public void run() {
                        refreshLevel();
                    }
                });
            }
        });
        if (!Utils.isValidExtraContent()) {
            mug.setVisible(false);
        }
        return mug;
    }

    private void goToLevel(int level) {
        if (level == 10 && !Utils.isValidExtraContent()) {
            new ProVersionPopup(Game.getInstance().getAbstractScreen()).addToPopupManager();
        } else if (level == 20 || level == 30) {
            Game.getInstance().getAppInfoService().showPopupAd(new Runnable() {
                @Override
                public void run() {
                }
            });
        }

        if (isGameOver()) {
            IqTestGame.getInstance().getScreenManager().showGameOver(iqTestCurrentGame.getQuestionWithAnswer());
        } else {
            iqTestCurrentGame.setCurrentQuestion(level);
            refreshLevel();
        }
    }

    private void startNewGame() {
        Game.getInstance().getAppInfoService().showPopupAd(new Runnable() {
            @Override
            public void run() {
                iqTestCurrentGame.reset();
                refreshLevel();
            }
        });
    }

    protected void goToNextLevel() {
        int currentQuestion = iqTestCurrentGame.getCurrentQuestion();
        int nextQuestion = -1;
        for (Map.Entry<Integer, Integer> entry : iqTestCurrentGame.getQuestionWithAnswer().entrySet()) {
            if (entry.getKey() > currentQuestion && entry.getValue() == -1) {
                nextQuestion = entry.getKey();
                break;
            }

        }
        if (nextQuestion == -1) {
            nextQuestion = getNextQuestionForSkipped();
        }
        goToLevel(nextQuestion);
    }


    private int getNextQuestionForSkipped() {
        int currentQuestion = iqTestCurrentGame.getCurrentQuestion();
        List<Integer> skipped = getSkippedQuestions();
        int nextSkipped = -1;
        for (Integer q : skipped) {
            if (q > currentQuestion) {
                nextSkipped = q;
            }
        }
        if (nextSkipped == -1 && !skipped.isEmpty()) {
            nextSkipped = skipped.get(0);
        }
        return nextSkipped;
    }

    private List<Integer> getSkippedQuestions() {
        List<Integer> skippedQuestions = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : iqTestCurrentGame.getQuestionWithAnswer().entrySet()) {
            if (entry.getValue() == -1) {
                skippedQuestions.add(entry.getKey());
            }
        }
        return skippedQuestions;
    }

    private boolean isGameOver() {
        boolean isGameOver = true;
        for (Map.Entry<Integer, Integer> entry : iqTestCurrentGame.getQuestionWithAnswer().entrySet()) {
            if (entry.getValue() == -1) {
                isGameOver = false;
                break;
            }
        }
        return isGameOver;
    }
}
