package libgdx.implementations.resourcewars;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.I18NBundle;
import libgdx.game.Game;
import libgdx.resources.SpecificResource;

public enum ResourceWarsSpecificResource implements SpecificResource {

    // @formatter:off

    specific_labels("labels/labels", I18NBundle.class),
    downarrow("uparrow.png", Texture.class),
    downdownarrow("upuparrow.png", Texture.class),
    uparrow("downarrow.png", Texture.class),
    upuparrow("downdownarrow.png", Texture.class),
    disableddownarrow("disableduparrow.png", Texture.class),
    disableddowndownarrow("disabledupuparrow.png", Texture.class),
    disableduparrow("disableddownarrow.png", Texture.class),
    disabledupuparrow("disableddowndownarrow.png", Texture.class),
    ;

    // @formatter:on

    private String path;
    private Class<?> classType;

    ResourceWarsSpecificResource(String path, Class<?> classType) {
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
