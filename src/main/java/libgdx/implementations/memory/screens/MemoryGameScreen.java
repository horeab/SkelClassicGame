package libgdx.implementations.memory.screens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.math.MathScreenManager;
import libgdx.implementations.memory.spec.*;
import libgdx.implementations.skelgame.SkelGameRatingService;
import libgdx.resources.Resource;
import libgdx.screen.AbstractScreen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoryGameScreen extends AbstractScreen<MathScreenManager> {


    private List<String> discoveredItems = new ArrayList<>();
    private MatrixElement[][] levelMatrix;
    private CurrentGame currentGame;
    private boolean enableImageClick = false;
    private List<TableCell> cells = new ArrayList<>();

    public MemoryGameScreen() {
        this.currentGame = new CurrentGame(this, 0, 0);
    }

    public List<String> getDiscoveredItems() {
        return discoveredItems;
    }

    @Override
    protected void initFields() {
        levelMatrix = new GameLogic().generateMatrix(GameLevel._0);
    }

    @Override
    public void buildStage() {
        new SkelGameRatingService(this).appLaunched();
        addAllTable();
    }

    private void addAllTable() {
        Table table = new Table();

        final int rows = currentGame.getCurrentLevel().getRows();
        final int columns = currentGame.getCurrentLevel().getCols();

        table.setFillParent(true);

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                final MatrixElement currentItem = levelMatrix[row][col];
                final MatrixChoice clickedItem = new MatrixChoice(col, row, currentItem.getItem());
                Table cell = new Table();
                Image image = getMatrixElementImage(currentItem);
                cell.add(image);
                cells.add(new TableCell(cell, currentItem));
                image.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        if (enableImageClick && !clickedItem.equals(currentGame.getFirstChoice()) && !currentItem.isFound()
                                && !currentItem.isShowed()) {
                            currentItem.setShowed(true);
                            refreshImageViews();

                            if (currentGame.getFirstChoice() != null) {
                                processScore(currentGame, clickedItem);
                                if (isLevelFinished(levelMatrix)) {
                                    currentGame.setTotalScoreFor(currentGame.getTotalScoreFor() + currentGame.getStageScoreFor());
                                    processLevelFinishedPopup(currentGame.getTotalScoreFor(), currentGame.getTotalScoreAgainst(),
                                            currentGame.getStageScoreFor(), currentGame.getStageScoreAgainst());
                                }
                            }

                            MatrixChoice firstItemClicked = currentGame.getFirstChoice() == null ? clickedItem : null;
                            currentGame.setFirstChoice(firstItemClicked);

                            if (currentGame.getFirstChoice() == null) {
                                refreshImageViews();
                            }
                        }
                    }
                });
                table.add(cell).grow();
            }
            table.row();
        }

        addActor(table);
    }

    public void refreshImageViews() {
        for (TableCell tableCell : cells) {
            tableCell.getCell().clearChildren();
            if (tableCell.getMatrixElement().isFound() || tableCell.getMatrixElement().isShowed()) {
                tableCell.getCell().add(getMatrixElementImage(tableCell.getMatrixElement()));
            }
        }
    }

    private void processScore(CurrentGame currentGame, MatrixChoice clickedItem) {
        int scoreForToIncrement = 0;
        int scoreAgainstToIncrement = 0;
        String itemValue = null;
    }

    private void processLevelFinishedPopup(int totalScoreFor, int totalScoreAgainst,
                                           int stageScoreFor, int stageScoreAgainst) {
    }

    private Image getMatrixElementImage(MatrixElement currentItem) {
        return GraphicUtils.getImage(Resource.valueOf(currentItem.isShowed() ? "item" + currentItem.getItem() : "unknown"));
    }

    public boolean isEnableImageClick() {
        return enableImageClick;
    }

    public void setEnableImageClick(boolean enableImageClick) {
        this.enableImageClick = enableImageClick;
    }

    private boolean isLevelFinished(MatrixElement[][] matrix) {
        boolean isGameOver = true;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (!matrix[i][j].isFound()) {
                    return false;
                }
            }
        }
        return isGameOver;
    }

    public void refreshTextViews(String itemValue) {
//        Map<String, String> scores = new HashMap<String, String>();
//        scores.put(Utils.GREEN, currentGame.getTotalScoreFor() + "");
//        scores.put(Utils.RED, currentGame.getTotalScoreAgainst() + "");
//
//        String scoreFor = Utils.fontString(currentGame.getTotalScoreFor() + "", false, Utils.GREEN);
//        String scoreAgainst = Utils.fontString(currentGame.getTotalScoreAgainst() + "", false, Utils.RED);
//        String scoreLabel = scoreFor + " - " + scoreAgainst;
//        TextView scoreTextView = (TextView) findViewById(R.id.scoreLabel);
//
//        String stageScoreFor = Utils.fontString(currentGame.getStageScoreFor() + "", false, Utils.GREEN);
//        String stageScoreAgainst = Utils.fontString(currentGame.getStageScoreAgainst() + "", false, Utils.RED);
//        String stageScoreLabel = " (" + stageScoreFor + " - " + stageScoreAgainst + ")";
//        TextView stageScoreTextView = (TextView) findViewById(R.id.stageScoreLabel);
//
//        scoreTextView.setText(Html.fromHtml(scoreLabel));
//        stageScoreTextView.setText(Html.fromHtml(stageScoreLabel));
//
//        TextView commentaryTextView = (TextView) findViewById(R.id.commentaryLabel);
//        if (itemValue != null) {
//            commentaryTextView.setText(Html.fromHtml(itemValue));
//        } else {
//            commentaryTextView.setText("");
//        }
    }

    @Override
    public void onBackKeyPress() {
        screenManager.showMainScreen();
    }

}
