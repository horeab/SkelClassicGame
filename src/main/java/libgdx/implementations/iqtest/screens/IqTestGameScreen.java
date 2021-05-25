package libgdx.implementations.iqtest.screens;

import com.badlogic.gdx.Gdx;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.game.Game;
import libgdx.implementations.SkelClassicButtonSkin;
import libgdx.implementations.iqtest.spec.numseq.IqNumSeqQuestion;
import libgdx.implementations.iqtest.spec.IqTestCurrentGame;
import libgdx.implementations.iqtest.spec.numseq.IqTestNumberSeqCreator;
import libgdx.screen.AbstractScreen;
import libgdx.skelgameimpl.skelgame.SkelGameRatingService;
import libgdx.utils.Utils;

public class IqTestGameScreen extends AbstractScreen {

    private IqTestCurrentGame currentGame;

    @Override
    public void buildStage() {
        if (Game.getInstance().isFirstTimeMainMenuDisplayed()) {
            new SkelGameRatingService(this).appLaunched();
        }
        currentGame = new IqTestCurrentGame(IqNumSeqQuestion.Q0.getEnumAllValues());
        IqTestNumberSeqCreator creator = new IqTestNumberSeqCreator(currentGame, this);
//        IqTestGameCreator creator = new IqTestGameCreator(currentGame);
        creator.refreshLevel();
        new BackButtonBuilder()
                .setButtonSkin(SkelClassicButtonSkin.IQTEST_BACK_BTN)
                .addHoverBackButton(this, BackButtonBuilder.getX(), BackButtonBuilder.getY() * 1.01f);
    }

    @Override
    public void onBackKeyPress() {
        Gdx.app.exit();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        Utils.createChangeLangPopup();
    }
}
