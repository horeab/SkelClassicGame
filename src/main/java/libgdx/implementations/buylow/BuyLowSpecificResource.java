package libgdx.implementations.buylow;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.I18NBundle;

import libgdx.game.Game;
import libgdx.resources.SpecificResource;
import sun.net.www.content.image.png;

public enum BuyLowSpecificResource implements SpecificResource {

    // @formatter:off

    specific_labels("labels/labels", I18NBundle.class),

    btn0("buttons/btn0.png", Texture.class),
    btn0_clicked("buttons/btn0_clicked.png", Texture.class),
    btn1("buttons/btn1.png", Texture.class),
    btn1_clicked("buttons/btn1_clicked.png", Texture.class),
    btn2("buttons/btn2.png", Texture.class),
    btn2_clicked("buttons/btn2_clicked.png", Texture.class),
    btn3("buttons/btn3.png", Texture.class),
    btn3_clicked("buttons/btn3_clicked.png", Texture.class),
    btn_disabled("buttons/btn_disabled.png", Texture.class),
    menu("buttons/menu.png", Texture.class),
    menu_clicked("buttons/menu_clicked.png", Texture.class),

    price_down("price_down.png", Texture.class),
    price_normal("price_normal.png", Texture.class),
    price_up("price_up.png", Texture.class),

    diamond("diamond.png", Texture.class),
    gold("gold.png", Texture.class),
    iron("iron.png", Texture.class),
    wood("wood.png", Texture.class),;

    // @formatter:on

    private String path;
    private Class<?> classType;

    BuyLowSpecificResource(String path, Class<?> classType) {
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
