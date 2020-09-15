package libgdx.implementations.skelgame;

import libgdx.resources.dimen.MainDimen;

public enum SkelGameButtonSize implements libgdx.controls.button.ButtonSize {

    FINAL_WORD_BUTTON(MainDimen.horizontal_general_margin.getDimen() * 5f, MainDimen.horizontal_general_margin.getDimen() * 5f),
    LETTER_BUTTON(MainDimen.horizontal_general_margin.getDimen() * 8f, MainDimen.horizontal_general_margin.getDimen() * 8f),
    SUBMIT_DELETE_BUTTON(LETTER_BUTTON.width * 1.3f, LETTER_BUTTON.height * 1.3f),
    BIG_MENU_ROUND_IMAGE(MainDimen.side_btn_image.getDimen() * 2.5f, MainDimen.side_btn_image.getDimen() * 2.5f),
    MAINMENU(LETTER_BUTTON.width * 6, LETTER_BUTTON.height*2),
    CAMPAIGN_LEVEL_ROUND_IMAGE(MainDimen.side_btn_image.getDimen() * 2f, MainDimen.side_btn_image.getDimen() * 2f),
    HINT_BUTTON(SUBMIT_DELETE_BUTTON.width * 1.3f, SUBMIT_DELETE_BUTTON.height * 1.3f),;

    private float width;
    private float height;

    SkelGameButtonSize(float width, float height) {
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
