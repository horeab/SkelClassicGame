package libgdx.implementations.iqtest;

import libgdx.implementations.iqtest.screens.IqTestCorrectAnswersScreen;
import libgdx.implementations.iqtest.screens.IqTestGameOverScreen;
import libgdx.implementations.iqtest.screens.IqTestGameScreen;
import libgdx.implementations.iqtest.screens.IqTestMainMenuScreen;
import libgdx.screen.AbstractScreen;
import libgdx.screen.ScreenType;

import java.util.Map;

public enum IqTestScreenTypeEnum implements ScreenType {

    MAIN_MENU_SCREEN {
        public AbstractScreen getScreen(Object... params) {
            return new IqTestMainMenuScreen();
        }
    },
    GAME_SCREEN {
        public AbstractScreen getScreen(Object... params) {
            return new IqTestGameScreen();
        }
    },
    GAME_OVER_SCREEN {
        public AbstractScreen getScreen(Object... params) {
            return new IqTestGameOverScreen((Map<Integer, Integer>) params[0]);
        }
    },
    CORRECT_ANSWERS {
        public AbstractScreen getScreen(Object... params) {
            return new IqTestCorrectAnswersScreen((Map<Integer, Integer>) params[0]);
        }
    },

}