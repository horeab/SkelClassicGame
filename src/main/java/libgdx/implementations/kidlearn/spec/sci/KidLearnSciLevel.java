package libgdx.implementations.kidlearn.spec.sci;

import libgdx.implementations.kidlearn.KidLearnQuestionDifficultyLevel;

public enum KidLearnSciLevel {

    L0("Feed the animal", 5, KidLearnQuestionDifficultyLevel._0),//ce mananca fiecare animal
    L1("Recycle waste", 5, KidLearnQuestionDifficultyLevel._0),// cosuri gunoi si clasificarea gunoaielor
    L2("Food chain", 5, KidLearnQuestionDifficultyLevel._0),//
    L3("States of water", 5, KidLearnQuestionDifficultyLevel._0), // clasificarea imaginilor (cub gheata, ceai, abur) solid/lichid/aer
    ;

    public String category;
    public int nrOfCorrectUnknownWords;
    public KidLearnQuestionDifficultyLevel difficulty;

    KidLearnSciLevel(String category, int nrOfCorrectUnknownWords, KidLearnQuestionDifficultyLevel difficulty) {
        this.category = category;
        this.nrOfCorrectUnknownWords = nrOfCorrectUnknownWords;
        this.difficulty = difficulty;
    }
}
