package libgdx.resources;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

import com.badlogic.gdx.utils.I18NBundle;
import libgdx.game.Game;

public enum Resource implements Res {

    // @formatter:off

    title_background("game/title_background.png", Texture.class),
    title_rays("game/title_rays.png", Texture.class),

    //CAMPAIGN///////////////////////////////////////////////////////////////////////////////////////
    stars_table_background("game/campaign/stars_table_background.png", Texture.class),
    star_disabled("game/campaign/star_disabled.png", Texture.class),
    star_enabled("game/campaign/star_enabled.png", Texture.class),
    btn_campaign_disabled("game/campaign/buttons/btn_campaign_disabled.png", Texture.class),
    btn_current_level_down("game/campaign/buttons/btn_current_level_down.png", Texture.class),
    btn_current_level_up("game/campaign/buttons/btn_current_level_up.png", Texture.class),
    question_background("game/question_background.png", Texture.class),

    labels_cs("labels/labels_cs", I18NBundle.class),
    labels_da("labels/labels_da", I18NBundle.class),
    labels_de("labels/labels_de", I18NBundle.class),
    labels_el("labels/labels_el", I18NBundle.class),
    labels_en("labels/labels_en", I18NBundle.class),
    labels_es("labels/labels_es", I18NBundle.class),
    labels_fi("labels/labels_fi", I18NBundle.class),
    labels_fr("labels/labels_fr", I18NBundle.class),
    labels_hi("labels/labels_hi", I18NBundle.class),
    labels_hr("labels/labels_hr", I18NBundle.class),
    labels_hu("labels/labels_hu", I18NBundle.class),
    labels_id("labels/labels_id", I18NBundle.class),
    labels_it("labels/labels_it", I18NBundle.class),
    labels_ja("labels/labels_ja", I18NBundle.class),
    labels_ko("labels/labels_ko", I18NBundle.class),
    labels_ms("labels/labels_ms", I18NBundle.class),
    labels_nl("labels/labels_nl", I18NBundle.class),
    labels_no("labels/labels_no", I18NBundle.class),
    labels_pl("labels/labels_pl", I18NBundle.class),
    labels_pt("labels/labels_pt", I18NBundle.class),
    labels_ro("labels/labels_ro", I18NBundle.class),
    labels_ru("labels/labels_ru", I18NBundle.class),
    labels_sk("labels/labels_sk", I18NBundle.class),
    labels_sv("labels/labels_sv", I18NBundle.class),
    labels_th("labels/labels_th", I18NBundle.class),
    labels_tr("labels/labels_tr", I18NBundle.class),
    labels_uk("labels/labels_uk", I18NBundle.class),
    labels_vi("labels/labels_vi", I18NBundle.class),
    labels_zh("labels/labels_zh", I18NBundle.class),
    sound_fail_game_over("sounds/sound_fail_game_over.mp3", Sound.class),
    sound_success_game_over("sounds/sound_success_game_over.mp3", Sound.class),;
    // @formatter:on

    private String path;
    private Class<?> classType;

    Resource(String path, Class<?> classType) {
        this.path = path;
        this.classType = classType;
    }

    @Override
    public String getPath() {
        return Game.getInstance().getAppInfoService().getResourcesFolder() + path;
    }

    public String getRawPath() {
        return path;
    }

    @Override
    public Class<?> getClassType() {
        return classType;
    }


}
