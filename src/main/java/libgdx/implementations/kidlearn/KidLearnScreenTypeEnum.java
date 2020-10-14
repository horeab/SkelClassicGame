package libgdx.implementations.kidlearn;

import libgdx.implementations.kidlearn.screens.KidLearnChooseLevelScreen;
import libgdx.implementations.kidlearn.screens.KidLearnGameScreen;
import libgdx.implementations.kidlearn.screens.KidLearnLevelScreen;
import libgdx.implementations.kidlearn.screens.KidLearnMainMenuScreen;
import libgdx.implementations.kidlearn.spec.KidLearnGameContext;
import libgdx.implementations.kidlearn.spec.cater.KidLearnMathCaterConfig;
import libgdx.implementations.kidlearn.spec.cater.KidLearnMathCaterLevel;
import libgdx.screen.AbstractScreen;
import libgdx.screen.ScreenType;

public enum KidLearnScreenTypeEnum implements ScreenType {

    MAIN_SCREEN {
        public AbstractScreen getScreen(Object... params) {
            return new KidLearnMainMenuScreen();
        }
    },
    GAME_SCREEN {
        public AbstractScreen getScreen(Object... params) {
            return new KidLearnGameScreen((KidLearnGameContext) params[0]);
        }
    },
    LEVEL_SCREEN {
        public AbstractScreen getScreen(Object... params) {
            return new KidLearnLevelScreen((Class<? extends KidLearnMathCaterLevel>) params[0]);
        }
    },
    CHOOSE_LEVEL_SCREEN {
        public AbstractScreen getScreen(Object... params) {
            return new KidLearnChooseLevelScreen();
        }
    },

}