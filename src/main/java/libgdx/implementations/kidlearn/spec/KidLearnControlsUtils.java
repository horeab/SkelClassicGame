package libgdx.implementations.kidlearn.spec;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import org.apache.commons.lang3.StringUtils;

import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;

public class KidLearnControlsUtils {

    public static float getBtnLevelFontSize(String text) {
        return KidLearnUtils.getFontSize(text, 18,
                FontConfig.FONT_SIZE * 1.3f, 0.02f);
    }

    public static FontConfig getBtnLevelFontConfig(String text) {
        return getBtnLevelFontConfig(text, FontColor.WHITE.getColor());
    }

    public static FontConfig getBtnLevelFontConfig(String text, Color mainFontColor) {
        return new FontConfig(mainFontColor, FontColor.BLACK.getColor(),
                getBtnLevelFontSize(text), 3f);
    }

    public static FontConfig getTitleFontConfig(String text, int maxTextLength, FontColor borderColor, float borderWidth) {
        float fontSize = KidLearnUtils.getFontSize(text, maxTextLength, getTitleStandardFontSize(), 0.05f);
        return new FontConfig(FontColor.WHITE.getColor(), borderColor.getColor(),
                fontSize, borderWidth);
    }

    public static FontConfig getTitleFontConfig(String text, FontColor borderColor, float borderWidth) {
        return getTitleFontConfig(text, 20, borderColor, borderWidth);
    }

    protected static float getTitleStandardFontSize() {
        return FontConfig.FONT_SIZE * 2f;
    }

    public static FontConfig getTitleFontConfig(String text, FontColor borderColor) {
        return getTitleFontConfig(text, borderColor, getStandardTitleBorderWidth());
    }

    public static float getStandardTitleBorderWidth() {
        return 3f;
    }

    public static Table createGameTitle(String text, FontConfig fontConfig) {
        return new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setSingleLineLabel()
                .setFontConfig(fontConfig).setText(StringUtils.capitalize(text)).build());
    }

    public static Table createGameTitleAllWidth(String text, FontColor borderColor) {
        return createGameTitle(text, getTitleFontConfig(text,
                99, borderColor, KidLearnControlsUtils.getStandardTitleBorderWidth()));
    }

    public static Table createGameSubTitle(String text) {
        return new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setSingleLineLabel()
                .setFontConfig(getSubTitleFontConfig(FontColor.WHITE.getColor())).setText(StringUtils.capitalize(text)).build());
    }

    public static FontConfig getSubTitleFontConfig(Color mainColor) {
        return getSubTitleFontConfig(mainColor, getTitleStandardFontSize() / 1.8f);
    }

    public static FontConfig getSubTitleFontConfig(Color mainColor, float fontSize) {
        return new FontConfig(mainColor, FontColor.BLACK.getColor(),
                fontSize, 3f);
    }

    public static Table createGameTitle(String text, FontColor borderColor) {
        return createGameTitle(text, getTitleFontConfig(text, borderColor));
    }
}
