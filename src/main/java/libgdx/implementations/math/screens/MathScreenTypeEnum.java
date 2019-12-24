package libgdx.implementations.math.screens;

import libgdx.screen.AbstractScreen;
import libgdx.screen.ScreenType;

public enum MathScreenTypeEnum implements ScreenType {

    CAMPAIGN_SCREEN {
        public AbstractScreen getScreen(Object... params) {
            return new MathCampaignScreen();
        }
    },

}