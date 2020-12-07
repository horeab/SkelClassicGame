package libgdx.implementations.imagesplit.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.Arrays;

import libgdx.campaign.CampaignLevelEnumService;
import libgdx.constants.Language;
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
import libgdx.implementations.imagesplit.spec.ImageSplitPreferencesManager;
import libgdx.implementations.imagesplit.spec.SwipeDirection;
import libgdx.resources.MainResource;
import libgdx.resources.Res;
import libgdx.resources.dimen.MainDimen;
import libgdx.screen.AbstractScreen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.Utils;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;
import libgdx.utils.model.RGBColor;

public class ImageSplitMainMenuScreen extends AbstractScreen<ImageSplitScreenManager> {

    private Table imgLevelTable;
    private ImageSplitPreferencesManager imageSplitPreferencesManager = new ImageSplitPreferencesManager();

    @Override
    public void buildStage() {
        setBackgroundColor(new RGBColor(1, 206, 255, 211));
        addAllTable();
        setBackgroundColor(RGBColor.LIGHT_BLUE);
    }

    protected Res getBackgroundTexture() {
        return MainResource.transparent_background;
    }

    private void addTitle(Table table) {

        String appName = Game.getInstance().getAppInfoService().getAppName();
        float mult = appName.length() > 10 || Arrays.asList(Language.zh.name(), Language.ja.name(), Language.ko.name()).contains(Game.getInstance().getAppInfoService().getLanguage())
                ? 1.5f : 1.9f;
        mult = appName.length() > 16 ? 1.4f : mult;
        table.add(new MyWrappedLabel(new MyWrappedLabelConfigBuilder().setFontConfig(new FontConfig(RGBColor.WHITE.toColor(),
                RGBColor.DARK_GREEN.toColor(), Math.round(FontConfig.FONT_SIZE * mult * 1.4f),
                3f, 3, 3, RGBColor.BLACK.toColor(0.5f))).setText(appName).build())).padBottom(MainDimen.vertical_general_margin.getDimen() * 1).row();
    }

    private void addAllTable() {
        Table allTable = new Table();
        allTable.setFillParent(true);
        addTitle(allTable);

        imgLevelTable = new Table();
        allTable.add(createImgLevel(getFirstNotFinishedLevel()));
        addActor(allTable);
    }

    private Table createImgLevel(ImageSplitCampaignLevelEnum campaignLevelEnum) {
        Image image = GraphicUtils.getImage(campaignLevelEnum.getRes());
        float width = ScreenDimensionsManager.getScreenWidthValue(60);
        imgLevelTable.add(new MyWrappedLabel(new MyWrappedLabelConfigBuilder().setFontConfig(new FontConfig(FontColor.WHITE.getColor(),
                FontColor.DARK_GRAY.getColor(), Math.round(FontConfig.FONT_SIZE * 1.4f),
                3f)).setText(campaignLevelEnum.getCols() + " X " + campaignLevelEnum.getRows()).build())).row();

        Table imgNavigTable = new Table();
        addNavigBtnToTable(imgNavigTable, campaignLevelEnum, SwipeDirection.LEFT);
        imgNavigTable.add(image).width(width).height(ScreenDimensionsManager.getNewHeightForNewWidth(width, image));
        addNavigBtnToTable(imgNavigTable, campaignLevelEnum, SwipeDirection.RIGHT);
        imgLevelTable.add(imgNavigTable).height(ScreenDimensionsManager.getScreenHeightValue(50)).row();

        Table btnTable = new Table();
        for (ImageSplitGameType gameType : campaignLevelEnum.getGameTypes()) {
            btnTable.add(createStartGameBtnTable(gameType, campaignLevelEnum));
        }
        imgLevelTable.add(btnTable);
        return imgLevelTable;
    }

    private void addNavigBtnToTable(Table imgNavigTable, ImageSplitCampaignLevelEnum campaignLevelEnum, SwipeDirection direction) {
        MyButton btn = createNavigationBtn(direction, campaignLevelEnum);
        if (direction == SwipeDirection.LEFT && campaignLevelEnum == ImageSplitCampaignLevelEnum.LEVEL_0_0
                || direction == SwipeDirection.RIGHT && campaignLevelEnum == getLastCampaignLevel()) {
            btn.setVisible(false);
        }
        imgNavigTable.add(btn).height(btn.getHeight()).width(btn.getWidth()).pad(MainDimen.horizontal_general_margin.getDimen());
    }

    private ImageSplitCampaignLevelEnum getLastCampaignLevel() {
        return ImageSplitCampaignLevelEnum.values()[ImageSplitCampaignLevelEnum.values().length - 1];
    }

