package libgdx.implementations.balloon.logic;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.resources.gamelabel.MainGameLabel;

import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import libgdx.campaign.CampaignService;
import libgdx.controls.animations.ActorAnimation;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.game.Game;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.balloon.BalloonGame;
import libgdx.implementations.balloon.BalloonSpecificResource;
import libgdx.implementations.balloon.model.CurrentLevel;
import libgdx.implementations.balloon.model.LevelInfo;
import libgdx.implementations.balloon.model.MatrixValue;
import libgdx.implementations.balloon.model.MatrixWithChangedCells;
import libgdx.implementations.balloon.model.PlayerPosition;
import libgdx.implementations.balloon.screens.BalloonCampaignScreen;
import libgdx.resources.FontManager;
import libgdx.resources.Res;
import libgdx.resources.dimen.MainDimen;
import libgdx.screen.AbstractScreen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.SoundUtils;
import libgdx.utils.Utils;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;

public class MainViewCreator {

    private static final int NR_OF_TRIES_TO_FIND_A_CORRECT_MOVEMENT = 200;

    public final static int ROW_ID_STARTING_INT_VALUE = 200;
    public final static float BALLOON_PAUSE_INTERVAL = 0.2f;
    public final static float PLAYER2_PAUSE_INTERVAL = 0.8f;
    public static final String PL_2_CONTAINER_NAME = "PL2CONTAINER";
    public static final String LVLINFO_NAME = "LVLINFO_NAME";
    private int imageViewIdForFinalBalloonPosition = 777;

    private final static String SCOREPLAYER = "SCOREPLAYER";

    private ImageManager imageManager;

    private CurrentLevel currentLevel;
    private LevelInfo levelInfo;

    private int nrOfRows;
    private int nrOfCols;

    private float cellDimen;

    private MatrixCoordinatesUtils mcu;

    private AbstractScreen abstractScreen;

    private CampaignService campaignService;

    private MyButton backBtn;


    public MainViewCreator(int nrOfRows, int nrOfCols, LevelInfo levelInfo, CurrentLevel currentLevel, AbstractScreen screen) {
        this.nrOfRows = nrOfRows;
        this.nrOfCols = nrOfCols;
        this.currentLevel = currentLevel;
        this.levelInfo = levelInfo;
        this.abstractScreen = screen;
        campaignService = new CampaignService();
        imageManager = new ImageManager();
        mcu = new MatrixCoordinatesUtils(nrOfCols, nrOfRows);
        cellDimen = ScreenDimensionsManager.getScreenWidthValue(100 / nrOfCols);
        backBtn = new BackButtonBuilder().addHoverBackButton(screen);
    }

    public Table createGameRowsContainer() {
        List<Table> gameRows = createGameRows();
        Table container = new Table();
        float pad = MainDimen.vertical_general_margin.getDimen();
        container.add(createHeader()).padBottom(pad / 2).padLeft(pad).padRight(pad).padTop(pad).grow().row();
        container.setFillParent(true);
        for (Table row : gameRows) {
            container.add(row).grow().row();
        }
        return container;
    }

    private Table createHeader() {
        Table header = new Table();
        header.setBackground(GraphicUtils.getNinePatch(BalloonSpecificResource.light_blue_backgr));
        header.add(createPlContainer(true, BalloonSpecificResource.balloonp1, "20", 1)).growX();
        header.add(createLevelInfo()).growX();
        Table pl2Container = createPlContainer(false, BalloonSpecificResource.balloonp2, "20", 2);
        pl2Container.setName(PL_2_CONTAINER_NAME);
        header.add(pl2Container).growX();
        return header;
    }

