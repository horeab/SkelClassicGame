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
import libgdx.controls.button.MainButtonSkin;
import libgdx.controls.button.MyButton;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.controls.popup.ProVersionPopup;
import libgdx.game.Game;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.SkelClassicButtonSize;
import libgdx.implementations.SkelClassicButtonSkin;
import libgdx.implementations.iqtest.IqTestGame;
import libgdx.implementations.iqtest.IqTestGameLabel;
import libgdx.implementations.iqtest.screens.IqTestGameOverScreen;
import libgdx.resources.FontManager;
import libgdx.resources.MainResource;
import libgdx.resources.ResourceService;
import libgdx.resources.dimen.MainDimen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.Utils;
import libgdx.utils.model.FontConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class IqTestLevelCreator {


    ResourceService resourceService = Game.getInstance().getMainDependencyManager().createResourceService();

    final static String MAIN_TABLE_NAME = "MAIN_TABLE_NAME";

    IqTestCurrentGame iqTestCurrentGame;

    IqTestLevelCreator(IqTestCurrentGame iqTestCurrentGame) {
        this.iqTestCurrentGame = iqTestCurrentGame;
    }

    public abstract void addQuestionScreen(int currentQuestion);

    private void refreshLevel() {
        Group root = Game.getInstance().getAbstractScreen().getStage().getRoot();
        root.findActor(MAIN_TABLE_NAME).remove();
        addQuestionScreen(iqTestCurrentGame.getCurrentQuestion());
//        saveCurrentState();
    }

    Table createHeader() {
        Table table = new Table();
        MyButton newGame = new ButtonBuilder()
                .setButtonSkin(SkelClassicButtonSkin.IQTEST_NEW_GAME_BTN)
                .setFixedButtonSize(SkelClassicButtonSize.IQTEST_HEADER_IMG_BUTTON).build();
        newGame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                startNewGame();
            }

        });
        final MyButton skip = new ButtonBuilder().setButtonSkin(SkelClassicButtonSkin.IQTEST_SKIP_BTN)
                .setFixedButtonSize(SkelClassicButtonSize.IQTEST_HEADER_IMG_BUTTON).build();
        skip.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                skip.setDisabled(true);
                goToNextLevel();
            }
        });
        Table firstRow = new Table();
        Table secondRow = new Table();
        float dimen = MainDimen.horizontal_general_margin.getDimen();
        MyWrappedLabel currentQLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setText((iqTestCurrentGame.getCurrentQuestionToDisplay() + "/" + IqTestQuestion.values().length))
                .setFontConfig(new FontConfig(Color.WHITE, Color.BLACK,
                        FontConfig.FONT_SIZE * 1.5f, FontConfig.STANDARD_BORDER_WIDTH * 8.5f))
                .setSingleLineLabel().build());
        final Image mug = GraphicUtils.getImage(MainResource.mug_black_border);
        float mugDimen = dimen * 7;
        mug.setWidth(mugDimen);
        mug.setHeight(mugDimen);
        new ActorAnimation(Game.getInstance().getAbstractScreen()).animateZoomInZoomOut(mug);
        firstRow.add(mug).padLeft(MainButtonSize.BACK_BUTTON.getWidth() * 1.2f).growX().width(mugDimen).height(mugDimen);
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
        firstRow.add().growX();
        /////////////////
        secondRow.add(currentQLabel).colspan(4);

        if (Utils.isValidExtraContent()) {
            mug.setVisible(false);
        }

        firstRow.add().growX();
        firstRow.add(newGame).pad(dimen).width(newGame.getWidth()).height(newGame.getHeight());
        firstRow.add(skip).pad(dimen).width(skip.getWidth()).height(skip.getHeight());
        table.add(firstRow).growX();
        table.row();
        table.add(secondRow);
        return table;
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

    void goToNextLevel() {
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
