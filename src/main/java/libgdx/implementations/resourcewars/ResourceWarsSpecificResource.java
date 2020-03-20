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
    
    amount_buy("buttons/amount_buy.png", Texture.class),
    amount_sell("buttons/amount_sell.png", Texture.class),
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
    location("buttons/location.png", Texture.class),
    location_clicked("buttons/location_clicked.png", Texture.class),
    location_locked("buttons/location_locked.png", Texture.class),
    location_locked_clicked("buttons/location_locked_clicked.png", Texture.class),
    location_travelto("buttons/location_travelto.png", Texture.class),
    location_travelto_clicked("buttons/location_travelto_clicked.png", Texture.class),
    menu("buttons/menu.png", Texture.class),
    menu_clicked("buttons/menu_clicked.png", Texture.class),
    next_day("buttons/next_day.png", Texture.class),
    next_day_clicked("buttons/next_day_clicked.png", Texture.class),
    btn_disabled("buttons/btn_disabled.png", Texture.class),

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