    private Table createLevelInfo() {
        if (!levelInfo.isMultiplayer()) {
            Table table = new Table();
            int levelNrToDisplay = levelInfo.getLevelEnum().getLevelNr() + 1;
            Table cont = new Table();
            FontColor stageColor = FontColor.GREEN;
            if (levelInfo.getLevelEnum().getStageNr() == 1) {
                stageColor = FontColor.LIGHT_BLUE;
            } else if (levelInfo.getLevelEnum().getStageNr() == 2) {
                stageColor = FontColor.RED;
            }
            cont.add(getLvlInfoLabel(BalloonCampaignScreen.getStageTitle(levelInfo.getLevelEnum().getStageNr()), stageColor)).padRight(MainDimen.horizontal_general_margin.getDimen() * 2);
            //Is tutorial
            if (levelInfo.getLevelEnum().getStageNr() != 0) {
                cont.add(getLvlInfoLabel(MainGameLabel.l_level.getText(levelNrToDisplay + ""), FontColor.LIGHT_GREEN));
            }
            table.add(cont);
            return table;
        }
        return new Table();
    }

    private MyWrappedLabel getLvlInfoLabel(String text, FontColor fontColor) {
        return new MyWrappedLabel(new MyWrappedLabelConfigBuilder().setWidth(ScreenDimensionsManager.getScreenWidthValue(20))
                .setFontConfig(new FontConfig(fontColor.getColor(), FontColor.BLACK.getColor(), Math.round(FontConfig.FONT_SIZE * (text.length() > 7 ? 2f : 2.5f)), 4f))
                .setText(text).build());
    }

    private Table createPlContainer(boolean displayIconFirst, BalloonSpecificResource
            plIcon, String score, int playerNr) {
        Table plContainer = new Table();
        MyWrappedLabel plScore = new MyWrappedLabel(score, FontConfig.FONT_SIZE * 3);
        plScore.setName(SCOREPLAYER + playerNr);
        float dimen = MainDimen.horizontal_general_margin.getDimen();
        float iconDimen = dimen * 2;
        if (displayIconFirst) {
            plContainer.add(GraphicUtils.getImage(plIcon)).width(iconDimen).height(iconDimen);
        }
        plContainer.add(plScore).padLeft(dimen).padRight(dimen);
        if (!displayIconFirst) {
            plContainer.add(GraphicUtils.getImage(plIcon)).width(iconDimen).height(iconDimen);
        }
        return plContainer;
    }

    private List<Table> createGameRows() {
        List<Table> rows = new ArrayList<Table>();
        for (int i = 0; i < nrOfRows; i++) {
            Table row = new Table();
            row.setName(String.valueOf(ROW_ID_STARTING_INT_VALUE + i));
            rows.add(row);
        }
        return rows;
    }

    public List<Table> getCreatedRows() {
        List<Table> rows = new ArrayList<Table>();
        for (int i = 0; i < nrOfRows; i++) {
            Table row = abstractScreen.getRoot().findActor(String.valueOf(ROW_ID_STARTING_INT_VALUE + i));
            rows.add(row);
        }
        return rows;
    }

    public Table getCreatedRow(int rowNr) {
        return getCreatedRows().get(rowNr);
    }

    public void createDisplayOfMatrix(int[][] matrix) {
        int row = 0;
        updateMatrixWithStartPositionForPlayer();
        for (Table viewRow : getCreatedRows()) {
            viewRow.clearChildren();
            for (int col = 0; col < nrOfCols; col++) {
                Table img = createImgView(matrix[row][col], col, row);
                img.setBackground(getBackgroundForColumn(col));
                viewRow.add(img).width(cellDimen).height(cellDimen);
            }
            row++;
        }
    }


    private NinePatchDrawable getBackgroundForColumn(int col) {
        Res colColor = BalloonSpecificResource.air;
        if (col % 2 == 0) {
            colColor = BalloonSpecificResource.light_green_backgr;
        }
        return GraphicUtils.getNinePatch(colColor);
    }

