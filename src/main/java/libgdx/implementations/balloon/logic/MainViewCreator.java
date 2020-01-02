package libgdx.implementations.balloon.logic;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

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

import libgdx.controls.label.MyWrappedLabel;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.balloon.BalloonSpecificResource;
import libgdx.implementations.balloon.model.CurrentLevel;
import libgdx.implementations.balloon.model.LevelInfo;
import libgdx.implementations.balloon.model.MatrixValue;
import libgdx.implementations.balloon.model.MatrixWithChangedCells;
import libgdx.implementations.balloon.model.PlayerPosition;
import libgdx.resources.Res;
import libgdx.screen.AbstractScreen;
import libgdx.utils.SoundUtils;
import libgdx.utils.Utils;

public class MainViewCreator {

    private static final int NR_OF_TRIES_TO_FIND_A_CORRECT_MOVEMENT = 200;

    public final static int ROW_ID_STARTING_INT_VALUE = 200;
    public final static long BALLOON_PAUSE_INTERVAL = 200;
    public final static long PLAYER2_PAUSE_INTERVAL = 400;
    private int imageViewIdForFinalBalloonPosition = 777;

    private final static String SCOREPLAYER1 = "SCOREPLAYER1";
    private final static String SCOREPLAYER2 = "SCOREPLAYER2";

    private ImageManager imageManager;

    private CurrentLevel currentLevel;

    private int nrOfRows;
    private int nrOfCols;

    private LevelInfo levelInfo;

    private MatrixCoordinatesUtils mcu;

    private AbstractScreen abstractScreen;


    public MainViewCreator(CurrentLevel currentLevel, LevelInfo levelInfo, AbstractScreen screen) {
        this.nrOfRows = currentLevel.getLeveltMatrix().length;
        this.nrOfCols = currentLevel.getLeveltMatrix()[0].length;
        this.currentLevel = currentLevel;
        this.levelInfo = levelInfo;
        this.abstractScreen = screen;
        imageManager = new ImageManager();
        mcu = new MatrixCoordinatesUtils(nrOfCols, nrOfRows);
    }

