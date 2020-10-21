package libgdx.implementations.kidlearn;

import libgdx.implementations.kidlearn.screens.KidLearnEngGameScreen;
import libgdx.implementations.kidlearn.screens.KidLearnEngWordsLevelScreen;
import libgdx.implementations.kidlearn.screens.KidLearnMainMenuScreen;
import libgdx.implementations.kidlearn.screens.KidLearnMathGameScreen;
import libgdx.implementations.kidlearn.screens.KidLearnMathCaterLevelScreen;
import libgdx.implementations.kidlearn.screens.KidLearnSciGameScreen;
import libgdx.implementations.kidlearn.screens.KidLearnSciLevelScreen;
import libgdx.implementations.kidlearn.spec.KidLearnGameContext;
import libgdx.implementations.kidlearn.spec.math.KidLearnMathCaterLevel;
import libgdx.implementations.kidlearn.spec.sci.KidLearnSciLevel;
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
            KidLearnGameContext param = (KidLearnGameContext) params[0];
            if (KidLearnMathCaterLevel.class.isAssignableFrom(param.level.getClass())) {
                return new KidLearnMathGameScreen(param);
            } else if (KidLearnSciLevel.class.isAssignableFrom(param.level.getClass())) {
                return new KidLearnSciGameScreen(param);
            } else {
                return new KidLearnEngGameScreen(param);
            }
        }
    },
    LEVEL_SCREEN {
        public AbstractScreen getScreen(Object... params) {
            Object param = params[0];
            if (param instanceof Class && KidLearnMathCaterLevel.class.isAssignableFrom((Class<?>) param)) {
                return new KidLearnMathCaterLevelScreen();
            } else if (param instanceof Class && KidLearnMathCaterLevel.class.isAssignableFrom((Class<?>) param)) {
                return new KidLearnSciLevelScreen();
            } else {
                return new KidLearnEngWordsLevelScreen();
            }
        }
    }

}