    private MyButton createNavigationBtn(SwipeDirection direction, final ImageSplitCampaignLevelEnum campaignLevelEnum) {
        final MyButton button = new ButtonBuilder()
                .setFixedButtonSize(SkelClassicButtonSize.IMAGE_SPLIT_NAVIG_BTN)
                .setButtonSkin(direction == SwipeDirection.RIGHT ? SkelClassicButtonSkin.IMAGE_SPLIT_NAVIG_RIGHT : SkelClassicButtonSkin.IMAGE_SPLIT_NAVIG_LEFT).build();
        final int amount = direction == SwipeDirection.RIGHT ? 1 : -1;
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                final float duration = 0.1f;
                imgLevelTable.addAction(Actions.sequence(Actions.fadeOut(duration), Utils.createRunnableAction(new Runnable() {
                    @Override
                    public void run() {
                        button.setTouchable(Touchable.disabled);
                        imgLevelTable.clear();
                        imgLevelTable.addAction(Actions.fadeIn(duration));
                        createImgLevel(ImageSplitCampaignLevelEnum.valueOf("LEVEL_0_" + (CampaignLevelEnumService.getCategory(campaignLevelEnum.getName()) + amount)));
                    }
                })));
            }
        });
        return button;
    }

    private Table createStartGameBtnTable(ImageSplitGameType gameType, ImageSplitCampaignLevelEnum campaignLevelEnum) {
        MyButton startGameBtn = createStartGameBtn(gameType, campaignLevelEnum);
        int maxMoves = imageSplitPreferencesManager.getMaxMoves(gameType, campaignLevelEnum);
        int maxSec = imageSplitPreferencesManager.getMaxSeconds(gameType, campaignLevelEnum);
        float btnWidth = startGameBtn.getWidth();
        Table btnTable = new Table();
        btnTable.add(startGameBtn).height(startGameBtn.getHeight()).width(btnWidth).pad(MainDimen.horizontal_general_margin.getDimen()).row();
        btnTable.add(createScoresTable(maxSec, maxMoves, false, false)).width(btnWidth);
        return btnTable;
    }

    static Table createScoresTable(int maxSec, int maxMoves, boolean secondsRecord, boolean movesRecord) {
        float scoreTableHeight = SkelClassicButtonSize.IMAGE_SPLIT_START_GAME_BTN.getHeight() / 4;
        float scoreItemWidth = SkelClassicButtonSize.IMAGE_SPLIT_START_GAME_BTN.getWidth() / 2;
        Table scoreTable = new Table();
        scoreTable.add(createScoreTable(maxSec, ImageSplitSpecificResource.seconds_icon, secondsRecord, scoreItemWidth, scoreTableHeight)).width(scoreItemWidth).height(scoreTableHeight).row();
        scoreTable.add(createScoreTable(maxMoves, ImageSplitSpecificResource.moves_icon, movesRecord, scoreItemWidth, scoreTableHeight)).width(scoreItemWidth).height(scoreTableHeight);
        return scoreTable;
    }

    private static Table createScoreTable(int val, Res icon, boolean isRecord, float totalWidth, float totalHeight) {
        String text = val == 0 ? "-" : val + "";
        Table scoreTable = new Table();
        float imgSideDimen = totalWidth / 2;
        scoreTable.add(GraphicUtils.getImage(icon)).height(imgSideDimen).width(imgSideDimen);
        Color borderColor = isRecord ? RGBColor.DARK_GREEN.toColor() : FontColor.BLACK.getColor();
        scoreTable.add(new MyWrappedLabel(new MyWrappedLabelConfigBuilder().setFontConfig(new FontConfig(isRecord ? RGBColor.LIGHT_GREEN.toColor() : FontColor.WHITE.getColor(),
                borderColor, Math.round(FontConfig.FONT_SIZE * 1.2f), 2.5f)).setText(text).build()))
                .height(totalHeight).width(totalWidth);
        return scoreTable;
    }

    private MyButton createStartGameBtn(final ImageSplitGameType gameType, final ImageSplitCampaignLevelEnum campaignLevelEnum) {
        int category = CampaignLevelEnumService.getCategory(campaignLevelEnum.getName());
        int firstNotFinishedCategory = CampaignLevelEnumService.getCategory(getFirstNotFinishedLevel().getName());
        boolean btnDisabled = category > firstNotFinishedCategory;
        MyButton button = new ButtonBuilder()
                .setDisabled(btnDisabled)
                .setFixedButtonSize(SkelClassicButtonSize.IMAGE_SPLIT_START_GAME_BTN)
                .setButtonSkin(SkelClassicButtonSkin.valueOf("IMAGE_SPLIT_GAME_TYPE_" + gameType.name())).build();
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screenManager.showGameScreen(gameType, campaignLevelEnum);
            }
        });
        return button;
    }

    private ImageSplitCampaignLevelEnum getFirstNotFinishedLevel() {
        for (ImageSplitCampaignLevelEnum campaignLevelEnum : ImageSplitCampaignLevelEnum.values()) {
            for (ImageSplitGameType gameType : campaignLevelEnum.getGameTypes()) {
                if (imageSplitPreferencesManager.getMaxSeconds(gameType, campaignLevelEnum) == 0) {
                    return campaignLevelEnum;
                }
            }
        }
        return getLastCampaignLevel();
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
