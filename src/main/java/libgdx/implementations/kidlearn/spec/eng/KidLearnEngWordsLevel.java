package libgdx.implementations.kidlearn.spec.eng;

import libgdx.implementations.kidlearn.KidLearnQuestionDifficultyLevel;
import libgdx.implementations.kidlearn.spec.KidLearnGameLabel;
import libgdx.implementations.kidlearn.spec.KidLearnLevel;
import libgdx.resources.gamelabel.GameLabel;
import libgdx.resources.gamelabel.MainGameLabel;

public enum KidLearnEngWordsLevel implements KidLearnLevel {

    L0("Animals", 5, KidLearnQuestionDifficultyLevel._0, KidLearnGameLabel.l_eng_findword),
    L1("Numbers", 5, KidLearnQuestionDifficultyLevel._0, KidLearnGameLabel.l_eng_findword),
    L2("Fruits", 5, KidLearnQuestionDifficultyLevel._0, KidLearnGameLabel.l_eng_findword),
    L3("Colors", 5, KidLearnQuestionDifficultyLevel._0, KidLearnGameLabel.l_eng_findword),
    L4("Animals", 5, KidLearnQuestionDifficultyLevel._1, KidLearnGameLabel.l_eng_findword),
    L5("Numbers", 5, KidLearnQuestionDifficultyLevel._1, KidLearnGameLabel.l_eng_findword),
    L6("Fruits", 5, KidLearnQuestionDifficultyLevel._1, KidLearnGameLabel.l_eng_findword),
    L7("Colors", 5, KidLearnQuestionDifficultyLevel._1, KidLearnGameLabel.l_eng_findword),
    L8("Animals", 5, KidLearnQuestionDifficultyLevel._2, KidLearnGameLabel.l_eng_findword),
    L9("Numbers", 5, KidLearnQuestionDifficultyLevel._2, KidLearnGameLabel.l_eng_findword),
    L10("Fruits", 5, KidLearnQuestionDifficultyLevel._2, KidLearnGameLabel.l_eng_findword),
    L11("Colors", 5, KidLearnQuestionDifficultyLevel._2, KidLearnGameLabel.l_eng_findword),
    ;

    public String category;
    public int nrOfCorrectUnknownWords;
    public KidLearnQuestionDifficultyLevel difficulty;
    public GameLabel title;

    KidLearnEngWordsLevel(String category, int nrOfCorrectUnknownWords, KidLearnQuestionDifficultyLevel difficulty, GameLabel title) {
        this.category = category;
        this.nrOfCorrectUnknownWords = nrOfCorrectUnknownWords;
        this.difficulty = difficulty;
        this.title = title;
    }

    @Override
    public String title() {
        return title.getText();
    }
}
