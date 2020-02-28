package libgdx.implementations.memory.spec;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameLogic {

    public MatrixElement[][] generateMatrix(GameLevel gameLevel) {
        int rows = gameLevel.getRows();
        int col = gameLevel.getCols();
        int[][] matrix = generateRandomMatrix(gameLevel);
        MatrixElement[][] randomMatrix = new MatrixElement[rows][col];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < col; j++) {
                int value = matrix[i][j];
                randomMatrix[i][j] = new MatrixElement(value, true, false);
            }
        }
        return randomMatrix;
    }

    public void showAllImages(MatrixElement[][] matrix) {
        showAllImages(true, matrix);
    }

    public void hideAllImages(MatrixElement[][] matrix) {
        showAllImages(false, matrix);
    }

    private void showAllImages(boolean show, MatrixElement[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                matrix[i][j].setShowed(show);
            }
        }
    }

    private boolean choiceIsMatch(MatrixChoice choice, MatrixChoice openedChoice) {
        return choice.getItem() == openedChoice.getItem();
    }

    public boolean doButtonsMatchAndProcess(MatrixChoice choice, MatrixChoice openedChoice, MatrixElement[][] matrix) {
        if (choiceIsMatch(choice, openedChoice)) {
            matrix[choice.getY()][choice.getX()].setShowed(true);
            matrix[choice.getY()][choice.getX()].setFound(true);
            matrix[openedChoice.getY()][openedChoice.getX()].setFound(true);
            return true;
        } else {
            matrix[choice.getY()][choice.getX()].setShowed(false);
            matrix[openedChoice.getY()][openedChoice.getX()].setShowed(false);
            return false;
        }
    }

    public int[][] generateRandomMatrix(GameLevel gameLevel) {

        int rows = gameLevel.getRows();
        int columns = gameLevel.getCols();
        int[][] randomMatrix = resetedMatrix(rows, columns);

        Random random = new Random();
        int row = random.nextInt(rows);
        int column = random.nextInt(columns);

        int matrixElementsFilled = 0;
        List<Integer> addedValues = new ArrayList<Integer>();
        while (matrixElementsFilled < rows * columns) {
            int numberOfElements = gameLevel.getMaxItems();
            int cellValue = random.nextInt(numberOfElements);
            int addedNr = 0;
            while (addedValues.contains(cellValue)) {
                cellValue = random.nextInt(numberOfElements);
                addedNr++;
                if (addedNr > 1000) {
                    break;
                }
            }
            addedValues.add(cellValue);
            for (int i = 0; i < 2; i++) {
                int matrixElementsTraversed = 0;
                while (randomMatrix[row][column] != -1) {
                    row = random.nextInt(rows);
                    column = random.nextInt(columns);
                    matrixElementsTraversed++;
                    if (matrixElementsTraversed > 1000) {
                        break;
                    }
                }
                randomMatrix[row][column] = cellValue;
                matrixElementsFilled++;
            }
        }
        return randomMatrix;
    }

    private int[][] resetedMatrix(int rows, int columns) {
        int[][] randomMatrix = new int[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                randomMatrix[i][j] = -1;
            }
        }
        return randomMatrix;
    }

    public int randomNumber(int itemNr) {
        if (itemNr <= 4) {
            switch (itemNr) {
                case 2:
                    if (Math.random() > 0.5) {
                        return 0;
                    } else {
                        return 1;
                    }
                case 3:
                    if (Math.random() < 0.33) {
                        return 0;
                    } else if (Math.random() < 0.66) {
                        return 1;
                    } else {
                        return 2;
                    }
                case 4:
                    if (Math.random() < 0.25) {
                        return 0;
                    } else if (Math.random() < 0.5) {
                        return 1;
                    } else if (Math.random() < 0.75) {
                        return 2;
                    } else {
                        return 3;
                    }
                default:
                    return 0;
            }
        } else {
            return new Random().nextInt(itemNr);
        }
    }
}
