package libgdx.implementations.iqtest.spec;

import libgdx.implementations.iqtest.IqTestGameLabel;
import libgdx.implementations.iqtest.IqTestSpecificResource;

public enum IqTestGameType {

    IQ_TEST(IqTestGameLabel.gametype_iqtest, null, IqTestSpecificResource.btn_level_main_0_up),
    SPACE(IqTestGameLabel.gametype_space, IqTestGameLabel.gametype_space_descr, IqTestSpecificResource.btn_level_main_3_up),
    NUM_SEQ(IqTestGameLabel.gametype_numseq, null, IqTestSpecificResource.btn_level_main_1_up),
    MEM_NUM(IqTestGameLabel.gametype_memnum, IqTestGameLabel.gametype_memnum_descr, IqTestSpecificResource.btn_level_main_2_up),
    ;

    IqTestGameLabel name;
    IqTestGameLabel descr;
    IqTestSpecificResource icon;

    IqTestGameType(IqTestGameLabel name, IqTestGameLabel descr, IqTestSpecificResource icon) {
        this.name = name;
        this.descr = descr;
        this.icon = icon;
    }

    public IqTestGameLabel getName() {
        return name;
    }

    public IqTestGameLabel getDescr() {
        return descr;
    }

    public IqTestSpecificResource getIcon() {
        return icon;
    }
}
