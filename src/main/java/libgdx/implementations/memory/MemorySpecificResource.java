package libgdx.implementations.memory;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.I18NBundle;
import libgdx.game.Game;
import libgdx.resources.SpecificResource;

import javax.xml.soap.Text;

public enum MemorySpecificResource implements SpecificResource {

    // @formatter:off

    specific_labels("labels/labels", I18NBundle.class),
    item0("items/item0.png", Texture.class),
    item1("items/item1.png", Texture.class),
    item2("items/item2.png", Texture.class),
    item3("items/item3.png", Texture.class),
    item4("items/item4.png", Texture.class),
    item5("items/item5.png", Texture.class),
    unknown("items/unknown.png", Texture.class),
    ;

    // @formatter:on

    private String path;
    private Class<?> classType;

    MemorySpecificResource(String path, Class<?> classType) {
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
