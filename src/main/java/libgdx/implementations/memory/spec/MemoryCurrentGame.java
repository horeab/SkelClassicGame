package libgdx.implementations.memory.spec;


import libgdx.implementations.memory.screens.MemoryGameScreen;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MemoryCurrentGame implements Serializable {

    private MatrixChoice firstChoice;

    private List<Item> allItems = new ArrayList<Item>();
    private List<Level> allLevels = new ArrayList<Level>();

    private Level currentLevel;

    private int totalScoreFor;
    private int totalScoreAgainst;

    private int stageScoreFor;
    private int stageScoreAgainst;

    private int selectedDifficulty;

    public MemoryCurrentGame(MemoryGameScreen memoryGameScreen, int levelNr, int selectedDifficulty) {
        ItemsUtil itemsUtil = new ItemsUtil(memoryGameScreen);
        this.allItems = itemsUtil.getItemsFromResources();
        this.allLevels = itemsUtil.getLevelsFromResources(allItems);

        this.setCurrentLevel(allLevels.get(levelNr));

        this.firstChoice = null;

        this.selectedDifficulty = selectedDifficulty;
    }

    public int getTotalScoreFor() {
        return totalScoreFor;
    }

    public void setTotalScoreFor(int totalScoreFor) {
        this.totalScoreFor = totalScoreFor;
    }

    public int getTotalScoreAgainst() {
        return totalScoreAgainst;
    }

    public void setTotalScoreAgainst(int totalScoreAgainst) {
        this.totalScoreAgainst = totalScoreAgainst;
    }

    public int getStageScoreFor() {
        return stageScoreFor;
    }

    public void setStageScoreFor(int stageScoreFor) {
        this.stageScoreFor = stageScoreFor;
    }

    public int getStageScoreAgainst() {
        return stageScoreAgainst;
    }

    public void setStageScoreAgainst(int stageScoreAgainst) {
        this.stageScoreAgainst = stageScoreAgainst;
    }

    public MatrixChoice getFirstChoice() {
        return firstChoice;
    }

    public void setFirstChoice(MatrixChoice firstChoice) {
        this.firstChoice = firstChoice;
    }

    public List<Item> getAllItems() {
        return allItems;
    }

    public void setAllItems(List<Item> allItems) {
        this.allItems = allItems;
    }

    public Level getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(Level currentLevel) {
        this.currentLevel = currentLevel;
    }

    public List<Level> getAllLevels() {
        return allLevels;
    }

    public void setAllLevels(List<Level> allLevels) {
        this.allLevels = allLevels;
    }

    public int getSelectedDifficulty() {
        return selectedDifficulty;
    }

    public void setSelectedDifficulty(int selectedDifficulty) {
        this.selectedDifficulty = selectedDifficulty;
    }
}
