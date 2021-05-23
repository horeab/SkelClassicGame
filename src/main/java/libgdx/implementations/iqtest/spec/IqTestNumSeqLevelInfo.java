package libgdx.implementations.iqtest.spec;

import libgdx.screen.AbstractScreen;
import libgdx.utils.model.RGBColor;

import java.util.List;

public class IqTestNumSeqLevelInfo {

    private List<String> levelInfo;

    public IqTestNumSeqLevelInfo(List<String> levelInfo) {
        this.levelInfo = levelInfo;
    }

    public void setBackgroundColor(AbstractScreen abstractScreen) {
        abstractScreen.setBackgroundColor(new RGBColor("#" + levelInfo.get(2).trim()));
    }

    public String getQuestionMarkInfo() {
        return levelInfo.get(1);
    }

    public String getCorrectAnswer() {
        return levelInfo.get(0);
    }

    public List<String> getAnswerSolution() {
        return levelInfo.subList(3, levelInfo.size());
    }
}
