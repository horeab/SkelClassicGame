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
import libgdx.implementations.math.MathCampaignLevelEnum;
import libgdx.implementations.math.MathScreenManager;
import libgdx.implementations.math.MathSpecificResource;
import libgdx.implementations.skelgame.SkelGameRatingService;
import libgdx.resources.MainResource;
import libgdx.resources.Res;
import libgdx.resources.dimen.MainDimen;
import libgdx.resources.gamelabel.MainGameLabel;
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
        String appName = Game.getInstance().getAppInfoService().getAppName();
        float mult = appName.length() > 10 ? (appName.length() > 15 ? 1.5f : 2.5f) : 2.5f;
        mult = Game.getInstance().getAppInfoService().getLanguage().equals(Language.th.name()) ? mult / 2f : mult;
        table.add(new MyWrappedLabel(
                new MyWrappedLabelConfigBuilder().setFontConfig(new FontConfig(FontColor.WHITE.getColor(),
                        FontColor.BLACK.getColor(),
                        Math.round(FontConfig.FONT_SIZE * mult),
                        2f)).setText(appName).build())).padBottom(MainDimen.vertical_general_margin.getDimen() * 3).row();
    }


    private Table createAllTable() {
        Table table = new Table();
        List<MathCampaignLevelEnum> playableLevels = Arrays.asList(MathCampaignLevelEnum.values());
        int i = 0;
        float horizontalGeneralMarginDimen = MainDimen.horizontal_general_margin.getDimen();
        for (final MathCampaignLevelEnum campaignLevelEnum : MathCampaignLevelEnum.values()) {
            MyButton levelBtn = new ButtonBuilder()
                    .setButtonSkin(SkelClassicButtonSkin.MATH_LEVELBTN)
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
                levelText = MainGameLabel.l_play.getText();
                image = GraphicUtils.getImage(res);
                image.setWidth(imgDimen);
                image.setHeight(imgDimen);
                new ActorAnimation(getAbstractScreen()).animateZoomInZoomOut(image);
            } else {
                res = MainResource.lock;
                levelBtn.setTouchable(Touchable.disabled);
                levelText = (i + 1) + "";
            }
            image = image == null ? GraphicUtils.getImage(res) : image;
            image.setWidth(imgDimen);
            image.setHeight(imgDimen);
            float btnDimen = horizontalGeneralMarginDimen * 15;
            levelBtn.add(image).width(imgDimen).height(imgDimen).padBottom(horizontalGeneralMarginDimen).row();
            levelBtn.add(new MyWrappedLabel(
                    new MyWrappedLabelConfigBuilder().setWrappedLineLabel(btnDimen).setFontConfig(new FontConfig(FontColor.WHITE.getColor(),
                            FontColor.BLACK.getColor(),
                            Math.round(FontConfig.FONT_SIZE * (Arrays.asList(Language.th.name(), Language.tr.name()).contains(Game.getInstance().getAppInfoService().getLanguage()) ? 0.9f : 1.3f)),
                            2f)).setText(levelText).build()));
            levelBtn.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    screenManager.showGameScreen(campaignLevelEnum);
                }
            });
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
        table.add(new MyWrappedLabel(MainGameLabel.l_highscore.getText("" + totalScore))).padTop(horizontalGeneralMarginDimen * 2).colspan(2);
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
