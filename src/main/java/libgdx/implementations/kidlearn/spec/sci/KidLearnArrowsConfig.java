package libgdx.implementations.kidlearn.spec.sci;

import java.util.List;

import libgdx.implementations.kidlearn.spec.KidLearnLevelConfig;
import libgdx.resources.Res;

public class KidLearnArrowsConfig extends KidLearnLevelConfig {

    public Res mainImg;
    public List<KidLearnArrowConfig> words;

    public KidLearnArrowsConfig(Res mainImg, List<KidLearnArrowConfig> words) {
        this.mainImg = mainImg;
        this.words = words;
    }
}
