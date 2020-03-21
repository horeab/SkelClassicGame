package libgdx.implementations.resourcewars.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.MainButtonSize;
import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.ButtonWithIconBuilder;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.controls.popup.MyPopup;
import libgdx.game.Game;
import libgdx.implementations.SkelClassicButtonSize;
import libgdx.implementations.SkelClassicButtonSkin;
import libgdx.implementations.resourcewars.ResourceWarsScreenManager;
import libgdx.implementations.resourcewars.spec.logic.GamePreferencesManager;
import libgdx.implementations.resourcewars.spec.logic.HighScorePreferencesManager;
import libgdx.implementations.resourcewars.spec.model.CurrentGame;
import libgdx.implementations.skelgame.SkelGameLabel;
import libgdx.resources.MainResource;
import libgdx.resources.dimen.MainDimen;
import libgdx.resources.gamelabel.MainGameLabel;
import libgdx.resources.gamelabel.SpecificPropertiesUtils;
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


    private void addTitle(Table table) {

        String appName = Game.getInstance().getAppInfoService().getAppName();
        float mult = appName.length() > 13 ? 2.0f : 2.7f;
        table.add(new MyWrappedLabel(
                new MyWrappedLabelConfigBuilder().setFontConfig(new FontConfig(FontColor.WHITE.getColor(),
                        FontColor.GREEN.getColor(),
                        Math.round(FontConfig.FONT_SIZE * mult * 1.2f),
                        8f)).setText(appName).build())).padBottom(MainDimen.vertical_general_margin.getDimen() * 1).row();
    }

    private void addAllTable() {
        Table table = new Table();
        table.setFillParent(true);
        addTitle(table);
        float verticalGeneralMarginDimen = MainDimen.vertical_general_margin.getDimen();

        MyButton infoBtn = new ButtonWithIconBuilder("", MainResource.question)
                .setFixedButtonSize(MainButtonSize.BACK_BUTTON).build();
        infoBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                new MyPopup<AbstractScreen, ResourceWarsScreenManager>(getAbstractScreen()) {
                    @Override
                    protected String getLabelText() {
                        return SpecificPropertiesUtils.getText("info");
                    }

                    @Override
                    protected void addButtons() {

                    }
                }.addToPopupManager();
            }
        });
        float horizontalGeneralMarginDimen = MainDimen.horizontal_general_margin.getDimen();
        infoBtn.setX(ScreenDimensionsManager.getScreenWidth() - MainButtonSize.BACK_BUTTON.getWidth() - horizontalGeneralMarginDimen);
        infoBtn.setY(ScreenDimensionsManager.getScreenHeight() - MainButtonSize.BACK_BUTTON.getHeight() - horizontalGeneralMarginDimen);
        addActor(infoBtn);
        table.add(createStartGameBtn())
                .width(SkelClassicButtonSize.RESOURCEWARS_MENU_BTN.getWidth())
                .height(SkelClassicButtonSize.RESOURCEWARS_MENU_BTN.getHeight())
                .padTop(verticalGeneralMarginDimen * 4).row();
        if (new GamePreferencesManager().getSavedGame() != null) {
            table.add(createContinueGameBtn())
                    .width(SkelClassicButtonSize.RESOURCEWARS_MENU_BTN.getWidth())
                    .height(SkelClassicButtonSize.RESOURCEWARS_MENU_BTN.getHeight())
                    .padTop(verticalGeneralMarginDimen * 2).row();
        }
        if (new HighScorePreferencesManager().getMaxDays() < HighScorePreferencesManager.MAX_DAYS_DEF) {
            MyWrappedLabel highScoreLabel = new MyWrappedLabel(
                    new MyWrappedLabelConfigBuilder().setFontConfig(
                            new FontConfig(FontColor.BLACK.getColor(), FontConfig.FONT_SIZE * 1.5f))
                            .setText(MainGameLabel.l_highscore.getText().replace(": {0}", "")).build());

            HighScorePreferencesManager highScorePreferencesManager = new HighScorePreferencesManager();
            String text = SkelGameLabel.l_highscorebudget.getText(highScorePreferencesManager.getMaxReputation(), highScorePreferencesManager.getMaxDays());
            MyWrappedLabel highScoreText = new MyWrappedLabel(
                    new MyWrappedLabelConfigBuilder().setFontConfig(
                            new FontConfig(FontColor.BLACK.getColor(), FontConfig.FONT_SIZE * 1.3f))
                            .setText(text).build());
            table.add(highScoreLabel).padTop(verticalGeneralMarginDimen * 2).row();
            table.add(highScoreText).padTop(verticalGeneralMarginDimen * 1).row();
        }
        addActor(table);
    }

    private MyButton createStartGameBtn() {
        MyButton button = new ButtonBuilder()
                .setFixedButtonSize(SkelClassicButtonSize.RESOURCEWARS_MENU_BTN)
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
                .setFixedButtonSize(SkelClassicButtonSize.RESOURCEWARS_MENU_BTN)
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
                        new FontConfig(FontColor.BLACK.getColor(), FontConfig.FONT_SIZE * 1.6f))
                        .setText(text).build());
    }

    @Override
    public void onBackKeyPress() {
        Gdx.app.exit();
    }
}
