package libgdx.implementations.memory.screens;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import libgdx.campaign.CampaignStoreService;
import libgdx.controls.ScreenRunnable;
import libgdx.controls.button.MainButtonSize;
import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.controls.button.builders.RefreshButtonBuilder;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.controls.popup.notificationpopup.MyNotificationPopupConfigBuilder;
import libgdx.controls.popup.notificationpopup.MyNotificationPopupCreator;
import libgdx.game.Game;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.LevelFinishedPopup;
import libgdx.implementations.memory.MemoryGame;
import libgdx.implementations.memory.MemoryScreenManager;
import libgdx.implementations.memory.MemorySpecificResource;
import libgdx.implementations.memory.spec.*;
import libgdx.resources.dimen.MainDimen;
import libgdx.resources.gamelabel.MainGameLabel;
import libgdx.resources.gamelabel.SpecificPropertiesUtils;
import libgdx.screen.AbstractScreen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.Utils;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;
import libgdx.utils.model.RGBColor;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class MemoryGameScreen extends AbstractScreen<MemoryScreenManager> {

    private final String CELL_NAME = "CELL_NAME";
    private MatrixElement[][] levelMatrix;
    private CurrentGame currentGame;
    private boolean enableImageClick = false;
    private int levelNr;
    private List<TableCell> cells = new ArrayList<>();
    private GameLevel gameLevel;
    private MyWrappedLabel forScore;
    private MyWrappedLabel againstScore;
    private CampaignStoreService campaignStoreService = new CampaignStoreService();
    private MyButton backButton;
    private MyButton refreshButton;

    public MemoryGameScreen(int levelNr) {
        this.levelNr = levelNr;
    }

    @Override
    protected void setBackgroundColor(RGBColor backgroundColor) {
        super.setBackgroundColor(backgroundColor);
    }

    @Override
    protected void setBackgroundContainer(Container<Group> backgroundContainer) {
    }

    @Override
    public void buildStage() {
        setBackgroundColor(new RGBColor(1, 206, 255, 211));
        backButton = new BackButtonBuilder().addHoverBackButton(this);
        addActor(backButton);
        refreshButton = new RefreshButtonBuilder().addHoverRefreshButton(this, createRefreshChangeListener(), ScreenDimensionsManager.getScreenWidth() - MainDimen.horizontal_general_margin.getDimen() * 2 - MainButtonSize.BACK_BUTTON.getWidth());
        addActor(refreshButton);
        addAllTable(levelNr);
    }

    private ChangeListener createRefreshChangeListener() {
        return new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                MemoryGame.getInstance().getScreenManager().showGameScreen(levelNr);
            }
        };
    }

    private void addAllTable(int levelNr) {
        this.levelNr = levelNr;
        this.gameLevel = GameLevel.values()[levelNr];
        this.currentGame = new CurrentGame(this, this.gameLevel.ordinal(), 0);
        levelMatrix = new GameLogic().generateMatrix(gameLevel);
        hideAllImageViews();
        Table table = new Table();

        final int rows = currentGame.getCurrentLevel().getRows();
        final int columns = currentGame.getCurrentLevel().getCols();

        table.setFillParent(true);
        Table headerTable = createHeaderTable();
        float headerHeight = getHeaderHeight();
        table.add(headerTable).width(ScreenDimensionsManager.getScreenWidth()).height(headerHeight).row();
        Table gameTable = new Table();
        headerTable.setHeight(headerHeight);
        boolean horiz = !isVerticalGreater();
        float imagePadding = getImagePadding(horiz);
        int imageSideDimen = getImageSideDimen();
        int index = 0;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                final MatrixElement currentItem = levelMatrix[row][col];
                final MatrixChoice clickedItem = new MatrixChoice(col, row, currentItem.getItem(), index);
                index++;
                Table cell = new Table();
                Image image = getMatrixElementImage(currentItem);
                image.setWidth(imageSideDimen);
                image.setHeight(imageSideDimen);
                cell.setTransform(true);
                cell.add(image).grow();
                TableCell tableCell = new TableCell(cell, currentItem);
                final String name = getCellName(col, row);
                cell.setName(name);
//                cell.setBackground(GraphicUtils.getNinePatch(MainResource.popup_background));
                cells.add(tableCell);
                cell.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        if (enableImageClick && !clickedItem.equals(currentGame.getFirstChoice()) && !currentItem.isFound() && !currentItem.isShowed()) {
                            currentItem.setShowed(true);
                            refreshImageViews(new Runnable() {
                                @Override
                                public void run() {
                                    if (currentGame.getFirstChoice() != null) {
                                        processScore(currentGame, clickedItem);
                                        if (isLevelFailed(levelMatrix)) {
                                            new LevelFinishedPopup(getAbstractScreen(), false, new Runnable() {
                                                @Override
                                                public void run() {
                                                    MemoryGame.getInstance().getScreenManager().showGameScreen(levelNr);
                                                }
                                            }).addToPopupManager();
                                        } else if (isLevelFinished(levelMatrix)) {
                                            getRoot().addAction(Actions.sequence(Actions.delay(2f, Utils.createRunnableAction(new Runnable() {
                                                @Override
                                                public void run() {
                                                    table.remove();
                                                    int nextLevel = levelNr + 1;
                                                    if (nextLevel == 1) {
                                                        setBackgroundColor(new RGBColor(1, 234, 234, 234));
                                                    } else if (nextLevel == 2) {
                                                        setBackgroundColor(new RGBColor(1, 220, 250, 249));
                                                    } else if (nextLevel == 3) {
                                                        setBackgroundColor(new RGBColor(1, 255, 215, 254));
                                                    } else if (nextLevel == 4) {
                                                        setBackgroundColor(new RGBColor(1, 255, 251, 215));
                                                    } else {
                                                        setBackgroundColor(new RGBColor(1, 251, 219, 220));
                                                    }

                                                    int questionsPlayed = new CampaignStoreService().getNrOfQuestionsPlayed();
                                                    if (questionsPlayed > 0 && questionsPlayed % 3 == 0) {
                                                        Game.getInstance().getAppInfoService().showPopupAd(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                addAllTable(nextLevel);
                                                            }
                                                        });
                                                    } else {
                                                        addAllTable(nextLevel);
                                                    }
                                                    new CampaignStoreService().incrementNrOfQuestionsPlayed();
                                                }
                                            }))));
                                        }
                                    }

                                    String firstChoiceName = currentGame.getFirstChoice() == null ? "" : getCellName(currentGame.getFirstChoice().getX(), currentGame.getFirstChoice().getY());
                                    MatrixChoice firstItemClicked = currentGame.getFirstChoice() == null ? clickedItem : null;
                                    currentGame.setFirstChoice(firstItemClicked);

                                    if (currentGame.getFirstChoice() == null) {
                                        setEnableImageClick(false);
                                        getAbstractScreen().getRoot().addAction(Actions.sequence(Actions.delay(0.5f), Utils.createRunnableAction(new Runnable() {
                                            @Override
                                            public void run() {
                                                refreshImageViews(name, firstChoiceName);
                                                refreshTextViews();
                                                setEnableImageClick(true);
                                            }
                                        })));
                                    }
                                }
                            }, name);
                        }
                    }
                });
                gameTable.add(cell).width(imageSideDimen).height(imageSideDimen)
                        .pad(imagePadding).grow();
            }
            gameTable.row();
        }
        gameTable.setVisible(false);
        gameTable.addAction(Actions.sequence(Actions.fadeOut(0f), Utils.createRunnableAction(new Runnable() {
            @Override
            public void run() {
                gameTable.setVisible(true);
            }
        }), Actions.fadeIn(0.3f)));
        table.add(gameTable).width(ScreenDimensionsManager.getScreenWidthValue(95));
        addActor(table);
        backButton.toFront();
        refreshButton.toFront();
    }

    private float getHeaderHeight() {
        return ScreenDimensionsManager.getScreenHeightValue(10);
    }

    private int getImageSideDimen() {
        final int rows = currentGame.getCurrentLevel().getRows();
        int side = calcHorizSide();
        if (isVerticalGreater()) {
            side = (int) ((ScreenDimensionsManager.getScreenHeight() / rows) - (getImagePadding(false) * rows));
        }
        return side;
    }

    private int calcHorizSide() {
        double columns = currentGame.getCurrentLevel().getCols();
        return (int) ((ScreenDimensionsManager.getScreenWidth() / columns) - (getImagePadding(true) * (columns / 2)));
    }

    private boolean isVerticalGreater() {
        int side = calcHorizSide();
        final int rows = currentGame.getCurrentLevel().getRows();
        return side * rows + ((getImagePadding(true) * rows)) > ScreenDimensionsManager.getScreenHeight() - (getImagePadding(true) * rows) - getHeaderHeight();
    }

    private float getImagePadding(boolean horiz) {
        float imagePadding = (horiz ? MainDimen.horizontal_general_margin.getDimen() * 2 : MainDimen.vertical_general_margin.getDimen() * 2);
        if (gameLevel == GameLevel._6) {
            imagePadding = imagePadding / 3;
        } else if (gameLevel == GameLevel._5) {
            imagePadding = imagePadding / 2;
        } else if (gameLevel == GameLevel._4) {
            imagePadding = imagePadding / 2;
        } else if (gameLevel == GameLevel._3) {
            imagePadding = imagePadding / 2;
        } else if (gameLevel == GameLevel._2) {
            imagePadding = imagePadding / 2;
        } else if (gameLevel == GameLevel._0) {
            imagePadding = imagePadding * 2;
        }
        return imagePadding;
    }

    private Table createHeaderTable() {
        Table table = new Table();
        int fontSize = FontConfig.FONT_SIZE * 3;
        forScore = new MyWrappedLabel(new MyWrappedLabelConfigBuilder().setText(currentGame.getStageScoreFor() + "")
                .setFontConfig(new FontConfig(FontColor.GREEN.getColor(), fontSize)).build());
        MyWrappedLabel between = new MyWrappedLabel(new MyWrappedLabelConfigBuilder().setText(" - ")
                .setFontConfig(new FontConfig(fontSize)).build());
        againstScore = new MyWrappedLabel(new MyWrappedLabelConfigBuilder().setText(currentGame.getStageScoreAgainst() + "")
                .setFontConfig(new FontConfig(FontColor.RED.getColor(), fontSize)).build());
        table.add(forScore).width(ScreenDimensionsManager.getScreenWidthValue(10));
        table.add(between).width(ScreenDimensionsManager.getScreenWidthValue(10));
        table.add(againstScore).width(ScreenDimensionsManager.getScreenWidthValue(10));
        return table;
    }

    private String getCellName(int x, int y) {
        return CELL_NAME + x + "_" + y;
    }

    private void hideAllImageViews() {
        RunnableAction action2 = new RunnableAction();
        action2.setRunnable(new ScreenRunnable(getAbstractScreen()) {
            @Override
            public void executeOperations() {
                for (TableCell tableCell : cells) {
                    tableCell.getMatrixElement().setShowed(false);
                    refreshImageViews();
                    setEnableImageClick(true);
                }
            }
        });
        addAction(Actions.sequence(Actions.delay(2f), action2));
    }

    public void refreshImageViews(String... namesToBeRefreshed) {
        refreshImageViews(new Runnable() {
            @Override
            public void run() {
            }
        }, namesToBeRefreshed);
    }

    public void refreshImageViews(final Runnable afterRefresh, String... namesToBeRefreshed) {
        final float duration = 0.1f;
        int imageSideDimen = getImageSideDimen();
        for (final TableCell tableCell : cells) {
            if (namesToBeRefreshed.length == 0 || Arrays.asList(namesToBeRefreshed).contains(tableCell.getCell().getName())) {
                for (final Actor actor : tableCell.getCell().getChildren()) {
                    actor.addAction(Actions.sequence(Actions.fadeOut(duration), Utils.createRunnableAction(new Runnable() {
                        @Override
                        public void run() {
                            actor.remove();
                            final Image matrixElementImage = getMatrixElementImage(tableCell.getMatrixElement());
                            matrixElementImage.setWidth(imageSideDimen);
                            matrixElementImage.setHeight(imageSideDimen);
                            matrixElementImage.setVisible(false);
                            matrixElementImage.addAction(Actions.sequence(Actions.delay(duration), Actions.fadeOut(0f), Utils.createRunnableAction(new Runnable() {
                                @Override
                                public void run() {
                                    matrixElementImage.setVisible(true);
                                }
                            }), Actions.fadeIn(duration), Utils.createRunnableAction(afterRefresh)));
                            tableCell.getCell().clearChildren();
                            if (tableCell.getCell().getChildren().size == 0) {
                                tableCell.getCell().add(matrixElementImage).grow();
                            }
                        }
                    })));
                }
            }
        }
    }

    private void processScore(CurrentGame currentGame, MatrixChoice clickedItem) {
        int scoreForToIncrement = 0;
        int scoreAgainstToIncrement = 0;
        String itemValue = null;

        if (new GameLogic().doButtonsMatchAndProcess(clickedItem, currentGame.getFirstChoice(), levelMatrix)) {
            insertDiscoveredItem(currentGame, clickedItem);
//            scoreForToIncrement = difficultyUtil.getScoreForToIncrement();
            scoreForToIncrement = 1;
        } else {
//            scoreAgainstToIncrement = difficultyUtil.getScoreAgainstToIncrement();
            scoreAgainstToIncrement = 1;
        }
        currentGame.setStageScoreFor(currentGame.getStageScoreFor() + scoreForToIncrement);
        currentGame.setStageScoreAgainst(currentGame.getStageScoreAgainst() + scoreAgainstToIncrement);
        currentGame.setTotalScoreFor(currentGame.getTotalScoreFor() + scoreForToIncrement);
        currentGame.setTotalScoreAgainst(currentGame.getTotalScoreAgainst() + scoreAgainstToIncrement);
    }

    private void insertDiscoveredItem(CurrentGame currentGame, MatrixChoice clickedItem) {
        String itemId = "item" + clickedItem.getItem();
        if (!campaignStoreService.isQuestionAlreadyPlayed(itemId)) {
            campaignStoreService.putQuestionPlayed(itemId);
            new MyNotificationPopupCreator(new MyNotificationPopupConfigBuilder().setResource(MemorySpecificResource.valueOf(itemId)).setText(
                    MainGameLabel.l_item_discovered.getText(StringUtils.capitalize(SpecificPropertiesUtils.getText(itemId)))).build()).
                    shortNotificationPopup().addToPopupManager();

        }
    }

    private Image getMatrixElementImage(MatrixElement currentItem) {
        return GraphicUtils.getImage(MemorySpecificResource.valueOf(currentItem.isShowed() ? "item" + currentItem.getItem() : "unknown"));
    }

    private void setEnableImageClick(boolean enableImageClick) {
        this.enableImageClick = enableImageClick;
    }

    private boolean isLevelFailed(MatrixElement[][] matrix) {
        int totalItemPairs = (currentGame.getCurrentLevel().getCols() * currentGame.getCurrentLevel().getRows()) / 2;
        int totalFoundPairs = totalFoundItems(matrix) / 2;
        int scoreDiff = currentGame.getTotalScoreAgainst() - currentGame.getTotalScoreFor();
        int notFoundPairs = totalItemPairs - totalFoundPairs;
        return scoreDiff > notFoundPairs;
    }

    private boolean isLevelFinished(MatrixElement[][] matrix) {
        int totalItemPairs = (currentGame.getCurrentLevel().getCols() * currentGame.getCurrentLevel().getRows()) / 2;
        int totalFoundPairs = totalFoundItems(matrix) / 2;
        return totalFoundPairs == totalItemPairs;
    }

    private int totalFoundItems(MatrixElement[][] matrix) {
        int total = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j].isFound()) {
                    total++;
                }
            }
        }
        return total;
    }

    private void refreshTextViews() {
        forScore.setText(currentGame.getStageScoreFor() + "");
        againstScore.setText(currentGame.getStageScoreAgainst() + "");
    }

    @Override
    public void onBackKeyPress() {
        screenManager.showMainScreen();
    }

}
