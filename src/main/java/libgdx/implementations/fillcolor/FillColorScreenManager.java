package libgdx.implementations.fillcolor;

import libgdx.screen.AbstractScreenManager;

public class FillColorScreenManager extends AbstractScreenManager {

    @Override
    public void showMainScreen() {
        showScreen(FillColorScreenTypeEnum.MAIN_SCREEN);
    }

}