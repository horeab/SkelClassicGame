package libgdx.implementations;

import libgdx.controls.button.ButtonSize;
import libgdx.resources.dimen.MainDimen;

public enum SkelClassicButtonSize implements ButtonSize {

    RESOURCEWARS_LOCATION_BTN(MainDimen.horizontal_general_margin.getDimen() * 15f, MainDimen.horizontal_general_margin.getDimen() * 8),
    RESOURCEWARS_MENU_BTN(MainDimen.horizontal_general_margin.getDimen() * 30, MainDimen.horizontal_general_margin.getDimen() * 12),

    MATH_CORRECT_WRONG_IMG_BTN(MainDimen.side_btn_image.getDimen() * 2f, MainDimen.side_btn_image.getDimen() * 2f),

    APPLEPIE_PRESS_BTN(MainDimen.side_btn_image.getDimen() * 3f, MainDimen.side_btn_image.getDimen() * 3f),
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
