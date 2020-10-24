package libgdx.implementations.kidlearn.spec.eng;

import libgdx.implementations.kidlearn.KidLearnQuestionDifficultyLevel;
import libgdx.implementations.kidlearn.spec.KidLearnGameLabel;
import libgdx.implementations.kidlearn.spec.KidLearnLevel;
import libgdx.resources.gamelabel.GameLabel;

public enum KidLearnEngHangmanLevel implements KidLearnLevel, KidLearnEngLevel {

    L0(KidLearnGameLabel.l_eng_vegetables, KidLearnQuestionDifficultyLevel._0, KidLearnGameLabel.l_eng_hangman),
    L1(KidLearnGameLabel.l_eng_colors, KidLearnQuestionDifficultyLevel._1, KidLearnGameLabel.l_eng_hangman),
    L2(KidLearnGameLabel.l_eng_kitchen, KidLearnQuestionDifficultyLevel._2, KidLearnGameLabel.l_eng_hangman),
    ;

    public GameLabel category;
    public KidLearnQuestionDifficultyLevel difficulty;
    public GameLabel title;

    KidLearnEngHangmanLevel(GameLabel category, KidLearnQuestionDifficultyLevel difficulty, GameLabel title) {
        this.category = category;
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
