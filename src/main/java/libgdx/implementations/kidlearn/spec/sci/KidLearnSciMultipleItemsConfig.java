package libgdx.implementations.kidlearn.spec.sci;

import java.util.List;

import libgdx.implementations.kidlearn.spec.KidLearnLevelConfig;
import libgdx.implementations.kidlearn.spec.KidLearnWordImgConfig;
import libgdx.resources.Res;

public class KidLearnSciMultipleItemsConfig extends KidLearnLevelConfig {

    public List<KidLearnWordImgConfig> words;
    public List<Res> responses;

    public KidLearnSciMultipleItemsConfig( List<KidLearnWordImgConfig> words, List<Res> responses) {
        this.words = words;
        this.responses = responses;
    }
}
