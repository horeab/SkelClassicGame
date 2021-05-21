package libgdx.implementations.iqtest.screens;

import com.badlogic.gdx.Gdx;
import libgdx.game.Game;
import libgdx.implementations.iqtest.spec.IqTestCurrentGame;
import libgdx.implementations.iqtest.spec.IqTestGameCreator;
import libgdx.implementations.iqtest.spec.IqTestNumberSeqCreator;
import libgdx.screen.AbstractScreen;
import libgdx.skelgameimpl.skelgame.SkelGameRatingService;
import libgdx.utils.Utils;
import libgdx.utils.model.RGBColor;

public class IqTestGameScreen extends AbstractScreen {

    private IqTestCurrentGame currentGame;

    @Override
    public void buildStage() {
        if (Game.getInstance().isFirstTimeMainMenuDisplayed()) {
            new SkelGameRatingService(this).appLaunched();
        }
        setBackgroundColor(RGBColor.WHITE);
        currentGame = new IqTestCurrentGame();
        IqTestNumberSeqCreator creator = new IqTestNumberSeqCreator(currentGame);
//        IqTestGameCreator creator = new IqTestGameCreator(currentGame);
        creator.refreshLevel();
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
