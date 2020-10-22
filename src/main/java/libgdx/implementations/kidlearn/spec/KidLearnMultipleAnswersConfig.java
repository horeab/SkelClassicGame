package libgdx.implementations.kidlearn.spec;

import java.util.List;
import java.util.Map;


public class KidLearnMultipleAnswersConfig extends KidLearnLevelConfig {

    public Map<KidLearnWordImgConfig, List<KidLearnWordImgConfig>> responseWithAnswers;

    public KidLearnMultipleAnswersConfig(Map<KidLearnWordImgConfig, List<KidLearnWordImgConfig>> responseWithAnswers) {
        this.responseWithAnswers = responseWithAnswers;
    }
}
