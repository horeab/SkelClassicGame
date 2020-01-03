package libgdx.implementations.balloon;

import libgdx.implementations.balloon.screens.BalloonCampaignScreen;
import libgdx.implementations.balloon.screens.BalloonGameScreen;
import libgdx.implementations.balloon.screens.BalloonMainMenuScreen;
import libgdx.implementations.math.MathCampaignLevelEnum;
import libgdx.screen.AbstractScreen;
import libgdx.screen.ScreenType;

public enum BalloonScreenTypeEnum implements ScreenType {

    MAINMENU_SCREEN {
        public AbstractScreen getScreen(Object... params) {
            return new BalloonMainMenuScreen();
        }
    },

    CAMPAIGN_SCREEN {
        public AbstractScreen getScreen(Object... params) {
            return new BalloonCampaignScreen();
        }
    },

    GAME_SCREEN {
        public AbstractScreen getScreen(Object... params) {
            return new BalloonGameScreen((BalloonCampaignLevelEnum) params[0]);
        }
    },

}