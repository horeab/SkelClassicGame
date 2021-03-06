package libgdx.implementations;

import libgdx.controls.button.ButtonSize;
import libgdx.resources.dimen.MainDimen;

public enum SkelClassicButtonSize implements ButtonSize {

    RESOURCEWARS_LOCATION_BTN(MainDimen.horizontal_general_margin.getDimen() * 15f, MainDimen.horizontal_general_margin.getDimen() * 8),
    RESOURCEWARS_MENU_BTN(MainDimen.horizontal_general_margin.getDimen() * 30, MainDimen.horizontal_general_margin.getDimen() * 12),

    MATH_CORRECT_WRONG_IMG_BTN(MainDimen.side_btn_image.getDimen() * 2f, MainDimen.side_btn_image.getDimen() * 2f),

    APPLEPIE_PRESS_BTN(MainDimen.side_btn_image.getDimen() * 3f, MainDimen.side_btn_image.getDimen() * 3f),

    BUYLOW_MENU_BTN(MainDimen.horizontal_general_margin.getDimen() * 30, MainDimen.horizontal_general_margin.getDimen() * 12),
    BUYLOW_SELLBUY_BTN(MainDimen.side_btn_image.getDimen() * 2f, MainDimen.side_btn_image.getDimen() * 1.0f),

    IMAGE_SPLIT_START_GAME_BTN(MainDimen.side_btn_image.getDimen() * 2f, MainDimen.side_btn_image.getDimen() * 2f),
    IMAGE_SPLIT_NAVIG_BTN(MainDimen.side_btn_image.getDimen() * 1.2f, MainDimen.side_btn_image.getDimen() * 1.2f),

    KIDLEARN_MAIN_MENU_LEVEL(MainDimen.side_btn_image.getDimen() * 4f, MainDimen.side_btn_image.getDimen() * 4f),
    KIDLEARN_CHOOSE_LEVEL(MainDimen.side_btn_image.getDimen() * 5f, MainDimen.side_btn_image.getDimen() * 5f),
    KIDLEARN_ENG_WORDS_LEVEL(MainDimen.side_btn_image.getDimen() * 3f, MainDimen.side_btn_image.getDimen() * 3f),
    KIDLEARN_SCI_LEVEL(MainDimen.side_btn_image.getDimen() * 6.5f, MainDimen.side_btn_image.getDimen() * 5f),
    KIDLEARN_MATH_CATER_LEVEL(MainDimen.side_btn_image.getDimen() * 7.5f, MainDimen.side_btn_image.getDimen() * 2f),
    KIDLEARN_RESET(MainDimen.side_btn_image.getDimen() * 2f, MainDimen.side_btn_image.getDimen() * 2f),
    KIDLEARN_VERIFY(MainDimen.side_btn_image.getDimen() * 8f, MainDimen.side_btn_image.getDimen() * 3f),
    KIDLEARN_DIFFICULTY(MainDimen.side_btn_image.getDimen() * 1.5f, MainDimen.side_btn_image.getDimen() * 1.5f),
    KIDLEARN_RESPONSE_ARROW(MainDimen.side_btn_image.getDimen() * 3f, MainDimen.side_btn_image.getDimen() / 1.4f),
    KIDLEARN_HANGMAN_BUTTON(MainDimen.side_btn_image.getDimen() * 1.5f, MainDimen.side_btn_image.getDimen() * 1.5f),

    IQTEST_HEADER_BUTTON(MainDimen.horizontal_general_margin.getDimen() * 14f, MainDimen.horizontal_general_margin.getDimen() * 6f),
    IQTEST_HEADER_IMG_BUTTON(MainDimen.side_btn_image.getDimen(), MainDimen.side_btn_image.getDimen()),
    IQTEST_NUM_SEQ_BUTTON(MainDimen.side_btn_image.getDimen(), MainDimen.side_btn_image.getDimen()),
    IQTEST_NUM_SEQ_SUBMIT_DELETE(MainDimen.side_btn_image.getDimen(), MainDimen.side_btn_image.getDimen()),
    IQTEST_LEVEL_BTN(MainDimen.side_btn_image.getDimen() * 1.1f, MainDimen.side_btn_image.getDimen() * 1.1f),

    FILLCOLOR_ZOOM_BUTTON(MainDimen.side_btn_image.getDimen() * 1f, MainDimen.side_btn_image.getDimen() * 1f),
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
