package libgdx.implementations.memory.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import libgdx.campaign.CampaignLevelStatusEnum;
import libgdx.campaign.CampaignService;
import libgdx.campaign.CampaignStoreLevel;
import libgdx.campaign.CampaignStoreService;
import libgdx.constants.Language;
import libgdx.controls.animations.ActorAnimation;
import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.MainButtonSkin;
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
import libgdx.implementations.memory.MemoryScreenManager;
import libgdx.implementations.memory.MemorySpecificResource;
import libgdx.implementations.memory.spec.GameLevel;
import libgdx.implementations.memory.spec.Item;
import libgdx.implementations.memory.spec.ItemsUtil;
import libgdx.implementations.skelgame.SkelGameRatingService;
import libgdx.resources.FontManager;
import libgdx.resources.MainResource;
import libgdx.resources.Res;
import libgdx.resources.dimen.MainDimen;
import libgdx.resources.gamelabel.MainGameLabel;
import libgdx.resources.gamelabel.SpecificPropertiesUtils;
import libgdx.screen.AbstractScreen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.Utils;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

public class MemoryCampaignScreen extends AbstractScreen<MemoryScreenManager> {

    private CampaignStoreService campaignStoreService = new CampaignStoreService();

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
    }


    private void addTitle(Table table) {

        String appName = Game.getInstance().getAppInfoService().getAppName();
        float mult = appName.length() > 13 ? 2.0f : 2.7f;
        table.add(new MyWrappedLabel(
                new MyWrappedLabelConfigBuilder().setFontConfig(new FontConfig(FontColor.WHITE.getColor(),
                        FontColor.GREEN.getColor(),
                        Math.round(FontConfig.FONT_SIZE * mult),
                        8f)).setText(appName).build())).padBottom(MainDimen.vertical_general_margin.getDimen() * 1).row();
    }


    private Table createAllTable() {
        Table table = new Table();
        float verticalGeneralMarginDimen = MainDimen.vertical_general_margin.getDimen();
        table.add(createStartGameBtn()).height(ScreenDimensionsManager.getScreenHeightValue(13)).width(ScreenDimensionsManager.getScreenWidthValue(70))
                .padTop(verticalGeneralMarginDimen * 4).padBottom(verticalGeneralMarginDimen * 2).row();
        table.add(createDiscoveredItemsTable());
        return table;
    }

    private MyButton createStartGameBtn() {
        MyButton button = new ButtonBuilder().setSingleLineText(MainGameLabel.l_new_game.getText(), FontManager.getBigFontDim()).setButtonSkin(MainButtonSkin.DEFAULT).build();
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screenManager.showGameScreen(0);
            }
        });
        return button;
    }

    private Table createDiscoveredItemsTable() {
        Table table = new Table();
        List<Item> items = ItemsUtil.getItemsFromResources();
        int i = 0;
        for (Item item : items) {
            if (i % 5 == 0 && i != 0) {
                table.row();
            }
            Res res;
            String text = SpecificPropertiesUtils.getText("item" + i);
            if (campaignStoreService.isQuestionAlreadyPlayed("item" + i)) {
                res = MemorySpecificResource.valueOf("item" + item.getItemIndex());
            } else {
                text = "???";
                res = MemorySpecificResource.valueOf("unknown_menu");
            }
            Table itemTable = new Table();
            float side = ScreenDimensionsManager.getScreenWidth() / 7f;
            itemTable.add(GraphicUtils.getImage(res)).width(side).height(side).row();
            MyWrappedLabelConfigBuilder myWrappedLabelConfigBuilder = new MyWrappedLabelConfigBuilder()
                    .setFontConfig(new FontConfig(FontConfig.FONT_SIZE / 1.5f))
                    .setText(StringUtils.capitalize(text));
            if (text.contains(" ")) {
                myWrappedLabelConfigBuilder.setWrappedLineLabel(side);
            }
            itemTable.add(new MyWrappedLabel(
                    myWrappedLabelConfigBuilder
                            .build()));
            table.add(itemTable).width(side).pad(MainDimen.horizontal_general_margin.getDimen());
            i++;
        }
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
