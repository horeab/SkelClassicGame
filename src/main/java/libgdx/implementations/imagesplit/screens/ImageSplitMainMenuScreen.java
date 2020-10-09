package libgdx.implementations.imagesplit.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import libgdx.campaign.CampaignLevelEnumService;
import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.MyButton;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.game.Game;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.SkelClassicButtonSize;
import libgdx.implementations.SkelClassicButtonSkin;
import libgdx.implementations.imagesplit.ImageSplitCampaignLevelEnum;
import libgdx.implementations.imagesplit.ImageSplitScreenManager;
import libgdx.implementations.imagesplit.ImageSplitSpecificResource;
import libgdx.implementations.imagesplit.spec.ImageSplitGameType;
import libgdx.implementations.imagesplit.spec.SwipeDirection;
import libgdx.resources.dimen.MainDimen;
import libgdx.screen.AbstractScreen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.Utils;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;
import libgdx.utils.model.RGBColor;

public class ImageSplitMainMenuScreen extends AbstractScreen<ImageSplitScreenManager> {

    private Table imgLevelTable;
    private Table allTable;

    @Override
    public void buildStage() {
        setBackgroundColor(new RGBColor(1, 206, 255, 211));
        addAllTable();
    }

    private void addTitle(Table table) {

        String appName = Game.getInstance().getAppInfoService().getAppName();
        float mult = appName.length() > 10 ? 2.1f : 2.5f;
        mult = appName.length() > 16 ? 1.8f : mult;
        table.add(new MyWrappedLabel(
                new MyWrappedLabelConfigBuilder().setFontConfig(new FontConfig(FontColor.WHITE.getColor(),
                        FontColor.GREEN.getColor(),
                        Math.round(FontConfig.FONT_SIZE * mult * 1.2f),
                        8f)).setText(appName).build())).padBottom(MainDimen.vertical_general_margin.getDimen() * 1).row();
    }

    private void addAllTable() {
        Table allTable = new Table();
        allTable.setFillParent(true);
        addTitle(allTable);

        imgLevelTable = new Table();
        allTable.add(createImgLevel(ImageSplitCampaignLevelEnum.LEVEL_0_0));
        addActor(allTable);
    }

    private Table createImgLevel(ImageSplitCampaignLevelEnum campaignLevelEnum) {
        Image image = GraphicUtils.getImage(campaignLevelEnum.getRes());
        float width = ScreenDimensionsManager.getScreenWidthValue(60);
        imgLevelTable.add(new MyWrappedLabel(
                new MyWrappedLabelConfigBuilder().setFontConfig(
                        new FontConfig(FontColor.BLACK.getColor(), FontConfig.FONT_SIZE * 1.6f))
                        .setText(campaignLevelEnum.getCols() + " X " + campaignLevelEnum.getRows()).build())).row();

        Table imgNavigTable = new Table();
        addNavigBtnToTable(imgNavigTable, campaignLevelEnum, SwipeDirection.LEFT);
        imgNavigTable.add(image).width(width).height(ScreenDimensionsManager.getNewHeightForNewWidth(width, image));
        addNavigBtnToTable(imgNavigTable, campaignLevelEnum, SwipeDirection.RIGHT);
        imgLevelTable.add(imgNavigTable).row();

        Table btnTable = new Table();
        addStartGameBtnToTable(btnTable, ImageSplitGameType.SWAP, campaignLevelEnum);
        addStartGameBtnToTable(btnTable, ImageSplitGameType.PUSH, campaignLevelEnum);
        addStartGameBtnToTable(btnTable, ImageSplitGameType.SLIDE, campaignLevelEnum);
        imgLevelTable.add(btnTable);
        return imgLevelTable;
    }

    private void addNavigBtnToTable(Table imgNavigTable, ImageSplitCampaignLevelEnum campaignLevelEnum, SwipeDirection direction) {
        MyButton btn = createNavigationBtn(direction, campaignLevelEnum);
        if (direction == SwipeDirection.LEFT && campaignLevelEnum == ImageSplitCampaignLevelEnum.LEVEL_0_0
                || direction == SwipeDirection.RIGHT && campaignLevelEnum == ImageSplitCampaignLevelEnum.LEVEL_0_4) {
            btn.setVisible(false);
        }
        imgNavigTable.add(btn).height(btn.getHeight()).width(btn.getWidth()).pad(MainDimen.horizontal_general_margin.getDimen());
    }

    private MyButton createNavigationBtn(SwipeDirection direction, ImageSplitCampaignLevelEnum campaignLevelEnum) {
        MyButton button = new ButtonBuilder()
                .setFixedButtonSize(SkelClassicButtonSize.IMAGE_SPLIT_NAVIG_BTN)
                .setButtonSkin(SkelClassicButtonSkin.BUYLOW_MENU).build();
        int amount = direction == SwipeDirection.RIGHT ? 1 : -1;
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                final float duration = 0.1f;
                imgLevelTable.addAction(Actions.sequence(Actions.fadeOut(duration), Utils.createRunnableAction(new Runnable() {
                    @Override
                    public void run() {
                        imgLevelTable.clear();
                        imgLevelTable.addAction(Actions.fadeIn(duration));
                        createImgLevel(ImageSplitCampaignLevelEnum.valueOf("LEVEL_0_" + (CampaignLevelEnumService.getCategory(campaignLevelEnum.getName()) + amount)));
                    }
                })));
            }
        });
        return button;
    }

    private void addStartGameBtnToTable(Table table, ImageSplitGameType gameType, ImageSplitCampaignLevelEnum campaignLevelEnum) {
        MyButton swapBtn = createStartGameBtn(gameType, campaignLevelEnum);
        table.add(swapBtn).height(swapBtn.getHeight()).width(swapBtn.getWidth()).pad(MainDimen.horizontal_general_margin.getDimen());
    }

    private MyButton createStartGameBtn(ImageSplitGameType gameType, ImageSplitCampaignLevelEnum campaignLevelEnum) {
        MyButton button = new ButtonBuilder()
                .setFixedButtonSize(SkelClassicButtonSize.IMAGE_SPLIT_START_GAME_BTN)
                .setButtonSkin(SkelClassicButtonSkin.BUYLOW_MENU).build();
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screenManager.showGameScreen(gameType, campaignLevelEnum);
            }
        });
        return button;
    }

    @Override
    public void render(float dt) {
        super.render(dt);
        Utils.createChangeLangPopup();
    }

    @Override
    public void onBackKeyPress() {
        Gdx.app.exit();
    }
}
