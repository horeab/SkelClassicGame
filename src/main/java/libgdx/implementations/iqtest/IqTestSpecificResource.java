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

    red_background("red_background.png", Texture.class),
    popup_background("popup_background.png", Texture.class),
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
