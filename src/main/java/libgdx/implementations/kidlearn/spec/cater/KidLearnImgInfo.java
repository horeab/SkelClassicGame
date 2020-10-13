package libgdx.implementations.kidlearn.spec.cater;

import com.badlogic.gdx.scenes.scene2d.ui.Stack;

import org.apache.commons.lang3.tuple.Pair;

public class KidLearnImgInfo {

    public KidLearnImgInfo(Pair<Float, Float> initialCoord, Stack img, Float nr) {
        this.initialCoord = initialCoord;
        this.img = img;
        this.nr = nr;
    }

    Pair<Float, Float> initialCoord;
    Stack img;
    Float nr;
}
