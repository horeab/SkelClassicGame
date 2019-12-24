package libgdx.implementations.math.screens;

import libgdx.implementations.math.MathLevel;
import libgdx.screen.AbstractScreen;
import libgdx.screen.ScreenType;

public enum MathScreenTypeEnum implements ScreenType {

    CAMPAIGN_SCREEN {
        public AbstractScreen getScreen(Object... params) {
            return new MathCampaignScreen();
        }
    },

    GAME_SCREEN {
        public AbstractScreen getScreen(Object... params) {
            return new MathGameScreen(MathLevel._1);
        }
    },

}