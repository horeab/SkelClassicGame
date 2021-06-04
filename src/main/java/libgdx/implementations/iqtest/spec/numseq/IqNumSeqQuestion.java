package libgdx.implementations.iqtest.spec.numseq;

import libgdx.implementations.iqtest.spec.IqGameQuestion;

import java.util.Arrays;
import java.util.List;

public enum IqNumSeqQuestion implements IqGameQuestion {

    //@formatter:off
    Q0(0, 243),
    Q1(1, 2),
    Q2(2, 3),
    Q3(3, 10),
    Q4(4, 3),
    Q5(5, 32),
    Q6(6, 10),
    Q7(7, 4),
    Q8(8, 636),
    Q9(9, 36),
    Q10(10, 6),
    Q11(11, 2),
    Q12(12, 14),
    Q13(13, 19),
    Q14(14, 469),
    Q15(15, 38),
    Q16(16, 3),
    Q17(17, 81),
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
