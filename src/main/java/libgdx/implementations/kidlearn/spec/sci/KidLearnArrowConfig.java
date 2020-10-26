package libgdx.implementations.kidlearn.spec.sci;

import libgdx.implementations.kidlearn.spec.KidLearnLevelConfig;

public class KidLearnArrowConfig extends KidLearnLevelConfig {

    public String word;
    public float wordY;
    public float arrowX;

    public KidLearnArrowConfig(String word, float wordY, float arrowX) {
        this.word = word;
        this.wordY = wordY;
        this.arrowX = arrowX;
    }
}
