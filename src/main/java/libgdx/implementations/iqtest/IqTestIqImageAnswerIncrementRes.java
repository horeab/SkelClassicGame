package libgdx.implementations.iqtest;

import libgdx.resources.IncrementingRes;

public class IqTestIqImageAnswerIncrementRes extends IncrementingRes {

    public final static String PNG = "png";

    IqTestIqImageAnswerIncrementRes(int beginIndex, int endIndex, int answerIndex, String imgExtension) {
        super(beginIndex, endIndex,
                IqTestGame.getInstance().getAppInfoService().getImplementationGameResourcesFolder() + "questions/iq/q%sa" + answerIndex + "." + imgExtension,
                "q%sa" + answerIndex);
    }

}