    private void updateMatrixWithStartPositionForPlayer() {
        int value = currentLevel.isPlayer1Turn() ? MatrixValue.PLAYER_1.getValue() : MatrixValue.PLAYER_2.getValue();
        Set<Integer> startPositions = currentLevel.isPlayer1Turn() ? currentLevel.getStartPositionColumnsForPlayer1() : currentLevel
                .getStartPositionColumnsForPlayer2();
        if (currentLevel.isPlayer1Turn()
                && currentLevel.getStartPositionColumnsForPlayer1().size() == nrOfCols
                && levelInfo.getLevelEnum().getStageNr() == 0) {
            abstractScreen.addAction(Actions.sequence(Actions.delay(1f), Utils.createRunnableAction(new Runnable() {
                @Override
                public void run() {
                    float iconDimenHalf = MainDimen.horizontal_general_margin.getDimen() * 5;
                    ActorAnimation.pressFinger(
                            ScreenDimensionsManager.getScreenWidth() / 2 - iconDimenHalf,
                            -iconDimenHalf);
                }
            })));

        }
        resetFirstRowFromMatrix();
        int firstRowIndex = mcu.getFirstRowIndex();
        for (int j = 0; j < nrOfCols; j++) {
            if (startPositions.contains(j)) {
                currentLevel.getLevelMatrix()[firstRowIndex][j] = value;
            }
        }
    }

    private void refreshDisplayOfMatrix(Set<MutablePair<Integer, Integer>> cellsToUpdate,
                                        int[][] matrix) {
        updateMatrixWithStartPositionForPlayer();
        for (MutablePair<Integer, Integer> point : cellsToUpdate) {
            Table img = createImgView(matrix[point.right][point.left], point.left, point.right);
            Table createdRow = getCreatedRow(point.right);
            Table imgTableToUpdate = (Table) createdRow.getChildren().get(point.left);
            imgTableToUpdate.clearChildren();
            imgTableToUpdate.add(img).grow();
        }
    }

    public void refreshScore() {
        MyWrappedLabel scorePlayer1 = (MyWrappedLabel) abstractScreen.getRoot().findActor(SCOREPLAYER + 1);
        scorePlayer1.setText(calculateScore(currentLevel.getFinalPositionPairsForPlayer1().values()) + "");
        MyWrappedLabel scorePlayer2 = (MyWrappedLabel) abstractScreen.getRoot().findActor(SCOREPLAYER + 2);
        scorePlayer2.setText(calculateScore(currentLevel.getFinalPositionPairsForPlayer2().values()) + "");
        backBtn.toFront();
    }

    public void processFirstPlayerActions() {
        if (!currentLevel.isOnePlayerLevel() && currentLevel.isPlayer2Computer() && !currentLevel.isPlayer1Turn()) {
            togglePlayer(true);
        }
    }

    private int calculateScore(Collection<Integer> collection) {
        int score = 0;
        for (Integer integer : collection) {
            score = score + integer;
        }
        return score;
    }

    private void resetFirstRowFromMatrix() {
        int firstRowIndex = mcu.getFirstRowIndex();
        for (int j = 0; j < nrOfCols; j++) {
            currentLevel.getLevelMatrix()[firstRowIndex][j] = MatrixValue.AIR.getValue();
        }
    }

