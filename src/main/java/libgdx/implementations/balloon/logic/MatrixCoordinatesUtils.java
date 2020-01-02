package libgdx.implementations.balloon.logic;

import org.apache.commons.lang3.tuple.MutablePair;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import libgdx.implementations.balloon.model.CoordinateInfo;
import libgdx.implementations.balloon.model.MatrixValue;
import libgdx.implementations.balloon.model.MatrixWithChangedCells;
import libgdx.implementations.balloon.model.PlayerPosition;

public class MatrixCoordinatesUtils {

    private static final int POINTS_PER_MOVE = 5;
    private static final int POINTS_PER_POINT_CELL = 10;

    private int nrOfCols;
    private int nrOfRows;

    public MatrixCoordinatesUtils(int nrOfCols, int nrOfRows) {
        this.nrOfCols = nrOfCols;
        this.nrOfRows = nrOfRows;
    }

    public PlayerPosition fillCurrentPositionInfo(MutablePair<Integer, Integer> currentPositionCoord, int[][] matrix) {
        PlayerPosition pos = new PlayerPosition();

        CoordinateInfo currentPosition = new CoordinateInfo();
        currentPosition.setPoint(currentPositionCoord);
        currentPosition.setMatrixValue(MatrixValue.getMatrixValue(matrix[currentPositionCoord.getRight()][currentPositionCoord.getLeft()]));
        pos.setCurrentPosition(currentPosition);

        CoordinateInfo upValue = new CoordinateInfo();
        if (currentPositionCoord.getRight() - 1 >= 0) {
            upValue.setPoint(new MutablePair(currentPositionCoord.getLeft(), currentPositionCoord.getRight() - 1));
            upValue.setMatrixValue(MatrixValue.getMatrixValue(matrix[currentPositionCoord.getRight() - 1][currentPositionCoord.getLeft()]));
        }

        CoordinateInfo leftValue = new CoordinateInfo();
        if (currentPositionCoord.getLeft() - 1 >= 0) {
            leftValue.setPoint(new MutablePair(currentPositionCoord.getLeft() - 1, currentPositionCoord.getRight()));
            leftValue.setMatrixValue(MatrixValue.getMatrixValue(matrix[currentPositionCoord.getRight()][currentPositionCoord.getLeft() - 1]));
        }

        CoordinateInfo rightValue = new CoordinateInfo();
        if (currentPositionCoord.getLeft() + 1 <= nrOfCols - 1) {
            rightValue.setPoint(new MutablePair(currentPositionCoord.getLeft() + 1, currentPositionCoord.getRight()));
            rightValue.setMatrixValue(MatrixValue.getMatrixValue(matrix[currentPositionCoord.getRight()][currentPositionCoord.getLeft() + 1]));
        }

        pos.setUpValue(upValue);
        pos.setLeftValue(leftValue);
        pos.setRightValue(rightValue);

        return pos;
    }

    public int getFirstRowIndex() {
        return nrOfRows - 1;
    }

    private int getSecondRowIndex() {
        return nrOfRows - 2;
    }

    public boolean isFirstOrSecondRow(int row) {
        return row == getFirstRowIndex() || row == getSecondRowIndex();
    }

    private int getLastRowIndex() {
        return 0;
    }

    public boolean isLastRow(int row) {
        return row == getLastRowIndex();
    }

    public int getFinalPositionValue(MutablePair<Integer, Integer> finalPosition, int[][] matrix) {
        int finalPositionValue = MatrixValue.AIR.getValue();
        if (finalPosition != null) {
            finalPositionValue = matrix[finalPosition.getRight()][finalPosition.getLeft()];
        }
        return finalPositionValue;
    }

