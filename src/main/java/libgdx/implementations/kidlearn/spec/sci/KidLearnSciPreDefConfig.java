package libgdx.implementations.kidlearn.spec.sci;

import org.apache.commons.lang3.tuple.Pair;

import libgdx.resources.Res;

public class KidLearnSciPreDefConfig {
    public KidLearnSciPreDefConfig(String word, Res img, Res unkImg, Pair<Integer, Integer> posInMatrix) {
        this.word = word;
        this.img = img;
        this.unkImg = unkImg;
        this.posInMatrix = posInMatrix;
    }

    public String word;
    public Res img;
    public Res unkImg;
    public Pair<Integer, Integer> posInMatrix;
}
