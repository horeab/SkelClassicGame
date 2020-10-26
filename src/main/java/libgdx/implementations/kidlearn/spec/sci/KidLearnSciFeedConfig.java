package libgdx.implementations.kidlearn.spec.sci;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

import libgdx.implementations.kidlearn.spec.KidLearnLevelConfig;
import libgdx.implementations.kidlearn.spec.KidLearnWordImgConfig;

public class KidLearnSciFeedConfig extends KidLearnLevelConfig {

    public List<Pair<KidLearnWordImgConfig, KidLearnWordImgConfig>> words;

    public KidLearnSciFeedConfig(List<Pair<KidLearnWordImgConfig, KidLearnWordImgConfig>> words) {
        this.words = words;
    }
}
