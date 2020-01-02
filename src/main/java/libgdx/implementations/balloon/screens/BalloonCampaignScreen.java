package libgdx.implementations.balloon.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import java.util.Arrays;
import java.util.List;

import libgdx.campaign.CampaignLevelStatusEnum;
import libgdx.campaign.CampaignService;
import libgdx.campaign.CampaignStoreLevel;
import libgdx.constants.Language;
import libgdx.controls.animations.ActorAnimation;
import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.game.Game;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.SkelClassicButtonSkin;
import libgdx.implementations.balloon.BalloonCampaignLevelEnum;
import libgdx.implementations.balloon.BalloonScreenManager;
import libgdx.implementations.math.MathCampaignLevelEnum;
import libgdx.implementations.math.MathScreenManager;
import libgdx.implementations.math.MathSpecificResource;
import libgdx.implementations.skelgame.SkelGameRatingService;
import libgdx.resources.MainResource;
import libgdx.resources.Res;
import libgdx.resources.dimen.MainDimen;
import libgdx.resources.gamelabel.MainGameLabel;
import libgdx.screen.AbstractScreen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.Utils;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;

public class BalloonCampaignScreen extends AbstractScreen<BalloonScreenManager> {

    private CampaignService campaignService = new CampaignService();
    private List<CampaignStoreLevel> allCampaignLevelStores;

    public BalloonCampaignScreen() {
        allCampaignLevelStores = campaignService.processAndGetAllLevels();
    }

    @Override
    public void buildStage() {
        if (Game.getInstance().isFirstTimeMainMenuDisplayed()) {
            new SkelGameRatingService(this).appLaunched();
        }
        Game.getInstance().setFirstTimeMainMenuDisplayed(false);
        Table table = new Table();
        table.add(createAllTable());
        table.setFillParent(true);
        addActor(table);
        new BackButtonBuilder().addHoverBackButton(this);
    }

    private Table createAllTable() {
        Table table = new Table();
        int i = 0;
        float horizontalGeneralMarginDimen = MainDimen.horizontal_general_margin.getDimen();
        int currentStage = 1;
        SkelClassicButtonSkin btnSkin = SkelClassicButtonSkin.BALLOON_STAGE1;
        if (currentStage == 2) {
            btnSkin = SkelClassicButtonSkin.BALLOON_STAGE2;
        } else if (currentStage == 3) {
            btnSkin = SkelClassicButtonSkin.BALLOON_STAGE3;
        }
        float levelBtnWidth = ScreenDimensionsManager.getScreenWidthValue(20);
        float levelBtnHeight = ScreenDimensionsManager.getScreenWidthValue(6);
        table.add().width(levelBtnWidth);
        table.add(new MyWrappedLabel(
                new MyWrappedLabelConfigBuilder().setFontConfig(
                        new FontConfig(FontColor.BLACK.getColor(), FontConfig.FONT_SIZE * 4))
                        .setText("Stage " + (i + 1)).build())).padBottom(horizontalGeneralMarginDimen * 3).width(levelBtnWidth);
        table.add().width(levelBtnWidth).row();
        for (final BalloonCampaignLevelEnum campaignLevelEnum : BalloonCampaignLevelEnum.values()) {
            MyButton levelBtn = new ButtonBuilder()
                    .setButtonSkin(btnSkin)
                    .build();
            CampaignStoreLevel campaignStoreLevel = campaignService.getCampaignLevel(campaignLevelEnum.getIndex(), allCampaignLevelStores);
            if (campaignStoreLevel == null) {
                levelBtn.setDisabled(true);
            }
            levelBtn.add(new MyWrappedLabel(
                    new MyWrappedLabelConfigBuilder().setWrappedLineLabel(levelBtnWidth).setFontConfig(
                            new FontConfig(FontColor.BLACK.getColor(), FontConfig.FONT_SIZE * 2))
                            .setText(MainGameLabel.l_level.getText("" + (i + 1))).build()));
            levelBtn.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    screenManager.showGameScreen();
                }
            });
            table.add(levelBtn).height(levelBtnHeight).width(levelBtnWidth);
            if (i > 0 && (i + 1) % 3 == 0) {
                table.row();
            }
            i++;
        }
        return table;
    }

    @Override
    public void onBackKeyPress() {
        screenManager.showMainScreen();
    }

    @Override
    public void render(float dt) {
        super.render(dt);
        Utils.createChangeLangPopup();
    }
}
