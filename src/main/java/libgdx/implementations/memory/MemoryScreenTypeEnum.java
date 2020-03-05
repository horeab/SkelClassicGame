package libgdx.implementations.memory;

import libgdx.implementations.memory.screens.MemoryCampaignScreen;
import libgdx.implementations.memory.screens.MemoryGameScreen;
import libgdx.implementations.memory.spec.GameLevel;
import libgdx.screen.AbstractScreen;
import libgdx.screen.ScreenType;

public enum MemoryScreenTypeEnum implements ScreenType {

    CAMPAIGN_SCREEN {
        public AbstractScreen getScreen(Object... params) {
            return new MemoryCampaignScreen();
        }
    },

    GAME_SCREEN {
        public AbstractScreen getScreen(Object... params) {
            return new MemoryGameScreen((Integer) params[0]);
        }
    },

}