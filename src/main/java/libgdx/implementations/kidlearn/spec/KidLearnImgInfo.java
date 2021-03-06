package libgdx.implementations.kidlearn.spec;

import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import org.apache.commons.lang3.tuple.Pair;

public class KidLearnImgInfo {

    public KidLearnImgInfo(Pair<Float, Float> initialCoord, Table img, String val) {
        this.initialCoord = initialCoord;
        this.img = img;
        this.val = val;
    }

    public Pair<Float, Float> initialCoord;
    public Table img;
    public String val;
}
