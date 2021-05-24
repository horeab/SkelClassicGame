package libgdx.implementations.iqtest.spec.numseq;

import libgdx.implementations.iqtest.spec.IqGameQuestion;

import java.util.Arrays;
import java.util.List;

public enum IqNumSeqQuestion implements IqGameQuestion {

    //@formatter:off
    Q0(0, 10),
    Q1(1, 36),
    Q2(2, 4),
    Q3(3, 3),
    Q4(4, 3),
    Q5(5, 38),
    Q6(6, 14),
    Q7(7, 81),
    Q8(8, 2),
    Q9(9, 6),
    Q10(10, 32),
    Q11(11, 636),
    Q12(12, 469),
    Q13(13, 19),
    ;
    //@formatter:on

    private int questionNr;
    private int anwser;

    @Override
    public int getQuestionNr() {
        return questionNr;
    }

    @Override
    public int getAnwser() {
        return anwser;
    }

    @Override
    public List<IqGameQuestion> getEnumAllValues() {
        return Arrays.asList(values());
    }

    IqNumSeqQuestion(int questionNr, int anwser) {
        this.questionNr = questionNr;
        this.anwser = anwser;
    }
}
