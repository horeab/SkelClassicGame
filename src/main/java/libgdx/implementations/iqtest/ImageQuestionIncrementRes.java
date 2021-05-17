package libgdx.implementations.iqtest;

import libgdx.resources.IncrementingRes;

public class ImageQuestionIncrementRes extends IncrementingRes {

    public final static String PNG = "png";

    ImageQuestionIncrementRes(int beginIndex, int endIndex, String imgExtension) {
        super(beginIndex, endIndex,
                IqTestGame.getInstance().getAppInfoService().getImplementationGameResourcesFolder() + "questions/q%s." + imgExtension,
                "q%s");
    }

}
