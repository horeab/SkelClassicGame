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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MemoryGameScreen extends AbstractScreen<MemoryScreenManager> {

    private final String CELL_NAME = "CELL_NAME";
    private MatrixElement[][] levelMatrix;
    private MemoryCurrentGame memoryCurrentGame;
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
    public void setBackgroundColor(RGBColor backgroundColor) {
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

    private void addAllTable(final int levelNr) {
        this.levelNr = levelNr;
        this.gameLevel = GameLevel.values()[levelNr];
        this.memoryCurrentGame = new MemoryCurrentGame(this, this.gameLevel.ordinal(), 0);
        levelMatrix = new GameLogic().generateMatrix(gameLevel);
        hideAllImageViews();
        final Table table = new Table();

        final int rows = memoryCurrentGame.getCurrentLevel().getRows();
        final int columns = memoryCurrentGame.getCurrentLevel().getCols();

        table.setFillParent(true);
        Table headerTable = createHeaderTable();
        float headerHeight = getHeaderHeight();
        table.add(headerTable).width(ScreenDimensionsManager.getScreenWidth()).height(headerHeight).row();
        final Table gameTable = new Table();
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
                        if (enableImageClick && !clickedItem.equals(memoryCurrentGame.getFirstChoice()) && !currentItem.isFound() && !currentItem.isShowed()) {
                            currentItem.setShowed(true);
                            refreshImageViews(new Runnable() {
                                @Override
                                public void run() {
                                    if (memoryCurrentGame.getFirstChoice() != null) {
                                        processScore(memoryCurrentGame, clickedItem);
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
                                                    final int nextLevel = levelNr + 1;
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

                                    final String firstChoiceName = memoryCurrentGame.getFirstChoice() == null ? "" : getCellName(memoryCurrentGame.getFirstChoice().getX(), memoryCurrentGame.getFirstChoice().getY());
                                    MatrixChoice firstItemClicked = memoryCurrentGame.getFirstChoice() == null ? clickedItem : null;
                                    memoryCurrentGame.setFirstChoice(firstItemClicked);

                                    if (memoryCurrentGame.getFirstChoice() == null) {
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
        table.add(gameTable).width(ScreenDimensionsManager.getScreenWidth(95));
        addActor(table);
        backButton.toFront();
        refreshButton.toFront();
    }

    private float getHeaderHeight() {
        return ScreenDimensionsManager.getScreenHeight(10);
    }

    private int getImageSideDimen() {
        final int rows = memoryCurrentGame.getCurrentLevel().getRows();
        int side = calcHorizSide();
        if (isVerticalGreater()) {
            side = (int) ((ScreenDimensionsManager.getScreenHeight() / rows) - (getImagePadding(false) * rows));
        }
        return side;
    }

    private int calcHorizSide() {
        double columns = memoryCurrentGame.getCurrentLevel().getCols();
        return (int) ((ScreenDimensionsManager.getScreenWidth() / columns) - (getImagePadding(true) * (columns / 2)));
    }

    private boolean isVerticalGreater() {
        int side = calcHorizSide();
        final int rows = memoryCurrentGame.getCurrentLevel().getRows();
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
        forScore = new MyWrappedLabel(new MyWrappedLabelConfigBuilder().setText(memoryCurrentGame.getStageScoreFor() + "")
                .setFontConfig(new FontConfig(FontColor.GREEN.getColor(), fontSize)).build());
        MyWrappedLabel between = new MyWrappedLabel(new MyWrappedLabelConfigBuilder().setText(" - ")
                .setFontConfig(new FontConfig(fontSize)).build());
        againstScore = new MyWrappedLabel(new MyWrappedLabelConfigBuilder().setText(memoryCurrentGame.getStageScoreAgainst() + "")
                .setFontConfig(new FontConfig(FontColor.RED.getColor(), fontSize)).build());
        table.add(forScore).width(ScreenDimensionsManager.getScreenWidth(10));
        table.add(between).width(ScreenDimensionsManager.getScreenWidth(10));
        table.add(againstScore).width(ScreenDimensionsManager.getScreenWidth(10));
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
        final int imageSideDimen = getImageSideDimen();
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

    private void processScore(MemoryCurrentGame memoryCurrentGame, MatrixChoice clickedItem) {
        int scoreForToIncrement = 0;
        int scoreAgainstToIncrement = 0;
        String itemValue = null;

        if (new GameLogic().doButtonsMatchAndProcess(clickedItem, memoryCurrentGame.getFirstChoice(), levelMatrix)) {
            insertDiscoveredItem(memoryCurrentGame, clickedItem);
//            scoreForToIncrement = difficultyUtil.getScoreForToIncrement();
            scoreForToIncrement = 1;
        } else {
//            scoreAgainstToIncrement = difficultyUtil.getScoreAgainstToIncrement();
            scoreAgainstToIncrement = 1;
        }
        memoryCurrentGame.setStageScoreFor(memoryCurrentGame.getStageScoreFor() + scoreForToIncrement);
        memoryCurrentGame.setStageScoreAgainst(memoryCurrentGame.getStageScoreAgainst() + scoreAgainstToIncrement);
        memoryCurrentGame.setTotalScoreFor(memoryCurrentGame.getTotalScoreFor() + scoreForToIncrement);
        memoryCurrentGame.setTotalScoreAgainst(memoryCurrentGame.getTotalScoreAgainst() + scoreAgainstToIncrement);
    }

    private void insertDiscoveredItem(MemoryCurrentGame memoryCurrentGame, MatrixChoice clickedItem) {
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
        int totalItemPairs = (memoryCurrentGame.getCurrentLevel().getCols() * memoryCurrentGame.getCurrentLevel().getRows()) / 2;
        int totalFoundPairs = totalFoundItems(matrix) / 2;
        int scoreDiff = memoryCurrentGame.getTotalScoreAgainst() - memoryCurrentGame.getTotalScoreFor();
        int notFoundPairs = totalItemPairs - totalFoundPairs;
        return scoreDiff > notFoundPairs;
    }

    private boolean isLevelFinished(MatrixElement[][] matrix) {
        int totalItemPairs = (memoryCurrentGame.getCurrentLevel().getCols() * memoryCurrentGame.getCurrentLevel().getRows()) / 2;
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
        forScore.setText(memoryCurrentGame.getStageScoreFor() + "");
        againstScore.setText(memoryCurrentGame.getStageScoreAgainst() + "");
    }

    @Override
    public void onBackKeyPress() {
        screenManager.showMainScreen();
    }

}
