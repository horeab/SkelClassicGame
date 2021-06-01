package libgdx.implementations.iqtest;

import libgdx.resources.IncrementingRes;

public class IqTestSpaceImageQcQuestionIncrementRes extends IncrementingRes {

    public final static String PNG = "png";
    public static final String QC = "q%sc";

    IqTestSpaceImageQcQuestionIncrementRes(int beginIndex, int endIndex, String imgExtension) {
        super(beginIndex, endIndex,
                IqTestGame.getInstance().getAppInfoService().getImplementationGameResourcesFolder() + "questions/space/q%sc." + imgExtension,
                QC);
    }

}
