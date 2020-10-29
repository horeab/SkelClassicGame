package libgdx.implementations.kidlearn.spec.sci;

import libgdx.implementations.kidlearn.spec.KidLearnGameContext;
import libgdx.implementations.kidlearn.spec.KidLearnVerticalGameCreator;
import libgdx.utils.ScreenDimensionsManager;

public class KidLearnSciBodyGameCreator extends KidLearnVerticalGameCreator {

    KidLearnArrowsConfig config;

    public KidLearnSciBodyGameCreator(KidLearnGameContext gameContext, KidLearnArrowsConfig config) {
        super(gameContext, config);
        this.config = config;
    }

    @Override
    protected float getOptionWidth() {
        return ScreenDimensionsManager.getScreenWidthValue(15);
    }
}
