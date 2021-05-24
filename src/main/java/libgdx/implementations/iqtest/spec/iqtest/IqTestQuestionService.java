package libgdx.implementations.iqtest.spec.iqtest;

import libgdx.implementations.iqtest.IqTestGameLabel;
import libgdx.implementations.iqtest.spec.IqGameQuestion;
import libgdx.implementations.iqtest.spec.IqGameQuestionUtil;

import java.util.List;

public class IqTestQuestionService {

    public static final int MIN_SCORE = 57;
    public static final int MAX_SCORE = 143;

    public boolean isAnswerCorrect(int questionNr, int answer, List<IqGameQuestion> allQuestions) {
        return IqGameQuestionUtil.getQuestion(questionNr, allQuestions).getAnwser() == answer;
    }

    public int calculateIq(int correctAnswers) {
        float ratio = 2.2f;
        return (int) Math.ceil(MIN_SCORE + (correctAnswers * ratio));
    }

    public String getLevelForScore(int score) {
        String level = "";
        if (score < 70) {
            level = IqTestGameLabel.verylow.getText();
        } else if (score < 90) {
            level = IqTestGameLabel.low.getText();
        } else if (score < 110) {
            level = IqTestGameLabel.normal.getText();
        } else if (score < 130) {
            level = IqTestGameLabel.high.getText();
        } else {
            level = IqTestGameLabel.veryhigh.getText();
        }
        return level;
    }

}
