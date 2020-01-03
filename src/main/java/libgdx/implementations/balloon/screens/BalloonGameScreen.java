package libgdx.implementations.balloon.screens;

import com.badlogic.gdx.Gdx;

import libgdx.implementations.balloon.BalloonCampaignLevelEnum;
import libgdx.implementations.balloon.BalloonScreenManager;
import libgdx.implementations.balloon.logic.MainViewCreator;
import libgdx.implementations.balloon.logic.MatrixCoordinatesUtils;
import libgdx.implementations.balloon.logic.MatrixCreator;
import libgdx.implementations.balloon.model.CurrentLevel;
import libgdx.implementations.balloon.model.LevelInfo;
import libgdx.screen.AbstractScreen;
import libgdx.utils.Utils;

public class BalloonGameScreen extends AbstractScreen<BalloonScreenManager> {

    private BalloonCampaignLevelEnum levelEnum;
    private MainViewCreator mainViewCreator;
    private LevelInfo levelInfo;
    private CurrentLevel currentLevel;

    public BalloonGameScreen(BalloonCampaignLevelEnum levelEnum) {
        this.levelInfo = new LevelInfo(false, levelEnum);

        MatrixCreator matrixCreator = new MatrixCreator(levelInfo.getNrOfRowsForMatrix(), levelInfo.getNrOfColumnsForMatrix());
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

        mainViewCreator = new MainViewCreator(currentLevel, levelInfo, getAbstractScreen());
    }

    @Override
    public void buildStage() {
        mainViewCreator = new MainViewCreator(currentLevel, levelInfo, this);
        addActor(mainViewCreator.createGameRowsContainer());
        mainViewCreator.createDisplayOfMatrix(currentLevel.getLevelMatrix());
//        mainViewCreator.refreshScore();
        mainViewCreator.isPlayer2First();
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
//            TextView currentLevel = (TextView) findViewById(R.id.currentLevel);
//            int levelNrToDisplay = levelInfo.getLevelEnum().getLevelNr() + 1;
//            int stageNrToDisplay = levelInfo.getLevelEnum().getStageNr() + 1;
//            currentLevel.setText(levelNrToDisplay + " - " + stageNrToDisplay);
        }
    }

    private void decideWhatContainerToBeShown() {
        if (!levelInfo.isMultiplayer() && levelInfo.getLevelEnum().isOnePlayerLevel()) {
//            findViewById(R.id.scorePlayer2).setVisibility(View.GONE);
//            findViewById(R.id.scorePlayer2Img).setVisibility(View.GONE);
        }
    }

    private void toastDisplayWhichPlayerStarts() {
        int playerNr = currentLevel.isPlayer1Turn() ? 1 : 2;

//        Toast toast = Toast.makeText(this, null, Toast.LENGTH_LONG);
//        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
//
//        String color = RED;
//        if (!currentLevel.isPlayer1Turn()) {
//            color = YELLOW;
//        }
//        v.setText(Html.fromHtml(fontString(getResources().getString(R.string.player_label) + " " + playerNr, false, color) + " "
//                + getResources().getString(R.string.player_start)));
//        toast.show();
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
