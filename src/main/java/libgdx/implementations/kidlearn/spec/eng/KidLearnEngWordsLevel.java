package libgdx.implementations.kidlearn.spec.eng;

import libgdx.implementations.kidlearn.KidLearnQuestionDifficultyLevel;

public enum KidLearnEngWordsLevel {

    L0("Animals", 5, KidLearnQuestionDifficultyLevel._0),
    L1("Numbers", 5, KidLearnQuestionDifficultyLevel._0),
    L2("Fruits", 5, KidLearnQuestionDifficultyLevel._0),
    L3("Colors", 5, KidLearnQuestionDifficultyLevel._0),
    L4("Animals", 5, KidLearnQuestionDifficultyLevel._1),
    L5("Numbers", 5, KidLearnQuestionDifficultyLevel._1),
    L6("Fruits", 5, KidLearnQuestionDifficultyLevel._1),
    L7("Colors", 5, KidLearnQuestionDifficultyLevel._1),
    L8("Animals", 5, KidLearnQuestionDifficultyLevel._2),
    L9("Numbers", 5, KidLearnQuestionDifficultyLevel._2),
    L10("Fruits", 5, KidLearnQuestionDifficultyLevel._2),
    L11("Colors", 5, KidLearnQuestionDifficultyLevel._2),
    ;

    public String category;
    public int nrOfCorrectUnknownWords;
    public KidLearnQuestionDifficultyLevel difficulty;

    KidLearnEngWordsLevel(String category, int nrOfCorrectUnknownWords, KidLearnQuestionDifficultyLevel difficulty) {
        this.category = category;
        this.nrOfCorrectUnknownWords = nrOfCorrectUnknownWords;
        this.difficulty = difficulty;
    }
}
