package libgdx.implementations.buylow.spec;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import libgdx.controls.button.MyButton;
import libgdx.controls.popup.MyPopup;
import libgdx.game.Game;
import libgdx.implementations.skelgame.SkelGameLabel;
import libgdx.resources.gamelabel.MainGameLabel;
import libgdx.screen.AbstractScreen;
import libgdx.screen.AbstractScreenManager;

public class BuyLowLevelFinishedPopup extends MyPopup<AbstractScreen, AbstractScreenManager> {

    private boolean isHighScore;
    private int score;

    public BuyLowLevelFinishedPopup(AbstractScreen screen, boolean isHighScore, int score) {
        super(screen);
        this.isHighScore = isHighScore;
        this.score = score;
    }

    @Override
    public void addButtons() {
        MyButton goBack = new libgdx.controls.button.ButtonBuilder(SkelGameLabel.go_back.getText())
                .setDefaultButton()
                .build();
        goBack.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                onBackKeyPress();
            }
        });
        addButton(goBack);
    }

    @Override
    public void onBackKeyPress() {
        Game.getInstance().getAppInfoService().showPopupAd(new Runnable() {
            @Override
            public void run() {
                Game.getInstance().getScreenManager().showMainScreen();
            }
        });
    }

    @Override
    public void hide() {
        onBackKeyPress();
    }

    @Override
    protected String getLabelText() {
        if (isHighScore) {
            return MainGameLabel.l_congratulations.getText() + " " + MainGameLabel.l_highscore_record.getText() +
                    "\n" + MainGameLabel.l_score.getText(score);
        } else {
            return SkelGameLabel.l_game_finished.getText() + "\n" + MainGameLabel.l_score.getText(score);
        }
    }
}
