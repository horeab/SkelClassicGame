package libgdx.implementations.kidlearn;

import libgdx.implementations.kidlearn.spec.KidLearnGameContext;
import libgdx.implementations.kidlearn.spec.eng.KidLearnEngHangmanLevel;
import libgdx.implementations.kidlearn.spec.eng.KidLearnEngWordsLevel;
import libgdx.implementations.kidlearn.spec.math.KidLearnMathCaterOrdLevel;
import libgdx.implementations.kidlearn.spec.sci.KidLearnSciLevel;
import libgdx.screen.AbstractScreenManager;

public class KidLearnScreenManager extends AbstractScreenManager {

    @Override
    public void showMainScreen() {
        showScreen(KidLearnScreenTypeEnum.MAIN_SCREEN);
//        showLevelScreen(KidLearnMathCaterSeqLevel.class);
        showLevelScreen(KidLearnSciLevel.class);
        showGameScreen(new KidLearnGameContext(KidLearnSciLevel.L0));
        showGameScreen(new KidLearnGameContext(KidLearnMathCaterOrdLevel.L0));
        showGameScreen(new KidLearnGameContext(KidLearnEngHangmanLevel.L0));
        showLevelScreen(KidLearnEngWordsLevel.class);
        showScreen(KidLearnScreenTypeEnum.MAIN_SCREEN);
    }

    public void showGameScreen(KidLearnGameContext gameContext) {
        showScreen(KidLearnScreenTypeEnum.GAME_SCREEN, gameContext);
    }

    public void showLevelScreen(Class<?> levelType) {
        showScreen(KidLearnScreenTypeEnum.LEVEL_SCREEN, levelType);
    }
}
