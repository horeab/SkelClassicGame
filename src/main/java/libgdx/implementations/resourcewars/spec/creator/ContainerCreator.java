package libgdx.implementations.resourcewars.spec.creator;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.graphics.GraphicUtils;
import libgdx.resources.MainResource;

public class ContainerCreator {

    public Table inventoryItem(float tableWidth) {
        Table table = new Table();
        MyWrappedLabel displayName = new MyWrappedLabel(new MyWrappedLabelConfigBuilder().setText("Item1").build());
        MyWrappedLabel actualSellPrice = new MyWrappedLabel(new MyWrappedLabelConfigBuilder().setText("43$").build());
        MyWrappedLabel pastBuyPrice = new MyWrappedLabel(new MyWrappedLabelConfigBuilder().setText("12$").build());
        MyWrappedLabel amount = new MyWrappedLabel(new MyWrappedLabelConfigBuilder().setText("1").build());
        table.add(displayName).width(tableWidth / 2);
        table.add(actualSellPrice).width(tableWidth / 2);
        table.row();
        table.add(pastBuyPrice).width(tableWidth / 2);
        table.add(amount).width(tableWidth / 2);
        table.setWidth(tableWidth);
        return table;
    }

    public Table marketItem(float tableWidth) {
        Table table = new Table();
        MyWrappedLabel displayName = new MyWrappedLabel(new MyWrappedLabelConfigBuilder().setText("Item111").build());
        MyWrappedLabel marketPrice = new MyWrappedLabel(new MyWrappedLabelConfigBuilder().setText("143$").build());
        table.add(displayName).width(tableWidth / 2);
        table.row();
        table.add(marketPrice).width(tableWidth / 2);
        return table;
    }
}
