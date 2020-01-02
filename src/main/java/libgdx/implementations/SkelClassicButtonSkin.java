package libgdx.implementations;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import libgdx.controls.button.ButtonSkin;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.balloon.BalloonSpecificResource;
import libgdx.implementations.math.MathSpecificResource;
import libgdx.resources.Res;
import libgdx.utils.model.FontColor;

public enum SkelClassicButtonSkin implements ButtonSkin {

    //BALLOON
    BALLOON_MENU(BalloonSpecificResource.greenbtn_normal, BalloonSpecificResource.greenbtn_clicked, BalloonSpecificResource.greenbtn_clicked, BalloonSpecificResource.greenbtn_locked, null),
    BALLOON_STAGE1(BalloonSpecificResource.greenbtn_normal, BalloonSpecificResource.greenbtn_clicked, BalloonSpecificResource.greenbtn_clicked, BalloonSpecificResource.greenbtn_locked, null),
    BALLOON_STAGE2(BalloonSpecificResource.bluebtn_normal, BalloonSpecificResource.bluebtn_clicked, BalloonSpecificResource.bluebtn_clicked, BalloonSpecificResource.bluebtn_locked, null),
    BALLOON_STAGE3(BalloonSpecificResource.redbtn_normal, BalloonSpecificResource.redbtn_clicked, BalloonSpecificResource.redbtn_clicked, BalloonSpecificResource.redbtn_locked, null),

    //MATH
    MATH_LEVELBTN(MathSpecificResource.btn_level_up, MathSpecificResource.btn_level_down, MathSpecificResource.btn_level_down, MathSpecificResource.btn_level_up, null),
    MATH_CORRECT(MathSpecificResource.ok, MathSpecificResource.ok_pressed, MathSpecificResource.ok_pressed, MathSpecificResource.ok, null),
    MATH_WRONG(MathSpecificResource.wrong, MathSpecificResource.wrong_pressed, MathSpecificResource.wrong_pressed, MathSpecificResource.wrong, null),;

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
