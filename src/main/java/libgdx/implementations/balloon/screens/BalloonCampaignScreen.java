package libgdx.implementations.balloon.screens;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import java.util.List;

import libgdx.campaign.CampaignService;
import libgdx.campaign.CampaignStoreLevel;
import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.implementations.SkelClassicButtonSkin;
import libgdx.implementations.balloon.BalloonCampaignLevelEnum;
import libgdx.implementations.balloon.BalloonScreenManager;
import libgdx.implementations.balloon.model.LevelInfo;
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
    private int stageNr;

    public BalloonCampaignScreen(int stageNr) {
        this.stageNr = stageNr;
        allCampaignLevelStores = campaignService.processAndGetAllLevels();
    }

    @Override
    public void buildStage() {
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
        SkelClassicButtonSkin btnSkin = SkelClassicButtonSkin.BALLOON_STAGE0;
        if (stageNr == 1) {
            btnSkin = SkelClassicButtonSkin.BALLOON_STAGE1;
        } else if (stageNr == 2) {
            btnSkin = SkelClassicButtonSkin.BALLOON_STAGE2;
        }
        float levelBtnWidth = ScreenDimensionsManager.getScreenWidth(20);
        float levelBtnHeight = ScreenDimensionsManager.getScreenWidth(6);
        String text = getStageTitle(stageNr);
        table.add(new MyWrappedLabel(
                new MyWrappedLabelConfigBuilder().setFontConfig(
                        new FontConfig(FontColor.BLACK.getColor(), FontConfig.FONT_SIZE * 4))
                        .setText(text).build())).padBottom(horizontalGeneralMarginDimen * 3).width(levelBtnWidth).row();
        List<BalloonCampaignLevelEnum> levelsForStage = BalloonCampaignLevelEnum.getLevelsForStage(stageNr);
        Table btnContainer = new Table();
        for (final BalloonCampaignLevelEnum campaignLevelEnum : levelsForStage) {
            MyButton levelBtn = new ButtonBuilder()
                    .setButtonSkin(btnSkin)
                    .build();
            CampaignStoreLevel campaignStoreLevel = campaignService.getCampaignLevel(campaignLevelEnum.getIndex(), allCampaignLevelStores);
            if (campaignStoreLevel == null) {
                levelBtn.setDisabled(true);
            }
            String btnText = levelsForStage.size() == 1 ? MainGameLabel.l_play.getText() : MainGameLabel.l_level.getText("" + (i + 1));
            levelBtn.add(new MyWrappedLabel(
                    new MyWrappedLabelConfigBuilder().setWrappedLineLabel(levelBtnWidth).setFontConfig(
                            new FontConfig(FontColor.WHITE.getColor(), FontColor.BLACK.getColor(), FontConfig.FONT_SIZE * 2, 4f))
                            .setText(btnText).build()));
            levelBtn.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    screenManager.showGameScreen(new LevelInfo(false, campaignLevelEnum));
                }
            });
            if (i > 0 && (i + 1) % 3 == 0) {
                btnContainer.row();
            }
            i++;
            btnContainer.add(levelBtn).pad(horizontalGeneralMarginDimen / 2).height(levelBtnHeight).width(levelBtnWidth);
        }
        table.add(btnContainer);
        return table;
    }

    public static String getStageTitle(int stageNr) {
        return stageNr == 0 ? MainGameLabel.l_tutorial.getText() : MainGameLabel.l_stage.getText(stageNr + "");
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