    private Table createImgView(final int mtrxVal, final int selectedColumn, final int row) {
        Table image = null;
        if (mtrxVal == MatrixValue.FINAL_PLAYER_1.getValue()) {
            image = imageManager.getFinalPositionImageWithPoints(currentLevel.getFinalPositionPairsForPlayer1().get(new MutablePair<Integer, Integer>(selectedColumn, row)),
                    MatrixValue.FINAL_PLAYER_1, nrOfCols);
            imageViewIdForFinalBalloonPosition = imageViewIdForFinalBalloonPosition + 1;
        } else if (mtrxVal == MatrixValue.FINAL_PLAYER_2.getValue()) {
            image = imageManager.getFinalPositionImageWithPoints(currentLevel.getFinalPositionPairsForPlayer2().get(new MutablePair<Integer, Integer>(selectedColumn, row)),
                    MatrixValue.FINAL_PLAYER_2, nrOfCols);
            imageViewIdForFinalBalloonPosition = imageViewIdForFinalBalloonPosition + 1;
        } else {
            image = imageManager.getImage(MatrixValue.getMatrixValue(mtrxVal));
        }
        image.setTouchable(Touchable.enabled);
        image.addListener(
                new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        if (currentLevel.getLevelMatrix()[row][selectedColumn] != 0 && (!currentLevel.isPlayer2Computer() || (currentLevel.isPlayer2Computer() && currentLevel.isPlayer1Turn()))) {
                            clickBalloon(selectedColumn, mtrxVal);
                        }
                    }
                });
        return image;
    }

    private void putFinalPositionForPlayer(MutablePair<Integer, Integer> finalPosition) {
        if (currentLevel.isPlayer1Turn()) {
            currentLevel.getFinalPositionPairsForPlayer1().put(new MutablePair<Integer, Integer>(finalPosition.left, finalPosition.right),
                    currentLevel.getCurrentMove().getMovementFinishedInfo().getScore());
        } else {
            currentLevel.getFinalPositionPairsForPlayer2().put(new MutablePair<Integer, Integer>(finalPosition.left, finalPosition.right),
                    currentLevel.getCurrentMove().getMovementFinishedInfo().getScore());
        }
    }

    private void clickBalloon(int clickedColumn, int cellValue) {
        boolean isPlayerClicked = isPlayerValue(cellValue);
        if (isPlayerClicked && currentLevel.getCurrentMove().isMovementStopped()) {
            currentLevel.getCurrentMove().setClickedColumn(clickedColumn);

            MutablePair<Integer, Integer> currentPosition = new MutablePair<Integer, Integer>(clickedColumn, mcu.getFirstRowIndex());
            currentLevel.getCurrentMove().setPlayerPosition(mcu.fillCurrentPositionInfo(currentPosition, currentLevel.getLevelMatrix()));
            resetState();
            moveElement();
        }
    }

    private boolean isPlayerValue(int cellValue) {
        return cellValue == MatrixValue.PLAYER_1.getValue() || cellValue == MatrixValue.PLAYER_2.getValue();
    }

    private void movePlanes() {
        for (MutablePair<Integer, Integer> plane : currentLevel.getPlanes()) {
            updatePlaneNeighbours(plane);
            processOldPlanePosition(plane);
            movePlaneRight(plane);
        }
        for (MutablePair<Integer, Integer> plane : currentLevel.getPlanes()) {
            if (currentLevel.getLevelMatrix()[plane.right][plane.left] == MatrixValue.AIR.getValue()) {
                currentLevel.getLevelMatrix()[plane.right][plane.left] = MatrixValue.PLANE.getValue();
                currentLevel.getCurrentMove().getMovementFinishedInfo().addCellToUpdate(plane);
            }
        }
    }

    private void movePlaneRight(MutablePair<Integer, Integer> plane) {
        if (plane.left + 1 <= nrOfCols - 1) {
            plane.left = plane.left + 1;
        } else {
            plane.left = 0;
        }
    }

    private void processOldPlanePosition(MutablePair<Integer, Integer> plane) {
        int planePositionValue = currentLevel.getLevelMatrix()[plane.right][plane.left];
        if (planePositionValue == MatrixValue.PLANE.getValue()) {
            currentLevel.getLevelMatrix()[plane.right][plane.left] = MatrixValue.AIR.getValue();
        }
        currentLevel.getCurrentMove().getMovementFinishedInfo().addCellToUpdate(plane);
    }

    private void updatePlaneNeighbours(MutablePair<Integer, Integer> plane) {
        MatrixWithChangedCells matrixWithChangedCells = mcu.verifyPlaneNeighbours(plane, currentLevel.getLevelMatrix(), currentLevel.isPlayer1Turn());
        currentLevel.getCurrentMove().getMovementFinishedInfo().addCellsToUpdate(matrixWithChangedCells.getCellsToUpdate());
        currentLevel.setLevelMatrix(matrixWithChangedCells.getLeveltMatrix());
    }


    private void moveElement() {
        int tries = 0;
        while (!simulateMovementAndVerifyIfItsCorrect(tries, currentLevel.getCurrentMove().getPlayerPosition().copy(),
                MatrixCoordinatesUtils.cloneArray(currentLevel.getLevelMatrix()))) {
            tries++;
        }
        int nrOfMoves = currentLevel.getCurrentMove().getMovementFinishedInfo().getNrOfMoves();
        final MutableInt indx = new MutableInt(0);
        currentLevel.getCurrentMove().getMovementFinishedInfo().setDestroyed(false);
        removePlayerStartPosition(currentLevel.getCurrentMove().getClickedColumn());
        Action[] array = new Action[nrOfMoves];
        for (int i = 0; i < nrOfMoves; i++) {
            array[i] = Actions.sequence(createMoveRunnable(i), Actions.delay(BALLOON_PAUSE_INTERVAL));
        }
        abstractScreen.addAction(Actions.sequence(Actions.sequence(array), Utils.createRunnableAction(new Runnable() {
            @Override
            public void run() {
                movePlanes();
                finalPositionUpdate();

                addFirstRowToCellUpdate();
                refreshScore();
                currentLevel.getCurrentMove().setMovementStopped(true);

                togglePlayer(false);

                refreshDisplayOfMatrix(currentLevel.getCurrentMove().getMovementFinishedInfo().getCellsToUpdate(), currentLevel.getLevelMatrix());

                if (isLevelFinished()) {
                    levelFinished(calculateScore(currentLevel.getFinalPositionPairsForPlayer1().values()), calculateScore(currentLevel
                            .getFinalPositionPairsForPlayer2().values()));
                }
            }
        })));
    }

    private RunnableAction createMoveRunnable(final int indx) {
        return Utils.createRunnableAction(new Runnable() {
            @Override
            public void run() {
                playSound(indx);
                startElemMovement();
                refreshDisplayOfMatrix(currentLevel.getCurrentMove().getMovementFinishedInfo().getCellsToUpdate(), currentLevel.getLevelMatrix());
                currentLevel.getCurrentMove().getMovementFinishedInfo().getCellsToUpdate().clear();
            }
        });
    }

    private boolean isLevelFinished() {
        boolean levelFinished = false;
        if (currentLevel.getCurrentMove().isMovementStopped() && currentLevel.getStartPositionColumnsForPlayer1().isEmpty()) {
            if (currentLevel.isOnePlayerLevel()) {
                levelFinished = true;
            } else if (!currentLevel.isOnePlayerLevel() && currentLevel.getStartPositionColumnsForPlayer2().isEmpty()) {
                levelFinished = true;
            }
        }
        return levelFinished;
    }

    private void addFirstRowToCellUpdate() {
        int firstRowIndex = mcu.getFirstRowIndex();
        for (int j = 0; j < nrOfCols; j++) {
            currentLevel.getCurrentMove().getMovementFinishedInfo().addCellToUpdate(new MutablePair<Integer, Integer>(j, firstRowIndex));
        }
    }

    private void finalPositionUpdate() {
        if (!currentLevel.getCurrentMove().getMovementFinishedInfo().isDestroyed()) {
            MutablePair<Integer, Integer> finalPosition = currentLevel.getCurrentMove().getMovementFinishedInfo().getFinalPosition();
            currentLevel.getLevelMatrix()[finalPosition.right][finalPosition.left] = MatrixCoordinatesUtils.getFinalPositionForPlayer(currentLevel.isPlayer1Turn())
                    .getValue();
            currentLevel.getCurrentMove().getMovementFinishedInfo().addCellToUpdate(finalPosition);

            if (!currentLevel.getCurrentMove().getMovementFinishedInfo().isDestroyed()) {
                putFinalPositionForPlayer(finalPosition);
            }
        }
    }

    private void removePlayerStartPosition(int clickedColumn) {
        if (currentLevel.isPlayer1Turn()) {
            currentLevel.getStartPositionColumnsForPlayer1().remove(clickedColumn);
        } else {
            currentLevel.getStartPositionColumnsForPlayer2().remove(clickedColumn);
        }
    }

    private void startElemMovement() {
        Set<MutablePair<Integer, Integer>> cellsToUpdate = new HashSet<MutablePair<Integer, Integer>>();

        int firstRowIndex = mcu.getFirstRowIndex();
        if (currentLevel.getCurrentMove().getPlayerPosition().getCurrentPosition().getPoint().right == firstRowIndex) {
            cellsToUpdate.add(new MutablePair<Integer, Integer>(currentLevel.getCurrentMove().getClickedColumn(), firstRowIndex));
        }

        MutablePair<Integer, Integer> nextPosition = calculateNextPosition(currentLevel.getCurrentMove().getPlayerPosition());
        if (nextPosition != null) {
            cellsToUpdate.add(nextPosition);
            cellsToUpdate.add(currentLevel.getCurrentMove().getPlayerPosition().getCurrentPosition().getPoint());
            cellsToUpdate.add(currentLevel.getCurrentMove().getPlayerPosition().getUpValue().getPoint());

            currentLevel.setLevelMatrix(mcu.movePlayer(currentLevel.getCurrentMove().getPlayerPosition(), nextPosition, currentLevel.getLevelMatrix(),
                    currentLevel.isPlayer1Turn()));

            currentLevel.getCurrentMove().setPlayerPosition(mcu.fillCurrentPositionInfo(nextPosition, currentLevel.getLevelMatrix()));
        }
        cellsToUpdate.addAll(processDestroyedBalloon());
        currentLevel.getCurrentMove().getMovementFinishedInfo().addCellsToUpdate(cellsToUpdate);
    }

    private void playSound(int index) {
        int row = currentLevel.getCurrentMove().getPlayerPosition().getCurrentPosition().getPoint().right;
        List<Integer> soundsToPlayInOrder = currentLevel.getCurrentMove().getMovementFinishedInfo().getSoundsToPlayInOrder();
        int cellValue = soundsToPlayInOrder.get(index);
        SoundUtils.playSound(BalloonSpecificResource.getSoundForPosition(row, cellValue));
    }

    private Set<MutablePair<Integer, Integer>> processDestroyedBalloon() {
        Set<MutablePair<Integer, Integer>> cellsToUpdate = new HashSet<MutablePair<Integer, Integer>>();
        if (currentLevel.getCurrentMove().getMovementFinishedInfo().isDestroyed()) {
            currentLevel.getLevelMatrix()[currentLevel.getCurrentMove().getMovementFinishedInfo().getFinalPosition().right][currentLevel.getCurrentMove()
                    .getMovementFinishedInfo().getFinalPosition().left] = MatrixCoordinatesUtils.getDestroyedPlayer(currentLevel.isPlayer1Turn()).getValue();
            cellsToUpdate.add(currentLevel.getCurrentMove().getMovementFinishedInfo().getFinalPosition());
        }
        return cellsToUpdate;
    }

    private boolean simulateMovementAndVerifyIfItsCorrect(int tries, PlayerPosition
            playerPositionCopy, int[][] clonedMatrix) {
        int nrOfMoves = 0;

        MutablePair<Integer, Integer> point = playerPositionCopy.getCurrentPosition().getPoint();
        MutablePair<Integer, Integer> finalPosition = new MutablePair<Integer, Integer>(point.left, point.right);
        MutablePair<Integer, Integer> nextPosition = calculateNextPosition(playerPositionCopy);

        while (nextPosition != null) {

            incrementPointsForCurrentMove(clonedMatrix, nextPosition);

            if (!mcu.verifyMoveValidity(nextPosition, clonedMatrix, playerPositionCopy, currentLevel.getCurrentMove().getCurrentRandomTornadoNextPosition())
                    && tries < NR_OF_TRIES_TO_FIND_A_CORRECT_MOVEMENT) {
                resetState();
                return false;
            }

            clonedMatrix = mcu.movePlayer(playerPositionCopy, nextPosition, clonedMatrix, currentLevel.isPlayer1Turn());

            finalPosition.left = nextPosition.left;
            finalPosition.right = nextPosition.right;

            playerPositionCopy = mcu.fillCurrentPositionInfo(nextPosition, clonedMatrix);

            if (!mcu.verifyMoveValidity(nextPosition, clonedMatrix, playerPositionCopy, currentLevel.getCurrentMove().getCurrentRandomTornadoNextPosition())
                    && tries < NR_OF_TRIES_TO_FIND_A_CORRECT_MOVEMENT) {
                resetState();
                return false;
            }

            nextPosition = calculateNextPosition(playerPositionCopy);

            nrOfMoves++;
        }

        boolean playerDestroyedAfterTornado = currentLevel.getCurrentMove().getMovementFinishedInfo().isDestroyed()
                && currentLevel.getCurrentMove().getCurrentRandomTornadoNextPosition() != null;
        if ((finalPosition.equals(currentLevel.getCurrentMove().getCurrentRandomTornadoNextPosition()) || playerDestroyedAfterTornado)
                && tries < NR_OF_TRIES_TO_FIND_A_CORRECT_MOVEMENT) {
            resetState();
            return false;
        }

        if (currentLevel.getCurrentMove().getMovementFinishedInfo().isDestroyed()) {
            clonedMatrix[finalPosition.right][finalPosition.left] = MatrixCoordinatesUtils.getDestroyedPlayer(currentLevel.isPlayer1Turn()).getValue();
            currentLevel.getCurrentMove().getMovementFinishedInfo().getSoundsToPlayInOrder().add(MatrixValue.DESTROYED_PLAYER_1.getValue());
            nrOfMoves++;
        }

        currentLevel.getCurrentMove().getMovementFinishedInfo().setNrOfMoves(nrOfMoves);
        currentLevel.getCurrentMove().getMovementFinishedInfo().setFinalPosition(finalPosition);

        return true;
    }

    private int calculateBestColumnForPlayer() {
        Map<Integer, Integer> colsAndScores = new HashMap<Integer, Integer>();
        int[][] cloneArray = MatrixCoordinatesUtils.cloneArray(currentLevel.getLevelMatrix());
        resetState();
        for (Integer i : currentLevel.getStartPositionColumnsForPlayer2()) {
            MutablePair<Integer, Integer> currentPosition = new MutablePair<Integer, Integer>(i, mcu.getFirstRowIndex());
            PlayerPosition playerPosition = mcu.fillCurrentPositionInfo(currentPosition, cloneArray);

            int tries = 0;
            while (!simulateMovementAndVerifyIfItsCorrect(tries, playerPosition, cloneArray)) {
                tries++;
            }

            int score = currentLevel.getCurrentMove().getMovementFinishedInfo().getScore();
            if (currentLevel.getCurrentMove().getMovementFinishedInfo().isDestroyed()) {
                score = -1;
            }
            colsAndScores.put(i, score);
            resetState();
        }
        return mcu.bestColumnForPlayer(colsAndScores, currentLevel.getStartPositionColumnsForPlayer2());
    }

    private void togglePlayer(boolean isFirstTurn) {
        if (!currentLevel.isOnePlayerLevel() && !isFirstTurn) {
            currentLevel.setIsPlayer1Turn(!currentLevel.isPlayer1Turn());
        }
        if (!currentLevel.isPlayer1Turn() && !currentLevel.isOnePlayerLevel() && currentLevel.isPlayer2Computer()
                && !currentLevel.getStartPositionColumnsForPlayer2().isEmpty()) {

            int bestColumnForPlayer2 = 0;
            if (!currentLevel.isPlayer2ComputerMovesRandom()) {
                bestColumnForPlayer2 = calculateBestColumnForPlayer();
            } else {
                bestColumnForPlayer2 = new ArrayList<Integer>(currentLevel.getStartPositionColumnsForPlayer2()).get(new Random().nextInt(currentLevel
                        .getStartPositionColumnsForPlayer2().size()));
            }
            currentLevel.getCurrentMove().setMovementStopped(true);

            pauseMovementForSecondPlayer(bestColumnForPlayer2, isFirstTurn);
        }
    }

    private void pauseMovementForSecondPlayer(int bestColumnForPlayer, boolean isFirstTurn) {
        final int finalBestColumnForPlayer2 = bestColumnForPlayer;
        abstractScreen.addAction(Actions.sequence(Actions.delay(isFirstTurn && !currentLevel.isPlayer1Turn() ? PLAYER2_PAUSE_INTERVAL * 4f : PLAYER2_PAUSE_INTERVAL), Utils.createRunnableAction(new Runnable() {
            @Override
            public void run() {
                clickBalloon(finalBestColumnForPlayer2, MatrixValue.PLAYER_2.getValue());
            }
        })));
    }

    private void incrementPointsForCurrentMove(int[][] clonedMatrix, MutablePair<
            Integer, Integer> nextPosition) {
        if (nextPosition != null) {
            int mtrxValue = clonedMatrix[nextPosition.right][nextPosition.left];
            int score = currentLevel.getCurrentMove().getMovementFinishedInfo().getScore() + mcu.calculatePointsForCurrentMove(mtrxValue);
            currentLevel.getCurrentMove().getMovementFinishedInfo().setScore(score);
            currentLevel.getCurrentMove().getMovementFinishedInfo().getSoundsToPlayInOrder().add(mtrxValue);
        }
    }

    private void resetState() {
        currentLevel.getCurrentMove().getMovementFinishedInfo().setScore(0);
        currentLevel.getCurrentMove().setMovementStopped(false);
        currentLevel.getCurrentMove().getMovementFinishedInfo().setDestroyed(false);
        currentLevel.getCurrentMove().getMovementFinishedInfo().getSoundsToPlayInOrder().clear();
        currentLevel.getCurrentMove().setCurrentRandomTornadoNextPosition(null);
    }

    private MutablePair<Integer, Integer> calculateNextPosition(PlayerPosition pos) {

        MutablePair<Integer, Integer> nextPos = null;
        MutablePair<Integer, Integer> currentPos = pos.getCurrentPosition().getPoint();

        MatrixValue uv = pos.getUpValue().getMatrixValue();
        MatrixValue lv = pos.getLeftValue().getMatrixValue();
        MatrixValue rv = pos.getRightValue().getMatrixValue();
        MatrixValue cv = pos.getCurrentPosition().getMatrixValue();

        if (cv.isRandomUp()) {

            nextPos = new MutablePair<>();
            if (currentLevel.getCurrentMove().getCurrentRandomTornadoNextPosition() == null) {
                currentLevel.getCurrentMove().setCurrentRandomTornadoNextPosition(mcu.getRandomUpPosition(currentPos, currentLevel.getLevelMatrix()));
            }
            nextPos.left = currentLevel.getCurrentMove().getCurrentRandomTornadoNextPosition().getLeft();
            nextPos.right = currentLevel.getCurrentMove().getCurrentRandomTornadoNextPosition().getRight();

        } else if (uv != null) {
            if (uv.isMoveUp() && uv.isOverlap()) {
                nextPos = new MutablePair<>();
                nextPos.left = currentPos.left;
                nextPos.right = currentPos.right - 1;
            } else if (uv.isMoveLeft()) {

                if (lv != null && lv.isDestroy()) {
                    currentLevel.getCurrentMove().getMovementFinishedInfo().setDestroyed(true);
                } else if (lv != null && lv.isOverlap()) {
                    nextPos = new MutablePair<>();
                    nextPos.left = currentPos.left - 1;
                    nextPos.right = currentPos.right;
                }

            } else if (uv.isMoveRight()) {

                if (rv != null && rv.isDestroy()) {
                    currentLevel.getCurrentMove().getMovementFinishedInfo().setDestroyed(true);
                } else if (rv != null && rv.isOverlap()) {
                    nextPos = new MutablePair<>();
                    nextPos.left = currentPos.left + 1;
                    nextPos.right = currentPos.right;
                }

            } else if (uv.isDestroy()) {
                currentLevel.getCurrentMove().getMovementFinishedInfo().setDestroyed(true);
            }
        }

        return nextPos;
    }

    private void levelFinished(int player1Score, int player2Score) {
        if (player1Score >= player2Score) {
            campaignService.levelFinished(player1Score, levelInfo.getLevelEnum());
        }
        BalloonGame.getInstance().getScreenManager().showLevelFinishedScreen(levelInfo, player1Score, levelInfo.isOnePlayerLevel() ? -1 : player2Score);
    }

}
