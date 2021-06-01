package libgdx.implementations.iqtest;

import libgdx.resources.IncrementingRes;

public class IqTestSpaceImageQwQuestionIncrementRes extends IncrementingRes {

    public final static String PNG = "png";
    public static final String QW = "q%sw";

    IqTestSpaceImageQwQuestionIncrementRes(int beginIndex, int endIndex, String imgExtension) {
        super(beginIndex, endIndex,
                IqTestGame.getInstance().getAppInfoService().getImplementationGameResourcesFolder() + "questions/space/q%sw." + imgExtension,
                QW);
    }

}
