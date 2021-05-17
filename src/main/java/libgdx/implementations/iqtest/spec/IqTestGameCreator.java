package libgdx.implementations.iqtest.spec;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import libgdx.controls.animations.ActorAnimation;
import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.MainButtonSkin;
import libgdx.controls.button.MyButton;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.controls.popup.ProVersionPopup;
import libgdx.dbapi.GameStatsDbApiService;
import libgdx.game.Game;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.SkelClassicButtonSize;
import libgdx.implementations.iqtest.IqTestDimen;
import libgdx.implementations.iqtest.IqTestGame;
import libgdx.implementations.iqtest.IqTestGameLabel;
import libgdx.implementations.iqtest.IqTestSpecificResource;
import libgdx.implementations.iqtest.screens.IqTestGameOverScreen;
import libgdx.resources.FontManager;
import libgdx.resources.MainResource;
import libgdx.resources.Res;
import libgdx.resources.dimen.MainDimen;
import libgdx.utils.DateUtils;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class IqTestGameCreator {

    private final static String MAIN_TABLE_NAME = "MAIN_TABLE_NAME";

    private IqTestCurrentGame iqTestCurrentGame;

    public IqTestGameCreator(IqTestCurrentGame iqTestCurrentGame) {
        this.iqTestCurrentGame = iqTestCurrentGame;
        addQuestionScreen(iqTestCurrentGame.getCurrentQuestion());
    }

    private void addQuestionScreen(int questionNr) {
        Table table = new Table();
        table.setName(MAIN_TABLE_NAME);
        int screenWidth = ScreenDimensionsManager.getScreenWidth();
        float verticalGeneralMarginDimen = MainDimen.vertical_general_margin.getDimen();
        table.add(createHeader()).pad(verticalGeneralMarginDimen).width(screenWidth).row();
        table.add().growY().row();
        table.add(createQuestionImage(Game.getInstance().getMainDependencyManager().createResourceService().getByName("q" + questionNr))).fillX().row();
        table.add().growY().row();
        table.add(createAnswersImages(questionNr)).padBottom(verticalGeneralMarginDimen * 2).width(screenWidth);
        table.setFillParent(true);
        Game.getInstance().getAbstractScreen().addActor(table);
    }

    private Table createHeader() {
        Table table = new Table();
        float btnFontScale = FontManager.calculateMultiplierStandardFontSize(0.8f);
        MyButton newGame = new ButtonBuilder(IqTestGameLabel.new_game.getText(),
                btnFontScale).setButtonSkin(MainButtonSkin.DEFAULT).setFixedButtonSize(SkelClassicButtonSize.IQTEST_HEADER_BUTTON).build();
        newGame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                startNewGame();
            }

        });
        final MyButton skip = new ButtonBuilder(IqTestGameLabel.skip.getText(),
                btnFontScale).setButtonSkin(MainButtonSkin.DEFAULT).setFixedButtonSize(SkelClassicButtonSize.IQTEST_HEADER_BUTTON).build();
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
        MyWrappedLabel currentQLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder().setText((iqTestCurrentGame.getCurrentQuestionToDisplay() + "/" + IqTestQuestion.values().length)).setFontScale(FontManager.getBigFontDim()).setSingleLineLabel().build());
        if (!Utils.isValidExtraContent()) {
            final Image mug = GraphicUtils.getImage(MainResource.mug);
            float mugDimen = dimen * 7;
            mug.setWidth(mugDimen);
            mug.setHeight(mugDimen);
            new ActorAnimation(mug, Game.getInstance().getAbstractScreen()).animateZoomInZoomOut();
            firstRow.add(mug).pad(dimen).growX().width(mugDimen).height(mugDimen);
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
        } else {
            firstRow.add(currentQLabel).width(ScreenDimensionsManager.getScreenWidthValue(25));
        }

        firstRow.add(newGame).pad(dimen).width(newGame.getWidth()).height(newGame.getHeight());
        firstRow.add().growX();
        firstRow.add(skip).pad(dimen).width(skip.getWidth() + skip.getWidth() / 2).height(skip.getHeight());
        table.add(firstRow).growX();
        table.row();
        table.add(secondRow);
        return table;
    }

    private Table createAnswersImages(int questionNr) {
        Table table = new Table();
        float verticalGeneralMarginDimen = MainDimen.vertical_general_margin.getDimen();
        table.add(new MyWrappedLabel(
                new MyWrappedLabelConfigBuilder().setText((IqTestGameLabel.answerchoice.getText())).setFontScale(FontManager.getSmallFontDim()).setSingleLineLabel().build())).center()
                .pad(verticalGeneralMarginDimen)
                .growX()
                .colspan(4)
                .row();
        table.setBackground(GraphicUtils.getNinePatch(IqTestSpecificResource.popup_background));
        int answerNr = 0;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 4; j++) {
                Image questionImage = GraphicUtils.getImage(Game.getInstance().getMainDependencyManager().createResourceService().getByName("q" + questionNr + "a" + answerNr));
                float sideRatio = questionImage.getHeight() / ((float) questionImage.getWidth());
                questionImage.setWidth(IqTestDimen.side_answer_img.getDimen());
                questionImage.setHeight(IqTestDimen.side_answer_img.getDimen());
                final int btnAnswerNr = answerNr;
                questionImage.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        answerClick(btnAnswerNr);
                    }

                });
                table.add(questionImage).pad(verticalGeneralMarginDimen / 3).height(questionImage.getHeight() * sideRatio).width(questionImage.getWidth());
                answerNr++;
            }
            table.row();
        }
        return table;
    }

    private Table createQuestionImage(Res questionRes) {
        Image questionImage = GraphicUtils.getImage(questionRes);
        Table table = new Table();
        float sideRatio = questionImage.getHeight() / ((float) questionImage.getWidth());
        int screenWidth = ScreenDimensionsManager.getScreenWidth();
        table.add(questionImage).width(screenWidth).height(screenWidth * sideRatio);
        return table;
    }


    public void refreshLevel() {
        if (Game.getInstance().getCurrentUser() != null) {
            new GameStatsDbApiService().incrementGameStatsQuestionsWon(Game.getInstance().getCurrentUser().getId(), Long.valueOf(DateUtils.getNowMillis()).toString());
        }

        Group root = Game.getInstance().getAbstractScreen().getStage().getRoot();
        root.findActor(MAIN_TABLE_NAME).remove();
        addQuestionScreen(iqTestCurrentGame.getCurrentQuestion());
//        saveCurrentState();
    }

    private void answerClick(int answerNr) {
        iqTestCurrentGame.getQuestionWithAnswer().put(iqTestCurrentGame.getCurrentQuestion(), answerNr);
        goToNextLevel();
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

    private void goToNextLevel() {
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
}
