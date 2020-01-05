package libgdx.implementations.balloon;

import libgdx.implementations.balloon.model.LevelInfo;
import libgdx.implementations.balloon.screens.BalloonCampaignScreen;
import libgdx.implementations.balloon.screens.BalloonGameScreen;
import libgdx.implementations.balloon.screens.BalloonLevelFinishedScreen;
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
            return new BalloonCampaignScreen((int) params[0]);
        }
    },

    LEVELFINISHED_SCREEN {
        public AbstractScreen getScreen(Object... params) {
            return new BalloonLevelFinishedScreen((LevelInfo) params[0], (int) params[1], (int) params[2]);
        }
    },

    GAME_SCREEN {
        public AbstractScreen getScreen(Object... params) {
            return new BalloonGameScreen((LevelInfo) params[0]);
        }
    },

}