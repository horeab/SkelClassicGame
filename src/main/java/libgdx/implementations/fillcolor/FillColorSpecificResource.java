package libgdx.implementations.fillcolor;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.I18NBundle;

import libgdx.game.Game;
import libgdx.resources.SpecificResource;

public enum FillColorSpecificResource implements SpecificResource {

    // @formatter:off

    specific_labels("labels/labels", I18NBundle.class),

    img0("img/img0.png", Texture.class),
    img0_fill("img/img0_fill.png", Texture.class),
    img1("img/img1.png", Texture.class),
    ;

    // @formatter:on

    private String path;
    private Class<?> classType;

    FillColorSpecificResource(String path, Class<?> classType) {
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
