package libgdx.implementations;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import libgdx.controls.button.ButtonSkin;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.applepie.ApplePieSpecificResource;
import libgdx.implementations.balloon.BalloonSpecificResource;
import libgdx.implementations.buylow.BuyLowSpecificResource;
import libgdx.implementations.imagesplit.ImageSplitSpecificResource;
import libgdx.implementations.kidlearn.KidLearnSpecificResource;
import libgdx.implementations.math.MathSpecificResource;
import libgdx.implementations.resourcewars.ResourceWarsSpecificResource;
import libgdx.resources.MainResource;
import libgdx.resources.Res;
import libgdx.utils.model.FontColor;

public enum SkelClassicButtonSkin implements ButtonSkin {

    //RESOURCEWARS
    RESOURCEWARS_AMOUNT_BUY(ResourceWarsSpecificResource.amount_buy, ResourceWarsSpecificResource.amount_buysell_clicked, ResourceWarsSpecificResource.amount_buy, ResourceWarsSpecificResource.amount_buysell_disabled, null),
    RESOURCEWARS_AMOUNT_SELL(ResourceWarsSpecificResource.amount_sell, ResourceWarsSpecificResource.amount_buysell_clicked, ResourceWarsSpecificResource.amount_sell, ResourceWarsSpecificResource.amount_buysell_disabled, null),
    RESOURCEWARS_AMOUNT_DELETE(ResourceWarsSpecificResource.amount_delete, ResourceWarsSpecificResource.amount_delete_clicked, ResourceWarsSpecificResource.amount_delete, ResourceWarsSpecificResource.amount_delete_disabled, null),
    RESOURCEWARS_AMOUNT_PLUS(ResourceWarsSpecificResource.amount_plus, ResourceWarsSpecificResource.amount_plus_clicked, ResourceWarsSpecificResource.amount_plus, ResourceWarsSpecificResource.amount_plus_disabled, null),
    RESOURCEWARS_AMOUNT_MINUS(ResourceWarsSpecificResource.amount_minus, ResourceWarsSpecificResource.amount_minus_clicked, ResourceWarsSpecificResource.amount_minus, ResourceWarsSpecificResource.amount_minus_disabled, null),
    RESOURCEWARS_LOCATION(ResourceWarsSpecificResource.location, ResourceWarsSpecificResource.location_clicked, ResourceWarsSpecificResource.location, ResourceWarsSpecificResource.location, null),
    RESOURCEWARS_LOCATION_LOCKED(ResourceWarsSpecificResource.location_locked, ResourceWarsSpecificResource.location_locked_clicked, ResourceWarsSpecificResource.location_locked, ResourceWarsSpecificResource.btn_disabled, null),
    RESOURCEWARS_LOCATION_TRAVELTO(ResourceWarsSpecificResource.location_travelto, ResourceWarsSpecificResource.location_travelto_clicked, ResourceWarsSpecificResource.location_travelto, ResourceWarsSpecificResource.btn_disabled, null),
    RESOURCEWARS_LOCATION_NEXTDAY(ResourceWarsSpecificResource.next_day, ResourceWarsSpecificResource.next_day_clicked, ResourceWarsSpecificResource.next_day, ResourceWarsSpecificResource.btn_disabled, null),
    RESOURCEWARS_MENU(ResourceWarsSpecificResource.menu, ResourceWarsSpecificResource.menu_clicked, ResourceWarsSpecificResource.menu, ResourceWarsSpecificResource.btn_disabled, null),

    //BALLOON
    BALLOON_MENU(BalloonSpecificResource.greenbtn_normal, BalloonSpecificResource.greenbtn_clicked, BalloonSpecificResource.greenbtn_clicked, BalloonSpecificResource.greenbtn_locked, null),
    BALLOON_STAGE0(BalloonSpecificResource.greenbtn_normal, BalloonSpecificResource.greenbtn_clicked, BalloonSpecificResource.greenbtn_clicked, BalloonSpecificResource.greenbtn_locked, null),
    BALLOON_STAGE1(BalloonSpecificResource.bluebtn_normal, BalloonSpecificResource.bluebtn_clicked, BalloonSpecificResource.bluebtn_clicked, BalloonSpecificResource.bluebtn_locked, null),
    BALLOON_STAGE2(BalloonSpecificResource.redbtn_normal, BalloonSpecificResource.redbtn_clicked, BalloonSpecificResource.redbtn_clicked, BalloonSpecificResource.redbtn_locked, null),

