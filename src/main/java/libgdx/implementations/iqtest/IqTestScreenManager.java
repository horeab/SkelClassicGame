package libgdx.implementations.iqtest;

import libgdx.implementations.iqtest.spec.iqtest.IqTestQuestion;
import libgdx.screen.AbstractScreenManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class IqTestScreenManager extends AbstractScreenManager {

    @Override
    public void showMainScreen() {
        showScreen(IqTestScreenTypeEnum.GAME_SCREEN);
        HashMap<Integer, Integer> questionWithAnswer = new HashMap<>();
        for (IqTestQuestion question : IqTestQuestion.values()) {
            questionWithAnswer.put(question.getQuestionNr(), new Random().nextInt(8));
        }
        showScreen(IqTestScreenTypeEnum.MAIN_MENU_SCREEN);
//        showGameOver(questionWithAnswer);
//        showCorrectAnswers(questionWithAnswer);
    }

    public void showGamScreen() {
        showScreen(IqTestScreenTypeEnum.GAME_SCREEN);
        HashMap<Integer, Integer> questionWithAnswer = new HashMap<>();
        for (IqTestQuestion question : IqTestQuestion.values()) {
            questionWithAnswer.put(question.getQuestionNr(), new Random().nextInt(8));
        }
    }

    public void showGameOver(Map<Integer, Integer> questionWithAnswer) {
        showScreen(IqTestScreenTypeEnum.GAME_OVER_SCREEN, questionWithAnswer);
    }

    public void showCorrectAnswers(Map<Integer, Integer> questionWithAnswer) {
        showScreen(IqTestScreenTypeEnum.CORRECT_ANSWERS, questionWithAnswer);
    }
}
