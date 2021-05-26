package libgdx.implementations.iqtest;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.I18NBundle;
import libgdx.game.Game;
import libgdx.resources.SpecificResource;

public enum IqTestSpecificResource implements SpecificResource {

    // @formatter:off

    num_seq_btn_up("buttons/num_seq_btn_up.png", Texture.class),
    num_seq_btn_pressed("buttons/num_seq_btn_pressed.png", Texture.class),
    delete_btn_up("buttons/delete_btn_up.png", Texture.class),
    delete_btn_down("buttons/delete_btn_down.png", Texture.class),
    submit_btn_up("buttons/submit_btn_up.png", Texture.class),
    submit_btn_down("buttons/submit_btn_down.png", Texture.class),
    skip_btn_down("buttons/skip_btn_down.png", Texture.class),
    skip_btn_up("buttons/skip_btn_up.png", Texture.class),
    new_game_btn_down("buttons/new_game_btn_down.png", Texture.class),
    new_game_btn_up("buttons/new_game_btn_up.png", Texture.class),
    next_btn_down("buttons/next_btn_down.png", Texture.class),
    next_btn_up("buttons/next_btn_up.png", Texture.class),
    btn_back_up("buttons/btn_back_up.png", Texture.class),
    btn_back_down("buttons/btn_back_down.png", Texture.class),
    btn_level_main_0_down("buttons/btn_level_main_0_down.png", Texture.class),
    btn_level_main_0_up("buttons/btn_level_main_0_up.png", Texture.class),
    btn_level_main_1_down("buttons/btn_level_main_1_down.png", Texture.class),
    btn_level_main_1_up("buttons/btn_level_main_1_up.png", Texture.class),
    btn_level_main_2_down("buttons/btn_level_main_2_down.png", Texture.class),
    btn_level_main_2_up("buttons/btn_level_main_2_up.png", Texture.class),
    btn_level_main_3_down("buttons/btn_level_main_3_down.png", Texture.class),
    btn_level_main_3_up("buttons/btn_level_main_3_up.png", Texture.class),

    question_mark_orange("question_mark_orange.png", Texture.class),
    question_mark_blue("question_mark_blue.png", Texture.class),
    red_background("red_background.png", Texture.class),
    finalscore("questions/iq/finalscore.png", Texture.class),
    background_texture("background_texture.png", Texture.class),
    specific_labels("labels/labels", I18NBundle.class),
    ;
    // @formatter:on

    private String path;
    private Class<?> classType;

    IqTestSpecificResource(String path, Class<?> classType) {
        this.path = path;
        this.classType = classType;
    }

    @Override
    public Class<?> getClassType() {
        return classType;
    }

    @Override
    public String getPath() {
        return Game.getInstance().getAppInfoService().getImplementationGameResourcesFolder() + path;
    }

}
