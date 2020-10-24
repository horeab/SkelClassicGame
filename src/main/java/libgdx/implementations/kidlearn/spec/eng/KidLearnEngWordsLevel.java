package libgdx.implementations.kidlearn.spec.eng;

import libgdx.implementations.kidlearn.KidLearnQuestionDifficultyLevel;
import libgdx.implementations.kidlearn.spec.KidLearnGameLabel;
import libgdx.implementations.kidlearn.spec.KidLearnLevel;
import libgdx.resources.gamelabel.GameLabel;

public enum KidLearnEngWordsLevel implements KidLearnLevel, KidLearnEngLevel {

    L0(KidLearnGameLabel.l_eng_animals,  5, KidLearnQuestionDifficultyLevel._0, KidLearnGameLabel.l_eng_words),
    L1(KidLearnGameLabel.l_eng_fruits,  5, KidLearnQuestionDifficultyLevel._0, KidLearnGameLabel.l_eng_words),
    L2(KidLearnGameLabel.l_eng_numbers,  5, KidLearnQuestionDifficultyLevel._1, KidLearnGameLabel.l_eng_words),
    L3(KidLearnGameLabel.l_eng_shapes,  5, KidLearnQuestionDifficultyLevel._2, KidLearnGameLabel.l_eng_words),
    ;

    public GameLabel category;
    public int totalUnknownItems;
    public KidLearnQuestionDifficultyLevel difficulty;
    public GameLabel title;

    KidLearnEngWordsLevel(GameLabel category, int totalUnknownItems, KidLearnQuestionDifficultyLevel difficulty, GameLabel title) {
        this.category = category;
        this.totalUnknownItems = totalUnknownItems;
        this.difficulty = difficulty;
        this.title = title;
    }

    @Override
    public KidLearnQuestionDifficultyLevel difficulty() {
        return difficulty;
    }

    @Override
    public String title() {
        return title.getText();
    }

    @Override
    public GameLabel category() {
        return category;
    }
}