    //MATH
    MATH_LEVELBTN(MathSpecificResource.btn_level_up, MathSpecificResource.btn_level_down, MathSpecificResource.btn_level_down, MathSpecificResource.btn_level_up, null),
    MATH_CORRECT(MathSpecificResource.ok, MathSpecificResource.ok_pressed, MathSpecificResource.ok_pressed, MathSpecificResource.ok, null),
    MATH_WRONG(MathSpecificResource.wrong, MathSpecificResource.wrong_pressed, MathSpecificResource.wrong_pressed, MathSpecificResource.wrong, null),

    //APPLEPIE
    APPLEPIE_PRESS_BTN(ApplePieSpecificResource.amount_plus, ApplePieSpecificResource.amount_plus_clicked, ApplePieSpecificResource.amount_plus, ApplePieSpecificResource.amount_plus_disabled, null),

    //BUYLOW
    BUYLOW_MENU(ResourceWarsSpecificResource.menu, ResourceWarsSpecificResource.menu_clicked, ResourceWarsSpecificResource.menu, ResourceWarsSpecificResource.btn_disabled, null),
    BUYLOW_SELLBUY0_BTN(BuyLowSpecificResource.btn0, BuyLowSpecificResource.btn0_clicked, BuyLowSpecificResource.btn0, BuyLowSpecificResource.btn_disabled, null),
    BUYLOW_SELLBUY1_BTN(BuyLowSpecificResource.btn0, BuyLowSpecificResource.btn0_clicked, BuyLowSpecificResource.btn0, BuyLowSpecificResource.btn_disabled, null),
    BUYLOW_SELLBUY2_BTN(BuyLowSpecificResource.btn0, BuyLowSpecificResource.btn0_clicked, BuyLowSpecificResource.btn0, BuyLowSpecificResource.btn_disabled, null),
    BUYLOW_SELLBUY3_BTN(BuyLowSpecificResource.btn0, BuyLowSpecificResource.btn0_clicked, BuyLowSpecificResource.btn0, BuyLowSpecificResource.btn_disabled, null),

    IMAGE_SPLIT_GAME_TYPE_SLIDE(ImageSplitSpecificResource.gametype_slide, ImageSplitSpecificResource.gametype_slide_clicked, ImageSplitSpecificResource.gametype_slide, ImageSplitSpecificResource.gametype_slide_disabled, null),
    IMAGE_SPLIT_GAME_TYPE_PUSH(ImageSplitSpecificResource.gametype_push, ImageSplitSpecificResource.gametype_push_clicked, ImageSplitSpecificResource.gametype_push, ImageSplitSpecificResource.gametype_push_disabled, null),
    IMAGE_SPLIT_GAME_TYPE_SWAP(ImageSplitSpecificResource.gametype_swap, ImageSplitSpecificResource.gametype_swap_clicked, ImageSplitSpecificResource.gametype_swap, ImageSplitSpecificResource.gametype_swap_disabled, null),
    IMAGE_SPLIT_NAVIG_LEFT(ImageSplitSpecificResource.leftarrow, ImageSplitSpecificResource.leftarrow, ImageSplitSpecificResource.leftarrow, ImageSplitSpecificResource.leftarrow, null),
    IMAGE_SPLIT_NAVIG_RIGHT(ImageSplitSpecificResource.rightarrow, ImageSplitSpecificResource.rightarrow, ImageSplitSpecificResource.rightarrow, ImageSplitSpecificResource.rightarrow, null),
    IMAGE_SPLIT_VIEW_IMG(ImageSplitSpecificResource.viewimg, ImageSplitSpecificResource.viewimg, ImageSplitSpecificResource.viewimg, ImageSplitSpecificResource.viewimg, null),