    public void addGameRows() {
        List<Table> gameRows = createGameRows();
        Table container = new Table();
        for (Table row : gameRows) {
            container.add(row).growX();
        }
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
                viewRow.add(img);
            }
            row++;
        }
    }

    private NinePatchDrawable getBackgroundForColumn(int col) {
        Res colColor = BalloonSpecificResource.light_green_backgr;
        if (col % 2 == 0) {
            colColor = BalloonSpecificResource.light_blue_backgr;
        }
        return GraphicUtils.getNinePatch(colColor);
    }

    private void updateMatrixWithStartPositionForPlayer() {
        int value = currentLevel.isPlayer1Turn() ? MatrixValue.PLAYER_1.getValue() : MatrixValue.PLAYER_2.getValue();
        Set<Integer> startPositions = currentLevel.isPlayer1Turn() ? currentLevel.getStartPositionColumnsForPlayer1() : currentLevel
                .getStartPositionColumnsForPlayer2();
        resetFirstRowFromMatrix();
        int firstRowIndex = mcu.getFirstRowIndex();
        for (int j = 0; j < nrOfCols; j++) {
            if (startPositions.contains(j)) {
                currentLevel.getLeveltMatrix()[firstRowIndex][j] = value;
            }
        }
    }

    private void refreshDisplayOfMatrix(Set<MutablePair<Integer, Integer>> cellsToUpdate, int[][] matrix) {
        updateMatrixWithStartPositionForPlayer();
        for (MutablePair<Integer, Integer> point : cellsToUpdate) {
            Table img = createImgView(matrix[point.right][point.left], point.left, point.right);
            img.setBackground(getBackgroundForColumn(point.left));
            Table createdRow = getCreatedRow(point.right);
            Table imgTableToUpdate = (Table) createdRow.getChildren().get(point.left);
            imgTableToUpdate.clearChildren();
            imgTableToUpdate.add(img);
        }
    }

    public void refreshScore() {
        MyWrappedLabel scorePlayer1 = (MyWrappedLabel) abstractScreen.getRoot().findActor(SCOREPLAYER1);
        scorePlayer1.setText(calculateScore(currentLevel.getFinalPositionPairsForPlayer1().values()) + "");
        MyWrappedLabel scorePlayer2 = (MyWrappedLabel) abstractScreen.getRoot().findActor(SCOREPLAYER2);
        scorePlayer2.setText(calculateScore(currentLevel.getFinalPositionPairsForPlayer2().values()) + "");
    }

    public void isPlayer2First() {
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
            currentLevel.getLeveltMatrix()[firstRowIndex][j] = MatrixValue.AIR.getValue();
        }
    }

    private Table createImgView(final int mtrxVal, final int selectedColumn, final int row) {
        Table image = null;
        if (mtrxVal == MatrixValue.FINAL_PLAYER_1.getValue()) {
            image = imageManager.getFinalPositionImageWithPoints(currentLevel.getFinalPositionPairsForPlayer1().get(new MutablePair<Integer, Integer>(selectedColumn, row)),
                    imageViewIdForFinalBalloonPosition, MatrixValue.FINAL_PLAYER_1);
            imageViewIdForFinalBalloonPosition = imageViewIdForFinalBalloonPosition + 1;
        } else if (mtrxVal == MatrixValue.FINAL_PLAYER_2.getValue()) {
            image = imageManager.getFinalPositionImageWithPoints(currentLevel.getFinalPositionPairsForPlayer2().get(new MutablePair<Integer, Integer>(selectedColumn, row)),
                    imageViewIdForFinalBalloonPosition, MatrixValue.FINAL_PLAYER_2);
            imageViewIdForFinalBalloonPosition = imageViewIdForFinalBalloonPosition + 1;
        } else {
            image = imageManager.getImage(MatrixValue.getMatrixValue(mtrxVal));
        }
        image.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        if (!currentLevel.isPlayer2Computer() || (currentLevel.isPlayer2Computer() && currentLevel.isPlayer1Turn())) {
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
        boolean isPlayerClicked = cellValue == MatrixValue.PLAYER_1.getValue() || cellValue == MatrixValue.PLAYER_2.getValue();
        if (isPlayerClicked && currentLevel.getCurrentMove().isMovementStopped()) {
            currentLevel.getCurrentMove().setClickedColumn(clickedColumn);

            MutablePair<Integer, Integer> currentPosition = new MutablePair<Integer, Integer>(clickedColumn, mcu.getFirstRowIndex());
            currentLevel.getCurrentMove().setPlayerPosition(mcu.fillCurrentPositionInfo(currentPosition, currentLevel.getLeveltMatrix()));
            resetState();
            moveElement();
        }
    }

    private void movePlanes() {
        for (MutablePair<Integer, Integer> plane : currentLevel.getPlanes()) {
            updatePlaneNeighbours(plane);
            processOldPlanePosition(plane);
            movePlaneRight(plane);
        }
        for (MutablePair<Integer, Integer> plane : currentLevel.getPlanes()) {
            if (currentLevel.getLeveltMatrix()[plane.right][plane.left] == MatrixValue.AIR.getValue()) {
                currentLevel.getLeveltMatrix()[plane.right][plane.left] = MatrixValue.PLANE.getValue();
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
        int planePositionValue = currentLevel.getLeveltMatrix()[plane.right][plane.left];
        if (planePositionValue == MatrixValue.PLANE.getValue()) {
            currentLevel.getLeveltMatrix()[plane.right][plane.left] = MatrixValue.AIR.getValue();
        }
        currentLevel.getCurrentMove().getMovementFinishedInfo().addCellToUpdate(plane);
    }

    private void updatePlaneNeighbours(MutablePair<Integer, Integer> plane) {
        MatrixWithChangedCells matrixWithChangedCells = mcu.verifyPlaneNeighbours(plane, currentLevel.getLeveltMatrix(), currentLevel.isPlayer1Turn());
        currentLevel.getCurrentMove().getMovementFinishedInfo().addCellsToUpdate(matrixWithChangedCells.getCellsToUpdate());
        currentLevel.setLeveltMatrix(matrixWithChangedCells.getLeveltMatrix());
    }

    private void moveElement() {
        int tries = 0;
        while (!simulateMovementAndVerifyIfItsCorrect(tries, currentLevel.getCurrentMove().getPlayerPosition().copy(),
                MatrixCoordinatesUtils.cloneArray(currentLevel.getLeveltMatrix()))) {
            tries++;
        }
        int nrOfMoves = currentLevel.getCurrentMove().getMovementFinishedInfo().getNrOfMoves();
        final MutableInt indx = new MutableInt(0);
        currentLevel.getCurrentMove().getMovementFinishedInfo().setDestroyed(false);
        removePlayerStartPosition(currentLevel.getCurrentMove().getClickedColumn());
        while (indx.intValue() < nrOfMoves) {
            abstractScreen.addAction(Actions.sequence(Utils.createRunnableAction(new Runnable() {
                @Override
                public void run() {
                    playSound(indx.intValue());
                    startElemMovement();
                    refreshDisplayOfMatrix(currentLevel.getCurrentMove().getMovementFinishedInfo().getCellsToUpdate(), currentLevel.getLeveltMatrix());
                    currentLevel.getCurrentMove().getMovementFinishedInfo().getCellsToUpdate().clear();
                    indx.setValue(indx.intValue() + 1);
                }
            }), Actions.delay(BALLOON_PAUSE_INTERVAL)));
        }
        movePlanes();
        finalPositionUpdate();

        addFirstRowToCellUpdate();
        refreshScore();
        currentLevel.getCurrentMove().setMovementStopped(true);

        togglePlayer(false);
        refreshDisplayOfMatrix(currentLevel.getCurrentMove().getMovementFinishedInfo().getCellsToUpdate(), currentLevel.getLeveltMatrix());

        if (isLevelFinished()) {
            levelFinished(calculateScore(currentLevel.getFinalPositionPairsForPlayer1().values()), calculateScore(currentLevel
                    .getFinalPositionPairsForPlayer2().values()));
        }
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
            currentLevel.getLeveltMatrix()[finalPosition.right][finalPosition.left] = MatrixCoordinatesUtils.getFinalPositionForPlayer(currentLevel.isPlayer1Turn())
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

            currentLevel.setLeveltMatrix(mcu.movePlayer(currentLevel.getCurrentMove().getPlayerPosition(), nextPosition, currentLevel.getLeveltMatrix(),
                    currentLevel.isPlayer1Turn()));

            currentLevel.getCurrentMove().setPlayerPosition(mcu.fillCurrentPositionInfo(nextPosition, currentLevel.getLeveltMatrix()));
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
            currentLevel.getLeveltMatrix()[currentLevel.getCurrentMove().getMovementFinishedInfo().getFinalPosition().right][currentLevel.getCurrentMove()
                    .getMovementFinishedInfo().getFinalPosition().left] = MatrixCoordinatesUtils.getDestroyedPlayer(currentLevel.isPlayer1Turn()).getValue();
            cellsToUpdate.add(currentLevel.getCurrentMove().getMovementFinishedInfo().getFinalPosition());
        }
        return cellsToUpdate;
    }

    private boolean simulateMovementAndVerifyIfItsCorrect(int tries, PlayerPosition playerPositionCopy, int[][] clonedMatrix) {
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
        int[][] cloneArray = MatrixCoordinatesUtils.cloneArray(currentLevel.getLeveltMatrix());
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

            pauseMovementForSecondPlayer(bestColumnForPlayer2);
        }
    }

    private void pauseMovementForSecondPlayer(int bestColumnForPlayer) {
        final int finalBestColumnForPlayer2 = bestColumnForPlayer;
        abstractScreen.addAction(Actions.sequence(Actions.delay(PLAYER2_PAUSE_INTERVAL), Utils.createRunnableAction(new Runnable() {
            @Override
            public void run() {
                clickBalloon(finalBestColumnForPlayer2, MatrixValue.PLAYER_2.getValue());
            }
        })));
    }

    private void incrementPointsForCurrentMove(int[][] clonedMatrix, MutablePair<Integer, Integer> nextPosition) {
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
                currentLevel.getCurrentMove().setCurrentRandomTornadoNextPosition(mcu.getRandomUpPosition(currentPos, currentLevel.getLeveltMatrix()));
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
        //TODO go to levelfinished screen
    }

}
