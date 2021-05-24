package libgdx.implementations.iqtest.spec;

import java.util.List;

public class IqGameQuestionUtil {


    public static String getQuestionFileName(int questionNr) {
        return "q" + questionNr;
    }

    public static String getAnswerForQuestionFileName(int questionNr, int answerNr) {
        return "q" + questionNr + "a" + answerNr;
    }

    public static IqGameQuestion getQuestion(int nr, List<IqGameQuestion> allQuestions) {
        for (IqGameQuestion iqTestQuestion : allQuestions) {
            if (iqTestQuestion.getQuestionNr() == nr) {
                return iqTestQuestion;
            }
        }
        return null;
    }
}
