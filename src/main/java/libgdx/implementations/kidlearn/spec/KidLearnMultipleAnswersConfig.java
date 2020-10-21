package libgdx.implementations.kidlearn.spec;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;


public class KidLearnMultipleAnswersConfig extends KidLearnLevelConfig {

    public Pair<KidLearnWordImgConfig, List<KidLearnWordImgConfig>> vals;

    public KidLearnMultipleAnswersConfig(Pair<KidLearnWordImgConfig, List<KidLearnWordImgConfig>> vals) {
        this.vals = vals;
    }
}
