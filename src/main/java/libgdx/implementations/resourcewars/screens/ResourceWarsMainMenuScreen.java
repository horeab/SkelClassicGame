package libgdx.implementations.resourcewars.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.MyButton;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.implementations.SkelClassicButtonSkin;
import libgdx.implementations.resourcewars.ResourceWarsScreenManager;
import libgdx.implementations.resourcewars.spec.logic.GamePreferencesManager;
import libgdx.implementations.resourcewars.spec.logic.HighScorePreferencesManager;
import libgdx.implementations.resourcewars.spec.model.CurrentGame;
import libgdx.implementations.skelgame.SkelGameLabel;
import libgdx.resources.dimen.MainDimen;
import libgdx.resources.gamelabel.MainGameLabel;
import libgdx.screen.AbstractScreen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;
import libgdx.utils.model.RGBColor;

public class ResourceWarsMainMenuScreen extends AbstractScreen<ResourceWarsScreenManager> {


    @Override
    public void buildStage() {
        setBackgroundColor(new RGBColor(1, 206, 255, 211));
        addAllTable();
    }

    private void addAllTable() {
        Table table = new Table();
        table.setFillParent(true);
        float verticalGeneralMarginDimen = MainDimen.vertical_general_margin.getDimen();
        float btnHeight = ScreenDimensionsManager.getScreenHeightValue(14);
        float btnWidth = ScreenDimensionsManager.getScreenWidthValue(35);

        MyWrappedLabel highScoreLabel = new MyWrappedLabel(
                new MyWrappedLabelConfigBuilder().setFontConfig(
                        new FontConfig(FontColor.BLACK.getColor(), FontConfig.FONT_SIZE * 1.3f))
                        .setText(MainGameLabel.l_highscore.getText().replace(": {0}", "")).build());

        HighScorePreferencesManager highScorePreferencesManager = new HighScorePreferencesManager();
        String text = SkelGameLabel.l_highscorebudget.getText(highScorePreferencesManager.getMaxReputation(), highScorePreferencesManager.getMaxDays());
        MyWrappedLabel highScoreText = new MyWrappedLabel(
                new MyWrappedLabelConfigBuilder().setFontConfig(
                        new FontConfig(FontColor.BLACK.getColor(), FontConfig.FONT_SIZE * 1.3f))
                        .setText(text).build());

        table.add(createStartGameBtn()).height(btnHeight).width(btnWidth).padTop(verticalGeneralMarginDimen * 4).row();
        table.add(createContinueGameBtn()).height(btnHeight).width(btnWidth).padTop(verticalGeneralMarginDimen * 2).row();
        if (new HighScorePreferencesManager().getMaxDays() < HighScorePreferencesManager.MAX_DAYS_DEF) {
            table.add(highScoreLabel).width(btnWidth).padTop(verticalGeneralMarginDimen * 2).row();
            table.add(highScoreText).width(btnWidth).padTop(verticalGeneralMarginDimen * 1).row();
        }
        addActor(table);
    }

    private MyButton createStartGameBtn() {
        MyButton button = new ButtonBuilder()
                .setButtonSkin(SkelClassicButtonSkin.RESOURCEWARS_MENU).build();
        button.add(createBtnText(MainGameLabel.l_new_game.getText() + "!"));
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                new GamePreferencesManager().reset();
                screenManager.showGameScreen(new CurrentGame());
            }
        });
        return button;
    }

    private MyButton createContinueGameBtn() {
        MyButton button = new ButtonBuilder()
                .setButtonSkin(SkelClassicButtonSkin.RESOURCEWARS_MENU).build();
        button.add(createBtnText(MainGameLabel.l_continue.getText()));
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screenManager.showGameScreen(new GamePreferencesManager().getSavedGame());
            }
        });
        return button;
    }

    private MyWrappedLabel createBtnText(String text) {
        return new MyWrappedLabel(
                new MyWrappedLabelConfigBuilder().setFontConfig(
                        new FontConfig(FontColor.BLACK.getColor(), Math.round(text.length() > 12 ? FontConfig.FONT_SIZE * 2f : FontConfig.FONT_SIZE * 2.5f)))
                        .setText(text).build());
    }

    @Override
    public void onBackKeyPress() {
        Gdx.app.exit();
    }
}
