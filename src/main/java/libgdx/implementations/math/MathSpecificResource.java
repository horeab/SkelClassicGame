package libgdx.implementations.math;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.I18NBundle;

import libgdx.game.Game;
import libgdx.resources.SpecificResource;

public enum MathSpecificResource implements SpecificResource {

    // @formatter:off

    specific_labels("labels/labels", I18NBundle.class),
    ok("ok.png", Texture.class),
    wrong("wrong.png", Texture.class),
    ok_pressed("ok_pressed.png", Texture.class),
    wrong_pressed("wrong_pressed.png", Texture.class),
    btn_level_up("btn_level_up.png", Texture.class),
    btn_level_down("btn_level_down.png", Texture.class),
    background_texture("background_texture.png", Texture.class),
    calculator("calculator.png", Texture.class);

    // @formatter:on

    private String path;
    private Class<?> classType;

    MathSpecificResource(String path, Class<?> classType) {
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
