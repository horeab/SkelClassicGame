package libgdx.implementations.iqtest.spec;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.MyButton;
import libgdx.game.Game;
import libgdx.implementations.SkelClassicButtonSize;
import libgdx.implementations.SkelClassicButtonSkin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class IqTestLevelCreator extends IqTestBaseLevelCreator {


    protected IqTestCurrentGame iqTestCurrentGame;

    protected IqTestLevelCreator(IqTestCurrentGame iqTestCurrentGame) {
        this.iqTestCurrentGame = iqTestCurrentGame;
    }

    @Override
    public void refreshLevel() {
        iqTestPreferencesManager.putCurrentQAState(getIqTestGameType(), iqTestCurrentGame.getQuestionWithAnswer());
        Group root = Game.getInstance().getAbstractScreen().getStage().getRoot();
        root.findActor(MAIN_TABLE_NAME).remove();
        addQuestionScreen(iqTestCurrentGame.getCurrentQuestion());
    }

    @Override
    protected MyButton createSkipBtn() {
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

    @Override
    protected int getCurrentQuestionToDisplay() {
        return iqTestCurrentGame.getCurrentQuestionToDisplay();
    }

    protected abstract void goToLevel(int level);

    @Override
    protected void startNewGame() {
        Game.getInstance().getAppInfoService().showPopupAd(new Runnable() {
            @Override
            public void run() {
                iqTestCurrentGame.reset();
                refreshLevel();
            }
        });
    }

    protected void goToNextLevel() {
        int nextQuestion = iqTestCurrentGame.getNextQuestion();
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
}
