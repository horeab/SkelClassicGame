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
    greenbtn_clicked("buttons/greenbtn_clicked.png", Texture.class),
    greenbtn_normal("buttons/greenbtn_normal.png", Texture.class),
    
    amount_buysell("buttons/amount_buysell.png", Texture.class),
    amount_buysell_clicked("buttons/amount_buysell_clicked.png", Texture.class),
    amount_buysell_disabled("buttons/amount_buysell_disabled.png", Texture.class),
    amount_delete("buttons/amount_delete.png", Texture.class),
    amount_delete_clicked("buttons/amount_delete_clicked.png", Texture.class),
    amount_delete_disabled("buttons/amount_delete_disabled.png", Texture.class),
    amount_plus("buttons/amount_plus.png", Texture.class),
    amount_plus_clicked("buttons/amount_plus_clicked.png", Texture.class),
    amount_plus_disabled("buttons/amount_plus_disabled.png", Texture.class),
    amount_minus("buttons/amount_minus.png", Texture.class),
    amount_minus_clicked("buttons/amount_minus_clicked.png", Texture.class),
    amount_minus_disabled("buttons/amount_minus_disabled.png", Texture.class),

    invmarket_itemnotselected("invmarket_itemnotselected.png", Texture.class),
    invmarket_itemselected("invmarket_itemselected.png", Texture.class),
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
