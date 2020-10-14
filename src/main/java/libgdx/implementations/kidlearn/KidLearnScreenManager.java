package libgdx.implementations.kidlearn;

import libgdx.implementations.kidlearn.spec.KidLearnGameContext;
import libgdx.implementations.kidlearn.spec.cater.KidLearnMathCaterLevel;
import libgdx.implementations.kidlearn.spec.cater.ord.KidLearnMathCaterOrdLevel;
import libgdx.implementations.kidlearn.spec.cater.seq.KidLearnMathCaterSeqLevel;
import libgdx.screen.AbstractScreenManager;

public class KidLearnScreenManager extends AbstractScreenManager {

    @Override
    public void showMainScreen() {
        showScreen(KidLearnScreenTypeEnum.MAIN_SCREEN);
        showLevelScreen(KidLearnMathCaterSeqLevel.class);
    }

    public void showGameScreen(KidLearnGameContext gameContext) {
        showScreen(KidLearnScreenTypeEnum.GAME_SCREEN, gameContext);
    }

    public void showChooseLevelScreen() {
        showScreen(KidLearnScreenTypeEnum.CHOOSE_LEVEL_SCREEN);
    }

    public void showLevelScreen(Class<? extends KidLearnMathCaterLevel> levelType) {
        showScreen(KidLearnScreenTypeEnum.LEVEL_SCREEN, levelType);
    }
}
