package libgdx.implementations.balloon.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.popup.notificationpopup.MyNotificationPopupConfig;
import libgdx.controls.popup.notificationpopup.MyNotificationPopupConfigBuilder;
import libgdx.controls.popup.notificationpopup.MyNotificationPopupCreator;
import libgdx.controls.popup.notificationpopup.ShortNotificationPopup;
import libgdx.implementations.balloon.BalloonCampaignLevelEnum;
import libgdx.implementations.balloon.BalloonScreenManager;
import libgdx.implementations.balloon.logic.MainViewCreator;
import libgdx.implementations.balloon.logic.MatrixCoordinatesUtils;
import libgdx.implementations.balloon.logic.MatrixCreator;
import libgdx.implementations.balloon.model.CurrentLevel;
import libgdx.implementations.balloon.model.LevelInfo;
import libgdx.resources.FontManager;
import libgdx.resources.gamelabel.SpecificPropertiesUtils;
import libgdx.screen.AbstractScreen;
import libgdx.utils.Utils;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;

public class BalloonGameScreen extends AbstractScreen<BalloonScreenManager> {

    private MainViewCreator mainViewCreator;
    private LevelInfo levelInfo;
    private CurrentLevel currentLevel;

    public BalloonGameScreen(BalloonCampaignLevelEnum levelEnum) {
        this.levelInfo = new LevelInfo(false, levelEnum);

        Integer nrOfRowsForMatrix = 8;
        Integer nrOfColumnsForMatrix = 16;
//        Integer nrOfRowsForMatrix = 4;
//        Integer nrOfColumnsForMatrix = 8;
//        Integer nrOfRowsForMatrix = levelInfo.getNrOfRowsForMatrix();
//        Integer nrOfColumnsForMatrix = levelInfo.getNrOfColumnsForMatrix();
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

        mainViewCreator = new MainViewCreator(nrOfRowsForMatrix, nrOfColumnsForMatrix, currentLevel, levelInfo, getAbstractScreen());
    }

    @Override
    public void buildStage() {
        addActor(mainViewCreator.createGameRowsContainer());
        mainViewCreator.createDisplayOfMatrix(currentLevel.getLevelMatrix());
//        mainViewCreator.refreshScore();
        mainViewCreator.isPlayer2First();
        addAction(Actions.sequence(Actions.delay(1),Utils.createRunnableAction(new Runnable() {
            @Override
            public void run() {
                toastDisplayWhichPlayerStarts();
            }
        })));
    }

    private int[][] getMatrix(MatrixCreator matrixCreator) {
        int[][] matrix = null;
        if (!levelInfo.isMultiplayer()) {
//            matrix = MatrixCoordinatesUtils.cloneArray(levelInfo.getLevelEnum().getMatrix());
//            if (matrix == null) {
            matrix = matrixCreator.getCreatedMatrix();
//            }
        } else {
            matrix = matrixCreator.getCreatedMatrix();
        }
        return matrix;
    }

    private void displayCurrentStageAndLevel() {
        if (!levelInfo.isMultiplayer()) {
            MyWrappedLabel levelInfoLabel = getRoot().findActor(MainViewCreator.LVLINFO_NAME);
            int levelNrToDisplay = levelInfo.getLevelEnum().getLevelNr() + 1;
            int stageNrToDisplay = levelInfo.getLevelEnum().getStageNr() + 1;
            levelInfoLabel.setText(levelNrToDisplay + " - " + stageNrToDisplay);
        }
    }

    private void decideWhatContainerToBeShown() {
        if (!levelInfo.isMultiplayer() && levelInfo.getLevelEnum().isOnePlayerLevel()) {
            getRoot().findActor(MainViewCreator.PL_2_CONTAINER_NAME).setVisible(false);
        }
    }

    private void toastDisplayWhichPlayerStarts() {
        int playerNr = currentLevel.isPlayer1Turn() ? 1 : 2;

        MyNotificationPopupConfigBuilder myNotificationPopupConfigBuilder = new MyNotificationPopupConfigBuilder().setText(
                "Player " + playerNr + " starts");
        myNotificationPopupConfigBuilder.setFontScale(FontManager.getBigFontDim());
        if (playerNr == 1) {
            myNotificationPopupConfigBuilder.setTextColor(FontColor.DARK_RED);
        } else {
            myNotificationPopupConfigBuilder.setTextColor(FontColor.YELLOW);
        }
        new MyNotificationPopupCreator(myNotificationPopupConfigBuilder.build()).shortNotificationPopup().addToPopupManager();
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
