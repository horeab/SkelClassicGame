package libgdx.implementations.resourcewars;

import com.badlogic.gdx.utils.I18NBundle;
import libgdx.game.Game;
import libgdx.resources.SpecificResource;

public enum ResourceWarsSpecificResource implements SpecificResource {

    // @formatter:off

    specific_labels("labels/labels", I18NBundle.class),
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