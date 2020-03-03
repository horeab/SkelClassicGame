package libgdx.implementations.memory.spec;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import libgdx.game.Game;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.memory.MemorySpecificResource;
import libgdx.implementations.memory.screens.MemoryGameScreen;
import libgdx.resources.Res;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ItemsUtil {

    public static final int BUTTON_ID_STARTING_INT_VALUE = 0;
    public final static int ROW_ID_STARTING_INT_VALUE = 200;

    private GameLogic gameUtil;
    private DifficultyUtil difficultyUtil;
    private MemoryGameScreen context;

    public ItemsUtil(MemoryGameScreen context) {
        this.context = context;
        this.gameUtil = new GameLogic();
        this.difficultyUtil = new DifficultyUtil();
    }

    public void addImagesToRows(int rows, int col) {
        int position = BUTTON_ID_STARTING_INT_VALUE;
        for (Table row : getCreatedRows(rows)) {
            for (int nr = 0; nr < col; nr++) {
                Table image = createImage(position);
                row.add(image);
                position++;
            }
        }
    }

    private Table createImage(int position) {
        Table table = new Table();
        Image image = new Image();
        image.setName("" + position);
        table.add(image);
        return table;

    }

    private List<Table> getCreatedRows(int nrRows) {
        List<Table> rows = new ArrayList<Table>();
        for (int i = 0; i < nrRows; i++) {
            Table row = getRoot().findActor("" + ROW_ID_STARTING_INT_VALUE + i);
            rows.add(row);
        }
        return rows;
    }

    private Group getRoot() {
        return Game.getInstance().getAbstractScreen().getRoot();
    }

    public void createRows(int rows) {
        Table table = new Table();
        for (int i = 0; i < rows; i++) {
            Table row = new Table();
            row.setName("" + ROW_ID_STARTING_INT_VALUE + i);
            table.add(row);
        }
    }

    public void onClickImageViews(final CurrentGame currentGame) {
        int position = 0;
        int i = 0;
        final int rows = currentGame.getCurrentLevel().getRows();
        final int col = currentGame.getCurrentLevel().getCols();
        for (int row = 0; row < rows; row++) {
            for (int nr = 0; nr < col; nr++) {
                Image image = getRoot().findActor("" + BUTTON_ID_STARTING_INT_VALUE + position);
                final int finalI = i;
                final int finalNr = nr;
                image.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        final MatrixElement[][] items = currentGame.getLevelMatrix();
                        MatrixElement currentItem = items[finalI][finalNr];
                        MatrixChoice clickedItem = new MatrixChoice(finalNr, finalI, currentItem.getItem());
                        if (context.isEnableImageClick() && !clickedItem.equals(currentGame.getFirstChoice()) && !currentItem.isFound()
                                && !currentItem.isShowed()) {
                            currentItem.setShowed(true);
                            refreshImageViews(rows, col, items, false);

                            if (currentGame.getFirstChoice() != null) {
                                processScore(currentGame, clickedItem);
                                if (isLevelFinished(items)) {
                                    processLevelFinishedPopup(currentGame.getTotalScoreFor(), currentGame.getTotalScoreAgainst(),
                                            currentGame.getStageScoreFor(), currentGame.getStageScoreAgainst());
                                }
                            }

                            MatrixChoice firstItemClicked = currentGame.getFirstChoice() == null ? clickedItem : null;
                            currentGame.setFirstChoice(firstItemClicked);

                            secondChoiceMadeProcesses(currentGame);
                        }
                    }

                    private void processLevelFinishedPopup(int totalScoreFor, int totalScoreAgainst, int stageScoreFor, int stageScoreAgainst) {
                        boolean levelSucces = isLevelSucces(stageScoreFor, stageScoreAgainst, currentGame.getCurrentLevel().getLevelNr());
                        int stageScore = getScore(stageScoreFor, stageScoreAgainst);
                        int totalScore = getScore(totalScoreFor, totalScoreAgainst);
//                        Dialog levelFinishedPopup = context.levelFinishedPopup(levelSucces, !levelSucces, stageScore, totalScore);
//                        levelFinishedPopup.show();
                    }
                });
                position++;
            }
            i++;
        }
    }

    private void processScore(CurrentGame currentGame, MatrixChoice clickedItem) {
        int scoreForToIncrement = 0;
        int scoreAgainstToIncrement = 0;
        String itemValue = null;

        if (gameUtil.doButtonsMatchAndProcess(clickedItem, currentGame.getFirstChoice(), currentGame.getLevelMatrix())) {
            insertDiscoveredItem(currentGame, clickedItem);
//            scoreForToIncrement = difficultyUtil.getScoreForToIncrement();
            scoreForToIncrement = 1;
            int rows = currentGame.getCurrentLevel().getRows();
            int col = currentGame.getCurrentLevel().getCols();
            refreshImageViews(rows, col, currentGame.getLevelMatrix(), false);
            itemValue = getRandomItemValue(clickedItem.getItem(), currentGame.getAllItems());
            itemValue = getRandomCommentaryValue(itemValue);
        } else {
//            scoreAgainstToIncrement = difficultyUtil.getScoreAgainstToIncrement();
            scoreAgainstToIncrement = 1;
        }
        currentGame.setStageScoreFor(currentGame.getStageScoreFor() + scoreForToIncrement);
        currentGame.setStageScoreAgainst(currentGame.getStageScoreAgainst() + scoreAgainstToIncrement);
        currentGame.setTotalScoreFor(currentGame.getTotalScoreFor() + scoreForToIncrement);
        currentGame.setTotalScoreAgainst(currentGame.getTotalScoreAgainst() + scoreAgainstToIncrement);

        context.refreshTextViews(itemValue);
    }

    private String getRandomCommentaryValue(String itemValue) {
//        String[] comms = context.getResources().getStringArray(R.array.commentary_values);
//        String commentary = comms[new Random().nextInt(comms.length)];
//        commentary = Utils.fontString(itemValue, false, "#003399") + " " + Utils.fontString(commentary, false, null);
        return "comm";
    }

    private void insertDiscoveredItem(CurrentGame currentGame, MatrixChoice clickedItem) {
        List<String> discoveredItems = context.getDiscoveredItems();
        Item item = getItemForIndex(clickedItem.getItem(), currentGame.getAllItems());
        if (!discoveredItems.contains(item.getItemName())) {
            context.getDiscoveredItems().add(item.getItemName());
        }
    }

    private void secondChoiceMadeProcesses(final CurrentGame currentGame) {
        if (currentGame.getFirstChoice() == null) {
//            Handler handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    int rows = currentGame.getCurrentLevel().getRows();
//                    int col = currentGame.getCurrentLevel().getCols();
//                    refreshImageViews(rows, col, currentGame.getLevelMatrix(), false);
//                }
//            }, 500);
        }
    }

    @SuppressWarnings("deprecation")
    public void refreshImageViews(int rows, int col, MatrixElement[][] items, boolean ignoreBackGround) {
        int i = 0;
        int position = 0;
        for (int row = 0; row < rows; row++) {
            for (int nr = 0; nr < col; nr++) {
                MatrixElement currentItem = items[i][nr];
                Image image = getRoot().findActor("" + BUTTON_ID_STARTING_INT_VALUE + position);
                Res imageIdentifier = MemorySpecificResource.valueOf(currentItem.isShowed() ? "items" + currentItem.getItem() : "unknown");
                image.setDrawable(GraphicUtils.getImage(imageIdentifier).getDrawable());
                position++;
            }
            i++;
        }
    }

    public List<Level> getLevelsFromResources(List<Item> allItems) {
        List<Level> levels = new ArrayList<Level>();
        for (GameLevel gameLevel : GameLevel.values()) {
            List<Item> levelItems = getItemsForRange(0, 4, allItems);
//            List<Item> levelItems = getItemsForRange(0, Integer.parseInt(levelValues[3]), allItems);
            Level level = new Level(gameLevel.ordinal(), gameLevel.getRows(), gameLevel.getCols(), levelItems);
            levels.add(level);
        }
        return levels;
    }

    public static List<Item> getItemsFromResources() {
        List<Item> items = new ArrayList<Item>();
//        String[] itemNames = context.getResources().getStringArray(R.array.item_names);
        for (int i = 0; i < 4; i++) {
            Item item = new Item();
            item.setItemIndex(i);
//            items.setItemName(itemNames[i]);
            String itemValuesArrayId = "item_" + i + "_values";
//            int resID = context.getResources().getIdentifier(itemValuesArrayId, "array", context.getPackageName());
//            String[] itemValues = context.getResources().getStringArray(resID);
//            for (int j = 0; j < itemValues.length; j++) {
//                items.getItemValues().add(itemValues[j]);
//            }
            items.add(item);
        }
        return items;
    }

    private List<Item> getItemsForRange(int start, int end, List<Item> allItems) {
        List<Item> items = new ArrayList<Item>();
        for (int i = 0; i < end; i++) {
            items.add(allItems.get(i));
        }
        return items;
    }

    private String getRandomItemValue(int itemIndex, List<Item> items) {
        Item item = getItemForIndex(itemIndex, items);
        int randomNr = new Random().nextInt(item.getItemValues().size());
        return item.getItemValues().get(randomNr);
    }

    private Item getItemForIndex(int index, List<Item> items) {
        for (Item item : items) {
            if (item.getItemIndex() == index) {
                return item;
            }
        }
        return null;
    }

    private boolean isLevelSucces(int scoreFor, int scoreAgainst, int levelNr) {
        if (scoreFor >= scoreAgainst) {
            return true;
        }
        return false;
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

    public int getScore(int scoreFor, int scoreAgainst) {
        return scoreFor - scoreAgainst;
    }

}
