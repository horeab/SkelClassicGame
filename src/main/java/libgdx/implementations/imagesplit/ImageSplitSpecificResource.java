package libgdx.implementations.imagesplit;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.I18NBundle;

import libgdx.game.Game;
import libgdx.resources.SpecificResource;

public enum ImageSplitSpecificResource implements SpecificResource {

    // @formatter:off

    specific_labels("labels/labels", I18NBundle.class),

    btn0("buttons/btn0.png", Texture.class),
    btn0_clicked("buttons/btn0_clicked.png", Texture.class),
    btn_disabled("buttons/btn_disabled.png", Texture.class),
    menu("buttons/menu.png", Texture.class),
    menu_clicked("buttons/menu_clicked.png", Texture.class),
    ;

    // @formatter:on

    private String path;
    private Class<?> classType;

    ImageSplitSpecificResource(String path, Class<?> classType) {
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
