package libgdx.implementations.balloon.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import libgdx.campaign.CampaignStoreService;
import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.controls.button.builders.SoundIconButtonBuilder;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.game.Game;
import libgdx.implementations.SkelClassicButtonSkin;
import libgdx.implementations.balloon.BalloonCampaignLevelEnum;
import libgdx.implementations.balloon.BalloonScreenManager;
import libgdx.implementations.balloon.logic.LevelManager;
import libgdx.implementations.balloon.model.LevelInfo;
import libgdx.implementations.skelgame.SkelGameRatingService;
import libgdx.resources.dimen.MainDimen;
import libgdx.resources.gamelabel.MainGameLabel;
import libgdx.screen.AbstractScreen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.Utils;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;

public class BalloonMainMenuScreen extends AbstractScreen<BalloonScreenManager> {

    @Override
    public void buildStage() {
        if (Game.getInstance().isFirstTimeMainMenuDisplayed()) {
            new SkelGameRatingService(this).appLaunched();
        }
        addButtons();
        Game.getInstance().setFirstTimeMainMenuDisplayed(false);
    }

    private void addButtons() {
        Table table = new Table();
        float verticalGeneralMarginDimen = MainDimen.vertical_general_margin.getDimen();
        new BackButtonBuilder().addHoverBackButton(this);
        table.setFillParent(true);
        float soundBtnSize = verticalGeneralMarginDimen * 5;
        table.add(new SoundIconButtonBuilder().createSoundButton()).right().width(soundBtnSize).height(soundBtnSize).row();
        addTitle(table);
        float btnHeight = ScreenDimensionsManager.getScreenHeightValue(17);
        float btnWidth = ScreenDimensionsManager.getScreenWidthValue(40);
        table.add(createStartGameBtn()).height(btnHeight).width(btnWidth).padTop(verticalGeneralMarginDimen * 4).row();
        table.add(createMultiplayerBtn()).height(btnHeight).width(btnWidth).padTop(verticalGeneralMarginDimen * 2).row();
        addActor(table);
    }

    private void addTitle(Table table) {
        String appName = Game.getInstance().getAppInfoService().getAppName();
        float mult = 6.5f;
        table.add(new MyWrappedLabel(
                new MyWrappedLabelConfigBuilder().setFontConfig(new FontConfig(FontColor.DARK_RED.getColor(),
                        FontColor.BLACK.getColor(),
                        Math.round(FontConfig.FONT_SIZE * mult),
                        3f)).setText(appName).build())).padBottom(MainDimen.vertical_general_margin.getDimen() * 3).row();
    }

    private MyButton createStartGameBtn() {
        MyButton button = new ButtonBuilder()
                .setButtonSkin(SkelClassicButtonSkin.BALLOON_MENU).build();
        button.add(createBtnText(MainGameLabel.l_play.getText() + "!"));
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                int stageNr = new LevelManager().lastStageUnlocked();
                if (stageNr == 0) {
                    screenManager.showGameScreen(new LevelInfo(false, BalloonCampaignLevelEnum.LEVEL_0_0));
                } else {
                    screenManager.showCampaignScreen(stageNr);
                }
            }
        });
        return button;
    }

    private MyWrappedLabel createBtnText(String text) {
        return new MyWrappedLabel(
                new MyWrappedLabelConfigBuilder().setFontConfig(
                        new FontConfig(FontColor.BLACK.getColor(), Math.round(FontConfig.FONT_SIZE * 3.1f)))
                        .setText(text).build());
    }

    private MyButton createMultiplayerBtn() {
        MyButton button = new ButtonBuilder()
                .setButtonSkin(SkelClassicButtonSkin.BALLOON_MENU).build();
        button.add(createBtnText(MainGameLabel.l_multiplayer.getText()));
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                new CampaignStoreService().reset();
                BalloonCampaignLevelEnum levelEnum = BalloonCampaignLevelEnum.LEVEL_2_7;
                LevelInfo levelInfo = new LevelInfo(true, levelEnum);
                levelInfo.setNrColsForMultiplayer(levelEnum.getNrOfColumns());
                levelInfo.setNrRowsForMultiplayer(levelEnum.getNrOfRows());
                screenManager.showGameScreen(levelInfo);
            }
        });
        return button;
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        Utils.createChangeLangPopup();
    }

    @Override
    public void onBackKeyPress() {
        Gdx.app.exit();
    }

}
