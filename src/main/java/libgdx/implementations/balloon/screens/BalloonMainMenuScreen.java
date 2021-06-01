package libgdx.implementations.balloon.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import libgdx.campaign.CampaignStoreService;
import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.SoundIconButtonBuilder;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.game.Game;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.SkelClassicButtonSkin;
import libgdx.implementations.balloon.BalloonCampaignLevelEnum;
import libgdx.implementations.balloon.BalloonScreenManager;
import libgdx.implementations.balloon.BalloonSpecificResource;
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
        table.setFillParent(true);
        float soundBtnSize = verticalGeneralMarginDimen * 5;
        table.add(new SoundIconButtonBuilder().createSoundButton()).right().width(soundBtnSize).height(soundBtnSize).row();
        addTitle(table);
        float btnHeight = ScreenDimensionsManager.getScreenHeight(14);
        float btnWidth = ScreenDimensionsManager.getScreenWidth(35);
        table.add(createStartGameBtn()).height(btnHeight).width(btnWidth).padTop(verticalGeneralMarginDimen * 4).row();
        table.add(createMultiplayerBtn()).height(btnHeight).width(btnWidth).padTop(verticalGeneralMarginDimen * 2).row();
        addActor(table);
        addBackgrImage();
    }

    private void addBackgrImage() {
        Image image = GraphicUtils.getImage(BalloonSpecificResource.hot_air_balloon);
        float verticalGeneralMarginDimen = MainDimen.vertical_general_margin.getDimen();
        float imgDimen = verticalGeneralMarginDimen * 25;
        image.setHeight(imgDimen);
        image.setWidth(imgDimen);
        image.setX(ScreenDimensionsManager.getScreenWidth(5));
        image.setY(ScreenDimensionsManager.getScreenHeight(5));
        image.toBack();
        addActor(image);
    }

    private void addTitle(Table table) {
        String appName = Game.getInstance().getAppInfoService().getAppName();
        float mult = 5f;
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
                        new FontConfig(FontColor.BLACK.getColor(), Math.round(text.length() > 12 ? FontConfig.FONT_SIZE * 2f : FontConfig.FONT_SIZE * 2.5f)))
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
