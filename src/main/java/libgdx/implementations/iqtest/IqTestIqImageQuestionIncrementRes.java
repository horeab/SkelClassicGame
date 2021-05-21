package libgdx.implementations.iqtest;

import libgdx.resources.IncrementingRes;

public class IqTestIqImageQuestionIncrementRes extends IncrementingRes {

    public final static String PNG = "png";

    IqTestIqImageQuestionIncrementRes(int beginIndex, int endIndex, String imgExtension) {
        super(beginIndex, endIndex,
                IqTestGame.getInstance().getAppInfoService().getImplementationGameResourcesFolder() + "questions/iq/q%s." + imgExtension,
                "q%s");
    }

}
