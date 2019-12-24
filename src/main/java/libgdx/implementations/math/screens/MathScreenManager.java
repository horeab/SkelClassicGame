package libgdx.implementations.math.screens;

import libgdx.screen.AbstractScreenManager;
import libgdx.screens.ScreenTypeEnum;

public class MathScreenManager extends AbstractScreenManager {

    @Override
    public void showMainScreen() {
        showScreen(MathScreenTypeEnum.CAMPAIGN_SCREEN);
    }

}
