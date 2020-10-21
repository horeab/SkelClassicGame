package libgdx.implementations.kidlearn.spec.eng;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import libgdx.controls.animations.ActorAnimation;
import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.MyButton;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.game.Game;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.SkelClassicButtonSize;
import libgdx.implementations.SkelClassicButtonSkin;
import libgdx.implementations.kidlearn.spec.KidLearnGameContext;
import libgdx.implementations.kidlearn.spec.KidLearnGameCreator;
import libgdx.implementations.kidlearn.spec.KidLearnWordImgConfig;
import libgdx.resources.dimen.MainDimen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;

public class KidLearnEngHangmanGameCreator extends KidLearnGameCreator {

    private static final String STANDARD_LETTERS = "a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z";
    public static final int TOTAL_QUESTIONS = 3;
    KidLearnWordImgConfig kidLearnWordImgConfig;
    MyWrappedLabel answersLabel;
    private Map<String, MyButton> allAnswerButtons;
    private List<String> pressedAnswers = new ArrayList<>();

    public KidLearnEngHangmanGameCreator(KidLearnGameContext gameContext, KidLearnWordImgConfig kidLearnWordImgConfig) {
        super(gameContext);
        this.kidLearnWordImgConfig = kidLearnWordImgConfig;
        this.allAnswerButtons = createAnswerOptionsButtons();
    }

    @Override
    protected int getTotalQuestions() {
        return TOTAL_QUESTIONS;
    }

    public Table createTable() {
        super.create();
        Table table = new Table();
        Table image = new Table();
        float imgSideDimen = ScreenDimensionsManager.getScreenHeightValue(30);
        image.add(GraphicUtils.getImage(kidLearnWordImgConfig.img)).width(imgSideDimen).height(imgSideDimen);
        table.add(image).row();
        table.add(createPressedAnswersTable()).row();
        Table squareAnswerOptionsTable = new Table();
        squareAnswerOptionsTable.add(createSquareAnswerOptionsTable(new ArrayList<>(allAnswerButtons.values())));
        table.add(squareAnswerOptionsTable);
        return table;
    }

    private Table createPressedAnswersTable() {
        Table table = new Table();
        answersLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setFontConfig(new FontConfig(FontColor.WHITE.getColor(), FontColor.GREEN.getColor(),
                        Math.round(FontConfig.FONT_SIZE * 1.7f), 4f))
                .setText(getUnkString(kidLearnWordImgConfig.word.length())).build());
        table.add(answersLabel).padBottom(MainDimen.vertical_general_margin.getDimen() * 1).row();
        return table;
    }

    private String getUnkString(int length) {
        StringBuilder unkString = new StringBuilder();
        for (int i = 0; i < length; i++) {
            unkString.append("_");
        }
        return unkString.toString();
    }

    private int getNrOfAnswerRows() {
        return 3;
    }

    private int getNrOfAnswersOnRow() {
        return (int) Math.ceil(getAllAnswerOptions().size() / Float.valueOf(getNrOfAnswerRows()));
    }

    private MyButton createAnswerButton(final String answer) {
        MyButton myButton = new ButtonBuilder()
                .setFontConfig(new FontConfig(FontColor.WHITE.getColor(), FontColor.BLACK.getColor(),
                        Math.round(FontConfig.FONT_SIZE * 1.3f), 4f))
                .setText(answer.toUpperCase())
                .setButtonSkin(SkelClassicButtonSkin.KIDLEARN_HANGMAN_LETTER)
                .setFixedButtonSize(SkelClassicButtonSize.KIDLEARN_HANGMAN_BUTTON)
                .build();
        myButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String word = kidLearnWordImgConfig.word;
                if (pressedAnswers.size() < word.length()) {
                    pressedAnswers.add(answer.toUpperCase());
                    String join = StringUtils.join(pressedAnswers, "");
                    if (join.toLowerCase().equals(word.toLowerCase().substring(0, join.length()))) {
                        String answ = StringUtils.capitalize(join.toLowerCase());
                        answersLabel.setText(answ + getUnkString(kidLearnWordImgConfig.word.length() - answ.length()));
                        if (pressedAnswers.size() == word.length()) {
                            executeCorrectAnswer();
                        }
                    } else {
                        new ActorAnimation(Game.getInstance().getAbstractScreen()).animatePulse();
                        pressedAnswers.remove(pressedAnswers.size() - 1);
                    }
                }
            }
        });
        return myButton;

    }

    private Map<String, MyButton> createAnswerOptionsButtons() {
        Map<String, MyButton> allAnswerButtons = new LinkedHashMap<>();
        for (String answer : getAllAnswerOptions()) {
            MyButton button = createAnswerButton(answer);
            allAnswerButtons.put(answer.toUpperCase(), button);
        }
        return allAnswerButtons;
    }

    private Table createSquareAnswerOptionsTable(List<MyButton> allAnswerButtons) {
        int answerIndex = 0;
        Table buttonTable = new Table();
        float verticalGeneralMarginDimen = MainDimen.vertical_general_margin.getDimen();
        for (int i = getNrOfAnswerRows(); i >= 0; i--) {
            Table buttonRow = new Table();
            for (int j = 0; j < getNrOfAnswersOnRow(); j++) {
                if (answerIndex < allAnswerButtons.size()) {
                    MyButton button = allAnswerButtons.get(answerIndex);
                    buttonRow.add(button).width(button.getWidth()).height(button.getHeight()).padRight(ScreenDimensionsManager.getScreenWidthValue(0.65f));
                    answerIndex++;
                }
            }
            buttonTable.add(buttonRow).padBottom(verticalGeneralMarginDimen / 2).row();
        }
        return buttonTable;
    }

    public List<String> getAllAnswerOptions() {
        return new ArrayList<>(Arrays.asList(STANDARD_LETTERS.split(",")));
    }
}
