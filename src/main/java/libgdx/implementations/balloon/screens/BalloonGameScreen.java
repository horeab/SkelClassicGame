package libgdx.implementations.balloon.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import libgdx.campaign.CampaignStoreService;
import libgdx.controls.MyTextField;
import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.controls.popup.MyPopup;
import libgdx.controls.popup.notificationpopup.MyNotificationPopupConfigBuilder;
import libgdx.controls.popup.notificationpopup.MyNotificationPopupCreator;
import libgdx.controls.textfield.MyTextFieldBuilder;
import libgdx.dbapi.GameStatsDbApiService;
import libgdx.game.Game;
import libgdx.implementations.balloon.BalloonScreenManager;
import libgdx.implementations.balloon.logic.MainViewCreator;
import libgdx.implementations.balloon.logic.MatrixCoordinatesUtils;
import libgdx.implementations.balloon.logic.MatrixCreator;
import libgdx.implementations.balloon.model.CurrentLevel;
import libgdx.implementations.balloon.model.LevelInfo;
import libgdx.resources.dimen.MainDimen;
import libgdx.resources.gamelabel.MainGameLabel;
import libgdx.screen.AbstractScreen;
import libgdx.utils.DateUtils;
import libgdx.utils.Utils;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;

public class BalloonGameScreen extends AbstractScreen<BalloonScreenManager> {

    private MainViewCreator mainViewCreator;
    private LevelInfo levelInfo;
    private CurrentLevel currentLevel;

    public BalloonGameScreen(LevelInfo levelInfo) {
        this.levelInfo = levelInfo;

//        Integer nrOfRowsForMatrix = 8;
//        Integer nrOfColumnsForMatrix = 16;
//        nrOfRowsForMatrix = 5;
//        nrOfColumnsForMatrix = 10;
        Integer nrOfRowsForMatrix = levelInfo.getNrOfRowsForMatrix();
        Integer nrOfColumnsForMatrix = levelInfo.getNrOfColumnsForMatrix();
        MatrixCreator matrixCreator = new MatrixCreator(nrOfRowsForMatrix, nrOfColumnsForMatrix);
        int[][] matrix = getMatrix(matrixCreator);

        currentLevel = new CurrentLevel();
        currentLevel.setLevelMatrix(matrix);
        currentLevel.setPlanes(matrixCreator.findPlanesInMatrix(matrix));
        currentLevel.getStartPositionColumnsForPlayer1().addAll(matrixCreator.startPositionList());
        currentLevel.getStartPositionColumnsForPlayer2().addAll(matrixCreator.startPositionList());
        currentLevel.setIsPlayer1Turn(levelInfo.decideWhatPlayerStarts());
        currentLevel.setOnePlayerLevel(levelInfo.isOnePlayerLevel());
        currentLevel.setPlayer2ComputerMovesRandom(levelInfo.isEasyLevel());
        currentLevel.setPlayer2Computer(levelInfo.isPlayer2Computer());

        mainViewCreator = new MainViewCreator(nrOfRowsForMatrix, nrOfColumnsForMatrix, levelInfo, currentLevel, getAbstractScreen());
    }

    @Override
    public void buildStage() {
        if (currentLevel.isPlayer2Computer()) {
            addAction(Actions.sequence(Actions.delay(2.8f)));
        }
        addActor(mainViewCreator.createGameRowsContainer());
        mainViewCreator.createDisplayOfMatrix(currentLevel.getLevelMatrix());
        mainViewCreator.refreshScore();
        mainViewCreator.processFirstPlayerActions();
        decideWhatContainerToBeShown();
        if (!levelInfo.isOnePlayerLevel()) {
            addAction(Actions.sequence(Actions.delay(.3f), Utils.createRunnableAction(new Runnable() {
                @Override
                public void run() {
                    toastDisplayWhichPlayerStarts();
                }
            })));
        }
    }

    private int[][] getMatrix(MatrixCreator matrixCreator) {
        int[][] matrix = null;
        if (!levelInfo.isMultiplayer()) {
            matrix = MatrixCoordinatesUtils.cloneArray(levelInfo.getLevelEnum().getMatrix());
            if (matrix == null) {
                matrix = matrixCreator.getCreatedMatrix();
            }
        } else {
            matrix = matrixCreator.getCreatedMatrix();
        }
        return matrix;
    }

    private void decideWhatContainerToBeShown() {
        if (!levelInfo.isMultiplayer() && levelInfo.getLevelEnum().isOnePlayerLevel()) {
            getRoot().findActor(MainViewCreator.PL_2_CONTAINER_NAME).setVisible(false);
        }
    }

    private void toastDisplayWhichPlayerStarts() {
        int playerNr = currentLevel.isPlayer1Turn() ? 1 : 2;

        String text = MainGameLabel.l_player_starts.getText(playerNr + "");
        if (!levelInfo.isMultiplayer()) {
            text = playerNr == 1 ? MainGameLabel.l_you_start.getText() : MainGameLabel.l_opponent_starts.getText();
        }
        MyNotificationPopupConfigBuilder myNotificationPopupConfigBuilder = new MyNotificationPopupConfigBuilder()
                .setText(text)
                .setTransferBetweenScreens(false);
        FontColor fontColor = FontColor.DARK_RED;
        if (playerNr == 2) {
            fontColor = FontColor.YELLOW;
        }
        myNotificationPopupConfigBuilder.setFontConfig(new FontConfig(fontColor.getColor(), FontColor.BLACK.getColor(), FontConfig.FONT_SIZE * 3, 3f));
        new MyNotificationPopupCreator(myNotificationPopupConfigBuilder.build()).shortNotificationPopup().addToPopupManager();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        Utils.createChangeLangPopup();

//        if (Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_LEFT)) {
//            screenManager.showLevelFinishedScreen(levelInfo, 250, 235);
//        }
    }

    @Override
    public void onBackKeyPress() {
        if (!levelInfo.isMultiplayer() && levelInfo.getLevelEnum().getStageNr() > 0) {
            screenManager.showCampaignScreen(levelInfo.getLevelEnum().getStageNr());
        } else {
            screenManager.showMainScreen();
        }
    }

}
