package libgdx.implementations.iqtest;

import libgdx.resources.IncrementingRes;

public class IqTestNumberSeqImageQuestionIncrementRes extends IncrementingRes {

    public final static String PNG = "PNG";
    public static final String NUM_SEQ_RES_Q = "numSeqQ%sa";

    IqTestNumberSeqImageQuestionIncrementRes(int beginIndex, int endIndex, String imgExtension) {
        super(beginIndex, endIndex,
                IqTestGame.getInstance().getAppInfoService().getImplementationGameResourcesFolder() + "questions/numberseq/q%s." + imgExtension,
                NUM_SEQ_RES_Q);
    }

}
