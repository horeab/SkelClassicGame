package libgdx.implementations.kidlearn.spec.eng;

import libgdx.implementations.kidlearn.KidLearnQuestionDifficultyLevel;
import libgdx.implementations.kidlearn.spec.KidLearnGameLabel;
import libgdx.implementations.kidlearn.spec.KidLearnLevel;
import libgdx.resources.gamelabel.GameLabel;

public enum KidLearnEngWordsLevel implements KidLearnLevel {

    L0("Animals", 4, KidLearnQuestionDifficultyLevel._0, KidLearnGameLabel.l_eng_words),
    L1("Numbers", 4, KidLearnQuestionDifficultyLevel._0, KidLearnGameLabel.l_eng_words),
    L2("Animals", 4, KidLearnQuestionDifficultyLevel._1, KidLearnGameLabel.l_eng_words),
    L3("Numbers", 4, KidLearnQuestionDifficultyLevel._1, KidLearnGameLabel.l_eng_words),
    L4("Animals", 4, KidLearnQuestionDifficultyLevel._2, KidLearnGameLabel.l_eng_words),
    L5("Numbers", 4, KidLearnQuestionDifficultyLevel._2, KidLearnGameLabel.l_eng_words),
    ;

    public String category;
    public int totalUnknownItems;
    public KidLearnQuestionDifficultyLevel difficulty;
    public GameLabel title;

    KidLearnEngWordsLevel(String category, int totalUnknownItems, KidLearnQuestionDifficultyLevel difficulty, GameLabel title) {
        this.category = category;
        this.totalUnknownItems = totalUnknownItems;
        this.difficulty = difficulty;
        this.title = title;
    }

    @Override
    public String title() {
        return title.getText();
    }
}
