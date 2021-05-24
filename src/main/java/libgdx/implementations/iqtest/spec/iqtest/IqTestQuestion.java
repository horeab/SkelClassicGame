package libgdx.implementations.iqtest.spec.iqtest;

import libgdx.implementations.iqtest.spec.IqGameQuestion;

import java.util.Arrays;
import java.util.List;

public enum IqTestQuestion implements IqGameQuestion {

    //@formatter:off
    Q0(0, 3),
    Q1(1, 5),
    Q2(2, 1),
    Q3(3, 6),
    Q4(4, 0),
    Q5(5, 7),
    Q6(6, 1),
    Q7(7, 4),
    Q8(8, 7),
    Q9(9, 0),
    Q10(10, 2),

    Q11(11, 5),
    Q12(12, 1),
    Q13(13, 3),
    Q14(14, 7),
    Q15(15, 4),
    Q16(16, 5),
    Q17(17, 2),
    Q18(18, 4),
    Q19(19, 3),
    Q20(20, 6),

    Q21(21, 0),
    Q22(22, 1),
    Q23(23, 7),
    Q24(24, 1),
    Q25(25, 0),
    Q26(26, 7),
    Q27(27, 6),
    Q28(28, 4),
    Q29(29, 0),
    Q30(30, 3),

    Q31(31, 4),
    Q32(32, 6),
    Q33(33, 6),
    Q34(34, 2),
    Q35(35, 5),
    Q36(36, 7),
    Q37(37, 5),
    Q38(38, 1);;
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

    IqTestQuestion(int questionNr, int anwser) {
        this.questionNr = questionNr;
        this.anwser = anwser;
    }
}
