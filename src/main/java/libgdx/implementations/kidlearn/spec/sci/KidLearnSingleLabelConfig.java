package libgdx.implementations.kidlearn.spec.sci;

import java.util.List;

import libgdx.implementations.kidlearn.spec.KidLearnLevelConfig;

public class KidLearnSingleLabelConfig extends KidLearnLevelConfig {

    public List<String> words;

    public KidLearnSingleLabelConfig( List<String> words) {
        this.words = words;
    }
}
