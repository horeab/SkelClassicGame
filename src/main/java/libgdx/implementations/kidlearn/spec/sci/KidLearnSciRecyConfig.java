package libgdx.implementations.kidlearn.spec.sci;

import java.util.List;

import libgdx.implementations.kidlearn.spec.KidLearnWordImgConfig;
import libgdx.resources.Res;

public class KidLearnSciRecyConfig {

    public List<KidLearnWordImgConfig> words;
    public List<Res> responses;

    public KidLearnSciRecyConfig(List<KidLearnWordImgConfig> words, List<Res> responses) {
        this.words = words;
        this.responses = responses;
    }
}