    public MatrixWithChangedCells verifyPlaneNeighbours(MutablePair<Integer, Integer> plane, int[][] matrix, boolean isPlayer1Turn) {
        MatrixWithChangedCells matrixWithChangedCells = new MatrixWithChangedCells();
        if ((plane.getRight() + 1 < nrOfRows - 1) && (matrix[plane.getRight() + 1][plane.getLeft()] == getDestroyedPlayer(isPlayer1Turn).getValue())) {
            matrix[plane.getRight() + 1][plane.getLeft()] = MatrixValue.AIR.getValue();
            matrixWithChangedCells.addCellToUpdate(new MutablePair(plane.getLeft(), plane.getRight() + 1));
        }
        if ((plane.getLeft() - 1 >= 0) && (matrix[plane.getRight()][plane.getLeft() - 1] == getDestroyedPlayer(isPlayer1Turn).getValue())) {
            matrix[plane.getRight()][plane.getLeft() - 1] = MatrixValue.AIR.getValue();
            matrixWithChangedCells.addCellToUpdate(new MutablePair(plane.getLeft() - 1, plane.getRight()));
        }
        if ((plane.getLeft() + 1 <= nrOfCols - 1) && (matrix[plane.getRight()][plane.getLeft() + 1] == getDestroyedPlayer(isPlayer1Turn).getValue())) {
            matrix[plane.getRight()][plane.getLeft() + 1] = MatrixValue.AIR.getValue();
            matrixWithChangedCells.addCellToUpdate(new MutablePair(plane.getLeft() + 1, plane.getRight()));
        }
        matrixWithChangedCells.setLeveltMatrix(matrix);
        return matrixWithChangedCells;
    }

    public static int[][] cloneArray(int[][] src) {
        if (src == null) {
            return null;
        }
        int length = src.length;
        int[][] target = new int[length][src[0].length];
        for (int i = 0; i < length; i++) {
            System.arraycopy(src[i], 0, target[i], 0, src[i].length);
        }
        return target;
    }

    private MatrixValue processMatrixValueChange(MatrixValue value, boolean isPlayer1Turn) {
        switch (value) {
            case ARROW_LEFT:
                return MatrixValue.ARROW_RIGHT;
            case ARROW_RIGHT:
                return MatrixValue.ARROW_LEFT;
            case PLAYER_1:
                return MatrixValue.PLAYER_1;
            case PLAYER_2:
                return MatrixValue.PLAYER_2;
            case TORNADO:
                return getCorrectPlayerForTornado(isPlayer1Turn);
            case TORNADO_1_PLAYER_1:
                return MatrixValue.TORNADO_2_PLAYER_1;
            case TORNADO_1_PLAYER_2:
                return MatrixValue.TORNADO_2_PLAYER_2;
            case TORNADO_2_PLAYER_1:
                return MatrixValue.TORNADO;
            case TORNADO_2_PLAYER_2:
                return MatrixValue.TORNADO;
            case AIR:
                return getCorrectPlayerForAir(isPlayer1Turn);
            case POINTS:
                return getCorrectPlayerForPoints(isPlayer1Turn);
            case BLOCKING_CLOUD:
                return MatrixValue.BLOCKING_CLOUD;
            default:
                return MatrixValue.AIR;
        }
    }

    private MatrixValue getCorrectPlayerForTornado(boolean isPlayer1Turn) {
        return isPlayer1Turn ? MatrixValue.TORNADO_1_PLAYER_1 : MatrixValue.TORNADO_1_PLAYER_2;
    }

    private MatrixValue getCorrectPlayerForAir(boolean isPlayer1Turn) {
        return isPlayer1Turn ? MatrixValue.PLAYER_1 : MatrixValue.PLAYER_2;
    }

    private MatrixValue getCorrectPlayerForPoints(boolean isPlayer1Turn) {
        return isPlayer1Turn ? MatrixValue.PLAYER_1 : MatrixValue.PLAYER_2;
    }

    private MatrixValue getCorrectPlayerForMovement(boolean isPlayer1Turn) {
        return isPlayer1Turn ? MatrixValue.PLAYER_1 : MatrixValue.PLAYER_2;
    }

    public static MatrixValue getFinalPositionForPlayer(boolean isPlayer1Turn) {
        return isPlayer1Turn ? MatrixValue.FINAL_PLAYER_1 : MatrixValue.FINAL_PLAYER_2;
    }

    public static MatrixValue getDestroyedPlayer(boolean isPlayer1Turn) {
        return isPlayer1Turn ? MatrixValue.DESTROYED_PLAYER_1 : MatrixValue.DESTROYED_PLAYER_2;
    }

