package libgdx.implementations.balloon.screens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.List;

import libgdx.campaign.CampaignService;
import libgdx.campaign.CampaignStoreLevel;
import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.controls.popup.notificationpopup.MyNotificationPopupConfigBuilder;
import libgdx.controls.popup.notificationpopup.MyNotificationPopupCreator;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.SkelClassicButtonSkin;
import libgdx.implementations.balloon.BalloonCampaignLevelEnum;
import libgdx.implementations.balloon.BalloonScreenManager;
import libgdx.implementations.balloon.BalloonSpecificResource;
import libgdx.implementations.balloon.logic.LevelManager;
import libgdx.implementations.balloon.model.LevelInfo;
import libgdx.resources.dimen.MainDimen;
import libgdx.screen.AbstractScreen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.Utils;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;

public class BalloonLevelFinishedScreen extends AbstractScreen<BalloonScreenManager> {

    private LevelManager levelManager = new LevelManager();
    private CampaignService campaignService = new CampaignService();
    private List<CampaignStoreLevel> allCampaignLevelStores;
    private LevelInfo levelInfo;
    private int player1Score;
    private int player2Score;

    public BalloonLevelFinishedScreen(LevelInfo levelInfo, int player1Score, int player2Score) {
        allCampaignLevelStores = campaignService.processAndGetAllLevels();
        this.levelInfo = levelInfo;
        this.player1Score = player1Score;
        this.player2Score = player2Score;
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
        table.add(createPlayerContainers());
        table.row();
        table.add(createButtons());
        return table;
    }

    private void replayLevel() {
        screenManager.showGameScreen(levelInfo);
    }

    private Table createPlayerContainers() {
        float verticalGeneralMarginDimen = MainDimen.vertical_general_margin.getDimen();
        Table infoContainer = new Table();
        //TODO
        infoContainer.add(new MyWrappedLabel(levelManager.isGameFinished() ? "Congratulations!\nGame finished!" : "Level finished!",
                new FontConfig(FontConfig.FONT_SIZE * 4))).pad(verticalGeneralMarginDimen).row();
        Table plInfoTable = new Table();
        plInfoTable.add(createPlContainer(BalloonSpecificResource.balloonp1)).growX();
        plInfoTable.add(createLevelInfo()).growX();
        if (!levelInfo.isOnePlayerLevel()) {
            Table pl2Container = createPlContainer(BalloonSpecificResource.balloonp2);
            plInfoTable.add(pl2Container).growX();
        }
        infoContainer.add(plInfoTable).pad(verticalGeneralMarginDimen).row();
        String levelStatusText = "";
        FontColor fontColor = FontColor.BLACK;
        if (player1Score > player2Score) {
            levelStatusText = levelInfo.isMultiplayer() ? "Player 1 wins!" : "You win!";
            fontColor = FontColor.DARK_RED;
        } else if (player1Score == player2Score) {
            levelStatusText = "Game ended in a draw!";
        } else {
            levelStatusText = levelInfo.isMultiplayer() ? "Player 2 wins!" : "Your opponent wins!";
            fontColor = FontColor.YELLOW;
        }
        infoContainer.add(new MyWrappedLabel(levelStatusText, new FontConfig(fontColor.getColor(), FontColor.BLACK.getColor(), FontConfig.FONT_SIZE * 4, 2f))).pad(verticalGeneralMarginDimen);
        return infoContainer;
    }

    private Table createLevelInfo() {
        String text = player1Score + "";
        if (!levelInfo.isOnePlayerLevel()) {
            text = text + " - " + player2Score;
        }
        Table table = new Table();
        MyWrappedLabel lvlInfo = new MyWrappedLabel(new MyWrappedLabelConfigBuilder().setWidth(ScreenDimensionsManager.getScreenWidthValue(levelInfo.isOnePlayerLevel() ? 20 : 50))
                .setFontConfig(new FontConfig(FontColor.LIGHT_GREEN.getColor(), FontColor.BLACK.getColor(), Math.round(FontConfig.FONT_SIZE * 5f), 4f))
                .setText(text).build());
        table.add(lvlInfo);
        return table;
    }

    private Table createPlContainer(BalloonSpecificResource plIcon) {
        Table plContainer = new Table();
        float iconDimen = MainDimen.horizontal_general_margin.getDimen() * 7;
        plContainer.add(GraphicUtils.getImage(plIcon)).width(iconDimen).height(iconDimen);
        return plContainer;
    }

    private Table createButtons() {
        Table btnTable = new Table();
        float btnWidth = ScreenDimensionsManager.getScreenWidthValue(25);
        float btnHeight = ScreenDimensionsManager.getScreenWidthValue(10);

        MyButton replayBtn = new ButtonBuilder()
                .setButtonSkin(SkelClassicButtonSkin.BALLOON_STAGE0)
                .build();
        //TODO
        replayBtn.add(createBtnLabel(btnWidth, "Play again!"));
        if ((!levelInfo.isMultiplayer() && player1Score < player2Score)
                ||
                levelInfo.isMultiplayer()) {
            btnTable.add(replayBtn).height(btnHeight).width(btnWidth);
            replayBtn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    replayLevel();
                }
            });
        }

        MyButton goBackBtn = new ButtonBuilder()
                .setButtonSkin(SkelClassicButtonSkin.BALLOON_STAGE0)
                .build();
        //TODO
        goBackBtn.add(createBtnLabel(btnWidth, (!levelInfo.isMultiplayer() && player1Score > player2Score) ? "Continue" : "Go back"));
        goBackBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (levelInfo.isMultiplayer()) {
                    screenManager.showMainScreen();
                } else {
                    screenManager.showCampaignScreen(levelManager.lastStageUnlocked());
                }
            }
        });

        btnTable.add(goBackBtn).height(btnHeight).width(btnWidth);
        return btnTable;
    }

    private MyWrappedLabel createBtnLabel(float levelBtnWidth, String text) {
        return new MyWrappedLabel(
                new MyWrappedLabelConfigBuilder().setWrappedLineLabel(levelBtnWidth).setFontConfig(
                        new FontConfig(FontColor.BLACK.getColor(), FontConfig.FONT_SIZE * 2))
                        .setText(text).build());
    }

    private void decideWhatContainerToBeShown() {
//        if (!levelInfo.isMultiplayer()) {
//            BalloonCampaignLevelEnum level = BalloonCampaignLevelEnum.getLevelForStageNrAndLevelNr(levelInfo.getLevelEnum().getStageNr(), levelInfo.getLevelEnum().getLevelNr());
//            if (level.isOnePlayerLevel()) {
//                findViewById(R.id.finalScoreInfoForTwoPlayers).setVisibility(View.GONE);
//                findViewById(R.id.scoreInfo).setVisibility(View.GONE);
//            } else {
//                findViewById(R.id.finalScoreInfoForOnePlayer).setVisibility(View.GONE);
//            }
//        } else {
//            findViewById(R.id.finalScoreInfoForOnePlayer).setVisibility(View.GONE);
//        }
    }

    @Override
    public void onBackKeyPress() {
        if(levelInfo.isMultiplayer()){
            screenManager.showMainScreen();
        }else {
            screenManager.showCampaignScreen(levelInfo.getLevelEnum().getStageNr());
        }
    }

    @Override
    public void render(float dt) {
        super.render(dt);
        Utils.createChangeLangPopup();
    }
}
