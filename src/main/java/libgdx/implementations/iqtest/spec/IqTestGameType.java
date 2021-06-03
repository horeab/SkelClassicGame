package libgdx.implementations.iqtest.spec;

import libgdx.implementations.iqtest.IqTestGameLabel;
import libgdx.implementations.iqtest.IqTestSpecificResource;
import libgdx.implementations.iqtest.spec.iqtest.IqTestQuestion;
import libgdx.implementations.iqtest.spec.numseq.IqNumSeqQuestion;

public enum IqTestGameType {

    IQ_TEST(IqTestGameLabel.gametype_iqtest, null, IqTestSpecificResource.btn_level_main_0_up, IqTestQuestion.values().length),
    SPACE(IqTestGameLabel.gametype_space, IqTestGameLabel.gametype_space_descr, IqTestSpecificResource.btn_level_main_3_up, 10),
    NUM_SEQ(IqTestGameLabel.gametype_numseq, null, IqTestSpecificResource.btn_level_main_1_up, IqNumSeqQuestion.values().length),
    MEM_NUM(IqTestGameLabel.gametype_memnum, IqTestGameLabel.gametype_memnum_descr, IqTestSpecificResource.btn_level_main_2_up, 10),
    ;

    public IqTestGameLabel name;
    public IqTestGameLabel descr;
    public IqTestSpecificResource icon;
    public int totalQuestions;

    IqTestGameType(IqTestGameLabel name, IqTestGameLabel descr, IqTestSpecificResource icon, int totalQuestions) {
        this.name = name;
        this.descr = descr;
        this.icon = icon;
        this.totalQuestions = totalQuestions;
    }

}
