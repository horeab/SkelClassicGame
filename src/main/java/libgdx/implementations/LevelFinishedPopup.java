package libgdx.implementations;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import libgdx.controls.button.MyButton;
import libgdx.controls.popup.MyPopup;
import libgdx.game.Game;
import libgdx.implementations.skelgame.SkelGameLabel;
import libgdx.screen.AbstractScreen;
import libgdx.screen.AbstractScreenManager;

public class LevelFinishedPopup extends MyPopup<AbstractScreen, AbstractScreenManager> {

    private boolean levelSuccess;
    private Runnable showPlayAgainScreen;

    public LevelFinishedPopup(AbstractScreen screen, boolean levelSuccess, Runnable showPlayAgainScreen) {
        super(screen);
        this.levelSuccess = levelSuccess;
        this.showPlayAgainScreen = showPlayAgainScreen;
    }

    @Override
    public void addButtons() {
        if (levelSuccess) {
            MyButton button = new libgdx.controls.button.ButtonBuilder(SkelGameLabel.next_level.getText())
                    .setDefaultButton()
                    .build();
            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    onBackKeyPress();
                }
            });
            addButton(button);
        } else {
            MyButton playAgain = new libgdx.controls.button.ButtonBuilder(SkelGameLabel.play_again.getText())
                    .setDefaultButton()
                    .build();
            playAgain.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    showPlayAgainScreen.run();
                }
            });
            MyButton goBack = new libgdx.controls.button.ButtonBuilder(SkelGameLabel.go_back.getText())
                    .setDefaultButton()
                    .build();
            goBack.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Game.getInstance().getScreenManager().showMainScreen();
                }
            });
            addButton(playAgain);
            addButton(goBack);
        }
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
        if (levelSuccess) {
            return SkelGameLabel.level_finished.getText();
        } else {
            return SkelGameLabel.level_failed.getText();
        }
    }
}
