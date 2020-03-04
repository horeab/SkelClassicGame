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
    item6("items/item6.png", Texture.class),
    item7("items/item7.png", Texture.class),
    item8("items/item8.png", Texture.class),
    item9("items/item9.png", Texture.class),
    item10("items/item10.png", Texture.class),
    item11("items/item11.png", Texture.class),
    item12("items/item12.png", Texture.class),
    item13("items/item13.png", Texture.class),
    item14("items/item14.png", Texture.class),
    item15("items/item15.png", Texture.class),
    item16("items/item16.png", Texture.class),
    item17("items/item17.png", Texture.class),
    item18("items/item18.png", Texture.class),
    item19("items/item19.png", Texture.class),
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
