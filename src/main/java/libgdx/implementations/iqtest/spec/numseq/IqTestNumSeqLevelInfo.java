package libgdx.implementations.iqtest.spec.numseq;

import libgdx.screen.AbstractScreen;
import libgdx.utils.model.RGBColor;

import java.util.List;

public class IqTestNumSeqLevelInfo {

    private List<String> levelInfo;

    public IqTestNumSeqLevelInfo(List<String> levelInfo) {
        this.levelInfo = levelInfo;
    }

    public void setBackgroundColor(AbstractScreen abstractScreen) {
        abstractScreen.setBackgroundColor(new RGBColor("#" + levelInfo.get(1).trim()));
    }

    public String getQuestionMarkInfo() {
        return levelInfo.get(0);
    }

    public List<String> getAnswerSolution() {
        return levelInfo.subList(2, levelInfo.size());
    }
}
