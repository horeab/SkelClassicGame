package libgdx.implementations.memory;

import libgdx.implementations.math.screens.MathCampaignScreen;
import libgdx.implementations.math.screens.MathGameScreen;
import libgdx.implementations.memory.screens.MemoryGameScreen;
import libgdx.implementations.memory.spec.CurrentGame;
import libgdx.screen.AbstractScreen;
import libgdx.screen.ScreenType;

public enum MemoryScreenTypeEnum implements ScreenType {

    CAMPAIGN_SCREEN {
        public AbstractScreen getScreen(Object... params) {
            return new MathCampaignScreen();
        }
    },

    GAME_SCREEN {
        public AbstractScreen getScreen(Object... params) {
            return new MemoryGameScreen();
        }
    },

}