package libgdx.implementations.kidlearn.spec.eng;

import java.util.List;

import libgdx.implementations.kidlearn.spec.KidLearnLevelConfig;
import libgdx.implementations.kidlearn.spec.KidLearnWordImgConfig;

public class KidLearnEngWordsConfig extends KidLearnLevelConfig {

    public List<KidLearnWordImgConfig> words;

    public KidLearnEngWordsConfig( List<KidLearnWordImgConfig> words) {
        this.words = words;
    }
}