    KIDLEARN_MAIN_MENU_LEVEL(KidLearnSpecificResource.diff_btn_1_up, KidLearnSpecificResource.diff_btn_1_up, KidLearnSpecificResource.diff_btn_1_up, KidLearnSpecificResource.diff_btn_1_up, null),
    KIDLEARN_DIFF_LEVEL_0(KidLearnSpecificResource.diff_btn_0_up, KidLearnSpecificResource.diff_btn_0_down, KidLearnSpecificResource.diff_btn_0_up, KidLearnSpecificResource.diff_btn_0_up, null),
    KIDLEARN_DIFF_LEVEL_1(KidLearnSpecificResource.diff_btn_1_up, KidLearnSpecificResource.diff_btn_1_down, KidLearnSpecificResource.diff_btn_1_up, KidLearnSpecificResource.diff_btn_1_up, null),
    KIDLEARN_DIFF_LEVEL_2(KidLearnSpecificResource.diff_btn_2_up, KidLearnSpecificResource.diff_btn_2_down, KidLearnSpecificResource.diff_btn_2_up, KidLearnSpecificResource.diff_btn_2_up, null),
    KIDLEARN_ENGWORDS_0(KidLearnSpecificResource.diff_btn_2_up, KidLearnSpecificResource.diff_btn_2_down, KidLearnSpecificResource.diff_btn_2_up, KidLearnSpecificResource.diff_btn_2_up, null),
    KIDLEARN_ENGWORDS_1(KidLearnSpecificResource.diff_btn_2_up, KidLearnSpecificResource.diff_btn_2_down, KidLearnSpecificResource.diff_btn_2_up, KidLearnSpecificResource.diff_btn_2_up, null),
    KIDLEARN_ENGWORDS_2(KidLearnSpecificResource.diff_btn_2_up, KidLearnSpecificResource.diff_btn_2_down, KidLearnSpecificResource.diff_btn_2_up, KidLearnSpecificResource.diff_btn_2_up, null),
    KIDLEARN_ENGWORDS_3(KidLearnSpecificResource.diff_btn_2_up, KidLearnSpecificResource.diff_btn_2_down, KidLearnSpecificResource.diff_btn_2_up, KidLearnSpecificResource.diff_btn_2_up, null),
    KIDLEARN_ENGWORDS_FIN_0(KidLearnSpecificResource.diff_btn_1_up, KidLearnSpecificResource.diff_btn_2_down, KidLearnSpecificResource.diff_btn_2_up, KidLearnSpecificResource.diff_btn_2_up, null),
    KIDLEARN_ENGWORDS_FIN_1(KidLearnSpecificResource.diff_btn_1_up, KidLearnSpecificResource.diff_btn_2_down, KidLearnSpecificResource.diff_btn_2_up, KidLearnSpecificResource.diff_btn_2_up, null),
    KIDLEARN_ENGWORDS_FIN_2(KidLearnSpecificResource.diff_btn_1_up, KidLearnSpecificResource.diff_btn_2_down, KidLearnSpecificResource.diff_btn_2_up, KidLearnSpecificResource.diff_btn_2_up, null),
    KIDLEARN_ENGWORDS_FIN_3(KidLearnSpecificResource.diff_btn_1_up, KidLearnSpecificResource.diff_btn_2_down, KidLearnSpecificResource.diff_btn_2_up, KidLearnSpecificResource.diff_btn_2_up, null),
    KIDLEARN_SCI_LEVEL(KidLearnSpecificResource.btn_sci_level_up, KidLearnSpecificResource.btn_sci_level_down, KidLearnSpecificResource.btn_sci_level_up, KidLearnSpecificResource.btn_sci_level_up, null),
    KIDLEARN_HANGMAN_LETTER(KidLearnSpecificResource.btn_hangman_up, KidLearnSpecificResource.btn_hangman_down, KidLearnSpecificResource.btn_hangman_up, MainResource.btn_lowcolor_down, null),
    ;

    private Res imgUp;
    private Res imgDown;
    private Res imgChecked;
    private Res imgDisabled;
    private FontColor buttonDisabledFontColor;

    SkelClassicButtonSkin(Res imgUp, Res imgDown, Res imgChecked, Res imgDisabled, FontColor buttonDisabledFontColor) {
        this.imgUp = imgUp;
        this.imgDown = imgDown;
        this.imgChecked = imgChecked;
        this.imgDisabled = imgDisabled;
        this.buttonDisabledFontColor = buttonDisabledFontColor;
    }

    @Override
    public Drawable getImgUp() {
        return GraphicUtils.getImage(imgUp).getDrawable();
    }

    @Override
    public Drawable getImgDown() {
        return GraphicUtils.getImage(imgDown).getDrawable();
    }

    @Override
    public Drawable getImgChecked() {
        return GraphicUtils.getImage(imgChecked).getDrawable();
    }

    @Override
    public Drawable getImgDisabled() {
        return GraphicUtils.getImage(imgDisabled).getDrawable();
    }

    @Override
    public FontColor getButtonDisabledFontColor() {
        return buttonDisabledFontColor;
    }
}