    public int[][] movePlayer(PlayerPosition playerPosition, MutablePair<Integer, Integer> nextPosition, int[][] matrix, boolean isPlayer1Turn) {

        MatrixValue currentPositionMatrixValue = playerPosition.getCurrentPosition().getMatrixValue();
        MatrixValue nextPositionMatrixValue = MatrixValue.getMatrixValue(matrix[nextPosition.getRight()][nextPosition.getLeft()]);

        CoordinateInfo upValue = playerPosition.getUpValue();
        if (upValue != null && !upValue.getMatrixValue().isOverlap() && !currentPositionMatrixValue.isRandomUp()) {
            matrix[upValue.getPoint().getRight()][upValue.getPoint().getLeft()] = processMatrixValueChange(upValue.getMatrixValue(), isPlayer1Turn).getValue();
        }
        if (nextPositionMatrixValue.isOverlap()) {

            if (currentPositionMatrixValue.isMoveOneStepWithPlayer() && currentPositionMatrixValue.isRandomUp()) {
                MatrixValue newPosVal = processMatrixValueChange(currentPositionMatrixValue, isPlayer1Turn);
                matrix[nextPosition.getRight()][nextPosition.getLeft()] = newPosVal.getValue();
            } else {
                MatrixValue newPosVal = processMatrixValueChange(nextPositionMatrixValue, isPlayer1Turn);
                matrix[nextPosition.getRight()][nextPosition.getLeft()] = newPosVal.getValue();
            }

        } else {
            matrix[nextPosition.getRight()][nextPosition.getLeft()] = getCorrectPlayerForMovement(isPlayer1Turn).getValue();
        }

        if (currentPositionMatrixValue.isOverlap() && !currentPositionMatrixValue.isMoveOneStepWithPlayer()) {
            matrix[playerPosition.getCurrentPosition().getPoint().getRight()][playerPosition.getCurrentPosition().getPoint().getLeft()] = processMatrixValueChange(
                    currentPositionMatrixValue, isPlayer1Turn).getValue();
        } else {
            matrix[playerPosition.getCurrentPosition().getPoint().getRight()][playerPosition.getCurrentPosition().getPoint().getLeft()] = MatrixValue.AIR.getValue();
        }

        return matrix;
    }

    public MutablePair<Integer, Integer> getRandomUpPosition(MutablePair<Integer, Integer> currentPosition, int[][] matrix) {
        MutablePair<Integer, Integer> randomPoint = randomPoint(currentPosition, matrix);
        while (matrix[randomPoint.getRight()][randomPoint.getLeft()] != MatrixValue.AIR.getValue() || isFirstOrSecondRow(randomPoint.getRight()) || isLastRow(randomPoint.getRight())) {
            randomPoint = randomPoint(currentPosition, matrix);
        }
        return randomPoint;
    }

    private MutablePair<Integer, Integer> randomPoint(MutablePair<Integer, Integer> currentPosition, int[][] matrix) {
        Random rand = new Random();
        int x = rand.nextInt(nrOfCols);
        int y = rand.nextInt(nrOfRows);
        return new MutablePair(x, y);
    }

    public int calculatePointsForCurrentMove(int mtrxValue) {
        int pointsToIncrement = POINTS_PER_MOVE;
        if (mtrxValue == MatrixValue.POINTS.getValue()) {
            pointsToIncrement = POINTS_PER_POINT_CELL + pointsToIncrement;
        }
        return pointsToIncrement;
    }

    public boolean verifyMoveValidity(MutablePair<Integer, Integer> nextPosition, int[][] matrix, PlayerPosition playerPosition, MutablePair<Integer, Integer> currentRandomTornadoNextPosition) {
        boolean isPlayerInAnotherTornado = currentRandomTornadoNextPosition != null && matrix[nextPosition.getRight()][nextPosition.getLeft()] == MatrixValue.TORNADO.getValue();
        if (isPlayerInAnotherTornado) {
            return false;
        }

        boolean isTornado2 = playerPosition.getCurrentPosition().getMatrixValue() == MatrixValue.TORNADO_2_PLAYER_1
                || playerPosition.getCurrentPosition().getMatrixValue() == MatrixValue.TORNADO_2_PLAYER_2;

        if (isTornado2 && playerPosition.getUpValue().getMatrixValue() != MatrixValue.AIR) {
            return false;
        }
        return true;
    }

    public int bestColumnForPlayer(Map<Integer, Integer> colsAndScores, Set<Integer> startPositionColumnsForPlayer2) {
        int maxScore = 0;
        int col = new ArrayList<Integer>(startPositionColumnsForPlayer2).get(0);
        for (Integer i : startPositionColumnsForPlayer2) {
            Integer colScore = colsAndScores.get(i);
            if (colScore > maxScore) {
                col = i;
                maxScore = colScore;
            }
        }
        return col;
    }

}
