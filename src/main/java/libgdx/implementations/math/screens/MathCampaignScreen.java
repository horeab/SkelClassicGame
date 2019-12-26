package libgdx.implementations.math.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import libgdx.campaign.CampaignLevelStatusEnum;
import libgdx.campaign.CampaignService;
import libgdx.campaign.CampaignStoreLevel;
import libgdx.controls.animations.ActorAnimation;
import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.game.Game;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.math.MathCampaignLevelEnum;
import libgdx.implementations.math.MathSpecificResource;
import libgdx.implementations.skelgame.SkelGameRatingService;
import libgdx.resources.MainResource;
import libgdx.resources.Res;
import libgdx.resources.dimen.MainDimen;
import libgdx.screen.AbstractScreen;
import libgdx.utils.Utils;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;

import java.util.Arrays;
import java.util.List;

public class MathCampaignScreen extends AbstractScreen<MathScreenManager> {

    private CampaignService campaignService = new CampaignService();
    private List<CampaignStoreLevel> allCampaignLevelStores;

    public MathCampaignScreen() {
        allCampaignLevelStores = campaignService.processAndGetAllLevels();
    }

    @Override
    public void buildStage() {
        if (Game.getInstance().isFirstTimeMainMenuDisplayed()) {
            new SkelGameRatingService(this).appLaunched();
        }
        Game.getInstance().setFirstTimeMainMenuDisplayed(false);
        Table table = new Table();
        addTitle(table);
        table.add(createAllTable());
        table.setFillParent(true);
        addActor(table);
        new BackButtonBuilder().addHoverBackButton(this);
    }


    private void addTitle(Table table) {
        table.add(new MyWrappedLabel(
                new MyWrappedLabelConfigBuilder().setFontConfig(new FontConfig(FontColor.WHITE.getColor(),
                        FontColor.BLACK.getColor(),
                        Math.round(FontConfig.FONT_SIZE * 2.5f),
                        2f)).setText(Game.getInstance().getAppInfoService().getAppName()).build())).padBottom(MainDimen.vertical_general_margin.getDimen() * 3).row();
    }


    private Table createAllTable() {
        Table table = new Table();
        List<MathCampaignLevelEnum> playableLevels = Arrays.asList(MathCampaignLevelEnum.values());
        int i = 0;
        float horizontalGeneralMarginDimen = MainDimen.horizontal_general_margin.getDimen();
        for (final MathCampaignLevelEnum campaignLevelEnum : MathCampaignLevelEnum.values()) {
            MyButton levelBtn = new ButtonBuilder()
                    .setDefaultButton()
                    .build();
            levelBtn.setBackground(GraphicUtils.getNinePatch(MainResource.popup_background));
            float imgDimen = horizontalGeneralMarginDimen * 8;
            String levelText;
            CampaignStoreLevel campaignStoreLevel = campaignService.getCampaignLevel(campaignLevelEnum.getIndex(), allCampaignLevelStores);
            Res res = MathSpecificResource.calculator;
            Image image = null;
            if (campaignStoreLevel != null && campaignStoreLevel.getStatus() == CampaignLevelStatusEnum.FINISHED.getStatus()) {
                levelText = campaignStoreLevel.getScore() + "";
            } else if (campaignStoreLevel != null && campaignStoreLevel.getStatus() == CampaignLevelStatusEnum.IN_PROGRESS.getStatus()) {
                levelText = "Play";
                image = GraphicUtils.getImage(res);
                image.setWidth(imgDimen);
                image.setHeight(imgDimen);
                new ActorAnimation(image, getAbstractScreen()).animateZoomInZoomOut();
            } else {
                res = MainResource.lock;
                levelBtn.setTouchable(Touchable.disabled);
                levelText = (i + 1) + "";
            }
            image = image == null ? GraphicUtils.getImage(res) : image;
            image.setWidth(imgDimen);
            image.setHeight(imgDimen);
            levelBtn.add(image).width(imgDimen).height(imgDimen).padBottom(horizontalGeneralMarginDimen).row();
            levelBtn.add(new MyWrappedLabel(
                    new MyWrappedLabelConfigBuilder().setFontConfig(new FontConfig(FontColor.WHITE.getColor(),
                            FontColor.BLACK.getColor(),
                            Math.round(FontConfig.FONT_SIZE * 1.3f),
                            2f)).setText(levelText).build()));
            levelBtn.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    screenManager.showGameScreen(campaignLevelEnum);
                }
            });
            float btnDimen = horizontalGeneralMarginDimen * 15;
            table.add(levelBtn).pad(horizontalGeneralMarginDimen).height(btnDimen).width(btnDimen);
            if (i > 0 && (i + 1) % 2 == 0) {
                table.row();
            }
            i++;
        }
        long totalScore = 0;
        for (CampaignStoreLevel campaignStoreLevel : allCampaignLevelStores) {
            totalScore = totalScore + campaignStoreLevel.getScore();
        }
        table.add(new MyWrappedLabel("Highscore: " + totalScore)).padTop(horizontalGeneralMarginDimen * 2).colspan(2);
        return table;
    }

    @Override
    public void onBackKeyPress() {
        Gdx.app.exit();
    }

    @Override
    public void render(float dt) {
        super.render(dt);
        Utils.createChangeLangPopup();
    }
}
