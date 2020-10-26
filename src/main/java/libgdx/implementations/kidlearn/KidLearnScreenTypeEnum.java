package libgdx.implementations.kidlearn;

import libgdx.implementations.kidlearn.screens.KidLearnEngGameScreen;
import libgdx.implementations.kidlearn.screens.KidLearnEngLevelScreen;
import libgdx.implementations.kidlearn.screens.KidLearnMainMenuScreen;
import libgdx.implementations.kidlearn.screens.KidLearnMathGameScreen;
import libgdx.implementations.kidlearn.screens.KidLearnMathLevelScreen;
import libgdx.implementations.kidlearn.screens.KidLearnSciGameScreen;
import libgdx.implementations.kidlearn.screens.KidLearnSciLevelScreen;
import libgdx.implementations.kidlearn.spec.KidLearnGameContext;
import libgdx.implementations.kidlearn.spec.eng.KidLearnEngLevel;
import libgdx.implementations.kidlearn.spec.math.KidLearnMathCaterLevel;
import libgdx.implementations.kidlearn.spec.sci.KidLearnSciFeedLevel;
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
            } else if (KidLearnEngLevel.class.isAssignableFrom(param.level.getClass())) {
                return new KidLearnEngGameScreen(param);
            } else {
                return new KidLearnSciGameScreen(param);
            }
        }
    },
    LEVEL_SCREEN {
        public AbstractScreen getScreen(Object... params) {
            Object param = params[0];
            if (param instanceof Class && KidLearnMathCaterLevel.class.isAssignableFrom((Class<?>) param)) {
                return new KidLearnMathLevelScreen();
            } else if (param instanceof Class && KidLearnEngLevel.class.isAssignableFrom((Class<?>) param)) {
                return new KidLearnEngLevelScreen();
            } else {
                return new KidLearnSciLevelScreen();
            }
        }
    }

}