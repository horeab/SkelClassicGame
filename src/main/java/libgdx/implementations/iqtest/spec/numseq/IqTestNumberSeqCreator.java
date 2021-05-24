package libgdx.implementations.iqtest.spec.numseq;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import libgdx.campaign.QuestionConfigFileHandler;
import libgdx.controls.animations.ActorAnimation;
import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.MyButton;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.controls.popup.MyPopup;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.SkelClassicButtonSize;
import libgdx.implementations.SkelClassicButtonSkin;
import libgdx.implementations.imagesplit.ImageSplitScreenManager;
import libgdx.implementations.iqtest.IqTestNumberSeqImageQuestionIncrementRes;
import libgdx.implementations.iqtest.IqTestSpecificResource;
import libgdx.implementations.iqtest.spec.IqGameQuestionUtil;
import libgdx.implementations.iqtest.spec.IqTestCurrentGame;
import libgdx.implementations.iqtest.spec.IqTestLevelCreator;
import libgdx.resources.Res;
import libgdx.resources.dimen.MainDimen;
import libgdx.resources.gamelabel.MainGameLabel;
import libgdx.screen.AbstractScreen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.Utils;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class IqTestNumberSeqCreator extends IqTestLevelCreator {

    private QuestionConfigFileHandler fileHandler = new QuestionConfigFileHandler();
    private MyWrappedLabel pressedLettersLabel;
    private List<Integer> pressedAnswers = new ArrayList<>();
    private final static String MAIN_TABLE_NAME = "MAIN_TABLE_NAME";
    private final static List<Integer> availableNr = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
    private MyButton clearBtn;
    private MyButton submitBtn;
    private AbstractScreen abstractScreen;
    private ActorAnimation actorAnimation;

    public IqTestNumberSeqCreator(IqTestCurrentGame currentGame, AbstractScreen abstractScreen) {
        super(currentGame);
        int questionNr = currentGame.getCurrentQuestion();
        this.abstractScreen = abstractScreen;
        actorAnimation = new ActorAnimation(abstractScreen);

        pressedLettersLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setFontConfig(new FontConfig(Color.WHITE, Color.BLACK,
                        FontConfig.FONT_SIZE * 2.1f, FontConfig.STANDARD_BORDER_WIDTH * 8.5f))
                .setWidth(getPressedLettersLabelWidth())
                .setText(" ").build());
        clearBtn = new ButtonBuilder()
                .setFixedButtonSize(SkelClassicButtonSize.IQTEST_NUM_SEQ_SUBMIT_DELETE)
                .setButtonSkin(SkelClassicButtonSkin.IQTEST_DELETE_BTN).build();
        clearBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearPressedLetters();
            }
        });
        clearBtn.setVisible(false);
        submitBtn = new ButtonBuilder()
                .setFixedButtonSize(SkelClassicButtonSize.IQTEST_NUM_SEQ_SUBMIT_DELETE)
                .setButtonSkin(SkelClassicButtonSkin.IQTEST_SUBMIT_BTN).build();
        submitBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (pressedAnswers.size() > 0) {
                    Integer answer = Integer.valueOf(StringUtils.join(pressedAnswers, ""));
                    iqTestCurrentGame.getQuestionWithAnswer().put(iqTestCurrentGame.getCurrentQuestion(), answer);
                    if (iqTestCurrentGame.getCurrentQuestionEnum().getAnwser() == answer) {
                        scoreLabel.setText(getScore());
                        processCorrectAnswerPressed(new Runnable() {
                            @Override
                            public void run() {
                                goToNextLevel();
                            }
                        });
                    } else {
                        goToNextLevel();
                    }
                    clearPressedLetters();
                }
            }
        });
        submitBtn.setVisible(false);
        addQuestionScreen(questionNr);
    }

    private void processCorrectAnswerPressed(Runnable afterAnimation) {
        float scaleFactor = 0.3f;
        float duration = 0.2f;
        if (scoreLabel != null) {
            scoreLabel.addAction(Actions.sequence(Actions.scaleBy(scaleFactor, scaleFactor, duration),
                    Actions.scaleBy(-scaleFactor, -scaleFactor, duration), Utils.createRunnableAction(afterAnimation)));
        }
    }

    @Override
    protected String getScore() {
        int totalScore = 0;
        Map<Integer, Integer> questionWithAnswer = iqTestCurrentGame.getQuestionWithAnswer();
        for (IqNumSeqQuestion question : IqNumSeqQuestion.values()) {
            if (questionWithAnswer.get(question.getQuestionNr()) == question.getAnwser()) {
                totalScore++;
            }
        }
        String prefix = totalScore > 0 ? "+" : "";
        return prefix + totalScore;
    }

    private void clearPressedLetters() {
        pressedAnswers.clear();
        pressedLettersLabel.setText(" ");
        actorAnimation.animateFastFadeOut(clearBtn);
        actorAnimation.animateFastFadeOut(submitBtn);
    }

    public void refreshLevel() {
        Group root = abstractScreen.getRoot();
        root.findActor(MAIN_TABLE_NAME).remove();
//        addQuestionScreen(iqTestCurrentGame.getCurrentQuestion());
        addQuestionScreen(0);
//        saveCurrentState();
    }

    public void addQuestionScreen(int questionNr) {
        List<String> answerCoordsSolution = Arrays.asList(fileHandler.getFileText(String.format("questions/numberseq/q%sa.txt",
                questionNr)).split("\n"));
        IqTestNumSeqLevelInfo iqTestNumSeqLevelInfo = new IqTestNumSeqLevelInfo(answerCoordsSolution);
        iqTestNumSeqLevelInfo.setBackgroundColor(abstractScreen);

        Table table = new Table();
        table.setName(MAIN_TABLE_NAME);
        int screenWidth = ScreenDimensionsManager.getScreenWidth();
        float verticalGeneralMarginDimen = MainDimen.vertical_general_margin.getDimen();
        table.add(createHeader()).pad(verticalGeneralMarginDimen).width(screenWidth).row();
        table.add().growY().row();
        table.add(createQuestionTable(questionNr, 40, iqTestNumSeqLevelInfo))
                .width(ScreenDimensionsManager.getScreenWidth())
                .padBottom(verticalGeneralMarginDimen * 2)
                .row();
        table.add().growY().row();
        table.add(createAnswerButtonsTable()).padBottom(verticalGeneralMarginDimen).width(screenWidth);
        table.setFillParent(true);
        abstractScreen.addActor(table);
    }

    private Table createQuestionWithAnswerTable(int questionNr, IqTestNumSeqLevelInfo iqTestNumSeqLevelInfo) {
        Table table = new Table();
        float verticalGeneralMarginDimen = MainDimen.vertical_general_margin.getDimen();
        Table imgTable = createQuestionTable(questionNr, iqTestNumSeqLevelInfo.getAnswerSolution().size() > 5 ? 35 : 50, iqTestNumSeqLevelInfo);
        Table answerTable = createAnswerTable(iqTestNumSeqLevelInfo);
        table.add(imgTable).padBottom(verticalGeneralMarginDimen);
        table.row();
        table.add(answerTable);
        return table;
    }

    private Table createAnswerTable(IqTestNumSeqLevelInfo iqTestNumSeqLevelInfo) {
        Table table = new Table();

        float solRowHeight = ScreenDimensionsManager.getScreenHeightValue(5);
        MyWrappedLabel answerLabel = createAnswLabel(MainGameLabel.l_correct_answer.getText()
                + ": " + iqTestCurrentGame.getCurrentQuestionEnum().getAnwser(), 1.8f, FontColor.WHITE);
        table.add(answerLabel).height(solRowHeight).row();

        for (String sol : iqTestNumSeqLevelInfo.getAnswerSolution()) {
            MyWrappedLabel solutionLabel = createAnswLabel(sol, 1.2f, FontColor.WHITE);
            table.add(solutionLabel).height(solRowHeight).row();
        }
        return table;
    }

    private Table createQuestionTable(int questionNr, int ifHeightGreaterPercent, IqTestNumSeqLevelInfo iqTestNumSeqLevelInfo) {
        Res questionRes = resourceService.getByName(String.format(IqTestNumberSeqImageQuestionIncrementRes.NUM_SEQ_RES_Q, questionNr));
        Image image = GraphicUtils.getImage(questionRes);
        float imgHeight = image.getHeight();
        float imgWidth = image.getWidth();
        if (imgHeight > imgWidth) {
            image.setHeight(ScreenDimensionsManager.getScreenHeightValue(ifHeightGreaterPercent));
            image.setWidth(ScreenDimensionsManager.getNewWidthForNewHeight(image.getHeight(), imgWidth, imgHeight));
        } else {
            int percent = imgWidth / imgHeight > 2 ? 100 : 75;
            image.setWidth(ScreenDimensionsManager.getScreenWidthValue(percent));
            image.setHeight(ScreenDimensionsManager.getNewHeightForNewWidth(image.getWidth(), imgWidth, imgHeight));
        }

        Table imgTable = new Table();
        Group questionImageGroup = createQuestionImageGroup(image, iqTestNumSeqLevelInfo);
        imgTable.add(questionImageGroup).width(questionImageGroup.getWidth()).height(questionImageGroup.getHeight());
        image.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.print((x / image.getWidth()) * 100 + "," + (y / image.getHeight()) * 100 + "\n");
                createCorrectAnswerPopup(questionNr, iqTestNumSeqLevelInfo).addToPopupManager();
            }
        });
        return imgTable;
    }

    private Group createQuestionImageGroup(Image image, IqTestNumSeqLevelInfo iqTestNumSeqLevelInfo) {
        String[] questionMarkInfoArray = iqTestNumSeqLevelInfo.getQuestionMarkInfo().split(",");
        Group group = new Group();
        group.addActor(image);
        group.setHeight(image.getHeight());
        group.setWidth(image.getWidth());
        float imgSideDimen = ScreenDimensionsManager.getScreenWidthValue(Integer.valueOf(questionMarkInfoArray[1]));
        Image questionMark = GraphicUtils.getImage(IqTestSpecificResource.valueOf(questionMarkInfoArray[0]));
        questionMark.setWidth(imgSideDimen);
        questionMark.setHeight(imgSideDimen);
        new ActorAnimation(abstractScreen).animateZoomInZoomOut(questionMark);
        questionMark.setPosition(image.getWidth() / 100 * Float.valueOf(questionMarkInfoArray[questionMarkInfoArray.length - 2]),
                image.getHeight() / 100 * Float.valueOf(questionMarkInfoArray[questionMarkInfoArray.length - 1]), Align.center);
        group.addActor(questionMark);
        return group;
    }

    private MyWrappedLabel createAnswLabel(String text, float fontSize, FontColor fontColor) {
        return new MyWrappedLabel(
                new MyWrappedLabelConfigBuilder()
//                        .setFontConfig(new FontConfig(fontColor.getColor(), FontColor.BLACK.getColor(),
//                                FontConfig.FONT_SIZE * fontSize, FontConfig.STANDARD_BORDER_WIDTH * 6))
                        .setFontConfig(new FontConfig(FontColor.BLACK.getColor(),
                                FontConfig.FONT_SIZE * fontSize))
                        .setSingleLineLabel()
                        .setText(text)
                        .build());
    }

    private Table createAnswerButtonsTable() {
        Table table = new Table();
        Table pressedAnswTable = new Table();
        pressedAnswTable.add(clearBtn).width(clearBtn.getWidth()).height(clearBtn.getHeight());
        pressedAnswTable.add(pressedLettersLabel).width(getPressedLettersLabelWidth());
        pressedAnswTable.add(submitBtn).width(submitBtn.getWidth()).height(submitBtn.getHeight());
        table.add(pressedAnswTable).padBottom(MainDimen.vertical_general_margin.getDimen()).row();
        table.add(createAnswerBtnTable());
        return table;
    }

    private MyPopup createCorrectAnswerPopup(final int questionNr, IqTestNumSeqLevelInfo iqTestNumSeqLevelInfo) {
        return new MyPopup<AbstractScreen, ImageSplitScreenManager>(abstractScreen) {
            @Override
            protected String getLabelText() {
                return "";
            }

            @Override
            protected void setBackground() {
                setBackground(GraphicUtils.getColorBackground(Color.WHITE));
            }

            @Override
            protected void addButtons() {
            }

            @Override
            public void hide() {
                super.hide();
            }

            @Override
            public float getPrefWidth() {
                return ScreenDimensionsManager.getScreenWidth();
            }

            @Override
            public MyPopup addToPopupManager() {
                super.addToPopupManager();
                getContentTable().add(createQuestionWithAnswerTable(questionNr, iqTestNumSeqLevelInfo));
                return this;
            }
        };
    }

    private float getPressedLettersLabelWidth() {
        return ScreenDimensionsManager.getScreenWidthValue(60);
    }

    private Table createAnswerBtnTable() {
        List<MyButton> answList = createAnswButtons();
        int nrOfRows = 2;
        int nrOfAnswersOnRow = 5;
        int answerIndex = 0;
        Table buttonTable = new Table();
        float verticalGeneralMarginDimen = MainDimen.vertical_general_margin.getDimen();
        for (int i = nrOfRows; i >= 0; i--) {
            Table buttonRow = new Table();
            for (int j = 0; j < nrOfAnswersOnRow; j++) {
                if (answerIndex < answList.size()) {
                    MyButton button = answList.get(answerIndex);
                    buttonRow.add(button).width(button.getWidth())
                            .height(button.getHeight()).pad(verticalGeneralMarginDimen / 2);
                    answerIndex++;
                }
            }
            buttonTable.add(buttonRow).padBottom(verticalGeneralMarginDimen / 2).row();
        }
        return buttonTable;
    }

    private List<MyButton> createAnswButtons() {
        List<MyButton> answList = new ArrayList<>();
        for (Integer integer : availableNr) {
            MyButton button = new ButtonBuilder()
                    .setFixedButtonSize(SkelClassicButtonSize.IQTEST_NUM_SEQ_BUTTON)
                    .setButtonSkin(SkelClassicButtonSkin.IQTEST_NUM_SEQ_BTN)
                    .setFontConfig(new FontConfig(Color.WHITE, Color.BLACK,
                            FontConfig.FONT_SIZE * 1.5f, FontConfig.STANDARD_BORDER_WIDTH * 8.5f))
                    .setText(integer + "")
                    .build();
            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (pressedAnswers.size() < 5) {
                        pressedAnswers.add(integer);
                        pressedLettersLabel.setText(StringUtils.join(pressedAnswers, ""));
                        if (!clearBtn.isVisible()) {
                            actorAnimation.animateFastFadeIn(clearBtn);
                            actorAnimation.animateFastFadeIn(submitBtn);
                        }
                    }
                }
            });
            answList.add(button);
        }
        return answList;
    }

}
