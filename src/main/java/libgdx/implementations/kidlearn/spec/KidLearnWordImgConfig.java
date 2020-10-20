package libgdx.implementations.kidlearn.spec;

import libgdx.resources.Res;

public class KidLearnWordImgConfig extends KidLearnLevelConfig{
    public KidLearnWordImgConfig(String word, Res img) {
        this.word = word;
        this.img = img;
    }

    public String word;
    public Res img;
}
