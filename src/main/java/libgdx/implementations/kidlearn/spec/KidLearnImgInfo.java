package libgdx.implementations.kidlearn.spec;

import com.badlogic.gdx.scenes.scene2d.ui.Stack;

import org.apache.commons.lang3.tuple.Pair;

public class KidLearnImgInfo {

    public KidLearnImgInfo(Pair<Float, Float> initialCoord, Stack img, String val) {
        this.initialCoord = initialCoord;
        this.img = img;
        this.val = val;
    }

    public Pair<Float, Float> initialCoord;
    public Stack img;
    public String val;
}
