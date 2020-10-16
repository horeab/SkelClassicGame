package libgdx.implementations.kidlearn;

import libgdx.implementations.kidlearn.spec.KidLearnGameContext;
import libgdx.implementations.kidlearn.spec.cater.KidLearnMathCaterOrdLevel;
import libgdx.implementations.kidlearn.spec.cater.KidLearnMathCaterSeqLevel;
import libgdx.implementations.kidlearn.spec.eng.KidLearnEngWordsLevel;
import libgdx.screen.AbstractScreenManager;

public class KidLearnScreenManager extends AbstractScreenManager {

    @Override
    public void showMainScreen() {
        showScreen(KidLearnScreenTypeEnum.MAIN_SCREEN);
        showLevelScreen(KidLearnEngWordsLevel.class);
//        showLevelScreen(KidLearnMathCaterSeqLevel.class);
    }

    public void showGameScreen(KidLearnGameContext gameContext) {
        showScreen(KidLearnScreenTypeEnum.GAME_SCREEN, gameContext);
    }

    public void showChooseLevelScreen() {
        showScreen(KidLearnScreenTypeEnum.CHOOSE_LEVEL_SCREEN);
    }

    public void showLevelScreen(Class<? extends Enum> levelType) {
        showScreen(KidLearnScreenTypeEnum.LEVEL_SCREEN, levelType);
    }
}
