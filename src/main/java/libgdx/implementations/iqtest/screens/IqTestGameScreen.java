package libgdx.implementations.iqtest.screens;

import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.game.Game;
import libgdx.implementations.SkelClassicButtonSkin;
import libgdx.implementations.iqtest.spec.*;
import libgdx.implementations.iqtest.spec.iqtest.IqTestGameCreator;
import libgdx.implementations.iqtest.spec.iqtest.IqTestQuestion;
import libgdx.implementations.iqtest.spec.numseq.IqNumSeqQuestion;
import libgdx.implementations.iqtest.spec.numseq.IqTestNumberSeqCreator;
import libgdx.screen.AbstractScreen;
import libgdx.skelgameimpl.skelgame.SkelGameRatingService;
import libgdx.utils.Utils;
import libgdx.utils.model.RGBColor;

import java.util.List;
import java.util.Map;

public class IqTestGameScreen extends AbstractScreen {

    private IqTestPreferencesManager iqTestPreferencesManager = new IqTestPreferencesManager();
    private IqTestGameType iqTestGameType;

    public IqTestGameScreen(IqTestGameType iqTestGameType) {
        this.iqTestGameType = iqTestGameType;
    }

    @Override
    public void buildStage() {
        if (Game.getInstance().isFirstTimeMainMenuDisplayed()) {
            new SkelGameRatingService(this).appLaunched();
        }

        IqTestLevelCreator creator;
        if (iqTestGameType == IqTestGameType.IQ_TEST) {
            IqTestCurrentGame currentGame = createCurrentGame(IqTestGameType.IQ_TEST, IqTestQuestion.Q0.getEnumAllValues());
            creator = new IqTestGameCreator(currentGame);
            setBackgroundColor(RGBColor.WHITE);

        } else {
            IqTestCurrentGame currentGame = createCurrentGame(IqTestGameType.NUM_SEQ, IqNumSeqQuestion.Q0.getEnumAllValues());
            creator = new IqTestNumberSeqCreator(currentGame, this);
        }

        creator.refreshLevel();
        new BackButtonBuilder()
                .setButtonSkin(SkelClassicButtonSkin.IQTEST_BACK_BTN)
                .addHoverBackButton(this, BackButtonBuilder.getX(), BackButtonBuilder.getY() * 1.01f);
    }

    private IqTestCurrentGame createCurrentGame(IqTestGameType iqTestGameType, List<IqGameQuestion> allQ) {
        IqTestCurrentGame currentGame = new IqTestCurrentGame(allQ);
        Map<Integer, Integer> state = iqTestPreferencesManager.getCurrentQAState(iqTestGameType);
        if (state != null) {
            currentGame.setQuestionWithAnswer(state);
        }
        return currentGame;
    }

    @Override
    public void onBackKeyPress() {
        screenManager.showMainScreen();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        Utils.createChangeLangPopup();
    }
}
