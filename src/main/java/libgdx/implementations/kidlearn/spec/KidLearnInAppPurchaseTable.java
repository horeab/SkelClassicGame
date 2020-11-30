package libgdx.implementations.kidlearn.spec;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import libgdx.controls.labelimage.InAppPurchaseTable;
import libgdx.implementations.kidlearn.KidLearnSpecificResource;
import libgdx.resources.Res;

public class KidLearnInAppPurchaseTable extends InAppPurchaseTable {

    private float imageSideDimen;

    public KidLearnInAppPurchaseTable(float imageSideDimen) {
        this.imageSideDimen = imageSideDimen;
    }

    @Override
    protected float getUnlockImageSideDimen() {
        return imageSideDimen;
    }

    @Override
    protected Res getUnlockRes() {
        return KidLearnSpecificResource.locked_level;
    }

    @Override
    protected void setLockedTableBackground(Table lockBackgrTable, Image image) {
    }
}
