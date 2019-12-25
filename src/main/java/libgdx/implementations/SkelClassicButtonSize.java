package libgdx.implementations;

import libgdx.controls.button.ButtonSize;
import libgdx.resources.dimen.MainDimen;

public enum SkelClassicButtonSize implements ButtonSize {

    MATH_CORRECT_WRONG_IMG_BTN(MainDimen.side_btn_image.getDimen() * 2f, MainDimen.side_btn_image.getDimen() * 2f),
    ;

    private float width;
    private float height;

    SkelClassicButtonSize(float width, float height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public float getHeight() {
        return height;
    }
}
