package libgdx.implementations.applepie;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.I18NBundle;

import libgdx.game.Game;
import libgdx.resources.SpecificResource;

public enum ApplePieSpecificResource implements SpecificResource {

    // @formatter:off

    specific_labels("labels/labels", I18NBundle.class),

    amount_plus("buttons/amount_plus.png", Texture.class),
    amount_plus_clicked("buttons/amount_plus_clicked.png", Texture.class),
    amount_plus_disabled("buttons/amount_plus_disabled.png", Texture.class),
    ;

    // @formatter:on

    private String path;
    private Class<?> classType;

    ApplePieSpecificResource(String path, Class<?> classType) {
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
