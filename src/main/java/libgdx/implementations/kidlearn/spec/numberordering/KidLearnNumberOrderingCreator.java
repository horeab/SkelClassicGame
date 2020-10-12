package libgdx.implementations.kidlearn.spec.numberordering;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;

import java.util.ArrayList;
import java.util.List;

import libgdx.graphics.GraphicUtils;
import libgdx.resources.MainResource;
import libgdx.screen.AbstractScreen;
import libgdx.utils.ScreenDimensionsManager;

public class KidLearnNumberOrderingCreator {

    int totalNumbers;
    int totalUnknownNumber;
    int totalOptions;
    List<Stack> unknownNumberImg = new ArrayList<>();
    AbstractScreen screen;

    public KidLearnNumberOrderingCreator(AbstractScreen screen) {
        this.screen = screen;
    }

    public void create() {
        Stack img1 = createNumberStack();
        img1.setX(ScreenDimensionsManager.getScreenWidthValue(20));
        img1.setY(ScreenDimensionsManager.getScreenHeightValue(20));
        img1.addListener(new DragListener() {
            public void drag(InputEvent event, float x, float y, int pointer) {
                img1.moveBy(x - img1.getWidth() / 2, y - img1.getHeight() / 2);
            }
        });
        screen.addActor(img1);
    }

    private Stack createNumberStack() {
        Stack stack = new Stack();
        stack.add(GraphicUtils.getImage(MainResource.heart_full));
        return stack;
    }

    private float getNumberImgSideDimen() {
        return ScreenDimensionsManager.getScreenWidthValue(10);
    }

}
