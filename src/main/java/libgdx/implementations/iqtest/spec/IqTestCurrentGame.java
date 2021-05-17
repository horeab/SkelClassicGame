package libgdx.implementations.iqtest.spec;

import java.util.LinkedHashMap;
import java.util.Map;

public class IqTestCurrentGame {

    private int currentQuestion;

    private Map<Integer, Integer> questionWithAnswer = new LinkedHashMap<>();

    public IqTestCurrentGame() {
        resetQA();
    }

    public IqTestCurrentGame(int currentQuestion, Map<Integer, Integer> questionWithAnswer) {
        this.currentQuestion = currentQuestion;
        this.questionWithAnswer = questionWithAnswer;
    }

    public void reset() {
        this.currentQuestion = 0;
        resetQA();
    }

    private void resetQA() {
        questionWithAnswer.clear();
        for (IqTestQuestion iqTestQuestion : IqTestQuestion.values()) {
            questionWithAnswer.put(iqTestQuestion.getQuestionNr(), -1);
        }
    }

    public int getCurrentQuestion() {
        return currentQuestion;
    }

    public int getCurrentQuestionToDisplay() {
        return currentQuestion + 1;
    }

    public void setCurrentQuestion(int currentQuestion) {
        this.currentQuestion = currentQuestion;
    }

    public Map<Integer, Integer> getQuestionWithAnswer() {
        return questionWithAnswer;
    }

    public void setQuestionWithAnswer(Map<Integer, Integer> questionWithAnswer) {
        this.questionWithAnswer = questionWithAnswer;
    }
}
