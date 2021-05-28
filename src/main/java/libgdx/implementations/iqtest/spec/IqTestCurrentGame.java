package libgdx.implementations.iqtest.spec;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class IqTestCurrentGame {

    private int currentQuestion;
    private List<IqGameQuestion> questions;
    private Map<Integer, Integer> questionWithAnswer = new LinkedHashMap<>();

    public IqTestCurrentGame(List<IqGameQuestion> questions) {
        this.questions = questions;
        resetQA();
    }

    public void reset() {
        this.currentQuestion = 0;
        resetQA();
    }

    public List<IqGameQuestion> getQuestions() {
        return questions;
    }

    private void resetQA() {
        questionWithAnswer.clear();
        for (IqGameQuestion iqTestQuestion : questions) {
            questionWithAnswer.put(iqTestQuestion.getQuestionNr(), -1);
        }
    }

    public int getCurrentQuestion() {
        return currentQuestion;
    }

    public IqGameQuestion getCurrentQuestionEnum() {
        for (IqGameQuestion iqGameQuestion : questions) {
            if (iqGameQuestion.getQuestionNr() == getCurrentQuestion()) {
                return iqGameQuestion;
            }
        }
        return null;
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

    public int getNextQuestion() {
        int nextQuestion = -1;
        for (Map.Entry<Integer, Integer> entry : getQuestionWithAnswer().entrySet()) {
            if (entry.getKey() > currentQuestion && entry.getValue() == -1) {
                nextQuestion = entry.getKey();
                break;
            }

        }
        return nextQuestion;
    }
}
